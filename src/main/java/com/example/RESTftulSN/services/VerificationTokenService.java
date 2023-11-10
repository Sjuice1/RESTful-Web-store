package com.example.RESTftulSN.services;


import com.example.RESTftulSN.DTO.VerificationTokenDTO;
import com.example.RESTftulSN.enums.USER_ROLE;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.models.VerificationToken;
import com.example.RESTftulSN.repositories.VerificationTokenRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository, ModelMapper modelMapper) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.modelMapper = modelMapper;
    }
    @Transactional
    public void createToken(VerificationTokenDTO verificationTokenDTO, Users user){
        VerificationToken verificationToken = dtoToModel(verificationTokenDTO,user);
        verificationTokenRepository.save(verificationToken);
    }
    @Transactional
    public boolean checkToken(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken.isPresent()){
            verificationToken.get().getUser().setUserRole(USER_ROLE.ROLE_VERIFIED);
            verificationTokenRepository.delete(verificationToken.get());
            return true;
        }
        return false;
    }
    private VerificationToken dtoToModel(VerificationTokenDTO verificationTokenDTO,Users user) {
        VerificationToken verificationToken = modelMapper.map(verificationTokenDTO,VerificationToken.class);
        verificationToken.setUser(user);
        return verificationToken;
    }
}
