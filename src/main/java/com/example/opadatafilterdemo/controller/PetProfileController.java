package com.example.opadatafilterdemo.controller;

import com.example.opadatafilterdemo.model.Pet;
import com.example.opadatafilterdemo.service.PetProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author joffryferrater
 */
@RestController
@Tag(name = "petprofile", description = "The Pet Profile API")
public class PetProfileController {

    private PetProfileService petProfileService;

    public PetProfileController(PetProfileService petProfileService) {
        this.petProfileService = petProfileService;
    }

    @Operation(summary = "Print hello", description = "Just a hello to test if the application is running")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @GetMapping("/hello")
    public ResponseEntity<String> getConfig() {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }


    @Operation(
            summary = "Get the list of pet profiles",
            description = "Returns a list of pet profiles. Veterinarians can view the list of pet profiles assign to them from the devices at the clinic.",
            tags = "petprofile"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Pet.class)))),
            @ApiResponse(responseCode = "500", description = "An internal server error has occurred. Might be Open Policy Agent server is not reachable")
    })
    @GetMapping("/pets")
    public ResponseEntity<List<Pet>> getLovelyPets() {
        List<Pet> pets = petProfileService.getPetProfiles();
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }
}
