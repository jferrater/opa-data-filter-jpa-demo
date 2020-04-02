## Pet Profile Microservice

This is a demo project for [opa-data-filter-spring-boot-starter](https://github.com/jferrater/opa-data-filter-spring-boot-starter)

### Spring Data JPA and OPA Data Filter starters dependencies:
```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation group:'com.github.jferrater', name: 'opa-data-filter-spring-boot-starter', version: '0.2.1'
```
### Prerequisite
* docker-compose

### Quick Start
1. ``git clone https://github.com/jferrater/opa-data-filter-demo.git``
2. ``cd opa-data-filter-demo``
3. ``docker-compose up`` --> This will spin up an Open Policy Agent server with a policy, `petclinic_policy.rego``
4. ``./gradlew bootRun`` --> starts the PetProfile application.
5. Open http://localhost:8081/swagger-ui.html for API documentation.

The application will start using H2 database with the initial data from classpath:sql/init.sql. This is not recommended on production.
Use appropriate database for production use.

### TO BE FILLED UP!