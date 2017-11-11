import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.io.File;

public class Main {

    private static String input_path;   // -docs
    private static float f_param;       // -f (probability of tails)
    private static ArrayList<Page> pages;
    private static int n = 0;
    private static float epsilon;
    private static float sum_base = 0;
    private static Hashtable<String, Integer> lookup = new Hashtable<>();
    private static float outWeights[][];
    private static float inWeights[][];

    public static void main(String[] args) throws IOException {

        getInput(args);
        input_path 	= ".\\input";
        f_param 	= (float)0.7;
        pages 		= processInputPages(input_path);
        epsilon 	= (float) 0.01/n;
        pages		= getInboundLinksIntoPages(pages);
        
        
        // Calculate Total Score for all Pages
        for(Page p : pages){
        	sum_base += p.base;
        }
        
        // Use Total Score above to get the normalized score
        for(Page p : pages){ 
        	p.score = p.base = p.base / sum_base; 
        }
 
        // Declaration of Inweights and Outweights
        outWeights = new float[n][n];
        inWeights = new float[n][n];
        
        for(Page p : pages){
        	// if page has no outlinks, then assign w[p] a weight of 1/n for all q
        	if(p.outlinks.size() == 0){
                for(int j=0; j<n; j++){
                    outWeights[j][lookup.get(p.title)] = (float) 1.0/n;
                }
            }
            	
        	// compute link outWeights
            else{
                // get unnormalized outWeights
            	for(Link l : p.outlinks){
            		outWeights[lookup.get(l.to)][lookup.get(l.from)] += calculateWeight(l, p);
                }
            	//get sum
                float sum = 0;
                for(int j=0; j<n; j++){
                    sum += outWeights[j][lookup.get(p.title)];
                }

                //adjust outWeights to be normalized
                for(int j=0; j<n; j++){
                    outWeights[j][lookup.get(p.title)] = outWeights[j][lookup.get(p.title)] / sum;
                }
                
            }
        	
        	
        	
        	// if page has no inlinks, then assign w[p] a weight of 1/n for all q
        	if(p.inlinks.size() == 0){
                for(int j=0; j<n; j++){
                    inWeights[j][lookup.get(p.title)] = (float) 1.0/n;
                }
            }
            	
        	// compute link inWeights
            else{
                // get unnormalized inWeights
            	for(Link l : p.inlinks){
            		inWeights[lookup.get(l.to)][lookup.get(l.from)] += calculateWeight(l, p);
                }
            	//get sum
                float sum = 0;
                for(int j=0; j<n; j++){
                    sum += inWeights[j][lookup.get(p.title)];
                }

                //adjust inWeights to be normalized
                for(int j=0; j<n; j++){
                    inWeights[j][lookup.get(p.title)] = inWeights[j][lookup.get(p.title)] / sum;
                }
                
            }        	
        }
        
        printArray(inWeights);
    	System.out.println("====================================");
       
        ///// (3) Main loop to compute scores /////
        boolean changed = true;

        while(changed){
            changed = false;

            for(Page p : pages){
            	
//            	  System.out.println("==================================================================");
//                System.out.println("----- " + p.title + " -----");
//                System.out.println("p.base: " + p.base);
//                System.out.println("f param: " + f_param);

                float q_sum = 0;
                for(int j=0; j<n; j++){
//                System.out.print(outWeights[j][lookup.get(p.title)] + "\t | \t");
//                System.out.print(pages.get(j).score + "\n");
                    q_sum += (outWeights[lookup.get(p.title)][j] * inWeights[lookup.get(p.title)][j] * pages.get(j).score);
                }

//              System.out.println("q_sum: " + q_sum + "\n");

                p.newscore = (1 - f_param) * p.base + f_param * q_sum;

//              System.out.println("newscore: " + p.newscore);

                if (Math.abs(p.newscore - p.score) > epsilon){
//                  System.out.println("DIFF: " + Math.abs(p.newscore - p.score));
                    changed = true;
                }

            }

            //initialize scores
            for(Page p : pages){
                p.score = p.newscore;
            }
        }




        ///// (*) PRINT DEBUGGING INFO /////

//        System.out.println("\n-------------------------------------\n");
//        Util.print2DArray(outWeights);
//        System.out.println(pages.toString());


        Collections.sort(pages, new Comparator<Page>() {
            public int compare(Page p1, Page p2) {
                return p1.score > p2.score ? -1 : 1;
            }
        });

        for(int j=0; j<n; j++){
            System.out.print(String.format("%-15s", pages.get(j).title));
                    System.out.println(pages.get(j).score);
        }
    }



    // compute a weight given a link (l) and a page (p)
    private static int calculateWeight(Link l, Page p){

        int score = 1;

        if(l.isBold){
            score++;
        }

        return score;
    }

    private static ArrayList<Page> getInboundLinksIntoPages(ArrayList<Page> pages) {
    	Page[] arrayPages = pages.toArray(new Page[pages.size()]);
    	for (int i = 0; i < arrayPages.length; i++) {
			Link[] arrayLinks = arrayPages[i].outlinks.toArray(new Link[arrayPages[i].outlinks.size()]);
			for (int j = 0; j < arrayLinks.length; j++) {
				for (int k = 0; k < arrayPages.length; k++) {
					if(arrayPages[k].title.equals(arrayLinks[j].to)) {
						arrayPages[k].inlinks.add(arrayLinks[j].reverse());
					}
				}
			}
		}
    	return pages;
    }
    
    //count the number of docs there are, initialize an array of Page objects, for each page object compute a wordcount/score
    private static ArrayList<Page> processInputPages(String input_path) throws IOException {

        ArrayList<Page> pages = new ArrayList<>();
        File dir = new File(input_path);
        File[] directoryListing = dir.listFiles();
        
        // Iterate through all the files in the directory
        if (directoryListing != null) {
            for (File child : directoryListing) {

                // Create a Page object from child file, extract name of page
                Page p = new Page(child.getName().replaceFirst("[.][^.]+$", ""));

                // Create an outlinks array for the page
                Document doc = Jsoup.parse(child, "UTF-8");
                Elements links = doc.select("a[href]");
                
                // Iterate through all possible links
                for(Element link : links){
                	
                	// Get the name of the file to which this current file links
                	String x = link.attr("href").replaceFirst("[.][^.]+$", "");
                	
                	// Create a link corresponding to this outlink
                	Link outLinks = new Link(p.title, x);

                	// Code to determine the importance of the hyperlink
                    String parent_tag = link.parent().tagName();
                    if(parent_tag.equals("b") || parent_tag.equals("em") || parent_tag.equals("h1") || parent_tag.equals("h2") || parent_tag.equals("h3") || parent_tag.equals("h4")){
                        outLinks.isBold = true;
                    }
                    
                    // Add this link to the outlinks for the page
                    p.outlinks.add(outLinks);
                }

                // Get the document's total Word count and initialize its base value
                p.wordcount = doc.text().split(" +").length;
                p.base = (float) (Math.log(p.wordcount) / Math.log(2));
                //System.out.println("P.BASE: " + p.base);

                // store page object in page array
                pages.add(p);
                

                // add an index for the page
                lookup.put(p.title, n);
                
                //increment page count
                n++;
            }
        } else {
            System.out.println(input_path + " is not a valid directory");
        }

        return pages;
    }
    
    // Helper function to print any 2D Float function
    static void printArray(float[][] x) {
    	for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				System.out.print(x[i][j]+"\t");
			}
			System.out.println();
		}
    }

    // Function to read in input
    private static void getInput(String[] args){
        for(int i=0; i<args.length; i++){
            switch(args[i]){
                case "-docs":
                    input_path = args[i+1];
                    i++;
                    break;
                case "-f":
                    f_param = Float.parseFloat(args[i + 1]);
                    i++;
                    break;
                default:
                    throw new IllegalArgumentException("Must provide arguments: " + "-docs [input path] -f [F parameter]");
            }
        }

//        System.out.println("-docs: " + input_path);
//        System.out.println("-f: " + f_param);

    }


}










