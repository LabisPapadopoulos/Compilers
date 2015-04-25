package gr.uoa.di.std08169.compilers.hw2.visitors.secondpass;

import gr.uoa.di.std08169.compilers.hw2.symbols.Clazz;
import gr.uoa.di.std08169.compilers.hw2.symbols.Type;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.ExpressionList;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.ExpressionTerm;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.Node;
import gr.uoa.di.std08169.compilers.hw2.visitor.GJNoArguDepthFirst;
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
    private Map<String, Clazz> classes;

    public SecondPassExpressionListVisitor(String clazz, String method, Map<String, Clazz> classes) {
        this.clazz = clazz;
        this.method = method;
        this.classes = classes;
    }
    
    @Override
    public List<Type> visit(ExpressionList expressionList) {
        
        List <Type> types = new ArrayList<Type>();
        //prosthikh tou prwtou sti lista
        types.add(expressionList.f0.accept(new SecondPassExpressionVisitor(clazz, method, classes)));
        
        //prosthikh twn upoloipwn expressions
        for (Node node : expressionList.f1.f0.nodes)
            types.add(((ExpressionTerm) node).f1.accept(new SecondPassExpressionVisitor(clazz, method, classes)));
        return types;
    }
}
