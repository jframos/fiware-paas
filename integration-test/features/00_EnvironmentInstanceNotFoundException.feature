Feature: Create an environment Instance

    Scenario: Create Environment Instance NotFoundException
    Given Environment NFE OS:
    |OSType | OS Name| Version |
    | 95 | Debian | 5 |
    And Environment NFE default attributes:
    | Key | Value | Description |
    | key1 | value1 | description1 |
    | key2 | value2 | description2 |
    And Environment NFE Product Type:
    | Name | Description |
    | name1 | description1 |
    And Environment NFE the product releases:
    | Product | Version |
    | productA | 1.0 |
    And Environment NFE Tiers:
    | TierName | Initial | Maximum | Minimum |
    | tierName | 1 | 1 | 1 |
    And EnvironmentType:
    | Name | Description |
    | envName1 | envDescription1 |   
    When Environment NFE I create an environmentInstance "environmentName"
    Then Environment NFE There is an environmentInstance in the bbdd