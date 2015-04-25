//
// Generated by JTB 1.3.2 DIT@UoA patched
//

package gr.uoa.di.std08169.compilers.hw3.syntaxtree;

/**
 * The interface which all syntax tree classes must implement.
 */
public interface Node extends java.io.Serializable {
   public void accept(gr.uoa.di.std08169.compilers.hw3.visitor.Visitor v);
   public <R,A> R accept(gr.uoa.di.std08169.compilers.hw3.visitor.GJVisitor<R,A> v, A argu);
   public <R> R accept(gr.uoa.di.std08169.compilers.hw3.visitor.GJNoArguVisitor<R> v);
   public <A> void accept(gr.uoa.di.std08169.compilers.hw3.visitor.GJVoidVisitor<A> v, A argu);
}

