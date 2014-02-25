__author__ = 'henar'

from xml.etree.ElementTree import Element, SubElement


class EnvironmentInstance:

    def __init__(self, blueprint_name, blueprint_description, environment=None, status='INIT'):
        self.blueprint_name = blueprint_name
        self.blueprint_description = blueprint_description
        self.status = status
        self.environment = environment

    def add_environment(self, environment):
        self.environment = environment

    def get_environment(self):
        return self.environment

    def to_xml(self):
        blueprint_dto = Element('environmentInstanceDto')
        name = SubElement(blueprint_dto, "blueprintName")
        name.text = self.blueprint_name
        description = SubElement(blueprint_dto, "description")
        description.text = self.blueprint_description
        blueprint_dto.append(self.environment.to_xml())
        return blueprint_dto

    def to_string(self):
        var = str(self.name).upper()
        for tier in self.tiers:
            var = var + '\t' + tier.name
            for product in tier.products:
                var = var + '\t' + product.name + '\t' + product.version
        print var

    def add_tier_instance(self, tier_instance):
        self.tier_instances.append(tier_instance)
