package vn.com.greencraze.product.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import vn.com.greencraze.product.enumeration.SaleStatus;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sale")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "image", nullable = false, columnDefinition = "TEXT")
    private String image;

    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date", nullable = false)
    private Instant endDate;

    @Column(name = "promotional_percent", nullable = false)
    private Double promotionalPercent;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SaleStatus status;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "sale_product_category",
            joinColumns = @JoinColumn(name = "sale_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "product_category_id", nullable = false)
    )
    @Builder.Default
    private Set<ProductCategory> productCategories = new HashSet<>();
}
