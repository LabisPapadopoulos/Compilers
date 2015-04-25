/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework4.visitors.firstpass;

import homework4.syntaxtree.StmtExp;
import homework4.visitor.GJNoArguDepthFirst;

/**
 *
 * @author labis
 */
public class FirstPassStatementExpressionVisitor extends GJNoArguDepthFirst<Integer> {
    
    @Override
    public Integer visit(final StmtExp statementExpression) {
        
        int statementList = statementExpression.f1.accept(new FirstPassStatementListVisitor());
        int expression = statementExpression.f3.accept(new FirstPassExpressionVisitor());
        
        return (statementList > expression) ? statementList : expression;
    }
}
