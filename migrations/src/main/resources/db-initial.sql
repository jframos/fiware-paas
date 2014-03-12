--liquibase formatted sql

--changeset initial:1
CREATE SEQUENCE hibernate_sequence;

--changeset initial:2
CREATE TABLE applicationinstance (
  id                     INT8 NOT NULL,
  applicationrelease_id  VARCHAR(255),
  environmentinstance_id INT8
);

--changeset initial:3
CREATE TABLE applicationrelease (
  id                 VARCHAR(255) NOT NULL,
  description        VARCHAR(2048),
  name               VARCHAR(256) NOT NULL,
  applicationtype_id INT8,
  version            VARCHAR(256) NOT NULL

);

--changeset initial:4

CREATE TABLE applicationrelease_applicationrelease (
  applicationrelease_id  VARCHAR(255) NOT NULL,
  transitablereleases_id VARCHAR(255) NOT NULL
);

--changeset initial:5
CREATE TABLE applicationrelease_artifact (
  applicationrelease_id VARCHAR(255) NOT NULL,
  artifacts_id          INT8         NOT NULL
);

--changeset initial:6
CREATE TABLE applicationtype (
  id          INT8         NOT NULL,
  description VARCHAR(2048),
  name        VARCHAR(256) NOT NULL
);


--changeset initial:7
CREATE TABLE applicationtype_environmenttype (
  applicationtype_id  INT8 NOT NULL,
  environmenttypes_id INT8 NOT NULL
);


--changeset initial:8
CREATE TABLE artifact (
  id                INT8         NOT NULL,
  name              VARCHAR(256) NOT NULL,
  path              VARCHAR(2048),
  artifacttype_id   INT8,
  productrelease_id INT8
);

--changeset initial:9
CREATE TABLE artifact_artifact (
  artifact_id   INT8 NOT NULL,
  attributes_id INT8 NOT NULL
);

--changeset initial:10
CREATE TABLE artifacttype (
  id             INT8         NOT NULL,
  description    VARCHAR(2048),
  name           VARCHAR(256) NOT NULL,
  producttype_id INT8
);

--changeset initial:11

CREATE TABLE attribute (
  id          INT8          NOT NULL,
  description VARCHAR(2048),
  key         VARCHAR(256)  NOT NULL,
  v           INT8,
  value       VARCHAR(2048) NOT NULL
);
--changeset initial:12

CREATE TABLE configuration (

  id    INT8         NOT NULL,
  key   VARCHAR(64)  NOT NULL,
  value VARCHAR(256) NOT NULL
);
--changeset initial:13

CREATE TABLE configuration_properties (
  key       VARCHAR(255) NOT NULL,
  namespace VARCHAR(255) NOT NULL,
  value     VARCHAR(32672)

);

--changeset initial:14
CREATE TABLE environment (
  id                 INT8         NOT NULL,
  description        VARCHAR(256),
  name               VARCHAR(256) NOT NULL,
  org                VARCHAR(256) NOT NULL,
  ovf                VARCHAR(90000),
  vdc                VARCHAR(256),
  environmenttype_id INT8
);

--changeset initial:15
CREATE TABLE environment_has_tiers (
  environment_id INT8 NOT NULL,
  tier_id        INT8 NOT NULL
);

--changeset initial:16

CREATE TABLE environmentinstance (
  id             INT8 NOT NULL,
  blueprintname  VARCHAR(255),
  description    VARCHAR(255),
  vapp           VARCHAR(90000),
  environment_id INT8,
  taskid         VARCHAR(255)
);

--changeset initial:17

CREATE TABLE environmentinstance_has_tierinstances (
  environmentinstance_id INT8 NOT NULL,
  tierinstance_id        INT8 NOT NULL
);

--changeset initial:18
CREATE TABLE environmenttype (
  id          INT8         NOT NULL,
  description VARCHAR(2048),
  name        VARCHAR(256) NOT NULL
);

--changeset initial:19
CREATE TABLE installableinstance (
  id     INT8         NOT NULL,
  date   TIMESTAMP WITHOUT TIME ZONE,
  name   VARCHAR(256) NOT NULL,
  status VARCHAR(255),
  vdc    VARCHAR(255)
);

--changeset initial:20
CREATE TABLE installableinstance_attribute (
  installableinstance_id INT8 NOT NULL,
  privateattributes_id   INT8 NOT NULL
);


--changeset initial:21
CREATE TABLE os (
  id          INT8       NOT NULL,
  description VARCHAR(2048),
  name        VARCHAR(128),
  ostype      VARCHAR(3) NOT NULL,
  v           INT8,
  version     VARCHAR(128)
);

--changeset initial:22
CREATE TABLE productinstance (
  id                INT8 NOT NULL,
  productrelease_id INT8,
  taskid            VARCHAR(255)
);
--changeset initial:23
CREATE TABLE productrelease (
  id             INT8         NOT NULL,
  description    VARCHAR(2048),
  name           VARCHAR(256) NOT NULL,
  product        VARCHAR(256) NOT NULL,
  version        VARCHAR(256) NOT NULL,
  withartifact   BOOL,
  producttype_id INT8
);

--changeset initial:24
CREATE TABLE productrelease_attribute (
  productrelease_id INT8 NOT NULL,
  attributes_id     INT8 NOT NULL
);

--changeset initial:25
CREATE TABLE productrelease_has_ooss (
  productrelease_id INT8 NOT NULL,
  supportedooss_id  INT8 NOT NULL
);

--changeset initial:26
CREATE TABLE productrelease_productrelease (
  productrelease_id      INT8 NOT NULL,
  transitablereleases_id INT8 NOT NULL
);

--changeset initial:27
CREATE TABLE producttype (
  id          INT8         NOT NULL,
  description VARCHAR(2048),
  name        VARCHAR(256) NOT NULL

);


--changeset initial:28
CREATE TABLE rule (
  id          INT8 NOT NULL,
  cidr        VARCHAR(255),
  fromport    VARCHAR(255),
  idparent    VARCHAR(255),
  idrule      VARCHAR(255),
  ipprotocol  VARCHAR(255),
  sourcegroup VARCHAR(255),
  toport      VARCHAR(255)
);

--changeset initial:29
CREATE TABLE securitygroup (
  id              INT8         NOT NULL,
  description     VARCHAR(255),
  idsecuritygroup VARCHAR(255),
  name            VARCHAR(256) NOT NULL
);

--changeset initial:30
CREATE TABLE securitygroup_rule (
  securitygroup_id INT8 NOT NULL,
  rules_id         INT8 NOT NULL

);
--changeset initial:31

CREATE TABLE service (
  id          INT8         NOT NULL,
  description VARCHAR(2048),
  name        VARCHAR(256) NOT NULL
);

--changeset initial:32
CREATE TABLE service_attribute (
  service_id    INT8 NOT NULL,
  attributes_id INT8 NOT NULL
);
--changeset initial:33

CREATE TABLE task (
  id                      INT8 NOT NULL,
  description             VARCHAR(1024),
  endtime                 TIMESTAMP WITHOUT TIME ZONE,
  environment             VARCHAR(1024),
  majorerrorcode          VARCHAR(1024),
  message                 VARCHAR(1024),
  minorerrorcode          VARCHAR(255),
  venodrspecificerrorcode VARCHAR(255),
  expiretime              INT8,
  owner_href              VARCHAR(255),
  owner_name              VARCHAR(255),
  owner_type              VARCHAR(255),
  result_href             VARCHAR(255),
  result_name             VARCHAR(255),
  result_type             VARCHAR(255),
  starttime               TIMESTAMP WITHOUT TIME ZONE,
  status                  INT4,
  tier                    VARCHAR(1024),
  vdc                     VARCHAR(1024)

);

--changeset initial:34
CREATE TABLE template (
  id              INT8         NOT NULL,
  name            VARCHAR(256) NOT NULL,
  tierinstance_id INT8
);

--changeset initial:35
CREATE TABLE tier (
  id                     INT8         NOT NULL,
  environmentname        VARCHAR(255),
  flavour                VARCHAR(255),
  floatingip             VARCHAR(255),
  icono                  VARCHAR(255),
  image                  VARCHAR(255),
  initialnumberinstances INT4,
  keypair                VARCHAR(255),
  maximumnumberinstances INT4,
  minimumnumberinstances INT4,
  name                   VARCHAR(256) NOT NULL,
  payload                VARCHAR(90000),
  vdc                    VARCHAR(255),
  securitygroup_id       INT8

);

--changeset initial:36

CREATE TABLE tier_has_productreleases (
  tier_id           INT8 NOT NULL,
  productrelease_id INT8 NOT NULL
);

--changeset initial:37
CREATE TABLE tierinstance (
  numberreplica INT4 NOT NULL,
  ovf           VARCHAR(100000),
  vapp          VARCHAR(10000),
  domain        VARCHAR(128),
  fqn           VARCHAR(512),
  hostname      VARCHAR(128),
  networks      VARCHAR(512),
  ostype        VARCHAR(8),
  vmid          VARCHAR(512),
  id            INT8 NOT NULL,
  tier_id       INT8,
  taskid        VARCHAR(255),
  ip            VARCHAR(128)
);
--changeset initial:38

CREATE TABLE tierinstance_has_productinstances (
  tierinstance_id    INT8 NOT NULL,
  productinstance_id INT8 NOT NULL

);

--changeset initial:39
ALTER TABLE applicationinstance ADD CONSTRAINT applicationinstance_pkey PRIMARY KEY (id);

--changeset initial:40
ALTER TABLE applicationrelease ADD CONSTRAINT applicationrelease_pkey PRIMARY KEY (id);

--changeset initial:41
ALTER TABLE applicationtype ADD CONSTRAINT applicationtype_pkey PRIMARY KEY (id);

--changeset initial:42
ALTER TABLE artifact ADD CONSTRAINT artifact_pkey PRIMARY KEY (id);

--changeset initial:43
ALTER TABLE artifacttype ADD CONSTRAINT artifacttype_pkey PRIMARY KEY (id);

--changeset initial:44
ALTER TABLE attribute ADD CONSTRAINT attribute_pkey PRIMARY KEY (id);

--changeset initial:45
ALTER TABLE configuration ADD CONSTRAINT configuration_pkey PRIMARY KEY (id);

--changeset initial:46
ALTER TABLE configuration_properties ADD CONSTRAINT configuration_properties_pkey PRIMARY KEY (key, namespace);

--changeset initial:47
ALTER TABLE environment ADD CONSTRAINT environment_pkey PRIMARY KEY (id);

--changeset initial:48
ALTER TABLE environmentinstance ADD CONSTRAINT environmentinstance_pkey PRIMARY KEY (id);

--changeset initial:49
ALTER TABLE environmenttype ADD CONSTRAINT environmenttype_pkey PRIMARY KEY (id);

--changeset initial:50
ALTER TABLE installableinstance ADD CONSTRAINT installableinstance_pkey PRIMARY KEY (id);

--changeset initial:51
ALTER TABLE os ADD CONSTRAINT os_pkey PRIMARY KEY (id);

--changeset initial:52
ALTER TABLE productinstance ADD CONSTRAINT productinstance_pkey PRIMARY KEY (id);

--changeset initial:53
ALTER TABLE productrelease ADD CONSTRAINT productrelease_pkey PRIMARY KEY (id);

--changeset initial:54
ALTER TABLE producttype ADD CONSTRAINT producttype_pkey PRIMARY KEY (id);

--changeset initial:55
ALTER TABLE rule ADD CONSTRAINT rule_pkey PRIMARY KEY (id);

--changeset initial:56
ALTER TABLE securitygroup ADD CONSTRAINT securitygroup_pkey PRIMARY KEY (id);

--changeset initial:57
ALTER TABLE service ADD CONSTRAINT service_pkey PRIMARY KEY (id);

--changeset initial:58
ALTER TABLE task ADD CONSTRAINT task_pkey PRIMARY KEY (id);

--changeset initial:59
ALTER TABLE template ADD CONSTRAINT template_pkey PRIMARY KEY (id);

--changeset initial:60
ALTER TABLE tier ADD CONSTRAINT tier_pkey PRIMARY KEY (id);

--changeset initial:61
ALTER TABLE tierinstance ADD CONSTRAINT tierinstance_pkey PRIMARY KEY (id);

--changeset initial:62
ALTER TABLE tierinstance_has_productinstances ADD CONSTRAINT fk_tierinstance_has_productinstances_tierinstance FOREIGN KEY (tierinstance_id)
REFERENCES tierinstance (id) NOT DEFERRABLE;

--changeset initial:63
ALTER TABLE tierinstance_has_productinstances ADD CONSTRAINT fk_tierinstance_has_productinstances_productinstance FOREIGN KEY (productinstance_id)
REFERENCES productinstance (id) NOT DEFERRABLE;

--changeset initial:64
ALTER TABLE tierinstance ADD CONSTRAINT fk_tierinstance_has_productinstances_tier FOREIGN KEY (tier_id)
REFERENCES tier (id) NOT DEFERRABLE;

--changeset initial:65
ALTER TABLE tierinstance ADD CONSTRAINT fk_tierinstance_installableinstance FOREIGN KEY (id)
REFERENCES installableinstance (id) NOT DEFERRABLE;

--changeset initial:66
ALTER TABLE environmentinstance_has_tierinstances ADD CONSTRAINT fk_environmentinstance_has_tierinstances_tierinstance FOREIGN KEY (tierinstance_id)
REFERENCES tierinstance (id) NOT DEFERRABLE;

--changeset initial:67
ALTER TABLE environmentinstance_has_tierinstances ADD CONSTRAINT fk_environmentinstance_has_tierinstances FOREIGN KEY (environmentinstance_id)
REFERENCES environmentinstance (id) NOT DEFERRABLE;

--changeset initial:68
ALTER TABLE tier_has_productreleases ADD CONSTRAINT fk_tier_has_productreleases_tier FOREIGN KEY (tier_id)
REFERENCES tier (id) NOT DEFERRABLE;

--changeset initial:69
ALTER TABLE tier_has_productreleases ADD CONSTRAINT fk_tier_has_productreleases_productrelease FOREIGN KEY (productrelease_id)
REFERENCES productrelease (id) NOT DEFERRABLE;

--changeset initial:70
ALTER TABLE tier ADD CONSTRAINT fk_tier_securitygroup FOREIGN KEY (securitygroup_id)
REFERENCES securitygroup (id) NOT DEFERRABLE;

--changeset initial:71
ALTER TABLE productrelease_productrelease ADD CONSTRAINT fk_productrelease_productrelease_transitablereleases FOREIGN KEY (transitablereleases_id)
REFERENCES productrelease (id) NOT DEFERRABLE;

--changeset initial:72
ALTER TABLE productrelease_productrelease ADD CONSTRAINT fk_productrelease_productrelease_productrelease FOREIGN KEY (productrelease_id)
REFERENCES productrelease (id) NOT DEFERRABLE;

--changeset initial:73
ALTER TABLE productrelease_has_ooss ADD CONSTRAINT fk_productrelease_has_ooss_os FOREIGN KEY (supportedooss_id)
REFERENCES os (id) NOT DEFERRABLE;

--changeset initial:74
ALTER TABLE productrelease_has_ooss ADD CONSTRAINT fk_productrelease_has_ooss_productrelease FOREIGN KEY (productrelease_id)
REFERENCES productrelease (id) NOT DEFERRABLE;

--changeset initial:75
ALTER TABLE artifact_artifact ADD CONSTRAINT fk_artifact_artifact_artifact FOREIGN KEY (attributes_id)
REFERENCES artifact (id) NOT DEFERRABLE;


--changeset initial:76
ALTER TABLE artifact_artifact ADD CONSTRAINT fk_artifact_artifact_artifact2 FOREIGN KEY (artifact_id)
REFERENCES artifact (id) NOT DEFERRABLE;

--changeset initial:77
ALTER TABLE environmentinstance ADD CONSTRAINT fk_environmentinstance_environment FOREIGN KEY (environment_id)
REFERENCES environment (id) NOT DEFERRABLE;

--changeset initial:78
ALTER TABLE environmentinstance ADD CONSTRAINT fk_environmentinstance_installableinstance FOREIGN KEY (id)
REFERENCES installableinstance (id) NOT DEFERRABLE;

--changeset initial:79
ALTER TABLE environment ADD CONSTRAINT fk_environment_environmenttype FOREIGN KEY (environmenttype_id)
REFERENCES environmenttype (id) NOT DEFERRABLE;

--changeset initial:80
ALTER TABLE applicationinstance ADD CONSTRAINT fk_applicationinstance_installableinstance FOREIGN KEY (id)
REFERENCES installableinstance (id) NOT DEFERRABLE;

--changeset initial:81
ALTER TABLE applicationinstance ADD CONSTRAINT fk_applicationinstance_environmentinstance FOREIGN KEY (environmentinstance_id)
REFERENCES environmentinstance (id) NOT DEFERRABLE;

--changeset initial:82
ALTER TABLE applicationinstance ADD CONSTRAINT fk_applicationinstance_applicationrelease FOREIGN KEY (applicationrelease_id)
REFERENCES applicationrelease (id) NOT DEFERRABLE;

--changeset initial:83
ALTER TABLE applicationrelease_applicationrelease ADD CONSTRAINT fk_applicationrelease_applicationrelease_applicationrelease FOREIGN KEY (applicationrelease_id)
REFERENCES applicationrelease (id) NOT DEFERRABLE;

--changeset initial:84
ALTER TABLE applicationrelease_applicationrelease ADD CONSTRAINT fk_applicationrelease_applicationrelease_applicationrelease2 FOREIGN KEY (transitablereleases_id)
REFERENCES applicationrelease (id) NOT DEFERRABLE;

--changeset initial:85
ALTER TABLE template ADD CONSTRAINT fk_template_tierinstance FOREIGN KEY (tierinstance_id)
REFERENCES tierinstance (id) NOT DEFERRABLE;

--changeset initial:86
ALTER TABLE artifact ADD CONSTRAINT fk_artifact_artifacttype FOREIGN KEY (artifacttype_id)
REFERENCES artifacttype (id) NOT DEFERRABLE;

--changeset initial:87
ALTER TABLE artifact ADD CONSTRAINT fk_artifact_productrelease FOREIGN KEY (productrelease_id)
REFERENCES productrelease (id) NOT DEFERRABLE;

--changeset initial:88
ALTER TABLE applicationrelease ADD CONSTRAINT fk_applicationrelease_applicationtype FOREIGN KEY (applicationtype_id)
REFERENCES applicationtype (id) NOT DEFERRABLE;

--changeset initial:89
ALTER TABLE applicationtype_environmenttype ADD CONSTRAINT fk_applicationtype_environmenttype_environmenttype FOREIGN KEY (environmenttypes_id)
REFERENCES environmenttype (id) NOT DEFERRABLE;

--changeset initial:90
ALTER TABLE applicationtype_environmenttype ADD CONSTRAINT fk_applicationtype_environmenttype_applicationtype FOREIGN KEY (applicationtype_id)
REFERENCES applicationtype (id) NOT DEFERRABLE;

--changeset initial:91
ALTER TABLE installableinstance_attribute ADD CONSTRAINT fk_installableinstance_attribute_installableinstance FOREIGN KEY (installableinstance_id)
REFERENCES installableinstance (id) NOT DEFERRABLE;

--changeset initial:92
ALTER TABLE installableinstance_attribute ADD CONSTRAINT fk_installableinstance_attribute_attribute FOREIGN KEY (privateattributes_id)
REFERENCES attribute (id) NOT DEFERRABLE;

--changeset initial:93
ALTER TABLE artifacttype ADD CONSTRAINT fk_artifacttype_producttype FOREIGN KEY (producttype_id)
REFERENCES producttype (id) NOT DEFERRABLE;

--changeset initial:94
ALTER TABLE securitygroup_rule ADD CONSTRAINT fk_securitygroup_rule_rule FOREIGN KEY (rules_id)
REFERENCES rule (id) NOT DEFERRABLE;

--changeset initial:95
ALTER TABLE securitygroup_rule ADD CONSTRAINT fk_securitygroup_rule_securitygroup FOREIGN KEY (securitygroup_id)
REFERENCES securitygroup (id) NOT DEFERRABLE;

--changeset initial:96
ALTER TABLE productrelease_attribute ADD CONSTRAINT fk_productrelease_attribute FOREIGN KEY (attributes_id)
REFERENCES attribute (id) NOT DEFERRABLE;

--changeset initial:97
ALTER TABLE productrelease_attribute ADD CONSTRAINT fk_productrelease_productrelease FOREIGN KEY (productrelease_id)
REFERENCES productrelease (id) NOT DEFERRABLE;

--changeset initial:98
ALTER TABLE productinstance ADD CONSTRAINT fk_productinstance_installableinstance FOREIGN KEY (id)
REFERENCES installableinstance (id) NOT DEFERRABLE;

--changeset initial:99
ALTER TABLE productinstance ADD CONSTRAINT fk_productinstance_productrelease FOREIGN KEY (productrelease_id)
REFERENCES productrelease (id) NOT DEFERRABLE;

--changeset initial:100
ALTER TABLE service_attribute ADD CONSTRAINT fk_service_attribute_service FOREIGN KEY (service_id)
REFERENCES service (id) NOT DEFERRABLE;

--changeset initial:101
ALTER TABLE service_attribute ADD CONSTRAINT fk_service_attribute_attribute FOREIGN KEY (attributes_id)
REFERENCES attribute (id) NOT DEFERRABLE;

--changeset initial:102
ALTER TABLE applicationrelease_artifact ADD CONSTRAINT fk_applicationrelease_artifact_applicationrelease FOREIGN KEY (applicationrelease_id)
REFERENCES applicationrelease (id) NOT DEFERRABLE;

--changeset initial:103
ALTER TABLE applicationrelease_artifact ADD CONSTRAINT fk_applicationrelease_artifact_artifact FOREIGN KEY (artifacts_id)
REFERENCES artifact (id) NOT DEFERRABLE;

--changeset initial:104
ALTER TABLE environment_has_tiers ADD CONSTRAINT fk_environment_has_tiers_tier FOREIGN KEY (tier_id)
REFERENCES tier (id) NOT DEFERRABLE;

--changeset initial:105
ALTER TABLE environment_has_tiers ADD CONSTRAINT fk_environment_has_tiers_environment FOREIGN KEY (environment_id)
REFERENCES environment (id) NOT DEFERRABLE;

--changeset initial:106
ALTER TABLE productrelease ADD CONSTRAINT fk_productrelease_producttype FOREIGN KEY (producttype_id)
REFERENCES producttype (id) NOT DEFERRABLE;

--changeset initial:107
ALTER TABLE applicationtype_environmenttype ADD CONSTRAINT applicationtype_environmenttype_environmenttypes_id_key UNIQUE (environmenttypes_id);

--changeset initial:108
ALTER TABLE applicationtype ADD CONSTRAINT applicationtype_name_key UNIQUE (name);

--changeset initial:109
ALTER TABLE artifact_artifact ADD CONSTRAINT artifact_artifact_attributes_id_key UNIQUE (attributes_id);

--changeset initial:110
ALTER TABLE artifact ADD CONSTRAINT artifact_name_key UNIQUE (name);

--changeset initial:111
ALTER TABLE artifacttype ADD CONSTRAINT artifacttype_name_key UNIQUE (name);

--changeset initial:112
ALTER TABLE configuration ADD CONSTRAINT configuration_key_key UNIQUE (key);

--changeset initial:113
ALTER TABLE configuration ADD CONSTRAINT configuration_value_key UNIQUE (value);

--changeset initial:114
ALTER TABLE environmenttype ADD CONSTRAINT environmenttype_name_key UNIQUE (name);

--changeset initial:115
ALTER TABLE installableinstance_attribute ADD CONSTRAINT installableinstance_attribute_privateattributes_id_key UNIQUE (privateattributes_id);

--changeset initial:116
ALTER TABLE os ADD CONSTRAINT os_ostype_key UNIQUE (ostype);

--changeset initial:117
ALTER TABLE productrelease_attribute ADD CONSTRAINT productrelease_attribute_attributes_id_key UNIQUE (attributes_id);

--changeset initial:118
ALTER TABLE producttype ADD CONSTRAINT producttype_name_key UNIQUE (name);

--changeset initial:119
ALTER TABLE securitygroup_rule ADD CONSTRAINT securitygroup_rule_rules_id_key UNIQUE (rules_id);

--changeset initial:120
ALTER TABLE service_attribute ADD CONSTRAINT service_attribute_attributes_id_key UNIQUE (attributes_id);

--changeset initial:121
ALTER TABLE service ADD CONSTRAINT service_name_key UNIQUE (name);

--changeset initial:122
ALTER TABLE template ADD CONSTRAINT template_name_key UNIQUE (name);
