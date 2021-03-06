//
// Generated by JTB 1.3.2 DIT@UoA patched
//

package homework4.syntaxtree;

/**
 * Grammar production:
 * f0 -> StmtExp()
 *       | Call()
 *       | HAllocate()
 *       | BinOp()
 *       | Temp()
 *       | IntegerLiteral()
 *       | Label()
 */
public class Exp implements Node {
   public NodeChoice f0;

   public Exp(NodeChoice n0) {
      f0 = n0;
   }

   public void accept(homework4.visitor.Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(homework4.visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(homework4.visitor.GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(homework4.visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
}

