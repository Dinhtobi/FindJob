package com.pblintern.web.Payload.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyCardDetailResponse extends CompanyCardResponse{

    private String size;

    private String type;

    private String location;

    public CompanyCardDetailResponse(int id, String name, String logo, String size, String type, String location) {
        super(id, name, logo);
        this.size = size;
        this.type = type;
        this.location = location;
    }
}
