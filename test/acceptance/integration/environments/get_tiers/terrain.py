# -*- coding: utf-8 -*-
from lettuce import world, after, before
from tools import environment_request
from tools.environment_request import EnvironmentRequest


@before.each_scenario
def before_each_scenario(scenario):
    world.env_requests = EnvironmentRequest(world.config['paas']['keystone_url'],
                                            world.config['paas']['paasmanager_url'],
                                            world.config['paas']['tenant'],
                                            world.config['paas']['user'],
                                            world.config['paas']['password'],
                                            world.config['paas']['vdc'],
                                            world.config['paas']['sdc_url'])


@after.each_scenario
def after_each_scenario(scenario):
    # Delete the environments created in the scenario.
    environment_request.delete_created_environments()
