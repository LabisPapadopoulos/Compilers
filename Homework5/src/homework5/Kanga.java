package homework5;

import homework5.symbols.Graph;
import homework5.symbols.Register;
import homework5.syntaxtree.Goal;
import homework5.visitors.firstpass.FirstpassProcedureVisitor;
import homework5.visitors.firstpass.FirstpassStatementExpressionVisitor;
import homework5.visitors.firstpass.FirstpassStatementListVisitor;
import homework5.visitors.secondpass.SecondpassProcedureVisitor;
import homework5.visitors.secondpass.SecondpassStatementExpressionVisitor;
import homework5.visitors.secondpass.SecondpassStatementListVisitor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author labis
 */
public class Kanga {
    public static final int MAX_ARGS = 4;
    
    private static final String SPIGLET_EXTENSION = ".spg";
    private static final String KANGA_EXTENSION = ".kg";
    
    private final Goal goal;
    private final Map<String, Graph> procedures;

    public static void main(final String[] arguments) {
        
        for (String argument : arguments) {
            try {
                final Kanga spiglet = new Kanga(new File(argument));
                spiglet.firstPass();
                
                final PrintWriter printWriter = new PrintWriter(new File(argument.replaceFirst(SPIGLET_EXTENSION, KANGA_EXTENSION)));
                spiglet.secondPass(printWriter);
                
                printWriter.close();
            } catch (final FileNotFoundException e) {
                System.err.println("Error compiling file " + argument + ": " + e.getMessage());
            } catch (final ParseException e) {
                System.err.println("Error compiling file " + argument + ": " + e.getMessage());
            }
        }
    }
    
    public Kanga(final File file) throws FileNotFoundException, ParseException {
        goal = new SpigletParser(new FileInputStream(file)).Goal();
        procedures = new HashMap<String, Graph>();
    }
    
    public void firstPass() {
        //keno map gia thn procedure main
        procedures.put(null, new Graph(0));
        goal.f1.accept(new FirstpassStatementListVisitor(new FirstpassStatementExpressionVisitor()), procedures.get(null));
        goal.f3.accept(new FirstpassProcedureVisitor(), procedures);
        
        for(Map.Entry<String, Graph> entry : procedures.entrySet()) {
            entry.getValue().chaitinAlgorithm();
        }
    }
    
    public void secondPass(final PrintWriter printWriter) {
        final Graph graph = procedures.get(null);
        final int stackFrameSize = graph.countSpilledVariables() + Register.values().length;
        final int maxArgs = graph.getMaxArgs();
        
        printWriter.println("MAIN [0] [" + stackFrameSize + "] [" + maxArgs + "]");
        
        //Den uparxoun orismata gia fortwma oute kalousa sunarthsh 
        //gia na xreiastei na swthoun kataxwrhtes
        goal.f1.accept(new SecondpassStatementListVisitor(graph, 
                new SecondpassStatementExpressionVisitor(graph)), printWriter);
        printWriter.println("END");
        goal.f3.accept(new SecondpassProcedureVisitor(procedures), printWriter);
    }
}
