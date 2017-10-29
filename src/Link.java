
public class Link {

    public String from;
    public String to;

    public boolean isBold = false;
    public int linkCount;

    public Link(String from, String to){
        this.from = from;
        this.to = to;
        this.linkCount = 1;
    }

    public String toString(){
        return "(" + this.from + ", " + this.to + ")";
    }


    @Override
    public boolean equals(Object o){

        if(o == this){
            return true;
        }
        if (!(o instanceof Link)) {
            return false;
        }

        Link l = (Link) o;

        return this.to.equals(l.to) && this.from.equals(l.from);


    }

}
