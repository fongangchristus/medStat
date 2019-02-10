package com.lightsoftit.medstat.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.lightsoftit.medstat.domain.enumeration.TypeEtab;

/**
 * A DTO for the Etablissement entity.
 */
public class EtablissementDTO implements Serializable {

    private Long id;

    @NotNull
    private String libele;

    private String description;

    @NotNull
    private TypeEtab type;

    private Double nbrMedecin;

    private Double nbrPersonnelSfIsIdeTms;

    private Double nbrPersonnelIbIbaTal;

    private Double nbrPersonnelAsAtms;

    private Double nbrPersonnelAdministratif;

    private Double nbrPersonnelNomQualifie;

    private Double ratioPersonnelQualifieparHab;

    private Double nbrLitparHabitant;

    private Double penta1;

    private Double penta3;

    private Double difPenta1Et3;


    private Long medecinId;

    private Long localisationEtablissementId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeEtab getType() {
        return type;
    }

    public void setType(TypeEtab type) {
        this.type = type;
    }

    public Double getNbrMedecin() {
        return nbrMedecin;
    }

    public void setNbrMedecin(Double nbrMedecin) {
        this.nbrMedecin = nbrMedecin;
    }

    public Double getNbrPersonnelSfIsIdeTms() {
        return nbrPersonnelSfIsIdeTms;
    }

    public void setNbrPersonnelSfIsIdeTms(Double nbrPersonnelSfIsIdeTms) {
        this.nbrPersonnelSfIsIdeTms = nbrPersonnelSfIsIdeTms;
    }

    public Double getNbrPersonnelIbIbaTal() {
        return nbrPersonnelIbIbaTal;
    }

    public void setNbrPersonnelIbIbaTal(Double nbrPersonnelIbIbaTal) {
        this.nbrPersonnelIbIbaTal = nbrPersonnelIbIbaTal;
    }

    public Double getNbrPersonnelAsAtms() {
        return nbrPersonnelAsAtms;
    }

    public void setNbrPersonnelAsAtms(Double nbrPersonnelAsAtms) {
        this.nbrPersonnelAsAtms = nbrPersonnelAsAtms;
    }

    public Double getNbrPersonnelAdministratif() {
        return nbrPersonnelAdministratif;
    }

    public void setNbrPersonnelAdministratif(Double nbrPersonnelAdministratif) {
        this.nbrPersonnelAdministratif = nbrPersonnelAdministratif;
    }

    public Double getNbrPersonnelNomQualifie() {
        return nbrPersonnelNomQualifie;
    }

    public void setNbrPersonnelNomQualifie(Double nbrPersonnelNomQualifie) {
        this.nbrPersonnelNomQualifie = nbrPersonnelNomQualifie;
    }

    public Double getRatioPersonnelQualifieparHab() {
        return ratioPersonnelQualifieparHab;
    }

    public void setRatioPersonnelQualifieparHab(Double ratioPersonnelQualifieparHab) {
        this.ratioPersonnelQualifieparHab = ratioPersonnelQualifieparHab;
    }

    public Double getNbrLitparHabitant() {
        return nbrLitparHabitant;
    }

    public void setNbrLitparHabitant(Double nbrLitparHabitant) {
        this.nbrLitparHabitant = nbrLitparHabitant;
    }

    public Double getPenta1() {
        return penta1;
    }

    public void setPenta1(Double penta1) {
        this.penta1 = penta1;
    }

    public Double getPenta3() {
        return penta3;
    }

    public void setPenta3(Double penta3) {
        this.penta3 = penta3;
    }

    public Double getDifPenta1Et3() {
        return difPenta1Et3;
    }

    public void setDifPenta1Et3(Double difPenta1Et3) {
        this.difPenta1Et3 = difPenta1Et3;
    }

    public Long getMedecinId() {
        return medecinId;
    }

    public void setMedecinId(Long medecinId) {
        this.medecinId = medecinId;
    }

    public Long getLocalisationEtablissementId() {
        return localisationEtablissementId;
    }

    public void setLocalisationEtablissementId(Long localisationId) {
        this.localisationEtablissementId = localisationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EtablissementDTO etablissementDTO = (EtablissementDTO) o;
        if (etablissementDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), etablissementDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EtablissementDTO{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", nbrMedecin=" + getNbrMedecin() +
            ", nbrPersonnelSfIsIdeTms=" + getNbrPersonnelSfIsIdeTms() +
            ", nbrPersonnelIbIbaTal=" + getNbrPersonnelIbIbaTal() +
            ", nbrPersonnelAsAtms=" + getNbrPersonnelAsAtms() +
            ", nbrPersonnelAdministratif=" + getNbrPersonnelAdministratif() +
            ", nbrPersonnelNomQualifie=" + getNbrPersonnelNomQualifie() +
            ", ratioPersonnelQualifieparHab=" + getRatioPersonnelQualifieparHab() +
            ", nbrLitparHabitant=" + getNbrLitparHabitant() +
            ", penta1=" + getPenta1() +
            ", penta3=" + getPenta3() +
            ", difPenta1Et3=" + getDifPenta1Et3() +
            ", medecin=" + getMedecinId() +
            ", localisationEtablissement=" + getLocalisationEtablissementId() +
            "}";
    }
}
