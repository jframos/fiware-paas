PaaS Maanger - Installation and Administration Guide
_________________________________________________________


Introduction
============

This guide defines the procedure to install the different components that build
up the PaaS Manager GE, including its requirements and possible troubleshooting.

Requirements
============
In order to execute the PaaS Manager, it is needed to have previously installed the following software:
- PostgreSQL

Building instructions
============
The PaaS Manager a a maven application:

- Compile, launch test and build all modules
.. code::

   $  mvn clean install
- Create a zip with distribution in target/paas-manager-server-dist.zip
.. code::

   $  mvn assembly:assembly -DskipTests

- You can generate a rpm o debian packages (using profiles in pom)

    for debian/ubuntu:
.. code::

   $ mvn install -Pdebian -DskipTests
        (created target/paas-manager-server-XXXXX.deb)

    for centOS:
.. code::

   $ mvn install -Prpm -DskipTests
        (created target/rpm/paasmanager/RPMS/noarch/paasmanager-XXXX.noarch.rpm)


 Installation  (for CentOS)
============

Requirements: Install PostgreSQL
------------------
The first thing is to install and configure the requirements, in this case, the postgresql
    .. code::

   $ yum install postgresql postgresql-server postgresql-contrib

Type the following commands to install the postgresql as service and start it

    .. code::

    $ chkconfig --add postgresql
    $ chkconfig postgresql on
    $ service postgresql initdb
    $ service postgresql start
    
Edit file /var/lib/pgsql/data/pg_hba.conf and set authentication method to md5:

 .. code::

    $    # TYPE  DATABASE    USER        CIDR-ADDRESS          METHOD
    $     "local" is for Unix domain socket connections only
    $    ocal   all         all                               md5
    $    local   all         postgres                          md5
    $    # IPv4 local connections:
    $    host    all         all         0.0.0.0/0          md5
    
Edit file /var/lib/pgsql/data/postgresql.conf and set listen addresses to 0.0.0.0:

     .. code::

    $   listen_addresses = '0.0.0.0'
    
Reload configuration

     .. code::

    $   service postgresql reload
    
    
Install PaaS Manager
------------------
  
The PaaS Manager is packaged as RPM and stored in the rpm repository. Thus, the first thing to do is to create a file in /etc/yum.repos.d/fiware.repo, 
with the following content.

     .. code::

    $   [Fiware]
    $   name=FIWARE repository
    $   baseurl=http://repositories.testbed.fi-ware.eu/repo/rpm/x86_64/
    $   gpgcheck=0
    $   enabled=1
    

After that, you can install the paas manager just doing:
         .. code::

    $  yum install paas-manager

Configuring the database
------------------

We need to create the paasmanager database. To do that we need to connect as postgres user to the PostgreSQL
server and set the password for user postgres using alter user as below:

     .. code::

    $   su - postgres
    $  postgres$ psql postgres postgres;
    $  psql (8.4.13)
    $  Type "help" for help.
    $   postgres=# alter user postgres with password 'postgres';
    $  postgres=# create database paasmanager;
    $  postgres=# grant all privileges on database paasmanager to postgres;
 
To create the tables in the databases, just go to 
     .. code::

    $   cd /opt/fiware-paas/db
    $   su - potgres
    $  postgres$ psql postgres postgres;
    $  postgres=# \i db-initial.sql
    $ postgres=# \i db-changelog.sql
    $  exit


Configure Paas-manager application
------------------  

Once the prerequisites are satisfied, you shall modify the context file at  /opt/fiware-paas/webapps/paasmanager.xml 

See the snipet bellow to know how it works:
.. code::

    $  <New id="paasmanager" class="org.eclipse.jetty.plus.jndi.Resource">
    $     <Arg>jdbc/paasmanager</Arg>
    $     <Arg>

     $        <New class="org.postgresql.ds.PGSimpleDataSource">
     $            <Set name="User"> <database user> </Set>
     $            <Set name="Password"> <database password> </Set>
     $            <Set name="DatabaseName"> <database name>   </Set>
     $            <Set name="ServerName"> <IP/hostname> </Set>
      $           <Set name="PortNumber">5432</Set>
      $       </New>

     $    </Arg>
    $ </New>


Configuring the PaaS Manager as service 
------------------
Once we have isntalled and configure the paas manager, the next step is to configure it as a service. To do that just create a file in /etc/init.d/fiware-paas
with the following content

#!/bin/bash
# chkconfig: 2345 20 80
# description: Description comes here....

# Source function library.
. /etc/init.d/functions

start() {
     /opt/fiware-paas/bin/jetty.sh start
}

stop() {
    /opt/fiware-paas/bin/jetty.sh stop
}

case "$1" in 
    start)
       start
       ;;
    stop)
       stop
       ;;
    restart)
       stop
       start
       ;;
    status)
       /opt/fiware-paas/bin/jetty.sh status
       ;;
    *)
       echo "Usage: $0 {start|stop|status|restart}"
esac

exit 0 

Now you need to execute:
.. code::

    $   chkconfig --add fiware-paas
    $  chkconfig fiware-paas on
    $  service fiware-paas start


Configuring the PaaS Manager in the keystone 
=======================
The FIWARE keystone is a endpoint catalogue which collects all the endpoint of the different services

Sanity check procedures
=======================

Sanity check procedures
------------------
The Sanity Check Procedures are the steps that a System Administrator will take to verify that an installation is ready to be tested. This is therefore a preliminary set of tests to ensure that obvious or basic malfunctioning is fixed before proceeding to unit tests, integration tests and user validation.

End to End testing
------------------
Although one End to End testing must be associated to the Integration Test, we can show here a quick testing to check that everything is up and running. It involves to obtain the product information storaged in the catalogue. With it, we test that the service is running and the database configure correctly.
  http://{PaaSManagerIP}:{port}/paasmanager/rest
The request to test it in the testbed should be

 .. code::

    $   curl -v -H "Access-Control-Request-Method: GET" -H "Content-Type: application xml" -H "Accept: application/xml" 
-H "X-Auth-Token: 5d035c3a29be41e0b7007383bdbbec57" -H "Tenant-Id: 60b4125450fc4a109f50357894ba2e28" -X GET " http://{PaaSManagerIP}:{port}/paasmanager/rest/catalog/org/FIWARE/environment"


Whose result is the PaaS Manager API documentation.

List of Running Processes
------------------
Due to the PaaS Manager basically is running over the Tomcat, the list of processes must be only the Tomcat and PostgreSQL. If we execute the following command:
.. code::

    $   ps -ewF | grep 'postgres\|tomcat' | grep -v grep

It should show something similar to the following:
  .. code::

   $    postgres  2057     1  0 30179   884   0 Nov05 ?        00:00:00 /usr/bin/postmaster -p 5432 -D /var/lib/pgsql/data
  $    postgres  2062  2057  0 27473   248   0 Nov05 ?        00:00:00 postgres: logger process
  $    postgres  2064  2057  0 30207   636   0 Nov05 ?        00:00:00 postgres: writer process
  $    postgres  2065  2057  0 27724   160   0 Nov05 ?        00:00:00 postgres: stats buffer process
  $     postgres  2066  2065  0 27521   204   0 Nov05 ?        00:00:00 postgres: stats collector process
  $    root      2481     1  0 228407 96324  0 Nov05 ?        00:03:34 /usr/bin/java -Djava.util.logging.config.file=/opt/apache-tomcat-7.0.16/conf/...
  $    postgres  2501  2057  0 31629   560   0 Nov05 ?        00:00:01 postgres: postgres paasmanager 127.0.0.1(49303) idle
  $    postgres  7208  2057  0 30588  3064   0 Nov05 ?        00:00:00 postgres: postgres paasmanager 127.0.0.1(49360) idle


Network interfaces Up & Open
------------------
Taking into account the results of the ps commands in the previous section, we take the PID in order to know the information about the network interfaces up & open. To check the ports in use and listening, execute the command:
  .. code::

   $ netstat �p �a | grep $PID/java


Where $PID is the PID of Java process obtained at the ps command described before, in the previous case 18641 tomcat and 23546 (postgresql). The expected results must be something similar to the following:

  .. code::

  $   tcp        0      0 localhost.localdomain:8005  *:*                         LISTEN      2481/java
  $   tcp        0      0 *:8009                      *:*                         LISTEN      2481/java
  $   tcp        0      0 *:webcache                  *:*                         LISTEN      2481/java
  $   tcp        0      0 localhost.localdomain:49360 localhost.localdom:postgres ESTABLISHED 2481/java
  $   tcp        0      0 localhost.localdomain:49303 localhost.localdom:postgres ESTABLISHED 2481/java
  $   tcp        0      0 *:postgres                  *:*                         LISTEN      2057/postmaster
  $   tcp        0      0 *:postgres                  *:*                         LISTEN      2057/postmaster
  $   udp        0      0 localhost.localdomain:33556 localhost.localdomain:33556 ESTABLISHED 2057/postmaster

  $   unix       2      [ ACC ]     STREAM     LISTENING     8921   2057/postmaster     /tmp/.s.PGSQL.5432


Databases
The last step in the sanity check, once that we have identified the processes and ports is to check the different databases that have to be up and accept queries. Fort he first one, if we execute the following commands:

  .. code::

  $    psql -U postgres -d paasmanager

For obtaining the tables in the database, just use

  .. code::

  $    paasmanager=# \dt

   $    Schema|                Name                     | Type  |  Owner

  $    ---------+---------------------------------------+-------+----------
  $    public  | applicationinstance                   | tabla | postgres
  $    public  | applicationrelease                    | tabla | postgres
  $    public  | applicationrelease_applicationrelease | tabla | postgres
  $    public  | applicationrelease_artifact           | tabla | postgres
  $    ...

