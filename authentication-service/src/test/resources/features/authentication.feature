Feature: Authentication

  Scenario: Successful login
    Given user with username "alice" and password "pass123" exists
    When I send login request with username "alice" and password "pass123"
    Then authentication service gets user from user-service
    And authentication service validates username and password
    And authentication service requests traineeId or trainerId based on role
    And authentication service returns token

  Scenario: Failed login
    Given user with username "bob" and password "correct" exists
    When I send login request with username "bob" and password "wrong"
    Then authentication service returns authentication error

  Scenario: Validate valid token
    Given I have a valid token
    When I send validate token request
    Then authentication service returns OK

  Scenario: Validate invalid token
    Given I have an invalid token
    When I send validate token request
    Then authentication service returns validation error
