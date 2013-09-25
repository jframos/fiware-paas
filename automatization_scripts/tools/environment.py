__author__ = 'henar'
from xml.etree.ElementTree import Element, SubElement, fromstring


class Environment:
    def __init__(self, environment_name,environment_description):
        self.name=environment_name
        self.description=environment_description
        self.tiers=[]


    def to_env_xml (self):
        environment_dto = Element('environmentDto')
        name = SubElement(environment_dto, "name")
        name.text = self.name
        description = SubElement(environment_dto, "description")
        description.text = self.description

        for tier in self.tiers:
            tier_dto = fromstring(tier.to_product_xml())
            environment_dto.append(tier_dto)

        return environment_dto

    ##
    ## get_images - Obtiene la lista de imagenes --- Detalle images/detail
    ##
    def add_attribute(self, attribute):
        self.attributes.append(attribute)



