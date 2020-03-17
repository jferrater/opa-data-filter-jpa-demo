package com.example.opadatafilterdemo.controller;

import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author joffryferrater
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("mariadb")
class PetProfileControllerTestIT {

    private static final String DOCKER_COMPOSE_YML = "./docker-compose.yml";
    private static final String POLICY_ENDPOINT = "/v1/policies";
    private static final String MARIA_DB = "maria-database_1";
    private static final String OPA = "opa-server_1";
    private static final int MARIA_DB_PORT = 3306;
    private static final int OPA_PORT = 8181;


    @Autowired
    private MockMvc mockMvc;

    @ClassRule
    public static DockerComposeContainer environment = new DockerComposeContainer(new File(DOCKER_COMPOSE_YML))
            .withExposedService(MARIA_DB, MARIA_DB_PORT, Wait.forListeningPort())
            .withExposedService(OPA, OPA_PORT, Wait.forHttp(POLICY_ENDPOINT)
                    .forStatusCode(200))
            .withLocalCompose(true);

    @BeforeAll
    public static void start() {
        environment.start();
    }

    @AfterAll
    public static void stop() {
        environment.stop();
    }

    @DisplayName(
            "Use case: Only a pet owner can view the pet's profile he own" +

            "Given a running Open Policy Agent server with a policy ./petclinic_policy.rego" +
            "And a MariaDB with initial data from src/main/resources/sql/init.sql" +
            "When Bob, who owns Browny, views Browny's profile" +
            "And the response is OK with Browny's info"
    )
    @Test
    void petOwnerCanViewThePetProfileHeOwn() throws Exception {
        mockMvc.perform(get("/pets/browny?user=bob")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("browny")))
                .andExpect(jsonPath("$.owner", is("bob")))
                .andExpect(jsonPath("$.veterinarian", is("alice")))
                .andExpect(jsonPath("$.clinic", is("SOMA")));
    }

    @DisplayName(
            "Use case: Only a pet owner can view the pet's profile he own" +

            "Given a running Open Policy Agent server with a policy ./petclinic_policy.rego" +
            "And a MariaDB with initial data from src/main/resources/sql/init.sql" +
            "When Bob views Fluffy's profile" +
            "And the response is Not Found"
    )
    @Test
    void userCannotViewThePetHeDontOwn() throws Exception {
        mockMvc.perform(get("/pets/fluffy?user=bob")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @DisplayName(
            "Use case: Veterinarians can view the pet profiles assign to them from devices at the clinic" +

            "Given a running Open Policy Agent server with a policy ./petclinic_policy.rego" +
            "And a MariaDB with initial data from src/main/resources/sql/init.sql" +
            "When Alice, a veterinarian, get the list of pets assign to her" +
            "The response is Ok and the five pet profiles data assign to her"
    )
    @Test
    void veterinarianCanViewThePetProfilesAssignToThem() throws Exception {
        mockMvc.perform(get("/pets?user=alice&clinic_location=SOMA")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @DisplayName(
            "Use case: Veterinarians can view the pet profiles assign to them from devices at the clinic" +

            "Given a running Open Policy Agent server with a policy ./petclinic_policy.rego" +
            "And a MariaDB with initial data from src/main/resources/sql/init.sql" +
            "When Alice, a veterinarian, get the list of pets from VETE clinic" +
            "The response is Ok and the result is empty"
    )
    @Test
    void veterinariansCannotViewThePetProfilesTheyDontAssignedWith() throws Exception {
        mockMvc.perform(get("/pets?user=alice&clinic_location=VETE")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}