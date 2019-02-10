package com.lightsoftit.medstat.service.dto;
import io.swagger.annotations.ApiModel;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Question entity.
 */
@ApiModel(description = "The Employee entity.")
public class QuestionDTO implements Serializable {

    private Long id;

    @NotNull
    private String libele;

    private String type;


    private Long questionnaireQuestionId;

    private Long categorieQuestionId;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getQuestionnaireQuestionId() {
        return questionnaireQuestionId;
    }

    public void setQuestionnaireQuestionId(Long questionnaireId) {
        this.questionnaireQuestionId = questionnaireId;
    }

    public Long getCategorieQuestionId() {
        return categorieQuestionId;
    }

    public void setCategorieQuestionId(Long categorieId) {
        this.categorieQuestionId = categorieId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestionDTO questionDTO = (QuestionDTO) o;
        if (questionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), questionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            ", type='" + getType() + "'" +
            ", questionnaireQuestion=" + getQuestionnaireQuestionId() +
            ", categorieQuestion=" + getCategorieQuestionId() +
            "}";
    }
}
