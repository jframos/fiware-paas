Feature: Create an Application Instance

    Scenario: Create Application Instance
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
    And Environment Type:
    | Name | Description |
    | Java-Spring Environment | Java-Spring Env description  |
    And an Application Release "HelloWorld" and "1.0"
    And Application Type:
    | appTypeName | appTypeDescription | envTypeName |
    | Java-Spring Application | Java-Spring Env description | Java-Spring Environment |
    And Application Release attributes:
    | Key | Value | Description |
    | key1 | value1 | description1 |
    | key2 | value2 | description2 |
    And Product Type:
    | Name | Description |
    | ApplicationWebServer | Application Web Server description |
    And Artifact Types:
    | Name | Description | ProductType |
    | war | war Descriptor | ApplicationWebServer |
    And Application Release artifacts:
    | Name | Path | ArtifactType | ProductName | ProductVersion |
    | war | /opt/war/releases/80f5053b166c69d81697ba21113c673f8372aca0.war | AWSDescriptor | tomcat | 7 |
    | HelloWorldAWSDescriptor | /opt/war/releases/80f5053b166c69d81697ba21113c673f8372aca0.xml | AWSDescriptor | tomcat | 7 |
    When I install an applicationInstance
    Then There is an applicationInstance in the bbdd