#!/usr/bin/env python3
"""
Mock server to simulate person data APIs
- REST API: GET /person_data/{id} with JSON response
- SOAP API: POST /soap/PersonService with XML SOAP 1.2 response
"""

import json
import http.server
import socketserver
import urllib.parse
import xml.etree.ElementTree as ET
from datetime import datetime

class PersonDataHandler(http.server.BaseHTTPRequestHandler):
    
    def do_GET(self):
        """Handle GET requests"""
        try:
            # Parse the URL path
            path = urllib.parse.urlparse(self.path).path
            
            # Check if it matches the person_data endpoint pattern
            if path.startswith('/person_data/'):
                # Extract the ID from the path
                person_id = path.split('/person_data/')[-1]
                
                # Generate mock person data
                mock_data = self.generate_person_data(person_id)
                
                # Send response
                self.send_response(200)
                self.send_header('Content-Type', 'application/json')
                self.send_header('Access-Control-Allow-Origin', '*')
                self.end_headers()
                
                # Write JSON response
                self.wfile.write(json.dumps(mock_data, indent=2).encode('utf-8'))
                
                # Log the request
                print(f"✅ REST GET {self.path} - Returned person data for ID: {person_id}")
                
            else:
                # Return 404 for unknown endpoints
                self.send_error(404, f"Endpoint not found: {path}")
                print(f"❌ GET {self.path} - 404 Not Found")
                
        except Exception as e:
            # Return 500 for server errors
            self.send_error(500, f"Server error: {str(e)}")
            print(f"💥 GET {self.path} - 500 Server Error: {e}")
    
    def do_POST(self):
        """Handle POST requests (SOAP)"""
        try:
            # Parse the URL path
            path = urllib.parse.urlparse(self.path).path
            
            # Check if it's a SOAP request
            if path == '/soap/PersonService':
                # Read the request body
                content_length = int(self.headers.get('Content-Length', 0))
                post_data = self.rfile.read(content_length).decode('utf-8')
                
                # Parse SOAP request to extract person ID
                person_id = self.extract_person_id_from_soap(post_data)
                
                # Generate SOAP response
                soap_response = self.generate_soap_response(person_id)
                
                # Send response
                self.send_response(200)
                self.send_header('Content-Type', 'application/soap+xml; charset=utf-8')
                self.send_header('Access-Control-Allow-Origin', '*')
                self.end_headers()
                
                # Write SOAP response
                self.wfile.write(soap_response.encode('utf-8'))
                
                # Log the request
                print(f"🧼 SOAP POST {self.path} - Returned employee data for ID: {person_id}")
                
            else:
                # Return 404 for unknown endpoints
                self.send_error(404, f"SOAP endpoint not found: {path}")
                print(f"❌ POST {self.path} - 404 Not Found")
                
        except Exception as e:
            # Return 500 for server errors
            self.send_error(500, f"Server error: {str(e)}")
            print(f"💥 POST {self.path} - 500 Server Error: {e}")
    
    def generate_person_data(self, person_id):
        """Generate mock person data based on ID"""
        
        # Mock data templates
        first_names = ["Person", "John", "Jane", "Alice", "Bob", "Charlie", "Diana", "Eve"]
        last_names = ["Doe", "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller"]
        
        # Use ID to generate consistent data
        id_num = int(person_id) if person_id.isdigit() else 1
        first_name_idx = (id_num - 1) % len(first_names)
        last_name_idx = (id_num - 1) % len(last_names)
        
        # Generate consistent timestamp based on ID
        base_timestamp = "2025-08-19T09:25:30.135028Z"
        
        return {
            "Ok": {
                "id": id_num,
                "first_name": f"{first_names[first_name_idx]}{id_num}",
                "last_name": f"{last_names[last_name_idx]}{id_num}",
                "email": f"person{id_num}@example.com",
                "phone": f"+3312345{id_num:04d}",
                "birth_date": "1990-05-15",
                "gender": "M" if id_num % 2 == 1 else "F",
                "created_at": base_timestamp,
                "updated_at": base_timestamp,
                "is_active": True
            }
        }
    
    def extract_person_id_from_soap(self, soap_request):
        """Extract person ID from SOAP request"""
        try:
            # Simple regex to find employeeId or personId in SOAP request
            import re
            id_match = re.search(r'<(?:employeeId|personId)>(\d+)</(?:employeeId|personId)>', soap_request)
            if id_match:
                return id_match.group(1)
            
            # Fallback: look for any number in the request
            number_match = re.search(r'>(\d+)<', soap_request)
            if number_match:
                return number_match.group(1)
            
            return "1"  # Default ID
        except Exception:
            return "1"
    
    def generate_soap_response(self, person_id):
        """Generate SOAP 1.2 response with employee data"""
        
        # Different employee data templates for SOAP
        employee_names = ["Alexandre", "Sophie", "Pierre", "Marie", "Laurent", "Isabelle", "Nicolas", "Céline"]
        departments = ["IT", "HR", "Finance", "Marketing", "Operations", "Sales", "Legal", "R&D"]
        positions = ["Developer", "Manager", "Analyst", "Director", "Coordinator", "Specialist", "Lead", "Consultant"]
        
        # Use ID to generate consistent employee data
        id_num = int(person_id) if person_id.isdigit() else 1
        name_idx = (id_num - 1) % len(employee_names)
        dept_idx = (id_num - 1) % len(departments)
        pos_idx = (id_num - 1) % len(positions)
        
        # Generate salary based on ID
        base_salary = 45000 + (id_num * 2500)
        
        soap_response = f'''<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope 
    xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
    xmlns:per="http://example.com/person"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <soap:Header/>
    <soap:Body>
        <per:GetEmployeeResponse>
            <per:Employee>
                <per:EmployeeId>{id_num}</per:EmployeeId>
                <per:FullName>{employee_names[name_idx]} {employee_names[(name_idx + 1) % len(employee_names)]}</per:FullName>
                <per:FirstName>{employee_names[name_idx]}</per:FirstName>
                <per:LastName>{employee_names[(name_idx + 1) % len(employee_names)]}</per:LastName>
                <per:Department>{departments[dept_idx]}</per:Department>
                <per:Position>{positions[pos_idx]}</per:Position>
                <per:Email>{employee_names[name_idx].lower()}.{employee_names[(name_idx + 1) % len(employee_names)].lower()}@company.com</per:Email>
                <per:Salary>{base_salary}</per:Salary>
                <per:HireDate>2020-0{(id_num % 9) + 1}-15</per:HireDate>
                <per:IsActive>true</per:IsActive>
                <per:Office>Building {(id_num % 3) + 1}, Floor {(id_num % 10) + 1}</per:Office>
                <per:Phone>+33 1 42 {id_num:02d} {id_num:02d} {id_num:02d}</per:Phone>
                <per:Extension>{1000 + id_num}</per:Extension>
            </per:Employee>
            <per:ResponseCode>SUCCESS</per:ResponseCode>
            <per:ResponseMessage>Employee data retrieved successfully</per:ResponseMessage>
            <per:Timestamp>{datetime.now().isoformat()}Z</per:Timestamp>
        </per:GetEmployeeResponse>
    </soap:Body>
</soap:Envelope>'''
        
        return soap_response
    
    def log_message(self, format, *args):
        """Override to customize logging"""
        # Custom logging is handled in do_GET and do_POST
        pass

def run_server(port=8001):
    """Start the mock server"""
    print(f"🚀 Starting Mock Server on http://localhost:{port}")
    print(f"📋 Available endpoints:")
    print(f"   📊 REST API:")
    print(f"     GET /person_data/{{id}} - Returns JSON person data")
    print(f"   🧼 SOAP API:")
    print(f"     POST /soap/PersonService - Returns XML employee data (SOAP 1.2)")
    print(f"")
    print(f"💡 Example requests:")
    print(f"   📊 REST: curl http://localhost:{port}/person_data/1")
    print(f"   🧼 SOAP: curl -X POST -H 'Content-Type: application/soap+xml' \\")
    print(f"           -d '<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">")
    print(f"               <soap:Body><employeeId>1</employeeId></soap:Body></soap:Envelope>' \\")
    print(f"           http://localhost:{port}/soap/PersonService")
    print(f"")
    print(f"🛑 Press Ctrl+C to stop the server")
    print(f"" + "="*70)
    
    try:
        with socketserver.TCPServer(("", port), PersonDataHandler) as httpd:
            httpd.serve_forever()
    except KeyboardInterrupt:
        print(f"\n🛑 Server stopped by user")
    except OSError as e:
        if "Address already in use" in str(e):
            print(f"❌ Port {port} is already in use. Please stop the existing process or use a different port.")
        else:
            print(f"❌ Error starting server: {e}")

if __name__ == "__main__":
    import sys
    
    # Allow custom port as command line argument
    port = 8001
    if len(sys.argv) > 1:
        try:
            port = int(sys.argv[1])
        except ValueError:
            print("❌ Invalid port number. Using default port 8001.")
    
    run_server(port)
