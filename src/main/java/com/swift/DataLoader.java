package com.swift;

import com.swift.services.SwiftCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private SwiftCodeService swiftCodeService;

    @Override
    public void run(String... args) {
        List<SwiftCode> parsedCodes = new ArrayList<SwiftCode>();
        List<String> rows = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("swift_codes.csv"))) {
            while (scanner.hasNextLine()) {
                rows.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {throw new RuntimeException(e);}
        //remove header
        rows.remove(0);
        //parse every line in the csv
        for (String row : rows)
        {
            //System.out.println("Parsing row: " + row);
            SwiftCode swiftcode = new SwiftCode();

            String address;
            //if there's quotes anywhere, that means there's an address with commas to remove
            if (row.contains("\""))
            {
                try (Scanner rowScanner = new Scanner(row)) {
                    rowScanner.useDelimiter("\"");
                    rowScanner.next(); //ignore everything before the delimiter
                    address=rowScanner.next();
                }
                swiftcode.setAddress(address);

            }
            else {
                //otherwise, just get the 5th address in this row
                try (Scanner rowScanner = new Scanner(row)) {
                    //since there was no quotes, there's also no commas in the address, so we can split()
                    String[] values = row.split(",", -1);
                    address=values[4].trim();
                    swiftcode.setAddress(address);
                }
            }

            //delete address from the row
            row = row.replace("\""+address+"\",", "");

            //split the rest of the row, now that there's no extra commas
            String[] values = row.split(",", -1);

            swiftcode.setCountryISO2(values[0].toUpperCase());
            swiftcode.setSwiftCode(values[1]);
            swiftcode.setBankName(values[3]);
            swiftcode.setCountryName(values[5]);

            String code = swiftcode.getSwiftCode();
            if (code.substring(code.length() - 3).equals("XXX")) {
                swiftcode.setIsHeadquarter(true);
            }
            //System.out.println(swiftcode);
            parsedCodes.add(swiftcode);
        }
        swiftCodeService.saveUniqueSwiftCodes(parsedCodes);
    }

}
