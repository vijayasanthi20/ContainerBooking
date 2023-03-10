package com.project.codingexcercise.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;
import com.project.codingexcercise.model.Bookings;
/**
 * @author vijayasanthi
 *
 */

@EnableReactiveCassandraRepositories
public interface BookingsRepository extends ReactiveCassandraRepository<Bookings,Long>{
	

}
