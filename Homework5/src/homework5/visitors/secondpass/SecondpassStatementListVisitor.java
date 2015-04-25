package homework5.visitors.secondpass;

import homework5.symbols.Graph;
import homework5.syntaxtree.Label;
import homework5.syntaxtree.Node;
import homework5.syntaxtree.NodeOptional;
import homework5.syntaxtree.NodeSequence;
import homework5.syntaxtree.Stmt;
import homework5.syntaxtree.StmtList;
import homework5.visitor.GJVoidDepthFirst;
import java.io.PrintWriter;

/**
 *
 * @author labis
 */
public class SecondpassStatementListVisitor extends GJVoidDepthFirst<PrintWriter> {
    
    private final Graph graph;
    private final SecondpassStatementExpressionVisitor secondpassStatementExpressionVisitor;
    
    public SecondpassStatementListVisitor(final Graph graph, 
            final SecondpassStatementExpressionVisitor secondpassStatementExpressionVisitor) {
        this.graph = graph;
        this.secondpassStatementExpressionVisitor = secondpassStatementExpressionVisitor;
    }

    @Override
    public void visit(final StmtList stmtList, final PrintWriter printWriter) {
        //Gia kathe zeugari Label, Stmt mesa sto StmtList
        for (Node node1 : stmtList.f0.nodes) {
            if (node1 instanceof NodeSequence) {
                
                //Gia kathe Label kai Stmt mesa sto zeugari
                for (Node node2 : ((NodeSequence) node1).nodes) {
                    if (node2 instanceof NodeOptional) {
                        final NodeOptional nodeOptional = ((NodeOptional) node2);
                        
                        if(nodeOptional.node instanceof Label) {
                            printWriter.print(((Label) nodeOptional.node).f0.tokenImage);
                        }

                    } else if (node2 instanceof Stmt) {
                        node2.accept(new SecondpassStatementVisitor(graph, 
                                secondpassStatementExpressionVisitor), printWriter);
                    }
                }
            }
        }
    }
}
