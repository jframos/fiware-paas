Feature: Create an environment Instance

    Scenario: Create Environment Instance
    Given Environment OS:
    |OSType | OS Name| Version |
    | 95 | Debian | 5 |
    And Environment default attributes:
    | Key | Value | Description |
    | key1 | value1 | description1 |
    | key2 | value2 | description2 |
    And Environment Product Type:
    | Name | Description |
    | name1 | description1 |
    And Environment the product releases:
    | Product | Version |
    | productA | 1.0 |
    And Environment Tiers:
    | TierName | Initial | Maximum | Minimum |
    | tierName | 1 | 1 | 1 |
    And EnvironmentType:
    | Name | Description |
    | envName1 | envDescription1 |   
   When I create an environmentInstance "production"
    Then There is an environmentInstance in the bbdd