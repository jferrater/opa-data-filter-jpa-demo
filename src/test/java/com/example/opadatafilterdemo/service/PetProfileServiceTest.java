package com.example.opadatafilterdemo.service;

import com.example.opadatafilterdemo.entity.PetProfileEntity;
import com.example.opadatafilterdemo.model.Pet;
import com.example.opadatafilterdemo.repository.PetProfileRepository;
import com.github.jferrater.opa.ast.to.sql.query.model.request.PartialRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class PetProfileServiceTest {

    @MockBean
    PetProfileRepository petProfileRepository;

    @Autowired
    private PetProfileService target;

    @Test
    void shouldFilterData() {
        PartialRequest partialRequest = mock(PartialRequest.class);
        PetProfileEntity petProfileEntity = petEntity();
        when(petProfileRepository.filterData(partialRequest)).thenReturn(List.of(petProfileEntity));

        List<Pet> results = target.getPets(partialRequest);

        assertThat(results.size(), is(1));
    }

    private PetProfileEntity petEntity() {
        PetProfileEntity petProfileEntity = new PetProfileEntity();
        petProfileEntity.setId(1L);
        petProfileEntity.setClinic("SOMA");
        petProfileEntity.setName("fluffy");
        petProfileEntity.setOwner("dodong");
        petProfileEntity.setVeterinarian("alice");
        return petProfileEntity;
    }
}