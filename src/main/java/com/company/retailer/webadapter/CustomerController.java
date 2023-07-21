package com.company.retailer.webadapter;

import com.company.retailer.application.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Create customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created customer. Returns UUID of created Customer",
                    content = {@Content(schema = @Schema(implementation = UUID.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied. Additional information in body"),
            @ApiResponse(responseCode = "500", description = "UnexpectedError")
    })
    ResponseEntity<String> createCustomer(@RequestBody @Valid CreateCustomerRequest createCustomerRequest, BindingResult bindingResult) {
        RequestErrorValidator.validateRequestErrors(bindingResult);
        UUID customerId = customerService.createCustomer(createCustomerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerId.toString());
    }

    @PostMapping("{id}/transactions")
    @Operation(summary = "Create transaction for customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created. Returns UUID of created Transaction",
                    content = {@Content(schema = @Schema(implementation = UUID.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied. Additional information in body"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "UnexpectedError")
    })
    ResponseEntity<String> createTransaction(@PathVariable("id") UUID customerId,
                                             @RequestBody @Valid CreateTransactionRequest createTransactionRequest,
                                             BindingResult bindingResult) {
        RequestErrorValidator.validateRequestErrors(bindingResult);
        UUID transactionId = customerService.createTransaction(createTransactionRequest, customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionId.toString());
    }

    @PutMapping("{id}/transactions/{transaction-id}"
    )
    @Operation(summary = "Update transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transaction updated. Returns UUID of created Transaction"),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied. Additional information in body"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "UnexpectedError")
    })
    ResponseEntity<String> updateTransaction(@PathVariable("id") UUID customerId,
                                             @PathVariable("transaction-id") UUID transactionId,
                                             @RequestBody @Valid UpdateTransactionRequest updateTransactionRequest,
                                             BindingResult bindingResult) {
        RequestErrorValidator.validateRequestErrors(bindingResult);
        customerService.updateTransaction(customerId, transactionId, updateTransactionRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
