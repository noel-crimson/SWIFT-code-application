package com.swift.services;

import com.swift.DTO.CountryDTO;
import com.swift.DTO.SwiftCodeDTO;
import com.swift.SwiftCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class SwiftCodeService {

    @Autowired
    private SwiftCodeRepository repository;

    @Autowired
    public SwiftCodeService(SwiftCodeRepository repository) {
        this.repository = repository;
    }

    public void saveUniqueSwiftCodes(List<SwiftCode> codes) {
        for (SwiftCode code : codes) {
            if (!repository.existsById(code.getSwiftCode())) {
                System.out.println("Saved " + code + " to repository.");
                repository.save(code);
            }
        }
    }

    private SwiftCodeDTO convertToDTO(SwiftCode swift) {
        return new SwiftCodeDTO(
                swift.getSwiftCode(),
                swift.getBankName(),
                swift.getAddress(),
                swift.getCountryISO2(),
                swift.getCountryName(),
                swift.getIsHeadquarter()
        );
    }


    //ENDPOINTS
    public Optional<SwiftCodeDTO> getSwiftCode(String swiftCode) {
        Optional<SwiftCode> swiftCodeEntity = repository.findById(swiftCode);

        if (swiftCodeEntity.isEmpty()) {
            return Optional.empty();
        }

        SwiftCode swift = swiftCodeEntity.get();
        SwiftCodeDTO response = convertToDTO(swift);

        if (swift.getSwiftCode().endsWith("XXX")) {
            List<SwiftCode> branches = repository.findBySwiftCodeStartingWith(swift.getSwiftCode().substring(0, 8));
            branches.removeIf(branch -> branch.getSwiftCode().equals(swift.getSwiftCode())); // remove itself from branches
            response.setBranches(branches.stream().map(this::convertToDTO).toList());
        }

        return Optional.of(response);
    }


    public Optional<CountryDTO> getSwiftCodesByCountry(String countryISO2) {
        List<SwiftCode> swiftCodes = repository.findByCountryISO2(countryISO2.toUpperCase());

        if (swiftCodes.isEmpty()) {
            return Optional.empty();
        }

        String countryName = new Locale("", countryISO2.toUpperCase()).getDisplayCountry();

        if (countryName.isEmpty()) {
            countryName = "Unknown Country";
        }

        List<SwiftCodeDTO> swiftCodeResponses = swiftCodes.stream()
                .map(this::convertToDTO)
                .toList();

        CountryDTO response = new CountryDTO(countryISO2, countryName, swiftCodeResponses);
        return Optional.of(response);
    }

    public void saveSwiftCode(SwiftCode swiftCode) {
        if (swiftCode.getSwiftCode().length() != 11){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SWIFT code must be exactly 11 characters.");
        }
        if (swiftCode.getSwiftCode() == null || swiftCode.getSwiftCode().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SWIFT code is required.");
        }
        if (swiftCode.getAddress() == null || swiftCode.getAddress().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address is required.");
        }
        if (swiftCode.getIsHeadquarter() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "isHeadquarters flag is required.");
        }
        if (swiftCode.getCountryName() == null  || swiftCode.getCountryName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Country name is required.");
        }
        if (swiftCode.getCountryISO2() == null || swiftCode.getCountryISO2().length() != 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Country ISO2 code must be exactly 2 letters.");
        }
        String relatedCountryName = new Locale("", swiftCode.getCountryISO2().toUpperCase()).getDisplayCountry();
        //when country code is not valid, getDisplayCountry() returns the countryISO2 code 
        if (relatedCountryName.equals(swiftCode.getCountryISO2().toUpperCase()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Country ISO2 code does not exist.");
        }
        if (!relatedCountryName.equals(swiftCode.getCountryName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Country ISO2 code does not match provided country, expected: " + relatedCountryName);
        }
        if (!swiftCode.getIsHeadquarter() && swiftCode.getSwiftCode().endsWith("XXX")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SWIFT code ends with XXX, yet isHeadquarter is false.");
        }
        if (swiftCode.getIsHeadquarter() && !swiftCode.getSwiftCode().endsWith("XXX")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "isHeadquarter is true, yet SWIFT code doesn't end with XXX.");
        }
        if (repository.existsById(swiftCode.getSwiftCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SWIFT code already exists.");
        }
        repository.save(swiftCode);
    }

    public boolean deleteSwiftCode(String swiftCode) {
        if (repository.existsById(swiftCode)) {
            repository.deleteById(swiftCode);
            return true;
        }
        return false;
    }
}
