import java.util.Comparator;

//Term API
public class Term implements Comparable<Term>{ //Class Term, taking a query and a weight, comparable lexicographically by query

    private String query;
    private long weight;

    public Term(String query, long weight){ /**Term needs to be immutable therefore constructors and getters but no setters
     *
     * @author  H. Thompson (ht44)
     * @param   query   String value stored by the term, can be compared to via Comparable
     * @param   weight
     */
        if (query.equals(null)){
            throw new NullPointerException();
        }
        if (weight < 0) {
            throw new IllegalArgumentException();
        }
        this.query = query;
        this.weight = weight;
    }

    public String getQuery(){
        return this.query;
    } //Returns this term's query

    public long getWeight(){
        return this.weight;
    } //Returns this term's weight

    // Compares the two terms in descending order by weight
    public static Comparator<Term> byReverseWeightOrder(){
        return new Comparator<Term>() {
            //return values that compare in reverse weight order
            public int compare(Term term1, Term term2){

                return Long.compare(term2.getWeight(), term1.getWeight());
            }
        };

    }

    //Compares the two terms in lexicographic order
    //but using only the first r characters of each query
     public static Comparator<Term> byPrefixOrder(int r){
        if (r < 0){
            throw new IllegalArgumentException();
        }
        return new Comparator<Term>() {
            public int compare(Term term1, Term term2){

                if (r <= term2.getQuery().length()){
                    return (term1.getQuery().substring(0,r).compareToIgnoreCase(term2.getQuery().substring(0,r)));
                }
                else {
                    return term1.getQuery().compareToIgnoreCase(term2.getQuery());
                    //returns the signum of the alphabetical comparison of two Terms' query attributes, used for comparing 2 Terms
                }
            }
        };

    }

    //Compares the two terms in lexicographic order by query
    public int compareTo(Term that){    //Compares the queries using String.compareTo
    return(query.toLowerCase().compareTo(that.query.toLowerCase()));
    }

    //Returns a string representation of this term in the format:
    //the weight, followed by a tab, followed by the query
    public String toString(){
        return(weight + "   " + query);
    }
}