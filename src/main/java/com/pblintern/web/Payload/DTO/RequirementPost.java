package com.pblintern.web.Payload.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequirementPost {
    private String value;

    private String label;

    private Boolean __isNew__;
}
