//
// Generated by JTB 1.3.2 DIT@UoA patched
//

package homework4.syntaxtree;

/**
 * Grammar production:
 * f0 -> "ERROR"
 */
public class ErrorStmt implements Node {
   public NodeToken f0;

   public ErrorStmt(NodeToken n0) {
      f0 = n0;
   }

   public ErrorStmt() {
      f0 = new NodeToken("ERROR");
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

