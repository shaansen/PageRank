import java.util.ArrayList;



public class Page {

    public String title;
    public int wordcount;
    public float base;
    
    public ArrayList<Link> outlinks;
    public ArrayList<Link> inlinks;
    
    public float popularityScore;
    public float newpopularityScore;
        
    public float avgtime;
    public float historyScore;
    
    public float domainScore;
    
    public float contentScore;
    
    public String url;
    
    public Page(String title){

        this.title = title;

        this.base = 0;
        this.popularityScore = 0;
        this.newpopularityScore = 0;
        this.outlinks = new ArrayList<>();
        this.inlinks = new ArrayList<>();
        this.avgtime=0;
        this.historyScore = 0;
        this.contentScore = 0;
        this.domainScore = 0;
        this.url = null;

    }

    public String toString(){
//        return this.title + ":" + this.outlinks.toString() + this.wordcount + "\n";
    	System.out.println( "\n" + this.title + "\n" + this.outlinks.toString() + "\n" + this.inlinks.toString() + "\n" + this.wordcount + "\n" + this.popularityScore);
        return "\n" + this.title + "\n" + this.outlinks.toString() + "\n" + this.inlinks.toString() + "\n" + this.wordcount + "\n" + this.popularityScore + "\n" + this.avgtime;
    }


}
