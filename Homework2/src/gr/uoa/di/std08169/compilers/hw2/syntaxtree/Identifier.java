//
// Generated by JTB 1.3.2 DIT@UoA patched
//

package gr.uoa.di.std08169.compilers.hw2.syntaxtree;

/**
 * Grammar production:
 * f0 -> <IDENTIFIER>
 */
public class Identifier implements Node {
   public NodeToken f0;

   public Identifier(NodeToken n0) {
      f0 = n0;
   }

   public void accept(gr.uoa.di.std08169.compilers.hw2.visitor.Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(gr.uoa.di.std08169.compilers.hw2.visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(gr.uoa.di.std08169.compilers.hw2.visitor.GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(gr.uoa.di.std08169.compilers.hw2.visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
}

