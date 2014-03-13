# -*- coding: utf-8 -*-
Feature: Delete a tier from an abstract environment

    As a fi-ware user
    I want to be able to delete a tier of an abstract environment
    so that I can keep the environment free of outdated tiers

    @happy_path
    Scenario: Delete tier from an abstract environment
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        And a tier has already been added to the abstract environment "nameqa" with data:
            | name       |
            | tiernameqa |
        When I request the deletion of the tier "tiernameqa" from the abstract environment "nameqa"
        Then I receive a "No Content" response
        And the tier "tiernameqa" of the abstract environment "nameqa" is no longer available
        
    Scenario Outline: Delete tier with different valid data from an abstract environment
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        And a tier has already been added to the abstract environment "<name>" with data:
            | name       |
            | <tiername> |
        When I request the deletion of the tier "<tiername>" from the abstract environment "<name>"
        Then I receive a "No Content" response
        And the tier "<tiername>" of the abstract environment "<name>" is no longer available
        
        Examples:
            | name    | tiername                |
            | nameqa1 | a                       |
            | nameqa2 | nameqa-1                |
            | nameqa3 | [STRING_WITH_LENGTH_30] |
        
    Scenario Outline: Delete tier with products from an abstract environment
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        And a tier has already been added to the abstract environment "<name>" with data:
            | name       | products   |
            | <tiername> | <products> |
        When I request the deletion of the tier "<tiername>" from the abstract environment "<name>"
        Then I receive a "No Content" response
        And the tier "<tiername>" of the abstract environment "<name>" is no longer available
        
        Examples:
            | name    | tiername    | products                 |
            | nameqa1 | tiernameqa1 | git=1.7                  |
            | nameqa2 | tiernameqa2 | git=1.7,mediawiki=1.17.0 |
        
    Scenario Outline: Delete tier with networks from an abstract environment
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        And a tier has already been added to the abstract environment "<name>" with data:
            | name       | networks   |
            | <tiername> | <networks> |
        When I request the deletion of the tier "<tiername>" from the abstract environment "<name>"
        Then I receive a "No Content" response
        And the tier "<tiername>" of the abstract environment "<name>" is no longer available
        
        Examples:
            | name    | tiername    | networks      |
            | nameqa1 | tiernameqa1 | netqa1        |
            | nameqa2 | tiernameqa2 | netqa1,netqa2 |
        
    Scenario Outline: Delete tier with products and networks from an abstract environment
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        And a tier has already been added to the abstract environment "<name>" with data:
            | name       | products   | networks   |
            | <tiername> | <products> | <networks> |
        When I request the deletion of the tier "<tiername>" from the abstract environment "<name>"
        Then I receive a "No Content" response
        And the tier "<tiername>" of the abstract environment "<name>" is no longer available
        
        Examples:
            | name    | tiername    | products                 | networks      |
            | nameqa1 | tiernameqa1 | git=1.7                  | netqa1        |
            | nameqa2 | tiernameqa2 | git=1.7,mediawiki=1.17.0 | netqa1,netqa2 |

    Scenario: Delete nonexistent tier from an abstract environment
        Given the paas manager is up and properly configured
        And an abstract environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        When I request the deletion of the tier "tiernameqa" from the abstract environment "nameqa"
        Then I receive a "Not Found" response
