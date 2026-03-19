package com.axxohub.dummyspringboot;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class DummySpringbootApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnDashboardSummary() throws Exception {
        mockMvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers").value(greaterThanOrEqualTo(3)))
                .andExpect(jsonPath("$.products").value(greaterThanOrEqualTo(3)));
    }

    @Test
    void shouldCreateAndConfirmOrder() throws Exception {
        mockMvc.perform(post("/api/test/reset"))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult customersResult = mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult productsResult = mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode customers = objectMapper.readTree(customersResult.getResponse().getContentAsString());
        JsonNode products = objectMapper.readTree(productsResult.getResponse().getContentAsString());
        long customerId = customers.get(0).get("id").asLong();
        long productId = products.get(1).get("id").asLong();

        String payload = """
                {
                  "customerId": %d,
                  "productId": %d,
                  "quantity": 1
                }
                """.formatted(customerId, productId);

        MvcResult createResult = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn();

        JsonNode order = objectMapper.readTree(createResult.getResponse().getContentAsString());
        long orderId = order.get("id").asLong();

        mockMvc.perform(post("/api/orders/" + orderId + "/confirm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldResetDatabase() throws Exception {
        mockMvc.perform(post("/api/test/reset"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Database reset complete"));
    }
}
