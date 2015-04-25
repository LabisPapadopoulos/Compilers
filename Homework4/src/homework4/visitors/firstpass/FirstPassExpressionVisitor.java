/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework4.visitors.firstpass;

import homework4.syntaxtree.BinOp;
import homework4.syntaxtree.Call;
import homework4.syntaxtree.Exp;
import homework4.syntaxtree.HAllocate;
import homework4.syntaxtree.IntegerLiteral;
import homework4.syntaxtree.Label;
import homework4.syntaxtree.Node;
import homework4.syntaxtree.StmtExp;
import homework4.syntaxtree.Temp;
import homework4.visitor.GJNoArguDepthFirst;

/**
 *
 * @author labis
 */
public class FirstPassExpressionVisitor extends GJNoArguDepthFirst<Integer> {
    
    @Override
    public Integer visit(final Exp expression) {
        
        if (expression.f0.choice instanceof StmtExp) {
            final StmtExp statementExpression = ((StmtExp) expression.f0.choice);
            int statementList = statementExpression.f1.accept(new FirstPassStatementListVisitor());
            int exp = statementExpression.f3.accept(new FirstPassExpressionVisitor());
            
            return (statementList > exp) ? statementList : exp;
        } else if (expression.f0.choice instanceof Call) {
            final Call call = ((Call) expression.f0.choice);
            int result = call.f1.accept(new FirstPassExpressionVisitor());
            
            for (Node node : call.f3.nodes) {
                if (node instanceof Exp) {
                    int r = node.accept(new FirstPassExpressionVisitor());
                    if (r > result)
                        result = r;
                }
            }
            
            return result;
        } else if (expression.f0.choice instanceof HAllocate) {
            return ((HAllocate) expression.f0.choice).f1.accept(new FirstPassExpressionVisitor());
        } else if (expression.f0.choice instanceof BinOp) {
            final BinOp binaryOperator = ((BinOp) expression.f0.choice);
            final int expression1 = binaryOperator.f1.accept(new FirstPassExpressionVisitor());
            final int expression2 = binaryOperator.f2.accept(new FirstPassExpressionVisitor());
            
            return (expression1 > expression2) ? expression1 : expression2;
        } else if (expression.f0.choice instanceof Temp)
            return Integer.parseInt(((Temp) expression.f0.choice).f1.f0.tokenImage);
        else if (expression.f0.choice instanceof IntegerLiteral)
            return -1;
        else if (expression.f0.choice instanceof Label)
            return -1;
        return -1;
    }
}
