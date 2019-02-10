package com.lightsoftit.medstat.service.mapper;

import com.lightsoftit.medstat.domain.*;
import com.lightsoftit.medstat.service.dto.EvaluationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Evaluation and its DTO EvaluationDTO.
 */
@Mapper(componentModel = "spring", uses = {SyntheseEvaluationMapper.class, MedecinMapper.class, QuestionnaireMapper.class})
public interface EvaluationMapper extends EntityMapper<EvaluationDTO, Evaluation> {

    @Mapping(source = "syntheseEvaluation.id", target = "syntheseEvaluationId")
    @Mapping(source = "medecinEvaluation.id", target = "medecinEvaluationId")
    @Mapping(source = "questionnaireEvaluation.id", target = "questionnaireEvaluationId")
    EvaluationDTO toDto(Evaluation evaluation);

    @Mapping(source = "syntheseEvaluationId", target = "syntheseEvaluation")
    @Mapping(source = "medecinEvaluationId", target = "medecinEvaluation")
    @Mapping(source = "questionnaireEvaluationId", target = "questionnaireEvaluation")
    Evaluation toEntity(EvaluationDTO evaluationDTO);

    default Evaluation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Evaluation evaluation = new Evaluation();
        evaluation.setId(id);
        return evaluation;
    }
}
