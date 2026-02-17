package org.example.pos_back_end.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @Column(name = "c_id", length = 20)
    private String cId;

    @Column(name = "c_name", length = 100, nullable = false)
    private String cName;

    @Column(name = "c_address", length = 200, nullable = false)
    private String cAddress;
}
