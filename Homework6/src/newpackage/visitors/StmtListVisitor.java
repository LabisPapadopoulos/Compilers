package newpackage.visitors;

import homework6.syntaxtree.Label;
import homework6.syntaxtree.Node;
import homework6.syntaxtree.NodeOptional;
import homework6.syntaxtree.NodeSequence;
import homework6.syntaxtree.Stmt;
import homework6.syntaxtree.StmtList;
import homework6.visitor.GJVoidDepthFirst;
import java.io.PrintWriter;

/**
 *
 * @author labis
 */
public class StmtListVisitor extends GJVoidDepthFirst<PrintWriter> {
    
    private final int mipsStackEntries;
    private final int spilledArgs;
    //orismata pou exoun perasei epipleon apo ta 4
    private int passedArgs;

    public StmtListVisitor(final int mipsStackEntries, final int spilledArgs) {
        this.mipsStackEntries = mipsStackEntries;
        this.spilledArgs = spilledArgs;
        this.passedArgs = 0;
    }
    
    public void increasePassedArgs() {
        passedArgs++;
    }
    
    public int getPassedArgs() {
        int passedArgs = this.passedArgs;
        this.passedArgs = 0;
        return passedArgs;
    }

    @Override
    public void visit(final StmtList stmtList, final PrintWriter printWriter) {
        //Gia kathe zeugari Label, Stmt mesa sto StmtList
        for (Node node : stmtList.f0.nodes) {
            if (node instanceof NodeSequence) {
                
                //Gia kathe Label kai Stmt mesa sto zeugari
                for (Node node2 : ((NodeSequence) node).nodes) {
                    if (node2 instanceof NodeOptional) {
                        final NodeOptional nodeOptional = ((NodeOptional) node2);
                        
                        if(nodeOptional.node instanceof Label) {
                            printWriter.println(((Label) nodeOptional.node).f0.tokenImage + ":");//L2:
                        }

                    } else if (node2 instanceof Stmt) {
                        node2.accept(new StmtVisitor(this, mipsStackEntries, spilledArgs), printWriter);
                    }
                }
            }
        }
    }
}
