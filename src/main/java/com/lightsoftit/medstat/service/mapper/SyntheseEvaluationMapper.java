package com.lightsoftit.medstat.service.mapper;

import com.lightsoftit.medstat.domain.*;
import com.lightsoftit.medstat.service.dto.SyntheseEvaluationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SyntheseEvaluation and its DTO SyntheseEvaluationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SyntheseEvaluationMapper extends EntityMapper<SyntheseEvaluationDTO, SyntheseEvaluation> {



    default SyntheseEvaluation fromId(Long id) {
        if (id == null) {
            return null;
        }
        SyntheseEvaluation syntheseEvaluation = new SyntheseEvaluation();
        syntheseEvaluation.setId(id);
        return syntheseEvaluation;
    }
}
