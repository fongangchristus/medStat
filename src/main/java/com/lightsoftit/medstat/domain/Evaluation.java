package com.lightsoftit.medstat.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Evaluation.
 */
@Entity
@Table(name = "evaluation")
@Document(indexName = "evaluation")
public class Evaluation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "lieu")
    private String lieu;

    @NotNull
    @Column(name = "note", nullable = false)
    private Double note;

    @Column(name = "date_evaluation")
    private Instant dateEvaluation;

    @OneToOne
    @JoinColumn(unique = true)
    private SyntheseEvaluation syntheseEvaluation;

    @ManyToOne
    @JsonIgnoreProperties("evaluations")
    private Medecin medecinEvaluation;

    @ManyToOne
    @JsonIgnoreProperties("evaluations")
    private Questionnaire questionnaireEvaluation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLieu() {
        return lieu;
    }

    public Evaluation lieu(String lieu) {
        this.lieu = lieu;
        return this;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Double getNote() {
        return note;
    }

    public Evaluation note(Double note) {
        this.note = note;
        return this;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    public Instant getDateEvaluation() {
        return dateEvaluation;
    }

    public Evaluation dateEvaluation(Instant dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
        return this;
    }

    public void setDateEvaluation(Instant dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }

    public SyntheseEvaluation getSyntheseEvaluation() {
        return syntheseEvaluation;
    }

    public Evaluation syntheseEvaluation(SyntheseEvaluation syntheseEvaluation) {
        this.syntheseEvaluation = syntheseEvaluation;
        return this;
    }

    public void setSyntheseEvaluation(SyntheseEvaluation syntheseEvaluation) {
        this.syntheseEvaluation = syntheseEvaluation;
    }

    public Medecin getMedecinEvaluation() {
        return medecinEvaluation;
    }

    public Evaluation medecinEvaluation(Medecin medecin) {
        this.medecinEvaluation = medecin;
        return this;
    }

    public void setMedecinEvaluation(Medecin medecin) {
        this.medecinEvaluation = medecin;
    }

    public Questionnaire getQuestionnaireEvaluation() {
        return questionnaireEvaluation;
    }

    public Evaluation questionnaireEvaluation(Questionnaire questionnaire) {
        this.questionnaireEvaluation = questionnaire;
        return this;
    }

    public void setQuestionnaireEvaluation(Questionnaire questionnaire) {
        this.questionnaireEvaluation = questionnaire;
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
        Evaluation evaluation = (Evaluation) o;
        if (evaluation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), evaluation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Evaluation{" +
            "id=" + getId() +
            ", lieu='" + getLieu() + "'" +
            ", note=" + getNote() +
            ", dateEvaluation='" + getDateEvaluation() + "'" +
            "}";
    }
}
