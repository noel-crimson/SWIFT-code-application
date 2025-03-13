package com.swift.services;

import com.swift.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, String> {
    List<SwiftCode> findByCountryISO2(String countryISO2);

    List<SwiftCode> findBySwiftCodeStartingWith(String swiftPrefix);
}
