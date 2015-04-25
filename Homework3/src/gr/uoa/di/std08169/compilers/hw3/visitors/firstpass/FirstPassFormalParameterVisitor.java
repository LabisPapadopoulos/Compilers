/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uoa.di.std08169.compilers.hw3.visitors.firstpass;

import gr.uoa.di.std08169.compilers.hw3.CompileException;
import gr.uoa.di.std08169.compilers.hw3.symbols.Type;
import gr.uoa.di.std08169.compilers.hw3.symbols.Variable;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.FormalParameter;
import gr.uoa.di.std08169.compilers.hw3.visitor.GJVoidDepthFirst;
import java.util.List;

/**
 * Visitor gia tis parametrous (Lista) kathe methodou
 * @author labis
 */
public class FirstPassFormalParameterVisitor extends GJVoidDepthFirst<List<Variable>> {
    
    private String clazz;
    private String method;
    
    public FirstPassFormalParameterVisitor(String clazz, String method) {
        this.clazz = clazz;
        this.method = method;
    }
    
    @Override
    public void visit(FormalParameter formalParameter, List<Variable> parameters) {
        Type type = new Type(formalParameter.f0);
        String name = formalParameter.f1.f0.tokenImage;
        
        //Elenxos an ta onomata twn parametrwn einai monadika
        for (Variable parameter : parameters) {
            if (parameter.getName().equals(name)) {
                throw new CompileException("Error: Grammh " + formalParameter.f1.f0.beginLine + ": h parametros " 
                    + name + " einai hdh dhlwmenh sth methodo " + method + " ths klashs " + clazz 
                    + ", den boreis na tin xana dhlwseis!");
            }
        }
        
        Variable parameter = new Variable(name, type);
        parameters.add(parameter);
    }
}
