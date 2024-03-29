package com.slowv.fruit.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.slowv.fruit.domain.enums.EProductStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@ToString
@Entity
@Getter
@Setter
@RedisHash
public class Product extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private double unitPrice;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String images;
    private int perCent;
    @Column(columnDefinition = "TEXT")
    private String detail;
    private int quantity;
    private int status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Collection collection;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    @ToString.Exclude
    private Set<OrderDetail> orderDetails = new HashSet<>();

    public Product() {
        this.status = EProductStatus.VAN_CON.getNumber();
    }

    public boolean isNew() {
        final var dateCompare = Instant.now().plus(7, ChronoUnit.DAYS);
        return super.createdDate.isBefore(dateCompare);
    }

    public double getPriceDiscount() {
        if (this.perCent <= 0) return 0;
        return this.unitPrice - (this.unitPrice * (this.perCent / 100.0));
    }
}
