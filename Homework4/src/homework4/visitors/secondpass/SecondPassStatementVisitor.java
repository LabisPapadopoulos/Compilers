/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework4.visitors.secondpass;

import java.io.PrintWriter;

import homework4.Spiglet;
import homework4.syntaxtree.CJumpStmt;
import homework4.syntaxtree.ErrorStmt;
import homework4.syntaxtree.Exp;
import homework4.syntaxtree.HLoadStmt;
import homework4.syntaxtree.HStoreStmt;
import homework4.syntaxtree.JumpStmt;
import homework4.syntaxtree.MoveStmt;
import homework4.syntaxtree.NoOpStmt;
import homework4.syntaxtree.PrintStmt;
import homework4.syntaxtree.Stmt;
import homework4.visitor.GJVoidDepthFirst;

/**
 *
 * @author labis
 */
public class SecondPassStatementVisitor extends GJVoidDepthFirst<PrintWriter> {
    private final String procedure;
    private final Spiglet spiglet;
    
    public SecondPassStatementVisitor(final String procedure, final Spiglet spiglet) {
        this.procedure = procedure;
        this.spiglet = spiglet;
    }
    
    @Override
    public void visit(final Stmt statement, final PrintWriter printWriter) {
        
        if (statement.f0.choice instanceof NoOpStmt) {
            printWriter.println("\tNOOP");
            
        } else if (statement.f0.choice instanceof ErrorStmt) {
            printWriter.println("\tERROR");
            
        } else if (statement.f0.choice instanceof CJumpStmt) {
            final CJumpStmt conditionalJumpStatement = ((CJumpStmt) statement.f0.choice);
            final int expression = conditionalJumpStatement.f1.accept(new SecondPassExpressionVisitor(procedure, spiglet), printWriter);
            final String label = conditionalJumpStatement.f2.f0.tokenImage;
            
            printWriter.print("\tCJUMP TEMP " + expression + "  " + label);
            
        } else if (statement.f0.choice instanceof JumpStmt) {
            final String label = ((JumpStmt) statement.f0.choice).f1.f0.tokenImage;
            
            printWriter.println("\tJUMP " + label);
            
        } else if (statement.f0.choice instanceof HStoreStmt) {
            final HStoreStmt heapStoreStatement = ((HStoreStmt) statement.f0.choice);
            final int expression1 = heapStoreStatement.f1.accept(new SecondPassExpressionVisitor(procedure, spiglet), printWriter);
            final int integer = Integer.parseInt(heapStoreStatement.f2.f0.tokenImage);
            final int expression2 = heapStoreStatement.f3.accept(new SecondPassExpressionVisitor(procedure, spiglet), printWriter);
            
            printWriter.println("\tHSTORE TEMP " + expression1 + " " + integer + " TEMP " + expression2);
            
        } else if (statement.f0.choice instanceof HLoadStmt) {
            final HLoadStmt heapLoadStatement = ((HLoadStmt) statement.f0.choice);
            final int temp = Integer.parseInt(heapLoadStatement.f1.f1.f0.tokenImage);
            final int expression = heapLoadStatement.f2.accept(new SecondPassExpressionVisitor(procedure, spiglet), printWriter);
            final int integer = Integer.parseInt(heapLoadStatement.f3.f0.tokenImage);
            
            printWriter.println("\tHLOAD TEMP " + temp + " TEMP " + expression + " " + integer);
            
        } else if (statement.f0.choice instanceof MoveStmt) {
            final MoveStmt moveStatement = ((MoveStmt) statement.f0.choice);
            final int temp = Integer.parseInt(moveStatement.f1.f1.f0.tokenImage);
            final int expression = moveStatement.f2.accept(new SecondPassExpressionVisitor(procedure, spiglet), printWriter);
            
            printWriter.println("\tMOVE TEMP " + temp + " TEMP " + expression);
            
        } else if (statement.f0.choice instanceof PrintStmt) {
            final int expression = ((PrintStmt) statement.f0.choice).f1.accept(new SecondPassExpressionVisitor(procedure, spiglet), printWriter);
            
            printWriter.println("\tPRINT TEMP " + expression);
        }
    }
}

