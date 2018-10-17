package plagiarismdetectiontool;

import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static int defaultTupleSize = 3;
    
    public static void execute(List<String> inputs) throws FileNotFoundException, IOException{
        //check how many arguments are passed
        if(inputs.size() < 3) {
            System.out.println("Invalid Inputs. Please mention in the code properly");
        } else {
            String file1 = null;
            String file2 = null;
            ArrayList<String> synonym = null;
            
            //read the files
            synonym = FileHandler.readFileArray(inputs.get(0));
            file1 = FileHandler.readFile(inputs.get(1));
            file2 = FileHandler.readFile(inputs.get(2));
            
            //check the optional argument
            if(inputs.size() == 4) {
                defaultTupleSize = parseInt(inputs.get(3));
            }
            if(defaultTupleSize < 1) {
                defaultTupleSize = 3;
            }
            int percentage = Detector.getInstance().analysePlagarismContent(synonym, file1, file2, defaultTupleSize);
            System.out.println(percentage + "%");
        }
    }
    
    public static void main (String[] args) throws FileNotFoundException, IOException {
        ArrayList<String> inputargs = new ArrayList<String>();
        String syns = "syns.txt";
        String file1 = "input1.txt";
        String file2 = "input2.txt";
        String optionalArgument = "4";
        
        inputargs.add(syns);
        inputargs.add(file1);
        inputargs.add(file2);
        inputargs.add(optionalArgument);
        execute(inputargs);
    }
}
