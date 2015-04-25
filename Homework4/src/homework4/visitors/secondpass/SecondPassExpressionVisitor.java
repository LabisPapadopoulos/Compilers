/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework4.visitors.secondpass;

import java.io.PrintWriter;

import homework4.Spiglet;
import homework4.syntaxtree.BinOp;
import homework4.syntaxtree.Call;
import homework4.syntaxtree.Exp;
import homework4.syntaxtree.HAllocate;
import homework4.syntaxtree.IntegerLiteral;
import homework4.syntaxtree.Label;
import homework4.syntaxtree.Node;
import homework4.syntaxtree.NodeToken;
import homework4.syntaxtree.StmtExp;
import homework4.syntaxtree.StmtList;
import homework4.syntaxtree.Temp;
import homework4.visitor.GJDepthFirst;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author labis
 */
public class SecondPassExpressionVisitor extends GJDepthFirst<Integer, PrintWriter> {
    private final String procedure;
    private final Spiglet spiglet;
    
    public SecondPassExpressionVisitor(final String procedure, final Spiglet spiglet) {
        this.procedure = procedure;
        this.spiglet = spiglet;
    }
    
    @Override
    public Integer visit(final Exp expression, final PrintWriter printWriter) {
        
        if (expression.f0.choice instanceof StmtExp) {
            final StmtExp statementExpression = ((StmtExp) expression.f0.choice);
            statementExpression.f1.accept(new SecondPassStatementListVisitor(procedure, spiglet), printWriter);            
            final int exp = statementExpression.f3.accept(new SecondPassExpressionVisitor(procedure, spiglet), printWriter);
            
            return exp;
        } else if (expression.f0.choice instanceof Call) {
            final Call call = ((Call) expression.f0.choice);
            final int exp = call.f1.accept(new SecondPassExpressionVisitor(procedure, spiglet), printWriter);
            final List<Integer> expressions = new ArrayList<Integer>();
            
            for (Node node : call.f3.nodes) {
                if (node instanceof Exp)
                    expressions.add(node.accept(new SecondPassExpressionVisitor(procedure, spiglet), printWriter));
            }
            
            final int temp = spiglet.getTemp(procedure);
            
            printWriter.print("\tMOVE TEMP " + temp + " CALL TEMP " + exp + " (");
                for (int e : expressions)
                    printWriter.print("TEMP " + e + " ");
            printWriter.println(")");
            
            return temp;
        } else if (expression.f0.choice instanceof HAllocate) {
            final int exp = ((HAllocate) expression.f0.choice).f1.accept(new SecondPassExpressionVisitor(procedure, spiglet), printWriter);
            final int temp = spiglet.getTemp(procedure);
            
            printWriter.println("\tMOVE TEMP " + temp + " HALLOCATE TEMP " + exp);
            
            return temp;
        } else if (expression.f0.choice instanceof BinOp) {
            final BinOp binaryOperator = ((BinOp) expression.f0.choice);
            final int expression1 = binaryOperator.f1.accept(new SecondPassExpressionVisitor(procedure, spiglet), printWriter);
            final int expression2 = binaryOperator.f2.accept(new SecondPassExpressionVisitor(procedure, spiglet), printWriter);
            final int temp = spiglet.getTemp(procedure);
            
            printWriter.println("\tMOVE TEMP " + temp + " " + ((NodeToken) binaryOperator.f0.f0.choice).tokenImage 
                    + " TEMP " + expression1 + " TEMP " + expression2);
            
            return temp;
        } else if (expression.f0.choice instanceof Temp) {
            final int temp = Integer.parseInt(((Temp) expression.f0.choice).f1.f0.tokenImage);
            
            return temp;
        } else if (expression.f0.choice instanceof IntegerLiteral) {
            final int integer = Integer.parseInt(((IntegerLiteral) expression.f0.choice).f0.tokenImage);
            final int temp = spiglet.getTemp(procedure);
            
            printWriter.println("\tMOVE TEMP " + temp + " " + integer);
            
            return temp;
        } else if (expression.f0.choice instanceof Label) {
            final String label = ((Label) expression.f0.choice).f0.tokenImage;
            final int temp = spiglet.getTemp(procedure);
            
            printWriter.println("\tMOVE TEMP " + temp + " " + label);
            
            return temp;
        }
        return -1;
    }
}

