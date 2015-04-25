package gr.uoa.di.std08169.compilers.hw3.visitors.firstpass;

import gr.uoa.di.std08169.compilers.hw3.symbols.Clazz;
import gr.uoa.di.std08169.compilers.hw3.symbols.Method;
import gr.uoa.di.std08169.compilers.hw3.symbols.Type;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.MainClass;
import gr.uoa.di.std08169.compilers.hw3.visitor.GJNoArguDepthFirst;

/**
 *
 * @author labis
 */
public class FirstPassMainClassVisitor extends GJNoArguDepthFirst<Clazz> {
    
    //Mazeuei tin mainClass
    @Override
    public Clazz visit(MainClass mainClass) {
        Clazz clazz = new Clazz(mainClass.f1.f0.tokenImage, null);
        
        //thesh methodou mesa sto v-table (an uparxei, tin vazei ekei pou htan,
        //diaforetika tin vazei sto telos)
        int offset = clazz.getMethods().containsKey(mainClass.f6.tokenImage) ? 
                clazz.getMethods().get(mainClass.f6.tokenImage).getOffset() : 
                clazz.getMethods().size();
        
        //onoma tis main class anti gia onoma methodou (main) kai tupos episrofhs unknown anti gia void
        Method main = new Method(mainClass.f6.tokenImage, Type.UNKNOWN, offset);
        
        clazz.getMethods().put(main.getName(), main);
        
        //Gemizei tis variables tis main
        mainClass.f14.accept(new FirstPassVariableVisitor(clazz.getName(), main.getName()), main.getVariables());
        //periptwsh pou den uparxei kamia metavlith mesa stin main.
        
        return clazz;
    }
}
