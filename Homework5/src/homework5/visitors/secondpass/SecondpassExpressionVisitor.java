package homework5.visitors.secondpass;

import homework5.Kanga;
import homework5.symbols.Graph;
import homework5.symbols.Register;
import homework5.syntaxtree.BinOp;
import homework5.syntaxtree.Call;
import homework5.syntaxtree.Exp;
import homework5.syntaxtree.HAllocate;
import homework5.syntaxtree.Node;
import homework5.syntaxtree.SimpleExp;
import homework5.syntaxtree.Temp;
import homework5.visitor.GJDepthFirst;
import java.io.PrintWriter;

/**
 *
 * @author labis
 */
public class SecondpassExpressionVisitor extends GJDepthFirst<String, PrintWriter> {
    
    private final Graph graph;
    private final SecondpassStatementExpressionVisitor secondpassStatementExpressionVisitor;

    public SecondpassExpressionVisitor(final Graph graph, 
            final SecondpassStatementExpressionVisitor secondpassStatementExpressionVisitor) {
        this.graph = graph;
        this.secondpassStatementExpressionVisitor = secondpassStatementExpressionVisitor;
    }

    @Override
    public String visit(final Exp exp, final PrintWriter printWriter) {
        if(exp.f0.choice instanceof Call) {
            final Call call = (Call) exp.f0.choice;
            
            //Ta prwta 4 orismata (an uparxoun) apothikeuontai stous kataxwrhtes a0 - a3
            printWriter.println("\t/* store first 4 args in a0 - a3 */");
            for(int i = 0; (i < Kanga.MAX_ARGS) && (i < call.f3.nodes.size()); i++) {
                final Node node = call.f3.nodes.get(i);
                if (node instanceof Temp) {
                    final int temp = Integer.parseInt(((Temp) node).f1.f0.tokenImage);
                    final Register register = graph.getTempInfo(temp).getColour();
                    
                    if(register == null) {
                        //Fortwsh apo to Stack ston kataxwrhth ai
                        printWriter.println("\tALOAD a" + i + " " + " SPILLEDARG " + 
                                (graph.countSpilledArgs() + graph.getTempInfo(temp).getStackOffset()));
                    } else {
                        printWriter.println("\tMOVE a" + i + " " + register);
                    }
                }
            }
            
            //Ta upoloipa orismata apothikeuontai sth mnhmh (Stack)
            printWriter.println("\t/* store rest args in stack */");
            for(int i = Kanga.MAX_ARGS; i < call.f3.nodes.size(); i++) {
                final Node node = call.f3.nodes.get(i);
                
                if (node instanceof Temp) {
                    final int temp = Integer.parseInt(((Temp) node).f1.f0.tokenImage);
                    final Register register = graph.getTempInfo(temp).getColour();
                    
                    if(register == null) {
                        //Fortwsh apo to stack sto v0 kai klhsh PASSARG
                        printWriter.println("\tALOAD v0 SPILLEDARG " + (graph.countSpilledArgs() + 
                                graph.getTempInfo(temp).getStackOffset()));
                        printWriter.println("\tPASSARG " + (i - Kanga.MAX_ARGS + 1) + " v0");
                    } else {
                        //Xekinaei na ta apothikeuei apo to 1 ...
                        printWriter.println("\tPASSARG " + (i - Kanga.MAX_ARGS + 1) + " " + register);
                    }
                }
            }
            
            //Oi t0 - t9 bainoun epishs sto stack
            printWriter.println("\t/* store t0 - t9 in stack */");
            for(int i = 0; i < Register.T_REGISTERS; i++) {
                printWriter.println("\tASTORE SPILLEDARG " + (graph.countSpilledArgs() +
                        graph.countSpilledVariables() + i) + " t" + i);
            }
            
            printWriter.println("\tCALL " + call.f1.accept(new SecondpassSimpleExpressionVisitor(graph, "v0"), printWriter));
            
            //Oi t0 - t9 epanaferontai apo to stack
            printWriter.println("\t/* load t0 - t9 from stack */");
            for(int i = 0; i < Register.T_REGISTERS; i++) {
                printWriter.println("\tALOAD t" + i + " SPILLEDARG " + (graph.countSpilledArgs() +
                        graph.countSpilledVariables() + i));
            }
            
            return "v0"; //apotelesma tis sunatishs pou klhthike
            
        } else if(exp.f0.choice instanceof HAllocate) {
            return "HALLOCATE " + ((HAllocate) exp.f0.choice).f1.accept(new SecondpassSimpleExpressionVisitor(graph, "v0"), 
                    printWriter);
        } else if(exp.f0.choice instanceof BinOp) {
            BinOp binOp = ((BinOp) exp.f0.choice);
            final String operator = binOp.f0.f0.choice.toString();
            final int temp = Integer.parseInt(binOp.f1.f1.f0.tokenImage);
            final Register register = graph.getTempInfo(temp).getColour();
            
            if(register == null) {
                printWriter.println("\tALOAD v0 SPILLEDARG " + (graph.countSpilledArgs() + 
                        graph.getTempInfo(temp).getStackOffset()));
                                                                    //v1, giati o v0 xrhsimopoieitai hdh gia ton 1o telesteo.
                return operator + " v0 " + binOp.f2.accept(new SecondpassSimpleExpressionVisitor(graph, "v1"), printWriter);
            } else {
                                                    //v0, giati o prwtos telesteos einai hdh se kanoniko kataxwrhth s 'h t.
                return operator + " " + register + " " + binOp.f2.accept(new SecondpassSimpleExpressionVisitor(graph, "v0"), 
                    printWriter);
            }
        } else if(exp.f0.choice instanceof SimpleExp) {
            return exp.f0.choice.accept(new SecondpassSimpleExpressionVisitor(graph, "v0"), printWriter);
        }
        return null;
    }
}
