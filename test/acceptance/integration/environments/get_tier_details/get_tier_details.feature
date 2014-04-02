# -*- coding: utf-8 -*-
Feature: Get the details of a tier of an environment in a tenant

    As a fi-ware user
    I want to be able to the details of a tier of an environment in a tenant
    so that I have access to its data

    @happy_path
    Scenario: Get the details of a tier
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        And a tier has already been added to the environment "nameqa" with data:
            | name       |
            | tiernameqa |
        When I request the details of the tier "tiernameqa" of the environment "nameqa"
        Then I receive an "OK" response with data:
            | name       |
            | tiernameqa |
                
    Scenario Outline: Get the details of a tier with different valid data
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        And a tier has already been added to the environment "<name>" with data:
            | name       |
            | <tiername> |
        When I request the details of the tier "<tiername>" of the environment "<name>"
        Then I receive an "OK" response with data:
            | name       |
            | <tiername> |
        
        Examples:
            | name    | tiername                |
            | nameqa1 | a                       |
            | nameqa2 | nameqa-1                |
            | nameqa3 | [STRING_WITH_LENGTH_30] |
                
    Scenario Outline: Get the details of a tier with products
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        And a tier has already been added to the environment "<name>" with data:
            | name       | products   |
            | <tiername> | <products> |
        When I request the details of the tier "<tiername>" of the environment "<name>"
        Then I receive an "OK" response with data:
            | name       | products   |
            | <tiername> | <products> |
        
        Examples:
            | name    | tiername    | products                 |
            | nameqa1 | tiernameqa1 | git=1.7                  |
            | nameqa2 | tiernameqa2 | git=1.7,mediawiki=1.17.0 |
                
    Scenario Outline: Get the details of a tier with networks
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        And a tier has already been added to the environment "<name>" with data:
            | name       | networks   |
            | <tiername> | <networks> |
        When I request the details of the tier "<tiername>" of the environment "<name>"
        Then I receive an "OK" response with data:
            | name       | networks   |
            | <tiername> | <networks> |
        
        Examples:
            | name    | tiername    | networks      |
            | nameqa1 | tiernameqa1 | netqa1        |
            | nameqa2 | tiernameqa2 | netqa1,netqa2 |
                
    Scenario Outline: Get the details of a tier with products and networks
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | <name> | descqa      |
        And a tier has already been added to the environment "<name>" with data:
            | name       | products   | networks   |
            | <tiername> | <products> | <networks> |
        When I request the details of the tier "<tiername>" of the environment "<name>"
        Then I receive an "OK" response with data:
            | name       | products   | networks   |
            | <tiername> | <products> | <networks> |
        
        Examples:
            | name    | tiername    | products                 | networks      |
            | nameqa1 | tiernameqa1 | git=1.7                  | netqa1        |
            | nameqa2 | tiernameqa2 | git=1.7,mediawiki=1.17.0 | netqa1,netqa2 |
                
    Scenario: Get the details of a nonexistent tier
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        When I request the details of the tier "tiernameqa" of the environment "nameqa"
        Then I receive a "Not Found" response
