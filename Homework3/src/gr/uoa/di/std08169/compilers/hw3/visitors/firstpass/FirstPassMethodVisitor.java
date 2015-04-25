package gr.uoa.di.std08169.compilers.hw3.visitors.firstpass;

import gr.uoa.di.std08169.compilers.hw3.CompileException;
import gr.uoa.di.std08169.compilers.hw3.symbols.Method;
import gr.uoa.di.std08169.compilers.hw3.symbols.Type;
import gr.uoa.di.std08169.compilers.hw3.symbols.Variable;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.MethodDeclaration;
import gr.uoa.di.std08169.compilers.hw3.visitor.GJVoidDepthFirst;
import java.util.Map;

/**
 *
 * @author labis
 */
public class FirstPassMethodVisitor extends GJVoidDepthFirst<Map<String, Method>> {
    
    private String clazz;
    
    public FirstPassMethodVisitor(String clazz) {
        this.clazz = clazz;
    }
    
    public void visit(MethodDeclaration methodDeclaration, Map<String, Method> methods) {
        
        String name = methodDeclaration.f2.f0.tokenImage;
        Type type = new Type(methodDeclaration.f1);
        
        //Den exoume overload se methodous
        if(methods.containsKey(name)) {
            throw new CompileException("Error: Grammh " + methodDeclaration.f2.f0.beginLine + ": h methodos " 
                    + name + " einai hdh dhlwmenh stin klash " + clazz + ", den boreis na tin xana dhlwseis!");
        } else {
            
            //thesh methodou mesa sto v-table (an uparxei, tin vazei ekei pou htan,
            //diaforetika tin vazei sto telos)
            int offset = methods.containsKey(name) ? 
                methods.get(name).getOffset() : 
                methods.size();

            Method method = new Method(name, type, offset);
            methods.put(name, method);

            //f4: Gia tis parametrous tis methodou
            methodDeclaration.f4.accept(new FirstPassFormalParameterVisitor(clazz, name), method.getParameters());
            
            //Prosthikh twn orismatwn stis metavlites, afou kai ta orismata theorountai metavlites
            for(Variable parameter : method.getParameters()) {
                method.getVariables().add(parameter);
            }
            
            //f7: Gia metavlites tis methodou
            methodDeclaration.f7.accept(new FirstPassVariableVisitor(clazz, name), method.getVariables());
        }
    }
    
}
