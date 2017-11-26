import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.File;

public class Main {

    private static String input_path;   // -docs
    private static float f_param;       // -f (probability of tails)
    private static ArrayList<Page> pages;
    private static ArrayList<Page> history_pages;
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
        //System.out.println(pages);
        
        //Calculate on the basis of history
        
        history_pages = processHistoryPages(pages);
        System.out.println("Printing history pages:"+history_pages);
        
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
        System.out.println("Printing page popularity Score");
        Collections.sort(pages, new Comparator<Page>() {
            public int compare(Page p1, Page p2) {
                return p1.score > p2.score ? -1 : 1;
            }
        });

        for(int j=0; j<n; j++){
            System.out.print(String.format("%-15s", pages.get(j).title));
                    System.out.println(pages.get(j).score);
        }

        // History Main loop computation
    ///// (3) Main loop to compute scores /////
        boolean changedhistory = true;

        while(changedhistory){
            changedhistory = false;

            for(Page p : history_pages){
            	
//            	  System.out.println("==================================================================");
//                System.out.println("----- " + p.title + " -----");
//                System.out.println("p.base: " + p.base);
//                System.out.println("f param: " + f_param);

                
                float history_sum=0;
                for(int j=0; j<n; j++){
//                System.out.print(outWeights[j][lookup.get(p.title)] + "\t | \t");
//                System.out.print(pages.get(j).score + "\n");
                
                    history_sum += pages.get(j).avgtime;
                }

//              System.out.println("q_sum: " + q_sum + "\n");

                //p.newscore = (1 - f_param) * p.base + f_param * q_sum;
                p.avgtimescore = (1 - f_param) * p.base + f_param * history_sum;

//              System.out.println("newscore: " + p.newscore);

                if (Math.abs(p.avgtimescore - p.score) > epsilon){
//                  System.out.println("DIFF: " + Math.abs(p.newscore - p.score));
                    changedhistory = true;
                }

            }

            //initialize scores
            for(Page p : history_pages){
                p.score = p.avgtimescore;
            }
        }



        ///// (*) PRINT DEBUGGING INFO /////

//        System.out.println("\n-------------------------------------\n");
//        Util.print2DArray(outWeights);
//        System.out.println(pages.toString());

        System.out.println("Printing page history Score");
        Collections.sort(history_pages, new Comparator<Page>() {
            public int compare(Page p1, Page p2) {
                return p1.score > p2.score ? -1 : 1;
            }
        });

        for(int j=0; j<n; j++){
            System.out.print(String.format("%-15s", history_pages.get(j).title));
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
    
    //Calculate average time spent by the user on every page
    private static ArrayList<Page> processHistoryPages(ArrayList<Page> pages)
    {
    	//ArrayList<Page> pages = new ArrayList<>();
    	List<String> records = new ArrayList<String>();
    	HashMap<String,List<Integer>> map = new HashMap<>();
    	try{
    		BufferedReader reader = new BufferedReader(new FileReader(".//input/history_log"));
    	    String line;
    	    while ((line = reader.readLine()) != null)
    	    {
    	      //System.out.println(line);
    	    	//records.add(line);
    	    	String[] recordinfo = line.split("\\s");
    	    	//System.out.println(recordinfo[2]);
    	    	if(!map.containsKey(recordinfo[1]))
        		{
        			map.put(recordinfo[1],new ArrayList<Integer>());
        			map.get(recordinfo[1]).add(Integer.parseInt(recordinfo[2]));
        			map.get(recordinfo[1]).add(1);
        		}
    	    	else
    	    	{
    	    		map.get(recordinfo[1]).set(0,map.get(recordinfo[1]).get(0)+Integer.parseInt(recordinfo[2]));
    	    		map.get(recordinfo[1]).set(1,map.get(recordinfo[1]).get(1)+1);
    	    	}
    	    	
    	    }
    	    System.out.println("map size"+map.size());
    	    
    	    reader.close();
    	    //return records;
    	  }
    	  catch (Exception e)
    	  {
    	    System.err.format("Exception occurred trying to read ");
    	    e.printStackTrace();
    	    return null;
    	  }
    	//iterate over map to calculate avg time
    	HashMap<String,Float> avgtime = new HashMap<String,Float>();
    	for(Map.Entry<String,List<Integer>> entry: map.entrySet())
    	{
    		List<Integer> internal = entry.getValue();
    		float average_time = (float)(internal.get(0)/internal.get(1));
    		avgtime.put(entry.getKey(),average_time);
    	}
    	//Now normalize the average time spent by a group of user on each web page
    	float total_avg = 0;
    	for(Map.Entry<String,Float> entry : avgtime.entrySet())
    	{
    		//System.out.println("File "+entry.getKey()+"avg time:"+entry.getValue());
    		total_avg = total_avg + entry.getValue();
    	}
    	for(Map.Entry<String,Float> entry : avgtime.entrySet())
    	{
    		//System.out.println("File "+entry.getKey()+"avg time:"+entry.getValue());
    		entry.setValue(entry.getValue()/total_avg);
    	}
    	for(int k=0;k<pages.size();k++)
    	{
    		//System.out.println("page object"+pages.get(k).title);
    		String htmlname = pages.get(k).title+".html";
    		pages.get(k).avgtime = avgtime.get(htmlname); 
    	}
    	return pages;
    	}
    	
    
    //count the number of docs there are, initialize an array of Page objects, for each page object compute a wordcount/score
    private static ArrayList<Page> processInputPages(String input_path) throws IOException {

        ArrayList<Page> pages = new ArrayList<>();
        ArrayList<File> htmlfiles = new ArrayList<>();
        File dir = new File(input_path);
        File[] directoryListing = dir.listFiles();
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith((".html"))) {
              htmlfiles.add(file);
            }
          }
        
        // Iterate through all the files in the directory
        if (directoryListing != null) {
            for (File child : htmlfiles) {

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










