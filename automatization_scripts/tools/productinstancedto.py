__author__ = 'henar'


from xml.etree.ElementTree import Element, SubElement,tostring

class Attribute:
    def __init__(self, att_name, att_value):
        self.key=att_name
        self.value=att_value


class ProductReleaseDto:
    def __init__(self, name, version):
        self.name = name
        self.version = version

    def to_xml (self):
        product = Element('product')
        name = SubElement(product, 'name')
        name.text = self.name
        version = SubElement(product, 'version')
        version.text = self.version



class ProductInstanceDto:
    def __init__(self, ip,product_release, attributes):
        self.ip=ip
        self.product_release=product_release
        self.attributes=attributes

    def to_xml (self):
        product_instance = Element('productInstanceDto')
        product_instance.append(self.product_release.to_xml())
        vm = SubElement(product_instance, 'VM')
        ip = SubElement(vm, 'ip')
        ip.text = self.ip
        for att in self.attributes:
            attri = SubElement(product_instance, 'attributes')
            key = SubElement(attri, "key")
            key.text = att.key
            value = SubElement(attri, "value")
            value.text = att.value

        print product_instance
        return product_instance

