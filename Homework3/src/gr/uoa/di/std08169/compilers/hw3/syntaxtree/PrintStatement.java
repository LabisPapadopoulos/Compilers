//
// Generated by JTB 1.3.2 DIT@UoA patched
//

package gr.uoa.di.std08169.compilers.hw3.syntaxtree;

/**
 * Grammar production:
 * f0 -> "System.out.println"
 * f1 -> "("
 * f2 -> Expression()
 * f3 -> ")"
 * f4 -> ";"
 */
public class PrintStatement implements Node {
   public NodeToken f0;
   public NodeToken f1;
   public Expression f2;
   public NodeToken f3;
   public NodeToken f4;

   public PrintStatement(NodeToken n0, NodeToken n1, Expression n2, NodeToken n3, NodeToken n4) {
      f0 = n0;
      f1 = n1;
      f2 = n2;
      f3 = n3;
      f4 = n4;
   }

   public PrintStatement(Expression n0) {
      f0 = new NodeToken("System.out.println");
      f1 = new NodeToken("(");
      f2 = n0;
      f3 = new NodeToken(")");
      f4 = new NodeToken(";");
   }

   public void accept(gr.uoa.di.std08169.compilers.hw3.visitor.Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(gr.uoa.di.std08169.compilers.hw3.visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(gr.uoa.di.std08169.compilers.hw3.visitor.GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(gr.uoa.di.std08169.compilers.hw3.visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
}
