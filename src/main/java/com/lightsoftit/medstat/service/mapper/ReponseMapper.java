package com.lightsoftit.medstat.service.mapper;

import com.lightsoftit.medstat.domain.*;
import com.lightsoftit.medstat.service.dto.ReponseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Reponse and its DTO ReponseDTO.
 */
@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface ReponseMapper extends EntityMapper<ReponseDTO, Reponse> {

    @Mapping(source = "questionReponse.id", target = "questionReponseId")
    ReponseDTO toDto(Reponse reponse);

    @Mapping(source = "questionReponseId", target = "questionReponse")
    Reponse toEntity(ReponseDTO reponseDTO);

    default Reponse fromId(Long id) {
        if (id == null) {
            return null;
        }
        Reponse reponse = new Reponse();
        reponse.setId(id);
        return reponse;
    }
}
