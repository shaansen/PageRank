import java.util.ArrayList;



public class Page {

    public String title;
    public int wordcount;
    public float base;
    public float score;
    public float newscore;
    
    public ArrayList<Link> outlinks;
    public ArrayList<Link> inlinks;
    public float avgtime;
    public float avgtimescore;

    public Page(String title){

        this.title = title;

        this.base = 0;
        this.score = 0;
        this.newscore = 0;
        this.outlinks = new ArrayList<>();
        this.inlinks = new ArrayList<>();
        this.avgtime=0;
        this.avgtimescore = 0;
        

    }

    public String toString(){
//        return this.title + ":" + this.outlinks.toString() + this.wordcount + "\n";
    	System.out.println( "\n" + this.title + "\n" + this.outlinks.toString() + "\n" + this.inlinks.toString() + "\n" + this.wordcount + "\n" + this.score);
        return "\n" + this.title + "\n" + this.outlinks.toString() + "\n" + this.inlinks.toString() + "\n" + this.wordcount + "\n" + this.score + "\n" + this.avgtime;
    }


}
