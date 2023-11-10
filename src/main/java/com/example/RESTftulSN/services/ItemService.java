package com.example.RESTftulSN.services;

import com.example.RESTftulSN.DTO.ItemDTO;
import com.example.RESTftulSN.models.Item;
import com.example.RESTftulSN.repositories.ItemRepository;
import com.example.RESTftulSN.util.InvalidDataException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ItemService(UserService userService, ItemRepository itemRepository, ModelMapper modelMapper) {
        this.userService = userService;
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
    }
    @Transactional
    public void addItem(ItemDTO itemDTO) {
        Item item = dtoToModel(itemDTO);
        itemRepository.save(item);
    }
    @Transactional
    public void deleteById(Long id) {
        Item item = getById(id);
        itemRepository.delete(item);
    }
    @Transactional
    public void updateUserById(Long id, ItemDTO itemDTO) {
        Item item = getById(id);
        if(!itemDTO.getSellerId().equals(item.getSeller().getId())){
            throw new InvalidDataException("You cant change seller");
        }
        item.setName(itemDTO.getName());
        item.setStateOfItem(itemDTO.getStateOfItem());
        item.setPrice(itemDTO.getPrice());
        item.setDescription(item.getDescription());
        itemRepository.save(item);
    }

    public List<Item> getAllItems(){
        return itemRepository.findAll();
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
        item.setSeller(userService.getById(itemDTO.getSellerId()));
        return item;
    }
}
