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
|     Hotspots    	| "


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
  
  
