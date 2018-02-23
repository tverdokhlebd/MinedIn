package com.mining.profit.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mining.profit.db.model.Configuration;

/**
 * Repository for working with configuration model.
 *
 * @author Dmitry Tverdokhleb
 *
 */
@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

}
