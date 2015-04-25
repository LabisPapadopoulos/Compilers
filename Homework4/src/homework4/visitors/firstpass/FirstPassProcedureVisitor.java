/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework4.visitors.firstpass;

import java.util.Map;

import homework4.syntaxtree.Procedure;
import homework4.visitor.GJVoidDepthFirst;

/**
 *
 * @author labis
 */
public class FirstPassProcedureVisitor extends GJVoidDepthFirst<Map<String, Integer>> {
    
    @Override
    public void visit(final Procedure procedure, final Map<String, Integer> procedures) {
        procedures.put(procedure.f0.f0.tokenImage, procedure.f4.accept(new FirstPassStatementExpressionVisitor()));
    }    
}
