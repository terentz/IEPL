
import java.util.Vector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This class separates and holds the head and conditions contained 
 * in a rule as defined in the CSE3ALR assignment description.
 * 
 * @author Tristan Rentz
 * @version 1.2
 */

public class Clause {

    private String conclusion;
    private Vector<String> conditions;

    /**
     * @param input the rule in correct syntactic form as a string.
     */
    public Clause(String input){
        this.setClause(input);
    }
    /**
     * Takes a string and checks if it exists in the clause as a condition.
     * @param condition The condition to check.
     * @return TRUE if condition exists in the clause, FALSE otherwise.
     */
    public boolean hasCondition(String condition){
    	for(String c : conditions)
    		if(c.equalsIgnoreCase(condition))
    			return true;
    	return false;
    }
    
    /**
     * Takes a string and checks if it matches this clauses' consequence.
     * @param head The consequence to test this clause for the presence of.
     * @return TRUE if this clause contains as its consequence the test consequence.
     */
    public boolean hasConclusion(String head){
    	return this.conclusion.equals(head);
    }
    /**
     * Takes a string, converts it to a clause, then checks to see if it matches this clause.
     * @param candidate The string to test.
     * @return TRUE if it matches, FALSE otherwise
     */
    @SuppressWarnings("unused")
	public boolean match(String candidate){
    	Clause temp = new Clause(candidate);
    	boolean[] matches = new boolean[this.conditions.size() + 1];
    	for(boolean m : matches)
    		m = false;
    	for(int co=0; co<conditions.size(); ++co)
    		for(int tempC=0; tempC<temp.conditions.size(); ++tempC)
    			if(this.hasCondition(temp.getConds().get(tempC)))
    				matches[co] = true;
    	if(this.hasConclusion(temp.getConclusion()))
    		matches[matches.length-1] = true;
    	for(boolean m : matches)
    		if(!m) return false;
    	return true;
    }

    public String getConclusion(){ return this.conclusion; }
   
    public Vector<String> getConds(){ return this.conditions; }

    public int getNumConds(){ return this.conditions.size(); }
    
    /**
     * Treats the input string as an inference rule, isolating the head of the
     * rule, then assigning the head of the rule to this.head.
     * 
     * @param input the rule being processed
     */
    private void setClause(String input){
        String[] sections = input.split(":-");
    	// assign the head
        this.conclusion = sections[0].trim();
    	// separate and format the conditions
        String[] condArr = sections[1].split(",");
        this.conditions = new Vector<String>();
        for(String cond : condArr)
        	this.conditions.add(IEPL.killDot(cond.trim()));        
    }
    
    public String toString(){
    	String output = this.conclusion;
    	output += " :-";
    	for(String condition : this.conditions)
    		output += (" " + condition);
    	return output;
    }

}
