import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;


//class that defines an interactive user interface
public class AutocompleteGUI extends JFrame{/**
 * A GUI that displays and live-updates autocomplete terms based on a user-entered query.
 */
    //Constants for outputs
    private static final String NO_MATCHES_MESSAGE = "No Matches";
    private static final Term[] NO_MATCHES_ARRAY = {new Term(NO_MATCHES_MESSAGE, 0)};

    //
    static int noOfTermsDisplayed;
    String[] args;
    static Term[] allTerms;

    //declare the GUI elements
    JLabel autocompleteLabel = new JLabel("Autocomplete");
    JTextField queryBox = new JTextField("");
    JPanel panel = new JPanel();
    JTextArea resultsArea = new JTextArea();
    JFrame frame = new JFrame("Autocomplete");
    JScrollPane scroll = new JScrollPane(resultsArea);


    //constructor
    public AutocompleteGUI(String[] args, Term[] allTerms){/**
     *Class constructor that creates a GUI with query box and scrollable results area.
     *
     * @author  Sandy Johnstone (aj87)
     * @param   args    an array of the strings inputted by the user as command line arguments
     * @param   allTerms    an array of all terms from the input file
     */
        //call JFrame constructor
        super("Autocomplete");

        //get arguments and list of terms from main method
        this.args = args;
        this.allTerms = allTerms;

        //initialise settings of the window
        frame.setSize(600,500);
        frame.setVisible(false);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel.setLayout(null);

        //make all the elements visible
        queryBox.setVisible(true);
        resultsArea.setVisible(true);
        autocompleteLabel.setVisible(true);

        //position elements
        autocompleteLabel.setBounds(260,10,100,50);
        queryBox.setBounds(257, 60, 100,25);
        scroll.setBounds(210,125,200,300);

        //give text area a border
        scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK));




        //change fonts
        autocompleteLabel.setFont(Font.decode("Arial"));

        //makes results area uneditable and scrollable
        resultsArea.setEditable(false);

        //Creates a vertical scroll bar in the results area if there are more items than the area can show at once
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);


        //add listeners
        queryBox.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                resultsOutput();
            }


            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                resultsOutput();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                resultsOutput();
            }
        });


        //add everything to the panel
        panel.add(scroll);
        panel.add(queryBox);
        panel.add(autocompleteLabel);
        panel.setBackground(Color.WHITE);
        add(panel);
        panel.setLocation(0,0);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }

    public void resultsOutput(){/**
     * Outputs the 'k' highest weighted matching terms to the query to the result area.
     * In the case of less than k matching terms, all of the matching files are output anyway
     *
     * @author  Sandy Johnstone (aj87)
     */
        resultsArea.setText(null);

        try {
            if (!queryBox.getText().equals("")){
                Term[] matches = autocompleteRun(queryBox.getText(), allTerms);

                if(matches.length < noOfTermsDisplayed) {
                    for (int i = 0; i < matches.length; i++) {
                        resultsArea.append((matches[i].getQuery() +"\n"));
                    }
                }
                else{
                    for(int i = 0; i < noOfTermsDisplayed; i++){
                        resultsArea.append((matches[i].getQuery()+ "\n"));
                    }

                }
            }

        }catch(NegativeArraySizeException e){ //The BinarySearchDeluxe will output -1 if no matches are found. Since we don't want the program to crash, we output 'no matches' to the user instead
            resultsArea.append("No Matches");
        }


    }

    public static Term[] autocompleteRun(String query, Term[] allTerms){/**
     * Calls the autocomplete class to match terms and then sort them.
     * If there are no matches then it outputs 'no matches' to the results area.
     *
     * @author  H. Thompson (ht44)
     * @param   query   The string that the user has input into the GUI
     * @param   allTerms    An array containing all the terms from the file
     *
     * @return          An array of all the matching terms, sorted by reverse weight order
     *
     */

        Autocomplete autocomplete = new Autocomplete(allTerms); //Creates an Autocomplete object where the array of terms is the input file as

        try { //Attempts to find all the matches to the input term

            Term[] matchingTerms = autocomplete.allMatches(query); //Finds the matching terms, sorts them by reverse weight order, and then returns the sorted list
            Arrays.sort(matchingTerms, Term.byReverseWeightOrder());
            return matchingTerms;

        } catch (ArrayIndexOutOfBoundsException e){ //Catches if BinarySearchDeluxe returns -1 in the Autocomplete class, indicating no matches
            return NO_MATCHES_ARRAY;
        }

    }


    public static void main(String[] args){
        if (args.length != 2) { //Tests if the correct number of arguments have been supplied, and if not, exits and displays a message
            System.out.println("Usage: java AutocompleteGUI <input_file> <number_of_matches_displayed>");
            System.exit(0);

        }

        try{ //Tests if corresponding argument is in correct argument and if not changes to default
            noOfTermsDisplayed = Integer.parseInt(args[1]);
            if (noOfTermsDisplayed < 1){throw new IndexOutOfBoundsException();}
        } catch (NumberFormatException e) { //If argument is not an integer
            System.out.println("NumberFormatException: Number of matches displayed is not a valid integer.");
            System.exit(0);
        } catch (IndexOutOfBoundsException e){ //If argument is an integer less than 1
            System.out.println("NumberFormatException: Number of matches displayed must be greater than or equal to 1");
            System.exit(0);
        }

        try (Scanner autoScanner= new Scanner(new File(args[0]))){


        allTerms = new Term[Integer.parseInt(autoScanner.nextLine())]; //Reads the first line of the text file which is the size of the text file and creates an array of that size

        String currentLine = "";

        for (int j = 0; j < allTerms.length; j++) { //Enters all the terms and their weights into an array of Term objects
            currentLine = autoScanner.nextLine();
            currentLine = currentLine.trim();
            String[] splitLine = currentLine.split("\t");
            allTerms[j] = new Term(splitLine[1], Long.parseLong(splitLine[0]));
        }
        } catch (FileNotFoundException e){
            System.out.println("Exception: File Not Found " + e.getMessage());
            System.exit(0);
        }

        Arrays.sort(allTerms); //Uses the Term's comparable interface to sort the Terms alphabetically

        EventQueue.invokeLater(() -> {
            AutocompleteGUI myGUI = new AutocompleteGUI(args, allTerms);
        });

    }

}