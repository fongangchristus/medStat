package com.lightsoftit.medstat.repository;

import com.lightsoftit.medstat.domain.Etablissement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Etablissement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtablissementRepository extends JpaRepository<Etablissement, Long> {

}
