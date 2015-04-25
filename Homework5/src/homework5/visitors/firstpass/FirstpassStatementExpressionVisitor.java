package homework5.visitors.firstpass;

import homework5.symbols.Graph;
import homework5.syntaxtree.StmtExp;
import homework5.visitor.GJVoidDepthFirst;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author labis
 */
public class FirstpassStatementExpressionVisitor extends GJVoidDepthFirst<Graph> {
    
    //Metrhths entolwn
    private int statementCounter;

    public FirstpassStatementExpressionVisitor() {
        statementCounter = 0;
    }
    
    public int nextStatement() {
        return statementCounter++;
    }
    
    //visit to idio to StmtExp kai to orisma ths
    @Override
    public void visit(final StmtExp stmtExp, final Graph variables) {
        
        //apostolh visitor gia to StmtList
        stmtExp.f1.accept(new FirstpassStatementListVisitor(this), variables);
        
        final List<Integer> temps = new ArrayList<Integer>();
        
        stmtExp.f3.accept(new FirstpassSimpleExpressionVisitor(), temps);
        final int statement = nextStatement();
        
        //upologismos tou live range tis teleutaias metavlhths pou tha ginei return
        if(temps.size() > 0) {
            variables.getTempInfo(temps.get(0)).setEnd(statement);
        }
    }
}
