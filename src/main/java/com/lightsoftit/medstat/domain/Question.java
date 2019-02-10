package com.lightsoftit.medstat.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * The Employee entity.
 */
@Entity
@Table(name = "question")
@Document(indexName = "question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libele", nullable = false)
    private String libele;

    @Column(name = "jhi_type")
    private String type;

    @ManyToOne
    @JsonIgnoreProperties("questions")
    private Questionnaire questionnaireQuestion;

    @ManyToOne
    @JsonIgnoreProperties("questions")
    private Categorie categorieQuestion;

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

    public Question libele(String libele) {
        this.libele = libele;
        return this;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getType() {
        return type;
    }

    public Question type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Questionnaire getQuestionnaireQuestion() {
        return questionnaireQuestion;
    }

    public Question questionnaireQuestion(Questionnaire questionnaire) {
        this.questionnaireQuestion = questionnaire;
        return this;
    }

    public void setQuestionnaireQuestion(Questionnaire questionnaire) {
        this.questionnaireQuestion = questionnaire;
    }

    public Categorie getCategorieQuestion() {
        return categorieQuestion;
    }

    public Question categorieQuestion(Categorie categorie) {
        this.categorieQuestion = categorie;
        return this;
    }

    public void setCategorieQuestion(Categorie categorie) {
        this.categorieQuestion = categorie;
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
        Question question = (Question) o;
        if (question.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), question.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
