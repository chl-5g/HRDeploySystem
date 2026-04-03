package org.caihaolun.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "dept")
public class Dept {

    @Id
    @Column(length = 64)
    private String id;

    @Column(length = 64)
    private String deptname;

    @Column(length = 64)
    private String deptid;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDeptname() { return deptname; }
    public void setDeptname(String deptname) { this.deptname = deptname; }
    public String getDeptid() { return deptid; }
    public void setDeptid(String deptid) { this.deptid = deptid; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dept)) return false;
        return Objects.equals(id, ((Dept) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
