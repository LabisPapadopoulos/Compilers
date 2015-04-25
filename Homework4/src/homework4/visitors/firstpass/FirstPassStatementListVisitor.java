/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homework4.visitors.firstpass;

import homework4.syntaxtree.Node;
import homework4.syntaxtree.NodeSequence;
import homework4.syntaxtree.Stmt;
import homework4.syntaxtree.StmtList;
import homework4.visitor.GJNoArguDepthFirst;

/**
 *
 * @author labis
 */
public class FirstPassStatementListVisitor extends GJNoArguDepthFirst<Integer> {
    
    @Override
    public Integer visit(final StmtList statementList) {
        
        int result = -1;
        
        for (Node node : statementList.f0.nodes) {
            if (node instanceof NodeSequence) {
                for (Node n : ((NodeSequence) node).nodes) {
                    if (n instanceof Stmt) {
                        int statement = n.accept(new FirstPassStatementVisitor());
                        if (statement > result)
                            result = statement;
                    }
                }
            }
        }
        
        return result;
    }
}
