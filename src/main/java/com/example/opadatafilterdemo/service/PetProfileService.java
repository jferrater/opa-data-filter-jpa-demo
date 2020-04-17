package com.example.opadatafilterdemo.service;

import com.example.opadatafilterdemo.entity.PetProfileEntity;
import com.example.opadatafilterdemo.model.Pet;
import com.example.opadatafilterdemo.repository.PetProfileRepository;
import com.github.jferrater.opa.ast.db.query.model.request.PartialRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author joffryferrater
 */
@Service
public class PetProfileService {

    private PetProfileRepository petProfileRepository;

    @Resource(name = "partialRequestGenerator")
    private PartialRequestGenerator partialRequestGenerator;

    public PetProfileService(PetProfileRepository petProfileRepository) {
        this.petProfileRepository = petProfileRepository;
    }

    public List<Pet> getPets(PartialRequest partialRequest) {
        return filterGetPets(partialRequest);
    }
    public List<Pet> getPets() {
        return filterGetPets(partialRequestGenerator.getPartialRequest());
    }

    private List<Pet> filterGetPets(PartialRequest partialRequest) {
        List<PetProfileEntity> pets = petProfileRepository.filterData(partialRequest);
        return pets.stream()
                .map(pet -> new Pet(pet.getName(), pet.getOwner(), pet.getVeterinarian(), pet.getClinic()))
                .collect(toList());
    }
}
