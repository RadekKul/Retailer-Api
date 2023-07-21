package com.company.retailer.webadapter;

import com.company.retailer.application.CustomerService;
import com.company.retailer.utils.CreateCustomerRequestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturn201AndUUIDForCorrectCreateCustomerRequest() throws Exception {
        CreateCustomerRequest request = CreateCustomerRequestBuilder.create();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        UUID customerId = UUID.fromString(responseBody);
        assertNotNull(customerId);
    }

    @Test
    void shouldReturn400WhenWrongJSONInCustomerCreate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("{}")))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void shouldReturn400WhenNoJSONSentInCustomerCreate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("   ")))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void shouldReturn400WhenNoLoginSentInCustomerCreate() throws Exception {
        String body = "{" +
                "    \"email\" : \"email@test.com\"," +
                "    \"firstName\" : \"first\"," +
                "    \"surname\" : \"surname\"" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].fieldName").value("login"))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Login must be not empty"))
                .andReturn();
    }

    @Test
    void shouldReturn400WhenNoEmailSentInCustomerCreate() throws Exception {
        String body = "{" +
                "    \"login\" : \"log\"," +
                "    \"firstName\" : \"first\"," +
                "    \"surname\" : \"surname\"" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].fieldName").value("email"))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Email must be not empty"))
                .andReturn();
    }

    @Test
    void shouldReturn400WhenWrongEmailSentInCustomerCreate() throws Exception {
        String body = "{" +
                "    \"login\" : \"log\"," +
                "    \"email\" : \"email\"," +
                "    \"firstName\" : \"first\"," +
                "    \"surname\" : \"surname\"" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].fieldName").value("email"))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Email must have correct format"))
                .andReturn();
    }

    @Test
    void shouldReturn400ForCreateCustomerRequestWithAlreadyUsedLoginOrEmail() throws Exception {

        CreateCustomerRequest customerCreateRequest = CreateCustomerRequestBuilder.create();
        customerService.createCustomer(customerCreateRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        String.format("Customer with given login %s or email %s already exists", customerCreateRequest.login(), customerCreateRequest.email())))
                .andReturn();

    }

    @Test
    void shouldReturn201AndUUIDForCorrectCreateTransactionRequest() throws Exception {
        CreateCustomerRequest customerCreateRequest = CreateCustomerRequestBuilder.create();
        UUID customerId = customerService.createCustomer(customerCreateRequest);
        CreateTransactionRequest request = new CreateTransactionRequest(BigDecimal.valueOf(80));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/customers/{id}/transactions", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        UUID transactionId = UUID.fromString(responseBody);
        assertNotNull(transactionId);
    }

    @Test
    void shouldReturn400ForCreateTransactionRequestWith0TransactionAmount() throws Exception {

        CreateCustomerRequest customerCreateRequest = CreateCustomerRequestBuilder.create();
        UUID customerId = customerService.createCustomer(customerCreateRequest);
        CreateTransactionRequest request = new CreateTransactionRequest(BigDecimal.valueOf(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/customers/{id}/transactions", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].fieldName").value("amount"))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Transaction amount must be positive"))
                .andReturn();
    }

    @Test
    void shouldReturn400ForCreateTransactionRequestWithNegativeTransactionAmount() throws Exception {

        CreateCustomerRequest customerCreateRequest = CreateCustomerRequestBuilder.create();
        UUID customerId = customerService.createCustomer(customerCreateRequest);
        CreateTransactionRequest request = new CreateTransactionRequest(BigDecimal.valueOf(-10.55));

        mockMvc.perform(MockMvcRequestBuilders.post("/customers/{id}/transactions", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].fieldName").value("amount"))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Transaction amount must be positive"))
                .andReturn();
    }

    @Test
    void shouldReturn400ForCreateTransactionRequestWithNotJsonBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/{id}/transactions", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("   ")))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void shouldReturn400ForCreateTransactionRequestWithNoAmount() throws Exception {
        String body = "{}";
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/{id}/transactions", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].fieldName").value("amount"))
                .andExpect(jsonPath("$.errors[0].errorMessage").value("Transaction amount must not be null"))
                .andReturn();
    }

    @Test
    void shouldReturn404WhenCustomerNotFoundWhenAddingTransaction() throws Exception {
        UUID customerId = UUID.randomUUID();
        CreateTransactionRequest request = new CreateTransactionRequest(BigDecimal.valueOf(80));

        mockMvc.perform(MockMvcRequestBuilders.post("/customers/{id}/transactions", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldReturn204ForCorrectUpdateTransactionRequest() throws Exception {
        CreateCustomerRequest customerCreateRequest = CreateCustomerRequestBuilder.create();
        UUID customerId = customerService.createCustomer(customerCreateRequest);

        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest(BigDecimal.valueOf(80));
        UUID transactionId = customerService.createTransaction(createTransactionRequest, customerId);

        UpdateTransactionRequest updateTransactionRequest = new UpdateTransactionRequest(BigDecimal.valueOf(100));

        mockMvc.perform(MockMvcRequestBuilders.put("/customers/{id}/transactions/{transaction-id}", customerId, transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTransactionRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenTransactionNotFoundWhenUpdatingTransaction() throws Exception {
        CreateCustomerRequest customerCreateRequest = CreateCustomerRequestBuilder.create();
        UUID customerId = customerService.createCustomer(customerCreateRequest);

        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest(BigDecimal.valueOf(80));
        customerService.createTransaction(createTransactionRequest, customerId);

        UpdateTransactionRequest updateTransactionRequest = new UpdateTransactionRequest(BigDecimal.valueOf(100));
        UUID wrongTransactionId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.put("/customers/{id}/transactions/{transaction-id}", customerId, wrongTransactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTransactionRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenCustomerNotFoundWhenUpdatingTransaction() throws Exception {
        CreateCustomerRequest customerCreateRequest = CreateCustomerRequestBuilder.create();
        UUID customerId = customerService.createCustomer(customerCreateRequest);

        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest(BigDecimal.valueOf(80));
        UUID transactionId = customerService.createTransaction(createTransactionRequest, customerId);

        UpdateTransactionRequest updateTransactionRequest = new UpdateTransactionRequest(BigDecimal.valueOf(100));

        UUID wrongCustomerId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.put("/customers/{id}/transactions/{transaction-id}", wrongCustomerId, transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTransactionRequest)))
                .andExpect(status().isNotFound());
    }
}