package com.example.RESTftulSN.util.validations;

import com.example.RESTftulSN.DTO.UserDTO;
import com.example.RESTftulSN.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UsernameAndEmailValidation implements Validator {
    private final UsersRepository usersRepository;

    @Autowired
    public UsernameAndEmailValidation(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserDTO.Request.Register.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO.Request.Register userDTO = (UserDTO.Request.Register) target;
        Boolean usernameCheck = usersRepository.existsByUsername(userDTO.getUsername());
        Boolean emailCheck = usersRepository.existsByEmail(userDTO.getEmail());
        if(usernameCheck){
            errors.rejectValue("username","500","Username already exist");
        }
        if(emailCheck){
            errors.rejectValue("email","500","Email already exist");
        }
    }
}
