/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework4.visitors.secondpass;

import java.io.PrintWriter;

import homework4.Spiglet;
import homework4.syntaxtree.Label;
import homework4.syntaxtree.Node;
import homework4.syntaxtree.NodeOptional;
import homework4.syntaxtree.NodeSequence;
import homework4.syntaxtree.Stmt;
import homework4.syntaxtree.StmtList;
import homework4.visitor.GJVoidDepthFirst;

/**
 *
 * @author labis
 */
public class SecondPassStatementListVisitor extends GJVoidDepthFirst<PrintWriter> {
    private final String procedure;
    private final Spiglet spiglet;
    
    public SecondPassStatementListVisitor(final String procedure, final Spiglet spiglet) {
        this.procedure = procedure;
        this.spiglet = spiglet;
    }
    
    @Override
    public void visit(final StmtList statementList, final PrintWriter printWriter) {
        
        for (Node node : statementList.f0.nodes) {
            if (node instanceof NodeSequence) {
                for (Node n : ((NodeSequence) node).nodes) {
                    if ((n instanceof NodeOptional) && (((NodeOptional) n).node instanceof Label)) {
		    	final String label = ((Label) ((NodeOptional) n).node).f0.tokenImage;
		        printWriter.print(label);
                        
                    } else if (n instanceof Stmt) {
                        n.accept(new SecondPassStatementVisitor(procedure, spiglet), printWriter);
                    }
                }
            }
        }
    }
}
