# AWS Serverless Deployment Guide

This guide walks you through deploying the Online CV application to AWS using serverless services (always free tier).

## Architecture

```
┌─────────────┐     ┌──────────────┐     ┌─────────────────┐
│  CloudFront │────▶│  S3 Bucket   │     │  API Gateway    │
│   (CDN)     │     │ (Frontend)   │     │                 │
└─────────────┘     └──────────────┘     └────────┬────────┘
                                                  │
                                         ┌────────▼────────┐
                                         │  Lambda         │
                                         │ (Quarkus Native)│
                                         └────────┬────────┘
                                                  │
                                         ┌────────▼────────┐
                                         │  Supabase       │
                                         │   (PostgreSQL)  │
                                         └─────────────────┘
```

## Prerequisites

- [ ] AWS Account with IAM user (Lambda, API Gateway, S3, CloudFront permissions)
- [ ] Supabase account (free tier)
- [ ] AWS CLI installed and configured
- [ ] Docker installed (for native image build)

## Step 1: Create Supabase Database

1. Go to [supabase.com](https://supabase.com) and create a new project
2. Go to Project Settings → Database
3. Note down the connection details:
   - Host: `<project>.supabase.co`
   - Database: `postgres`
   - User: `postgres`
   - Password: `<your-password>`
   - Port: `5432` (or `6543` for connection pooler)
4. JDBC URL format: `jdbc:postgresql://<project>.supabase.co:5432/postgres`

## Step 2: Generate JWT Keys

Run the key generation script:

```bash
./scripts/generate-jwt-keys.sh
```

This outputs:
- `privateKey.pem` and `publicKey.pem` files
- Base64-encoded values for GitHub Secrets[setup-aws-infrastructure.sh](scripts%2Fsetup-aws-infrastructure.sh)

## Step 3: Set Up AWS Infrastructure

Run the setup script:

```bash
export AWS_ACCESS_KEY_ID=your_access_key
export AWS_SECRET_ACCESS_KEY=your_secret_key
export AWS_REGION=eu-west-1

./scripts/setup-aws-infrastructure.sh
```

This creates:
- S3 bucket for frontend
- Lambda function
- API Gateway
- CloudFront distribution

Note the output values for GitHub Secrets.

## Step 4: Configure GitHub Secrets

Go to your repository → Settings → Secrets and variables → Actions

Add these secrets:

| Secret | Description | Example |
|--------|-------------|---------|
| `AWS_ACCESS_KEY_ID` | IAM access key | `AKIAIOSFODNN7EXAMPLE` |
| `AWS_SECRET_ACCESS_KEY` | IAM secret key | `wJalrXUtnFEMI/K7MDENG/...` |
| `AWS_REGION` | AWS region | `eu-west-1` |
| `SUPABASE_JDBC_URL` | JDBC connection string | `jdbc:postgresql://xyz.supabase.co:5432/postgres` |
| `SUPABASE_USERNAME` | Database username | `postgres` |
| `SUPABASE_PASSWORD` | Database password | `your-password` |
| `ADMIN_USERNAME` | Admin login username | `admin` |
| `ADMIN_PASSWORD` | Admin login password | `secure-password` |
| `JWT_PRIVATE_KEY` | Base64 private key | (from generate-jwt-keys.sh) |
| `JWT_PUBLIC_KEY` | Base64 public key | (from generate-jwt-keys.sh) |
| `S3_BUCKET_NAME` | S3 bucket name | `online-cv-frontend-123456` |
| `LAMBDA_FUNCTION_NAME` | Lambda function name | `online-cv-backend` |
| `CLOUDFRONT_DISTRIBUTION_ID` | CloudFront ID | `E1ABCD2EFGHIJ3` |
| `API_URL` | API Gateway URL | `https://abc123.execute-api.eu-west-1.amazonaws.com/prod` |
| `CLOUDFRONT_URL` | CloudFront URL | `https://d123.cloudfront.net` |
| `JWT_ISSUER` | JWT issuer URL | `https://your-domain.com` |

## Step 5: Deploy

Push to main branch or manually trigger the workflow:

```bash
git push origin main
```

Or go to Actions → "Deploy to AWS" → "Run workflow"

## Step 6: Database Schema Management

The `%prod` profile in `application.properties` uses `hibernate-orm.database.generation = update`, which automatically creates tables on first deploy and applies schema changes on subsequent deploys. No manual database setup is needed.

> **Note:** The GitHub Actions workflow injects JWT keys from the `JWT_PRIVATE_KEY` and `JWT_PUBLIC_KEY` secrets (base64-encoded) into `src/main/resources/` before the native build. The `generate-jwt-keys.sh` script outputs the base64 values you need.

## Cost Summary

| Service | Free Tier | Estimated Usage |
|---------|-----------|-----------------|
| Lambda | 1M requests + 400K GB-seconds | ~$0/month |
| API Gateway | 1M requests | ~$0/month |
| S3 | 5GB + 20K PUT + 2M GET | ~$0/month |
| CloudFront | 1TB transfer + 10M requests | ~$0/month |
| Supabase | 500MB database | ~$0/month |
| **Total** | | **$0/month** |

## Troubleshooting

### Lambda Cold Start Issues

Native Quarkus starts in ~50-100ms. If still slow:
- Increase Lambda memory to 1024MB
- Enable Lambda provisioned concurrency (not free tier)

### CORS Errors

1. Check `CORS_ORIGIN` environment variable in Lambda
2. Ensure it matches your CloudFront URL exactly (including `https://`)

### Database Connection Issues

1. Verify Supabase project is not paused (free tier pauses after inactivity)
2. Check connection string format
3. Ensure port 5432 is used (not 6543 for direct connection)

## Manual Deployment Commands

### Backend Only

```bash
cd backend
./mvnw clean package -Pnative -Dquarkus.native.enabled=true
cd target && zip -j function.zip bootstrap && cd ..
aws lambda update-function-code --function-name online-cv-backend --zip-file fileb://target/function.zip
```

### Frontend Only

```bash
cd frontend
npm run build
aws s3 sync dist/spa/ s3://your-bucket-name --delete
aws cloudfront create-invalidation --distribution-id YOUR_DISTRIBUTION_ID --paths "/*"
```
