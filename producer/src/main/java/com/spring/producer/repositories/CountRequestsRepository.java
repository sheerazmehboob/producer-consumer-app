package com.spring.producer.repositories;

import com.spring.producer.entities.CountRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountRequestsRepository extends JpaRepository <CountRequests, Long>{
}
