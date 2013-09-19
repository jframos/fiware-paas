Feature: Create an environment Instance from a Payload

    Scenario: Create Environment Instance Payload
    Given Payload Location 
    When I create an environmentInstance
    Then There is an environmentInstance in the bbdd