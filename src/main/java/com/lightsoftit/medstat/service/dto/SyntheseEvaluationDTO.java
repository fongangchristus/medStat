package com.lightsoftit.medstat.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SyntheseEvaluation entity.
 */
public class SyntheseEvaluationDTO implements Serializable {

    private Long id;

    @NotNull
    private String composante;

    private Double pointDisponnible;

    private Double nbrIndicateurComposite;

    private Double pourcentageObtenue;

    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComposante() {
        return composante;
    }

    public void setComposante(String composante) {
        this.composante = composante;
    }

    public Double getPointDisponnible() {
        return pointDisponnible;
    }

    public void setPointDisponnible(Double pointDisponnible) {
        this.pointDisponnible = pointDisponnible;
    }

    public Double getNbrIndicateurComposite() {
        return nbrIndicateurComposite;
    }

    public void setNbrIndicateurComposite(Double nbrIndicateurComposite) {
        this.nbrIndicateurComposite = nbrIndicateurComposite;
    }

    public Double getPourcentageObtenue() {
        return pourcentageObtenue;
    }

    public void setPourcentageObtenue(Double pourcentageObtenue) {
        this.pourcentageObtenue = pourcentageObtenue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SyntheseEvaluationDTO syntheseEvaluationDTO = (SyntheseEvaluationDTO) o;
        if (syntheseEvaluationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), syntheseEvaluationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SyntheseEvaluationDTO{" +
            "id=" + getId() +
            ", composante='" + getComposante() + "'" +
            ", pointDisponnible=" + getPointDisponnible() +
            ", nbrIndicateurComposite=" + getNbrIndicateurComposite() +
            ", pourcentageObtenue=" + getPourcentageObtenue() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
