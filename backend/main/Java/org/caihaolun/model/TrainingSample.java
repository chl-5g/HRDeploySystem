package org.caihaolun.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "training_sample")
public class TrainingSample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32, nullable = false)
    private String category;

    @Column(length = 16, nullable = false)
    private String qual;

    @Column(name = "major_bucket", length = 16, nullable = false)
    private String majorBucket;

    @Column(name = "graduate_tier", length = 16, nullable = false)
    private String graduateTier;

    @Column(length = 64, nullable = false)
    private String label;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getQual() { return qual; }
    public void setQual(String qual) { this.qual = qual; }
    public String getMajorBucket() { return majorBucket; }
    public void setMajorBucket(String majorBucket) { this.majorBucket = majorBucket; }
    public String getGraduateTier() { return graduateTier; }
    public void setGraduateTier(String graduateTier) { this.graduateTier = graduateTier; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrainingSample)) return false;
        return id != null && id.equals(((TrainingSample) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
