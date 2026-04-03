package org.caihaolun.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @Column(length = 64)
    private String id;

    @Column(length = 64)
    private String name;

    @Column(length = 8)
    private String sex;

    private LocalDateTime birth;

    @Column(length = 32)
    private String nation;

    @Column(length = 128)
    private String locate;

    @Column(name = "IDnum", length = 32)
    private String idNum;

    @Column(name = "Tel", length = 32)
    private String tel;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public LocalDateTime getBirth() { return birth; }
    public void setBirth(LocalDateTime birth) { this.birth = birth; }
    public String getNation() { return nation; }
    public void setNation(String nation) { this.nation = nation; }
    public String getLocate() { return locate; }
    public void setLocate(String locate) { this.locate = locate; }
    public String getIdNum() { return idNum; }
    public void setIdNum(String idNum) { this.idNum = idNum; }
    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Staff)) return false;
        return Objects.equals(id, ((Staff) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
