import java.util.ArrayList;



public class Page {

    public String title;
    public int wordcount;
    public float base;
    public float score;
    public float newscore;
    public ArrayList<Link> outlinks;
    public ArrayList<Link> inlinks;


    public Page(String title){

        this.title = title;

        this.base = 0;
        this.score = 0;
        this.newscore = 0;
        this.outlinks = new ArrayList<>();
        this.inlinks = new ArrayList<>();

    }

    public String toString(){
//        return this.title + ":" + this.outlinks.toString() + this.wordcount + "\n";
        return "\n" + this.title + "\n" + this.outlinks.toString() + "\n" + this.inlinks.toString() + "\n" + this.wordcount + "\n" + this.score;
    }


}
