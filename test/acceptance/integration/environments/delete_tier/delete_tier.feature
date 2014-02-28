# -*- coding: utf-8 -*-
Feature: Delete a tier from an environment in a tenant

    As a fi-ware user
    I want to be able to delete a tier from an environment in a tenant
    so that I can keep the environment free of outdated tiers

    @happy_path
    Scenario: Delete tier from an environment
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        And a tier has already been added to the environment "nameqa" with data:
            | name       |
            | tiernameqa |
        When I request the deletion of the tier "tiernameqa" from the environment "nameqa"
        Then I receive a "No Content" response
        And the tier "tiernameqa" of the environment "nameqa" is no longer available
        
    Scenario Outline: Delete tier with different valid data from an environment
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        And a tier has already been added to the environment "<name>" with data:
            | name       |
            | <tiername> |
        When I request the deletion of the tier "<tiername>" from the environment "<name>"
        Then I receive a "No Content" response
        And the tier "<tiername>" of the environment "<name>" is no longer available
        
        Examples:
            | name    | tiername                |
            | nameqa1 | a                       |
            | nameqa2 | nameqa-1                |
            | nameqa3 | [STRING_WITH_LENGTH_30] |
        
    Scenario Outline: Delete tier with products from an environment
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        And a tier has already been added to the environment "<name>" with data:
            | name       | products   |
            | <tiername> | <products> |
        When I request the deletion of the tier "<tiername>" from the environment "<name>"
        Then I receive a "No Content" response
        And the tier "<tiername>" of the environment "<name>" is no longer available
        
        Examples:
            | name    | tiername    | products                 |
            | nameqa1 | tiernameqa1 | git=1.7                  |
            | nameqa2 | tiernameqa2 | git=1.7,mediawiki=1.17.0 |
        
    Scenario Outline: Delete tier with networks from an environment
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        And a tier has already been added to the environment "<name>" with data:
            | name       | networks   |
            | <tiername> | <networks> |
        When I request the deletion of the tier "<tiername>" from the environment "<name>"
        Then I receive a "No Content" response
        And the tier "<tiername>" of the environment "<name>" is no longer available
        
        Examples:
            | name    | tiername    | networks      |
            | nameqa1 | tiernameqa1 | netqa1        |
            | nameqa2 | tiernameqa2 | netqa1,netqa2 |
        
    Scenario Outline: Delete tier with products and networks from an environment
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        And a tier has already been added to the environment "<name>" with data:
            | name       | products   | networks   |
            | <tiername> | <products> | <networks> |
        When I request the deletion of the tier "<tiername>" from the environment "<name>"
        Then I receive a "No Content" response
        And the tier "<tiername>" of the environment "<name>" is no longer available
        
        Examples:
            | name    | tiername    | products                 | networks      |
            | nameqa1 | tiernameqa1 | git=1.7                  | netqa1        |
            | nameqa2 | tiernameqa2 | git=1.7,mediawiki=1.17.0 | netqa1,netqa2 |

    @skip @CLAUDIA-3678
    Scenario: Delete nonexistent tier from an environment
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        When I request the deletion of the tier "tiernameqa" from the environment "nameqa"
        Then I receive a "Not Found" response
