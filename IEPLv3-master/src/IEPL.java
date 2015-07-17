import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Title:               IEPL.java
 *
 * Author:              Tristan Rentz (sole author)
 *
 * Date of completion:  21 May 2010
 *
 * This program is the solution to the CSE3ALR (Artificial Intelligence -
 * Logic and Reasoning) Assignment 2010, La Trobe University Computer Science
 * Department.  The Program is an inference engine using propositional logic
 * that uses a simplified command line framework and syntax based on Prolog.
 * It performs five main tasks:
 *
 *  - consultations : for loading files and populating the knowledge base.
 * 				Use the syntax:
 *              	
 *              	consult('<filename>.iepl').
 *
 *
 *  - queries : for questioning the knowledge base, with one or two questions at
 *              a time.  A question is an unbroken string of alphaNumeric
 *              characters.  Syntax:
 *
 *              	query.
 *              
 *                   OR
 *                  
 *                  query1, query2.
 *
 *
 *  - stocktake : lists the entries in the current fact base. Syntax:
 *
 *                  X.
 *
 *
 *	- empty base : resets the knowledge base, ie both the facts and the clauses.
 *				Syntax:
 *
 *					reset.
 *
 *
 *  - kill session : end the current program session.  
 *  			Syntax:
 *
 *                  halt.
 *	
 *
 *	
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
 * @version 2.0
 */
public class IEPL {
	
	/* command strings */
	public static final String PRINT_FACTS = "X";
	public static final String PRINT_RULES = "Y";
	public static final String CREATE_DB = "CONSULT";
	public static final String CLEAR_DB = "RESET";
	public static final String ADD_FACT = "FACT";
	public static final String ADD_RULE = "RULE";
	public static final String HELP = "HELP";
	public static final String QUIT = "HALT";

	/* engine attributes */
    /**
     * Temporary storage for the unprocessed lines read in from a consulted .iepl file.
     */
    private Vector<String> linesFromFile;
    /**
     * The current set of facts in the knowledge base.
     */
    private Vector<String> facts;
    /**
     * The current set of clauses in the knowledge base.
     */
    private Vector<Clause> clauses;
    /**
     * Strict cumulative value printed at the beginning of each line in the program's command prompt. 
     */
    private int lineNumber;
   
    /* query variables */
    /**
     * Represents all sub-queries of a given query.
     */
    private Vector<String> query;
    /**
     * Represents the current state of a derivation's success.
     */
    private boolean proven;
    	
    /**
     * Takes no arguments. Initialises attributes to empty state.
     */
    public IEPL(){
        linesFromFile 	= new Vector<String>(0);
        facts       	= new Vector<String>(0);
        clauses       	= new Vector<Clause>(0);
        lineNumber  	= 0;
        query			= null;
        proven			= false;
    }

    public static void main(String[] args){
        IEPL ie = new IEPL();
        Scanner kb = new Scanner(System.in);
        String input = null;
        boolean run = true;
        prLn("");
    	prLn("**********************************************");
    	prLn("**********   Tristan's Inferenece   **********");
    	prLn("**********      Engine   using      **********");
    	prLn("**********   Propositional  Logic   **********");
    	prLn("**********************************************");
        printMenu();

        // main procedural loop
        while(run){

            // prompt for input...
            print(++ie.lineNumber + "?- ");
            // ... and fetch it, removing any ending period immediately
            input = killDot(kb.nextLine().trim());
            int length = input.length();
            if(length == 0){
            	prLn("Error: no input!");
            	continue;
            }
            
            
            
            // START J2SE 1.6 code - switch version
            /*
            // This version can be expected to yield occasional strange behavior due
            // to the incidence of multiple strings with identical hash-codes.
            //int command = input.split("\\(")[0].toUpperCase().hashCode();
            String command = input.split("\\(")[0].toUpperCase().trim();
            
            // main operations
            switch(command){
            // list from database
            	case 88				: printVector(ie.facts);				break;
            	case 89				: printVector(ie.clauses);				break;
            	
	        // add to database
	            case 2149996		: ie.addToBase(ParamType.FACT, kb);		break;
	            case 1990361455		: ie.addToBase(ParamType.CLAUSE, kb);	break;
	            
	         // create database
	            case 1669498828		:
	            	
	            	if(length>9 && (input.substring(0, 9).equalsIgnoreCase("CONSULT('")) 
	            			&& input.substring(input.length()-2).equals("')"))
	            		ie.consult(extractFilenameFromQuery(input));
	                else	// SYNTAX ERROR!
	                	prLn("Syntax Error! \"" + input + "\"");
	            	break;

	            	// clear database
	            //case 77866287		: ie.reset();							break;
	            case CLEAR_DB		: ie.reset();							break;
		        
	        // view help
	            //case 2213697		: printMenu();							break;
	            case HELP			: printMenu();							break;
	        
	        // quit
	            //case 2209857		: run = false; 	prLn("\nbye...\n");		break;
	            case QUIT	public static final String PRINT_FACTS = "X";
	public static final String PRINT_RULES = "Y";
	public static final String CREATE_DB = "CONSULT";
	public static final String CLEAR_DB = "RESET";
	public static final String ADD_FACT = "FACT";
	public static final String ADD_RULE = "RULE";
	public static final String HELP = "HELP";
	public static final String QUIT = "HALT";
		: run = false; 	prLn("\nbye...\n");		break;

	        // question database
	            default		  : 
	                if(correctQuerySyntax(input))
	                    ie.query(input);
	                else	// SYNTAX ERROR!
	                	prLn("Syntax Error! '" + input + "'");
	            	break;
            }
            */	
            // END J2SE 1.6 code - switch version
            

            
            // START J2SE 1.6 code - if-else version
            /*
            String first = input.split("\\(")[0];
            
            // stocktake
	        if(first.equalsIgnoreCase("X"))
	        	printVector(ie.facts);
	        else if(first.equalsIgnoreCase("Y"))
	        	printVector(ie.clauses);
	        
	        // add to database
	        else if(first.equalsIgnoreCase("FACT"))
	        	ie.addToBase(ParamType.FACT, kb);
	        else if(first.equalsIgnoreCase("CLAUSE"))
	        	ie.addToBase(ParamType.CLAUSE, kb);
	            
	        // clear database
	        else if(first.equalsIgnoreCase("RESET"))
	        	ie.reset();
	        
	        // view help
	        else if(first.equalsIgnoreCase("HELP"))
	            printMenu();
	        
	        // quit
	        else if(first.equalsIgnoreCase("HALT")){
	        	run = false; 	
	        	prLn("\nbye...\n");
	        }

	        // create database
	        else if(first.equalsIgnoreCase("CONSULT")){
            	if(length>9 && (input.substring(0, 9).equalsIgnoreCase("consult('")) 
            			&& input.substring(input.length()-2).equals("')"))
            		ie.consult(extractFilenameFromQuery(input));
                else	// SYNTAX ERROR!
                	prLn("Syntax Error! '" + input + "'");
	        }

	        // question database
	        else{
	        	if(correctQuerySyntax(input))
                    ie.query(input);
                else	// SYNTAX ERROR!
                	prLn("Syntax Error! '" + input + "'");
            }
            */
	        // END J2SE 1.6 code - if-else version
            
            

            // START J2SE 1.7 code
            // main operations
            switch(input.split("(")[0]){
            // list from database
	            case PRINT_FACTS	: printVector(ie.facts);				break;
	            case PRINT_RULES	: printVector(ie.clauses);				break;
	        
	        // add to database
	            case ADD_FACT		: ie.addToBase(ParamType.FACT, kb);		break;
	            case ADD_RULE		: ie.addToBase(ParamType.CLAUSE, kb);	break;
	            
	        // clear database
	            case CLEAR_DB		: ie.reset();							break;
	        
	        // view help
	            case HELP			: printMenu();							break;
	        
	        // quit
	            case QUIT			: run = false; 	prLn("\nbye...\n");		break;

	        // consult file
	            case CREATE_DB	:  
	            	if(length>9 && (input.substring(0, 9).equalsIgnoreCase("consult('")) 
	            			&& input.substring(input.length()-2).equals("')"))
	            		ie.consult(extractFilenameFromQuery(input));
	                else	// SYNTAX ERROR!
	                	prLn("Syntax Error! '" + input + "'");
	            	break;

	        // question database
	            default		  : 
	                if(correctQuerySyntax(input))
	                    ie.query(input);
	                else	// SYNTAX ERROR!
	                	prLn("Syntax Error! '" + input + "'");
	            	break;
            }	
            // END J2SE 1.7 code
            
            
            input = null;
        }   // END MAIN PROGRAM LOOP
    }
 
    private static void printMenu(){
    	prLn("");
    	prLn(" ************  Basic  Commands  *************");
    	prLn("");
    	prLn("  \"X\"      - display fact base.");
    	prLn("  \"Y\"      - display rule base.");
    	prLn("  \"RESET\"  - clear database.");
    	prLn("  \"FACT\"   - add a fact to the database.");
    	prLn("  \"CLAUSE\" - add a rule to the database.");
    	prLn("  \"HALT\"   - quit the program.");
    	prLn("  \"HELP\"   - view this menu.");
    	prLn("");
    	prLn("");
    	prLn(" ********  Queries & Consultations  *********");
    	prLn("");
    	prLn("  1. To consult a file, type:");
    	prLn("       consult('file.iepl').");
    	prLn("     where \"file.iepl\" is the name");
    	prLn("     of a text file in IEPL format.");
    	prLn("");
    	prLn("  2. To execute a query, type a question");
    	prLn("     at the command line in fact format");
    	prLn("     (no spaces or question mark).");
    	prLn("");
    	prLn("**********************************************");
    	prLn("");
    }

    private void consult(String filename) {
		// BEGIN CONSULTATION BLOCK
        try{
            // extract all lines from file
            Scanner file = new Scanner(new FileInputStream(filename));
            while(file.hasNextLine()){
                String line = file.nextLine().trim();
                if(line.length()>0)
                    this.linesFromFile.add(killDot(line));
            }

            // loop through extraction to add to facts or rules
            for(int i=0; i<this.linesFromFile.size(); ++i){
                if(isRule(this.linesFromFile.get(i)))
                    this.addToClauses(this.linesFromFile.get(i));
                else
                    this.addToFacts(this.linesFromFile.get(i));
            }
            prLn("Yes");
        }
        // consultation exceptions
        catch(FileNotFoundException e){
            prLn("No");
        }
	}

    private void query(String input){
    	this.initDerivationSets(input);
    	// set and start the timer
    	long start, finish;
    	start = System.nanoTime();
        this.proven = this.initDerive();
        finish = System.nanoTime();
        double time = (finish - start)/(double)1000000;
        
        // print result
        prLn(this.proven ? "Yes" : "No");
        prLn(time + "ms");
        this.resetDerivationSets();
    	
    }
    
	private static String extractFilenameFromQuery(String input) {
    	// extract file name from input
        return input.substring(9, (input.length()-2));
	}

	/* PRIMARY METHODS */
    
    /**
     * Initialises all variables used in proof derivation.
     * @param input Initial query String from the user.
     */
	private void initDerivationSets(String input){
    	this.query = setSubQueries(input);
    	this.proven = false;
	}
    /**
     * Initialises the main recursive query method.
     * @return TRUE if the initial conditions are proven true.
     */
    private boolean initDerive(){
    	boolean[] truth = new boolean[this.query.size()];
    	// loop to seek satisfaction for each individual condition
    	for(int subQuery=0; subQuery<this.query.size(); ++subQuery){
        	// initialise temp sets
        	Vector<String> usedConditions = new Vector<String>();
        	Vector<Clause> usedClauses = new Vector<Clause>();
        	Vector<Clause> availableClauses = new Vector<Clause>();
        	for(Clause c : this.clauses)
        		availableClauses.add(c);
        	// execute subquery
        	truth[subQuery] = derive(availableClauses, usedClauses, 
        					  usedConditions, this.query.get(subQuery));
    	}
    	for(boolean t : truth)
    		if(t == false) return false;
    	return true;
    }
    /**
     * The main recursive query method. Takes a condition and seeks firstly a match in the fact base - in which
     * case TRUE is returned - or else seeks a match in the head of a clause, which then acts as a node. If the
     * latter takes place, the method is called once for each condition in the matching clause. If no match is 
     * found, FALSE is returned. When this occurs, backtracking is attempted; for each subsequent call the method
     * seeks any other matching clause not yet checked against the condition passed into the call that acts as 
     * the current leaf node. 
     * @param condition The current condition that seeks a clause to match, which then acts as a node.
     * @return TRUE if a proof has been derived, FALSE if no proof is found.
     */
    private boolean derive(Vector<Clause> availableClauses, Vector<Clause> usedClauses,
    					   Vector<String> usedHeads, String query){
    	// search for condition in fact base & return true if found
        if(inFactBase(query))
        	return true;
        // otherwise, loop through available clauses searching for a head that matches 	
    	for(int index = 0; index < availableClauses.size(); ++index){
	    	Clause testClause = availableClauses.get(index);
	    	// if current clause has matching head 
	    	if(testClause.hasConclusion(query)){
	    		// if query is in usedHeads, report recursive and continue the search for a fitting clause
	    		if(vectorContainsString(usedHeads, query)){
	    			prLn("Infinite loop!");
	    			// TODO decide whether to implement this with 'break' or 'continue'
	    			break;
	    		}
    			// otherwise, proceed...
	    		else{
	    			// add the query to the usedHeads set
	    			usedHeads.add(query);
		    		boolean[] proven = boolArray((int)testClause.getNumConds(), false);
	    			int clauseConditionIndex = 0;
		    		Vector<String> conditions = new Vector<String>(testClause.getConds());
		    		// loop through conditions in testClause
		    		for(String cond : conditions){
		    			// recurse, deriving truth of current clause condition
		    			proven[clauseConditionIndex] = derive(availableClauses, usedClauses, usedHeads, cond);
		    			// if proven, move testClause from availableClauses to usedClauses
		    			if(proven[clauseConditionIndex])
		    	    		++clauseConditionIndex;
		    		}
		    		if(boolArrayTest(proven, true))
	    				return true;
		    		else
		    			usedHeads.remove(query);
				}
	   		}
    	}	// END FOR loop thru availableClauses
    	return false;
    }
    /**
     * Facilitates the addition of a fact or clause (as specified) 
     * to the knowledge base by the user.
     * @param type The parameter type (FACT or CLAUSE) required as input.
     * @param kb A Scanner object with which to get user input.
     */
    private void addToBase(ParamType type, Scanner kb){
    	String input;
    	while(true){
	    	print("> ");
	    	input = killDot(kb.nextLine());
	    	if((type == ParamType.FACT && correctParamSyntax(ParamType.FACT, input)) 
	    			|| (type == ParamType.CLAUSE && correctParamSyntax(ParamType.CLAUSE, input)))
	    		break;
	    	else
	    		prLn("Syntax error: '" + input + "'");
	    }
	    prLn("Yes");
	    switch(type){
	    case FACT: 		this.addToFacts(input);		break;
	    case CLAUSE:	this.addToClauses(input);	break;
	    }
    }
    /**
     * Takes a string and does a regex check for fact syntax.
     * @param input The string to check.
     * @return TRUE if 'input' is valid fact syntax.
     */
    private static boolean correctParamSyntax(ParamType type, String input){
    	Pattern p = null;
    	switch(type){
	    	case FACT: 		p = Pattern.compile("[A-Za-z0-9]+");	
	    		break;
	    	case CLAUSE: 	p = Pattern.compile("[A-Za-z]+[ ]?:-[ ]?[A-Za-z]+(,[ ]?[A-Za-z])*");	
	    		break;
	    }
    	Matcher match = p.matcher(input);
    	return match.matches();
    }
    /**
     * Takes a string and does a regex check for query syntax.
     * @param input The string to check.
     * @return TRUE if 'input' is valid query syntax.
     */
    private static boolean correctQuerySyntax(String input){
    	String[] inputAsArray = input.split(",");
    	for(String s : inputAsArray)
    		if(!correctParamSyntax(ParamType.FACT, s.trim()))
    			return false;
    	return true;
    }
    /**
     * Resets all derivation variables.
     */
	private void resetDerivationSets(){
    	this.query 			= null;
		this.proven 		= false;
	}
	/**
     * Takes a string and returns the string stripped of the last character/index
     * if that character is a "." or ",".
     *
     * @param input the unmodified string
     * @return the string without last "." or ","
     */
	public static String killDot(String input){
		// if the last char is a dot or a comma...
        return (input.charAt(input.length()-1) == '.' || input.charAt(input.length()-1) == ',') ?
        	// return the recursively checked corrected string
            killDot(input.substring(0, input.length()-1)) : 
            // else return the input in it's current incarnation
        	input;
    }
    /**
	 * Takes a string and adds it to the clause set. 
	 * @param item The clause to add, in String form.
	 * @return TRUE if added, FALSE if such a clause already exists in the knowledge base. 
	 */
	private boolean addToClauses(String item){
	    for(int cl=0; cl<this.clauses.size(); ++cl)
	    	if(this.clauses.get(cl).match(item))
	        	return false;
	    this.clauses.add(new Clause(item));
	    return true;
	}
	/**
     * Adds a fact to the fact set in the knowledge base, if it hasn't
     * been added already.
     * @param item The fact to add.
     * @return TRUE if the item was added, FALSE if a matching item already exists in the fact set.
     */
    private boolean addToFacts(String item){
        for(int i=0; i<this.facts.size(); ++i)
            if(facts.get(i).equalsIgnoreCase(item))
            	return false;
        facts.add(item);
        return true;
    }
    /**
     * Resets all knowledge base variables.
     */
    private void reset(){
    	this.facts = null;
    	this.clauses = null;
    	this.facts = new Vector<String>();
    	this.clauses = new Vector<Clause>();
    	this.resetDerivationSets();
    }
    /**
     * Takes an unprocessed string in as a parameter, and checks 
     * whether or not the string is a rule.
     *
     * @param input The string to test.
     * @return TRUE if input is a rule, FALSE otherwise (ie input is a fact).
     */
    private static boolean isRule(String input){
        return input.contains(":-");
    }
    /**
     * Takes a string (fact) and checks for it presence in the primary fact base.
     *
     * @param fact The fact to test.
     * @return TRUE if the fact is already in the base, FALSE otherwise.
     */
    private boolean inFactBase(String fact){
        for(int i=0; i<this.facts.size(); ++i)
            if(facts.get(i).equals(fact))
                return true;
        return false;
    }
    /**
     * Takes a string vector and a string, testing the vector for 
     * the presence of the string.
     *
     * @param fact The string to match.
     * @return TRUE if the string is present in the vector, FALSE otherwise.
     */
    private static boolean vectorContainsString(Vector<String> set, String str){
        for(int i=0; i<set.size(); ++i)
            if(set.get(i).equalsIgnoreCase(str))
                return true;
        return false;
    }
    /**
     * Creates a boolean array given an array size and initialises
     * each element based on a given collective initial truth value.
     * @param size
     * @param init
     * @return
     */
    @SuppressWarnings("unused")
    private static boolean[] boolArray(int size, boolean init){
    	boolean[] output = new boolean[size];
    	for(boolean element : output){
    		element = init;
    	}
    	return output;
    }
    /**
     * Given a truth value and a boolean array, this method returns FALSE if any 
     * element in the array is not equal to the given truth value, TRUE otherwise.
     * 
     * @param input The boolean array to test.
     * @param truth The truth value to test against.
     * @return Whether or not all the array's values are compliant.
     */
    private static boolean boolArrayTest(boolean[] input, boolean truth){
    	for(boolean val : input)
    		if(val != truth) return false;
    	return true;
    }
    /**
     * Takes user input as a query, and breaks the query down into sub-queries.
     * @param input The user input.
     * @return An array of Strings
     */
    private static Vector<String> setSubQueries(String input){
    	Vector<String> queries = new Vector<String>();
        String[] queriesAsArray = input.split(",");
        assert queries.size()>0;
        for(String q : queriesAsArray)
        	queries.add(q);
        return queries;
    }
    /**
     * Abbreviates System.out.print(string).
     * @param output the content to print
     */
    private static void print(String output){
        System.out.print(output);
    }
    /**
     * Abbreviates System.out.println(string).
     * @param output the content to print
     */
    private static void prLn(String output){
        System.out.println(output);
    }
    
    /* RECYCLING */


    /**
     * Takes a specified vector and executes it's , iteratively.
     * @param set
     */
    public static void printVector(Vector<?> set){
    	for(Object item : set)
    		prLn(item.toString());
    }
}
