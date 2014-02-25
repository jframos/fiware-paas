# -*- coding: utf-8 -*-
Feature: Create an instance of an environment in a tenant

    As a fi-ware user
    I want to be able to create instances of the environments in a tenant
    so that I can use them and work with them

    @happy_path @slow
    Scenario: Create instance of an environment with one tier
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       |
            | tiernameqa |
        And an environment has already been created with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        When I request the creation of an instance of the environment "nameqa" using data:
            | name           | description    |
            | instancenameqa | instancedescqa |
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status
        
    @slow
    Scenario: Create instance of an environment with several tiers
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        |
            | tiernameqa1 |
            | tiernameqa2 |
            | tiernameqa3 |
        And an environment has already been created with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        When I request the creation of an instance of the environment "nameqa" using data:
            | name           | description    |
            | instancenameqa | instancedescqa |
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status

    @slow
    Scenario Outline: Create instance of an environment with tiers with different valid data (1/3)
    # Split into three parts due to OpenStack limits that cannot be easily overridden
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name                    |
            | a                       |
            | tiernameqa-1            |
            | [STRING_WITH_LENGTH_30] |
        And an environment has already been created with the previous tiers and data:
            | name      | description |
            | <envname> | <envdesc>   |
        When I request the creation of an instance of the environment "<envname>" using data:
            | name   | description    |
            | <name> | instancedescqa |
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status
        
        Examples:
            | name            | envname                 | envdesc                  |
            | instancenameqa1 | a                       | descqa                   |
            | instancenameqa2 | nameqa-1                | descqa                   |
            | instancenameqa3 | [STRING_WITH_LENGTH_30] | descqa                   |
        
    @slow
    Scenario Outline: Create instance of an environment with tiers with different valid data (2/3)
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name                    |
            | a                       |
            | tiernameqa-1            |
            | [STRING_WITH_LENGTH_30] |
        And an environment has already been created with the previous tiers and data:
            | name      | description |
            | <envname> | <envdesc>   |
        When I request the creation of an instance of the environment "<envname>" using data:
            | name   | description    |
            | <name> | instancedescqa |
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status
        
        Examples:
            | name            | envname                 | envdesc                  |
            | instancenameqa4 | nameqa1                 | a                        |
            | instancenameqa5 | nameqa2                 | descqa-1                 |
            | instancenameqa6 | nameqa3                 | Symbols: +*=._,;"@#%)/?! |
        
    @slow
    Scenario Outline: Create instance of an environment with tiers with different valid data (3/3)
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name                    |
            | a                       |
            | tiernameqa-1            |
            | [STRING_WITH_LENGTH_30] |
        And an environment has already been created with the previous tiers and data:
            | name      | description |
            | <envname> | <envdesc>   |
        When I request the creation of an instance of the environment "<envname>" using data:
            | name   | description    |
            | <name> | instancedescqa |
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status
        
        Examples:
            | name            | envname                 | envdesc                  |
            | instancenameqa7 | nameqa4                 | Non-ASCII: á.é.í.ñ       |
            | instancenameqa8 | nameqa5                 | [STRING_WITH_LENGTH_256] |
        
    @slow
    Scenario Outline: Create instance with different valid data of an environment with tiers
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       |
            | tiernameqa |
        And an environment has already been created with the previous tiers and data:
            | name      | description |
            | <envname> | descqa      |
        When I request the creation of an instance of the environment "<envname>" using data:
            | name   | description   |
            | <name> | <description> |
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status
        
        Examples:
            | name                    | description              | envname |
            | a                       | descqa                   | nameqa1 |
            | instancenameqa-1        | descqa                   | nameqa2 |
            | [STRING_WITH_LENGTH_30] | descqa                   | nameqa3 |
            | instancenameqa1         | a                        | nameqa4 |
            | instancenameqa2         | descqa-1                 | nameqa5 |
            | instancenameqa3         | Symbols: +*=._,;"@#%)/?! | nameqa6 |
            | instancenameqa4         | Non-ASCII: á.é.í.ñ       | nameqa7 |
            # skip, CLAUDIA-3663
            # | instancenameqa5         | [STRING_WITH_LENGTH_256] | nameqa8 |
        

    @slow @skip @CLAUDIA-3663
    Scenario: Create instance with different invalid data of an environment with tiers
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       |
            | tiernameqa |
        And an environment has already been created with the previous tiers and data:
            | name      | description |
            | <envname> | descqa      |
        When I request the creation of an instance of the environment "<envname>" using data:
            | name   | description   |
            | <name> | <description> |
        Then I receive a "Bad Request" response
        
        Examples:
            | name                    | description              | envname |
            | nameqa_1                | descqa                   | nameqa1 |
            | [STRING_WITH_LENGTH_31] | descqa                   | nameqa2 |
            |                         | descqa                   | nameqa3 |
            | [MISSING_PARAM]         | descqa                   | nameqa4 |
            | instancenameqa1         | [STRING_WITH_LENGTH_257] | nameqa5 |
            | instancenameqa2         |                          | nameqa6 |
            | instancenameqa3         | [MISSING_PARAM]          | nameqa7 |

    @slow
    Scenario Outline: Create instance of an environment with one tier with products
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       | products   |
            | <tiername> | <products> |
        And an environment has already been created with the previous tiers and data:
            | name      | description |
            | <envname> | descqa      |
        When I request the creation of an instance of the environment "<envname>" using data:
            | name   | description    |
            | <name> | instancedescqa |
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status

        Examples:
            | name            | envname | tiername    | products                 |
            | instancenameqa1 | nameqa1 | tiernameqa1 | git=1.7                  |
            | instancenameqa2 | nameqa2 | tiernameqa2 | git=1.7,mediawiki=1.17.0 |

    @slow
    Scenario: Create instance of an environment with several tiers with products
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        | products         |
            | tiernameqa1 | git=1.7          |
            | tiernameqa2 | mediawiki=1.17.0 |
        And an environment has already been created with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        When I request the creation of an instance of the environment "nameqa" using data:
            | name           | description    |
            | instancenameqa | instancedescqa |
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status

    @slow
    Scenario Outline: Create instance of an environment with one tier with networks
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       | networks   |
            | <tiername> | <networks> |
        And an environment has already been created with the previous tiers and data:
            | name      | description |
            | <envname> | descqa      |
        When I request the creation of an instance of the environment "<envname>" using data:
            | name   | description    |
            | <name> | instancedescqa |
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status

        Examples:
            | name            | envname | tiername    | networks      |
            | instancenameqa1 | nameqa1 | tiernameqa1 | netqa1        |
            # skip, CLAUDIA-3673 (workaround below)
            # | instancenameqa2 | nameqa2 | tiernameqa2 | netqa1,netqa2 |
            # skip, CLAUDIA-3702
            # | instancenameqa2 | nameqa2 | tiernameqa2 | netqa2,netqa3 |

    @slow @skip @CLAUDIA-3702
    Scenario: Create instance of an environment with several tiers with networks
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        | networks      |
            | tiernameqa1 | netqa1        |
            | tiernameqa2 | netqa2 |
        And an environment has already been created with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        When I request the creation of an instance of the environment "nameqa" using data:
            | name           | description    |
            | instancenameqa | instancedescqa |
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status

    @slow
    Scenario Outline: Create instance of an environment with one tier with products and networks
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       | products   | networks   |
            | <tiername> | <products> | <networks> |
        And an environment has already been created with the previous tiers and data:
            | name      | description |
            | <envname> | descqa      |
        When I request the creation of an instance of the environment "<envname>" using data:
            | name   | description    |
            | <name> | instancedescqa |
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status

        Examples:
            | name            | envname | tiername    | products                 | networks      |
            | instancenameqa1 | nameqa1 | tiernameqa1 | git=1.7                  | netqa1        |
            # skip, CLAUDIA-3673 (workaround below)
            # | instancenameqa2 | nameqa2 | tiernameqa2 | git=1.7,mediawiki=1.17.0 | netqa1,netqa2 |
            # skip, CLAUDIA-3702
            # | instancenameqa2 | nameqa2 | tiernameqa2 | git=1.7,mediawiki=1.17.0 | netqa2,netqa3 |

    @slow @skip @CLAUDIA-3702
    Scenario: Create instance of an environment with tiers with products and networks
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name        | products         | networks |
            | tiernameqa1 | git=1.7          | netqa1   |
            | tiernameqa2 | mediawiki=1.17.0 | netqa2   |
        And an environment has already been created with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        When I request the creation of an instance of the environment "nameqa" using data:
            | name           | description    |
            | instancenameqa | instancedescqa |
        Then I receive an "OK" response with a task
        And the task ends with "SUCCESS" status

    Scenario: Create instance with the same name of an already existing one
        Given the paas manager is up and properly configured
        And a list of tiers has been defined with data:
            | name       |
            | tiernameqa |
        And an environment has already been created with the previous tiers and data:
            | name   | description |
            | nameqa | descqa      |
        And an instance of the environment "nameqa" has already been created using data:
            | name           | description    |
            | instancenameqa | instancedescqa |
        When I request the creation of an instance of the environment "nameqa" using data:
            | name           | description    |
            | instancenameqa | instancedescqa |
        Then I receive a "Conflict" response

    @skip @CLAUDIA-3705
    Scenario: Create instance of an environment without tiers
        Given the paas manager is up and properly configured
        And an environment has already been created with data:
            | name   | description |
            | nameqa | descqa      |
        When I request the creation of an instance of the environment "nameqa" using data:
            | name       | description |
            | instanceqa | instdescqa  |
        Then I receive a "Bad Request" response
