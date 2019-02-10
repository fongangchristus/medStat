package com.lightsoftit.medstat.repository;

import com.lightsoftit.medstat.domain.Localisation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Localisation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocalisationRepository extends JpaRepository<Localisation, Long> {

}
