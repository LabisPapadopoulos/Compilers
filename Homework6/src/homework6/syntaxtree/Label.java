//
// Generated by JTB 1.3.2 DIT@UoA patched
//

package homework6.syntaxtree;

/**
 * Grammar production:
 * f0 -> <IDENTIFIER>
 */
public class Label implements Node {
   public NodeToken f0;

   public Label(NodeToken n0) {
      f0 = n0;
   }

   public void accept(homework6.visitor.Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(homework6.visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(homework6.visitor.GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(homework6.visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
}
