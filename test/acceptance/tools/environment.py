__author__ = 'henar'

from xml.etree.ElementTree import Element, SubElement
from lettuce import world


class Environment:
    def __init__(self, environment_name, environment_description, tiers=None):
        self.name = environment_name
        self.description = environment_description
        if tiers:
            self.tiers = tiers
        else:
            self.tiers = []

    def to_env_xml(self):
        environment_dto = Element('environmentDto')
        if self.name:
            name = SubElement(environment_dto, "name")
            name.text = self.name
        if self.description:
            description = SubElement(environment_dto, "description")
            description.text = self.description

        if self.tiers:
            for tier in self.tiers:
                tier_dto = tier.to_xml()
                environment_dto.append(tier_dto)

        return environment_dto

    def to_xml(self):
        environment_dto = Element('environmentDto')
        if self.name:
            name = SubElement(environment_dto, "name")
            name.text = self.name
        if self.description:
            description = SubElement(environment_dto, "description")
            description.text = self.description

        for tier in self.tiers:
            tier_dto = tier.to_xml()
            environment_dto.append(tier_dto)

        return environment_dto

    def to_string(self):
        var = str(self.name).upper()
        for tier in self.tiers:
            var = var + '\t' + tier.name
            for product_release in tier.products:
                var = var + '\t' + product_release.product + '\t' + product_release.version
        print var

    ##
    ## get_images - Obtiene la lista de imagenes --- Detalle images/detail
    ##
    def add_attribute(self, attribute):
        self.attributes.append(attribute)

    def add_tier(self, tier):
        self.tiers.append(tier)

    def add_tiers(self, tiers):
        self.tiers.extend(tiers)


def delete_created_environments():
    """
    Delete the environment created so far in the tests.
    """
    try:
        while len(world.environments) > 0:
            world.env_requests.delete_environment(world.environments[0])
        del world.environments
    except AttributeError:
        pass
