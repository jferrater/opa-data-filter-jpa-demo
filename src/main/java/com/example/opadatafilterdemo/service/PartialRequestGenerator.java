package com.example.opadatafilterdemo.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import opa.datafilter.core.ast.db.query.model.request.PartialRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PartialRequestGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartialRequestGenerator.class);
    /*
     *   The policy query to run during OPA partial evaluation
     */
    private static final String POLICY_QUERY_TO_RUN = "data.petclinic.authz.allow = true";
    /*
     * The values to treat as unknown during OPA partial evaluation
     */
    private static final Set<String> UNKNOWNS = Set.of("data.pets");

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private ObjectMapper objectMapper;

    public PartialRequest getPartialRequest() {
        String method = httpServletRequest.getMethod();
        LOGGER.info("http method: {}", method);
        String servletPath = httpServletRequest.getServletPath();
        LOGGER.info("http path: {}", servletPath);
        String contextPath = httpServletRequest.getContextPath();
        LOGGER.info("Context path: {}", contextPath);
        String username = httpServletRequest.getHeader("X-USER");
        LOGGER.info("username: {}", username);
        String org = httpServletRequest.getHeader("X-ORG");
        LOGGER.info("organization: {}", org);
        PartialRequest partialRequest = new PartialRequest();
        partialRequest.setQuery(POLICY_QUERY_TO_RUN);
        partialRequest.setUnknowns(UNKNOWNS);
        Map<String, Object> input = new HashMap<>();
        input.put("path", List.of(servletPath));
        input.put("method", method);
        input.put("subject", new CurrentUser(username, org));
        partialRequest.setInput(input);
        printPartialRequest(partialRequest);
        return partialRequest;
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

    private void printPartialRequest(PartialRequest partialRequest) {
        try {
            String prettyString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(partialRequest);
            LOGGER.info("Partial Request={}", prettyString);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
