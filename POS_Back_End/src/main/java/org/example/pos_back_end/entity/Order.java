package org.example.pos_back_end.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.pos_back_end.dto.OrderDetailDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @Column(name = "order_id", length = 50)
    private String orderId;

    @ManyToOne
    @JoinColumn(name = "c_id", nullable = false)
    private Customer customer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "total", nullable = false)
    private Double total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
