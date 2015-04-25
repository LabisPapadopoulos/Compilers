/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uoa.di.std08169.compilers.hw2.visitors.firstpass;

import gr.uoa.di.std08169.compilers.hw2.symbols.Type;
import gr.uoa.di.std08169.compilers.hw2.symbols.Variable;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.FormalParameter;
import gr.uoa.di.std08169.compilers.hw2.visitor.GJVoidDepthFirst;
import java.util.List;

/**
 * Visitor gia tis parametrous (Lista) kathe methodou
 * @author labis
 */
public class FirstPassFormalParameterVisitor extends GJVoidDepthFirst<List<Variable>> {
    
    private String scope;
    
    public FirstPassFormalParameterVisitor(String scope) {
        this.scope = scope;
    }
    
    @Override
    public void visit(FormalParameter formalParameter, List<Variable> parameters) {
        Type type = new Type(formalParameter.f0);
        String name = formalParameter.f1.f0.tokenImage;
        
        //Elenxos an ta onomata twn parametrwn einai monadika
        for (Variable parameter : parameters) {
            if (parameter.getName().equals(name)) {
                System.err.println("Error: Grammh " + formalParameter.f1.f0.beginLine + ": h parametros " 
                    + name + " einai hdh dhlwmenh " + scope + ", den boreis na tin xana dhlwseis!");
                return;
            }
        }
        
        Variable parameter = new Variable(name, type);
        parameters.add(parameter);
    }
}
