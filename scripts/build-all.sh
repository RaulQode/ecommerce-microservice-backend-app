#!/bin/bash

# Build all microservices

set -e

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BLUE}  Building All Microservices${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

# Get script directory and project root
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$( cd "$SCRIPT_DIR/.." && pwd )"

echo -e "${BLUE}Project root: ${PROJECT_ROOT}${NC}"
echo ""

cd "$PROJECT_ROOT"

SERVICES=(
    "service-discovery"
    "cloud-config"
    "api-gateway"
    "user-service"
    "product-service"
    "order-service"
    "payment-service"
    "shipping-service"
)

for service in "${SERVICES[@]}"; do
    echo -e "${YELLOW}📦 Building $service...${NC}"
    cd "$PROJECT_ROOT/$service"
    ../mvnw clean package -DskipTests
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ $service built successfully${NC}"
    else
        echo -e "${RED}✗ $service build failed${NC}"
    fi
    echo ""
done

cd "$PROJECT_ROOT"

echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}✅ All services built successfully!${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
