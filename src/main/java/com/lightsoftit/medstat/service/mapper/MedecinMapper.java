package com.lightsoftit.medstat.service.mapper;

import com.lightsoftit.medstat.domain.*;
import com.lightsoftit.medstat.service.dto.MedecinDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Medecin and its DTO MedecinDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MedecinMapper extends EntityMapper<MedecinDTO, Medecin> {



    default Medecin fromId(Long id) {
        if (id == null) {
            return null;
        }
        Medecin medecin = new Medecin();
        medecin.setId(id);
        return medecin;
    }
}
