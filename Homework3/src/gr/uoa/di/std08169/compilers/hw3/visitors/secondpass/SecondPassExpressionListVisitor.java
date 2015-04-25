package gr.uoa.di.std08169.compilers.hw3.visitors.secondpass;

import gr.uoa.di.std08169.compilers.hw3.Piglet;
import gr.uoa.di.std08169.compilers.hw3.symbols.Clazz;
import gr.uoa.di.std08169.compilers.hw3.symbols.Type;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.ExpressionList;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.ExpressionTerm;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.Node;
import gr.uoa.di.std08169.compilers.hw3.visitor.GJNoArguDepthFirst;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author labis
 */
public class SecondPassExpressionListVisitor extends GJNoArguDepthFirst<List <Type>> {
    
    private String clazz;
    private String method;
    private Piglet piglet;
    
    public SecondPassExpressionListVisitor(String clazz, String method, 
            Piglet piglet) {
        this.clazz = clazz;
        this.method = method;
        this.piglet = piglet;
    }
    
    @Override
    public List<Type> visit(ExpressionList expressionList) {
        
        List <Type> types = new ArrayList<Type>();
        //prosthikh tou prwtou sti lista
        types.add(expressionList.f0.accept(new SecondPassExpressionVisitor(clazz, method, piglet)));
        
        //prosthikh twn upoloipwn expressions
        for (Node node : expressionList.f1.f0.nodes)
            types.add(((ExpressionTerm) node).f1.accept(new SecondPassExpressionVisitor(clazz, method, piglet)));
        return types;
    }
}
