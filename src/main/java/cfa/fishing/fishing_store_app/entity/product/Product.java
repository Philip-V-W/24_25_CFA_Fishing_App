package cfa.fishing.fishing_store_app.entity.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter // Generates getters
@Setter // Generates setters
@NoArgsConstructor // Generates empty constructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private Integer stockQuantity;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private String imageUrl;

    private boolean active = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(String name, BigDecimal price, ProductCategory category) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.active = true;
    }

    public Product(String name, String description, BigDecimal price,
                   Integer stockQuantity, ProductCategory category,
                   String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.imageUrl = imageUrl;
        this.active = true;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


}