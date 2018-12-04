# us-election-search-engine

U.S. Elections Search Engine Readme:

In order to run the project from the project folder run:

java -cp elections-1.0-SNAPSHOT-jar-with-dependencies.jar api.routes.Router

The webpage can be accessed on the following url:

http://localhost:8080/

Some sample queries with configurations:
1. Query: presidential election
   Relevance: Vector Space
   Clustering: No option selected
   Query Expansion: No option selected

2. Query: george w bush
   Relevance: Page Rank
   Clustering: No option selected
   Query Expansion: No option selected

3. Query: bill clinton
   Relevance: HITS
   Clustering: No option selected
   Query Expansion: No option selected

Note: For HITS the first 10 results are HUBS and the next 10 are AUTHORITIES

4. Query: florida recount
   Relevance: Vector Space
   Clustering: No option selected
   Query Expansion: Association

5. Query: us presidential
   Relevance: Vector Space
   Clustering: No option selected
   Query Expansion: Scalar

6. Query: electoral college
   Relevance: Vector Space
   Clustering: No option selected
   Query Expansion: Rocchio


If you want to build the project from scratch and then run you will require Java 1.8 and maven installed on your system.

Use the following instructions for building and running the code:

1. mvn clean -DskipTests install
2. mv target/elections-1.0-SNAPSHOT-jar-with-dependencies.jar .
3. java -cp elections-1.0-SNAPSHOT-jar-with-dependencies.jar api.routes.Router

Note: the commands need to be run from the project root directory.

The following openly available material was used (crediting to the authors):
1. The home webpage template has been taken from [Colorlib](https://colorlib.com/wp/).
2. The favicon is taken from [Flaticons](https://www.flaticon.com/authors/vectors-market).
