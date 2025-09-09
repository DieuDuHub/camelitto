#!/bin/bash
# Test script for Person API with different data types

echo "🚀 Testing Person API with different data sources"
echo "=================================================="

# Test JSON API (default)
echo ""
echo "📊 Testing JSON/REST API:"
echo "curl 'http://localhost:8090/api/camel/person/1'"
echo "curl 'http://localhost:8090/api/camel/person/1?type=json'"

# Test XML/SOAP API
echo ""
echo "🧼 Testing XML/SOAP API:"
echo "curl 'http://localhost:8090/api/camel/person/1?type=xml'"
echo "curl 'http://localhost:8090/api/camel/person/1?type=soap'"

echo ""
echo "💡 Available parameters:"
echo "  ?type=json (default) - Uses REST API, returns person data"
echo "  ?type=xml or ?type=soap - Uses SOAP API, returns employee data"

echo ""
echo "🔗 Expected data differences:"
echo "  JSON: first_name, last_name, creation_date"
echo "  XML:  full_name, department, position, salary, hire_date"
