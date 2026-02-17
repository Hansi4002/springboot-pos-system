package org.example.pos_back_end.controller;

import lombok.RequiredArgsConstructor;
import org.example.pos_back_end.dto.ItemDTO;
import org.example.pos_back_end.service.custom.ItemService;
import org.example.pos_back_end.util.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/items")
@CrossOrigin
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<APIResponse<String>> saveItem(@RequestBody ItemDTO itemDTO){
        itemService.saveItem(itemDTO);
        return new ResponseEntity<>(new APIResponse<>(201, "Item Saved", null), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIResponse<Iterable<ItemDTO>>> getAllItems(){
        return new ResponseEntity<>(new APIResponse<>(200, "All Items", itemService.getAllItems()), HttpStatus.OK);
    }

    @GetMapping("/{itemCode}")
    public ResponseEntity<APIResponse<ItemDTO>> getItem(@PathVariable String itemCode){
        ItemDTO itemDTO = itemService.getItem(itemCode);
        return new ResponseEntity<>(new APIResponse<>(200, "Item Found", itemDTO), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<APIResponse<String>> updateItem(@RequestBody ItemDTO itemDTO){
        itemService.updateItem(itemDTO);
        return new ResponseEntity<>(new APIResponse<>(200, "Item Updated", null), HttpStatus.OK);
    }

    @DeleteMapping("/{itemCode}")
    public ResponseEntity<APIResponse<String>> deleteItem(@PathVariable String itemCode){
        itemService.deleteItem(itemCode);
        return new ResponseEntity<>(new APIResponse<>(200, "Item Deleted", null), HttpStatus.OK);
    }
}