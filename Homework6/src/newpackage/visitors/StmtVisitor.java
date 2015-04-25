package newpackage.visitors;

import homework6.Mips;
import homework6.syntaxtree.ALoadStmt;
import homework6.syntaxtree.AStoreStmt;
import homework6.syntaxtree.BinOp;
import homework6.syntaxtree.CJumpStmt;
import homework6.syntaxtree.CallStmt;
import homework6.syntaxtree.ErrorStmt;
import homework6.syntaxtree.HAllocate;
import homework6.syntaxtree.HLoadStmt;
import homework6.syntaxtree.HStoreStmt;
import homework6.syntaxtree.IntegerLiteral;
import homework6.syntaxtree.JumpStmt;
import homework6.syntaxtree.Label;
import homework6.syntaxtree.MoveStmt;
import homework6.syntaxtree.NoOpStmt;
import homework6.syntaxtree.PassArgStmt;
import homework6.syntaxtree.PrintStmt;
import homework6.syntaxtree.Reg;
import homework6.syntaxtree.SimpleExp;
import homework6.syntaxtree.Stmt;
import homework6.visitor.GJVoidDepthFirst;
import java.io.PrintWriter;

/**
 *
 * @author labis
 */
public class StmtVisitor extends GJVoidDepthFirst<PrintWriter> {
    
    private static final String V0 = "v0";
    private static final String V1 = "v1";
    private static final int MAX_INT = 32768;
    
    private final StmtListVisitor stmtListVisitor;
    private final int mipsStackEntries;
    private final int spilledArgs;

    public StmtVisitor(final StmtListVisitor stmtListVisitor, 
            final int mipsStackEntries, final int spilledArgs) {
        this.stmtListVisitor = stmtListVisitor;
        this.mipsStackEntries = mipsStackEntries;
        this.spilledArgs = spilledArgs;
    }

    @Override
    public void visit(final Stmt stmt, final PrintWriter printWriter) {
        if (stmt.f0.choice instanceof NoOpStmt) {
            printWriter.println("\t\t\t# NOOP");
        } else if (stmt.f0.choice instanceof ErrorStmt) {
            printWriter.println("\tadd $v0, $0, 10\t# ERROR");
            printWriter.println("\tsyscall");
        } else if (stmt.f0.choice instanceof CJumpStmt) {
            final CJumpStmt cJumpStmt = (CJumpStmt) stmt.f0.choice;
            final String register = cJumpStmt.f1.f0.choice.toString();
            final String label = cJumpStmt.f2.f0.tokenImage;
            printWriter.println("\tbne $" + register + ", 1, " + label + "\t# CJUMP");
        } else if (stmt.f0.choice instanceof JumpStmt) {
            final String label = ((JumpStmt) stmt.f0.choice).f1.f0.tokenImage;
            printWriter.println("\tj " + label + "\t# JUMP");
        } else if (stmt.f0.choice instanceof HStoreStmt) {
            final HStoreStmt hStoreStmt = (HStoreStmt) stmt.f0.choice;
            final String register = hStoreStmt.f1.f0.choice.toString();
            final int integer = Integer.parseInt(hStoreStmt.f2.f0.tokenImage);
            final String register2 = hStoreStmt.f3.f0.choice.toString();
            
            //afou einai dieUthinsh den mporei na einai parapanw apo MAX_INT
            printWriter.println("\tsw $" + register2 + ", " + integer + "($" + register + ")\t# HSTORE");
        } else if (stmt.f0.choice instanceof HLoadStmt) {
            final HLoadStmt hLoadStmt = (HLoadStmt) stmt.f0.choice;
            final String register = hLoadStmt.f1.f0.choice.toString();
            final String register2 = hLoadStmt.f2.f0.choice.toString();
            final int integer = Integer.parseInt(hLoadStmt.f3.f0.tokenImage);
            
            //afou einai dieUthinsh den mporei na einai parapanw apo MAX_INT
            printWriter.println("\tlw $" + register + ", " + integer + "($" + register2 + ")\t# HLOAD");
        } else if (stmt.f0.choice instanceof MoveStmt) {
            final MoveStmt moveStmt = (MoveStmt) stmt.f0.choice;
            final String register = moveStmt.f1.f0.choice.toString();
            if (moveStmt.f2.f0.choice instanceof HAllocate) {
                final SimpleExp simpleExp = ((HAllocate) moveStmt.f2.f0.choice).f1;
                //metaferei to SimpleExp ston $a0 kai kalei syscall 9 gia na desmeusei mnhmh
                if (simpleExp.f0.choice instanceof Reg) {
                    final String register2 = ((Reg) simpleExp.f0.choice).f0.choice.toString();
                    
                    printWriter.println("\tmove $a0, $" + register2 + "\t# MOVE");
                } else if (simpleExp.f0.choice instanceof IntegerLiteral) {
                    final int integer = Integer.parseInt(((IntegerLiteral) simpleExp.f0.choice).f0.tokenImage);
                    
                    if (integer > MAX_INT) {
                        //ginetai fortwsh me li giati einai megalo noumeo
                        //xrhsimopoieitai o kataxwrhths a0 gia na ginei allocate meta
                        printWriter.println("\tli $a0, " + integer + "\t# MOVE");
                    } else {
                        printWriter.println("\taddi $a0, $0, " + integer + "\t# MOVE");
                    }
                } else if (simpleExp.f0.choice instanceof Label) {
                    final String label = ((Label) simpleExp.f0.choice).f0.tokenImage;

                    printWriter.println("\tla $a0, " + label + "\t# MOVE");
                }
                printWriter.println("\tjal .alloc");
                printWriter.println("\tmove $" + register + ", $v0");
            } else if (moveStmt.f2.f0.choice instanceof BinOp) {
                final BinOp binOp = (BinOp) moveStmt.f2.f0.choice;
                final String operator = binOp.f0.f0.choice.toString();
                final String register2 = binOp.f1.f0.choice.toString();
                
                //SimpleExp
                if (binOp.f2.f0.choice instanceof Reg) {
                    final String register3 = ((Reg) binOp.f2.f0.choice).f0.choice.toString();
                    
                    if(operator.equals("LT")) {
                        printWriter.println("\tslt $" + register + ", $" + register2 + ", $" + register3 + "\t# MOVE");
                    } else if(operator.equals("PLUS")) {
                        printWriter.println("\tadd $" + register + ", $" + register2 + ", $" + register3 + "\t# MOVE");
                    } else if(operator.equals("MINUS")) {
                        printWriter.println("\tsub $" + register + ", $" + register2 + ", $" + register3 + "\t# MOVE");
                    } else if(operator.equals("TIMES")) {
                        printWriter.println("\tmul $" + register + ", $" + register2 + ", $" + register3 + "\t# MOVE");
                    }
                    
                } else if (binOp.f2.f0.choice instanceof IntegerLiteral) {
                    final int integer = Integer.parseInt(((IntegerLiteral) binOp.f2.f0.choice).f0.tokenImage);
                    
                    if (integer > MAX_INT) {
                        //Afou to teliko apotelesma tha paei ston register, einai asfales na xrhsimopoihthei kai gia to li
                        printWriter.println("\tli $" + register + ", " + integer + "\t# MOVE");
                        
                        if(operator.equals("LT")) {
                            printWriter.println("\tslt $" + register + ", $" + register2 + ", $" + register);
                        } else if(operator.equals("PLUS")) {
                            printWriter.println("\tadd $" + register + ", $" + register2 + ", $" + register);
                        } else if(operator.equals("MINUS")) {
                            printWriter.println("\tsub $" + register + ", $" + register2 + ", $" + register);
                        } else if(operator.equals("TIMES")) {
                            printWriter.println("\tmul $" + register + ", $" + register2 + ", $" + register);
                        }
                    } else {
                        if(operator.equals("LT")) {
                            printWriter.println("\tslti $" + register + ", $" + register2 + ", " + integer + "\t# MOVE");
                        } else if(operator.equals("PLUS")) {
                            printWriter.println("\taddi $" + register + ", $" + register2 + ", " + integer + "\t# MOVE");
                        } else if(operator.equals("MINUS")) {
                            printWriter.println("\taddi $" + register + ", $" + register2 + ", " + (-integer) + "\t# MOVE");
                        } else if(operator.equals("TIMES")) {
                            final String register3 = register2.equals(V0) ? V1 : V0;
                            printWriter.println("\taddi $" + register3 + ", $0, " + integer + "\t# MOVE");
                            printWriter.println("\tmul $" + register + ", $" + register2 + ", $" + register3);
                        }
                    }
                    
                } else if (binOp.f2.f0.choice instanceof Label) {
                    final String label = ((Label) binOp.f2.f0.choice).f0.tokenImage;
                    //elenxos an xrisimopoieitai o v0, tha xrhsimopoihthei proswrina o v1, alliws o v0
                    final String register3 = register2.equals(V0) ? V1 : V0;
                    
                    printWriter.println("\tla $" + register3 + ", " + label + "\t# MOVE");
                    
                    if(operator.equals("LT")) {
                        printWriter.println("\tslt $" + register + ", $" + register2 + ", $" + register3);
                    } else if(operator.equals("PLUS")) {
                        printWriter.println("\tadd $" + register + ", $" + register2 + ", $" + register3);
                    } else if(operator.equals("MINUS")) {
                        printWriter.println("\tsub $" + register3 + ", $0, $" + register3); // register3 = -register3
                        printWriter.println("\tadd $" + register + ", $" + register2 + ", $" + register3);
                    } else if(operator.equals("TIMES")) {
                        printWriter.println("\tmul $" + register + ", $" + register2 + ", $" + register3);
                    }
                }
                
            } else if (moveStmt.f2.f0.choice instanceof SimpleExp) {
                final SimpleExp simpleExp = (SimpleExp)moveStmt.f2.f0.choice;
                if (simpleExp.f0.choice instanceof Reg) {
                    final String register2 = ((Reg) simpleExp.f0.choice).f0.choice.toString();
                    
                    printWriter.println("\tmove $" + register + ", $" + register2 + "\t# MOVE");
                    
                } else if (simpleExp.f0.choice instanceof IntegerLiteral) {
                    final int integer = Integer.parseInt(((IntegerLiteral) simpleExp.f0.choice).f0.tokenImage);
                 
                    if(integer > MAX_INT) {
                        printWriter.println("\tli $" + register + ", " + integer + "\t# MOVE");
                    } else {
                        printWriter.println("\taddi $" + register + ", $0, " + integer + "\t# MOVE");
                    }
                } else if (simpleExp.f0.choice instanceof Label) {
                    final String label = ((Label) simpleExp.f0.choice).f0.tokenImage;
                    printWriter.println("\tla $" + register + ", " + label + "\t# MOVE");
                }
            }
            
        } else if (stmt.f0.choice instanceof PrintStmt) {
            final SimpleExp simpleExp = ((PrintStmt) stmt.f0.choice).f1;
            //metaferei to SimpleExp ston $a0 kai kalei syscall 1 gia na tupwsei akeraio
            if (simpleExp.f0.choice instanceof Reg) {
                final String register = ((Reg) simpleExp.f0.choice).f0.choice.toString();

                printWriter.println("\tmove $a0, $" + register + "\t# PRINT");

            } else if (simpleExp.f0.choice instanceof IntegerLiteral) {
                final int integer = Integer.parseInt(((IntegerLiteral) simpleExp.f0.choice).f0.tokenImage);

                printWriter.println("\taddi $a0, $0, " + integer + "\t# PRINT");

            } else if (simpleExp.f0.choice instanceof Label) {
                final String label = ((Label) simpleExp.f0.choice).f0.tokenImage;
                
                printWriter.println("\tla $a0, " + label + "\t# PRINT");
            }
            printWriter.println("\tjal .print");
        } else if (stmt.f0.choice instanceof ALoadStmt) {
            //fortwnontai apo to stack kai orismata sunarthsewn kai avaftes metavlhtes
            final ALoadStmt aLoadStmt = (ALoadStmt) stmt.f0.choice;
            final String register = aLoadStmt.f1.f0.choice.toString();
            final int integer = Integer.parseInt(aLoadStmt.f2.f1.f0.tokenImage);
            
            //afou o integer einai dieuthinsh den tha einai megalo noumero (tha einai mexri 2^15 - 1 = 32767)
            //- giati megalwnei pros ta panw
            printWriter.println("\tlw $" + register + ", " + ((mipsStackEntries - (integer - spilledArgs)) * Mips.WORD_SIZE) + "($sp)\t# ALOAD");
        } else if (stmt.f0.choice instanceof AStoreStmt) {
            //apothikeush sto stack avaftes metavlites (kai orismata)
            final AStoreStmt aStoreStmt = (AStoreStmt) stmt.f0.choice;
            //noumero tou spilledArg
            final int integer = Integer.parseInt(aStoreStmt.f1.f1.f0.tokenImage);
            final String register = aStoreStmt.f2.f0.choice.toString();
            
            //afou o integer einai dieuthinsh den tha einai megalo noumero (tha einai mexri 2^15 - 1 = 32767)
            //integer - spilledArgs = afairountai ta pragmatika spilledArgs pou perasan me PASSARG, 
            //opote einai oi avaftes metavlites. An to integer einai mikro kai antistoixei se pragmatiko spilledArg
            //tote h diafora tha vgei arnhtikh. Opote afairontas thn apo ta mipsStackEntries tha prostethoun kai tha paei
            //pio katw sti stoiva sto xwro pou desmeuse oxi h trexousa alla h kalousa sunartish gia ta orismata.
            printWriter.println("\tsw $" + register + ", " + ((mipsStackEntries - (integer - spilledArgs)) * Mips.WORD_SIZE) + "($sp)\t# ASTORE");
        } else if (stmt.f0.choice instanceof PassArgStmt) {
            //apothikeush sto stack orismata sunarthsewn
            final PassArgStmt passArgStmt = (PassArgStmt) stmt.f0.choice;
            final int integer = Integer.parseInt(passArgStmt.f1.f0.tokenImage);
            final String register = passArgStmt.f2.f0.choice.toString();
            
            //afou o integer einai dieuthinsh den tha einai megalo noumero (tha einai mexri 2^15 - 1 = 32767)
            printWriter.println("\tsw $" + register + ", " + (-(integer - 1) * Mips.WORD_SIZE) + "($sp)\t# PASSARG");
            
            //metraei ta passArgs
            stmtListVisitor.increasePassedArgs();
        } else if (stmt.f0.choice instanceof CallStmt) {
            final SimpleExp simpleExp = ((CallStmt) stmt.f0.choice).f1;
            //metaferei to SimpleExp ston $a0 kai kalei syscall 1 gia na tupwsei akeraio
            if (simpleExp.f0.choice instanceof Reg) {
                final String register = ((Reg) simpleExp.f0.choice).f0.choice.toString();
                final int passedArgs = stmtListVisitor.getPassedArgs();
                
                //anoigei xwro sto stack gia ta orismata pou tha exei mazepsei
                printWriter.println("\tadd $sp, $sp, -" + passedArgs * Mips.WORD_SIZE + "\t# CALL");
                printWriter.println("\tjalr $" + register);
                printWriter.println("\tadd $sp, $sp, " + passedArgs * Mips.WORD_SIZE);

            } else if (simpleExp.f0.choice instanceof IntegerLiteral) {
                final int integer = Integer.parseInt(((IntegerLiteral) simpleExp.f0.choice).f0.tokenImage);
                
                //afou o integer einai label den tha einai megalo noumero (tha einai mexri 2^15 - 1 = 32767)
                printWriter.println("\tjal " + integer + "\t# CALL");
            } else if (simpleExp.f0.choice instanceof Label) {
                final String label = ((Label) simpleExp.f0.choice).f0.tokenImage;
                
                printWriter.println("\tjal " + label + "\t# CALL");
            }
        }
    }
}
