package com.example.opadatafilterdemo.service;

import com.example.opadatafilterdemo.entity.PetEntity;
import com.example.opadatafilterdemo.model.Pet;
import com.example.opadatafilterdemo.repository.PetRepository;
import com.github.jferrater.opa.ast.to.sql.query.model.request.PartialRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author joffryferrater
 */
@Service
public class PetService {

    private PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getPets(PartialRequest partialRequest) {
        return filterGetPets(partialRequest);
    }

    private List<Pet> filterGetPets(PartialRequest partialRequest) {
        List<PetEntity> pets = petRepository.filterData(partialRequest);
        return pets.stream()
                .map(pet -> new Pet(pet.getName(), pet.getOwner(), pet.getVeterinarian(), pet.getClinic()))
                .collect(toList());
    }
}
