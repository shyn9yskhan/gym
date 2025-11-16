package com.shyn9yskhan.authentication_service.steps;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import io.cucumber.java.en.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import static org.junit.Assert.*;

public class AuthSteps {

    private static WireMockServer wireMock = new WireMockServer(0);
    private String baseUrl;
    private ResponseEntity<String> lastResponse;
    private String lastToken;

    @Autowired
    private TestRestTemplate restTemplate;

    static {
        wireMock.start();
    }

    @Given("user with username {string} and password {string} exists")
    public void user_exists(String username, String password) {
        String body = String.format("{\"userId\":\"u-1\",\"username\":\"%s\",\"password\":\"%s\",\"role\":\"TRAINEE\"}", username, password);
        wireMock.stubFor(get(urlPathEqualTo("/users/" + username))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type","application/json").withBody(body)));
    }

    @When("I send login request with username {string} and password {string}")
    public void send_login(String username, String password) {
        baseUrl = "http://localhost:9000";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        HttpEntity<String> req = new HttpEntity<>(json, headers);
        lastResponse = restTemplate.postForEntity(baseUrl + "/login", req, String.class);
        if (lastResponse.getStatusCode().is2xxSuccessful()) {
            String body = lastResponse.getBody();
            lastToken = body.replaceAll(".*\"token\"\\s*:\\s*\"([^\"]+)\".*","$1");
        }
    }

    @Then("authentication service returns token")
    public void returns_token() {
        assertNotNull("response must be 200", lastResponse);
        assertEquals(200, lastResponse.getStatusCodeValue());
        assertNotNull("token must be present", lastToken);
        DecodedJWT jwt = JWT.decode(lastToken);
        assertEquals("u-1", jwt.getClaim("userId").asString());
        assertEquals("TRAINEE", jwt.getClaim("role").asString());
        assertNotNull(jwt.getClaim("traineeId").asString());
    }

    @Then("authentication service returns authentication error")
    public void returns_auth_error() {
        assertNotNull(lastResponse);
        assertEquals(401, lastResponse.getStatusCodeValue());
    }

    @Given("I have a valid token")
    public void have_valid_token() {
        lastToken = "ey.test.token";
    }

    @When("I send validate token request")
    public void send_validate() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + lastToken);
        HttpEntity<Void> req = new HttpEntity<>(headers);
        lastResponse = restTemplate.exchange(baseUrl + "/validate", HttpMethod.POST, req, String.class);
    }

    @Then("authentication service returns OK")
    public void returns_ok() {
        assertEquals(200, lastResponse.getStatusCodeValue());
    }

    @Then("authentication service returns validation error")
    public void returns_validation_error() {
        assertTrue(lastResponse.getStatusCode().is4xxClientError() || lastResponse.getStatusCode().is5xxServerError());
    }
}
