//
// Generated by JTB 1.3.2 DIT@UoA patched
//

package gr.uoa.di.std08169.compilers.hw3.syntaxtree;

/**
 * Grammar production:
 * f0 -> Expression()
 * f1 -> ExpressionTail()
 */
public class ExpressionList implements Node {
   public Expression f0;
   public ExpressionTail f1;

   public ExpressionList(Expression n0, ExpressionTail n1) {
      f0 = n0;
      f1 = n1;
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

