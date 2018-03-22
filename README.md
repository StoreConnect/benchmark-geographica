# GEOGRAPHICA #

[Geographica](http://geographica.di.uoa.gr/) is a **GeoSPARQL Benchmark** for Geospatial RDF Stores made by [George Garbis](http://users.uoa.gr/~ggarbis/),[Kostis Kyzirakos](http://cgi.di.uoa.gr/~kkyzir/) and [Manolis Koubarakis](http://cgi.di.uoa.gr/~koubarak/).

## Geographica (Introduction )


Geospatial extensions of SPARQL like GeoSPARQL and stSPARQL have recently been defined and corresponding geospatial RDF stores have been implemented. However, there is no widely used benchmark for evaluating geospatial RDF stores which takes into account recent advances to the state of the art in this area. We have developed a benchmark, called Geographica, which uses both real-world and synthetic data to test the offered functionality and the performance of some prominent geospatial RDF stores. Our benchmark is composed by two workloads with their associated datasets and queries: a real-world workload based on publicly available linked data sets and a synthetic workload.

## This Guide
Hi and welcome to the unofficial guide of Geographica !. This guide explain and describe *(in detail ? :-) )* how to use **Geographica** to experiments *(benchmarking)* four Geospatial RDF Store:
+ [Strabon (v3.3.2-SNAPSHOT)](http://geographica.di.uoa.gr/)
+ [Virtuoso Open Source Edition (VOS) v7.2.4](http://virtuoso.openlinksw.com/dataspace/doc/dav/wiki/Main/)
+ [Apache Jena/Fuseki (v3.6.0)](https://jena.apache.org/)
+ [RDF4J (v2.2.4)](http://rdf4j.org/)



### `Datasets`
To test and experiment Geopsatial performance of an RDF Store, **Geographica** executes a serie of Geospatial (GeoSPARQL) queries on *specific* data.

The data using to perform those queries are provided by six datasets:

+ Greek Administrative Geography Dataset (GAG) ([download](http://geographica.di.uoa.gr/datasets/gag.tar.gz))
+ CORINE Land Use/Land Cover Dataset (CLC) ([download](http://geographica.di.uoa.gr/datasets/corine.tar.gz))
+ LinkedGeoData Dataset (LGD) ([download](http://geographica.di.uoa.gr/datasets/linkedgeodata.tar.gz))
+ GeoNames Dataset ([download](http://geographica.di.uoa.gr/datasets/geonames.tar.gz))
+ DBPedia Dataset ([download](http://geographica.di.uoa.gr/datasets/dbpedia.tar.gz))
+ Hotspots Dataset ([download](http://geographica.di.uoa.gr/datasets/hotspots.tar.gz))




__Datasets specifications:__

|     Datasets     | Size  | Triples | # of Points   | # of Lines | # of Polygons |
|:---------------:|:-------:|:---------:|:-------------:|:------------:|:-----------:|
|       GAG       |  33MB |    4K   |      -      |      -     |    325    |
|       CLC       | 401MB | 630K    | -           | -          | 45K       |
| LGD (only ways) | 29MB  | 150K    | -           | 12k        | -         |
|     GeoNames    | 45MB  | 400K    | 22K         | -          | -         |
|     DBpedia     | 89MB  | 430K    | 8K          |            |           |
|      Hostspots           | 90MB      |  450K       | -             | -            |  37K          |




__Datasets Graph URI:__

|     Dataset     	|                    URI                    	|
|:---------------:	|:-----------------------------------------------:	|
|       GAG       	|    "http://geographica.di.uoa.gr/dataset/gag"   	|
|       CLC       	|    "http://geographica.di.uoa.gr/dataset/clc"   	|
| LGD (only ways) 	|    "http://geographica.di.uoa.gr/dataset/lgd"   	|
|     GeoNames    	| "http://geographica.di.uoa.gr/dataset/geonames" 	|
|     DBpedia     	|  "http://geographica.di.uoa.gr/dataset/dbpedia" 	|
|     Hotspots    	| "http://geographica.di.uoa.gr/dataset/hotspots"   |


## `Workload`
The **Geographica** workload uses real-world publicly available linked geospatial data. This workload consists of a micro benchmark and a macro benchmark. 

**Micro Benchmark**

The Micro Benchmark aims at testing the efficiency of primitive spatial functions in state of the art geospatial RDF stores. Thus, we use simple SPARQL queries which consist of one or two triple patterns and a spatial function. We start by checking simple spatial selections. Next, we test more complex operations such as spatial joins. We test spatial joins using the
topological relations defined by stSPARQL [9] and the Geometry Topology component of GeoSPARQL.
Apart from topological relations, we test non topological functions (e.g., geof:buffer), defined by the Geometry extension of GeoSPARQL, which construct a new geometry object. Additionally, we test the metric function strdf:area which is only defined in stSPARQL. The aggregate functions strdf:extent, and strdf:union of stSPARQL are also tested by this benchmark. GeoSPARQL does not define aggregate functions. We include aggregate functions in Geographica since they are present in all geospatial RDBMS.





**Macro Benchmark**

In the macro benchmark we aim to test the performance of the selected RDF stores in the following typical application scenarios: reverse geocoding, map search and browsing, and two scenarios from the Earth Observation domain.

+ Reverse Geocoding.

  Reverse geocoding is the process of attributing a readable address or place name to a given point. Thus, in this scenario, we pose SPARQL queries which sort retrieved objects by their distance to the given point and select the first one.


+ Map Search and Browsing.

  This scenario demonstrates the queries that are typically used in Web-based mapping applications. A user first searches for points  of interest based on thematic criteria. Then, she selects a specific point and information about the area around it is retrieved (e.g., POIs and roads).
 

+ Rapid Mapping for Wild Fire Monitoring.

  In this scenario we test queries which retrieve map layers for creating a map that can be used by decision makers tasked with the monitoring of wild fires. This application has been studied in detail in project TELEIOS [7] and the scenario covers its core querying needs. First, spatial selections are used to retrieve basic information of interest (e.g., roads, administrative areas etc.). Second more complex information can be derived using spatial joins and non-topological functions. For example, a user maybe interested in the segment of roads that may be damaged by fire. We point out that this scenario is representative of many rapid mapping tasks encountered in Earth Observation applications.

  
  
## `Queries`
The Experiments are measured through the execution of GeoSPARQL queries depending on the type of benchmark performed:

###  Micro Benchmark (29 queries)

  #### `Non-topological construct functions`

| Query |  Operation  |                    Description                   |
|:-----:|:-----------:|:------------------------------------------------:|
|   Q1  |   Boundary  |   Construct the boundary of all polygons of CLC  |
|   Q2  |   Envelope  |   Construct the envelope of all polygons of CLC  |
|   Q3  | Convex Hull | Construct the convex hull of all polygons of CLC |
|   Q4  |    Buffer   |  Construct the buffer of all points of GeoNames  |
|   Q5  |    Buffer   |     Construct the buffer of all lines of LGD     |
|   Q6  |     Area    |      Compute the area of all polygons of CLC     |
    
  #### `Spatial selections`
  
| Query |         Operation        |                                   Description                                   |
|:-----:|:------------------------:|:-------------------------------------------------------------------------------:|
|   Q7  |          Equals          |         Find all lines of LGD that are spatially equal with a given line        |
|   Q8  |          Equals          |        Find all polygons of GAG that are spatially equal a given polygon        |
|   Q9  |        Intersects        |       Find all lines of LGD that spatially intersect with a given polygon       |
|  Q10  |        Intersects        |       Find all polygons of GAG that spatially intersect with a given line       |
|  Q11  |         Overlaps         |       Find all polygons of GAG that spatially overlap with a given polygon      |
|  Q12  |          Crosses         |             Find all lines of LGD that spatially cross a given line             |
|  Q13  |      Within polygons     |        Find all points of GeoNames that are contained in a given polygon        |
|  Q14  | Within buffer of a point |  Find all points of GeoNames that are contained in the buffer of a given point  |
|  Q15  |       Near a point       | Find all points of GeoNames that are within specific distance from a givenpoint |
|  Q16  |         Disjoint         |    Find all points of GeoNames that are spatially disjoint of a given polygon   |
|  Q17  |         Disjoint         |       Find all lines of LGD that are spatially disjoint of a given polygon      |

  #### `Spatial joins`
  
| Query |  Operation |                                  Description                                 |
|:-----:|:----------:|:----------------------------------------------------------------------------:|
|  Q18  |   Equals   | Find all points of GeoNames that are spatially equal with a point of DBpedia |
|  Q19  | Intersects |      Find all points of GeoNames that spatially intersect a line of LGD      |
|  Q20  | Intersects |     Find all points of GeoNames that spatially intersect a polygon of GAG    |
|  Q21  | Intersects |        Find all lines of LGD that spatially intersect a polygon of GAG       |
|  Q22  |   Within   |         Find all points of GeoNames that are within a polygon of GAG         |
|  Q23  |   Within   |            Find all lines of LGD that are within a polygon of GAG            |
|  Q24  |   Within   |           Find all polygons of CLC that are within a polygon of GAG          |
|  Q25  |   Crosses  |          Find all lines of LGD that spatially cross a polygon of GAG         |
|  Q26  |   Touches  |      Find all polygons of GAG that spatially touch other polygons of GAG     |
|  Q27  |  Overlaps  |        Find all polygons of CLC that spatially overlap polygons of GAG       |

   #### `Aggregate functions`
  
| Query | Operation |                   Description                  |
|:-----:|:---------:|:----------------------------------------------:|
|  Q28  | Extension | Construct the extension of all polygons of GAG |
|  Q29  |   Union   |   Construct the union of all polygons of GAG   |


### Macro Benchmark (11 queries)

  #### `Reverse Geocoding`
  
| Query |                    Description                   |
|:-----:|:------------------------------------------------:|
|  RG1  | Find the closest populated place (from GeoNames) |
|  RG2  |        Find the closest street (from LGD)        |
  
  #### `Map Search and Browsing`
  
| Query |                                    Description                                   |
|:-----:|:--------------------------------------------------------------------------------:|
|  MSB1 |  Find the co-ordinates of a given POI based on thematic criteria (from GeoNames) |
|  MSB2 |      Find roads in a given bounding box around these co-ordinates (from LGD)     |
| MSB3  | Find other POI in a given bounding box around these co-ordinates (from GeoNames) |

  #### `Rapid Mapping for Fire Monitoring`
  
| Query |                                             Description                                            |
|:-----:|:--------------------------------------------------------------------------------------------------:|
|  RM1  |                 Find the land cover of areas inside a given bounding box (from CLC)                |
|  RM2  |                     )Find primary roads inside a given bounding box (from LGD)                     |
| RM3   | Find detected hotspots inside a given bounding box (from Hotspots)                                 |
| RM4   | Find municipality boundaries inside a given bounding box (from GAG)                                |
| RM5   | Find coniferous forests inside a given bounding box which are on fire (from CLC andHotspots)       |
| RM6   | Find road segments inside a given bounding box which may be damaged by fire (fromLGD and Hotspots) |


## `Experiments`
There are seven main experiments, each one coded as a Java Class:

###  Micro Benchmark

|              Experiment             | Queries |                                Class                                |
|:-----------------------------------:|:-------:|:-------------------------------------------------------------------:|
| MicroNonTopological |  Q1-Q6  | gr.uoa.di.rdf.Geographica.experiments.MicroNonTopologicalExperiment |
|          MicroSelections         |  Q7-Q17 |   gr.uoa.di.rdf.Geographica.experiments.MicroSelectionsExperiment   |
|            MicroJoins            | Q18-Q27 |      gr.uoa.di.rdf.Geographica.experiments.MicroJoinsExperiment     |
|         MicroAggregations         | Q28-Q29 |  .gr.uoa.di.rdf.Geographica.experiments.MicroAggregationsExperiment |


### Macro Benchmark

|       Experiment      |  Queries |                                 Class                                 |
|:---------------------:|:--------:|:---------------------------------------------------------------------:|
| MacroReverseGeocoding | RG1-RG2  | gr.uoa.di.rdf.Geographica.experiments.MacroReverseGeocodingExperiment |
|     MacroMapSearch    | MSB1-MSB3 |     gr.uoa.di.rdf.Geographica.experiments.MacroMapSearchExperiment    |
|   MacroRapidMapping   | RM1-RM6   |   gr.uoa.di.rdf.Geographica.experiments.MacroRapidMappingExperiment   |
