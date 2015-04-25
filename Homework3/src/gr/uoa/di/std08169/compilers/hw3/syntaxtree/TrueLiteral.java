//
// Generated by JTB 1.3.2 DIT@UoA patched
//

package gr.uoa.di.std08169.compilers.hw3.syntaxtree;

/**
 * Grammar production:
 * f0 -> "true"
 */
public class TrueLiteral implements Node {
   public NodeToken f0;

   public TrueLiteral(NodeToken n0) {
      f0 = n0;
   }

   public TrueLiteral() {
      f0 = new NodeToken("true");
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

