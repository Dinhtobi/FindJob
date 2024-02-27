package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvResponse {
    private int id;

    private String location;

    private String offer;

    private String requirements;

    private int salary;

    private String title;
}
