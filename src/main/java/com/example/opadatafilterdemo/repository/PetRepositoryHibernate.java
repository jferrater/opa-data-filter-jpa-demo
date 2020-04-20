package com.example.opadatafilterdemo.repository;

import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.hibernate.OpaGenericDataFilterDao;
import org.springframework.stereotype.Repository;

@Repository
public class PetRepositoryHibernate extends OpaGenericDataFilterDao<PetProfileEntity> {

    public PetRepositoryHibernate() {
        setClazz(PetProfileEntity.class);
    }
}
