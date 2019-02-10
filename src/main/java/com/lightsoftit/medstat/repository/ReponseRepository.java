package com.lightsoftit.medstat.repository;

import com.lightsoftit.medstat.domain.Reponse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Reponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReponseRepository extends JpaRepository<Reponse, Long> {

}
