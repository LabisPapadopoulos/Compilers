package homework4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import homework4.syntaxtree.Goal;
import homework4.visitors.firstpass.FirstPassProcedureVisitor;
import homework4.visitors.firstpass.FirstPassStatementListVisitor;
import homework4.visitors.secondpass.SecondPassProcedureVisitor;
import homework4.visitors.secondpass.SecondPassStatementListVisitor;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author labis
 */
public class Spiglet {
    private static final String PIGLET_EXTENSION = ".pg";
    private static final String SPIGLET_EXTENSION = ".spg";
    
    private final Goal goal;
    private final Map<String, Integer> procedures;

    public static void main(final String[] arguments) {
        
        for (String argument : arguments) {
            try {
                final Spiglet spiglet = new Spiglet(new File(argument));
                spiglet.firstPass();
                
                final PrintWriter printWriter = new PrintWriter(new File(argument.replaceFirst(PIGLET_EXTENSION, SPIGLET_EXTENSION)));
                spiglet.secondPass(printWriter);
                
                printWriter.close();
            } catch (final FileNotFoundException e) {
                System.err.println("Error compiling file " + argument + ": " + e.getMessage());
            } catch (final ParseException e) {
                System.err.println("Error compiling file " + argument + ": " + e.getMessage());
            }
        }
    }
    
    public Spiglet(final File file) throws FileNotFoundException, ParseException {
        goal = new PigletParser(new FileInputStream(file)).Goal();
        procedures = new HashMap<String, Integer>();
    }
    
    public void firstPass() {
        procedures.put(null, goal.f1.accept(new FirstPassStatementListVisitor()));
        goal.f3.accept(new FirstPassProcedureVisitor(), procedures);
    }
    
    public int getTemp(final String procedure) {
        int temp = procedures.get(procedure);
        procedures.put(procedure, temp + 1);
        return temp + 1;
    }
    
    public void secondPass(final PrintWriter printWriter) {
        printWriter.println("MAIN");
        goal.f1.accept(new SecondPassStatementListVisitor(null, this), printWriter);
        printWriter.println("END");
        goal.f3.accept(new SecondPassProcedureVisitor(this), printWriter);
    }
}
