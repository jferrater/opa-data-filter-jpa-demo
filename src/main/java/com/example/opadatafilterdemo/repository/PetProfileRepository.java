package com.example.opadatafilterdemo.repository;

import com.example.opadatafilterdemo.entity.PetProfileEntity;
import com.github.jferrater.opa.data.filter.spring.boot.starter.OpaGenericDataFilterDao;
import org.springframework.stereotype.Repository;

/**
 * @author joffryferrater
 */
@Repository
public class PetProfileRepository extends OpaGenericDataFilterDao<PetProfileEntity> {

    public PetProfileRepository() {
        setClazz(PetProfileEntity.class);
    }
}
