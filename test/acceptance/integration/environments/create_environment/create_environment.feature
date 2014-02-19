# -*- coding: utf-8 -*-
Feature: Create an environment in a tenant

    As a fi-ware user
    I want to be able to create environments in a tenant
    so that I can instantiate them later

    @happy_path
    Scenario: Create environment
        Given the paas manager is up and properly configured
        When I request the creation of an environment with data:
            | name   | description |
            | nameqa | descqa      |
        Then I receive a "No Content" response

    Scenario Outline: Create environment with different valid data
        Given the paas manager is up and properly configured
        When I request the creation of an environment with data:
            | name   | description   |
            | <name> | <description> |
        Then I receive a "No Content" response
        
        Examples:
            | name                    | description              |
            | a                       | descqa                   |
            | nameqa-1                | descqa                   |
            | [STRING_WITH_LENGTH_30] | descqa                   |
            | nameqa1                 | a                        |
            | nameqa2                 | descqa-1                 |
            | nameqa3                 | Symbols: +*=._,;"@#%)/?! |
            | nameqa4                 | Non-ASCII: á.é.í.ñ       |
            | nameqa5                 | [STRING_WITH_LENGTH_256] |

    @skip @CLAUDIA-3663
    Scenario Outline: Create environment with different invalid data
        Given the paas manager is up and properly configured
        When I request the creation of an environment with data:
            | name   | description   |
            | <name> | <description> |
        Then I receive an "Bad Request" response
        
        Examples:
            | name                    | description              |
            | nameqa_1                | descqa                   |
            | nameqa 1                | descqa                   |
            | [STRING_WITH_LENGTH_31] | descqa                   |
            |                         | descqa                   |
            | [MISSING_PARAM]         | descqa                   |
            | nameqa1                 | [STRING_WITH_LENGTH_257] |
            | nameqa2                 |                          |
            | nameqa2                 | [MISSING_PARAM]          |

    @skip @CLAUDIA-3674
    Scenario: Create environment with the same name of an already existing one
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        When I request the creation of an environment with data:
            | name   | description |
            | nameqa | descqa      |
        Then I receive a "Bad Request" response
