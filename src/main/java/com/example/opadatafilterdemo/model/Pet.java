package com.example.opadatafilterdemo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author joffryferrater
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pet {

    @Schema(description = "The name of the pet.", example = "fluffy", required = true)
    @JsonProperty("name")
    private String name;

    @Schema(description = "The owner of the pet.", example = "bob", required = true)
    @JsonProperty("owner")
    private String owner;

    @Schema(description = "The veterinarian of the pet.", example = "alice", required = true)
    @JsonProperty("veterinarian")
    private String veterinarian;

    @Schema(description = "The location of the clinic", example = "SOMA", required = true)
    @JsonProperty("clinic")
    private String clinic;

    public Pet() {
    }

    public Pet(String name, String owner, String veterinarian, String clinic) {
        this.name = name;
        this.owner = owner;
        this.veterinarian = veterinarian;
        this.clinic = clinic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(String veterinarian) {
        this.veterinarian = veterinarian;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }
}
