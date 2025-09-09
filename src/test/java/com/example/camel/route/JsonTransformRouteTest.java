package com.example.camel.route;

import com.example.camel.processor.PersonDataProcessor;
import org.apache.camel.CamelContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JsonTransformRouteTest {

    @Autowired
    private CamelContext camelContext;

    @Test
    void testCamelContextStartup() {
        // Vérifie que le contexte Camel démarre correctement
        assertNotNull(camelContext);
        assertTrue(camelContext.getRoutes().size() > 0);
    }

    @Test
    void testPersonDataProcessorDirectly() throws Exception {
        // Test direct du processeur de données de personne
        String inputJson = "{\"Ok\":{\"id\":1,\"first_name\":\"Jean\",\"last_name\":\"Dupont\",\"email\":\"jean.dupont@test.com\",\"phone\":\"+33123456789\",\"birth_date\":\"1990-05-15\",\"gender\":\"M\",\"created_at\":\"2025-08-19T09:25:30.135028Z\",\"updated_at\":\"2025-08-19T09:25:30.135028Z\",\"is_active\":true}}";
        
        PersonDataProcessor processor = new PersonDataProcessor();
        
        // Créer un exchange simple pour le test
        org.apache.camel.impl.DefaultCamelContext context = new org.apache.camel.impl.DefaultCamelContext();
        org.apache.camel.Exchange exchange = context.getEndpoint("direct:test").createExchange();
        exchange.getIn().setBody(inputJson);
        
        processor.process(exchange);
        
        String result = exchange.getIn().getBody(String.class);
        assertNotNull(result);
        assertTrue(result.contains("\"first_name\":\"Jean\""));
        assertTrue(result.contains("\"last_name\":\"Dupont\""));
        assertTrue(result.contains("\"creation_date\":\"2025-08-19T09:25:30.135028Z\""));
        // Vérifier que d'autres champs ne sont pas présents
        assertFalse(result.contains("email"));
        assertFalse(result.contains("phone"));
        assertFalse(result.contains("birth_date"));
        
        context.close();
    }
}
