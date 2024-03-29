package com.slowv.fruit.domain;

import com.slowv.fruit.domain.enums.ECategoryStatus;
import lombok.Data;
import org.hibernate.Hibernate;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@RedisHash
public class Collection extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String images;
    private long collectionParentId;
    private boolean isParent;
    private int status;

    @OneToMany(mappedBy = "collection")
    private Set<Product> products;

    public Collection() {
    }

    public Collection(String name, String description, String images, boolean isParent) {
        this.name = name;
        this.description = description;
        this.images = images;
        this.status = ECategoryStatus.HOAT_DONG.getNumber();
        this.isParent = isParent;
    }

    public Collection(String name, String description, String images, boolean isParent, long collectionParentId) {
        long now = Calendar.getInstance().getTimeInMillis();
        this.name = name;
        this.description = description;
        this.images = images;
        this.status = ECategoryStatus.HOAT_DONG.getNumber();
        this.isParent = isParent;
        this.collectionParentId = collectionParentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Collection that = (Collection) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 177877382;
    }
}
