# -*- coding: utf-8 -*-
Feature: Deploy an environment on all Fiware regions
    As a fi-ware user
    I want to be able to deploy an environment on each Fiware region
    so that I can work with VMs in all of them


    @region_existence
    Scenario Outline: 01: Verify the existence of regions in the service catalog
      Given the region "<region>"
      When I retrieve the service catalog
      Then the region is in image endpoints list

      Examples:
      | region              |
      | Spain               |
      | Trento              |
      | Lannion             |
      | Waterford           |
      | Berlin              |
      | Prague              |
      | Mexico              |
      | Athens_Neuropublic  |
      | Athens_UPRC         |
      | Zurich              |
      | Karlskrona          |
      | NITOS-UTH           |
      | Budapest            |
      | Stockholm           |
      | C4I                 |
      | Poznan              |
      | Gent                |


    @sdc_aware_images
    Scenario Outline: 02: Validate if the region has SDC-aware images
      Given the region "<region>"
      And endpoints from service catalog
      When I retrieve the list of SDC-aware images
      Then the region has at least one SDC-aware image

      Examples:
      | region              |
      | Spain               |
      | Trento              |
      | Lannion             |
      | Waterford           |
      | Berlin              |
      | Prague              |
      | Mexico              |
      | Athens_Neuropublic  |
      | Athens_UPRC         |
      | Zurich              |
      | Karlskrona          |
      | NITOS-UTH           |
      | Budapest            |
      | Stockholm           |
      | C4I                 |
      | Poznan              |
      | Gent                |


    @flavors
    Scenario Outline: 03: Validate if the region has flavors
      Given the region "<region>"
      And endpoints from service catalog
      When I retrieve the list of flavors
      Then the region has at least one flavor

      Examples:
      | region              |
      | Spain               |
      | Trento              |
      | Lannion             |
      | Waterford           |
      | Berlin              |
      | Prague              |
      | Mexico              |
      | Athens_Neuropublic  |
      | Athens_UPRC         |
      | Zurich              |
      | Karlskrona          |
      | NITOS-UTH           |
      | Budapest            |
      | Stockholm           |
      | C4I                 |
      | Poznan              |
      | Gent                |


    @blueprints_no_products
    Scenario Outline: 04: Deploy Blueprints without products and without networks
      Given the region "<region>"
      And endpoints from service catalog
      And the region exists and it has valid images and flavors
      And a created environment with data:
            | name        | description           |
            | qa-testing  | For testing purposes  |
      And a created tiers with data:
            | name        |
            | qa-tier     |
      When I request the creation of an instance for that environment using data:
            | name        | description           |
            | qa-instance | For testing purposes  |
      Then I receive an "OK" response with a task
      And the task ends with "SUCCESS" status

      Examples:
      | region              |
      | Spain               |
      | Trento              |
      | Lannion             |
      | Waterford           |
      | Berlin              |
      | Prague              |
      | Mexico              |
      | Athens_Neuropublic  |
      | Athens_UPRC         |
      | Zurich              |
      | Karlskrona          |
      | NITOS-UTH           |
      | Budapest            |
      | Stockholm           |
      | C4I                 |
      | Poznan              |
      | Gent                |


    @blueprints_products_no_networks
    Scenario Outline: 05: Deploy Blueprints with products and no networks
      Given the region "<region>"
      And endpoints from service catalog
      And the region exists and it has valid SDC-aware images and flavors
      And a created environment with data:
            | name        | description           |
            | qa-testing  | For testing purposes  |
      And a created tiers with data:
            | name        | products |
            | qa-tier     | tomcat=6 |
      When I request the creation of an instance for that environment using data:
            | name        | description           |
            | qa-instance | For testing purposes  |
      Then I receive an "OK" response with a task
      And the task ends with "SUCCESS" status

      Examples:
      | region              |
      | Spain               |
      | Trento              |
      | Lannion             |
      | Waterford           |
      | Berlin              |
      | Prague              |
      | Mexico              |
      | Athens_Neuropublic  |
      | Athens_UPRC         |
      | Zurich              |
      | Karlskrona          |
      | NITOS-UTH           |
      | Budapest            |
      | Stockholm           |
      | C4I                 |
      | Poznan              |
      | Gent                |


    @blueprints_products
    Scenario Outline: 06: Deploy Blueprints with products and networks
      Given the region "<region>"
      And endpoints from service catalog
      And the region exists and it has valid SDC-aware images and flavors
      And a created environment with data:
            | name        | description           |
            | qa-testing  | For testing purposes  |
      And a created tiers with data:
            | name        | networks   | products |
            | qa-tier     | qa-network | tomcat=6 |
      When I request the creation of an instance for that environment using data:
            | name        | description           |
            | qa-instance | For testing purposes  |
      Then I receive an "OK" response with a task
      And the task ends with "SUCCESS" status

      Examples:
      | region              |
      | Spain               |
      | Trento              |
      | Lannion             |
      | Waterford           |
      | Berlin              |
      | Prague              |
      | Mexico              |
      | Athens_Neuropublic  |
      | Athens_UPRC         |
      | Zurich              |
      | Karlskrona          |
      | NITOS-UTH           |
      | Budapest            |
      | Stockholm           |
      | C4I                 |
      | Poznan              |
      | Gent                |
