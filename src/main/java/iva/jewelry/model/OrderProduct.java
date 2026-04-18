    package iva.jewelry.model;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import iva.jewelry.dto.ProductSnapshot;
    import jakarta.persistence.*;
    import lombok.*;

    import java.io.Serializable;

    @Entity
    @Table(name = "order_product")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class OrderProduct implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_order_product")
        private Long id;

        @ManyToOne
        @JoinColumn(name = "order_id")
        @JsonIgnore
        private Order order;

        @Embedded
        private ProductSnapshot productSnapshot;

        @Column(nullable = false)
        private Integer amount;

    }
