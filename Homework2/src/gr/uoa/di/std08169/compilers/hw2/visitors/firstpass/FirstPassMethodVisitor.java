package gr.uoa.di.std08169.compilers.hw2.visitors.firstpass;

import gr.uoa.di.std08169.compilers.hw2.symbols.Method;
import gr.uoa.di.std08169.compilers.hw2.symbols.Type;
import gr.uoa.di.std08169.compilers.hw2.symbols.Variable;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.MethodDeclaration;
import gr.uoa.di.std08169.compilers.hw2.visitor.GJVoidDepthFirst;
import java.util.Map;

/**
 *
 * @author labis
 */
public class FirstPassMethodVisitor extends GJVoidDepthFirst<Map<String, Method>> {
    
    private String scope;
    
    public FirstPassMethodVisitor(String scope) {
        this.scope = scope;
    }
    
    public void visit(MethodDeclaration methodDeclaration, Map<String, Method> methods) {
        
        String name = methodDeclaration.f2.f0.tokenImage;
        Type type = new Type(methodDeclaration.f1);
        
        //Den exoume overload se methodous
        if(methods.containsKey(name)) {
            System.err.println("Error: Grammh " + methodDeclaration.f2.f0.beginLine + ": h methodos " 
                    + name + " einai hdh dhlwmenh " + scope + ", den boreis na tin xana dhlwseis!");
        } else {
            Method method = new Method(name, type);
            methods.put(name, method);
            
            //f4: Gia tis parametrous tis methodou
            methodDeclaration.f4.accept(new FirstPassFormalParameterVisitor("sth methodo " + name + " " + scope), method.getParameters());
            
            //Prosthikh twn orismatwn stis metavlites, afou kai ta orismata theorountai metavlites
            for(Variable parameter : method.getParameters()) {
                method.getVariables().put(parameter.getName(), parameter);
            }
            
            //f7: Gia metavlites tis methodou
            methodDeclaration.f7.accept(new FirstPassVariableVisitor("sth methodo " + name + " " + scope), method.getVariables());
        }
    }
    
}
