package iva.jewelry.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "`order`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderStatus> orderStatuses = new HashSet<>();

    @Temporal(TemporalType.DATE)
    @Column(name = "date_creation", nullable = false)
    private Date dateOfCreation;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_expected", nullable = false)
    private Date dateExpected;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_final")
    private Date dateFinal;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public Order(User user, Date dateOfCreation, Date dateExpected, List<OrderProduct> orderProducts) {
        this.user = user;
        this.dateOfCreation = dateOfCreation;
        this.dateExpected = dateExpected;
        this.orderProducts = orderProducts;
    }
}
