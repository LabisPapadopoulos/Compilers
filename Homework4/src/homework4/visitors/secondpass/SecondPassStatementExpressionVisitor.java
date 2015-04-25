/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework4.visitors.secondpass;

import java.io.PrintWriter;

import homework4.Spiglet;
import homework4.syntaxtree.StmtExp;
import homework4.visitor.GJDepthFirst;

/**
 *
 * @author labis
 */
public class SecondPassStatementExpressionVisitor extends GJDepthFirst<Integer, PrintWriter> {
    private final String procedure;
    private final Spiglet spiglet;
    
    public SecondPassStatementExpressionVisitor(final String procedure, final Spiglet spiglet) {
        this.procedure = procedure;
        this.spiglet = spiglet;
    }
    
    @Override
    public Integer visit(final StmtExp statementExpression, final PrintWriter printWriter) {
        statementExpression.f1.accept(new SecondPassStatementListVisitor(procedure, spiglet), printWriter);
        return statementExpression.f3.accept(new SecondPassExpressionVisitor(procedure, spiglet), printWriter);
    }
}
