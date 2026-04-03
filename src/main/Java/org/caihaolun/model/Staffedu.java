package org.caihaolun.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "staffedu")
public class Staffedu {

    @Id
    @Column(length = 64)
    private String id;

    @Column(length = 32)
    private String category;

    @Column(length = 32)
    private String qual;

    @Column(length = 64)
    private String major;

    @Column(length = 64)
    private String graduate;

    private LocalDateTime time;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getQual() { return qual; }
    public void setQual(String qual) { this.qual = qual; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    public String getGraduate() { return graduate; }
    public void setGraduate(String graduate) { this.graduate = graduate; }
    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Staffedu)) return false;
        return Objects.equals(id, ((Staffedu) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
