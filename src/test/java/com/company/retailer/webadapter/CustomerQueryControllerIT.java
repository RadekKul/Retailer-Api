package com.company.retailer.webadapter;

import com.company.retailer.application.CustomerService;
import com.company.retailer.utils.CreateCustomerRequestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerQueryControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Test
    void shouldReturn200AndCustomerResponse() throws Exception {
        CreateCustomerRequest customerCreateRequest = CreateCustomerRequestBuilder.create();
        UUID customerId = customerService.createCustomer(customerCreateRequest);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(customerCreateRequest.login()))
                .andExpect(jsonPath("$.email").value(customerCreateRequest.email()))
                .andExpect(jsonPath("$.firstName").value(customerCreateRequest.firstName()))
                .andExpect(jsonPath("$.surname").value(customerCreateRequest.surname()));
    }

    @Test
    void shouldReturn404WhenCustomerNotFoundWhenGettingCustomer() throws Exception {
        UUID customerId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{id}", customerId))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldReturn200AndCustomerTransactionsResponse() throws Exception {
        CreateCustomerRequest customerCreateRequest = CreateCustomerRequestBuilder.create();
        UUID customerId = customerService.createCustomer(customerCreateRequest);

        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest(BigDecimal.valueOf(80.55));
        UUID transactionId = customerService.createTransaction(createTransactionRequest, customerId);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{id}/transactions", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerTransactions").isArray())
                .andExpect(jsonPath("$.customerTransactions.length()").value(1))
                .andExpect(jsonPath("$.customerTransactions.[0].id").value(transactionId.toString()))
                .andExpect(jsonPath("$.customerTransactions.[0].amount").value(80.55));
    }

    @Test
    void shouldReturn404WhenCustomerNotFoundWhenGettingCustomerTransactions() throws Exception {
        UUID customerId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{id}/transactions", customerId))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldReturn200AndCustomerRewardsResponse() throws Exception {
        CreateCustomerRequest customerCreateRequest = CreateCustomerRequestBuilder.create();
        UUID customerId = customerService.createCustomer(customerCreateRequest);

        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest(BigDecimal.valueOf(80));
        customerService.createTransaction(createTransactionRequest, customerId);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{id}/rewards", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastMonthRewardPoints").value(30))
                .andExpect(jsonPath("$.lastThreeMonthsRewardPoints").value(30));
    }

    @Test
    void shouldReturn404WhenCustomerNotFoundWhenGettingCustomerRewards() throws Exception {
        UUID customerId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{id}/rewards", customerId))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}