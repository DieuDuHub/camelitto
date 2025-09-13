#!/bin/bash

# Script to test the request logging functionality

echo "🚀 Testing Camel Request Logging System..."
echo "=========================================="

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test different API calls
echo -e "${BLUE}Testing JSON API calls...${NC}"
curl -s "http://localhost:8080/api/camel/person/1?type=json" > /dev/null
curl -s "http://localhost:8080/api/camel/person/2" > /dev/null
curl -s "http://localhost:8080/api/camel/person/42?type=json" > /dev/null

echo -e "${BLUE}Testing SOAP/XML API calls...${NC}"
curl -s "http://localhost:8080/api/camel/person/1?type=soap" > /dev/null
curl -s "http://localhost:8080/api/camel/person/3?type=xml" > /dev/null
curl -s "http://localhost:8080/api/camel/person/99?type=soap" > /dev/null

echo -e "${BLUE}Testing error cases...${NC}"
curl -s "http://localhost:8080/api/camel/person/invalid" > /dev/null
curl -s "http://localhost:8080/api/camel/person/0?type=unknown" > /dev/null

echo -e "${GREEN}✅ Test requests completed!${NC}"
echo ""
echo -e "${YELLOW}📄 Check the logs:${NC}"
echo "  Console logs: Check terminal output"
echo "  File logs: logs/camel-requests.log"
echo ""
echo -e "${YELLOW}📊 Recent log entries:${NC}"
if [ -f "logs/camel-requests.log" ]; then
    echo "----------------------------------------"
    tail -20 logs/camel-requests.log
    echo "----------------------------------------"
else
    echo "⚠️  Log file not found yet. Check console output."
fi

echo ""
echo -e "${YELLOW}🔍 Log analysis commands:${NC}"
echo "  View all requests: cat logs/camel-requests.log"
echo "  Filter by HTTP code: grep 'HTTP 200' logs/camel-requests.log"
echo "  Filter by execution time: grep 'REQUEST_SUMMARY' logs/camel-requests.log"
echo "  Count requests: wc -l logs/camel-requests.log"
echo "  Real-time monitoring: tail -f logs/camel-requests.log"
