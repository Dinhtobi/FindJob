package com.pblintern.web.Services.Impl;

import com.pblintern.web.Entities.FieldOfActivity;
import com.pblintern.web.Payload.Responses.FieldResponse;
import com.pblintern.web.Repositories.FieldRepository;
import com.pblintern.web.Services.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IFieldService implements FieldService {

    @Autowired
    private FieldRepository fieldRepository;

    @Override
    public List<FieldResponse> getALL() {
        List<FieldOfActivity> fields = fieldRepository.findAll();
        List<FieldResponse> responses = new ArrayList<>();
        fields.stream().forEach(field -> {
            responses.add(new FieldResponse(field.getName()));
        });
        return responses;
    }
}
