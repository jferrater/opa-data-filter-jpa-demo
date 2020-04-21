package com.example.opadatafilterdemo.repository;

import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.OpaDataFilterRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends OpaDataFilterRepository<PetProfileEntity, Long> {

}
