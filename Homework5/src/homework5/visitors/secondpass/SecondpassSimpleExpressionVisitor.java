package homework5.visitors.secondpass;

import homework5.symbols.Graph;
import homework5.symbols.Register;
import homework5.syntaxtree.IntegerLiteral;
import homework5.syntaxtree.Label;
import homework5.syntaxtree.SimpleExp;
import homework5.syntaxtree.Temp;
import homework5.visitor.GJDepthFirst;
import java.io.PrintWriter;

/**
 *
 * @author labis
 */
public class SecondpassSimpleExpressionVisitor extends GJDepthFirst<String, PrintWriter> {
 
    private final Graph graph;
    private final String vRegister;
    
    public SecondpassSimpleExpressionVisitor(final Graph graph, final String vRegister) {
        this.graph = graph;
        this.vRegister = vRegister;
    }
    
    @Override
    public String visit(final SimpleExp simpleExp, final PrintWriter printWriter) {
        if(simpleExp.f0.choice instanceof Temp) {
            final int temp = Integer.parseInt(((Temp) simpleExp.f0.choice).f1.f0.tokenImage);
            
            final Register register = graph.getTempInfo(temp).getColour();
            
            if(register == null) {
                printWriter.println("\tALOAD " + vRegister + " SPILLEDARG " + 
                        (graph.countSpilledArgs() + graph.getTempInfo(temp).getStackOffset()));
                return vRegister;
            } else {
                return register.toString();
            }
            
        } else if(simpleExp.f0.choice instanceof IntegerLiteral) {
            return ((IntegerLiteral) simpleExp.f0.choice).f0.tokenImage;
        } else if(simpleExp.f0.choice instanceof Label) {
            return ((Label) simpleExp.f0.choice).f0.tokenImage;
        }
        return null;
    }
}
