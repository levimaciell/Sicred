package com.ufpb.sicred.repositories;

import com.ufpb.sicred.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface EventRepository extends JpaRepository<User, Long> {
}
