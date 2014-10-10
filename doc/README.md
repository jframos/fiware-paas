PaaS Manager - Overview
____________________________

[![Build Status](https://travis-ci.org/telefonicaid/fiware-paas.svg)](https://travis-ci.org/telefonicaid/fiware-paas) [![Coverage Status](https://coveralls.io/repos/telefonicaid/fiware-paas/badge.png?branch=develop)](https://coveralls.io/r/telefonicaid/fiware-paas?branch=develop) [![help stackoverflow](http://b.repl.ca/v1/help-stackoverflow-orange.png)](http://www.stackoverflow.com)

What you get
============

The PaaS Manager GE provides a
new layer over the IaaS layer (Openstack) in the aim of easing the task of deploying applications on a Cloud infrastructure.
Therefore, it orchestrates the provisioning of the required virtual resources at IaaS level, and then, the installation and configuration
of the whole software stack of the application by the SDC GE, taking into account the underlying virtual infrastructure.
It provides a flexible mechanism to perform the deployment, enabling multiple deployment architectures:
everything in a single VM or server, several VMs or servers, or elastic architectures based on load balancers and different software tiers.


Why to get it
=============

PaaS Manager GE - TID Implementation is the orchestration platform to be used in the
FIWARE Cloud ecosystem to deploy not just insfrastructure  but also software on top
of that.

-   **Full Openstack integrated solution**
    The PaaS Manager is fully integrated with the Opesntack services (nova for computation, neutron for networking and glance
    for image catalog.    

-   **Asynchronous interface**

    Asynchronous interface with polling mechanism to obtain information about the deployment status.

-   **Decoupling the management  and provisioning**

     Decoupling the management of the catalogue (specifications of what can be deployed) 
     and the management of the inventory (instances of what has been already deployed). 
     In addition, decoupling the management of environments from the management of applications, 
     since there could be uses cases where the users of those functionalities could be different ones.
    
   


Fast elasticity mechanisms, avoiding overhead or repeated work as much as possible during elasticity.


Documentation
=============

-   `User and Programmers Guide <user_guide.rst>`_
-   `Installation and Administration Guide <admin_guide.rst>`_


References
==========

.. _FIWARE.OpenSpecification.Cloud.PaaS: http://forge.fi-ware.org/plugins/mediawiki/wiki/fiware/index.php/FIWARE.OpenSpecification.Cloud.PaaS
.. _PaaS_Open_RESTful_API_Specification_(PRELIMINARY): http://forge.fi-ware.org/plugins/mediawiki/wiki/fiware/index.php/PaaS_Open_RESTful_API_Specification_(PRELIMINARY)
.. _PaaS_Manager_-_Installation_and_Administration_Guide: http://forge.fi-ware.org/plugins/mediawiki/wiki/fiware/index.php/PaaS_Manager_-_Installation_and_Administration_Guide
