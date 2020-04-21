package com.example.opadatafilterdemo.controller;

import com.example.opadatafilterdemo.model.Pet;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author joffryferrater
 */
class PetProfileControllerTestIT {

    private static final String DOCKER_COMPOSE_YML = "./docker-compose.yml";
    private static final String POLICY_ENDPOINT = "/v1/policies";
    private static final String MARIA_DB = "maria-database_1";
    private static final int MARIA_DB_PORT = 3306;
    private static final String OPA_SERVER = "opa-server_1";
    private static final int OPA_SERVER_PORT = 8181;
    private static final String PET_PROFILES_SERVICE = "petprofiles-service";
    private static final int PET_PROFILES_SERVICE_PORT = 8081;
    private static final String HELLO_API = "/hello";

    @ClassRule
    public static DockerComposeContainer environment = new DockerComposeContainer(new File(DOCKER_COMPOSE_YML))
            .withExposedService(MARIA_DB, MARIA_DB_PORT, Wait.forListeningPort())
            .withExposedService(OPA_SERVER, OPA_SERVER_PORT, Wait.forHttp(POLICY_ENDPOINT))
            .withExposedService(PET_PROFILES_SERVICE, PET_PROFILES_SERVICE_PORT, Wait.forHttp(HELLO_API)
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
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic Ym9iOnBhc3N3b3Jk"); //The user is bob, the pet owner of the pet with name browny
        httpHeaders.set("X-ORG-HEADER", "SOMA"); //The name of the clinic is in the X-ORG-HEADER
        Pet[] result = get(httpHeaders);
        assertThat(result.length, is(1));
        Pet pet = result[0];
        assertThat(pet.getName(), is("browny"));
        assertThat(pet.getOwner(), is("bob"));
        assertThat(pet.getVeterinarian(), is("alice"));
        assertThat(pet.getClinic(), is("SOMA"));
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
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic YWxpY2U6cGFzc3dvcmQ="); //The user is alice, veterinarian
        httpHeaders.set("X-ORG-HEADER", "SOMA"); //The name of the clinic is in the X-ORG-HEADER
        Pet[] result = get(httpHeaders);
        assertThat(result.length, is(5));
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
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic YWxpY2U6cGFzc3dvcmQ="); //The user is alice, veterinarian
        httpHeaders.set("X-ORG-HEADER", "VETE"); //The name of the clinic is in the X-ORG-HEADER
        Pet[] result = get(httpHeaders);
        assertThat(result.length, is(0));
    }

    private static Pet[] get(HttpHeaders httpHeaders) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Pet[]> responseEntity = restTemplate.exchange("http://localhost:8081/pets", HttpMethod.GET, httpEntity, Pet[].class);
        assertThat(responseEntity, is(notNullValue()));
        assertThat(responseEntity.getStatusCodeValue(), is(200));
        return responseEntity.getBody();
    }
}