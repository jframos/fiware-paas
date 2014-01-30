__author__ = 'henar'
from xml.etree.ElementTree import Element, SubElement, fromstring
from productrelease import ProductRelease, Product


class ApplicationInstance:


    def __init__(self, application_name, application_version):
        self.application_name=application_name
        self.application_version=application_version

        self.artifacts=[]

    def add_artifacts (self, environment):
        self.environment = environment

    def get_tier_environment (self, environment):
        self.environment = environment

    def to_xml (self):

        application_instance_dto = Element('applicationReleaseDto')
        name = SubElement(application_instance_dto, "applicationName")
        name.text = self.application_name
        name = SubElement(application_instance_dto, "version")
        name.text = self.application_version

        for art in self.artifacts:
            artifact_dto = art.to_xml()
            application_instance_dto.append(artifact_dto)


        return application_instance_dto


    def to_string (self):
        var = str(self.name).upper()
        for tier in self.tiers:
            var = var+  '\t' + tier.name
            for product in tier.products:
                var = var + '\t' + product.name + '\t' + product.version
        print var



    def add_artifact(self, artifcact):
        self.artifacts.append(artifcact)



class Artifact:
    def __init__(self, name,path, product_information):
        self.name=name
        self.path=path
        self.product= self.__process_product (product_information)


    def to_xml (self):
        artifact = Element('artifactsDto')
        name = SubElement(artifact, 'name')
        name.text = self.name
        path = SubElement(artifact, 'path')
        path.text = self.path
        product = SubElement(artifact, 'productReleaseDto')
        product_name = SubElement(product, 'productName')
        product_name.text = self.product.product
        product_version = SubElement(product, 'version')
        product_version.text = self.product.version
        return artifact


    def __process_product (self, product_information):
        a = product_information.split ('=')
        product = ProductRelease (a[0], a[1])
        return product