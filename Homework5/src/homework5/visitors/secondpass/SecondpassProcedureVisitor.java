package homework5.visitors.secondpass;

import homework5.Kanga;
import homework5.symbols.Graph;
import homework5.symbols.Register;
import homework5.syntaxtree.Procedure;
import homework5.visitor.GJVoidDepthFirst;
import java.io.PrintWriter;
import java.util.Map;

/**
 *
 * @author labis
 */
public class SecondpassProcedureVisitor extends GJVoidDepthFirst<PrintWriter> {
    
    private final Map<String, Graph> procedures;

    public SecondpassProcedureVisitor(final Map<String, Graph> procedures) {
        this.procedures = procedures;
    }

    @Override
    public void visit(final Procedure procedure, final PrintWriter printWriter) {
        String label = procedure.f0.f0.tokenImage;
        
        //orismata sunartishs (spiglet)
        final int argc = Integer.parseInt(procedure.f2.f0.tokenImage);
        
        final Graph graph = procedures.get(label);
        
        final int stackFrameSize = graph.countSpilledArgs() + graph.countSpilledVariables() + Register.values().length;
        
        final int maxArgs = graph.getMaxArgs();

        printWriter.println(label + " [" + argc + "] [" + stackFrameSize + "] [" + maxArgs + "]");
        
        //Oi s0 - s7 bainoun sto stack
        printWriter.println("\t/* store s0 - s7 in stack */");
        for(int i = 0; i < Register.S_REGISTERS; i++) {
            printWriter.println("\tASTORE SPILLEDARG " + (graph.countSpilledArgs() +
                    graph.countSpilledVariables() + Register.T_REGISTERS + i) + " s" + i);
        }
        
        //Ta prwta 4 orismata (an uparxoun) diavazontai apo tous kataxwrhtes a0 - a3
        printWriter.println("\t/* read first 4 args from a0 - a3 */");
        for(int i = 0; (i < Kanga.MAX_ARGS) && (i < argc); i++) {
            //Ginetai antistoixhsh apo to TEMP ths spiglet se poion register katelhxe gia thn kanga
            final Register register = graph.getTempInfo(i).getColour();
            
            if(register == null) {
                //To orisma pou einai ston kataxwrhth ai apothhkeuetai sto stack (meta ta 
                //parapanw apo 4 orismata) giati den xwraei se kanoniko kataxwrhth t 'h s
                printWriter.println("\tASTORE SPILLEDARG " + (graph.countSpilledArgs() + 
                        graph.getTempInfo(i).getStackOffset()) + " a" + i);
            } else {
                printWriter.println("\tMOVE " + register + " a" + i);
            }
        }

        //Ta upoloipa orismata diavazontai apo th mnhmh (Stack)
        printWriter.println("\t/* load rest args from stack */");
        for(int i = Kanga.MAX_ARGS; i < argc; i++) {
            final Register register = graph.getTempInfo(i).getColour();
            
            if(register == null) {
                //Fortwnetai proswrina sto v0 to orisma i apo to stack
                printWriter.println("ALOAD v0 SPILLEDARG " + i);
                //metafwra tou v0 sto stack se allh thesh
                printWriter.println("ASTORE SPILLEDARG " + (graph.countSpilledArgs() + 
                        graph.getTempInfo(i).getStackOffset()) + " v0");
            } else {
                //fortwsh tou kataxwrhth apo to stack
                printWriter.println("\tALOAD " + register + " SPILLEDARG " + i);
            }
        }
                                                                //metavlhtes tis procedure
        procedure.f4.accept(new SecondpassStatementExpressionVisitor(procedures.get(label)), printWriter);
        
        //Oi s0 - s7 epanaferontai apo to stack
        printWriter.println("\t/* load s0 - s7 from stack */");
        for(int i = 0; i < Register.S_REGISTERS; i++) {
            printWriter.println("\tALOAD s" + i + " SPILLEDARG " + (graph.countSpilledArgs() +
                    graph.countSpilledVariables() + Register.T_REGISTERS + i));
        }
        
        printWriter.println("END");
    }
}
