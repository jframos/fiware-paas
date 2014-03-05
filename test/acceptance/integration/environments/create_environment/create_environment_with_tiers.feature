# -*- coding: utf-8 -*-
Feature: Create an environment with tiers in a tenant

    As a fi-ware user
    I want to be able to create environments with tiers in a tenant
    so that I can instantiate them later

    @happy_path
    Scenario: Create environment with one tier
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       |
            | tiernameqa |
        When I request the creation of an environment with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        Then I receive a "No Content" response

    Scenario: Create environment with several tiers
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        |
            | tiernameqa1 |
            | tiernameqa2 |
            | tiernameqa3 |
        When I request the creation of an environment with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        Then I receive a "No Content" response

    Scenario: Create environment with several tiers with different valid data
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name                    |
            | a                       |
            | tiernameqa-1            |
            | [STRING_WITH_LENGTH_30] |
        When I request the creation of an environment with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        Then I receive a "No Content" response

    Scenario Outline: Create environment with tiers with different invalid data
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       |
            | <tiername> |
        When I request the creation of an environment with the previous tiers and data:
            | name   | description |
            | <name> | descqa      |
        Then I receive an "Bad Request" response
        
        Examples:
            | name    | tiername                |
            | nameqa1 | tiernameqa_1            |
            | nameqa2 | tiernameqa 1            |
            | nameqa3 | [STRING_WITH_LENGTH_31] |
            | nameqa4 |                         |
            | nameqa5 | [MISSING_PARAM]         |

    Scenario Outline: Create environment with one tier with products
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       | products   |
            | <tiername> | <products> |
        When I request the creation of an environment with the previous tiers and data:
            | name   | description |
            | <name> | descqa      |
        Then I receive a "No Content" response

        Examples:
            | name    | tiername    | products                 |
            | nameqa1 | tiernameqa1 | git=1.7                  |
            | nameqa2 | tiernameqa2 | git=1.7,mediawiki=1.17.0 |

    Scenario: Create environment with several tiers with products
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        | products         |
            | tiernameqa1 | git=1.7          |
            | tiernameqa2 | mediawiki=1.17.0 |
        When I request the creation of an environment with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        Then I receive a "No Content" response

    Scenario Outline: Create environment with one tier with networks
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       | networks   |
            | <tiername> | <networks> |
        When I request the creation of an environment with the previous tiers and data:
            | name   | description |
            | <name> | descqa      |
        Then I receive a "No Content" response

        Examples:
            | name    | tiername    | networks      |
            | nameqa1 | tiernameqa1 | netqa1        |
            | nameqa2 | tiernameqa2 | netqa1,netqa2 |

    Scenario: Create environment with several tiers with networks
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        | networks |
            | tiernameqa1 | netqa1   |
            | tiernameqa2 | netqa2   |
        When I request the creation of an environment with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        Then I receive a "No Content" response

    Scenario Outline: Create environment with one tier with products and networks
        And a list of tiers has been defined with data:
            | name       | products   | networks   |
            | <tiername> | <products> | <networks> |
        When I request the creation of an environment with the previous tiers and data:
            | name   | description |
            | <name> | descqa      |
        Then I receive a "No Content" response

        Examples:
            | name    | tiername    | products                 | networks      |
            | nameqa1 | tiernameqa1 | git=1.7                  | netqa1        |
            | nameqa2 | tiernameqa2 | git=1.7,mediawiki=1.17.0 | netqa1,netqa2 |

    Scenario: Create environment with several tiers with products and networks
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        | products         | networks |
            | tiernameqa1 | git=1.7          | netqa1   |
            | tiernameqa2 | mediawiki=1.17.0 | netqa2   |
        When I request the creation of an environment with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        Then I receive a "No Content" response
