package gr.uoa.di.std08169.compilers.hw3.visitors.secondpass;

import gr.uoa.di.std08169.compilers.hw3.Piglet;
import gr.uoa.di.std08169.compilers.hw3.symbols.Clazz;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.ClassDeclaration;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.ClassExtendsDeclaration;
import gr.uoa.di.std08169.compilers.hw3.visitor.DepthFirstVisitor;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Episkeutetai klaseis sto deutero perasma elenxontas tupous statements kai 
 * return expression
 * @author labis
 */
public class SecondPassClassVisitor extends DepthFirstVisitor {

    private Piglet piglet;

    public SecondPassClassVisitor(Piglet piglet) {
        this.piglet = piglet;
    }
    
    @Override
    public void visit(ClassDeclaration classDeclaration) {
        String name = classDeclaration.f1.f0.tokenImage;
        
        //Se kathe methodo tis klashs stelnetai o visitor gia tis methodous
        classDeclaration.f4.accept(new SecondPassMethodVisitor(name, piglet));
    }
    
    @Override
    public void visit(ClassExtendsDeclaration classExtendsDeclaration) {
        String name = classExtendsDeclaration.f1.f0.tokenImage;
        classExtendsDeclaration.f6.accept(new SecondPassMethodVisitor(name, piglet));
    }
}
