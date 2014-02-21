# -*- coding: utf-8 -*-
Feature: Get the details of an abstract environment

    As a fi-ware user
    I want to be able to get the details of an abstract environment
    so that I have access to its data 

    @happy_path
    Scenario: Get the details of an abstract environment
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        When I request the details of the abstract environment with name "nameqa"
        Then I receive an "OK" response with data:
            | name   | description |
            | nameqa | descqa      |
        
    Scenario Outline: Get the details of an abstract environment with different data
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description   |
            | <name> | <description> |
        When I request the details of the abstract environment with name "<name>"
        Then I receive an "OK" response with data:
            | name   | description |
            | <name> | <description> |
        
        Examples:
            | name                    | description              |
            | a                       | descqa                   |
            | nameqa-1                | descqa                   |
            | [STRING_WITH_LENGTH_30] | descqa                   |
            | accountsqa1             | a                        |
            | accountsqa2             | descqa-1                 |
            | accountsqa3             | Symbols: +*=._,;"@#%)/?! |
            | accountsqa4             | Non-ASCII: á.é.í.ñ       |
            | accountsqa5             | [STRING_WITH_LENGTH_150] |

    @skip @CLAUDIA-3679
    Scenario: Get the details of a nonexistent abstract environment
        Given the paas manager is up and properly configured
        And there is no abstract environment with name "nameqa" already created
        When I request the details of the abstract environment with name "nameqa"
        Then I receive a "Not Found" response
