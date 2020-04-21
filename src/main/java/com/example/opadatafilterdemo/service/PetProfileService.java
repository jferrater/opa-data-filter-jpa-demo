package com.example.opadatafilterdemo.service;

import com.example.opadatafilterdemo.model.Pet;
import com.example.opadatafilterdemo.repository.PetProfileEntity;
import com.example.opadatafilterdemo.repository.PetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public PetProfileService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getPetProfiles() {
        List<PetProfileEntity> all = petRepository.findAll();
        LOGGER.info("size of pets lists: {}", all.size());
        return all.stream()
                .map(pet -> new Pet(pet.getName(), pet.getOwner(), pet.getVeterinarian(), pet.getClinic()))
                .collect(toList());
    }
}
