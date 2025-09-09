package com.example.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

/**
 * Processor to create SOAP request XML from person ID
 */
@Component("soapRequestProcessor")
public class SoapRequestProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String personId = exchange.getIn().getBody(String.class);
        
        // Create SOAP request XML
        String soapRequest = String.format("""
            <?xml version="1.0" encoding="UTF-8"?>
            <soap:Envelope 
                xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
                xmlns:per="http://example.com/person">
                <soap:Header/>
                <soap:Body>
                    <per:GetEmployeeRequest>
                        <per:employeeId>%s</per:employeeId>
                    </per:GetEmployeeRequest>
                </soap:Body>
            </soap:Envelope>
            """, personId);
        
        exchange.getIn().setBody(soapRequest);
        exchange.getIn().setHeader("Content-Type", "application/soap+xml; charset=utf-8");
    }
}
