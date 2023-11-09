package com.example.RESTftulSN.services;

import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.repositories.UsersRepository;
import com.example.RESTftulSN.security.UserDetailsImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersDetailsServiceImplementation implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersDetailsServiceImplementation(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> users = usersRepository.findByUsername(username);
        if(users.isEmpty()){
            //TODO some logic
        }
        return new UserDetailsImplementation(users.get());
    }

}
