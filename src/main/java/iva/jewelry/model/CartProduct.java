package iva.jewelry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import iva.jewelry.dto.ProductSnapshot;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CartProduct implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cart_product")
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Embedded
    //@Convert(converter = ProductSnapshotConverter.class)
//    @JdbcTypeCode(SqlTypes.JSON)
//    @Column(name = "product_snapshot", columnDefinition = "jsonb")
    private ProductSnapshot productSnapshot;

    @Column(nullable = false)
    private Integer amount;
    @Column(name = "snapshot_hash", nullable = false)
    private String snapshotHash;

    @Transient
    public BigDecimal getTotalPrice() {
        BigDecimal price = productSnapshot.getPrice();
        if (price == null) price = BigDecimal.ZERO;
        return price.multiply(BigDecimal.valueOf(amount));
    }


    //@Column(nullable = false)
//    private BigDecimal price;


}
