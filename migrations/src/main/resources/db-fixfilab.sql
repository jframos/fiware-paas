--liquibase formatted sql

-- changeset fix-filab:1-1 --
ALTER TABLE networkinstance ADD COLUMN external BOOL NOT NULL;

ALTER TABLE networkinstance ADD COLUMN region VARCHAR(255);
ALTER TABLE network ADD COLUMN region VARCHAR(255);
ALTER TABLE subnetwork ADD COLUMN region VARCHAR(255);
ALTER TABLE subnetwork ADD COLUMN vdc VARCHAR(255);
ALTER TABLE subnetworkinstance ADD COLUMN region VARCHAR(255);
ALTER TABLE subnetworkinstance ADD COLUMN vdc VARCHAR(255);

ALTER TABLE tier ADD COLUMN affinity VARCHAR(128);
DROP TABLE artifacttype;

ALTER TABLE tierinstance ADD COLUMN floatingip VARCHAR(128);

ALTER TABLE network ADD COLUMN federatednetwork VARCHAR(128);
ALTER TABLE network ADD COLUMN federatedRange VARCHAR(128);
ALTER TABLE networkinstance ADD COLUMN federatednetwork VARCHAR(128);
ALTER TABLE networkinstance ADD COLUMN federatedRange VARCHAR(128);
