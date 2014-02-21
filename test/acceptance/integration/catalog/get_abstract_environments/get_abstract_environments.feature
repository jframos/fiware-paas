# -*- coding: utf-8 -*-
Feature: Get the abstract environments

    As a fi-ware user
    I want to be able to get the list of abstract environments
    so that I know the abstract environments I can use to create my own 

    @happy_path
    Scenario: Get a list with just one abstract environment
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        When I request the list of existing abstract environments
        # The catalog is shared, so the exact number of items is unknown 
        Then I receive an "OK" response with some items in the list
        And there is an abstract environment in the list with data:
            | name   | description |
            | nameqa | descqa      |
        
    Scenario: Get a list with several abstract environments
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name    | description |
            | nameqa1 | descqa1     |
        And an abstract environment has already been created with data:
            | name    | description |
            | nameqa2 | descqa2     |
        And an abstract environment has already been created with data:
            | name    | description |
            | nameqa3 | descqa3     |
        When I request the list of existing abstract environments
        Then I receive an "OK" response with some items in the list
        And there is an abstract environment in the list with data:
            | name    | description |
            | nameqa1 | descqa1     |
        And there is an abstract environment in the list with data:
            | name    | description |
            | nameqa2 | descqa2     |
        And there is an abstract environment in the list with data:
            | name    | description |
            | nameqa3 | descqa3     |
        
    Scenario: Get a list with many abstract environments with different valid data
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name                    | description              |
            | a                       | descqa                   |
        And an abstract environment has already been created with data:
            | name                    | description              |
            | nameqa-1                | descqa                   |
        And an abstract environment has already been created with data:
            | name                    | description              |
            | [STRING_WITH_LENGTH_30] | descqa                   |
        And an abstract environment has already been created with data:
            | name                    | description              |
            | accountsqa1             | a                        |
        And an abstract environment has already been created with data:
            | name                    | description              |
            | accountsqa2             | descqa-1                 |
        And an abstract environment has already been created with data:
            | name                    | description              |
            | accountsqa3             | Symbols: +*=._,;"@#%)/?! |
        And an abstract environment has already been created with data:
            | name                    | description              |
            | accountsqa4             | Non-ASCII: á.é.í.ñ       |
        And an abstract environment has already been created with data:
            | name                    | description              |
            | accountsqa5             | [STRING_WITH_LENGTH_150] |
        When I request the list of existing abstract environments
        Then I receive an "OK" response with some items in the list
        And there is an abstract environment in the list with data:
            | name                    | description              |
            | a                       | descqa                   |
        And there is an abstract environment in the list with data:
            | name                    | description              |
            | nameqa-1                | descqa                   |
        And there is an abstract environment in the list with data:
            | name                    | description              |
            | [STRING_WITH_LENGTH_30] | descqa                   |
        And there is an abstract environment in the list with data:
            | name                    | description              |
            | accountsqa1             | a                        |
        And there is an abstract environment in the list with data:
            | name                    | description              |
            | accountsqa2             | descqa-1                 |
        And there is an abstract environment in the list with data:
            | name                    | description              |
            | accountsqa3             | Symbols: +*=._,;"@#%)/?! |
        And there is an abstract environment in the list with data:
            | name                    | description              |
            | accountsqa4             | Non-ASCII: á.é.í.ñ       |
        And there is an abstract environment in the list with data:
            | name                    | description              |
            | accountsqa5             | [STRING_WITH_LENGTH_150] |
