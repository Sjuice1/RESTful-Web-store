package com.example.RESTftulSN.services;

import com.example.RESTftulSN.DTO.UserDTO;
import com.example.RESTftulSN.DTO.UsersDTOForRegister;
import com.example.RESTftulSN.enums.USER_ROLE;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.repositories.UsersRepository;
import com.example.RESTftulSN.util.InvalidDataException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService{
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsersDTO(){
         return usersRepository.findAll().stream().map(user ->
                 new UserDTO(user.getUsername(),user.getPassword(), user.getEmail())).
                 collect(Collectors.toList());
    }

    public void registerUser(UsersDTOForRegister usersDTO) {
        usersDTO.setPassword(passwordEncoder.encode(usersDTO.getPassword()));
        usersRepository.save(dtoToModel(usersDTO));

    }

    private Users dtoToModel(UsersDTOForRegister usersDTOForRegister){
        ModelMapper modelMapper = new ModelMapper();
        Users user = modelMapper.map(usersDTOForRegister,Users.class);
        user.setUserRole(USER_ROLE.ROLE_GUEST);
        user.setCreation_date(LocalDateTime.now());
        return user;
    }

    public Boolean isUsernameExist(String username) {
        return usersRepository.existsByUsername(username);
    }

    public Boolean isEmail(String email) {
        return usersRepository.existsByEmail(email);
    }

    public void deleteById(int id) {
        Users user = getById((long)id);
        usersRepository.delete(user);
    }

    public void updateUserById(int id, UserDTO userDTO) {
        Optional<Users> optionalUser = usersRepository.findById(id);
        if(optionalUser.isEmpty()){
            throw new InvalidDataException("No user with such id");
        }
        Users user = optionalUser.get();
        if(!user.getUsername().equals(userDTO.getUsername()) && isUsernameExist(userDTO.getUsername())){
            throw new InvalidDataException("Username already exist");
        }
        if(!user.getEmail().equals(userDTO.getEmail()) && isEmail(userDTO.getEmail())){
            throw new InvalidDataException("Email already exist");
        }
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        usersRepository.save(user);
    }

    public Users getById(Long userId) {
       Optional<Users> user = usersRepository.findById((int)(long)userId);
       if(user.isEmpty()){
           throw new InvalidDataException("There's no user with such id");
       }
       return user.get();
    }
}
