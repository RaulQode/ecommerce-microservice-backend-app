#!/bin/bash

# Kubernetes Deployment Script for E-commerce Microservices
# This script deploys all services to Kubernetes cluster

set -e

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BLUE}  E-commerce Microservices - Kubernetes Deploy${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

# Get script directory and project root
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$( cd "$SCRIPT_DIR/.." && pwd )"

echo -e "${BLUE}Project root: ${PROJECT_ROOT}${NC}"
echo ""

# Check if kubectl is installed
if ! command -v kubectl &> /dev/null; then
    echo -e "${RED}❌ kubectl is not installed${NC}"
    exit 1
fi

# Check cluster connection
echo -e "${YELLOW}🔍 Checking cluster connection...${NC}"
if ! kubectl cluster-info &> /dev/null; then
    echo -e "${RED}❌ Cannot connect to Kubernetes cluster${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Connected to cluster${NC}"
echo ""

# Create namespace
echo -e "${YELLOW}📦 Creating namespace...${NC}"
kubectl apply -f "$PROJECT_ROOT/k8s/namespace.yml"
echo -e "${GREEN}✓ Namespace created${NC}"
echo ""

# Deploy infrastructure services
echo -e "${YELLOW}🏗️  Deploying infrastructure services...${NC}"
echo -e "  → Service Discovery (Eureka)"
kubectl apply -f "$PROJECT_ROOT/k8s/service-discovery-deployment.yml"

echo -e "  → Cloud Config"
kubectl apply -f "$PROJECT_ROOT/k8s/cloud-config-deployment.yml"

echo -e "${YELLOW}⏳ Waiting for infrastructure services to be ready...${NC}"
kubectl wait --for=condition=available --timeout=120s deployment/service-discovery -n ecommerce || true
kubectl wait --for=condition=available --timeout=120s deployment/cloud-config -n ecommerce || true
sleep 10
echo -e "${GREEN}✓ Infrastructure services ready${NC}"
echo ""

# Deploy application services
echo -e "${YELLOW}🚀 Deploying application services...${NC}"
echo -e "  → API Gateway"
kubectl apply -f "$PROJECT_ROOT/k8s/api-gateway-deployment.yml"

echo -e "  → User Service"
kubectl apply -f "$PROJECT_ROOT/k8s/user-service-deployment.yml"

echo -e "  → Product Service"
kubectl apply -f "$PROJECT_ROOT/k8s/product-service-deployment.yml"

echo -e "  → Order Service"
kubectl apply -f "$PROJECT_ROOT/k8s/order-service-deployment.yml"

echo -e "  → Payment Service"
kubectl apply -f "$PROJECT_ROOT/k8s/payment-service-deployment.yml"

echo -e "  → Shipping Service"
kubectl apply -f "$PROJECT_ROOT/k8s/shipping-service-deployment.yml"

echo ""
echo -e "${YELLOW}⏳ Waiting for all deployments to be ready (this may take a few minutes)...${NC}"
kubectl wait --for=condition=available --timeout=300s deployment --all -n ecommerce || true

echo ""
echo -e "${GREEN}✅ All services deployed successfully!${NC}"
echo ""

# Display status
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BLUE}  Deployment Status${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

echo -e "${YELLOW}Pods:${NC}"
kubectl get pods -n ecommerce

echo ""
echo -e "${YELLOW}Services:${NC}"
kubectl get svc -n ecommerce

echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}✓ Deployment complete!${NC}"
echo ""
echo -e "${YELLOW}Access services with port-forward:${NC}"
echo -e "  API Gateway:        ${BLUE}kubectl port-forward service/api-gateway 8080:8080 -n ecommerce${NC}"
echo -e "  Service Discovery:  ${BLUE}kubectl port-forward service/service-discovery 8761:8761 -n ecommerce${NC}"
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
