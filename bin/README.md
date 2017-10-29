### Weighted Page Rank

Adds on to the basic PageRank algorithm by considering page quality and link weights when applying scoring rules for pages in the collection

#### Scoring Rules:

quality: 
log(# of words in P)

normalized quality: 
(quality) / (sum of all quality scores)

link weight: 
+1 for each link from P to Q
+1 for header or bold tags

normalized link weight:
(link weight) / (sum of all link weights)

epsilon:
0.01 / (# of pages)


___


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
