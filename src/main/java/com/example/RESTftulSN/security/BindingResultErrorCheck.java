package com.example.RESTftulSN.security;

import com.example.RESTftulSN.util.exceptions.InvalidDataException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class BindingResultErrorCheck {
    public void check(BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError er : bindingResult.getFieldErrors()) {
                sb.append(er.getDefaultMessage()).append(" ");
            }
            throw new InvalidDataException(sb.toString().trim());
        }
    }
}
