# -*- coding: utf-8 -*-
Feature: Get the abstract environments with tiers

    As a fi-ware user
    I want to be able to get the list of abstract environments with tiers
    so that I know the abstract environments I can use to create my own 

    @happy_path
    Scenario: Get a list with just one abstract environment with one tier
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       |
            | tiernameqa |
        And an abstract environment has already been created with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        When I request the list of existing abstract environments
        # The catalog is shared, so the exact number of items is unknown 
        Then I receive an "OK" response with some items in the list
        And there is an abstract environment in the list with "1" tier and data:
            | name   | description |
            | nameqa | descqa      |
        
    Scenario: Get a list with just one abstract environment with several tiers
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        |
            | tiernameqa1 |
            | tiernameqa2 |
            | tiernameqa3 |
        And an abstract environment has already been created with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        When I request the list of existing abstract environments
        Then I receive an "OK" response with some items in the list
        And there is an abstract environment in the list with "3" tiers and data:
            | name   | description |
            | nameqa | descqa      |
        
    Scenario: Get a list with several abstract environments with one tier
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        |
            | tiernameqa1 |
        And an abstract environment has already been created with the previous tiers and data:
            | name    | description |
            | nameqa1 | descqa1     |
        And a list of tiers has been defined with data:
            | name        |
            | tiernameqa2 |
        And an abstract environment has already been created with the previous tiers and data:
            | name    | description |
            | nameqa2 | descqa2     |
        And a list of tiers has been defined with data:
            | name        |
            | tiernameqa3 |
        And an abstract environment has already been created with the previous tiers and data:
            | name    | description |
            | nameqa3 | descqa3     |
        When I request the list of existing abstract environments
        Then I receive an "OK" response with some items in the list
        And there is an abstract environment in the list with "1" tier and data:
            | name    | description |
            | nameqa1 | descqa1     |
        And there is an abstract environment in the list with "1" tier and data:
            | name    | description |
            | nameqa2 | descqa2     |
        And there is an abstract environment in the list with "1" tier and data:
            | name    | description |
            | nameqa3 | descqa3     |
            
    Scenario: Get a list with several abstract environments with several tiers
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        |
            | tiernameqa1 |
        And an abstract environment has already been created with the previous tiers and data:
            | name    | description |
            | nameqa1 | descqa1     |
        And a list of tiers has been defined with data:
            | name         |
            | tiernameqa2a |
            | tiernameqa2b |
        And an abstract environment has already been created with the previous tiers and data:
            | name    | description |
            | nameqa2 | descqa2     |
        And a list of tiers has been defined with data:
            | name         |
            | tiernameqa3a |
            | tiernameqa3b |
            | tiernameqa3c |
        And an abstract environment has already been created with the previous tiers and data:
            | name    | description |
            | nameqa3 | descqa3     |
        When I request the list of existing abstract environments
        Then I receive an "OK" response with some items in the list
        And there is an abstract environment in the list with "1" tier and data:
            | name    | description |
            | nameqa1 | descqa1     |
        And there is an abstract environment in the list with "2" tiers and data:
            | name    | description |
            | nameqa2 | descqa2     |
        And there is an abstract environment in the list with "3" tiers and data:
            | name    | description |
            | nameqa3 | descqa3     |
            
    Scenario: Get a list with several abstract environments with tiers with different valid data
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name |
            | a    |
        And an abstract environment has already been created with the previous tiers and data:
            | name    | description |
            | nameqa1 | descqa1     |
        And a list of tiers has been defined with data:
            | name         |
            | tiernameqa-1 |
        And an abstract environment has already been created with the previous tiers and data:
            | name    | description |
            | nameqa2 | descqa2     |
        And a list of tiers has been defined with data:
            | name                    |
            | [STRING_WITH_LENGTH_30] |
        And an abstract environment has already been created with the previous tiers and data:
            | name    | description |
            | nameqa3 | descqa3     |
        When I request the list of existing abstract environments
        Then I receive an "OK" response with some items in the list
        And there is an abstract environment in the list with "1" tier and data:
            | name    | description |
            | nameqa1 | descqa1     |
        And there is an abstract environment in the list with "1" tier and data:
            | name    | description |
            | nameqa2 | descqa2     |
        And there is an abstract environment in the list with "1" tier and data:
            | name    | description |
            | nameqa3 | descqa3     |
            
    Scenario: Get a list with several abstract environments with tiers with products
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        | products |
            | tiernameqa1 | git=1.7  |
        And an abstract environment has already been created with the previous tiers and data:
            | name    | description |
            | nameqa1 | descqa1     |
        And a list of tiers has been defined with data:
            | name        | products                 |
            | tiernameqa2 | git=1.7,mediawiki=1.17.0 |
        And an abstract environment has already been created with the previous tiers and data:
            | name    | description |
            | nameqa2 | descqa2     |
        When I request the list of existing abstract environments
        Then I receive an "OK" response with some items in the list
        And there is an abstract environment in the list with "1" tier and data:
            | name    | description |
            | nameqa1 | descqa1     |
        And there is an abstract environment in the list with "1" tier and data:
            | name    | description |
            | nameqa2 | descqa2     |
            
    Scenario: Get a list with several abstract environments with tiers with networks
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        | networks |
            | tiernameqa1 | netqa1   |
        And an abstract environment has already been created with the previous tiers and data:
            | name    | description |
            | nameqa1 | descqa1     |
        And a list of tiers has been defined with data:
            | name        | networks |
            | tiernameqa2 | netqa1,netqa2 |
        And an abstract environment has already been created with the previous tiers and data:
            | name    | description |
            | nameqa2 | descqa2     |
        When I request the list of existing abstract environments
        Then I receive an "OK" response with some items in the list
        And there is an abstract environment in the list with "1" tier and data:
            | name    | description |
            | nameqa1 | descqa1     |
        And there is an abstract environment in the list with "1" tier and data:
            | name    | description |
            | nameqa2 | descqa2     |
            
    Scenario: Get a list with several abstract environments with tiers with products and networks
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        | products | networks |
            | tiernameqa1 | git=1.7  | netqa1   |
        And an abstract environment has already been created with the previous tiers and data:
            | name    | description |
            | nameqa1 | descqa1     |
        And a list of tiers has been defined with data:
            | name        | products         | networks |
            | tiernameqa2 | mediawiki=1.17.0 | netqa2   |
        And an abstract environment has already been created with the previous tiers and data:
            | name    | description |
            | nameqa2 | descqa2     |
        When I request the list of existing abstract environments
        Then I receive an "OK" response with some items in the list
        And there is an abstract environment in the list with "1" tier and data:
            | name    | description |
            | nameqa1 | descqa1     |
        And there is an abstract environment in the list with "1" tier and data:
            | name    | description |
            | nameqa2 | descqa2     |
            