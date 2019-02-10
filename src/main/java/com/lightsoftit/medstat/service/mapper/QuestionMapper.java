package com.lightsoftit.medstat.service.mapper;

import com.lightsoftit.medstat.domain.*;
import com.lightsoftit.medstat.service.dto.QuestionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Question and its DTO QuestionDTO.
 */
@Mapper(componentModel = "spring", uses = {QuestionnaireMapper.class, CategorieMapper.class})
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {

    @Mapping(source = "questionnaireQuestion.id", target = "questionnaireQuestionId")
    @Mapping(source = "categorieQuestion.id", target = "categorieQuestionId")
    QuestionDTO toDto(Question question);

    @Mapping(source = "questionnaireQuestionId", target = "questionnaireQuestion")
    @Mapping(source = "categorieQuestionId", target = "categorieQuestion")
    Question toEntity(QuestionDTO questionDTO);

    default Question fromId(Long id) {
        if (id == null) {
            return null;
        }
        Question question = new Question();
        question.setId(id);
        return question;
    }
}
