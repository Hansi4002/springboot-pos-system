package org.example.pos_back_end.service.custom;

import org.example.pos_back_end.dto.ItemDTO;

import java.util.List;

public interface ItemService {
    void saveItem(ItemDTO itemDTO);
    void updateItem(ItemDTO itemDTO);
    void deleteItem(String itemCode);
    ItemDTO getItem(String itemCode);
    List<ItemDTO> getAllItems();
    boolean updateStock(String itemCode, int qty);
}
