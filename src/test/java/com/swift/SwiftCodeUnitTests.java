package com.swift;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.swift.DTO.SwiftCodeDTO;
import com.swift.services.SwiftCodeRepository;
import com.swift.services.SwiftCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

class SwiftCodeUnitTests {

    @Mock
    private SwiftCodeRepository repository;

    @InjectMocks
    private SwiftCodeService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSwiftCode_WhenExists() {
        SwiftCode swiftCode = new SwiftCode("TESTBANKXXX", "123 Main St", "Test Bank", "US", "United States", true);
        when(repository.findById("TESTBANKXXX")).thenReturn(Optional.of(swiftCode));

        Optional<SwiftCodeDTO> result = service.getSwiftCode("TESTBANKXXX");

        assertTrue(result.isPresent());
        assertEquals("TESTBANKXXX", result.get().getSwiftCode());
        assertEquals("Test Bank", result.get().getBankName());
        assertEquals("123 Main St", result.get().getAddress());
        assertEquals("US", result.get().getCountryISO2());
        assertEquals("United States", result.get().getCountryName());
        assertEquals(true, result.get().getIsHeadquarter());
    }

    @Test
    void testGetSwiftCode_WhenNotExists() {
        when(repository.findById("UNKNOWN")).thenReturn(Optional.empty());

        Optional<SwiftCodeDTO> result = service.getSwiftCode("UNKNOWN");

        assertFalse(result.isPresent());
    }

    @Test
    void testSaveSwiftCode_ThrowsErrorForInvalidCode() {
        SwiftCode invalidCode = new SwiftCode("ERR", "123 Main St", "Test Bank", "US", "United States", true);

        Exception exception = assertThrows(ResponseStatusException.class, () -> service.saveSwiftCode(invalidCode));

        assertFalse(exception.getMessage().contains("SWIFT code must be exactly 8 or 11 characters."));
    }

    @Test
    void testSaveSwiftCode_ThrowsErrorForInvalidCountryISO() {
        SwiftCode invalidCode = new SwiftCode("ERR", "123 Main St", "Test Bank", "XX", "United States", true);

        Exception exception = assertThrows(ResponseStatusException.class, () -> service.saveSwiftCode(invalidCode));

        assertFalse(exception.getMessage().contains("Country ISO2 code does not exist."));
    }
}
