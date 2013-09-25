__author__ = 'henar'


from xml.etree.ElementTree import Element, SubElement,tostring

class Attribute:
    def __init__(self, att_name, att_value):
        self.key=att_name
        self.value=att_value

class ProductRelease:
    def __init__(self, product_name,product_version, product_description=''):
        self.name=product_name
        self.description=product_description
        self.version=product_version
        self.attributes=[]

    def add_attribute(self, attribute):
        self.attributes.append(attribute)

    def to_product_xml (self):

        product = Element('productReleaseDto')
        name = SubElement(product, 'productName')
        name.text = self.name
        description = SubElement(product, "productDescription")
        description.text = self.description
        version = SubElement(product, 'version')
        version.text = self.version
        print product
        if self.attributes == None:
            return tostring(product)
        for att in self.attributes:

            attribute = SubElement(product, "privateAttributes")
            key = SubElement(attribute, "key")
            key.text = att.key
            value = SubElement(attribute, "value")
            value.text = att.value

        return product

    def to_product_xml_env (self):

        product = Element('productReleaseDtos')
        name = SubElement(product, 'productName')
        name.text = self.name
        description = SubElement(product, "productDescription")
        description.text = self.description
        version = SubElement(product, 'version')
        version.text = self.version

        if self.attributes == None:
            return product
        for att in self.attributes:
            attribute = SubElement(product, "privateAttributes")
            key = SubElement(attribute, "key")
            key.text = att.key
            value = SubElement(attribute, "value")
            value.text = att.value

        return product

    def to_string (self):

        var = self.name+ "\t" +self.description + '\t' + self.version+ '\t'
        for att in self.attributes:
            var = var + att.key + ':' + att.value
        print var

    ##
    ## get_images - Obtiene la lista de imagenes --- Detalle images/detail
    ##

