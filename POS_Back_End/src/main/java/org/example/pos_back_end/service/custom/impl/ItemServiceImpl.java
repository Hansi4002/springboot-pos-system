package org.example.pos_back_end.service.custom.impl;

import org.example.pos_back_end.dto.ItemDTO;
import lombok.RequiredArgsConstructor;
import org.example.pos_back_end.entity.Item;
import org.example.pos_back_end.repository.ItemRepository;
import org.example.pos_back_end.service.custom.ItemService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void saveItem(ItemDTO itemDTO) {
        if (itemDTO == null) {
            throw new NullPointerException("Item is null");
        }

        // Check if item already exists
        if (itemRepository.existsById(itemDTO.getCode())) {
            throw new RuntimeException("Item with code " + itemDTO.getCode() + " already exists");
        }

        itemRepository.save(modelMapper.map(itemDTO, Item.class));
    }

    @Override
    public void updateItem(ItemDTO itemDTO) {
        if (itemDTO == null) {
            throw new NullPointerException("Item is null");
        }

        // Check if item exists
        if (!itemRepository.existsById(itemDTO.getCode())) {
            throw new RuntimeException("Item with code " + itemDTO.getCode() + " not found");
        }

        itemRepository.save(modelMapper.map(itemDTO, Item.class));
    }

    @Override
    public void deleteItem(String itemCode) {
        if (!itemRepository.existsById(itemCode)) {
            throw new RuntimeException("Item with code " + itemCode + " not found");
        }
        itemRepository.deleteById(itemCode);
    }

    @Override
    public ItemDTO getItem(String itemCode) {
        Item item = itemRepository.findById(itemCode)
                .orElseThrow(() -> new RuntimeException("Item with code " + itemCode + " not found"));
        return modelMapper.map(item, ItemDTO.class);
    }

    @Override
    public List<ItemDTO> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return modelMapper.map(items, new TypeToken<List<ItemDTO>>() {}.getType());
    }

    @Override
    public boolean updateStock(String itemCode, int qty) {
        Item item = itemRepository.findById(itemCode)
                .orElseThrow(() -> new RuntimeException("Item with code " + itemCode + " not found"));

        if (item.getQtyOnHand() < qty) {
            return false; // Insufficient stock
        }

        item.setQtyOnHand(item.getQtyOnHand() - qty);
        itemRepository.save(item);
        return true;
    }
}