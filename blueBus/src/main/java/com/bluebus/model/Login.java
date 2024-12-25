package com.bluebus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    private String username;
    private String password;
    @Override
    public String toString(){
        return username+" "+password;
    }
}
