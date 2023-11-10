package com.example.RESTftulSN.services;


import com.example.RESTftulSN.DTO.VerificationTokenDTO;
import com.example.RESTftulSN.enums.USER_ROLE;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.models.VerificationToken;
import com.example.RESTftulSN.repositories.VerificationTokenRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }
    public void createToken(VerificationTokenDTO verificationTokenDTO, Users user){
        VerificationToken verificationToken = dtoToModel(verificationTokenDTO,user);
        verificationTokenRepository.save(verificationToken);
    }

    private VerificationToken dtoToModel(VerificationTokenDTO verificationTokenDTO,Users user) {
        ModelMapper modelMapper = new ModelMapper();
        VerificationToken verificationToken =  modelMapper.map(verificationTokenDTO,VerificationToken.class);
        verificationToken.setUser(user);
        return verificationToken;
    }

    public boolean checkToken(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken.isPresent()){
            verificationToken.get().getUser().setUserRole(USER_ROLE.ROLE_VERIFIED);
            verificationTokenRepository.save(verificationToken.get());
            return true;
        }
        return false;
    }
}
