package homework6;

import homework6.syntaxtree.Goal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import newpackage.visitors.ProcedureVisitor;
import newpackage.visitors.StmtListVisitor;

/**
 *
 * @author labis
 */
public class Mips {

    public static final int WORD_SIZE = 4;
    public static final int MAX_UNSPILLED_ARGS = 4;
    private static final String KANGA_EXTENSION = ".kg";
    private static final String MIPS_EXTENSION = ".mips";
    
    private final Goal goal;

    public static void main(final String[] arguments) {
        
        for (String argument : arguments) {
            try {
                final Mips mips = new Mips(new File(argument));
                final PrintWriter printWriter = new PrintWriter(new File(argument.replaceFirst(KANGA_EXTENSION, MIPS_EXTENSION)));
                mips.writeCode(printWriter);
                printWriter.close();
            } catch (ParseException e) {
                System.err.println("Error compiling file " + argument + ": " + e.getMessage());
            } catch (final FileNotFoundException e) {
                System.err.println("Error compiling file " + argument + ": " + e.getMessage());
            }
        }
    }
    
    public Mips(final File file) throws FileNotFoundException, homework6.ParseException {
        goal = new KangaParser(new FileInputStream(file)).Goal();
    }
    
    public void writeCode(final PrintWriter printWriter) {
        
        printWriter.println("\t.text");
        printWriter.println("main:");
        int argc = Integer.parseInt(goal.f2.f0.tokenImage);
        //orismata se stoiva
        int spilledArgs = (argc > MAX_UNSPILLED_ARGS) ? argc - MAX_UNSPILLED_ARGS : 0;
        //orismata se stoiva + avaftes metavlhtes
        int kangaStackEntries = Integer.parseInt(goal.f5.f0.tokenImage);
        //avaftes metavlhtes + ra
        int mipsStackEntries = kangaStackEntries + 1 - spilledArgs;
        
        //Krataei xwro sto stack gia auth th methodo
        //stackEntries + 1 = gia tin apothikeush kai tou return address $ra
        printWriter.println("\tadd $sp, $sp, -" + mipsStackEntries * Mips.WORD_SIZE);
        printWriter.println("\tsw $ra, " + Mips.WORD_SIZE + "($sp)");
        
        goal.f10.accept(new StmtListVisitor(mipsStackEntries, spilledArgs), printWriter);
        
        printWriter.println("\tlw $ra, " + Mips.WORD_SIZE + "($sp)");
        printWriter.println("\tadd $sp, $sp, " + mipsStackEntries * Mips.WORD_SIZE);
        printWriter.println("\tjr $ra");
        
        goal.f12.accept(new ProcedureVisitor(), printWriter);
        writeAlloc(printWriter);
        writePrint(printWriter);
        printWriter.println();
    }
    
    public void writeAlloc(final PrintWriter printWriter) {
        
        printWriter.println(".alloc:");
        printWriter.println("\tadd $v0, $0, 9");
        printWriter.println("\tsyscall");
        printWriter.println("\tjr $ra");
    }
    
    public void writePrint(final PrintWriter printWriter) {
        printWriter.println(".print:");
        printWriter.println("\tadd $v0, $0, 1");
        printWriter.println("\tsyscall");
        printWriter.println("\tadd $a0, $0, 10");
        printWriter.println("\tadd $v0, $0, 11");
        printWriter.println("\tsyscall");
        printWriter.println("\tjr $ra");
    }
}
