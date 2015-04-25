package homework5.visitors.firstpass;

import homework5.syntaxtree.SimpleExp;
import homework5.syntaxtree.Temp;
import homework5.visitor.GJVoidDepthFirst;
import java.util.List;

/**
 *
 * @author labis
 */
public class FirstpassSimpleExpressionVisitor extends GJVoidDepthFirst<List<Integer>> {
    
    @Override
    public void visit(final SimpleExp simpleExp, final List<Integer> temps) {
        //an sunantise temp, gemizei tin lista
        if(simpleExp.f0.choice instanceof Temp) {
            temps.add(Integer.parseInt(((Temp)simpleExp.f0.choice).f1.f0.tokenImage));
        }
    }
    
}
