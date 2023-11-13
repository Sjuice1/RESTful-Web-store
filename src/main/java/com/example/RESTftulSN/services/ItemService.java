package com.example.RESTftulSN.services;

import com.example.RESTftulSN.DTO.ItemDTO;
import com.example.RESTftulSN.DTO.ReviewDTO;
import com.example.RESTftulSN.models.Item;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.repositories.ItemRepository;
import com.example.RESTftulSN.repositories.UsersRepository;
import com.example.RESTftulSN.security.BindingResultErrorCheck;
import com.example.RESTftulSN.util.exceptions.InvalidDataException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ItemService {
    private final UsersRepository usersRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final BindingResultErrorCheck bindingResultErrorCheck;

    @Autowired
    public ItemService(UsersRepository usersRepository, ItemRepository itemRepository, ModelMapper modelMapper, BindingResultErrorCheck bindingResultErrorCheck) {
        this.usersRepository = usersRepository;
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
        this.bindingResultErrorCheck = bindingResultErrorCheck;
    }
    @Transactional
    public ResponseEntity<?> addItem(ItemDTO itemDTO, BindingResult bindingResult) {
        bindingResultErrorCheck.check(bindingResult);
        Item item = dtoToModel(itemDTO);
        itemRepository.save(item);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> deleteItem(Long id) {
        Item item = getById(id);
        itemRepository.delete(item);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateItemById(Long id, ItemDTO itemDTO, BindingResult bindingResult) {
        bindingResultErrorCheck.check(bindingResult);
        Item item = getById(id);
        if(!itemDTO.getSellerId().equals(item.getSeller().getId())){
            throw new InvalidDataException("You cant change seller");
        }
        item.setName(itemDTO.getName());
        item.setStateOfItem(itemDTO.getStateOfItem());
        item.setPrice(itemDTO.getPrice());
        item.setDescription(item.getDescription());
        itemRepository.save(item);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> getAllItems(){
        List<ItemDTO> itemDTOS = itemRepository.findAll().stream()
                .map(item->new ItemDTO(item.getName()
                        ,item.getDescription()
                        ,item.getPrice()
                        ,item.getStateOfItem()
                        ,item.getItemCount()
                        ,item.getImgSource()
                        ,item.getItemCount())).toList();
        return new ResponseEntity<>(itemDTOS, HttpStatus.OK);
    }
    public Item getById(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if(item.isEmpty()){
            throw new InvalidDataException("There's no item with such id");
        }
        return item.get();
    }

    private Item dtoToModel(ItemDTO itemDTO) {
        Item item = modelMapper.map(itemDTO,Item.class);
        item.setPublicationTime(LocalDateTime.now());
        item.setSeller(getUserById(itemDTO.getSellerId()));
        return item;
    }

    public ResponseEntity<?> getItem(Long id) {
        return new ResponseEntity<>(getById(id),HttpStatus.OK);
    }

    public ResponseEntity<?> getItemReviews(Long id) {
        Item item = getById(id);
        List<ReviewDTO> reviewDTOS = item.getReviews().stream()
                .map(review -> new ReviewDTO(review.getMark(), review.getDescription(), item.getId()))
                .toList();
        return new ResponseEntity<>(reviewDTOS,HttpStatus.OK);
    }
    public Users getUserById(Long userId) {
        Optional<Users> user = usersRepository.findById(userId);
        if(user.isEmpty()){
            throw new InvalidDataException("There's no user with such id");
        }
        return user.get();
    }
}
