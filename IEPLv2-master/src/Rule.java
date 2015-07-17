
import java.util.Vector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tristan
 */

public class Rule {

    private String head;
    private Vector<String> prems;
    private short numPrems;

    /**
     * @param input the rule in correct syntactic form as a string.
     */
    public Rule(String input){
        this.setRule(input);
        this.numPrems = (short)this.prems.size();
    }
    
    /**
     * Treats the input string as an inference rule, isolating the head of the
     * rule, then assigning the head of the rule to this.head.
     * 
     * @param input the rule being processed
     */
    private void setRule(String input){
        boolean firstDone = false;
        int length = input.length();

        for(int i=0; i<length; ++i){
            if(input.charAt(i) == ' '){ 
                if(!firstDone){ // get the head
                    this.head = input.substring(0, i);
                    firstDone = true;
                    input = input.substring(i+4);
                    i=-1;
                }
                else{           // get a premise
                    this.prems.add((killDot(input.substring(0, i)).trim()));
                    input = input.substring(i).trim();
                    i=-1;
                }
            } // END IF char is a space
        }
    }

    /**
     * Returns 'this' rules head.
     * @return
     */
    public String getHead(){
        return this.head;
    }
   
    /**
     * Returns the rule's premises as a string vector
     * @return a string vector containing the premises
     */
    public Vector<String> getPrems(){
        return this.prems;
    }

    /**
     * Takes a string of input and returns it minus the last character if the
     * last character is a comma or period.
     *
     * @param input The string to process
     * @return input without its trailing ',' or 'period'
     */
    public String killDot(String input){
        int length = input.length();
        if(input.charAt(length-1) == '.' || input.charAt(length-1) == ',')
            return input.substring(0, length-1);
        else return input;
    }


}
