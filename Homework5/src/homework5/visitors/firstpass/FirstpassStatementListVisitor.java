package homework5.visitors.firstpass;

import homework5.symbols.Graph;
import homework5.syntaxtree.StmtList;
import homework5.visitor.GJVoidDepthFirst;

/**
 *
 * @author labis
 */
public class FirstpassStatementListVisitor extends GJVoidDepthFirst<Graph> {
    
    private FirstpassStatementExpressionVisitor firstpassStatementExpressionVisitor;
    
    public FirstpassStatementListVisitor(final FirstpassStatementExpressionVisitor firstpassStatementExpressionVisitor) {
        this.firstpassStatementExpressionVisitor = firstpassStatementExpressionVisitor;
    } 
    
    //visit ena StmtList me orisma tis metavlites tou
    @Override
    public void visit(final StmtList stmtList, final Graph variables) {
        
        stmtList.f0.accept(new FirstpassStatementVisitor(firstpassStatementExpressionVisitor), variables);
    }
}
