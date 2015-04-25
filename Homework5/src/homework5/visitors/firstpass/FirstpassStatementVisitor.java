package homework5.visitors.firstpass;

import homework5.symbols.Graph;
import homework5.symbols.TempInfo;
import homework5.syntaxtree.CJumpStmt;
import homework5.syntaxtree.ErrorStmt;
import homework5.syntaxtree.HLoadStmt;
import homework5.syntaxtree.HStoreStmt;
import homework5.syntaxtree.JumpStmt;
import homework5.syntaxtree.MoveStmt;
import homework5.syntaxtree.NoOpStmt;
import homework5.syntaxtree.PrintStmt;
import homework5.syntaxtree.Stmt;
import homework5.visitor.GJVoidDepthFirst;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author labis
 */
public class FirstpassStatementVisitor extends GJVoidDepthFirst<Graph> {
    
    private FirstpassStatementExpressionVisitor firstpassStatementExpressionVisitor;
    
    public FirstpassStatementVisitor (final FirstpassStatementExpressionVisitor firstpassStatementExpressionVisitor) {
        this.firstpassStatementExpressionVisitor = firstpassStatementExpressionVisitor;
    }
    
    //visit to idio to statemet kai enhmerwsh tou map metavlhtwn
    @Override
    public void visit(final Stmt stmt, final Graph variables) {
        //entoles spiglet
        if(stmt.f0.choice instanceof NoOpStmt) { //Den anakateuei pouthena metavlhtes
            firstpassStatementExpressionVisitor.nextStatement(); //proxwrai entolh
        } else if(stmt.f0.choice instanceof ErrorStmt) { //Omoia den anakateuei pouthena metavlhtes
            firstpassStatementExpressionVisitor.nextStatement();
        } else if(stmt.f0.choice instanceof CJumpStmt) {
            //xrhsimopoiei mia temp
            //einai zwntanh metavlith - right value
            final int temp = Integer.parseInt(((CJumpStmt) stmt.f0.choice).f1.f1.f0.tokenImage);
            final int statement = firstpassStatementExpressionVisitor.nextStatement();
            
            //update to end me tin trexousa entolh
            if(variables.containsTemp(temp)) {
                variables.getTempInfo(temp).setEnd(statement);
            }
        } else if(stmt.f0.choice instanceof JumpStmt) {
            firstpassStatementExpressionVisitor.nextStatement();
        } else if(stmt.f0.choice instanceof HStoreStmt) {
            //Ta temp pou xrhsimopoiei h HStoreStmt
            final int temp1 = Integer.parseInt(((HStoreStmt) stmt.f0.choice).f1.f1.f0.tokenImage); //r-value
            final int temp2 = Integer.parseInt(((HStoreStmt) stmt.f0.choice).f3.f1.f0.tokenImage); //r-value
            final int statement = firstpassStatementExpressionVisitor.nextStatement();
            
            //enhmerwsh tou live range ths temp1
            if(variables.containsTemp(temp1)) {
                variables.getTempInfo(temp1).setEnd(statement);
            }
            
            //enhmerwsh tou live range tis temp2
            if(variables.containsTemp(temp2)) {
                variables.getTempInfo(temp2).setEnd(statement);
            }
            
        } else if(stmt.f0.choice instanceof HLoadStmt) {
            final int temp1 = Integer.parseInt(((HLoadStmt) stmt.f0.choice).f1.f1.f0.tokenImage); //l-value
            final int temp2 = Integer.parseInt(((HLoadStmt) stmt.f0.choice).f2.f1.f0.tokenImage); //r-value

            //trexousa entolh
            final int statement = firstpassStatementExpressionVisitor.nextStatement();
            
            //Thn vlepei gia prwth fora, kai xekinaei kainourio live range
            //temp1 se def
            if(!variables.containsTemp(temp1)) {
                //update to begin sto l-value
                variables.addTemp(temp1, new TempInfo(statement));
            }
            
            //temp2 se use
            if(variables.containsTemp(temp2)) {
                variables.getTempInfo(temp2).setEnd(statement);
            }
            
            
        } else if(stmt.f0.choice instanceof MoveStmt) {
            final int temp = Integer.parseInt(((MoveStmt) stmt.f0.choice).f1.f1.f0.tokenImage); //l-value
            final int statement = firstpassStatementExpressionVisitor.nextStatement();
            
            if(!variables.containsTemp(temp)) {
                variables.addTemp(temp, new TempInfo(statement));
            }
            
            final List<Integer> temps = new ArrayList<Integer>(); //r-values
            //Visitor gia ta temps pou tha sinanthsei sto Exp()
            final int maxArgs = ((MoveStmt)stmt.f0.choice).f2.accept(new FirstpassExpressionVisitor(), temps);
            
            if (maxArgs > variables.getMaxArgs()) {
            	variables.setMaxArgs(maxArgs);
            }
            
            for(int t : temps) {
                if(variables.containsTemp(t)) {
                    //gia kathe mia temp, pou einai zwntanh
                    variables.getTempInfo(t).setEnd(statement);
                }
            }
        } else if(stmt.f0.choice instanceof PrintStmt) {
            final List<Integer> temps = new ArrayList<Integer>();
            final int statement = firstpassStatementExpressionVisitor.nextStatement();
            //mporei na exei ena temp 'h tipota
            ((PrintStmt)stmt.f0.choice).f1.accept(new FirstpassSimpleExpressionVisitor(), temps);
            
            if((temps.size() > 0) && variables.containsTemp(temps.get(0))) {
                variables.getTempInfo(temps.get(0)).setEnd(statement);
            }
        }
    }
}
