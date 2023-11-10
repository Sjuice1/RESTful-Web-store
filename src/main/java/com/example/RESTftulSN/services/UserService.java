package com.example.RESTftulSN.services;

import com.example.RESTftulSN.DTO.UserDTO;
import com.example.RESTftulSN.DTO.UsersDTOForRegister;
import com.example.RESTftulSN.enums.USER_ROLE;
import com.example.RESTftulSN.models.Item;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.repositories.UsersRepository;
import com.example.RESTftulSN.util.InvalidDataException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService{
    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UsersRepository usersRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public Users registerUser(UsersDTOForRegister usersDTO) {
        usersDTO.setPassword(passwordEncoder.encode(usersDTO.getPassword()));
        Users user = dtoToModel(usersDTO);
        usersRepository.save(user);
        return user;

    }
    @Transactional
    public void saveUser(Users user){
        usersRepository.save(user);
    }
    @Transactional
    public void deleteById(int id) {
        Users user = getById((long)id);
        usersRepository.delete(user);
    }

    @Transactional
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
    @Transactional
    public void addItemToCart(Users users, Item item) {
        if(users.getCart().stream().filter(cartItem -> cartItem.equals(item)).count() > item.getItemCount()){
            throw  new InvalidDataException("You can't put that much items in cart");
        }
        users.setCart(item);
        usersRepository.save(users);
    }
    @Transactional
    public void removeItemFromCart(Users users, Item item) {
        if(!users.getCart().contains(item)){
            throw new InvalidDataException("No item in cart");
        }
        users.getCart().remove(item);
        usersRepository.save(users);
    }


    public List<UserDTO> getAllUsersDTO(){
         return usersRepository.findAll().stream().map(user ->
                 new UserDTO(user.getUsername(),user.getPassword(), user.getEmail())).
                 collect(Collectors.toList());
    }

    public Boolean isUsernameExist(String username) {
        return usersRepository.existsByUsername(username);
    }

    public Boolean isEmail(String email) {
        return usersRepository.existsByEmail(email);
    }

    public Users getById(Long userId) {
       Optional<Users> user = usersRepository.findById((int)(long)userId);
       if(user.isEmpty()){
           throw new InvalidDataException("There's no user with such id");
       }
       return user.get();
    }

    private Users dtoToModel(UsersDTOForRegister usersDTOForRegister){
        Users user = modelMapper.map(usersDTOForRegister,Users.class);
        user.setUserRole(USER_ROLE.ROLE_GUEST);
        user.setCreation_date(LocalDateTime.now());
        return user;
    }


}
