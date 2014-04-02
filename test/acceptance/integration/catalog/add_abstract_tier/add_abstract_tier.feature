# -*- coding: utf-8 -*-
Feature: Add a tier to an abstract environment

    As a fi-ware user
    I want to be able to add tiers to the abstract environments
    so that they become more functional and useful

    @happy_path
    Scenario: Add tier to an abstract environment without tiers
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        When I request the addition of a tier to the abstract environment "nameqa" with data:
            | name       |
            | tiernameqa |
        Then I receive a "No Content" response
        
    Scenario: Add tier to an abstract environment with tiers
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        And a tier has already been added to the abstract environment "nameqa" with data:
            | name       |
            | tiernameqa |
        When I request the addition of a tier to the abstract environment "nameqa" with data:
            | name        |
            | newtiername |
        Then I receive a "No Content" response
        
    Scenario Outline: Add tier with different valid data to an abstract environment
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        When I request the addition of a tier to the abstract environment "<name>" with data:
            | name       |
            | <tiername> |
        Then I receive a "No Content" response
        
        Examples:
            | name    | tiername                |
            | nameqa1 | a                       |
            | nameqa2 | nameqa-1                |
            | nameqa3 | [STRING_WITH_LENGTH_30] |

    Scenario Outline: Add tier with different invalid data to an abstract environment
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        When I request the addition of a tier to the abstract environment "<name>" with data:
            | name       |
            | <tiername> |
        Then I receive a "Bad Request" response
        
        Examples:
            | name    | tiername                |
            | nameqa1 | tiernameqa_1            |
            | nameqa2 | tiernameqa 1            |
            | nameqa3 | [STRING_WITH_LENGTH_31] |
            | nameqa4 |                         |
            | nameqa5 | [MISSING_PARAM]         |

    Scenario: Add tier with the name of an already existing tier to an abstract environment
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        And a tier has already been added to the abstract environment "nameqa" with data:
            | name       |
            | tiernameqa |
        When I request the addition of a tier to the abstract environment "nameqa" with data:
            | name       |
            | tiernameqa |
        Then I receive a "Conflict" response
        
    Scenario Outline: Add tier with products to an abstract environment
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        When I request the addition of a tier to the abstract environment "<name>" with data:
            | name       | products   |
            | <tiername> | <products> |
        Then I receive a "No Content" response

        Examples:
            | name    | tiername    | products                 |
            | nameqa1 | tiernameqa1 | git=1.7                  |
            | nameqa2 | tiernameqa2 | git=1.7,mediawiki=1.17.0 |
        
    Scenario Outline: Add tier with networks to an abstract environment
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        When I request the addition of a tier to the abstract environment "<name>" with data:
            | name       | networks   |
            | <tiername> | <networks> |
        Then I receive a "No Content" response

        Examples:
            | name    | tiername    | networks      |
            | nameqa1 | tiernameqa1 | netqa1        |
            | nameqa2 | tiernameqa2 | netqa1,netqa2 |
        
    Scenario Outline: Add tier with products and networks to an abstract environment
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        When I request the addition of a tier to the abstract environment "<name>" with data:
            | name       | products   | networks   |
            | <tiername> | <products> | <networks> |
        Then I receive a "No Content" response

        Examples:
            | name    | tiername    | products                 | networks      |
            | nameqa1 | tiernameqa1 | git=1.7                  | netqa1        |
            | nameqa2 | tiernameqa2 | git=1.7,mediawiki=1.17.0 | netqa1,netqa2 |
        
    Scenario: Add tier to non existent abstract environment
        Given the paas manager is up and properly configured
        And there is no abstract environment with name "nameqa" already created
        When I request the addition of a tier to the abstract environment "nameqa" with data:
            | name       |
            | tiernameqa |
        Then I receive a "Not Found" response
