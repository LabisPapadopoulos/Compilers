package gr.uoa.di.std08169.compilers.hw3.visitors.firstpass;

import gr.uoa.di.std08169.compilers.hw3.CompileException;
import gr.uoa.di.std08169.compilers.hw3.symbols.Type;
import gr.uoa.di.std08169.compilers.hw3.symbols.Variable;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.VarDeclaration;
import gr.uoa.di.std08169.compilers.hw3.visitor.GJVoidDepthFirst;
import java.util.List;
import java.util.Map;

/**
 *
 * @author labis
 */
public class FirstPassVariableVisitor extends GJVoidDepthFirst<List<Variable>>{
    
    private String clazz;
    private String method;
    
    public FirstPassVariableVisitor(String clazz, String method) {
        this.clazz = clazz;
        this.method = method;
    }
    
    @Override
    public void visit(VarDeclaration varDeclaration, List<Variable> variables) {
        
        Type type = new Type(varDeclaration.f0);

        String name = varDeclaration.f1.f0.tokenImage;
        
        //an uparxei hdh auth h metavlhth
        
        for (Variable variable : variables) {
            if (variable.getName().equals(name)) {
                throw new CompileException("Error: Grammh " + varDeclaration.f1.f0.beginLine + ": h metavlhth " 
                    + name + " einai hdh dhlwmenh " + ((method == null) ? ("stin klash " + clazz) : 
                    ("sth methodo " + method + " tis klashs " + clazz)) + ", den boreis na tin xana dhlwseis!");
            
            }
        }
        variables.add(new Variable(name, type));
    }
}
