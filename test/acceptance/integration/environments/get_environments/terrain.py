# -*- coding: utf-8 -*-
from lettuce import world, after, before
from tools import environment_request
from tools.environment_request import EnvironmentRequest
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

    # Check that the initial list of environments is empty before launching the tests
    world.env_requests.get_environments()
    environment_request.check_get_environments_response(world.response, 200, 0)


@after.each_scenario
def after_each_scenario(scenario):
    # Delete the environments created in the scenario.
    environment_request.delete_created_environments()
