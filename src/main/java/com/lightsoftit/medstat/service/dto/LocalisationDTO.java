package com.lightsoftit.medstat.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Localisation entity.
 */
public class LocalisationDTO implements Serializable {

    private Long id;

    @NotNull
    private String region;

    private String departement;

    private String ville;

    @NotNull
    private String district;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocalisationDTO localisationDTO = (LocalisationDTO) o;
        if (localisationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), localisationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LocalisationDTO{" +
            "id=" + getId() +
            ", region='" + getRegion() + "'" +
            ", departement='" + getDepartement() + "'" +
            ", ville='" + getVille() + "'" +
            ", district='" + getDistrict() + "'" +
            "}";
    }
}
