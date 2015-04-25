package homework5.visitors.firstpass;

import homework5.symbols.Graph;
import homework5.symbols.TempInfo;
import homework5.syntaxtree.Procedure;
import homework5.visitor.GJVoidDepthFirst;
import java.util.Map;

/**
 * 
 * @author labis
 */
public class FirstpassProcedureVisitor extends GJVoidDepthFirst<Map<String, Graph>> {
    
    //Visit to procedure me orisma map sunarthsewn
    @Override
    public void visit(final Procedure procedure, final Map<String, Graph> procedures) {
        final String name = procedure.f0.f0.tokenImage;
        final int argc = Integer.valueOf(procedure.f2.f0.tokenImage);
        procedures.put(name, new Graph(argc));
        
        //ta orismata xekinane na zoune apo tin arxh
        for(int i = 0; i < argc; i++) {
            procedures.get(name).addTemp(i, new TempInfo(0));
        }
        
        //apostolh visitor se StmtExp
        procedure.f4.accept(new FirstpassStatementExpressionVisitor(), procedures.get(name));
    }
    
}
