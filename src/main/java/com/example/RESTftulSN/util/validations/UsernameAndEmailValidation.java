package com.example.RESTftulSN.util.validations;

import com.example.RESTftulSN.DTO.UserDTO.UserDTO;
import com.example.RESTftulSN.repositories.UsersRepository;
import com.example.RESTftulSN.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UsernameAndEmailValidation implements Validator {
    private final UsersRepository usersRepository;

    @Autowired
    public UsernameAndEmailValidation(UsersRepository usersRepository1) {
        this.usersRepository = usersRepository1;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
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
