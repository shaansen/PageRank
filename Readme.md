# Analysing the Importance of different weight factors on Weighted PageRank Algorithm

### Contributors

  - Isha Potnis (ipotnis1@umbc.edu)
  - Akanksha Bhosale (akanksh1@umbc.edu)
  - Shantanu Sengupta (ssen1@umbc.edu)

### Project Description
In the PageRank Algorithm, the rank of a page is dependent only on the number of inbound links associated with that page. We evaluate and analyze the impact of four important factors i.e. page popularity, page history, user preferences and content of the document while raking a web page. In addition, each of these factors contributes differently towards ranking the pages i.e. some factors are more dominant in returning more relevant pages than others. In this research, we focus sharply to find out which weight factor is more dominant in returning the most relevant results and by what factor, on the basis of a user study. In conclusion, we found that Content has the maximum weightr in returning high quality responses compared to popularity, history and domain of the web page.

### Installation Guide

##### Project Setup
##### 1. Git clone or download the repository into your local machine
Commands for cloning into through git:
```sh
git clone https://github.com/shaansen/PageRank.git
```
```sh
If you download the ZIP folder, unzip it.
```
##### 2. Include required html pages in the input folder if you want to experiment with more html pages

##### 3. Include the jsoup jar into the build path variables.

##### 4. Run the .py files in Python compiler to generate history_log and domain_log files
Commands for generating history_log:
```sh
python ./generatelog.py
```
Commands for generating domain_log:
```sh
python ./mydomainlog.py
```
Include both the generated files in input folder

You are done with initial setup required for the project.

#### Compile and Run:
##### 1. run javac and specify external jars
```sh
javac ­cp "./jars/*" *.java
```
##### 2. run program
```sh
java ­cp ".:./jars/*" Main ­f [F parameter] ­docs [input directory]
```
##### Example:
```sh
java ­cp ".:./jars/*" Main ­f 0.7 ­docs ./input
```

### References
- https://github.com/yfeng55/Weighted-Pagerank
##### 3rd Party Softwares/Libraries Used
- jsoup: Java HTML Parser - https://jsoup.org/
- WebCopy - https://www.cyotek.com/cyotek-webcopy
