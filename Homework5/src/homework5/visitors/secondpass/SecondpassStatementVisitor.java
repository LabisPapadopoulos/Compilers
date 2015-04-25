package homework5.visitors.secondpass;

import homework5.symbols.Graph;
import homework5.symbols.Register;
import homework5.symbols.TempInfo;
import homework5.syntaxtree.CJumpStmt;
import homework5.syntaxtree.ErrorStmt;
import homework5.syntaxtree.HLoadStmt;
import homework5.syntaxtree.HStoreStmt;
import homework5.syntaxtree.JumpStmt;
import homework5.syntaxtree.MoveStmt;
import homework5.syntaxtree.NoOpStmt;
import homework5.syntaxtree.PrintStmt;
import homework5.syntaxtree.Stmt;
import homework5.visitor.GJVoidDepthFirst;
import java.io.PrintWriter;

/**
 * Grafei kanga gia statements
 * @author labis
 */
public class SecondpassStatementVisitor extends GJVoidDepthFirst<PrintWriter> {
    
    private final Graph graph;
    private final SecondpassStatementExpressionVisitor secondpassStatementExpressionVisitor;
    
    public SecondpassStatementVisitor(final Graph graph, 
            final SecondpassStatementExpressionVisitor secondpassStatementExpressionVisitor) {
        this.graph = graph;
        this.secondpassStatementExpressionVisitor = secondpassStatementExpressionVisitor;
    }

    @Override
    public void visit(final Stmt stmt, final PrintWriter printWriter) {
        //entoles spiglet
        if(stmt.f0.choice instanceof NoOpStmt) {
            printWriter.println("\tNOOP");
            //gia na proxwrhsei ton metrhth entolwn
            secondpassStatementExpressionVisitor.nextStatement();
        } else if(stmt.f0.choice instanceof ErrorStmt) {
            printWriter.println("\tERROR");
            secondpassStatementExpressionVisitor.nextStatement();
        } else if(stmt.f0.choice instanceof CJumpStmt) {
            final CJumpStmt cJumpStmt = (CJumpStmt) stmt.f0.choice;
            final int temp = Integer.parseInt(cJumpStmt.f1.f1.f0.tokenImage);
            final Register register = graph.getTempInfo(temp).getColour();
            final String label = cJumpStmt.f2.f0.tokenImage;
            
            if(register == null) {
                printWriter.println("\tALOAD v0 SPILLEDARG " + (graph.countSpilledArgs() + 
                        graph.getTempInfo(temp).getStackOffset()));
                printWriter.println("\tCJUMP v0 " + label);
            } else {
                printWriter.println("\tCJUMP " + register + " " + label);
            }
            
            secondpassStatementExpressionVisitor.nextStatement();
        } else if(stmt.f0.choice instanceof JumpStmt) {
            final String label = ((JumpStmt) stmt.f0.choice).f1.f0.tokenImage;
            
            printWriter.println("\tJUMP " + label);
            secondpassStatementExpressionVisitor.nextStatement();
        } else if(stmt.f0.choice instanceof HStoreStmt) {
            final HStoreStmt hStoreStmt = (HStoreStmt) stmt.f0.choice;
            final int temp1 = Integer.parseInt(hStoreStmt.f1.f1.f0.tokenImage);
            final int integerLiteral = Integer.parseInt(hStoreStmt.f2.f0.tokenImage);
            final int temp2 = Integer.parseInt(hStoreStmt.f3.f1.f0.tokenImage);
            
            final Register register1 = graph.getTempInfo(temp1).getColour();
            final Register register2 = graph.getTempInfo(temp2).getColour();

            if((register1 == null) && (register2 == null)) {
                //fortswsh tou TEMP 1 sto v0
                printWriter.println("\tALOAD v0 SPILLEDARG " + 
                        (graph.countSpilledArgs() + graph.getTempInfo(temp1).getStackOffset()));
                
                //fortswsh tou TEMP 2 sto v1
                printWriter.println("\tALOAD v1 SPILLEDARG " + 
                        (graph.countSpilledArgs() + graph.getTempInfo(temp2).getStackOffset()));
                
                printWriter.println("\tHSTORE v0 " + integerLiteral + " v1");
            } else if((register1 == null) && (register2 != null)){
                //fortswsh tou TEMP 1 sto v0
                printWriter.println("\tALOAD v0 SPILLEDARG " + 
                        (graph.countSpilledArgs() + graph.getTempInfo(temp1).getStackOffset()));
                
                printWriter.println("\tHSTORE v0 " + integerLiteral + " " + register2);
            } else if((register2 == null) && (register1 != null)) {
                //fortswsh tou TEMP 2 sto v0
                printWriter.println("\tALOAD v0 SPILLEDARG " + 
                        (graph.countSpilledArgs() + graph.getTempInfo(temp2).getStackOffset()));
                
                printWriter.println("\tHSTORE " + register1 + " " + integerLiteral + " v0");
            } else {
                printWriter.println("\tHSTORE " + register1 + " " + integerLiteral + " " + register2);
            }
            secondpassStatementExpressionVisitor.nextStatement();
        } else if(stmt.f0.choice instanceof HLoadStmt) {
            final HLoadStmt hLoadStmt = (HLoadStmt) stmt.f0.choice;
            final int temp1 = Integer.parseInt(hLoadStmt.f1.f1.f0.tokenImage);
            final int temp2 = Integer.parseInt(hLoadStmt.f2.f1.f0.tokenImage);
            final int integerLiteral = Integer.parseInt(hLoadStmt.f3.f0.tokenImage);
            
            final Register register1 = graph.getTempInfo(temp1).getColour();
            final Register register2 = graph.getTempInfo(temp2).getColour();
            
            if((register1 == null) && (register2 == null)) {
                //fortswsh tou TEMP 2 sto v0 apo to stack
                printWriter.println("\tALOAD v0 SPILLEDARG " + 
                        (graph.countSpilledArgs() + graph.getTempInfo(temp2).getStackOffset()));
                
                //fortwsh sto v1 apo to heap
                printWriter.println("\tHLOAD v1 v0 " + integerLiteral);
                
                //apothikeush tou v1 sto TEMP 1 pou einai sto stack
                printWriter.println("\tASTORE " + (graph.countSpilledArgs() + graph.getTempInfo(temp1).getStackOffset()) + " v1");
            } else if((register1 == null) && (register2 != null)){
                //fortwsh sto v0 apo to heap
                printWriter.println("\tHLOAD v0 " + register2 + " " + integerLiteral);
                
                //apothikeush tou v0 sto TEMP 1 pou einai sto stack
                printWriter.println("\tASTORE " + (graph.countSpilledArgs() + graph.getTempInfo(temp1).getStackOffset()) + " v0");
            } else if((register2 == null) && (register1 != null)) {
                //fortswsh tou TEMP 2 sto v0 apo to stack
                printWriter.println("\tALOAD v0 SPILLEDARG " + 
                        (graph.countSpilledArgs() + graph.getTempInfo(temp2).getStackOffset()));
                
                //fortwsh sto register1 apo to heap
                printWriter.println("\tHLOAD " + register1 + " v0 " + integerLiteral);
            } else {
                printWriter.println("\tHLOAD " + register1 + " " + register2 + " " + integerLiteral);
            }
            
            secondpassStatementExpressionVisitor.nextStatement();
        } else if(stmt.f0.choice instanceof MoveStmt) {
            final MoveStmt moveStmt = (MoveStmt) stmt.f0.choice;
            final int temp = Integer.parseInt(moveStmt.f1.f1.f0.tokenImage);
            final TempInfo tempInfo = graph.getTempInfo(temp);
            
            //grapsimo tou MOVE mono an h metavlhth einai akoma zwntanh
	    if (tempInfo.getEnd() >= secondpassStatementExpressionVisitor.getStatement()) {
		    final Register register = tempInfo.getColour();
		    
		    if(register == null) {
		        //xrhsh proswrinou kataxwrhth v0
		        printWriter.println("\tMOVE v0 " + moveStmt.f2.accept(new SecondpassExpressionVisitor(graph, 
		            secondpassStatementExpressionVisitor), printWriter));
		        //eggrafh tou v0 piso sto stack giati den uparxei diathesimos kataxwrhths
		        printWriter.println("\tASTORE SPILLEDARG " + (graph.countSpilledArgs() + 
		                graph.getTempInfo(temp).getStackOffset()) + " v0");
		    } else {
		        printWriter.println("\tMOVE " + register + " " + moveStmt.f2.accept(new SecondpassExpressionVisitor(graph, 
		            secondpassStatementExpressionVisitor), printWriter));
		    }
            }
            secondpassStatementExpressionVisitor.nextStatement();
        } else if(stmt.f0.choice instanceof PrintStmt) {
            final PrintStmt printStmt = (PrintStmt) stmt.f0.choice;
            
            printWriter.println("\tPRINT " + 
                    printStmt.f1.accept(new SecondpassSimpleExpressionVisitor(graph, "v0"), printWriter));
            secondpassStatementExpressionVisitor.nextStatement();
        }
    }
}
