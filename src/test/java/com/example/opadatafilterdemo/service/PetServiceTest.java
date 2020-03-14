package com.example.opadatafilterdemo.service;

import com.example.opadatafilterdemo.entity.PetEntity;
import com.example.opadatafilterdemo.model.Pet;
import com.example.opadatafilterdemo.repository.PetRepository;
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
class PetServiceTest {

    @MockBean
    PetRepository petRepository;

    @Autowired
    private PetService target;

    @Test
    void shouldFilterData() {
        PartialRequest partialRequest = mock(PartialRequest.class);
        PetEntity petEntity = petEntity();
        when(petRepository.filterData(partialRequest)).thenReturn(List.of(petEntity));

        List<Pet> results = target.getPets(partialRequest);

        assertThat(results.size(), is(1));
    }

    private PetEntity petEntity() {
        PetEntity petEntity = new PetEntity();
        petEntity.setId(1L);
        petEntity.setClinic("SOMA");
        petEntity.setName("fluffy");
        petEntity.setOwner("dodong");
        petEntity.setVeterinarian("alice");
        return petEntity;
    }
}