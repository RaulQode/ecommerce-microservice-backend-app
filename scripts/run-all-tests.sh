#!/bin/bash

# Run all tests: Unit, Integration, and E2E

# Don't exit on error - we want to run all tests even if some fail
set +e

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BLUE}  Running All Tests${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

# Get script directory and project root
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$( cd "$SCRIPT_DIR/.." && pwd )"

echo -e "${BLUE}Project root: ${PROJECT_ROOT}${NC}"
echo ""

# Check if services are running (for integration/E2E tests)
check_services() {
    # Check if API Gateway is responding
    if command -v curl &> /dev/null; then
        HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health 2>/dev/null)
        if [ "$HTTP_CODE" = "200" ]; then
            return 0
        fi
    fi
    return 1
}

# Unit Tests
echo -e "${YELLOW}🧪 Running Unit Tests for all microservices...${NC}"
echo -e "${BLUE}ℹ️  Unit tests do not require services running${NC}"
echo ""

SERVICES=(
    "user-service"
    "product-service"
    "order-service"
    "payment-service"
    "shipping-service"
)

UNIT_STATUS=0
UNIT_PASSED=0
UNIT_FAILED=0

for service in "${SERVICES[@]}"; do
    echo -e "${BLUE}  Testing $service...${NC}"
    cd "$PROJECT_ROOT/$service"
    if ./mvnw test -q > /dev/null 2>&1; then
        echo -e "${GREEN}  ✓ $service tests passed${NC}"
        UNIT_PASSED=$((UNIT_PASSED + 1))
    else
        echo -e "${RED}  ✗ $service tests failed${NC}"
        UNIT_STATUS=1
        UNIT_FAILED=$((UNIT_FAILED + 1))
    fi
done

cd "$PROJECT_ROOT"
echo ""
if [ $UNIT_STATUS -eq 0 ]; then
    echo -e "${GREEN}✓ All unit tests passed ($UNIT_PASSED/${#SERVICES[@]} services)${NC}"
else
    echo -e "${RED}✗ Some unit tests failed ($UNIT_PASSED passed, $UNIT_FAILED failed)${NC}"
fi
echo ""

# Check if services are running before integration tests
SERVICES_RUNNING=false
if check_services; then
    SERVICES_RUNNING=true
    echo -e "${GREEN}✓ Services are running (API Gateway accessible on port 8080)${NC}"
else
    echo -e "${YELLOW}⚠️  Services are NOT running${NC}"
    echo -e "${YELLOW}   Integration and E2E tests require all services to be running${NC}"
    echo -e "${YELLOW}   Start services with: ${BLUE}docker-compose up -d${NC}"
fi
echo ""

# Integration Tests
INTEGRATION_STATUS=2
if [ "$SERVICES_RUNNING" = true ]; then
    echo -e "${YELLOW}🔗 Running Integration Tests...${NC}"
    cd "$PROJECT_ROOT/tests/integration"
    ../../mvnw clean test -q
    INTEGRATION_STATUS=$?
    
    if [ $INTEGRATION_STATUS -eq 0 ]; then
        echo -e "${GREEN}✓ Integration tests passed${NC}"
    else
        echo -e "${RED}✗ Integration tests failed${NC}"
    fi
    cd "$PROJECT_ROOT"
else
    echo -e "${YELLOW}⏭️  Skipping Integration Tests (services not running)${NC}"
fi
echo ""

# E2E Tests
E2E_STATUS=2
if [ "$SERVICES_RUNNING" = true ]; then
    echo -e "${YELLOW}🎯 Running E2E Tests...${NC}"
    cd "$PROJECT_ROOT/tests/e2e"
    ../../mvnw clean test -q
    E2E_STATUS=$?
    
    if [ $E2E_STATUS -eq 0 ]; then
        echo -e "${GREEN}✓ E2E tests passed${NC}"
    else
        echo -e "${RED}✗ E2E tests failed${NC}"
    fi
    cd "$PROJECT_ROOT"
else
    echo -e "${YELLOW}⏭️  Skipping E2E Tests (services not running)${NC}"
fi
echo ""

# Summary
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BLUE}  Test Summary${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

# Unit tests
if [ $UNIT_STATUS -eq 0 ]; then
    echo -e "  Unit Tests:        ${GREEN}✓ PASSED${NC} ($UNIT_PASSED/${#SERVICES[@]} services)"
else
    echo -e "  Unit Tests:        ${RED}✗ FAILED${NC} ($UNIT_PASSED passed, $UNIT_FAILED failed)"
fi

# Integration tests
if [ $INTEGRATION_STATUS -eq 2 ]; then
    echo -e "  Integration Tests: ${YELLOW}⏭️  SKIPPED${NC} (services not running)"
elif [ $INTEGRATION_STATUS -eq 0 ]; then
    echo -e "  Integration Tests: ${GREEN}✓ PASSED${NC}"
else
    echo -e "  Integration Tests: ${RED}✗ FAILED${NC}"
fi

# E2E tests
if [ $E2E_STATUS -eq 2 ]; then
    echo -e "  E2E Tests:         ${YELLOW}⏭️  SKIPPED${NC} (services not running)"
elif [ $E2E_STATUS -eq 0 ]; then
    echo -e "  E2E Tests:         ${GREEN}✓ PASSED${NC}"
else
    echo -e "  E2E Tests:         ${RED}✗ FAILED${NC}"
fi

echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

# Provide helpful next steps
if [ "$SERVICES_RUNNING" = false ]; then
    echo ""
    echo -e "${YELLOW}💡 To run integration and E2E tests:${NC}"
    echo -e "   1. Start all services: ${BLUE}docker-compose up -d${NC}"
    echo -e "      Or manually start each service in separate terminals"
    echo -e "   2. Wait 30-60 seconds for services to register"
    echo -e "   3. Run this script again: ${BLUE}./run-all-tests.sh${NC}"
    echo ""
fi

# Exit with appropriate code
if [ $UNIT_STATUS -ne 0 ]; then
    echo -e "${RED}Unit tests failed!${NC}"
    exit 1
elif [ $INTEGRATION_STATUS -eq 1 ] || [ $E2E_STATUS -eq 1 ]; then
    echo -e "${RED}Some tests failed!${NC}"
    exit 1
elif [ $INTEGRATION_STATUS -eq 2 ] || [ $E2E_STATUS -eq 2 ]; then
    echo -e "${YELLOW}Some tests were skipped. Start services to run all tests.${NC}"
    exit 0
else
    echo -e "${GREEN}All tests passed! 🎉${NC}"
    exit 0
fi
