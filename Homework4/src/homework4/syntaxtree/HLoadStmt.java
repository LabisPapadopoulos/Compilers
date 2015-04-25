//
// Generated by JTB 1.3.2 DIT@UoA patched
//

package homework4.syntaxtree;

/**
 * Grammar production:
 * f0 -> "HLOAD"
 * f1 -> Temp()
 * f2 -> Exp()
 * f3 -> IntegerLiteral()
 */
public class HLoadStmt implements Node {
   public NodeToken f0;
   public Temp f1;
   public Exp f2;
   public IntegerLiteral f3;

   public HLoadStmt(NodeToken n0, Temp n1, Exp n2, IntegerLiteral n3) {
      f0 = n0;
      f1 = n1;
      f2 = n2;
      f3 = n3;
   }

   public HLoadStmt(Temp n0, Exp n1, IntegerLiteral n2) {
      f0 = new NodeToken("HLOAD");
      f1 = n0;
      f2 = n1;
      f3 = n2;
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

