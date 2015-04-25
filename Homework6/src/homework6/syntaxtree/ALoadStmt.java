//
// Generated by JTB 1.3.2 DIT@UoA patched
//

package homework6.syntaxtree;

/**
 * Grammar production:
 * f0 -> "ALOAD"
 * f1 -> Reg()
 * f2 -> SpilledArg()
 */
public class ALoadStmt implements Node {
   public NodeToken f0;
   public Reg f1;
   public SpilledArg f2;

   public ALoadStmt(NodeToken n0, Reg n1, SpilledArg n2) {
      f0 = n0;
      f1 = n1;
      f2 = n2;
   }

   public ALoadStmt(Reg n0, SpilledArg n1) {
      f0 = new NodeToken("ALOAD");
      f1 = n0;
      f2 = n1;
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
