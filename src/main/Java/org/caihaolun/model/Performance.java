package org.caihaolun.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "performance")
public class Performance {

    @Id
    @Column(length = 64)
    private String id;

    private LocalDateTime starttime;
    private LocalDateTime endtime;

    @Column(name = "ATTweight")
    private Double attWeight;

    @Column(name = "ABIweight")
    private Double abiWeight;

    @Column(name = "PROweight")
    private Double proWeight;

    @Column(name = "STUweight")
    private Double stuWeight;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public LocalDateTime getStarttime() { return starttime; }
    public void setStarttime(LocalDateTime starttime) { this.starttime = starttime; }
    public LocalDateTime getEndtime() { return endtime; }
    public void setEndtime(LocalDateTime endtime) { this.endtime = endtime; }
    public Double getAttWeight() { return attWeight; }
    public void setAttWeight(Double attWeight) { this.attWeight = attWeight; }
    public Double getAbiWeight() { return abiWeight; }
    public void setAbiWeight(Double abiWeight) { this.abiWeight = abiWeight; }
    public Double getProWeight() { return proWeight; }
    public void setProWeight(Double proWeight) { this.proWeight = proWeight; }
    public Double getStuWeight() { return stuWeight; }
    public void setStuWeight(Double stuWeight) { this.stuWeight = stuWeight; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Performance)) return false;
        return Objects.equals(id, ((Performance) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
