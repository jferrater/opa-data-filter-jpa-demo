package com.example.opadatafilterdemo.controller;

import com.example.opadatafilterdemo.model.Pet;
import com.example.opadatafilterdemo.service.PetService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.jferrater.opa.ast.to.sql.query.model.request.PartialRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author joffryferrater
 */
@RestController
public class PetController {

    /*
     *   The policy query to run during OPA partial evaluation
     */
    private static final String POLICY_QUERY_TO_RUN = "data.petclinic.authz.allow = true";
    /*
     * The values to treat as unknown during OPA partial evaluation
     */
    private static final Set<String> UNKNOWNS = Set.of("data.pets");

    private PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> getConfig() {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }

    /**
     * Use case 1: “Pet owners can access their own pet’s profiles.”
     * Use case 2: “Veterinarians can access pet profiles from devices at the clinic.”
     *
     * @param name The pet's name
     * @param user The current user of the application. This can be a pet's owner or an attending veterinarian.
     *             For simplification, the current user is set from the request parameters. Normally, the current user
     *             is taken from the authentication object, access tokens or JWTS
     *
     * @return The pet's profile
     */
    @GetMapping("/pets/{name}")
    public ResponseEntity<Pet> getPets(@PathVariable("name") String name, @RequestParam("user") String user) {
        PartialRequest partialRequest = partialRequest();
        CurrentUser currentUser = getCurrentUser(user, null);
        Map<String, Object> input = opaInputDocument("GET", Set.of("pets", name), currentUser);
        partialRequest.setInput(input);
        Optional<Pet> pet = petService.getPets(partialRequest).stream().filter(p -> p.getName().equals(name)).findFirst();
        if(pet.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pet.get(), HttpStatus.OK);
    }

    /**
     * Use case: “Veterinarians can view the pets assign to them from devices at the clinic”
     *
     * For simplification, the current user is set from the request parameters. The current user can be a
     * pet's owner or a veterinarian using the application.
     * Normally, the PartialRequest object is created from the JWT or access tokens or from
     * HTTP header values
     *
     * @param clinicLocation The pet's clinic location
     * @param user The current user which is the veterinarian in this case
     */
    @GetMapping("/pets")
    public ResponseEntity<List<Pet>> getPetsByCurrentUser(@RequestParam("user") String user, @RequestParam("clinic_location") String clinicLocation) {
        PartialRequest partialRequest = partialRequest();
        CurrentUser currentUser = getCurrentUser(user, clinicLocation);
        Map<String, Object> input = opaInputDocument("GET", Set.of("pets"), currentUser);
        partialRequest.setInput(input);
        List<Pet> pets = petService.getPets(partialRequest);
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    private CurrentUser getCurrentUser(String user, String clinicLocation) {
        return new CurrentUser(user, clinicLocation);
    }

    private PartialRequest partialRequest() {
        PartialRequest partialRequest = new PartialRequest();
        partialRequest.setQuery(POLICY_QUERY_TO_RUN);
        partialRequest.setUnknowns(UNKNOWNS);
        return partialRequest;
    }

    /*
     * The OPA input document to use.
     */
    private Map<String, Object> opaInputDocument(String httpMethod, Set<String> httpRequestPath, CurrentUser currentUser) {
        Map<String, Object> input = new HashMap<>();
        input.put("method", httpMethod);
        input.put("path", httpRequestPath);
        input.put("subject", currentUser);
        return input;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class CurrentUser {
        String user;
        String location;

        public CurrentUser(String user, String location) {
            this.user = user;
            this.location = location;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
