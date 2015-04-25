//
// Generated by JTB 1.3.2 DIT@UoA patched
//

package gr.uoa.di.std08169.compilers.hw3.syntaxtree;

/**
 * Grammar production:
 * f0 -> "public"
 * f1 -> Type()
 * f2 -> Identifier()
 * f3 -> "("
 * f4 -> ( FormalParameterList() )?
 * f5 -> ")"
 * f6 -> "{"
 * f7 -> ( VarDeclaration() )*
 * f8 -> ( Statement() )*
 * f9 -> "return"
 * f10 -> Expression()
 * f11 -> ";"
 * f12 -> "}"
 */
public class MethodDeclaration implements Node {
   public NodeToken f0;
   public Type f1;
   public Identifier f2;
   public NodeToken f3;
   public NodeOptional f4;
   public NodeToken f5;
   public NodeToken f6;
   public NodeListOptional f7;
   public NodeListOptional f8;
   public NodeToken f9;
   public Expression f10;
   public NodeToken f11;
   public NodeToken f12;

   public MethodDeclaration(NodeToken n0, Type n1, Identifier n2, NodeToken n3, NodeOptional n4, NodeToken n5, NodeToken n6, NodeListOptional n7, NodeListOptional n8, NodeToken n9, Expression n10, NodeToken n11, NodeToken n12) {
      f0 = n0;
      f1 = n1;
      f2 = n2;
      f3 = n3;
      f4 = n4;
      f5 = n5;
      f6 = n6;
      f7 = n7;
      f8 = n8;
      f9 = n9;
      f10 = n10;
      f11 = n11;
      f12 = n12;
   }

   public MethodDeclaration(Type n0, Identifier n1, NodeOptional n2, NodeListOptional n3, NodeListOptional n4, Expression n5) {
      f0 = new NodeToken("public");
      f1 = n0;
      f2 = n1;
      f3 = new NodeToken("(");
      f4 = n2;
      f5 = new NodeToken(")");
      f6 = new NodeToken("{");
      f7 = n3;
      f8 = n4;
      f9 = new NodeToken("return");
      f10 = n5;
      f11 = new NodeToken(";");
      f12 = new NodeToken("}");
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

