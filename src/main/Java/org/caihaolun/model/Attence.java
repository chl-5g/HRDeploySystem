package org.caihaolun.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "attence")
public class Attence {

    @Id
    @Column(length = 64)
    private String id;

    @Column(length = 64)
    private String deptid;

    private LocalDateTime come;

    @Column(name = "`off`")
    private LocalDateTime off;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDeptid() { return deptid; }
    public void setDeptid(String deptid) { this.deptid = deptid; }
    public LocalDateTime getCome() { return come; }
    public void setCome(LocalDateTime come) { this.come = come; }
    public LocalDateTime getOff() { return off; }
    public void setOff(LocalDateTime off) { this.off = off; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attence)) return false;
        return Objects.equals(id, ((Attence) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
