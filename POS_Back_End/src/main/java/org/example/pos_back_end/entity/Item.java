package org.example.pos_back_end.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @Column(name = "qty_on_hand", nullable = false)
    private Integer qtyOnHand;
}
