package com.lightsoftit.medstat.service.mapper;

import com.lightsoftit.medstat.domain.*;
import com.lightsoftit.medstat.service.dto.EtablissementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Etablissement and its DTO EtablissementDTO.
 */
@Mapper(componentModel = "spring", uses = {MedecinMapper.class, LocalisationMapper.class})
public interface EtablissementMapper extends EntityMapper<EtablissementDTO, Etablissement> {

    @Mapping(source = "medecin.id", target = "medecinId")
    @Mapping(source = "localisationEtablissement.id", target = "localisationEtablissementId")
    EtablissementDTO toDto(Etablissement etablissement);

    @Mapping(source = "medecinId", target = "medecin")
    @Mapping(source = "localisationEtablissementId", target = "localisationEtablissement")
    Etablissement toEntity(EtablissementDTO etablissementDTO);

    default Etablissement fromId(Long id) {
        if (id == null) {
            return null;
        }
        Etablissement etablissement = new Etablissement();
        etablissement.setId(id);
        return etablissement;
    }
}
