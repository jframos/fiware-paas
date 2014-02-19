# -*- coding: utf-8 -*-
Feature: Delete an environment with tiers in a tenant

    As a fi-ware user
    I want to be able to delete an environment with tiers in a tenant
    so that I can keep the tenant free of outdated environments 

    @happy_path
    Scenario: Delete environment with one tier
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       |
            | tiernameqa |
        And an environment has already been created with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        When I request the deletion of the environment with name "nameqa"
        Then I receive a "No Content" response
        
    Scenario: Delete environment with several tiers
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        |
            | tiernameqa1 |
            | tiernameqa2 |
            | tiernameqa3 |
        And an environment has already been created with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        When I request the deletion of the environment with name "nameqa"
        Then I receive a "No Content" response
        
    Scenario: Delete environment with several tiers with different valid data
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name                    |
            | a                       |
            | tiernameqa-1            |
            | [STRING_WITH_LENGTH_30] |
        And an environment has already been created with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        When I request the deletion of the environment with name "nameqa"
        Then I receive a "No Content" response
        
    Scenario Outline: Delete environment with one tier with products
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       | products   |
            | <tiername> | <products> |
        And an environment has already been created with the previous tiers and data:
            | name   | description |
            | <name> | descqa      |
        When I request the deletion of the environment with name "<name>"
        Then I receive a "No Content" response
        
        Examples:
            | name    | tiername    | products                 |
            | nameqa1 | tiernameqa1 | git=1.7                  |
            | nameqa2 | tiernameqa2 | git=1.7,mediawiki=1.17.0 |

    Scenario: Delete environment with several tiers with products
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        | products         |
            | tiernameqa1 | git=1.7          |
            | tiernameqa2 | mediawiki=1.17.0 |
        And an environment has already been created with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        When I request the deletion of the environment with name "nameqa"
        Then I receive a "No Content" response
        
    Scenario Outline: Delete environment with one tier with networks
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       | networks   |
            | <tiername> | <networks> |
        And an environment has already been created with the previous tiers and data:
            | name   | description |
            | <name> | descqa      |
        When I request the deletion of the environment with name "<name>"
        Then I receive a "No Content" response
        
        Examples:
            | name    | tiername    | networks      |
            | nameqa1 | tiernameqa1 | netqa1        |
            # skip, CLAUDIA-3673 (workaround below)
            # | nameqa2 | tiernameqa2 | netqa1,netqa2 |
            | nameqa2 | tiernameqa2 | netqa2,netqa3 |

    Scenario: Delete environment with several tiers with networks
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        | networks |
            | tiernameqa1 | netqa1   |
            | tiernameqa2 | netqa2   |
        And an environment has already been created with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        When I request the deletion of the environment with name "nameqa"
        Then I receive a "No Content" response
        
    Scenario Outline: Delete environment with one tier with products and networks
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       | products   | networks   |
            | <tiername> | <products> | <networks> |
        And an environment has already been created with the previous tiers and data:
            | name   | description |
            | <name> | descqa      |
        When I request the deletion of the environment with name "<name>"
        Then I receive a "No Content" response

        Examples:
            | name    | tiername    | products                 | networks      |
            | nameqa1 | tiernameqa1 | git=1.7                  | netqa1        |
            # skip, CLAUDIA-3673 (workaround below)
            # | nameqa2 | tiernameqa2 | git=1.7,mediawiki=1.17.0 | netqa1,netqa2 |
            | nameqa2 | tiernameqa2 | git=1.7,mediawiki=1.17.0 | netqa2,netqa3 |
        
    Scenario: Delete environment with several tiers with products and networks
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        | products         | networks |
            | tiernameqa1 | git=1.7          | netqa1   |
            | tiernameqa2 | mediawiki=1.17.0 | netqa2   |
        And an environment has already been created with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        When I request the deletion of the environment with name "nameqa"
        Then I receive a "No Content" response
