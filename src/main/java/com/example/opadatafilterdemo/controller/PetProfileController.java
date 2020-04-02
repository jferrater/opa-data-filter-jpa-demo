package com.example.opadatafilterdemo.controller;

import com.example.opadatafilterdemo.model.Pet;
import com.example.opadatafilterdemo.service.PetProfileService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jferrater.opa.ast.to.sql.query.model.request.PartialRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@Tag(name = "petprofile", description = "The Pet Profile API")
public class PetProfileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetProfileController.class);
    /*
     *   The policy query to run during OPA partial evaluation
     */
    private static final String POLICY_QUERY_TO_RUN = "data.petclinic.authz.allow = true";
    /*
     * The values to treat as unknown during OPA partial evaluation
     */
    private static final Set<String> UNKNOWNS = Set.of("data.pets");

    private PetProfileService petProfileService;
    @Autowired
    private ObjectMapper objectMapper;

    public PetProfileController(PetProfileService petProfileService) {
        this.petProfileService = petProfileService;
    }

    @Operation(summary = "Print hello", description = "Just a hello to test if application is running")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @GetMapping("/hello")
    public ResponseEntity<String> getConfig() {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }

    @Operation(
            summary = "Get pet profile by name",
            description = "Returns a pet profile. A pet owner and the assign veterinarian can access the pet's profile. " +
                    "For simplification, the current user is set from the request parameter, 'user'. Normally, the current user is taken from the authentication object, access tokens or JWTS",
            tags = "petprofile"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = Pet.class))),
            @ApiResponse(responseCode = "404", description = "Pet Profile not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error has occurred. Might be Open Policy Agent server is not reachable")
    })
    @GetMapping("/pets/{name}")
    public ResponseEntity<Pet> getPets(
            @Parameter(description = "The name of the pet to be obtained.", required = true) @PathVariable("name") String name,
            @Parameter(description = "The current user of the application.") @RequestParam("user") String user
    ) {
        PartialRequest partialRequest = partialRequest();
        CurrentUser currentUser = getCurrentUser(user, null);
        Map<String, Object> input = opaInputDocument("GET", Set.of("pets", name), currentUser);
        partialRequest.setInput(input);
        printPartialRequest(partialRequest);
        Optional<Pet> pet = petProfileService.getPets(partialRequest).stream().filter(p -> p.getName().equals(name)).findFirst();
        if(pet.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pet.get(), HttpStatus.OK);
    }

    @Operation(
            summary = "Get the list of pet profiles",
            description = "Returns a list of pet profiles. Veterinarians can view the list of pet profiles assign to them from devices at the clinic. " +
                    "For simplification, the current user is set from the request parameter, 'user'. Normally, the current user is taken from the authentication object, access tokens or JWTS",
            tags = "petprofile"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Pet.class)))),
            @ApiResponse(responseCode = "500", description = "An internal server error has occurred. Might be Open Policy Agent server is not reachable")
    })
    @GetMapping("/pets")
    public ResponseEntity<List<Pet>> getPetsByCurrentUser(
            @Parameter(description = "The current user of the application. This can be a pet's owner or the assign veterinarian.") @RequestParam("user") String user,
            @Parameter(description = "The clinic location where the veterinarian is using a device to access a pet's profile") @RequestParam("clinic_location") String clinicLocation
    ) {
        PartialRequest partialRequest = partialRequest();
        CurrentUser currentUser = getCurrentUser(user, clinicLocation);
        Map<String, Object> input = opaInputDocument("GET", Set.of("pets"), currentUser);
        partialRequest.setInput(input);
        printPartialRequest(partialRequest);
        List<Pet> pets = petProfileService.getPets(partialRequest);
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    private void printPartialRequest(PartialRequest partialRequest) {
        try {
            String prettyString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(partialRequest);
            LOGGER.info(prettyString);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
        }
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
        input.put("path", httpRequestPath);
        input.put("method", httpMethod);
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
