## Pet Profile Microservice
[![Build Status](https://travis-ci.com/jferrater/opa-data-filter-demo.svg?branch=master)](https://travis-ci.com/jferrater/opa-data-filter-demo)<br>
<br>
This is a sample Spring Boot application that uses  [opa-data-filter-spring-boot-starter](https://github.com/jferrater/opa-data-filter-spring-boot-starter).

### Use cases
1. “Pet owners can access their own pet’s profiles.”
2. “Veterinarians can access pet profiles from devices at the clinic.”

### Design
![Spring Boot App with OPA Data Filter](https://github.com/jferrater/opa-data-filter-demo/blob/master/diagram.png)

### The Policy
In order to meet the use cases, the following policy is set in the Open Policy Agent server.
```text
package petclinic.authz

default allow = false

allow {
  input.method = "GET"
  input.path = ["pets", name]
  allowed[pet]
  pet.name = name
}

allow {
  input.method = "GET"
  input.path = ["pets"]
  allowed[pet]
}

allowed[pet] {
  pet = data.pets[_]
  pet.owner = input.subject.user
}

allowed[pet] {
  pet = data.pets[_]
  pet.veterinarian = input.subject.user
  pet.clinic = input.subject.location
}
```

### Running the Project
#### Pre-requisites
* java 11 or hihger
* docker-compose

#### Quick Start
1. ``git clone https://github.com/jferrater/opa-data-filter-demo.git``
2. ``cd opa-data-filter-demo && ./gradlew bootJar``
3. ``docker-compose up --build`` <br>
   The docker-compose will run the PetProfile service, Open Policy Agent server with a policy, `petclinic_policy.rego` and a MariaDB with initial data from `classpath:sql/init.sql`
5. Open http://localhost:8081/swagger-ui.html for API documentation.
#### Testing
Configured users are `alice` and `bob`. `alice` is a veterinarian of the pet with name `browny`
and `bob` is the pet owner.
```shell script
curl -i --user alice:password -H "X-ORG-HEADER: SOMA" http://localhost:8081/pets

curl -i --user bob:password -H "X-ORG-HEADER: SOMA" http://localhost:8081/pets
```
- alice should be able to see all the pets assigned to her.
- bob should only see his pet with name browny
### Project Implementation
#### Dependencies
The following dependencies were added in the project to enforce authorization at the Spring Data repository.
```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation group:'com.github.jferrater', name: 'opa-datafilter-jpa-spring-boot-starter', version: '0.4.4'
```
#### Configuration
The configuration is defined in the ``classpath:application-mariadb.yml``. This is the configuration used when running command ``./gradlew -Dspring.profiles.active=mariadb bootRun``
```yaml
opa:
  authorization:
    url: "http://localhost:8181/v1/compile"

#Spring Data JPA specific configurations
spring:
  datasource:
    driver-class-name: org.mariadb.jdbc.Driver
    url: jdbc:mariadb://localhost:3306/integrationTest
    username: admin
    password: MangaonTaNiny0!
```

### Contact
Contact me at `joffry.ferrater@gmail.com` for feedback and suggestions.