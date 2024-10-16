package com.ufpb.sicred.repositories;

import com.ufpb.sicred.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
