__author__ = 'henar'


from xml.etree.ElementTree import Element, SubElement,tostring

class Attribute:
    def __init__(self, att_name, att_value):
        self.key=att_name
        self.value=att_value



class ProductInstance:
    def __init__(self, hostname, status, ip,product_release):
        self.hostname=hostname
        self.status=status
        self.ip=ip
        self.product_release=product_release

    def add_attribute(self, attribute):
        self.attributes.append(attribute)



    def to_string (self):

        var = self.hostname+ "\t" +self.status + '\t' + self.ip+ '\t' + self.product_release.name + '\t' + self.product_release.version
        print var

    ##
    ## get_images - Obtiene la lista de imagenes --- Detalle images/detail
    ##

