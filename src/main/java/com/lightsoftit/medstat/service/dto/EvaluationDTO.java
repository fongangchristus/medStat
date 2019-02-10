package com.lightsoftit.medstat.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Evaluation entity.
 */
public class EvaluationDTO implements Serializable {

    private Long id;

    private String lieu;

    @NotNull
    private Double note;

    private Instant dateEvaluation;


    private Long syntheseEvaluationId;

    private Long medecinEvaluationId;

    private Long questionnaireEvaluationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    public Instant getDateEvaluation() {
        return dateEvaluation;
    }

    public void setDateEvaluation(Instant dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }

    public Long getSyntheseEvaluationId() {
        return syntheseEvaluationId;
    }

    public void setSyntheseEvaluationId(Long syntheseEvaluationId) {
        this.syntheseEvaluationId = syntheseEvaluationId;
    }

    public Long getMedecinEvaluationId() {
        return medecinEvaluationId;
    }

    public void setMedecinEvaluationId(Long medecinId) {
        this.medecinEvaluationId = medecinId;
    }

    public Long getQuestionnaireEvaluationId() {
        return questionnaireEvaluationId;
    }

    public void setQuestionnaireEvaluationId(Long questionnaireId) {
        this.questionnaireEvaluationId = questionnaireId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EvaluationDTO evaluationDTO = (EvaluationDTO) o;
        if (evaluationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), evaluationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EvaluationDTO{" +
            "id=" + getId() +
            ", lieu='" + getLieu() + "'" +
            ", note=" + getNote() +
            ", dateEvaluation='" + getDateEvaluation() + "'" +
            ", syntheseEvaluation=" + getSyntheseEvaluationId() +
            ", medecinEvaluation=" + getMedecinEvaluationId() +
            ", questionnaireEvaluation=" + getQuestionnaireEvaluationId() +
            "}";
    }
}
