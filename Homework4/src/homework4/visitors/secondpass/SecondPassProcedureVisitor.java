/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework4.visitors.secondpass;

import java.io.PrintWriter;

import homework4.Spiglet;
import homework4.syntaxtree.Procedure;
import homework4.visitor.GJVoidDepthFirst;

/**
 *
 * @author labis
 */
public class SecondPassProcedureVisitor extends GJVoidDepthFirst<PrintWriter> {
    private final Spiglet spiglet;
    
    public SecondPassProcedureVisitor(final Spiglet spiglet) {
        this.spiglet = spiglet;
    }
    
    @Override
    public void visit(final Procedure procedure, final PrintWriter printWriter) {
        
        final String name = procedure.f0.f0.tokenImage;
        final int arguments = Integer.parseInt(procedure.f2.f0.tokenImage);
        
        printWriter.println(name + "[" + arguments + "]");
        printWriter.println("BEGIN");
        
        int statementExpression = procedure.f4.accept(new SecondPassStatementExpressionVisitor(name, spiglet), printWriter);
        
        printWriter.println("\tRETURN TEMP " + statementExpression);
        printWriter.println("END");
    }
}
