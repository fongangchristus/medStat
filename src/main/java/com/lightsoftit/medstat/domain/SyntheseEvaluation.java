package com.lightsoftit.medstat.domain;



import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SyntheseEvaluation.
 */
@Entity
@Table(name = "synthese_evaluation")
@Document(indexName = "syntheseevaluation")
public class SyntheseEvaluation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "composante", nullable = false)
    private String composante;

    @Column(name = "point_disponnible")
    private Double pointDisponnible;

    @Column(name = "nbr_indicateur_composite")
    private Double nbrIndicateurComposite;

    @Column(name = "pourcentage_obtenue")
    private Double pourcentageObtenue;

    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComposante() {
        return composante;
    }

    public SyntheseEvaluation composante(String composante) {
        this.composante = composante;
        return this;
    }

    public void setComposante(String composante) {
        this.composante = composante;
    }

    public Double getPointDisponnible() {
        return pointDisponnible;
    }

    public SyntheseEvaluation pointDisponnible(Double pointDisponnible) {
        this.pointDisponnible = pointDisponnible;
        return this;
    }

    public void setPointDisponnible(Double pointDisponnible) {
        this.pointDisponnible = pointDisponnible;
    }

    public Double getNbrIndicateurComposite() {
        return nbrIndicateurComposite;
    }

    public SyntheseEvaluation nbrIndicateurComposite(Double nbrIndicateurComposite) {
        this.nbrIndicateurComposite = nbrIndicateurComposite;
        return this;
    }

    public void setNbrIndicateurComposite(Double nbrIndicateurComposite) {
        this.nbrIndicateurComposite = nbrIndicateurComposite;
    }

    public Double getPourcentageObtenue() {
        return pourcentageObtenue;
    }

    public SyntheseEvaluation pourcentageObtenue(Double pourcentageObtenue) {
        this.pourcentageObtenue = pourcentageObtenue;
        return this;
    }

    public void setPourcentageObtenue(Double pourcentageObtenue) {
        this.pourcentageObtenue = pourcentageObtenue;
    }

    public String getDescription() {
        return description;
    }

    public SyntheseEvaluation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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
        SyntheseEvaluation syntheseEvaluation = (SyntheseEvaluation) o;
        if (syntheseEvaluation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), syntheseEvaluation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SyntheseEvaluation{" +
            "id=" + getId() +
            ", composante='" + getComposante() + "'" +
            ", pointDisponnible=" + getPointDisponnible() +
            ", nbrIndicateurComposite=" + getNbrIndicateurComposite() +
            ", pourcentageObtenue=" + getPourcentageObtenue() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
