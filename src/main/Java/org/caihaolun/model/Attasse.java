package org.caihaolun.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "attasse")
public class Attasse {

    @Id
    @Column(length = 64)
    private String id;

    private Integer g;
    private Integer h;
    private Double i;
    private Integer f;
    private Double weight;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Integer getG() { return g; }
    public void setG(Integer g) { this.g = g; }
    public Integer getH() { return h; }
    public void setH(Integer h) { this.h = h; }
    public Double getI() { return i; }
    public void setI(Double i) { this.i = i; }
    public Integer getF() { return f; }
    public void setF(Integer f) { this.f = f; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attasse)) return false;
        return Objects.equals(id, ((Attasse) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
