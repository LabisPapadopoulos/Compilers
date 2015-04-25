/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uoa.di.std08169.compilers.hw2.visitors.firstpass;

import gr.uoa.di.std08169.compilers.hw2.symbols.Clazz;
import gr.uoa.di.std08169.compilers.hw2.symbols.Method;
import gr.uoa.di.std08169.compilers.hw2.symbols.Type;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.MainClass;
import gr.uoa.di.std08169.compilers.hw2.visitor.GJNoArguDepthFirst;
import gr.uoa.di.std08169.compilers.hw2.visitor.GJVoidDepthFirst;

/**
 *
 * @author labis
 */
public class FirstPassMainClassVisitor extends GJNoArguDepthFirst<Clazz> {
    
    //Mazeuei tin mainClass
    @Override
    public Clazz visit(MainClass mainClass) {
        Clazz clazz = new Clazz(mainClass.f1.f0.tokenImage, null);
        //onoma tis main class anti gia onoma methodou (main) kai tupos episrofhs unknown anti gia void
        Method main = new Method(mainClass.f6.tokenImage, Type.UNKNOWN);
        
        clazz.getMethods().put(main.getName(), main);
        
        //Gemizei tis variables tis main
        mainClass.f14.accept(new FirstPassVariableVisitor(main.getName()), main.getVariables());
        
        return clazz;
    }
}
