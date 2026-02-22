#!/bin/bash

set -e

REGION="${AWS_REGION:-eu-west-1}"
PROJECT_NAME="${PROJECT_NAME:-online-cv}"
S3_BUCKET_NAME="${S3_BUCKET_NAME:-$PROJECT_NAME-frontend-$(date +%s)}"
LAMBDA_FUNCTION_NAME="${LAMBDA_FUNCTION_NAME:-$PROJECT_NAME-backend}"
API_GATEWAY_NAME="${API_GATEWAY_NAME:-$PROJECT_NAME-api}"

echo "=== AWS Infrastructure Setup for $PROJECT_NAME ==="
echo "Region: $REGION"
echo "S3 Bucket: $S3_BUCKET_NAME"
echo "Lambda Function: $LAMBDA_FUNCTION_NAME"
echo "API Gateway: $API_GATEWAY_NAME"
echo ""

echo "Step 1: Creating S3 bucket for frontend..."
aws s3api create-bucket \
    --bucket "$S3_BUCKET_NAME" \
    --region "$REGION" \
    --create-bucket-configuration LocationConstraint="$REGION" \
    2>/dev/null || echo "Bucket may already exist"

echo "S3 bucket created: $S3_BUCKET_NAME"
echo ""

echo "Step 2: Creating IAM role for Lambda..."
LAMBDA_ROLE_NAME="$PROJECT_NAME-lambda-role"

aws iam create-role \
    --role-name "$LAMBDA_ROLE_NAME" \
    --assume-role-policy-document '{
        "Version": "2012-10-17",
        "Statement": [
            {
                "Effect": "Allow",
                "Principal": {
                    "Service": "lambda.amazonaws.com"
                },
                "Action": "sts:AssumeRole"
            }
        ]
    }' \
    2>/dev/null || echo "Role may already exist"

aws iam attach-role-policy \
    --role-name "$LAMBDA_ROLE_NAME" \
    --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole \
    2>/dev/null || echo "Policy may already be attached"

LAMBDA_ROLE_ARN="arn:aws:iam::$(aws sts get-caller-identity --query Account --output text):role/$LAMBDA_ROLE_NAME"
echo "Lambda role created: $LAMBDA_ROLE_ARN"
echo ""

echo "Step 3: Creating Lambda function (placeholder)..."
mkdir -p /tmp/lambda-placeholder
cat > /tmp/lambda-placeholder/bootstrap << 'EOF'
#!/bin/bash
echo "Lambda function placeholder - deploy with GitHub Actions"
EOF
chmod +x /tmp/lambda-placeholder/bootstrap
cd /tmp/lambda-placeholder && zip -j /tmp/lambda-placeholder.zip bootstrap

echo "Waiting for IAM role propagation..."
sleep 15

echo "Creating Lambda function..."
if ! aws lambda get-function --function-name "$LAMBDA_FUNCTION_NAME" --region "$REGION" > /dev/null 2>&1; then
    aws lambda create-function \
        --function-name "$LAMBDA_FUNCTION_NAME" \
        --runtime provided.al2023 \
        --role "$LAMBDA_ROLE_ARN" \
        --handler not.used \
        --zip-file fileb:///tmp/lambda-placeholder.zip \
        --environment "Variables={DATABASE_URL=placeholder,DATABASE_USERNAME=placeholder,DATABASE_PASSWORD=placeholder}" \
        --timeout 30 \
        --memory-size 512 \
        --region "$REGION"
    echo "Lambda function created"
else
    echo "Lambda function already exists"
fi

LAMBDA_ARN=$(aws lambda get-function --function-name "$LAMBDA_FUNCTION_NAME" --query Configuration.FunctionArn --output text --region "$REGION")
echo "Lambda function ARN: $LAMBDA_ARN"
echo ""

echo "Step 4: Creating API Gateway..."
API_GATEWAY_ID=$(aws apigateway create-rest-api \
    --name "$API_GATEWAY_NAME" \
    --query id \
    --output text \
    --region "$REGION" \
    2>/dev/null || aws apigateway get-rest-apis --query "items[?name=='$API_GATEWAY_NAME'].id" --output text --region "$REGION")

ROOT_RESOURCE_ID=$(aws apigateway get-resources \
    --rest-api-id "$API_GATEWAY_ID" \
    --query "items[?path=='/'].id" \
    --output text \
    --region "$REGION")

# Configure root resource (/) with ANY method + Lambda proxy
aws apigateway put-method \
    --rest-api-id "$API_GATEWAY_ID" \
    --resource-id "$ROOT_RESOURCE_ID" \
    --http-method ANY \
    --authorization-type NONE \
    --region "$REGION" \
    2>/dev/null || echo "Root method may already exist"

aws apigateway put-integration \
    --rest-api-id "$API_GATEWAY_ID" \
    --resource-id "$ROOT_RESOURCE_ID" \
    --http-method ANY \
    --type AWS_PROXY \
    --integration-http-method POST \
    --uri "arn:aws:apigateway:$REGION:lambda:path/2015-03-31/functions/$LAMBDA_ARN/invocations" \
    --region "$REGION" \
    2>/dev/null || echo "Root integration may already exist"

# Configure {proxy+} resource for all sub-paths (/v1/curriculum-vitae/*, /auth/*, etc.)
PROXY_RESOURCE_ID=$(aws apigateway create-resource \
    --rest-api-id "$API_GATEWAY_ID" \
    --parent-id "$ROOT_RESOURCE_ID" \
    --path-part "{proxy+}" \
    --query id \
    --output text \
    --region "$REGION" \
    2>/dev/null || aws apigateway get-resources --rest-api-id "$API_GATEWAY_ID" --query "items[?path=='/{proxy+}'].id" --output text --region "$REGION")

aws apigateway put-method \
    --rest-api-id "$API_GATEWAY_ID" \
    --resource-id "$PROXY_RESOURCE_ID" \
    --http-method ANY \
    --authorization-type NONE \
    --request-parameters "method.request.path.proxy=true" \
    --region "$REGION" \
    2>/dev/null || echo "Proxy method may already exist"

aws apigateway put-integration \
    --rest-api-id "$API_GATEWAY_ID" \
    --resource-id "$PROXY_RESOURCE_ID" \
    --http-method ANY \
    --type AWS_PROXY \
    --integration-http-method POST \
    --uri "arn:aws:apigateway:$REGION:lambda:path/2015-03-31/functions/$LAMBDA_ARN/invocations" \
    --region "$REGION" \
    2>/dev/null || echo "Proxy integration may already exist"

aws apigateway create-deployment \
    --rest-api-id "$API_GATEWAY_ID" \
    --stage-name prod \
    --region "$REGION" \
    2>/dev/null || echo "Deployment may already exist"

aws lambda add-permission \
    --function-name "$LAMBDA_FUNCTION_NAME" \
    --statement-id apigateway-invoke \
    --action lambda:InvokeFunction \
    --principal apigateway.amazonaws.com \
    --source-arn "arn:aws:execute-api:$REGION:$(aws sts get-caller-identity --query Account --output text):$API_GATEWAY_ID/*" \
    --region "$REGION" \
    2>/dev/null || echo "Permission may already exist"

API_URL="https://$API_GATEWAY_ID.execute-api.$REGION.amazonaws.com/prod"
echo "API Gateway created: $API_URL"
echo ""

echo "Step 5: Creating CloudFront distribution..."

# Create Origin Access Identity with proper config format
ORIGIN_ACCESS_IDENTITY=$(aws cloudfront create-cloud-front-origin-access-identity \
    --cloud-front-origin-access-identity-config "{\"CallerReference\": \"$PROJECT_NAME-oai\", \"Comment\": \"$PROJECT_NAME OAI\"}" \
    --query CloudFrontOriginAccessIdentity.Id \
    --output text \
    2>/dev/null || aws cloudfront list-cloud-front-origin-access-identities --query "CloudFrontOriginAccessIdentityList.Items[?Comment=='$PROJECT_NAME OAI'].Id" --output text)

echo "OAI ID: $ORIGIN_ACCESS_IDENTITY"

# Get the S3 canonical user ID for the OAI (more reliable than ARN-based principal)
OAI_CANONICAL_USER=$(aws cloudfront get-cloud-front-origin-access-identity \
    --id "$ORIGIN_ACCESS_IDENTITY" \
    --query CloudFrontOriginAccessIdentity.S3CanonicalUserId \
    --output text)

# Set S3 bucket policy for OAI access using CanonicalUser principal
aws s3api put-bucket-policy \
    --bucket "$S3_BUCKET_NAME" \
    --policy "{
        \"Version\": \"2012-10-17\",
        \"Statement\": [
            {
                \"Sid\": \"CloudFrontAccess\",
                \"Effect\": \"Allow\",
                \"Principal\": {
                    \"CanonicalUser\": \"$OAI_CANONICAL_USER\"
                },
                \"Action\": \"s3:GetObject\",
                \"Resource\": \"arn:aws:s3:::$S3_BUCKET_NAME/*\"
            }
        ]
    }"

CALLER_REF="$PROJECT_NAME-$(date +%s)"
DISTRIBUTION_CONFIG=$(cat <<DISTEOF
{
    "CallerReference": "$CALLER_REF",
    "Comment": "$PROJECT_NAME frontend distribution",
    "DefaultRootObject": "index.html",
    "Origins": {
        "Quantity": 1,
        "Items": [
            {
                "Id": "S3-$S3_BUCKET_NAME",
                "DomainName": "$S3_BUCKET_NAME.s3.$REGION.amazonaws.com",
                "S3OriginConfig": {
                    "OriginAccessIdentity": "origin-access-identity/cloudfront/$ORIGIN_ACCESS_IDENTITY"
                }
            }
        ]
    },
    "DefaultCacheBehavior": {
        "TargetOriginId": "S3-$S3_BUCKET_NAME",
        "ViewerProtocolPolicy": "redirect-to-https",
        "AllowedMethods": {
            "Quantity": 2,
            "Items": ["HEAD", "GET"],
            "CachedMethods": {
                "Quantity": 2,
                "Items": ["HEAD", "GET"]
            }
        },
        "ForwardedValues": {
            "QueryString": false,
            "Cookies": { "Forward": "none" }
        },
        "MinTTL": 0,
        "DefaultTTL": 86400,
        "MaxTTL": 31536000,
        "Compress": true
    },
    "CustomErrorResponses": {
        "Quantity": 2,
        "Items": [
            {
                "ErrorCode": 403,
                "ResponsePagePath": "/index.html",
                "ResponseCode": "200",
                "ErrorCachingMinTTL": 300
            },
            {
                "ErrorCode": 404,
                "ResponsePagePath": "/index.html",
                "ResponseCode": "200",
                "ErrorCachingMinTTL": 300
            }
        ]
    },
    "Enabled": true
}
DISTEOF
)

DISTRIBUTION_ID=$(aws cloudfront create-distribution \
    --distribution-config "$DISTRIBUTION_CONFIG" \
    --query Distribution.Id \
    --output text \
    2>/dev/null || echo "")

if [ -z "$DISTRIBUTION_ID" ]; then
    DISTRIBUTION_ID=$(aws cloudfront list-distributions --query "DistributionList.Items[?Origins.Items[0].DomainName=='$S3_BUCKET_NAME.s3.$REGION.amazonaws.com'].Id" --output text)
fi

echo "CloudFront distribution created: $DISTRIBUTION_ID"
echo ""

echo "=== Setup Complete ==="
echo ""
echo "Add these values to GitHub Secrets:"
echo ""
echo "  S3_BUCKET_NAME=$S3_BUCKET_NAME"
echo "  LAMBDA_FUNCTION_NAME=$LAMBDA_FUNCTION_NAME"
echo "  CLOUDFRONT_DISTRIBUTION_ID=$DISTRIBUTION_ID"
echo "  API_URL=$API_URL"
echo ""
echo "CloudFront URL will be available after distribution deployment (10-15 minutes):"
echo "  https://$(aws cloudfront get-distribution --id $DISTRIBUTION_ID --query Distribution.DomainName --output text 2>/dev/null || echo 'pending...')"
