package com.example.opadatafilterdemo.repository;

import com.example.opadatafilterdemo.entity.PetEntity;
import com.github.jferrater.opa.ast.to.sql.query.model.request.PartialRequest;
import com.github.jferrater.opa.ast.to.sql.query.service.OpaClientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author joffryferrater
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class PetRepositoryTest {

    private static final String PET_OWNER_QUERY = "SELECT * FROM pets WHERE (pets.owner = 'bob');";

    @MockBean
    OpaClientService mockOpaClientService;

    @Autowired
    private PetRepository target;

    @DisplayName(
            "Given an H2 database with initial data from src/main/resources/init.sql"
    )
    @Test
    @Transactional
    void petOwnerShouldBeAbleToAccessPetsProfile() {
        PartialRequest partialRequest = mock(PartialRequest.class);
        when(mockOpaClientService.getExecutableSqlStatements(partialRequest)).thenReturn(PET_OWNER_QUERY);

        List<PetEntity> petEntities = target.filterData(partialRequest);

        assertThat(petEntities.size(), is(1));
        assertThat(petEntities.get(0).getOwner(), is("bob"));
    }

}