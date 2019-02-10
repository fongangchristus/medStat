package com.lightsoftit.medstat.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import com.lightsoftit.medstat.domain.enumeration.TypeEtab;

/**
 * A Etablissement.
 */
@Entity
@Table(name = "etablissement")
@Document(indexName = "etablissement")
public class Etablissement implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libele", nullable = false)
    private String libele;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private TypeEtab type;

    @Column(name = "nbr_medecin")
    private Double nbrMedecin;

    @Column(name = "nbr_personnel_sf_is_ide_tms")
    private Double nbrPersonnelSfIsIdeTms;

    @Column(name = "nbr_personnel_ib_iba_tal")
    private Double nbrPersonnelIbIbaTal;

    @Column(name = "nbr_personnel_as_atms")
    private Double nbrPersonnelAsAtms;

    @Column(name = "nbr_personnel_administratif")
    private Double nbrPersonnelAdministratif;

    @Column(name = "nbr_personnel_nom_qualifie")
    private Double nbrPersonnelNomQualifie;

    @Column(name = "ratio_personnel_qualifiepar_hab")
    private Double ratioPersonnelQualifieparHab;

    @Column(name = "nbr_litpar_habitant")
    private Double nbrLitparHabitant;

    @Column(name = "penta_1")
    private Double penta1;

    @Column(name = "penta_3")
    private Double penta3;

    @Column(name = "dif_penta_1_et_3")
    private Double difPenta1Et3;

    @OneToOne
    @JoinColumn(unique = true)
    private Medecin medecin;

    @ManyToOne
    @JsonIgnoreProperties("etablissements")
    private Localisation localisationEtablissement;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibele() {
        return libele;
    }

    public Etablissement libele(String libele) {
        this.libele = libele;
        return this;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getDescription() {
        return description;
    }

    public Etablissement description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeEtab getType() {
        return type;
    }

    public Etablissement type(TypeEtab type) {
        this.type = type;
        return this;
    }

    public void setType(TypeEtab type) {
        this.type = type;
    }

    public Double getNbrMedecin() {
        return nbrMedecin;
    }

    public Etablissement nbrMedecin(Double nbrMedecin) {
        this.nbrMedecin = nbrMedecin;
        return this;
    }

    public void setNbrMedecin(Double nbrMedecin) {
        this.nbrMedecin = nbrMedecin;
    }

    public Double getNbrPersonnelSfIsIdeTms() {
        return nbrPersonnelSfIsIdeTms;
    }

    public Etablissement nbrPersonnelSfIsIdeTms(Double nbrPersonnelSfIsIdeTms) {
        this.nbrPersonnelSfIsIdeTms = nbrPersonnelSfIsIdeTms;
        return this;
    }

    public void setNbrPersonnelSfIsIdeTms(Double nbrPersonnelSfIsIdeTms) {
        this.nbrPersonnelSfIsIdeTms = nbrPersonnelSfIsIdeTms;
    }

    public Double getNbrPersonnelIbIbaTal() {
        return nbrPersonnelIbIbaTal;
    }

    public Etablissement nbrPersonnelIbIbaTal(Double nbrPersonnelIbIbaTal) {
        this.nbrPersonnelIbIbaTal = nbrPersonnelIbIbaTal;
        return this;
    }

    public void setNbrPersonnelIbIbaTal(Double nbrPersonnelIbIbaTal) {
        this.nbrPersonnelIbIbaTal = nbrPersonnelIbIbaTal;
    }

    public Double getNbrPersonnelAsAtms() {
        return nbrPersonnelAsAtms;
    }

    public Etablissement nbrPersonnelAsAtms(Double nbrPersonnelAsAtms) {
        this.nbrPersonnelAsAtms = nbrPersonnelAsAtms;
        return this;
    }

    public void setNbrPersonnelAsAtms(Double nbrPersonnelAsAtms) {
        this.nbrPersonnelAsAtms = nbrPersonnelAsAtms;
    }

    public Double getNbrPersonnelAdministratif() {
        return nbrPersonnelAdministratif;
    }

    public Etablissement nbrPersonnelAdministratif(Double nbrPersonnelAdministratif) {
        this.nbrPersonnelAdministratif = nbrPersonnelAdministratif;
        return this;
    }

    public void setNbrPersonnelAdministratif(Double nbrPersonnelAdministratif) {
        this.nbrPersonnelAdministratif = nbrPersonnelAdministratif;
    }

    public Double getNbrPersonnelNomQualifie() {
        return nbrPersonnelNomQualifie;
    }

    public Etablissement nbrPersonnelNomQualifie(Double nbrPersonnelNomQualifie) {
        this.nbrPersonnelNomQualifie = nbrPersonnelNomQualifie;
        return this;
    }

    public void setNbrPersonnelNomQualifie(Double nbrPersonnelNomQualifie) {
        this.nbrPersonnelNomQualifie = nbrPersonnelNomQualifie;
    }

    public Double getRatioPersonnelQualifieparHab() {
        return ratioPersonnelQualifieparHab;
    }

    public Etablissement ratioPersonnelQualifieparHab(Double ratioPersonnelQualifieparHab) {
        this.ratioPersonnelQualifieparHab = ratioPersonnelQualifieparHab;
        return this;
    }

    public void setRatioPersonnelQualifieparHab(Double ratioPersonnelQualifieparHab) {
        this.ratioPersonnelQualifieparHab = ratioPersonnelQualifieparHab;
    }

    public Double getNbrLitparHabitant() {
        return nbrLitparHabitant;
    }

    public Etablissement nbrLitparHabitant(Double nbrLitparHabitant) {
        this.nbrLitparHabitant = nbrLitparHabitant;
        return this;
    }

    public void setNbrLitparHabitant(Double nbrLitparHabitant) {
        this.nbrLitparHabitant = nbrLitparHabitant;
    }

    public Double getPenta1() {
        return penta1;
    }

    public Etablissement penta1(Double penta1) {
        this.penta1 = penta1;
        return this;
    }

    public void setPenta1(Double penta1) {
        this.penta1 = penta1;
    }

    public Double getPenta3() {
        return penta3;
    }

    public Etablissement penta3(Double penta3) {
        this.penta3 = penta3;
        return this;
    }

    public void setPenta3(Double penta3) {
        this.penta3 = penta3;
    }

    public Double getDifPenta1Et3() {
        return difPenta1Et3;
    }

    public Etablissement difPenta1Et3(Double difPenta1Et3) {
        this.difPenta1Et3 = difPenta1Et3;
        return this;
    }

    public void setDifPenta1Et3(Double difPenta1Et3) {
        this.difPenta1Et3 = difPenta1Et3;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public Etablissement medecin(Medecin medecin) {
        this.medecin = medecin;
        return this;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public Localisation getLocalisationEtablissement() {
        return localisationEtablissement;
    }

    public Etablissement localisationEtablissement(Localisation localisation) {
        this.localisationEtablissement = localisation;
        return this;
    }

    public void setLocalisationEtablissement(Localisation localisation) {
        this.localisationEtablissement = localisation;
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
        Etablissement etablissement = (Etablissement) o;
        if (etablissement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), etablissement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Etablissement{" +
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
            "}";
    }
}
