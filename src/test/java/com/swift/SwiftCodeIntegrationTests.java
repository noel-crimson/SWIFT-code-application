package com.swift;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SwiftCodeIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddSwiftCode_Success() throws Exception {
        String requestBody = """
        {
            "swiftCode": "TESTBANKXXX",
            "bankName": "Bank",
            "address": "St",
            "countryISO2": "US",
            "countryName": "United States",
            "isHeadquarter": true
        }
        """;

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated()) // 201 Created
                .andExpect(jsonPath("$.message").value("SWIFT code added successfully."));
    }

    @Test
    void testAddSwiftCode_FailsForInvalidCountry() throws Exception {
        String requestBody = """
        {
            "swiftCode": "BADCODEEEEE",
            "bankName": "Bank",
            "address": "St",
            "countryISO2": "XX",
            "countryName": "Nowhere",
            "isHeadquarter": false
        }
        """;

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest()) // 400 Bad Request
                .andExpect(jsonPath("$.message").value("Country ISO2 code does not exist."));
    }

    @Test
    void testGetSwiftCode_NotFound() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/UNKNOWN"))
                .andExpect(status().isNotFound()); // 404 Not Found
    }
}
