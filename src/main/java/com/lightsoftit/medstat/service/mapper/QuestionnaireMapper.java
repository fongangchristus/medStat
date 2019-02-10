package com.lightsoftit.medstat.service.mapper;

import com.lightsoftit.medstat.domain.*;
import com.lightsoftit.medstat.service.dto.QuestionnaireDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Questionnaire and its DTO QuestionnaireDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuestionnaireMapper extends EntityMapper<QuestionnaireDTO, Questionnaire> {



    default Questionnaire fromId(Long id) {
        if (id == null) {
            return null;
        }
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setId(id);
        return questionnaire;
    }
}
