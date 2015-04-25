package gr.uoa.di.std08169.compilers.hw2.visitors.secondpass;

import gr.uoa.di.std08169.compilers.hw2.symbols.Clazz;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.ClassDeclaration;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.ClassExtendsDeclaration;
import gr.uoa.di.std08169.compilers.hw2.visitor.DepthFirstVisitor;
import java.util.Map;

/**
 * Episkeutetai klaseis sto deutero perasma elenxontas tupous statements kai 
 * return expression
 * @author labis
 */
public class SecondPassClassVisitor extends DepthFirstVisitor {

    private Map<String, Clazz> classes;

    public SecondPassClassVisitor(Map<String, Clazz> classes) {
        this.classes = classes;
    }
    
    @Override
    public void visit(ClassDeclaration classDeclaration) {
        String name = classDeclaration.f1.f0.tokenImage;
        
        //Se kathe methodo tis klashs stelnetai o visitor gia tis methodous
        classDeclaration.f4.accept(new SecondPassMethodVisitor(name, classes));
    }
    
    @Override
    public void visit(ClassExtendsDeclaration classExtendsDeclaration) {
        String name = classExtendsDeclaration.f1.f0.tokenImage;
        classExtendsDeclaration.f6.accept(new SecondPassMethodVisitor(name, classes));
    }
}
