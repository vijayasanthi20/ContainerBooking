package com.project.codingexcercise.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.project.codingexcercise.model.Bookings;
@EnableReactiveCassandraRepositories
public interface BookingsRepository extends ReactiveCassandraRepository<Bookings,Long>{
	

}
