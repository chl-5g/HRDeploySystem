package org.caihaolun.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "productasse")
public class Productasse {

    @Id
    @Column(length = 64)
    private String id;

    private Double e;
    private Double weight;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Double getE() { return e; }
    public void setE(Double e) { this.e = e; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Productasse)) return false;
        return Objects.equals(id, ((Productasse) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
