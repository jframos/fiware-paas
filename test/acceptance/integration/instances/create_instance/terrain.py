# -*- coding: utf-8 -*-
from lettuce import world, after, before
from tools import environment_request, environment_instance_request
from tools.environment_request import EnvironmentRequest
from tools.environment_instance_request import EnvironmentInstanceRequest
from tools.constants import PAAS, KEYSTONE_URL, PAASMANAGER_URL, TENANT, USER,\
    PASSWORD, VDC, SDC_URL


@before.each_feature
def before_each_scenario(feature):
    world.env_requests = EnvironmentRequest(world.config[PAAS][KEYSTONE_URL],
                                            world.config[PAAS][PAASMANAGER_URL],
                                            world.config[PAAS][TENANT],
                                            world.config[PAAS][USER],
                                            world.config[PAAS][PASSWORD],
                                            world.config[PAAS][VDC],
                                            world.config[PAAS][SDC_URL])

    world.inst_requests = EnvironmentInstanceRequest(world.config[PAAS][KEYSTONE_URL],
                                                     world.config[PAAS][PAASMANAGER_URL],
                                                     world.config[PAAS][TENANT],
                                                     world.config[PAAS][USER],
                                                     world.config[PAAS][PASSWORD],
                                                     world.config[PAAS][VDC],
                                                     world.config[PAAS][SDC_URL])


@after.each_scenario
def after_each_scenario(scenario):
    # Delete the environments created in the scenario.
    environment_instance_request.delete_created_instances()
    environment_request.delete_created_environments()
