package com.lightsoftit.medstat.service.mapper;

import com.lightsoftit.medstat.domain.*;
import com.lightsoftit.medstat.service.dto.LocalisationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Localisation and its DTO LocalisationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LocalisationMapper extends EntityMapper<LocalisationDTO, Localisation> {



    default Localisation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Localisation localisation = new Localisation();
        localisation.setId(id);
        return localisation;
    }
}
