package org.example.pos_back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private String code;
    private String description;
    private double unitPrice;
    private int qtyOnHand;
}
