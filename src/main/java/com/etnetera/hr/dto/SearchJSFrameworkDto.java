package com.etnetera.hr.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class SearchJSFrameworkDto {

    final String name;

    final List<String> version;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    final LocalDate deprecationDateAfter;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    final LocalDate deprecationDateBefore;

    final Integer minHypeLevel;

    final Integer maxHypeLevel;
}
