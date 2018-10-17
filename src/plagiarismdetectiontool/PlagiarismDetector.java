package plagiarismdetectiontool;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This method does the main operations: a) Replacing synonyms with the first word in the respective list
 * b) Creating a tuple map for checking duplicate tuples between the two input Strings
 */
public class PlagiarismDetector {
    private final String WORD_SEPRATOR_PATTERN = "[^a-zA-Z0-9]";
    
    private static PlagiarismDetector instance = null;
    
    private HashMap<String, String> synonymMap = new HashMap<String, String>();
    private HashMap<String, Integer> tupleMap = new HashMap<String, Integer>();
    private String input1;
    private String input2;
    private ArrayList<String> synonym;
    private int tupleSize;
    
    protected PlagiarismDetector() {
    }
    
    public static PlagiarismDetector getInstance() {
        if(instance == null) {
            instance = new PlagiarismDetector();
        }
        return instance;
    }
    
    /**
     * This method analyzes plagiarism based on N-tuple mechanism
     * @param syn is the synonym file path
     * @param file1 is the input file 1 path
     * @param file2 is the input file 2 path
     * @param n is the additional argument
     * @return 0 or returns a boolean for a duplicate tuple
     */
    public int analyseFileforPlagiarismContent (ArrayList<String> syn, String file1, String file2, int n) {
        input1 = file1;
        input2 = file2;
        synonym = syn;
        tupleSize = n;
        //map all the synonym except the first one to point to the first one in the respective list
        createMapforSynonyms();
        
        //replace all the synonym from input files to the first one in the respective list
        input1 = replaceSynonyms(input1);
        input2 = replaceSynonyms(input2);
        if(input1 == null || input2 == null) {
            return 0;
        }
        
        //tuples (without the space) from second file are put in tupleMay with frequency of occurrences
        instantiateTupleMap();
        
        //first input file is analyzed and check against duplicate tuple in tupleMap
        return checkDuplicateTuple();
    }
    
    /**
     * This method populates the HashMap synonymMap such all the synonym except the first one to point to the first one as a key-value pair
     */
    private void createMapforSynonyms() {
        //run through the arraylist and create the map such
        for (String synWords : synonym) {
            String temp[] = synWords.split(" ");
            for(int i = 1; i < temp.length ; i++)  {
                if(!synonymMap.containsKey(temp[i].toLowerCase())) {
                    synonymMap.put(temp[i].toLowerCase(), temp[0].toLowerCase());
                }
            }
        }
    }
    
    /**
     * This method will replace synonyms with first word in respective synonym list using synonymMap
     * Here Alpha-Numeric words are separated and everything else such as , . space etc is considered as word delimiter
     * @param fileContent
     * @return file content
     */
    private String replaceSynonyms(String fileContent) {
        String temp[] = fileContent.split(WORD_SEPRATOR_PATTERN);
        //check if the size of the tuple is more than total number of words in any of the file
        if(temp.length < tupleSize) {
            return null;
        }
        for(int i = 0; i < temp.length ; i++)  {
            if(synonymMap.containsKey(temp[i].toLowerCase())) {
                temp[i] = synonymMap.get(temp[i].toLowerCase());
            }
        }
        
        StringBuilder builder = new StringBuilder();
        for(String s : temp) {
            if(!s.isEmpty()) {
                builder.append(s);
                builder.append(" ");
            }
        }
        return builder.toString();
    }
    
    /**
     * In this method the tuples (without the space) from second file are put in tupleMay with frequency of occurrences
     */
    private void instantiateTupleMap() {
        String temp[] = input2.split(" ");
        //outer loop to go through the whole file
        for(int i = 0; i <= temp.length - tupleSize ; i++)  {
            StringBuilder builder = new StringBuilder();
            //inner loop to construct a specific tuple
            for (int j = 0; j < tupleSize ; j++) {
                builder.append(temp[i+j]);
            }
            if(!tupleMap.containsKey(builder.toString().toLowerCase())) {
                tupleMap.put(builder.toString().toLowerCase(), 1);
            } else {
                int freq = tupleMap.get(builder.toString().toLowerCase());
                freq++;
                tupleMap.put(builder.toString().toLowerCase(), freq);
            }
        }
    }
    
    /**
     * In this method there will be a check for duplicate tuples against the second file
     * @return percentage of plagiarism detected
     */
    private int checkDuplicateTuple() {
        int matchedTuplesCount = 0;
        int unmatchedTuplesCount = 0;
        String temp[] = input1.split(" ");
        //outer loop to go through the whole file
        for(int i = 0; i <= temp.length - tupleSize ; i++)  {
            StringBuilder builder = new StringBuilder();
            //inner loop to construct a specific tuple
            for (int j = 0; j < tupleSize ; j++) {
                builder.append(temp[i+j]);
            }
            // check if tuple is matched or not
            if(tupleMap.containsKey(builder.toString().toLowerCase())) {
                matchedTuplesCount++;
            } else {
                unmatchedTuplesCount++;
            }
        }
        
        Float matchingRatio = ((float)matchedTuplesCount/(float)(matchedTuplesCount+unmatchedTuplesCount));
        int percentage = (int) (matchingRatio*100);
        return percentage;
    }
}
