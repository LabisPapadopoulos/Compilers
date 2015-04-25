package homework5.visitors.secondpass;

import homework5.symbols.Graph;
import homework5.syntaxtree.StmtExp;
import homework5.visitor.GJVoidDepthFirst;
import java.io.PrintWriter;

/**
 *
 * @author labis
 */
public class SecondpassStatementExpressionVisitor extends GJVoidDepthFirst<PrintWriter> {
    
    private final Graph graph;
    private int statementCounter;

    public SecondpassStatementExpressionVisitor(final Graph graph) {
        this.graph = graph;
        statementCounter = 0;
    }
    
    public int getStatement() {
        return statementCounter;
    }
    
    public int nextStatement() {
        return statementCounter++;
    }

    @Override
    public void visit(final StmtExp stmtExp, final PrintWriter printWriter) {
        
        //Swma sunartishs
        stmtExp.f1.accept(new SecondpassStatementListVisitor(graph, this), printWriter);
        
        //v0, h epistrefomenh timh
        printWriter.println("\t/* store return value in v0 */");
        printWriter.println("\tMOVE v0 " + 
                stmtExp.f3.accept(new SecondpassSimpleExpressionVisitor(graph, "v0"), printWriter));
    }
}
