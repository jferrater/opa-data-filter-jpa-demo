package com.example.opadatafilterdemo.repository;

import com.github.jferrater.opadatafilterjpaspringbootstarter.repository.OpaDataFilterRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends OpaDataFilterRepository<PetProfileEntity, Long> {

}
