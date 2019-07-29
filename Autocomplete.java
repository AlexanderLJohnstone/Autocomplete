public class Autocomplete {/**
 * A class that matches a query term to terms in an array, and returning information about the matches
 */

    private Term[] terms;
    int firstIndex;

    public Term[] getTerms() {
        return terms;
    }

    public Autocomplete(Term[] terms) { /**
     *  Class constructor specifying the list of terms to be used
     *
     * @author  H. Thompson (ht44)
     * @param   terms   The array of terms to be used by the class
     *
     */
        this.terms = new Term[terms.length];
        for (int i = 0; i < terms.length; i++) {
            if (terms[i].getQuery().equals(null)) {
                throw new NullPointerException();
            } else {
                this.terms[i] = terms[i];
            }
        }
    }

    public Term[] allMatches(String prefix){ /**
     * Takes in a string and finds all the matches in the lexicographically sorted array of terms
     * by calling the comparators from term and the BinarySearchDeluxe classes.
     *
     * @author   H. Thompson (ht44)
     * @param   prefix  the prefix to which all the matches are found
     * @return          an array of all the matching terms, sorted lexicographically
     *
     */
        if (prefix.equals(null)){
            throw new NullPointerException();
        }
        Term[] matchingTerms = new Term[numberOfMatches(prefix)];
        for (int i = 0; i < matchingTerms.length; i++) {
            matchingTerms[i] = terms[firstIndex + i];
        }

        return matchingTerms;
    }

    public int numberOfMatches(String prefix){/**
     * Returns the number of matches to a query in a list of terms.
     *
     * @author  H. Thompson (ht44)
     * @param   prefix  the query to which the number of matches are found.
     * @return          the number of matches
     */


        if (prefix.equals(null)){
            throw new NullPointerException();
        }
        Term searchTerm = new Term(prefix, 0);
        firstIndex = BinarySearchDeluxe.firstIndexOf(terms, searchTerm, Term.byPrefixOrder(prefix.length()));
        int lastIndex = BinarySearchDeluxe.lastIndexOf(terms, searchTerm, Term.byPrefixOrder(prefix.length()));
        return lastIndex - firstIndex  + 1; //The difference of two numbers is always one less than the number of numbers between those two numbers inclusive
    }
}