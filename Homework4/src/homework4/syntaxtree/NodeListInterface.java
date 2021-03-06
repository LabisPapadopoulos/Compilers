//
// Generated by JTB 1.3.2 DIT@UoA patched
//

package homework4.syntaxtree;

/**
 * The interface which NodeList, NodeListOptional, and NodeSequence
 * implement.
 */
public interface NodeListInterface extends Node {
   public void addNode(Node n);
   public Node elementAt(int i);
   public java.util.Enumeration<Node> elements();
   public int size();

   public void accept(homework4.visitor.Visitor v);
   public <R,A> R accept(homework4.visitor.GJVisitor<R,A> v, A argu);
   public <R> R accept(homework4.visitor.GJNoArguVisitor<R> v);
   public <A> void accept(homework4.visitor.GJVoidVisitor<A> v, A argu);
}

