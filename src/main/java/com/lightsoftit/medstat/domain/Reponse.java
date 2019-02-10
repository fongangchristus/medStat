package com.lightsoftit.medstat.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Reponse.
 */
@Entity
@Table(name = "reponse")
@Document(indexName = "reponse")
public class Reponse implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "libele", nullable = false)
    private String libele;

    @ManyToOne
    @JsonIgnoreProperties("reponses")
    private Question questionReponse;

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

    public Reponse libele(String libele) {
        this.libele = libele;
        return this;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public Question getQuestionReponse() {
        return questionReponse;
    }

    public Reponse questionReponse(Question question) {
        this.questionReponse = question;
        return this;
    }

    public void setQuestionReponse(Question question) {
        this.questionReponse = question;
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
        Reponse reponse = (Reponse) o;
        if (reponse.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reponse.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Reponse{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            "}";
    }
}
