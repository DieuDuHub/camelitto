package com.example.camel.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Processor to extract and filter employee data from SOAP XML response
 */
@Component("soapResponseProcessor")
public class SoapResponseProcessor implements Processor {
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        String xmlResponse = exchange.getIn().getBody(String.class);
        
        // Parse XML response
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes()));
        
        // Extract employee data (different fields than person data)
        Map<String, Object> employeeData = new HashMap<>();
        
        try {
            // Extract data from SOAP response
            employeeData.put("employee_id", getTextContent(doc, "EmployeeId"));
            employeeData.put("full_name", getTextContent(doc, "FullName"));
            employeeData.put("department", getTextContent(doc, "Department"));
            employeeData.put("position", getTextContent(doc, "Position"));
            employeeData.put("email", getTextContent(doc, "Email"));
            employeeData.put("salary", getTextContent(doc, "Salary"));
            employeeData.put("hire_date", getTextContent(doc, "HireDate"));
            employeeData.put("office", getTextContent(doc, "Office"));
            employeeData.put("phone", getTextContent(doc, "Phone"));
            employeeData.put("extension", getTextContent(doc, "Extension"));
            employeeData.put("is_active", getTextContent(doc, "IsActive"));
            
            // Add metadata
            employeeData.put("data_source", "SOAP/XML");
            employeeData.put("response_code", getTextContent(doc, "ResponseCode"));
            employeeData.put("response_message", getTextContent(doc, "ResponseMessage"));
            
        } catch (Exception e) {
            // Fallback data if XML parsing fails
            employeeData.put("error", "Failed to parse SOAP response: " + e.getMessage());
            employeeData.put("raw_response", xmlResponse);
        }
        
        // Convert to JSON and update exchange body
        String jsonResponse = objectMapper.writeValueAsString(employeeData);
        exchange.getIn().setBody(jsonResponse);
        exchange.getIn().setHeader("Content-Type", "application/json");
    }
    
    private String getTextContent(Document doc, String tagName) {
        try {
            NodeList nodeList = doc.getElementsByTagName("per:" + tagName);
            if (nodeList.getLength() == 0) {
                // Try without namespace prefix
                nodeList = doc.getElementsByTagName(tagName);
            }
            if (nodeList.getLength() > 0) {
                return nodeList.item(0).getTextContent();
            }
        } catch (Exception e) {
            // Ignore parsing errors for individual fields
        }
        return null;
    }
}
