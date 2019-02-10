package com.lightsoftit.medstat.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Reponse entity.
 */
public class ReponseDTO implements Serializable {

    private Long id;

    @NotNull
    private String libele;


    private Long questionReponseId;

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

    public Long getQuestionReponseId() {
        return questionReponseId;
    }

    public void setQuestionReponseId(Long questionId) {
        this.questionReponseId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReponseDTO reponseDTO = (ReponseDTO) o;
        if (reponseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reponseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReponseDTO{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            ", questionReponse=" + getQuestionReponseId() +
            "}";
    }
}
