//
// Generated by JTB 1.3.2 DIT@UoA patched
//

package homework5.syntaxtree;

/**
 * Grammar production:
 * f0 -> Call()
 *       | HAllocate()
 *       | BinOp()
 *       | SimpleExp()
 */
public class Exp implements Node {
   public NodeChoice f0;

   public Exp(NodeChoice n0) {
      f0 = n0;
   }

   public void accept(homework5.visitor.Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(homework5.visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(homework5.visitor.GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(homework5.visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
}

