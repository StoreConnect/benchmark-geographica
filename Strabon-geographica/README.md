# Strabon #

[Strabon](http://www.strabon.di.uoa.gr/) is a spatiotemporal RDF store. You can use it to store linked geospatial data that changes over time and pose queries using two popular extensions of SPARQL. Strabon supports spatial datatypes enabling the serialization of geometric objects in OGC standards WKT and GML. It also offers spatial and temporal selections, spatial and temporal joins, a rich set of spatial functions similar to those offered by geospatial relational database systems and support for multiple Coordinate Reference Systems. Strabon can be used to model temporal domains and concepts such as events, facts that change over time etc. through its support for valid time of triples, and a rich set of temporal functions.
Strabon is built by extending the well-known RDF store [Sesame (now called RDF4J)](http://rdf4j.org/) and extends RDF4J’s components to manage thematic, spatial and temporal data that is stored in the backend RDBMS.

The first query language supported by Strabon is stSPARQL. [stSPARQL](http://www.strabon.di.uoa.gr/files/stSPARQL_tutorial.pdf) can be used to query data represented in an extension of RDF called stRDF. stRDF and stSPARQL have been designed for representing and querying geospatial data that changes over time, e.g., the growth of a city over the years due to new developments can be represented and queried using the valid time dimesion of stRDF and stSPARQL respectively. The expressive power of stSPARQL makes Strabon the only fully implemented RDF store with rich spatial and temporal functionalities available today.

## Software Environment

To install a functonal Strabon platform, the folowing prerequisites have to be installed and verified (the versions corresponding to *the up to date* versions supported in Ubuntu)  


|                  Software                  |     Version    |
|:------------------------------------------:|:--------------:|
|                 [Ubuntu](https://ubuntu-fr.org/) (in my case)                |     16.04 LTS & 17.04 LTS     |
|                 [PostgreSQL](https://www.postgresql.org/)                 |     10.1     |
|                   [PostGIS](https://postgis.net/)                  |      2.4.3     |
|                   [Strabon](http://hg.strabon.di.uoa.gr/Strabon/)                  | 3.3.2-SNAPSHOT |
|     [Geometry Engine, Open Source (GEOS)](https://trac.osgeo.org/geos)    |      3.6.2     |
|                   [PROJ](http://proj4.org/#)                   |      4.9.3-2     |
| [Geospatial Data Abstraction Library (GDAL)](http://www.gdal.org/) |      2.2.4     |


## Installation
Strabon use PostgreSQL with PostGIS extension (a spatial database extender for **PostgreSQL**) as database to store geospatial data. 
PostGIS add support for geographic objects allowing location queries to be run in SQL.

 
Adapt the instructions in accordance with your OS

### GeoSpatial Librairies Installation

#### `GEOS`
GEOS (Geometry Engine - Open Source) is a C++ port of the [​Java Topology Suite](http://tsusiatsoftware.net/jts/main.html) (JTS). As such, it aims to contain the complete functionality of JTS in C++. This includes all the [​OpenGIS Simple Features for SQL](http://www.opengeospatial.org/standards/sfs) spatial predicate functions and spatial operators, as well as specific JTS enhanced topology functions.

Compile and Install:

```
cd /tmp
wget http://download.osgeo.org/geos/geos-3.6.2.tar.bz2
bunzip2 geos-3.6.2.tar.bz2 
tar xvf geos-3.6.2.tar 
cd geos-3.6.2 

sudo ./configure
sudo make
sudo make install
sudo ldconfig
```

#### `PROJ4`
PROJ is a standard UNIX filter function which converts geographic longitude and latitude coordinates into cartesian coordinates (and vice versa), and it is a C API for software developers to include coordinate transformation in their own software.

Two options are available to install **PROJ**:

1. Automatic, from repository:

```
sudo apt-get install proj-bin proj-data proj-ps-doc
```



2. Manual, from sources
At the time writting the [versions](http://proj4.org/download.html) are :

|           Software           | Version |
|:----------------------------:|:-------:|
|             proj             |  5.0.1  |
|        proj-datumgrid        |   1.7   |
|     proj-datumgrid-europe    |   1.0   |
| proj-datumgrid-north-america |   1.0   |
|    proj-datumgrid-oceania    |   1.0   |

Compile and Install:

```
cd /tmp
wget http://download.osgeo.org/proj/proj-5.0.1.tar.gz
tar zxvf proj-5.0.1.tar.gz 
cd proj-5.0.1/nad
wget http://download.osgeo.org/proj/proj-datumgrid-1.7.zip
wget http://download.osgeo.org/proj/proj-datumgrid-europe-1.0.zip
wget http://download.osgeo.org/proj/proj-datumgrid-north-america-1.0.zip


unzip -o -q proj-datumgrid-1.7.zip
unzip -o -q proj-datumgrid-europe-1.0.zip
unzip -o -q proj-datumgrid-north-america-1.0.zip

cd ..
sudo ./configure
sudo make
sudo make install
sudo ldconfig
```
#### `GDAL`
![GDAL](http://www.gdal.org/gdalicon.png) is a translator library for raster and vector geospatial data formats that is released under an [X/MIT](http://trac.osgeo.org/gdal/wiki/FAQGeneral#WhatlicensedoesGDALOGRuse) style [Open Source](http://www.opensource.org/) license by the [Open Source Geospatial Foundation](http://www.osgeo.org/). As a library, it presents a [single raster abstract data model](http://www.gdal.org/gdal_datamodel.html) and [single vector abstract data model](http://www.gdal.org/ogr_arch.html) to the calling application for all supported formats. It also comes with a variety of useful command line utilities for data translation and processing.

GDAL download repositories: https://download.osgeo.org/gdal/

Compile and Install:


Two options are available to install gdal:

1. Automatic, from repository:

```
sudo apt-get install gdal-bin gdal-data libgdal-dev libgdal-doc libgdal-grass libimageclasses1 liblasclasses1 pktools pktools-dev 
```

2. Manual, from sources:


```
cd /tmp
// Donwload GDAL
wget http://download.osgeo.org/gdal/2.2.4/gdal-2.2.4.tar.gz
tar zxvf gdal-2.2.4.tar.gz
cd gdal-2.2.4

sudo ./configure
sudo make
sudo make install
sudo ldconfig
```



### PostgreSQL Installation

#### Add PostgreSQL apt repository
-----------------------------
Execute the following steps to install, under **Ubuntu**, a PostgreSQL database with PostGIS extension installed.


The current default Ubuntu apt repositories only have up to postgresql-9.6. To get 10, we'll add the official postgres apt repository.

-   Ubuntu 14.04: `sudo add-apt-repository 'deb http://apt.postgresql.org/pub/repos/apt/ trusty-pgdg main'`
-   Ubuntu 16.04: `sudo add-apt-repository 'deb http://apt.postgresql.org/pub/repos/apt/ xenial-pgdg main'`
-   Ubuntu 17.04: `sudo add-apt-repository 'deb http://apt.postgresql.org/pub/repos/apt/ zesty-pgdg main'`

Now import the repository signing key, followed by an update to the package lists:

```
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -
sudo apt-get update
```

#### Install PostgreSQL/PostGIS packages
-----------------------------



```
sudo apt-get install postgresql-10 postgresql-server-dev-10 postgresql-doc-10 postgresql-contrib postgresql-client-10 postgresql-client-common  postgresql-10-postgis-2.4 postgresql-10-postgis-2.4-scripts postgresql-10-postgis-scripts
postgresql-10-pgrouting postgresql-10-pgrouting-doc postgresql-10-pgrouting-scripts
```

The following packages are installed:

- **postgresql-10**: object-relational SQL database, version 10 server
- **postgresql-server-dev-10**: development files for PostgreSQL 10 server-side programming
- **postgresql-doc-10**: documentation for the PostgreSQL database management system
- **postgresql-contrib**: additional facilities for PostgreSQL (supported version)
- **postgresql-client-10**: front-end programs for PostgreSQL 10
- **postgresql-client-common**: manager for multiple PostgreSQL client versions
- **postgresql-10-postgis-2.4**: Geographic objects support for PostgreSQL 10
- **postgresql-10-postgis-2.4-scripts**: Geographic objects support for PostgreSQL 10 -- SQL scripts
- **postgresql-10-postgis-scripts**: transitional dummy package
- **postgresql-10-pgrouting**: Routing functionality support for PostgreSQL/PostGIS
- **postgresql-10-pgrouting-doc**: Routing functionality support for PostgreSQL/PostGIS (Documentation)
- **postgresql-10-pgrouting-scripts**: Routing functionality support for PostgreSQL/PostGIS - SQL scripts

### Configuration
#### `Start PostgreSQL`
-----------------------------
Check the state of PostgreSQL by running this command:

```
sudo service postgresql status
```
The output will be:

- PostgreSQL is runnig: 
<span style="color:GreenYellow">●</span> ==postgresql.service - PostgreSQL RDBMS
Loaded: loaded(/lib/systemd/system/postgresql.service; enabled; vendor preset: enabled)
   Active: **active** (exited) since mer. YYYY-MM-DD HH:mm:s CEST; 1 day 1h ago
  Process: 1627 ExecStart=/bin/true (code=exited, status=0/SUCCESS)
 Main PID: 1627 (code=exited, status=0/SUCCESS)
    Tasks: 0
   Memory: 0B
      CPU: 0
   CGroup: /system.slice/postgresql.service==

- PostgreSQL is stopped:
<span style="color:red">●</span> ==postgresql.service - PostgreSQL RDBMS
   Loaded: loaded (/lib/systemd/system/postgresql.service; enabled; vendor preset: enabled)
   Active: **inactive** (dead) since ven. YYYY-MM-DD HH:mm:s CEST; 5s ago
  Process: 1627 ExecStart=/bin/true (code=exited, status=0/SUCCESS)
 Main PID: 1627 (code=exited, status=0/SUCCESS)==


If PostgreSQL is stopped, run it by this command:

```
sudo service postgresql start
```

#### `Create the Spatial Database`
-----------------------------
**All the commands are executed with the user "postgres" logged.**

##### Create Database:
Run the command below to create the spatial database that will be used as a template:

```
sudo -s
su postgres

createdb -E UTF8 -T template0 template_postgis
```

##### Load the PostGIS SQL routines:

```
psql -d template_postgis -f /usr/share/postgresql/9.6/contrib/postgis-2.4/postgis.sql
psql -d template_postgis -f /usr/share/postgresql/9.6/contrib/postgis-2.4/spatial_ref_sys.sql
```

##### Allow users to alter spatial tables:

```
psql -d template_postgis -c "GRANT ALL ON geometry_columns TO PUBLIC;"
psql -d template_postgis -c "GRANT ALL ON geography_columns TO PUBLIC;"
psql -d template_postgis -c "GRANT ALL ON spatial_ref_sys TO PUBLIC;"
```

##### Perform garbage collection:

```
psql -d template_postgis -c "VACUUM FULL;"
psql -d template_postgis -c "VACUUM FREEZE;"
```

##### Allows non-superusers the ability to create from this template:

```
psql -d postgres -c "UPDATE pg_database SET datistemplate='true' WHERE datname='template_postgis';"
psql -d postgres -c "UPDATE pg_database SET datallowconn='false' WHERE datname='template_postgis';"
```

##### Create our PostGIS Database:

```
createdb geodatadb -T template_postgis
```

**geodatadb** is the name of our PostGIS Geopsatial Database.
Of course you can choose your own name if you want..

##### Create our user

```
createuser -l -s -i -d -r --replication -P geouser
```
**geouser** is the name of our user able to manage our database.
Of course you can choose your own name if you want..


##### Assign our user to our PostGIS Database

```
psql -d geodatadb
ALTER DATABASE geodatadb OWNER TO geouser;
```

##### Test Configuration 

To verify that everythng was installed correctly, run those commands: 

```
psql -d geodatadb
SELECT postgis_full_version();
```

You shoudl have:

==POSTGIS="2.4.3 r16312" PGSQL="100" GEOS="3.6.2-CAPI-1.10.2 4d2925d6" PROJ="Rel. 4.9.3, 15 August 2016" GDAL="GDAL 2.2.4, released 2018/03/21" LIBXML="2.9.4" LIBJSON="0.12.1" LIBPROTOBUF="1.2.1" TOPOLOGY RASTER==




That means:

- POSTGIS 2.4.3 r16312
- PGSQL 100
- GEOS 3.6.2-CAPI-1.10.2
- PROJ 4.9.3-2
- GDAL 2.2.4
- LIBXML 2.9.4
- LIBJSON 0.12.1
- LIBPROTOBUF 1.2.1


## Strabon Installation
Download [Strabon](http://hg.strabon.di.uoa.gr/Strabon/archive/tip.tar.gz) (at time writting: **Strabon-3.3.2-SNAPSHOT**) at http://hg.strabon.di.uoa.gr/Strabon/

### Configure Strabon to connect to our Database
-----------------------------
```
// Change the archive name according to your Strabon release downloaded
tar zxvf Strabon-4c2fcc026c4c.tar.gz

cd Strabon-4c2fcc026c4c/endpoint/WebContent/WEB-INF
// Edit connection.properties 
vi connection.properties
```
**connection.properties** contents:

==hostname=localhost
port=5432
dbengine=postgis
password=postgres
dbname=endpoint
username=postgres
googlemapskey=AIzaSyAv7hfKpT7HkP0QN7DgHdnEwJBV0ST5FUk==

Change **hostname**, **port**, **password**, **dbname** and **username** values  according to your PostGIS Database configuration

In our case the changes are:

==hostname=localhost
port=5432
dbengine=postgis
**password=geouser**
**dbname=geodatadb**
**username=geouser**
googlemapskey=AIzaSyAv7hfKpT7HkP0QN7DgHdnEwJBV0ST5FUk==

Save the file and run 

```
cd Strabon-4c2fcc026c4c
mvn clean package

```


Once completed you should have:

==[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO]
[INFO] Strabon ............................................ <span style="color:LawnGreen">SUCCESS</span> [  1.645 s]
[INFO] Strabon: Spatial and Temporal Vocabulary ........... <span style="color:LawnGreen">SUCCESS</span> [ 32.969 s]
[INFO] OpenRDF Sesame: Query algebra - evaluation - spatial <span style="color:LawnGreen">SUCCESS</span> [02:12 min]
[INFO] OpenRDF Sesame: Spatial Query result IO ............ <span style="color:LawnGreen">SUCCESS</span> [  0.004 s]
[INFO] OpenRDF Sesame: Spatial Query result IO - API ...... <span style="color:LawnGreen">SUCCESS</span> [  0.564 s]
[INFO] OpenRDF Sesame: GeneralDBStore ..................... <span style="color:LawnGreen">SUCCESS</span> [ 18.884 s]
[INFO] OpenRDF Sesame: Spatial Query result IO - GeoJSON .. <span style="color:LawnGreen">SUCCESS</span> [ 14.587 s]
[INFO] OpenRDF Sesame: Spatial Query result IO - XML ...... <span style="color:LawnGreen">SUCCESS</span> [  0.550 s]
[INFO] OpenRDF Sesame: Spatial Query result IO - KML/KMZ .. <span style="color:LawnGreen">SUCCESS</span> [  5.454 s]
[INFO] OpenRDF Sesame: Spatial Query result IO - HTML ..... <span style="color:LawnGreen">SUCCESS</span> [  0.548 s]
[INFO] OpenRDF Sesame: Spatial Query result IO - Text ..... <span style="color:LawnGreen">SUCCESS</span> [  2.989 s]
[INFO] OpenRDF Sesame: PostGISStore ....................... <span style="color:LawnGreen">SUCCESS</span> [  1.261 s]
[INFO] OpenRDF Sesame: MonetDBStore ....................... <span style="color:LawnGreen">SUCCESS</span> [  1.261 s]
[INFO] Strabon: Runtime ................................... <span style="color:LawnGreen">SUCCESS</span> [ 46.655 s]
[INFO] Strabon: Endpoint .................................. <span style="color:LawnGreen">SUCCESS</span> [ 21.723 s]
[INFO] Strabon: Endpoint client ........................... <span style="color:LawnGreen">SUCCESS</span> [ 28.748 s]
[INFO] Strabon: Executable endpoint ....................... <span style="color:LawnGreen">SUCCESS</span> [ 32.153 s]
[INFO] Strabon: Test Suite ................................ <span style="color:LawnGreen">SUCCESS</span> [  0.813 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD <span style="color:LawnGreen">SUCCESS</span>
[INFO] ------------------------------------------------------------------------==

After you have successfully built Strabon, you have access to the following components:

-   Strabon Endpoint
    
    This is a SPARQL endpoint for Strabon. It is distributed as a war file so you may deploy it in a Tomcat container. You may find the war file under directory `endpoint/target'.
    
-   Strabon Endpoint (standalone)
    
    This is a SPARQL endpoint for Strabon like the above one, but it differs only in that it does not require the user to have already set up a Tomcat container. The standalone Strabon Endpoint may be run by issuing the following command:
    
    ```
    $ java -jar endpoint-exec/target/strabon-endpoint-executable-${version}.jar
    ```
    
    After issuing the above command, you may access the Strabon Endpoint at the following URL: [http://localhost:8080/](http://localhost:8080/).
    
    Please see the page at [http://hg.strabon.di.uoa.gr/Strabon/rev/674f8f91162b](http://hg.strabon.di.uoa.gr/Strabon/rev/674f8f91162b) to find out other options that you may pass to the Tomcat container that will run by the above command.
    
    SPECIAL NOTE: if you need to configure the connection details to the underlying database, you may do so in two ways:
    
    1.  By modifying file `endpoint/WebContent/WEB-INF/connection.properties' before building Strabon and executing the above command.
    2.  After executing the above command, by visiting the following page by a browser: [http://localhost:8080/ChangeConnection](http://localhost:8080/ChangeConnection)
-   Strabon Endpoint Client
    
    This is a Java client for interacting with Strabon Endpoint or any other SPARQL endpoint. It is packaged as a jar file and may be found under directory `endpoint-client/target/' with namestrabon-endpoint-client-${version}.jar'`. This jar contains any dependencies to other code, so may copy and paste it to your project and start playing with the code immediately.
    
-   Strabon script
    
    The strabon script is located under the`scripts/` directory and it is the main command-line tool for interacting with Strabon. You may use it to store RDF data with geospatial information or query/update it using one of stSPARQL or GeoSPARQL query languages.
    
-   Endpoint script
    
    The endpoint script is located under the `scripts` directory and it is the main command-line tool for interacting with a `Strabon Endpoint`. You may use it to do any operation you would like to do with the `strabon script above`, but in contrast to the `strabon script you need to have access to a Strabon endpoint.` Of course, the Strabon Endpoint Client component above can be used as well as a command-line tool. At the time of writing, the `Strabon Endpoint Client` component supports only querying of RDF data with geospatial information.



All the necessary Strabon librairies were build in the folder: ***Strabon-4c2fcc026c4c/runtime/target*** especially the Strabon ones:

- sesame-queryalgebra-evaluation-spatial-3.3.2-SNAPSHOT.jar
- sesame-queryresultio-spatial-api-3.3.2-SNAPSHOT.jar
- sesame-queryresultio-spatial-sparqlgeojson-3.3.2-SNAPSHOT.jar
- sesame-queryresultio-spatial-sparqlhtml-3.3.2-SNAPSHOT.jar
- sesame-queryresultio-spatial-sparqlkml-3.3.2-SNAPSHOT.jar
- sesame-queryresultio-spatial-sparqlxml-3.3.2-SNAPSHOT.jar
- sesame-queryresultio-spatial-text-3.3.2-SNAPSHOT.jar
- sesame-sail-generaldb-3.3.2-SNAPSHOT.jar
- sesame-sail-monetdb-3.3.2-SNAPSHOT.jar
- sesame-sail-postgis-3.3.2-SNAPSHOT.jar
- strabon-runtime-3.3.2-SNAPSHOT.jar
- strabon-vocabulary-3.3.2-SNAPSHOT.jar


## Run Strabon

Ensure PostgreSQL is running.

Deploy the standalone Strabon endpoint (**strabon-endpoint-3.3.2-SNAPSHOT.war** 
located in **Strabon-4c2fcc026c4c/endpoint/target**) in a Tomcat container.
 
Once deployed, access: http://localhost:8080/strabon-endpoint-3.3.2-SNAPSHOT


![Strabon](/home/chdor/Projects/StoreConnect/workspace/Geographica-bench/Strabon.png)


## Load the Datasets
In order to run **Geographica Benchmark** you have to load the Geographica datasets provided into our PostGIS database.

To do this perform the following steps:

1. Verify that Strabon endpoint is running (see above instuctions)

2. Donwload **Geographica Datasets** and save them  on your *laptop*:

-   Greek Administrative Geography Dataset (GAG) ([download](http://geographica.di.uoa.gr/datasets/gag.tar.gz))
-   CORINE Land Use/Land Cover Dataset (CLC) ([download](http://geographica.di.uoa.gr/datasets/corine.tar.gz))
-   LinkedGeoData Dataset (LGD) ([download](http://geographica.di.uoa.gr/datasets/linkedgeodata.tar.gz))
-   GeoNames Dataset ([download](http://geographica.di.uoa.gr/datasets/geonames.tar.gz))
-   DBPedia Dataset ([download](http://geographica.di.uoa.gr/datasets/dbpedia.tar.gz))
-   Hotspots Dataset ([download](http://geographica.di.uoa.gr/datasets/hotspots.tar.gz))


3. Insert Datasets into our dabase
Once you have downloaded and upacked the datasets, load them into your database.
To do this, we use the scripts provided with Strabon. There are loacated in: `Strabon-4c2fcc026c4c/scripts`

   The script we used to load dataset is the **endpoint** script:

   `Usage: endpoint [OPTIONS] COMMAND ENDPOINT ARGS`

   `Execute SPARQL and SPARQL Update queries as well as store RDF triples on a Strabon endpoint.`

	`COMMAND: one of query, queryfile, update, store, describe, or help`
    `ENDPOINT: the URL of the Strabon Endpoint `
	`ARGS: arguments according to selected command`

    `OPTIONS can be any of the following`
	`-d	 : don't run, just print what shall be executed`
	`-i	 : include URI prefixes in the SPARQL query. Prefixes are taken from file "prefixes.sparql"` 	                       
   
   

- **Load GAG Dataset**:
```
$ ./endpoint store http://localhost:8080/strabon-endpoint-3.3.2-SNAPSHOT/ N-Triples -g http://geographica.di.uoa.gr/dataset/gag -u file://[path to dataset folder]/gag.nt
// Example:
// ./endpoint store http://localhost:8080/strabon-endpoint-3.3.2-SNAPSHOT/ N-Triples -g http://geographica.di.uoa.gr/dataset/gag-u file:///home/chdor/Projects/StoreConnect/benchs/dataset/gag.nt
```
- Explanations:
  + command: store. Upload the data into Strabon endpoint database
  + endpoint: http://localhost:8080/strabon-endpoint-3.3.2-SNAPSHOT
  + N-Triples: Data format used in Datasets
  + -g: Graph Name/Graph URI
  + -u: URL of file to upload (store).

- **Load CORINE Dataset**:

```
$ ./endpoint store http://localhost:8080/strabon-endpoint-3.3.2-SNAPSHOT/ N-Triples -g http://geographica.di.uoa.gr/dataset/clc -u file://[path to dataset folder]/corine.nt

```

- **Load LinkedGeoData Dataset**:

```
./endpoint store http://localhost:8080/strabon-endpoint-3.3.2-SNAPSHOT/ N-Triples -g http://geographica.di.uoa.gr/dataset/lgd -u file://[path to dataset folder]/linkedgeodata.nt
```

- **Load Geonames Dataset**:

```
./endpoint store http://localhost:8080/strabon-endpoint-3.3.2-SNAPSHOT/ N-Triples -g http://geographica.di.uoa.gr/dataset/geonames -u file://[path to dataset folder]/geonames.nt
```

- **Load DBpedia Dataset**:

```
/endpoint store http://localhost:8080/strabon-endpoint-3.3.2-SNAPSHOT/ N-Triples -g http://geographica.di.uoa.gr/dataset/dbpedia -u file://[path to dataset folder]/dbpedia.nt
```

- **Load Hotspots Dataset**:

```
./endpoint store http://localhost:8080/strabon-endpoint-3.3.2-SNAPSHOT/ N-Triples -g http://geographica.di.uoa.gr/dataset/hotspots -u file://[path to dataset folder]/hotspots.nt
```



## Run the tests
Clone the Strabon repository on you *laptop*.

On eclipse (Eclipse Oxygen rev 3)


- [ ] sds

![[   ]](https://download.osgeo.org/icons/compressed.gif)
