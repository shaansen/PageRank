import java.util.ArrayList;



public class Page {

    public String title;
    public int wordcount;
    public float base;
    public ArrayList<Link> outlinks;
    public ArrayList<Link> inlinks;
    
    public float avgtime;
    public float historyScore;
    public float newhistoryScore;
    
    public float popularityScore;
    public float newpopularityScore;
    
    public float domainScore;
    
    public float contentScore;
    public float urlKeywords;
    public float titleKeywords;
    public float linkKeywords;
    public float bodyKeywords;
    
    public String url;
        
    public Page(String title){

        this.title = title;

        this.base = 0;
        // Initializing Popularity Score
        this.popularityScore = 0;
        this.newpopularityScore = 0;
        this.outlinks = new ArrayList<>();
        this.inlinks = new ArrayList<>();
        
        // Initializing History Variables
        this.avgtime=0;
        this.historyScore = 0;
        
        // Initializing Content Variables
        this.contentScore = 0;
        this.urlKeywords = 0;
        this.titleKeywords = 0;
        this.bodyKeywords = 0;
        this.linkKeywords = 0;

        // Initializing Domain Variables
        this.domainScore = 0;
        this.url = "";        

    }

    public String toString(){
//        return this.title + ":" + this.outlinks.toString() + this.wordcount + "\n";
    	System.out.println("Page :\t\t "+this.title);
    	System.out.println("Popularity :\t "+this.popularityScore);
    	System.out.println("History :\t "+this.historyScore);
    	System.out.println("Domain :\t "+this.domainScore);
    	System.out.println("Content :\t "+this.contentScore);
    	System.out.println("title :\t "+this.titleKeywords);
    	System.out.println("body :\t "+this.bodyKeywords);
    	System.out.println("link :\t "+this.linkKeywords);
    	System.out.println("url :\t "+this.urlKeywords);
    	
    	return "-----------------------------------------";
    }


}
