package org.caihaolun.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "staffpost")
public class Staffpost {

    @Id
    @Column(length = 64)
    private String id;

    @Column(length = 64)
    private String post;

    @Column(name = "`rank`", length = 16)
    private String rank;

    @Column(length = 64)
    private String payroll;

    private LocalDateTime entrytime;

    private Double salary;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPost() { return post; }
    public void setPost(String post) { this.post = post; }
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }
    public String getPayroll() { return payroll; }
    public void setPayroll(String payroll) { this.payroll = payroll; }
    public LocalDateTime getEntrytime() { return entrytime; }
    public void setEntrytime(LocalDateTime entrytime) { this.entrytime = entrytime; }
    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Staffpost)) return false;
        return Objects.equals(id, ((Staffpost) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
