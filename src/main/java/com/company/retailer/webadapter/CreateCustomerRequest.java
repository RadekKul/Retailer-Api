package com.company.retailer.webadapter;


import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequest(
        @Parameter(description = "Customer login. Must be unique")
        @NotBlank(message = "Login must be not empty")
        String login,
        @Parameter(description = "Customer email. Must be unique")
        @NotBlank(message = "Email must be not empty")
        @Email(message = "Email must have correct format")
        String email,
        @Parameter(description = "Customer first name")
        String firstName,
        @Parameter(description = "Customer surname")
        String surname
) {
}
