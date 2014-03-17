Table environment_has_tiers has duplicated rows.
Use next query:

select distinct 'INSERT INTO environment_has_tiers (environment_id, tier_id) VALUES ('||environment_id||', '||tier_id||')' from environment_has_tiers;

select distinct 'INSERT INTO productrelease_attribute (productrelease_id, attributes_id) VALUES ('|| productrelease_id||', '|| attributes_id ||')' from productrelease_attribute; 


to obtain valid insert sentences.

