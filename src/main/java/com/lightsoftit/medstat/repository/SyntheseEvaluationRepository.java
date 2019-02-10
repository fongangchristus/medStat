package com.lightsoftit.medstat.repository;

import com.lightsoftit.medstat.domain.SyntheseEvaluation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SyntheseEvaluation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SyntheseEvaluationRepository extends JpaRepository<SyntheseEvaluation, Long> {

}
