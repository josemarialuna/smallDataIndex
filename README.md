# smallDataIndex

This package contains Weka Cluster algorithm with a complete list of indices that will help you to decide the optimal number of clusters that the dataset could have.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

This is a Maven Project with OpenCSV 2.4 and Weka 3.8.0 dependencies. Both of them are included in the pom.xml file in the repository.

```
<dependency>
    <groupId>au.com.bytecode</groupId>
    <artifactId>opencsv</artifactId>
    <version>2.4</version>
</dependency>

        
<dependency>
    <groupId>nz.ac.waikato.cms.weka</groupId>
    <artifactId>weka-stable</artifactId>
    <version>3.8.0</version>
</dependency>
```

## Running

WekaCluster is the main class, and it includes 4 arguments that can be set from the code or directly when you execute the jar file:
* Argument 0: minNumCluster: it is the minimum cluster number that the dataset is going to be tested.
* Argument 1: maxNumCluster: it is the maximum cluster number that the dataset is going to be tested.
* Argument 2: pathFile: it is the path of the input dataset. It must includes the complete pathfile.
* Argument 3: outFile: it is the path of the output result file. It must includes the complete pathfile.
* Argument 4: selector: you can choose between SMALLDATA, BIGDATA and ALL. SMALLDATA only execute smalldata indices. BIGDATA just execute BD-Silhouette and BD-Dunn. And ALL execute SMALLDATA and BIGDATA indices.

```
int minNumCluster = 2;
int maxNumCluster = 10;
int selector = SMALLDATA;

String fileName = "SmallDataset.csv";
String folderFile = "C:\\datasets\\";
String pathFile = folderFile + fileName;
String outFile = getFileNameOutput(selector, fileName);
```
For this configuration the application load a file called SmallDataset.csv in "C:/datasets" and the result file will be saved as "Results-SmallDataset.csv"in the application folder.

### Execution example

If we preffer executing in a terminal using java we just have to:

```
java -jar smallDataIndices.jar 2 10 C:/datasets/SmallDataset.csv Results.csv ALL
java -jar smallDataIndices.jar 10 20 datasets/dataset.csv results.csv SMALLDATA
```

## Built With

* [Weka](http://www.cs.waikato.ac.nz/ml/weka/) - Kmeans from Weka.
* [Maven](https://maven.apache.org/) - Dependency Management.
* [EmergentOrder](https://github.com/EmergentOrder/opencsv) - For the use of CSV format.

## Authors

* **José María Luna** - *Initial work* - [José María Luna Romera](https://github.com/josemarialuna)

## Acknowledgments

* University of Waikato
