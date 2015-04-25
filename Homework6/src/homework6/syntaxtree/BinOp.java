//
// Generated by JTB 1.3.2 DIT@UoA patched
//

package homework6.syntaxtree;

/**
 * Grammar production:
 * f0 -> Operator()
 * f1 -> Reg()
 * f2 -> SimpleExp()
 */
public class BinOp implements Node {
   public Operator f0;
   public Reg f1;
   public SimpleExp f2;

   public BinOp(Operator n0, Reg n1, SimpleExp n2) {
      f0 = n0;
      f1 = n1;
      f2 = n2;
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
