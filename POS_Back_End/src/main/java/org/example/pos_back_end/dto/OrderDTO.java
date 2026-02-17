package org.example.pos_back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private String orderId;
    private String cId;
    private Date date;
    private Double total;
    private List<OrderDetailDTO> items;

}
