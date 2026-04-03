package org.caihaolun.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "abilityasse")
public class Abilityasse {

    @Id
    @Column(length = 64)
    private String id;

    private Integer times;
    private Integer a;
    private Double b;
    private Double c;
    private Double d;
    private Double weight;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Integer getTimes() { return times; }
    public void setTimes(Integer times) { this.times = times; }
    public Integer getA() { return a; }
    public void setA(Integer a) { this.a = a; }
    public Double getB() { return b; }
    public void setB(Double b) { this.b = b; }
    public Double getC() { return c; }
    public void setC(Double c) { this.c = c; }
    public Double getD() { return d; }
    public void setD(Double d) { this.d = d; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Abilityasse)) return false;
        return Objects.equals(id, ((Abilityasse) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
