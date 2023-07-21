package com.company.retailer.webadapter;

import com.company.retailer.application.CustomerQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
class CustomerQueryController {

    private final CustomerQueryService customerService;

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Customer",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "UnexpectedError")
    })
    ResponseEntity<CustomerResponse> getCustomer(@PathVariable(name = "id") UUID customerId) {
        CustomerResponse customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/{id}/transactions")
    @Operation(summary = "Get customer transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found transactions",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerTransactionsResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "UnexpectedError")
    })
    ResponseEntity<CustomerTransactionsResponse> getCustomerTransaction(@PathVariable(name = "id") UUID customerId) {
        return ResponseEntity.ok(customerService.getCustomerTransactions(customerId));
    }

    @GetMapping("{id}/rewards")
    @Operation(summary = "Get customer rewards information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found customer reward information",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerRewardsResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "UnexpectedError")
    })
    ResponseEntity<CustomerRewardsResponse> getRewards(@PathVariable(name = "id") UUID customerId) {
        return ResponseEntity.ok(customerService.calculateRewards(customerId));
    }
}
