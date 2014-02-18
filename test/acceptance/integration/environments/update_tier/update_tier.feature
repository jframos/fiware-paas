# -*- coding: utf-8 -*-
Feature: Update a tier of an environment

    As a fi-ware user
    I want to be able to update a tier of an environments in a tenant
    so that I do not need to delete it and create it again when some change is needed

    @happy_path
    Scenario: Update tier of an environment leaving the same data
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        And a tier has already been added to the environment "nameqa" with data:
            | name       |
            | tiernameqa |
        When I request the update of the tier "tiernameqa" of the environment "nameqa" with data:
            | name       |
            | tiernameqa |
        Then I receive a "No Content" response
        
    Scenario: Update non existing tier of an environment
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        When I request the update of the tier "tiernameqa" of the environment "nameqa" with data:
            | name       | networks |
            | tiernameqa | netqa1   |
        Then I receive a "Not Found" response
