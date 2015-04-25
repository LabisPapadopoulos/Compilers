package gr.uoa.di.std08169.compilers.hw2.visitors.firstpass;

import gr.uoa.di.std08169.compilers.hw2.symbols.Clazz;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.ClassDeclaration;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.ClassExtendsDeclaration;
import gr.uoa.di.std08169.compilers.hw2.visitor.GJVoidDepthFirst;
import java.util.Map;

/**
 * Visitor gia na vriskei ta onomata twn klasewn
 * @author labis
 */
public class FirstPassClassVisitor extends GJVoidDepthFirst<Map<String, Clazz>> {
    
    //Gia tis kanonikes klaseis
    @Override
    public void visit(ClassDeclaration classDeclaration, Map<String, Clazz> classes) {
        String name = classDeclaration.f1.f0.tokenImage;
        
        //an uparxei hdh auth h klash
        if(classes.containsKey(name)) {
            System.err.println("Error: Grammh " + classDeclaration.f1.f0.beginLine + ": h klassh " + name + 
                    " einai hdh dhlwmenh, den boreis na tin xana dhlwseis!");
        } else {
            Clazz clazz = new Clazz(name);
            
            classes.put(name, clazz);
            
            //Xekinaei o visitor na psaxnei tis metavlites tis klashs (f3)
            //Lunetai to provlima me ta scope, afou gemizoun diaforetika maps se kathe klash
            classDeclaration.f3.accept(new FirstPassVariableVisitor("sthn klash " + name), clazz.getVariables());
            
            //Xekinaei enas visitor na psaxnei tis methodous tis klashs (f4)
            classDeclaration.f4.accept(new FirstPassMethodVisitor("sthn klash " + name), clazz.getMethods());
        }
    }
    
    //Gia tis klaseis me patera (extends)
    @Override
    public void visit(ClassExtendsDeclaration classExtendsDeclaration, Map<String, Clazz> classes) {        
        
        String name = classExtendsDeclaration.f1.f0.tokenImage;
        String parent = classExtendsDeclaration.f3.f0.tokenImage;
        
        //an uparxei hdh auth h klash
        if(classes.containsKey(name)) {
            System.err.println("Error: Grammh " + classExtendsDeclaration.f1.f0.beginLine + ": h klassh " + name + 
                    " einai hdh dhlwmenh, den boreis na tin xana dhlwseis!");
        } else {
            Clazz clazz = new Clazz(name, parent);
            
            classes.put(name, clazz);
            
            //f5: gia tis metavlites sto classExtendsDeclaration
            classExtendsDeclaration.f5.accept(new FirstPassVariableVisitor("sthn klash " + name), clazz.getVariables());
            //f6: gia tis methodous sto classExtendsDeclaration
            classExtendsDeclaration.f6.accept(new FirstPassMethodVisitor("sthn klash " + name), clazz.getMethods());
        }
        
    }
}
