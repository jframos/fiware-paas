CREATE TABLE attribute (
	id int8 NOT NULL AUTO_INCREMENT,
	description varchar(2048),
	key varchar(256) NOT NULL,
	v int8,
	value varchar(2048) NOT NULL,
	PRIMARY KEY (id)
);
CREATE TABLE productrelease (
	id int8 NOT NULL AUTO_INCREMENT,
	description varchar(2048),
	name varchar(256) NOT NULL,
	product varchar(256) NOT NULL,
	version varchar(256) NOT NULL,
	withartifact bool,
	producttype_id int8,
	PRIMARY KEY (id)
);

CREATE TABLE network (
    id int8 NOT NULL AUTO_INCREMENT,
    name varchar(256) NOT NULL,
    idNetwork varchar(256) NOT NULL,
    subNetCount int8,
    PRIMARY KEY (id)
);

    
CREATE TABLE productrelease_attribute (
	productrelease_id int8 NOT NULL,
	attributes_id int8 NOT NULL
);

CREATE TABLE securitygroup (
	id int8 NOT NULL AUTO_INCREMENT,
	description varchar(255),
	idsecuritygroup varchar(255),
	name varchar(256) NOT NULL,
	PRIMARY KEY (id)
);
CREATE TABLE securitygroup_rule (
	securitygroup_id int8 NOT NULL,
	rules_id int8 NOT NULL
);


CREATE TABLE tier (
	id int8 NOT NULL AUTO_INCREMENT,
	flavour varchar(255),
	environmentname varchar(255),
	floatingip varchar(255),
	icono varchar(255),
	image varchar(255),
	initialnumberinstances int4,
	keypair varchar(255),
	maximumnumberinstances int4,
	minimumnumberinstances int4,
	name varchar(256) NOT NULL,
	payload varchar(90000),
	vdc varchar(255),
	PRIMARY KEY (id)
);



CREATE TABLE tier_has_productreleases (
	tier_id int8 NOT NULL,
	productrelease_id int8 NOT NULL
);

CREATE TABLE tier_has_networks (
    tier_id int8 NOT NULL,
    network_id int8 NOT NULL
);


CREATE TABLE environment (
	id int8 NOT NULL AUTO_INCREMENT,
	description varchar(256),
	name varchar(256) NOT NULL,
	org varchar(256) NOT NULL,
	ovf varchar(90000),
	vdc varchar(256),
	environmenttype_id int8,
	PRIMARY KEY (id)
);


CREATE TABLE environment_has_tiers (
	environment_id int8 NOT NULL,
	tier_id int8 NOT NULL
);

CREATE TABLE installableinstance (
	id int8 NOT NULL AUTO_INCREMENT,
	date timestamp,
	name varchar(256) NOT NULL,
	status varchar(255),
	vdc varchar(255),
	PRIMARY KEY (id)
);

CREATE TABLE productinstance (
	id int8 NOT NULL AUTO_INCREMENT,
	productrelease_id int8,
	PRIMARY KEY (id)
);


CREATE TABLE tierinstance (
	numberreplica int4 NOT NULL,
	ovf varchar(100000),
	vapp varchar(10000),
	domain varchar(128),
	fqn varchar(512),
	hostname varchar(128),
	ip varchar(128),
	networks varchar(512),
	ostype varchar(8),
	vmid varchar(512),
	id int8 NOT NULL AUTO_INCREMENT,
	tier_id int8,
	PRIMARY KEY (id)
);
CREATE TABLE tierinstance_has_productinstances (
	tierinstance_id int8 NOT NULL,
	productinstance_id int8 NOT NULL
);

CREATE TABLE environmentinstance (
	blueprintname varchar(255),
	description varchar(255),
	vapp varchar(90000),
	id int8 NOT NULL AUTO_INCREMENT,
	environment_id int8,
	PRIMARY KEY (id)
);
CREATE TABLE environmentinstance_has_tierinstances (
	environmentinstance_id int8 NOT NULL,
	tierinstance_id int8 NOT NULL
);