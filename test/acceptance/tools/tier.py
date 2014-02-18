__author__ = 'henar'

from xml.etree.ElementTree import Element, SubElement
from xml.etree.ElementTree import tostring
from productrelease import ProductRelease
from lettuce import world


class Network:
    def __init__(self, network_name):
        self.network_name = network_name

    def __eq__(self, other):
        return self.network_name == other.network_name

    def to_xml(self):
        networkDto = Element("networkDto")
        network = SubElement(networkDto, "networkName")
        network.text = self.network_name
        return networkDto


class Tier:
    def __init__(self, tier_name, tier_image, tier_num_min='1', tier_num_max='1', tier_num_initial='1',
                 tier_flavour='2', tier_keypair='pge354_keypair', tier_floatingip='false'):
        self.name = tier_name
        self.tier_num_min = tier_num_min
        self.tier_num_max = tier_num_max
        self.tier_num_initial = tier_num_initial
        self.tier_image = tier_image
        self.tier_flavour = tier_flavour
        self.tier_keypair = tier_keypair
        self.tier_floatingip = tier_floatingip
        self.products = []
        self.networks = []
        self.region = "RegionOne"

    def __eq__(self, other):
        return self.name == other.name \
            and self.tier_num_min == other.tier_num_min \
            and self.tier_num_max == other.tier_num_max \
            and self.tier_num_initial == other.tier_num_initial \
            and self.tier_image == other.tier_image \
            and self.tier_flavour == other.tier_flavour \
            and self.tier_keypair == other.tier_keypair \
            and self.tier_floatingip == other.tier_floatingip \
            and self.region == other.region \
            and self.products == other.products \
            and self.networks == other.networks

    def add_product(self, product):
        self.products.append(product)

    def add_network(self, network):
        self.networks.append(network)

    def parse_and_add_products(self, products_information):
        self.products.extend(parse_products(products_information))

    def parse_and_add_networks(self, networks_names):
        self.networks.extend(parse_networks(networks_names))

    def delete_product(self, product):
        self.products.pop(product)

    def to_tier_xml(self):
        tier_dtos = Element("tierDtos")
        min_num_inst = SubElement(tier_dtos, "minimumNumberInstances")
        min_num_inst.text = self.tier_num_min
        ini_num_inst = SubElement(tier_dtos, "initialNumberInstances")
        ini_num_inst.text = self.tier_num_initial
        max_mum_inst = SubElement(tier_dtos, "maximumNumberInstances")
        max_mum_inst.text = self.tier_num_max
        if self.name:
            name_tier = SubElement(tier_dtos, "name")
            name_tier.text = self.name
        image_tier = SubElement(tier_dtos, "image")
        image_tier.text = self.tier_image
        flavour_tier = SubElement(tier_dtos, "flavour")
        flavour_tier.text = self.tier_flavour
        keypair = SubElement(tier_dtos, "keypair")
        keypair.text = self.tier_keypair
        floating_ip = SubElement(tier_dtos, "floatingip")
        floating_ip.text = self.tier_floatingip

        region = SubElement(tier_dtos, "region")
        region.text = self.region

        #print tostring(tier_dtos)
        if self.products:
            for product in self.products:
                prod = product.to_product_xml_env()

                tier_dtos.append(prod)

        if self.networks:
            for net in self.networks:
                prod = net.to_xml()

                tier_dtos.append(prod)
        #print tostring(tier_dtos)
        return tier_dtos

    def to_xml(self):
        tier_dtos = Element("tierDtos")
        min_num_inst = SubElement(tier_dtos, "minimumNumberInstances")
        min_num_inst.text = self.tier_num_min
        ini_num_inst = SubElement(tier_dtos, "initialNumberInstances")
        ini_num_inst.text = self.tier_num_initial
        max_mum_inst = SubElement(tier_dtos, "maximumNumberInstances")
        max_mum_inst.text = self.tier_num_max
        name_tier = SubElement(tier_dtos, "name")
        name_tier.text = self.name
        image_tier = SubElement(tier_dtos, "image")
        image_tier.text = self.tier_image
        flavour_tier = SubElement(tier_dtos, "flavour")
        flavour_tier.text = self.tier_flavour
        keypair = SubElement(tier_dtos, "keypair")
        keypair.text = self.tier_keypair
        floating_ip = SubElement(tier_dtos, "floatingip")
        floating_ip.text = self.tier_floatingip

        region = SubElement(tier_dtos, "region")
        region.text = self.region

        if self.products:
            for product in self.products:
                prod = product.to_product_xml_env()

                tier_dtos.append(prod)
        return tier_dtos


def parse_products(products_information):
    """
    Parses a list of products in the format prod1=vers1,prod2=vers2,...
    :param products_information: string with the products to parse.
    :return: a list of ProductsRelease objects.
    """
    products = []
    if products_information:
        products_info_list = products_information.split(',')

        for product_info in products_info_list:
            product_name, product_version = product_info.split('=')
            products.append(ProductRelease(product_name, product_version))

    return products


def parse_networks(networks_names):
    """
    Parses a list of networks in the format net1,net2,...
    :param networks_names: string with the networks names to parse.
    :return: a list of Network objects.
    """
    networks = []
    if networks_names:
        networks_names_list = networks_names.split(',')

        for network_name in networks_names_list:
            networks.append(Network(network_name))

    return networks


def process_tiers(tiers):
    """
    Process the tiers provided as a list of dictionaries.
    :param tiers: tiers to be processed.
    :return: a list of Tier objects.
    """
    processed_tiers = []
    if isinstance(tiers, list):
        for tier in tiers:
            processed_tiers.append(process_tier(tier))
    else:
        # Single tier received
        processed_tiers.append(process_tier(tiers))

    return processed_tiers


def process_tier(tier):
    """
    Process the tier provided as dictionary.
    :param tiers: tier to be processed.
    :return: a Tier object.
    """
    processed_tier = Tier(tier['name'], world.config['paas']['image'])
    try:
        products_string = tier['productReleaseDtos']

        if isinstance(products_string, list):
            for product_string in products_string:
                processed_tier.add_product(ProductRelease(product_string['productName'],
                                                          product_string['version']))
        else:
            processed_tier.add_product(ProductRelease(products_string['productName'],
                                                      products_string['version']))
    except:
        pass

    return processed_tier
