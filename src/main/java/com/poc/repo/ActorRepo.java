package com.poc.repo;

import com.poc.entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepo extends JpaRepository<Actor, Integer> {
    Actor getActorByActorId(Integer actorId);
}
