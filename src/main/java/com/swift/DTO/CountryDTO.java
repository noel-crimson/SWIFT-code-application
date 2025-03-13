package com.swift.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.swift.DTO.SwiftCodeDTO;

import java.util.List;

@JsonPropertyOrder({ "countryISO2", "countryName", "swiftCodes" })
public class CountryDTO {
    private String countryISO2;
    private String countryName;
    private List<SwiftCodeDTO> swiftCodes;

    public CountryDTO(String countryISO2, String countryName, List<SwiftCodeDTO> swiftCodes) {
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.swiftCodes = swiftCodes;
    }

    public String getCountryISO2() { return countryISO2; }
    public String getCountryName() { return countryName; }
    public List<SwiftCodeDTO> getSwiftCodes() { return swiftCodes; }
}
