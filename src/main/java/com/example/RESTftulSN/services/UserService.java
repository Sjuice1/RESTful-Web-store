package com.example.RESTftulSN.services;

import com.example.RESTftulSN.DTO.CartDTO;
import com.example.RESTftulSN.DTO.ItemDTO;
import com.example.RESTftulSN.DTO.UserDTO;
import com.example.RESTftulSN.enums.User.USER_ROLE;
import com.example.RESTftulSN.models.Item;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.repositories.UsersRepository;
import com.example.RESTftulSN.security.BindingResultErrorCheck;
import com.example.RESTftulSN.util.exceptions.InvalidDataException;
import com.example.RESTftulSN.util.validations.UsernameAndEmailValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService{
    private final UsersRepository usersRepository;
    private final ItemService itemService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UsernameAndEmailValidation usernameAndEmailValidation;
    private final RabbitTemplate rabbitTemplate;
    private final BindingResultErrorCheck bindingResultErrorCheck;

    @Autowired
    public UserService(UsersRepository usersRepository, ItemService itemService, ModelMapper modelMapper, PasswordEncoder passwordEncoder, UsernameAndEmailValidation usernameAndEmailValidation, RabbitTemplate rabbitTemplate, BindingResultErrorCheck bindingResultErrorCheck) {
        this.usersRepository = usersRepository;
        this.itemService = itemService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.usernameAndEmailValidation = usernameAndEmailValidation;
        this.rabbitTemplate = rabbitTemplate;
        this.bindingResultErrorCheck = bindingResultErrorCheck;
    }
    @Transactional
    public ResponseEntity<?> registerUser(UserDTO.Request.Register usersDTO, BindingResult bindingResult) {
        usernameAndEmailValidation.validate(usersDTO, bindingResult);
        if (!usersDTO.getRepeatPassword().equals(usersDTO.getPassword())) {
            bindingResult.rejectValue("password", "", "Different Password");
        }
        bindingResultErrorCheck.check(bindingResult);
        Users user = dtoToModel(usersDTO);
        user.setPassword(usersDTO.getPassword());
        usersRepository.save(user);
        rabbitTemplate.convertAndSend("Direct-Exchange","registration",user);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @Transactional
    public void saveUser(Users user){
        usersRepository.save(user);
    }
    @Transactional
    public ResponseEntity<?> deleteById(Long id) {
        Users user = getById(id);
        usersRepository.delete(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateUserById(UserDTO.Request.Update userDTO, BindingResult bindingResult) {
        bindingResultErrorCheck.check(bindingResult);
        Users user = getById(userDTO.getId());
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
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> addItemToCart(CartDTO.Request.Create cartDTO, BindingResult bindingResult) {
        bindingResultErrorCheck.check(bindingResult);
        Users users = getById(cartDTO.getUserId());
        Item item = itemService.getById(cartDTO.getItemId());
        if(users.getCart().stream().filter(cartItem -> cartItem.equals(item)).count() > item.getItemCount()){
            throw  new InvalidDataException("You can't put that much items in cart");
        }
        users.setCart(item);
        usersRepository.save(users);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> getAllUsersDTO(){
         List<UserDTO.Response.Create> userDTOS  = usersRepository.findAll().stream().map(Users::toDto).toList();
         return new ResponseEntity<>(userDTOS,HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> removeItemFromCart(CartDTO.Request.Create cartDTO, BindingResult bindingResult) {
        bindingResultErrorCheck.check(bindingResult);
        Users users = getById(cartDTO.getUserId());
        Item item = itemService.getById(cartDTO.getItemId());
        if(!users.getCart().contains(item)){
            throw new InvalidDataException("No item in cart");
        }
        users.getCart().remove(item);
        usersRepository.save(users);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Users getById(Long userId) {
       Optional<Users> user = usersRepository.findById(userId);
       if(user.isEmpty()){
           throw new InvalidDataException("There's no user with such id");
       }
       return user.get();
    }

    private Users dtoToModel(UserDTO.Request.Register usersDTO){
        Users user = modelMapper.map(usersDTO,Users.class);
        user.setUserRole(USER_ROLE.ROLE_GUEST);
        user.setCreation_date(LocalDateTime.now());
        return user;
    }


    public ResponseEntity<?> generateNewToken(Long id) {
        Users user = getById(id);
        rabbitTemplate.convertAndSend("Direct-Exchange","registration",user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> getCartOfUser(Long id) {
        Users user = getById(id);
        List<ItemDTO.Request.Create> list = user.getItems().stream().
                map(item -> item.toDto()).toList();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    public ResponseEntity<?> getUserById(Long id) {
        Users users = getById(id);
        System.out.println(users.toDto());
        return new ResponseEntity<>(users.toDto(),HttpStatus.OK);
    }
    public boolean isUsernameExist(String username) {
        return usersRepository.existsByUsername(username);
    }

    public boolean isEmail(String email) {
        return usersRepository.existsByEmail(email);
    }
}
