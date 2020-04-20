package com.example.opadatafilterdemo.service;

import com.example.opadatafilterdemo.model.Pet;
import com.example.opadatafilterdemo.repository.PetProfileEntity;
import com.example.opadatafilterdemo.repository.PetRepository;
import com.example.opadatafilterdemo.repository.PetRepositoryHibernate;
import com.github.jferrater.opa.ast.db.query.service.OpaClientService;
import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.OpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author joffryferrater
 */
@Service
public class PetProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetProfileService.class);

    private PetRepository petRepository;
    private PetRepositoryHibernate petRepositoryHibernate;
    private OpaClientService<PetProfileEntity> opaClientService;
    @Autowired
    ApplicationContext applicationContext;

    public PetProfileService(PetRepository petRepository, PetRepositoryHibernate petRepositoryHibernate, OpaClientService<PetProfileEntity> opaClientService) {
        this.petRepository = petRepository;
        this.petRepositoryHibernate = petRepositoryHibernate;
        this.petRepositoryHibernate = petRepositoryHibernate;
        this.opaClientService = opaClientService;
    }

    public List<Pet> getLovelyPets() {
        LOGGER.info("getLovelyPets()");
        OpaRepository bean = applicationContext.getBean(OpaRepository.class);
        LOGGER.info("opa repository bean: {}", bean);
        LOGGER.info(opaClientService.getExecutableSqlStatements());
        List<PetProfileEntity> all = petRepository.findAll();
        LOGGER.info("size of pets lists: {}", all.size());
        return all.stream()
                .map(pet -> new Pet(pet.getName(), pet.getOwner(), pet.getVeterinarian(), pet.getClinic()))
                .collect(toList());
    }

    public List<Pet> getPetsHibernate() {
        List<PetProfileEntity> petProfileEntities = petRepositoryHibernate.filterData();
        return petProfileEntities.stream()
                .map(pet -> new Pet(pet.getName(), pet.getOwner(), pet.getVeterinarian(), pet.getClinic()))
                .collect(toList());
    }
}
