# -*- coding: utf-8 -*-
Feature: Delete an instance of an environment in a tenant

    As a fi-ware user
    I want to be able to delete instances of the environments in a tenant
    so that I can manage them


    @happy_path
    Scenario Outline: Delete instance of an environment
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       |
            | <tiername> |
        And an environment has already been created with the previous tiers and data:
            | name      | description |
            | <envname> | descqa      |
        And an instance of the environment "<envname>" has already been created using data:
            | name   | description    |
            | <name> | instancedescqa |
        When I request the deletion of the instance "<name>"
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status

        Examples:
            | name            | envname | tiername    |
            | instancenameqa  | nameqa  | tiernameqa  |


    Scenario: Delete instance of an environment with two tiers
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        |
            | tiernameqa1 |
            | tiernameqa2 |
        And an environment has already been created with the previous tiers and data:
            | name      | description |
            | nameqa    | descqa      |
        And an instance of the environment "nameqa" has already been created using data:
            | name            | description    |
            | instancenameqa  | instancedescqa |
        When I request the deletion of the instance "instancenameqa"
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status


    Scenario Outline: Delete instance of an environment with networks
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       | networks    |
            | <tiername> | <networks>  |
        And an environment has already been created with the previous tiers and data:
            | name      | description |
            | <envname> | descqa      |
        And an instance of the environment "<envname>" has already been created using data:
            | name   | description    |
            | <name> | instancedescqa |
        When I request the deletion of the instance "<name>"
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status

        Examples:
            | name     | envname | tiername | networks |
            | qa-inst  | qa-env  | qa-tier  | qa-net   |


    Scenario Outline: Delete instance of an environment with products
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       | products   |
            | <tiername> | <products> |
        And an environment has already been created with the previous tiers and data:
            | name      | description |
            | <envname> | descqa      |
        And an instance of the environment "<envname>" has already been created using data:
            | name   | description    |
            | <name> | instancedescqa |
        When I request the deletion of the instance "<name>"
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status

        Examples:
            | name              | envname   | tiername      | products                    |
            | instancenameqa    | nameqa    | tiernameqa    | testing_paas_product=0.0.1  |


    Scenario Outline: Delete instance of an environment with products and networks
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       | products   | networks    |
            | <tiername> | <products> | <networks>  |
        And an environment has already been created with the previous tiers and data:
            | name      | description |
            | <envname> | descqa     |
        And an instance of the environment "<envname>" has already been created using data:
            | name   | description      |
            | <name> | instancedescqa  |
        When I request the deletion of the instance "<name>"
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status

        Examples:
            | name            | envname | tiername    | products                    | networks |
            | instancenameqa  | nameqa  | tiernameqa  | testing_paas_product=0.0.1  | netqa    |


    Scenario Outline: Delete instance of an environment with VM not registered in Chef-Server
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       | products   | networks    |
            | <tiername> | <products> | <networks>  |
        And an environment has already been created with the previous tiers and data:
            | name      | description |
            | <envname> | descqa      |
        And an instance of the environment "<envname>" has already been created using data:
            | name   | description     |
            | <name> | instancedescqa  |
        And I remove the node from Chef-Server
        When I request the deletion of the instance "<name>"
        Then I receive an "OK" response with a task
        And the task ends with "ERROR" status

        Examples:
            | name            | envname | tiername    | products                    | networks |
            | instancenameqa  | nameqa  | tiernameqa  | testing_paas_product=0.0.1  | netqa    |