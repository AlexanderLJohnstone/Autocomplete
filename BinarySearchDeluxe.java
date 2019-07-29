import java.util.Comparator;

//Search API
public class BinarySearchDeluxe{/**
 * Uses a binary search algorithm to find the first and the last indexes of terms matching to a key in an array.
 *
 * @author  Sandy Johnstone (aj87)
 */

    //Returns the index of the first key in a[] that
    //equals the search key, or -1 if no such key
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        //throw exceptions for any null values
        if (a == null || key == null || comparator == null) {
            throw new NullPointerException();
        }

//define variables for start and end of sublist
        int start = 0;
        int end = a.length - 1;

//perform binary search

//loop until the sublist is two values
//conditional statement must include +1 or could end up in endless loop
        while (start + 1 < end) {

            //find new middle value of sub array
            int middle = (start + end) / 2;

            //find out if the middle value has the prefix
            //change end and start values accordingly
            if (comparator.compare(key, a[middle]) > 0) {
                start = middle + 1;
            } else if(comparator.compare(key, a[middle]) < 0){
                end = middle - 1;
            }else{
                end = middle;
            }
        }

            if (comparator.compare(key, a[start]) == 0) {
                return start;
            } else if (comparator.compare(key, a[end]) == 0) {
                return end;
            } else {
                return -1;
            }

    }

    //Returns the index of the last key in a[] that
    //equals the search key, or -1 if no such key
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator){

        //throw exceptions for any null values
        if (a == null || key == null || comparator == null){
            throw new NullPointerException();
        }

        //define variables for start and end of sublist
        int start = 0;
        int end = a.length-1;

        //perform binary search

        //loop until the sublist is two values
        //conditional statement must include +1 or could end up in endless loop
        while(start + 1 < end){
            //find new middle value of sub array
            int middle = (start + end)/2 ;

            //find out if the middle value has the prefix
            //change end and start values accordingly
            if (comparator.compare(key, a[middle]) > 0) {
                start = middle + 1;
            } else if(comparator.compare(key, a[middle]) < 0){
                end = middle - 1;
            }else{
                start = middle;
            }
        }

        if(comparator.compare(key, a[end]) == 0){

            return end;
        }
        else if(comparator.compare(key, a[start]) == 0){

            return start;
        }
        else{

            return -1;
        }
    }
}
