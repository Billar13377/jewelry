package iva.jewelry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "order_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatus implements Serializable {

    @EmbeddedId
    private OrderStatusId id;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dateOfChanging;

    @ManyToOne
    @MapsId("orderId")
    @JsonIgnore
    private Order order;

    @ManyToOne
    @MapsId("statusId")
    @JsonIgnore
    private Status status;

}
