package com.example.RESTftulSN.services;

import com.example.RESTftulSN.DTO.ItemDTO;
import com.example.RESTftulSN.models.Item;
import com.example.RESTftulSN.models.Users;
import com.example.RESTftulSN.repositories.ItemRepository;
import com.example.RESTftulSN.util.InvalidDataException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(UserService userService, ItemRepository itemRepository) {
        this.userService = userService;
        this.itemRepository = itemRepository;
    }

    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    public void addItem(ItemDTO itemDTO) {
        Item item = dtoToModel(itemDTO);
        itemRepository.save(item);
    }

    private Item dtoToModel(ItemDTO itemDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Item item = modelMapper.map(itemDTO,Item.class);
        item.setPublicationTime(LocalDateTime.now());
        item.setSeller(userService.getById(itemDTO.getSeller_id()));
        return item;
    }

    public void deleteById(int id) {
        Item item = getById(id);
        itemRepository.delete(item);
    }
    public Item getById(int id) {
        Optional<Item> item = itemRepository.findById(id);
        if(item.isEmpty()){
            throw new InvalidDataException("There's no item with such id");
        }
        return item.get();
    }

    public void updateUserById(int id, ItemDTO itemDTO) {
        Item item = getById(id);
        if(!itemDTO.getSeller_id().equals(item.getSeller().getId())){
            throw new InvalidDataException("You cant change seller");
        }
        item.setName(itemDTO.getName());
        item.setStateOfItem(itemDTO.getStateOfItem());
        item.setPrice(itemDTO.getPrice());
        item.setDescription(item.getDescription());
        itemRepository.save(item);
    }
}
