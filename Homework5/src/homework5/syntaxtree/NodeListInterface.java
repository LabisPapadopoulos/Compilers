//
// Generated by JTB 1.3.2 DIT@UoA patched
//

package homework5.syntaxtree;

/**
 * The interface which NodeList, NodeListOptional, and NodeSequence
 * implement.
 */
public interface NodeListInterface extends Node {
   public void addNode(Node n);
   public Node elementAt(int i);
   public java.util.Enumeration<Node> elements();
   public int size();

   public void accept(homework5.visitor.Visitor v);
   public <R,A> R accept(homework5.visitor.GJVisitor<R,A> v, A argu);
   public <R> R accept(homework5.visitor.GJNoArguVisitor<R> v);
   public <A> void accept(homework5.visitor.GJVoidVisitor<A> v, A argu);
}

