package com.swift.controllers;

import com.swift.DTO.CountryDTO;
import com.swift.DTO.MessageDTO;
import com.swift.DTO.SwiftCodeDTO;
import com.swift.SwiftCode;
import com.swift.services.SwiftCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftCodeController {

    private final SwiftCodeService service;

    public SwiftCodeController(SwiftCodeService service) {
        this.service = service;
    }

    @GetMapping("/{swiftCode}")
    public ResponseEntity<SwiftCodeDTO> getSwiftCode(@PathVariable String swiftCode) {
        return service.getSwiftCode(swiftCode)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/country/{countryISO2}")
    public ResponseEntity<CountryDTO> getSwiftCodesByCountry(@PathVariable String countryISO2) {
        return service.getSwiftCodesByCountry(countryISO2)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MessageDTO> addSwiftCode(@RequestBody SwiftCode swiftCode) {
        service.saveSwiftCode(swiftCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDTO("SWIFT code added successfully."));
    }

    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<MessageDTO> deleteSwiftCode(@PathVariable String swiftCode) {
        boolean deleted = service.deleteSwiftCode(swiftCode);
        return deleted ? ResponseEntity.ok(new MessageDTO("SWIFT code deleted."))
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("SWIFT code not found."));
    }
}
