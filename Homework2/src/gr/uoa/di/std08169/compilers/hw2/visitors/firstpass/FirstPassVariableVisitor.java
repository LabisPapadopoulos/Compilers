package gr.uoa.di.std08169.compilers.hw2.visitors.firstpass;

import gr.uoa.di.std08169.compilers.hw2.symbols.Type;
import gr.uoa.di.std08169.compilers.hw2.symbols.Variable;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.VarDeclaration;
import gr.uoa.di.std08169.compilers.hw2.visitor.GJVoidDepthFirst;
import java.util.Map;

/**
 *
 * @author labis
 */
public class FirstPassVariableVisitor extends GJVoidDepthFirst<Map<String, Variable>>{
    
    private String scope;
    
    public FirstPassVariableVisitor(String scope) {
        this.scope = scope;
    }
    
    @Override
    public void visit(VarDeclaration varDeclaration, Map<String, Variable> variables) {
        
        Type type = new Type(varDeclaration.f0);

        String name = varDeclaration.f1.f0.tokenImage;
        
        //an uparxei hdh auth h metavlhth
        if(variables.containsKey(name)) {
            System.err.println("Error: Grammh " + varDeclaration.f1.f0.beginLine + ": h metavlhth " 
                    + name + " einai hdh dhlwmenh " + scope + ", den boreis na tin xana dhlwseis!");
        } else {
            variables.put(name, new Variable(name, type));
        }
        
    }
}
