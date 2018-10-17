package plagiarismdetectiontool;

import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlagiarismDetectionTool {
    private static int defaultTupleSize = 3;
    
    /**
     * Accepts the inputs and passes that onto the code where the arguments are verified and the inputs are passed to the FileParser class
     */
    public static void execute(List<String> inputs) throws FileNotFoundException, IOException{
        //check how many arguments are passed
        if(inputs.size() < 3) {
            System.out.println("Invalid Inputs. Please mention in the code properly");
        } else {
            String file1 = null;
            String file2 = null;
            ArrayList<String> synonym = null;
            
            //read the files
            synonym = FileParser.readFileasAnArray(inputs.get(0));
            file1 = FileParser.readFile(inputs.get(1));
            file2 = FileParser.readFile(inputs.get(2));
            
            //check the optional argument
            if(inputs.size() == 4 && !inputs.get(3).equals("")) {
                defaultTupleSize = parseInt(inputs.get(3));
            }
            
            if(defaultTupleSize < 1) {
                defaultTupleSize = 3;
            }
            int percentage = PlagiarismDetector.getInstance().analyseFileforPlagiarismContent(synonym, file1, file2, defaultTupleSize);
            System.out.println(percentage + "%");
        }
    }
    
    /**
     * This is the main class for the project where the user will be entering the input files from the command line
     * Note: The files should always end with ".txt" extension
     */
    public static void main (String[] args) throws FileNotFoundException, IOException {
        ArrayList<String> inputargs = new ArrayList<String>();
        Scanner input = new Scanner(System.in);
        System.out.println("Hello User. Please Enter the Synonyms File");
        String syns = input.nextLine();
        System.out.println("Hello User. Please Enter the Input File1");
        String file1 = input.nextLine();
        System.out.println("Hello User. Please Enter the Input File2");
        String file2 = input.nextLine();
        
        System.out.println("Hello User. Please Enter the additional argument");
        String optionalArgument = input.nextLine();
        
        inputargs.add(syns);
        inputargs.add(file1);
        inputargs.add(file2);
        if(optionalArgument != ""){
            inputargs.add(optionalArgument);
        }
        
        execute(inputargs);
        input.close();
    }
}
