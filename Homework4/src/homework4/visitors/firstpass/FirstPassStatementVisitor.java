/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework4.visitors.firstpass;

import homework4.syntaxtree.CJumpStmt;
import homework4.syntaxtree.ErrorStmt;
import homework4.syntaxtree.HLoadStmt;
import homework4.syntaxtree.HStoreStmt;
import homework4.syntaxtree.JumpStmt;
import homework4.syntaxtree.MoveStmt;
import homework4.syntaxtree.NoOpStmt;
import homework4.syntaxtree.PrintStmt;
import homework4.syntaxtree.Stmt;
import homework4.visitor.GJNoArguDepthFirst;

/**
 *
 * @author labis
 */
public class FirstPassStatementVisitor extends GJNoArguDepthFirst<Integer> {
    
    @Override
    public Integer visit(final Stmt statement) {
        
        if (statement.f0.choice instanceof NoOpStmt)
            return -1;
        else if (statement.f0.choice instanceof ErrorStmt)
            return -1;
        else if (statement.f0.choice instanceof CJumpStmt)
            return ((CJumpStmt) statement.f0.choice).f1.accept(new FirstPassExpressionVisitor());
        else if (statement.f0.choice instanceof JumpStmt)
            return -1;
        else if (statement.f0.choice instanceof HStoreStmt) {
            final HStoreStmt heapStoreStatement = ((HStoreStmt) statement.f0.choice);
            final int expression1 = heapStoreStatement.f1.accept(new FirstPassExpressionVisitor());
            final int expression2 = heapStoreStatement.f3.accept(new FirstPassExpressionVisitor());
            
            return (expression1 > expression2) ? expression1 : expression2;
        } else if (statement.f0.choice instanceof HLoadStmt) {
            final HLoadStmt heapLoadStatement = ((HLoadStmt) statement.f0.choice);
            final int temp = Integer.parseInt(heapLoadStatement.f1.f1.f0.tokenImage);
            final int expression = heapLoadStatement.f2.accept(new FirstPassExpressionVisitor());
            
            return (temp > expression) ? temp : expression;
        } else if (statement.f0.choice instanceof MoveStmt) {
            final MoveStmt moveStatement = ((MoveStmt) statement.f0.choice);
            final int temp = Integer.parseInt(moveStatement.f1.f1.f0.tokenImage);
            final int expression = moveStatement.f2.accept(new FirstPassExpressionVisitor());
            
            return (temp > expression) ? temp : expression;
        } else if (statement.f0.choice instanceof PrintStmt) {
            return ((PrintStmt) statement.f0.choice).f1.accept(new FirstPassExpressionVisitor());
        }
        return -1;
    }
}
