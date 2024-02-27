package com.pblintern.web.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private boolean success;

    private String error;

    private HttpStatus status;

    private List<String> messages;

    private Instant instant;

    public ErrorResponse(boolean success, String error, HttpStatus status, List<String> messages) {
        this.success = success;
        this.error = error;
        this.status = status;
        this.messages = messages;
        this.instant = Instant.now();
    }
    public List<String> getMessage(){
        return messages == null ? null : new ArrayList<>(messages);
    }

    public void setMessage(List<String> messages){
        if(messages == null) {
            this.messages =null;
        }else {
            this.messages = Collections.unmodifiableList(messages);
        }
    }
}
