package com.management.app.repository;

import com.management.app.domain.Departement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Departement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {}
