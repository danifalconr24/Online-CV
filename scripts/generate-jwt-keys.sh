#!/bin/bash

set -e

OUTPUT_DIR="${1:-.}"

echo "Generating RSA key pair for JWT authentication..."

openssl genrsa -out "$OUTPUT_DIR/privateKey.pem" 2048
openssl rsa -in "$OUTPUT_DIR/privateKey.pem" -pubout -out "$OUTPUT_DIR/publicKey.pem"

echo ""
echo "Keys generated successfully:"
echo "  Private key: $OUTPUT_DIR/privateKey.pem"
echo "  Public key:  $OUTPUT_DIR/publicKey.pem"
echo ""
echo "Base64 encoded values for GitHub Secrets:"
echo ""
echo "JWT_PRIVATE_KEY:"
cat "$OUTPUT_DIR/privateKey.pem" | base64 | tr -d '\n'
echo ""
echo ""
echo "JWT_PUBLIC_KEY:"
cat "$OUTPUT_DIR/publicKey.pem" | base64 | tr -d '\n'
echo ""
echo ""
echo "IMPORTANT: Keep the private key secure and never commit it to the repository!"
