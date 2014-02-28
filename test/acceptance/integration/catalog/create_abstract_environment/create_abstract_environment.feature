# -*- coding: utf-8 -*-
Feature: Create an abstract environment

    As a fi-ware user
    I want to be able to create abstract environments
    so that I can clone them later

    @happy_path
    Scenario: Create abstract environment
        Given the paas manager is up and properly configured
        When I request the creation of an abstract environment with data:
            | name   | description |
            | nameqa | descqa      |
        Then I receive a "No Content" response

    Scenario Outline: Create abstract environment with different valid data
        Given the paas manager is up and properly configured
        When I request the creation of an abstract environment with data:
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

    Scenario Outline: Create abstract environment with different invalid data
        Given the paas manager is up and properly configured
        When I request the creation of an abstract environment with data:
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

    Scenario: Create abstract environment with the same name of an already existing one
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        When I request the creation of an abstract environment with data:
            | name   | description |
            | nameqa | descqa      |
        Then I receive a "Conflict" response
