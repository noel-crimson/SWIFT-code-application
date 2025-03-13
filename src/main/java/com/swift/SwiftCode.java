package com.swift;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "swiftcodes")
public class SwiftCode {
    @Id
    @Size(max = 12)
    @NotNull
    @Column(name = "swiftCode", nullable = false, length = 12)
    private String swiftCode;

    public SwiftCode() {}

    public SwiftCode(String swiftCode, String address, String bankName, String countryISO2, String countryName, Boolean isHeadquarter) {
        this.swiftCode = swiftCode;
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.isHeadquarter = isHeadquarter;
    }

    public @Size(max = 255) @NotNull String getAddress() {
        return address;
    }

    public void setAddress(@Size(max = 255) @NotNull String address) {
        this.address = address;
    }

    public @Size(max = 12) @NotNull String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(@Size(max = 12) @NotNull String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public @Size(max = 255) @NotNull String getBankName() {
        return bankName;
    }

    public void setBankName(@Size(max = 255) @NotNull String bankName) {
        this.bankName = bankName;
    }

    public @Size(max = 2) @NotNull String getCountryISO2() {
        return countryISO2;
    }

    public void setCountryISO2(@Size(max = 2) @NotNull String countryISO2) {
        this.countryISO2 = countryISO2;
    }

    public @Size(max = 30) @NotNull String getCountryName() {
        return countryName;
    }

    public void setCountryName(@Size(max = 30) @NotNull String countryName) {
        this.countryName = countryName;
    }

    public @NotNull Boolean getIsHeadquarter() {
        return isHeadquarter;
    }

    public void setIsHeadquarter(@NotNull Boolean headquarter) {
        isHeadquarter = headquarter;
    }

    @Size(max = 255)
    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @Size(max = 255)
    @NotNull
    @Column(name = "bankName", nullable = false)
    private String bankName;

    @Size(max = 2)
    @NotNull
    @Column(name = "countryISO2", nullable = false, length = 2)
    private String countryISO2;

    @Size(max = 30)
    @NotNull
    @Column(name = "countryName", nullable = false, length = 30)
    private String countryName;

    @NotNull
    @Column(name = "isHeadquarter", nullable = false)
    private Boolean isHeadquarter = false;

    @Override
    public String toString() {
        return "Swiftcode{" +
                "swiftCode='" + swiftCode + '\'' +
                ", address='" + address + '\'' +
                ", bankName='" + bankName + '\'' +
                ", countryISO2='" + countryISO2 + '\'' +
                ", countryName='" + countryName + '\'' +
                ", isHeadquarter=" + isHeadquarter +
                '}';
    }
}