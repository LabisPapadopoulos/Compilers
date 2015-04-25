package homework5.visitors.firstpass;

import homework5.syntaxtree.BinOp;
import homework5.syntaxtree.Call;
import homework5.syntaxtree.Exp;
import homework5.syntaxtree.HAllocate;
import homework5.syntaxtree.Node;
import homework5.syntaxtree.SimpleExp;
import homework5.syntaxtree.Temp;
import homework5.visitor.GJDepthFirst;
import java.util.List;

/**
 *
 * @author labis
 */
class FirstpassExpressionVisitor extends GJDepthFirst<Integer, List<Integer>> {

    @Override
    public Integer visit(final Exp exp, final List<Integer> temps) {
          if(exp.f0.choice instanceof Call) {
              final  Call call = ((Call)exp.f0.choice);
              
              call.f1.accept(new FirstpassSimpleExpressionVisitor(), temps);
              //gemisma tis listas me ta ena 'h perissotera temps tou Call
              for(Node node : call.f3.nodes) {
                  if (node instanceof Temp) {
                      temps.add(Integer.parseInt(((Temp) node).f1.f0.tokenImage));
                  }
              }
              return call.f3.nodes.size();
          } else if(exp.f0.choice instanceof HAllocate) {
              ((HAllocate)exp.f0.choice).f1.accept(new FirstpassSimpleExpressionVisitor(), temps);
              return 0;
          } else if(exp.f0.choice instanceof BinOp) {
              final BinOp binOp = ((BinOp)exp.f0.choice);
              
              temps.add(Integer.parseInt(binOp.f1.f1.f0.tokenImage));
              binOp.f2.accept(new FirstpassSimpleExpressionVisitor(), temps);
              return 0;
          } else if(exp.f0.choice instanceof SimpleExp) {
              //stelnei visitor gia na mazepsei temps
              ((SimpleExp)exp.f0.choice).accept(new FirstpassSimpleExpressionVisitor(), temps);
              return 0;
          }
          return 0;
    }
}
