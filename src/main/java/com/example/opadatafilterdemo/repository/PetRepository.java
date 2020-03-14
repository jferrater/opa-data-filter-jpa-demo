package com.example.opadatafilterdemo.repository;

import com.example.opadatafilterdemo.entity.PetEntity;
import com.github.jferrater.opa.data.filter.spring.boot.starter.OpaGenericDataFilterDao;
import org.springframework.stereotype.Repository;

/**
 * @author joffryferrater
 */
@Repository
public class PetRepository extends OpaGenericDataFilterDao<PetEntity> {

    public PetRepository() {
        setClazz(PetEntity.class);
    }
}
