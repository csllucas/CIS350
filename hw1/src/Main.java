import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.sql.Timestamp;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws IOException {
    	if (args.length != 2){
    		System.err.println("usage: java Main <input file> <log file>");
    	}
    	String inputF = args[0];
    	String logF = args[1];
    	Object[] inputs = input(inputF);
    	ArrayList<ArrayList<String>> persons = (ArrayList<ArrayList<String>>)inputs[0];
    	ArrayList<ArrayList<String>> pictures = (ArrayList<ArrayList<String>>)inputs[1];
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String userIn = null;
		System.out.println("Welcome to the Oscars database!\n");
    	boolean ifQuit = false;
    	while (! ifQuit){
    		instruc();
    		userIn = br.readLine();
    		logging(userIn, logF);
    		if (userIn.equals("Q")||userIn.equals("q")){
    			System.out.println("good bye");
    			ifQuit = true;
    		}
    		else if(userIn.equals("1")){
    			picWinner(pictures, logF);
    		}else if(userIn.equals("2")){
    			picNom(pictures, logF);
    		}else if(userIn.equals("3")){
    			actNom(persons, logF);
    		}else{
    			System.out.println("That is not a valid selection.\n");
    		}
    	}
    }
    
    public static void logging(String userIn, String logF){
   	 	java.util.Date date= new java.util.Date();
    	try {
    	    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logF, true)));
    	    out.println(new Timestamp(date.getTime()));
    	    out.println(userIn);
    	    out.close();
    	} catch (IOException e) {
    		System.err.println("openning log file failed");
    	}
    }
    
    public static void instruc(){
    	System.out.println("Please make your selection:\n"+
    		"1: Search for best picture award winner by year\n"+
    		"2: Search for best picture award nominees by year\n"+
    		"3: Search for actor/actress nominations by name\n"+
    		"Q: Quit");
    }
    
    public static void picWinner(ArrayList<ArrayList<String>> pictures, String logF) throws IOException {
    	boolean valid = false;
        while (!valid){
        	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        	System.out.print("Please enter the year: ");
        	String userIn = br.readLine();	
        	logging(userIn, logF);
        	Iterator<ArrayList<String>> iter = pictures.iterator();
        	while (iter.hasNext()){
        		ArrayList<String> curr = iter.next();
        		if ((curr.get(0).equals(userIn)) && (curr.get(3).equals("YES"))){
        			valid = true;
        			String result= String.format("The winner in %s was %s\n", userIn, curr.get(2));
        			System.out.println(result);
        		}
        	}
        	if(!valid){
        		System.out.println("invalid input\n");
        	}
        }
    }

    public static void picNom(ArrayList<ArrayList<String>> pictures, String logF) throws IOException {
    	boolean valid = false;
        while (!valid){
        	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        	System.out.print("Please enter the year: ");
        	String userIn = br.readLine();
        	logging(userIn, logF);
        	Iterator<ArrayList<String>> iter = pictures.iterator();
        	while (iter.hasNext()){
        		ArrayList<String> curr = iter.next();
        		if (curr.get(0).equals(userIn)) {
        			valid = true;
        			System.out.println(curr.get(2));
        		}
        	}
        	if(!valid){
        		System.out.println("invalid input\n");
        	}else{
            	System.out.println("Above are the nominees in "+userIn+ "\n");
        	}
        }
    }

    public static void actNom(ArrayList<ArrayList<String>> persons, String logF) throws IOException {
        boolean valid = false;
        while (!valid){
        	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        	System.out.print("Please enter all or part of the person's name: ");
        	String userIn = br.readLine();
        	logging(userIn, logF);
        	Iterator<ArrayList<String>> iter = persons.iterator();
        	while (iter.hasNext()){
        		ArrayList<String> curr = iter.next();
        		if(Pattern.compile(Pattern.quote(userIn), Pattern.CASE_INSENSITIVE).matcher(curr.get(2)).find()){
        			valid = true;
        			if (curr.get(3).equals("YES")){
        				String result= String.format("%s won %s in %s", curr.get(2),curr.get(1),curr.get(0));
        				System.out.println(result);
        			}else{
        				String result= String.format("%s was nominated for %s in %s", curr.get(2),curr.get(1),curr.get(0));
        				System.out.println(result);
        			}
        		}
        	}
        	if (!valid) {
        		System.out.println("No results found for "+userIn+"\n");
        	}else{
                System.out.println(" ");
        	}
        }
    }
    
    public static Object[] input(String inputF) throws IOException{
    	BufferedReader inputStream = null;
    	ArrayList<ArrayList<String>> persons = new ArrayList<ArrayList<String>>();
    	ArrayList<ArrayList<String>> pictures = new ArrayList<ArrayList<String>>();
    	Object[] toReturn = {persons, pictures};	//casting   
    	try {
    		inputStream = new BufferedReader(new FileReader(inputF));
    	} catch (IOException e) {
    		System.err.println("openning input file failed");
    	}
    	String l;
    	while ((l = inputStream.readLine()) != null) {
    		if (l.indexOf("Actor --") != -1){
    			ArrayList<String> value = new ArrayList<String>();
    			String delims = "[,]";
    			String[] tokens = l.split(delims);
    			value.add(tokens[0]);
    			value.add(tokens[1]);
    			value.add(tokens[2]);
    			value.add(tokens[3]);
    			persons.add(value);
    		}
    		if (l.indexOf("Actress --") != -1){
    			ArrayList<String> value = new ArrayList<String>();
    			String delims = "[,]";
    			String[] tokens = l.split(delims);
    			value.add(tokens[0]);
    			value.add(tokens[1]);
    			value.add(tokens[2]);
    			value.add(tokens[3]);
    			persons.add(value);
    		}
    		if (l.indexOf("Best Picture") != -1){
    			ArrayList<String> value = new ArrayList<String>();
    			String delims = "[,]";
    			String[] tokens = l.split(delims);
    			String year = tokens[0].substring(0,4);
    			value.add(year);
    			value.add(tokens[1]);
    			value.add(tokens[2]);
    			value.add(tokens[3]);
    			pictures.add(value);                	
    		}
    	}
    	if (inputStream != null) {
    		inputStream.close();
    	}
    	return toReturn;
    }
}



