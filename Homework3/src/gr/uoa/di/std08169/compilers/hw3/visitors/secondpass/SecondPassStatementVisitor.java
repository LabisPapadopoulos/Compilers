package gr.uoa.di.std08169.compilers.hw3.visitors.secondpass;

import gr.uoa.di.std08169.compilers.hw3.CompileException;
import gr.uoa.di.std08169.compilers.hw3.Piglet;
import gr.uoa.di.std08169.compilers.hw3.symbols.Type;
import gr.uoa.di.std08169.compilers.hw3.symbols.Variable;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.ArrayAssignmentStatement;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.AssignmentStatement;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.Block;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.IfStatement;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.PrintStatement;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.WhileStatement;
import gr.uoa.di.std08169.compilers.hw3.visitor.DepthFirstVisitor;
import java.util.List;

/**
 *
 * @author labis
 */
public class SecondPassStatementVisitor extends DepthFirstVisitor {
    
    private String clazz;
    private String method;
    private Piglet piglet;

    public SecondPassStatementVisitor(String clazz, String method, 
            Piglet piglet) {
        this.clazz = clazz;
        this.method = method;
        this.piglet = piglet;
    }
    
    @Override
    public void visit(Block block) {
        //Gia kathe statement tou block, kalei xana ton eauto tou
        block.f1.accept(this);
    }
    
    @Override
    public void visit(AssignmentStatement assignmentStatement) {
//        
//      MOVE TEMP n assignmentStatement.f2 //se periptwsh topikhs metavliths methodou
        
//      HSTORE TEMP 0 n assignmentStatement.f2 //se periptwsh metavliths antikeimenou
//        
//        
        String identifier = assignmentStatement.f0.f0.tokenImage;
        
        piglet.getPrintWriter().println("//assignementStatement");
        
        Type type1 = Type.UNKNOWN;
        //Elenxos an uparxei h metavlith mesa sti methodo pou trexei o visitor
        List<Variable> variables = piglet.getClasses().get(clazz).getMethods().get(method).getVariables();
        for (int i = 0; i < variables.size(); i++) {
            //i-osth metavlith tis methodou
            Variable variable = variables.get(i);
            if(variable.getName().equals(identifier)) {
                type1 = variable.getType();
                
                //pairnei temp pou hdh uparxei afou oi metavlites pou uparxoun
                //einai apothikeumenes seiriaka mesa se mia lista               
                piglet.getPrintWriter().print("MOVE TEMP " + (i + 1) + " ");
                
                break;
            }
        }

        List<String> variableNames = piglet.getClasses().get(clazz).getInheritedVariables(piglet.getClasses());
        
        //Psaxnei oles tis klhronomhmenes metavlites
        for(int i = variableNames.size() - 1; i >= 0; i--) {
            if(variableNames.get(i).equals(identifier)) {
                //
                //an vrethei h metavlith stin thesh i grafei tin lexh i + 1 tou antikeimenou
                piglet.getPrintWriter().print("HSTORE TEMP 0 " + (i + 1) * Piglet.WORD_SIZE + " ");
                
                //Psaxnei tous progonous gia metavlites pou klhronomei.
                for (String progonos = clazz; (progonos != null) && piglet.getClasses().containsKey(progonos) &&
                        (type1 == Type.UNKNOWN); progonos = piglet.getClasses().get(progonos).getParent()) {
                    for (Variable variable : piglet.getClasses().get(progonos).getVariables()) {
                        if (variable.getName().equals(variableNames.get(i))) {
                            type1 = variable.getType();
                            break; //Vrethike h metavlith se kapoio progono xekinwntas apo this
                        }
                    }
                }
            }
        }
        
        //Vriskei ton tupo tou expression pou einai sta dexia (tis anatheshs)
        Type type2 = assignmentStatement.f2.accept(new SecondPassExpressionVisitor(clazz, method, piglet));
        
        piglet.getPrintWriter().println();
        
        if(type1.equals(Type.UNKNOWN))
            throw new CompileException("Error: Grammh " + assignmentStatement.f0.f0.beginLine + ": h metavlith " + identifier +
                    " den exei dhlwthei.");
        else if(type2.equals(Type.UNKNOWN))
            throw new CompileException("Error: Grammh " + assignmentStatement.f1.beginLine + ": den borei na ginei "
                    + "anathesh giati to dexi meros exei tupo " + type2);
        else if((!type1.isClass()) || (!type2.isClass())) {
            //xekinaei psaximo gia aplous tupous
                
            if(!type1.equals(type2)) {
                throw new CompileException("Error: Grammh " + assignmentStatement.f1.beginLine + ": den borei na ginei "
                    + "anathesh giati to aristero meros einai " + type1 + " kai to dexi " + type2);
            }
        } else if((!type1.isSuperClass(type2, piglet.getClasses())) && (!type1.equals(type2))) {
            throw new CompileException("Error: Grammh " + assignmentStatement.f1.beginLine + ": den borei na ginei "
                    + "anathesh giati h klash " + type1 + " den einai uperklash ths " + type2);
        }
    }
    
    @Override
    public void visit(ArrayAssignmentStatement arrayAssignmentStatement) { //TODO    
//          //o pinakas einai topikh metavlhth sunartishs, ara einai hdh kapoio TEMP n
//          MOVE TEMP 1 assignmentStatement.f2 //TEMP 1 = offset tou pinaka
//          CJUMP LT TEMP n 1 label0 //if(TEMP n < 1)
//          ERROR //NullPointerException
//
//label0    HLOAD TEMP 2 TEMP n 0 //TEMP 2 = array.length (apo tin 0 thesh tou pinaka)
//          CJUMP LT TEMP 1 TEMP 2 label1 //if(index < array.length)
//          //prosthesh gia na ginei kataxwrish sto TEMP n gia ton upologismo tou offset  
//          HSTORE PLUS TEMP n TIMES PLUS TEMP 1 1 WORD_SIZE 0
//                  arrayAssignmentStatement.f5 //se periptwsh topikhs metavliths methodou
//          JUMP label2
//label1    ERROR //IndexOutOfBoundsException
//label2    NOOP
      

//          //o pinakas einai pedio antikeimenou, opote einai sth mnhmh
//          HLOAD TEMP 1 TEMP 0 n //TEMP 1 = array
//          MOVE TEMP 2 assignmentStatement.f2 //TEMP 2 = index
//          CJUMP LT TEMP 1 1 label0 //if(array < 1)
//          ERROR //NullPointerException
//label0    HLOAD TEMP 3 TEMP 1 0 //TEMP 3 = array.length
//          CJUMP LT TEMP 2 TEMP 3 label1 //if(index < array.length)
//          HSTORE PLUS TEMP 1 TIMES PLUS TEMP 2 1 WORD_SIZE 0 assignmentStatement.f5 //se periptwsh metavliths (p.x private) enos antikeimenou
//          JUMP label2
//label1    ERROR //IndexOutOfBoundsException
//label2    NOOP
//        

        int temp1 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        int temp2 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        int temp3 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        String label0 = piglet.getLabel();
        String label1 = piglet.getLabel();
        String label2 = piglet.getLabel();
        
        String identifier = arrayAssignmentStatement.f0.f0.tokenImage;
        
        Type type1 = Type.UNKNOWN;
        Type type2 = Type.UNKNOWN;
        Type type3 = Type.UNKNOWN;
        
        List<Variable> variables = piglet.getClasses().get(clazz).getMethods().get(method).getVariables();
        //Elenxos an uparxei h metavlith mesa sti methodo pou trexei o visitor
        for(int i = 0; i < variables.size(); i++) {
            Variable variable = variables.get(i);
            if(variable.getName().equals(identifier)) {
                type1 = variable.getType();
                
                piglet.getPrintWriter().print("MOVE TEMP " + temp1 + " ");
                
                //Vriskei ton tupo tou expression pou einai mesa ston telesth tou pinaka [] - index
                type2 = arrayAssignmentStatement.f2.accept(new SecondPassExpressionVisitor(clazz, method, piglet));
                piglet.getPrintWriter().println();
                
                piglet.getPrintWriter().println("CJUMP LT TEMP " + (i + 1) + " 1 " + label0);
                piglet.getPrintWriter().println("ERROR //NullPointerException");
                
                piglet.getPrintWriter().println(label0 + " HLOAD TEMP " + temp2 + " TEMP " + (i + 1) + " 0");
                piglet.getPrintWriter().println("CJUMP LT TEMP " + temp1 + " TEMP " + temp2 + " " + label1);
                piglet.getPrintWriter().print("HSTORE PLUS TEMP " + (i + 1) + " TIMES PLUS TEMP " + temp1 
                        + " 1 " + Piglet.WORD_SIZE + " 0 ");
                
                //Vriskei ton tupo tou expression pou einai sta dexia (tis anatheshs) - timh pou anathetei
                type3 = arrayAssignmentStatement.f5.accept(new SecondPassExpressionVisitor(clazz, method, piglet));
                piglet.getPrintWriter().println();
                piglet.getPrintWriter().println("JUMP label2");
                piglet.getPrintWriter().println(label1 + " ERROR //IndexOutOfBoundsException");
                piglet.getPrintWriter().println(label2 + " NOOP");
                
                break;
            }
        }
                
        
        
        //Elenxos an uparxei h metavlith mesa stis metavlites tis klashs pou trexei o visitor 'h se kapoio progono
        //Psaxnei tous progonous gia metavlites pou klhronomei.
        for(String progonos = clazz; (progonos != null) && piglet.getClasses().containsKey(progonos) && 
                (type1 == Type.UNKNOWN); progonos = piglet.getClasses().get(progonos).getParent()) {

            
            for(Variable variable : piglet.getClasses().get(progonos).getVariables()) {
                if(variable.getName().equals(identifier)) {
                    type1 = variable.getType();
                    
                    List<String> inheritedVariables = piglet.getClasses().get(clazz).getInheritedVariables(piglet.getClasses());
                    for(int i = (inheritedVariables.size() - 1); i >= 0; i--) {
                        if(inheritedVariables.get(i).equals(identifier)) { //vrethike h metavlith tou antikeimenou
                            piglet.getPrintWriter().println("HLOAD TEMP " + temp1 + " TEMP 0 " + ((i + 1) * Piglet.WORD_SIZE)); //i + 1: logw vTable
                            
                            piglet.getPrintWriter().print("MOVE TEMP " + temp2 + " ");
                            //Vriskei ton tupo tou expression pou einai mesa ston telesth tou pinaka [] - index
                            type2 = arrayAssignmentStatement.f2.accept(new SecondPassExpressionVisitor(clazz, method, piglet));
                            piglet.getPrintWriter().println();
                            
                            piglet.getPrintWriter().println("CJUMP LT TEMP " + temp1 + " 1 " + label0);
                            piglet.getPrintWriter().println("ERROR //NullPointerException");
                            piglet.getPrintWriter().println(label0 + " HLOAD TEMP " + temp3 + " TEMP " + temp1 + " 0");
                            piglet.getPrintWriter().println("CJUMP LT TEMP " + temp2 + " TEMP " + temp3 + " " +  label1);
                            piglet.getPrintWriter().print("HSTORE PLUS TEMP " + temp1 + " TIMES PLUS TEMP " + temp2 
                                    + " 1 " + Piglet.WORD_SIZE + " 0 ");
                            
                            //Vriskei ton tupo tou expression pou einai sta dexia (tis anatheshs) - timh pou anathetei
                            type3 = arrayAssignmentStatement.f5.accept(new SecondPassExpressionVisitor(clazz, method, piglet));
                            piglet.getPrintWriter().println();
                            piglet.getPrintWriter().println("JUMP " + label2);
                            piglet.getPrintWriter().println(label1 + " ERROR //IndexOutOfBoundsException");
                            piglet.getPrintWriter().println(label2 + " NOOP");
                            break;
                        }
                    }
                    break;
                }
            }
        }
        

        if(type1.equals(Type.UNKNOWN))
            throw new CompileException("Error: Grammh " + arrayAssignmentStatement.f0.f0.beginLine + ": h metavlith " + identifier +
                    " den exei dhlwthei.");
        else if(!type1.equals(Type.ARRAY))
            throw new CompileException("Error: Grammh " + arrayAssignmentStatement.f1.beginLine + ": o telesths [] apaitei san "
                    + "prwto orisma pinaka, alla vrike " + type1);
        
        if(!type2.equals(Type.INTEGER))
            throw new CompileException("Error: Grammh " + arrayAssignmentStatement.f1.beginLine + ": o telesths [] apaitei san "
                    + "deutero orisma akeraio, alla vrike " + type2);
        
        if(!type3.equals(Type.INTEGER))
            throw new CompileException("Error: Grammh " + arrayAssignmentStatement.f4.beginLine + ": den borei na ginei anathesh"
                    + " giati to dexi meros den einai akeraios, alla " + type3);
    }
    
    @Override
    public void visit(IfStatement ifStatement) {
        
//          CJUMP ifStatement.f2 label0
//              ifStatement.f4
//          JUMP label1
//label0        ifStatement.f6
//label1    NOOP
        
        String label0 = piglet.getLabel();
        String label1 = piglet.getLabel();
        
        piglet.getPrintWriter().print("CJUMP ");
        
        //Vriskei ton tupo tou expression pou einai mesa sto if
        Type type = ifStatement.f2.accept(new SecondPassExpressionVisitor(clazz, method, piglet));
        
        piglet.getPrintWriter().println(" " + label0 + " //if statement");
        
        if(!type.equals(Type.BOOLEAN))
            throw new CompileException("Error: Grammh " + ifStatement.f1.beginLine + ": to if apaitei san orisma boolean "
                    + "alla vrike " + type);  
        
        //Elenxei to statement pou akolouthei to if, stelnontas ton eauto tou
        ifStatement.f4.accept(this);
        
        piglet.getPrintWriter().println();
        piglet.getPrintWriter().println("JUMP " + label1 + " //else");
        piglet.getPrintWriter().print(label0 + " ");
        
        //Elenxei to statement pou akolouthei to else, stelnontas ton eauto tou
        ifStatement.f6.accept(this);
        
        piglet.getPrintWriter().println();
        piglet.getPrintWriter().println(label1 + " NOOP");
    }
    
    @Override
    public void visit(WhileStatement whileStatement) {
//label0        CJUMP whileStatement.f2 label1
//                  whileStatement.f4     
//                  JUMP label0
//label1        NOOP

        String label0 = piglet.getLabel();
        String label1 = piglet.getLabel();
        
        piglet.getPrintWriter().print(label0 + " CJUMP ");
        
        //Vriskei ton tupo tou expression pou einai mesa sto while
        Type type = whileStatement.f2.accept(new SecondPassExpressionVisitor(clazz, method, piglet));
        
        piglet.getPrintWriter().println(" " + label1 + " //while statement");
        
        if(!type.equals(Type.BOOLEAN))
            throw new CompileException("Error: Grammh " + whileStatement.f1.beginLine + ": to while apaitei san orisma boolean "
                    + "alla vrike " + type);

        //Elenxei to statement pou akolouthei to while, stelnontas ton eauto tou
        whileStatement.f4.accept(this);
        
        piglet.getPrintWriter().println("JUMP " + label0);
        piglet.getPrintWriter().println(label1 + " NOOP");
    }
    
    @Override
    public void visit(PrintStatement printStatement) {
        piglet.getPrintWriter().print("PRINT ");
        Type type = printStatement.f2.accept(new SecondPassExpressionVisitor(clazz, method, piglet));
        piglet.getPrintWriter().println();
        
        if(type.equals(Type.UNKNOWN))
            throw new CompileException("Error: Grammh " + printStatement.f1.beginLine + ": to System.out.println apaitei "
                    + "san orisma akeraio, pinaka, boolean 'h klash, alla vrike " + type);
    }
}
