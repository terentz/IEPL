import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

/**
 * Title:               IEPL.java
 * 
 * Author:              Tristan Rentz (sole author)
 *
 * Date of completion:  21 May 2010
 *
 * This program is the solution to the CSE3ALR (Artificial Intelligence -
 * Logic and Reasoning) Assignment 2010, La Trobe University Computer Science
 * Department.  The Program is an inference engine using proposititional logic
 * that uses a simplified command line framework and syntax based on Prolog.
 * It takes four types of input:
 *
 *  - consultations : for loading files and populating the knowledge base. 
 *                      Use the syntax:
 *                      consult('filename.iepl').
 * 
 *  - queries : for questioning the knowledge base, with one or two questions at
 *                      a time.  A question is an unbroken string of alphaNumeric
 *                      characters.   Queries take the form:
 *                      query.
 *                      OR
 *                      query1, query2.
 *
 *  - stocktake : lists the entries in the current fact base.  Syntax:
 *                      X.
 *
 *  - kill session : end the current program session.  Syntax:
 *                      halt.
 *
 * The program assumes strictly correct input, and responds to consultations and
 * queries with only 'Yes' or 'No', ie all exceptions come under 'No'.
 * With each consultation, the input is divided into rules and facts, then all
 * the current (including new) rules are checked against current (including new)
 * facts.  Whenever a new fact is forged, the checking loop re-runs, until no new
 * facts are derived.  It then checks queries against the fact base.
 *
 *
 * @author Tristan Rentz
 */
public class IEPL {

    public static void main(String[] args){
        prLn("****  Tristan's IEPL  ****\n\n");
        IEPL ie = new IEPL();
        Scanner kb = new Scanner(System.in);
        String input = null;

        // main procedural loop
        while(true){

            // prompt and get input
            prLn(++ie.lineNumber + "?- ");
            input = killDot(kb.nextLine().trim());
            int length = input.length();

            // test for print facts
            if(input.charAt(0) == 'X')
                printFacts(ie.facts);

            // test for kill
            else if(length == 4 && input.substring(0,4).equalsIgnoreCase("halt"))
                break;
            
            // test for consultation
            else if(length>7 && (input.substring(0, 7).equals("consult"))){

                // BEGIN CONSULTATION BLOCK
                try{

                    // extract file name from input
                    String filename = input.substring(9, (length-2));

                    // extract all lines from file
                    Scanner file = new Scanner(new FileInputStream(filename));
                    while(file.hasNextLine()){
                        String line = killDot(file.nextLine().trim());
                        if(line.length()>0)
                            ie.fromFile.add(line);
                    }

                    // loop through extraction to assign to facts or rules
                    for(int i=0; i<ie.fromFile.size(); ++i){
                        if(isRule(ie.fromFile.get(i)))
                            addToBase(ie.rules, ie.fromFile.get(i));
                        else
                            addToBase(ie.facts, ie.fromFile.get(i));
                    }
                    prLn("Yes");

                    // test
                    
                    // loop thru rules until no new facts to derive
                    boolean newFact = true;
                    while(newFact){
                        // reset flag
                        newFact = false;
                        // loop thru rules and facts to extrapolate new facts
                        for(int i=0; i<ie.rules.size(); ++i){
                            int ruleSize = howManyPremises(ie.rules.get(i));
                            // loop thru each fact to find match for premise 1
                            for(int j=0; j<ie.facts.size(); ++j){
                                // if rule has only one premise
                                if(ruleSize == 1){
                                    if(hasOneFact(ie.rules.get(i), ie.facts.get(j))){
                                        newFact = addToBase(ie.facts, getHead(ie.rules.get(i)));
                                    }
                                }
                                else if(ruleSize == 2){
                                    // loop thru each fact to find match for premise 2
                                    for(int k=0; k<ie.facts.size(); ++k){
                                        // skip if first fact equals second fact
                                        if(j==k) continue;
                                        // else test rule with facts and add head if true
                                        if(hasBothFacts(ie.rules.get(i), ie.facts.get(j), ie.facts.get(k))){
                                            newFact = addToBase(ie.facts, getHead(ie.rules.get(i)));
                                            if(newFact) break;
                                        }
                                    }
                                }
                                if(newFact) break;
                            }
                            if(newFact) break;
                        }
                    }
                }
                // consultation exceptions
                catch(FileNotFoundException e){
                    prLn("No");
                }
            }
            // END OF CONSULTATION BLOCK

            // input is query
            else{

                // BEGIN QUERY BLOCK

                // remove "." from query
                int inputSize=input.length();

                // test for number of queries
                int numQueries=1;
                inputSize=input.length();
                for(int i=0; i<inputSize; ++i){
                    if(input.charAt(i) == ',')
                        ++numQueries;
                }

                // create sub query var's
                String[] query = setSubQueries(numQueries, input);

                // set initial truth value
                boolean queryIsTrue = true;

                // test for falseness
                for(int i=0; i<numQueries; ++i){
                    if(!isFact(ie.facts, query[i])){
                        queryIsTrue = false;
                        break;
                    }
                }

                // print result
                prLn(queryIsTrue ? "Yes" : "No");

            }   // END QUERY BLOCK
        }   // END MAIN PROGRAM LOOP
    }

    /* engine attributes */
    private Vector<String> fromFile;
    private Vector<String> facts;
    private Vector<String> rules;
    private int lineNumber;

    /* constructor */
    public IEPL(){
        fromFile    = new Vector<String>(0);
        facts       = new Vector<String>(0);
        rules       = new Vector<String>(0);
        lineNumber  = 0;
    }


    /* static methods for Main */

    /**
     * Takes a string and returns the string stripped of the last character/index
     * if that character is a "." or ",".
     *
     * @param input the unmodified string
     * @return the string without last "." or ","
     */
    public static String killDot(String input){
        int length = input.length();
        if(input.charAt(length-1) == '.' || input.charAt(length-1) == ',')
            return input.substring(0, length-1);
        else return input;
    }

    /**
     * This method adds an item to a component of the knowledge base, if it hasn't
     * been added already.
     *
     * @param base the KB component to add to
     * @param item the element to add
     * @return whether or not the item was added
     */
    public static boolean addToBase(Vector<String> base, String item){
        boolean already = false;
        for(int i=0; i<base.size(); ++i){
            if(base.get(i).equals(item)){
                already = true;
                break;
            }
        }
        if(already)
            return false;
        else{
            base.add(item);
            return true;
        }
    }

    /**
     * This method takes a string in as a parameter, and checks whether or not
     * the string is a rule or not.
     *
     * @param input the string to test
     * @return true if input is a rule, false otherwise
     */
    public static boolean isRule(String input){
        int length = input.length();
        for(int i=0; i<(length-1); ++i){
            if(input.substring(i, (i+2)).equals(":-"))
                    return true;
        }
        return false;
    }

    /**
     * This method takes a vector of strings (facts) and a lone string (fact)
     * to check whether the string has a literal match with any elements in the
     * string vector.
     *
     * @param facts the fact base
     * @param fact the fact to test
     * @return true if the fact is already in the base, false otherwise
     */
    public static boolean isFact(Vector<String> facts, String fact){
        for(int i=0; i<facts.size(); ++i){
            if(facts.get(i).equals(fact))
                return true;
        }
        return false;
    }

    /**
     * This method copies the head of a rule to a separate string instance.
     *
     * @param rule the rule from which to extract
     * @return the extracted head of the rule
     */
    public static String getHead(String rule){
        int firstSpc = 0;
        int length = rule.length();

        for(int i=0; i<length; ++i){
            if(rule.charAt(i) == ' '){
                firstSpc = i;
                break;
            }
        }

        return rule.substring(0, firstSpc);
    }

    /**
     * This method counts the number of premises exist in a given rule.
     *
     * @param rule the rule to count
     * @return the number of premises
     */
    public static int howManyPremises(String rule){
        int spaces=0;
        for(int i=0; i<rule.length(); ++i){
            if(rule.charAt(i) == ' ') ++spaces;
        }
        return (spaces-1);
    }

    /**
     * This method is one of the key methods in the program.  It takes a rule and
     * two facts as input, and checks to see if the facts coexist in the tail of
     * the rule.
     *
     * @param rule the rule to check
     * @param fact1 the first of two facts to check
     * @param fact2 the second of two facts to check
     * @return true if both facts coexist in the tail of the rule
     */
    public static boolean hasBothFacts(String rule, String fact1, String fact2){
        int[] space = new int[3];
        int length = rule.length();
        int spcInd = 0;
        String premise1=null, premise2=null;

        // find space locations
        for(int i=0; i<length; ++i){
            if(rule.charAt(i) == ' '){
                space[spcInd++] = i;
                if(spcInd == 4) break;
            }
        }


        // separate premises from rule
        premise1 = rule.substring(space[1]+1, space[2]-1);
        premise2 = rule.substring(space[2]+1);

        // test the rule for truthful head
        if((premise1.equals(fact1) && premise2.equals(fact2)) ||
                (premise1.equals(fact2) && premise2.equals(fact1)))
            return true;

        return false;
    }

    /**
     * This method is a key method.  It takes a rule with only one premise and
     * a lone fact as input checks that rule for the presence of the fact in
     * the rule's tail.
     *
     * @param rule the rule to check
     * @param fact the fact to check
     * @return true if the fact is equal to the rule's premise
     */
    public static boolean hasOneFact(String rule, String fact){
        int[] space = new int[2];
        int length = rule.length();
        int spcInd = 0;
        String premise=null;

        // find space locations
        for(int i=0; i<length; ++i){
            if(rule.charAt(i) == ' '){
                space[spcInd++] = i;
                if(spcInd == 3) break;
            }
        }

        // separate premise from rule
        premise = killDot(rule.substring(space[1]+1));

        // test the rule for truthful head
        if(premise.equals(fact))
            return true;

        return false;
    }

    /**
     * This method separates queries given in one query line.  It takes the
     * keyboard input, and the number of queries present in the line and returns
     * the separated queries as an appropriately sized string array.
     *
     * @param numQueries the number of queries to extract
     * @param input the user's keyboard input
     * @return an array of separate individual queries
     */
    public static String[] setSubQueries(int numQueries, String input){
        String[] query = new String[numQueries];
        int[] space = new int[numQueries-1];
        int length = input.length();
        int spcInd = 0;

        // if only one query, return straight away
        if(numQueries==1){
            query[0] = killDot(input);
            return query;
        }

        // find space locations
        for(int i=0; i<length; ++i){
            if(input.charAt(i) == ' '){
                space[spcInd++] = i;
                if(spcInd == numQueries) break;
            }
        }

        // loop to assign each sub query
        for(int i=0; i<numQueries; ++i){
            // case for first sub query
            if(i==0)
                query[i] = killDot(input.substring(0, space[i]));
            // case for last sub query
            else if(i==numQueries-1)
                query[i] = killDot(input.substring(space[i-1] +1));
            // case for all others ie more than two queries
            else
                query[i] = killDot(input.substring(space[i]+1, space[i+1]));
        }

        return query;
    }

    /**
     * This method takes the system's current fact base as input and prints the
     * facts out separated by a ";".
     *
     * @param facts the current fact base
     */
    public static void printFacts(Vector<String> facts){
        print("X = [");
        for(int i=0; i<facts.size(); ++i){
            print(facts.get(i));
            if(i!=facts.size()-1) print("; ");
        }
        print("]\n\n");
    }

    /**
     * This method was used for testing purposes.  It takes in a knowledge base
     * component and an int, and prints the component's elements on separate
     * lines, indicating on the first line which component is being printed.
     *
     * @param baseNum a flag indicating which component is being printed
     * @param base the component to print
     */
    public static void printKB(int baseNum, Vector<String> base){
        switch(baseNum){
            case 1: prLn("KB: facts"); break;
            case 2: prLn("KB: rules"); break;
        }
        for(int i=0; i<base.size(); ++i){
            prLn(base.get(i));
        }
    }

    /**
     * This method abbreviates System.out.print(string).
     *
     * @param output the content to print
     */
    public static void print(String output){
        System.out.print(output);
    }

    /**
     * This method abbreviates System.out.println(string).
     *
     * @param output the content to print
     */
    public static void prLn(String output){
        System.out.println(output);
    }
}














