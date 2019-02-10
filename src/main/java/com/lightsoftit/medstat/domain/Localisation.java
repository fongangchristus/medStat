package com.lightsoftit.medstat.domain;



import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Localisation.
 */
@Entity
@Table(name = "localisation")
@Document(indexName = "localisation")
public class Localisation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "departement")
    private String departement;

    @Column(name = "ville")
    private String ville;

    @NotNull
    @Column(name = "district", nullable = false)
    private String district;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public Localisation region(String region) {
        this.region = region;
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDepartement() {
        return departement;
    }

    public Localisation departement(String departement) {
        this.departement = departement;
        return this;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getVille() {
        return ville;
    }

    public Localisation ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getDistrict() {
        return district;
    }

    public Localisation district(String district) {
        this.district = district;
        return this;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Localisation localisation = (Localisation) o;
        if (localisation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), localisation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Localisation{" +
            "id=" + getId() +
            ", region='" + getRegion() + "'" +
            ", departement='" + getDepartement() + "'" +
            ", ville='" + getVille() + "'" +
            ", district='" + getDistrict() + "'" +
            "}";
    }
}
