package com.shyn9yskhan.authentication_service;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    static WireMockServer wireMock;

    @Autowired
    TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void startWireMock() {
        wireMock = new WireMockServer(0);
        wireMock.start();
        System.setProperty("user.service.url", wireMock.baseUrl());
        System.setProperty("trainee.service.url", wireMock.baseUrl());
        System.setProperty("trainer.service.url", wireMock.baseUrl());
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMock != null) wireMock.stop();
    }

    @BeforeEach
    void prepareStubs() {
        wireMock.stubFor(get(urlPathMatching("/users/alice"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"userId\":\"u-1\",\"username\":\"alice\",\"password\":\"pass123\",\"role\":\"TRAINEE\"}")));

        wireMock.stubFor(get(urlPathMatching("/trainees/u-1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type","application/json")
                        .withBody("{\"traineeId\":\"t-100\",\"userId\":\"u-1\"}")));
    }

    @Test
    void successfulLogin_returnsTokenWithClaims() {
        var req = new HttpEntity<>(Map.of("username","alice","password","pass123"), headersJson());
        ResponseEntity<Map> resp = restTemplate.postForEntity("/login", req, Map.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).containsKey("token");
        String token = (String) resp.getBody().get("token");

        var decoded = com.auth0.jwt.JWT.decode(token);
        assertThat(decoded.getClaim("userId").asString()).isEqualTo("u-1");
        assertThat(decoded.getClaim("role").asString()).isEqualTo("TRAINEE");
        assertThat(decoded.getClaim("traineeId").asString()).isEqualTo("t-100");
    }

    @Test
    void validateToken_okForValidToken() {
        var loginReq = new HttpEntity<>(Map.of("username","alice","password","pass123"), headersJson());
        var loginResp = restTemplate.postForEntity("/login", loginReq, Map.class);
        String token = (String) loginResp.getBody().get("token");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        var resp = restTemplate.exchange("/validate", HttpMethod.POST, new HttpEntity<>(headers), Map.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).containsEntry("status","ok");
    }

    private HttpHeaders headersJson() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        return h;
    }
}
