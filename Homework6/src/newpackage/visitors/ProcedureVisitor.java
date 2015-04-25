package newpackage.visitors;

import homework6.Mips;
import homework6.syntaxtree.Procedure;
import homework6.visitor.GJVoidDepthFirst;
import java.io.PrintWriter;

/**
 *
 * @author labis
 */
public class ProcedureVisitor extends GJVoidDepthFirst<PrintWriter> {
    
    @Override
    public void visit(final Procedure procedure, final PrintWriter printWriter) {
        final String label = procedure.f0.f0.tokenImage;//p.x: BT_Start:
        //ola ta orismata
        int argc = Integer.parseInt(procedure.f2.f0.tokenImage);
        //orismata se stoiva
        int spilledArgs = (argc > Mips.MAX_UNSPILLED_ARGS) ? argc - Mips.MAX_UNSPILLED_ARGS : 0;
        //orismata se stoiva + avaftes metavlhtes
        int kangaStackEntries = Integer.parseInt(procedure.f5.f0.tokenImage);
        //avaftes metavlhtes + ra
        int mipsStackEntries = kangaStackEntries + 1 - spilledArgs;

        printWriter.println(label + ":");
        
        //Krataei xwro sto stack gia auth th methodo
        //stackEntries + 1 = gia tin apothikeush kai tou return address $ra
        printWriter.println("\tadd $sp, $sp, -" + mipsStackEntries * Mips.WORD_SIZE);
        printWriter.println("\tsw $ra, " + Mips.WORD_SIZE + "($sp)");
        
        //stelnei visitor gia to StmtList - entoles tis methodou
        procedure.f10.accept(new StmtListVisitor(mipsStackEntries, spilledArgs), printWriter);
        
        //Anapodh douleia, adeiazei to stack apo auth thn eggrafh (dieuthish epistrofhs,
        //orismata pou periseuoun kai avaftes metavlites) auths ths methodu
        
        printWriter.println("\tlw $ra, " + Mips.WORD_SIZE + "($sp)");
        printWriter.println("\tadd $sp, $sp, " + mipsStackEntries * Mips.WORD_SIZE);
        printWriter.println("\tjr $ra");
    }
}
