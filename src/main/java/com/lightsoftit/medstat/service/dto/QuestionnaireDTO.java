package com.lightsoftit.medstat.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Questionnaire entity.
 */
public class QuestionnaireDTO implements Serializable {

    private Long id;

    private String type;

    private String description;

    @NotNull
    private String libele;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestionnaireDTO questionnaireDTO = (QuestionnaireDTO) o;
        if (questionnaireDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), questionnaireDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuestionnaireDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", libele='" + getLibele() + "'" +
            "}";
    }
}
