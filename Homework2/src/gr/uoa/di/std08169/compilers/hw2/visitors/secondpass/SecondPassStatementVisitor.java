package gr.uoa.di.std08169.compilers.hw2.visitors.secondpass;

import gr.uoa.di.std08169.compilers.hw2.symbols.Clazz;
import gr.uoa.di.std08169.compilers.hw2.symbols.Type;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.ArrayAssignmentStatement;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.AssignmentStatement;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.Block;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.IfStatement;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.PrintStatement;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.WhileStatement;
import gr.uoa.di.std08169.compilers.hw2.visitor.DepthFirstVisitor;
import java.util.Map;

/**
 *
 * @author labis
 */
public class SecondPassStatementVisitor extends DepthFirstVisitor {
    
    private String clazz;
    private String method;
    private Map<String, Clazz> classes;

    public SecondPassStatementVisitor(String clazz, String method, Map<String, Clazz> classes) {
        this.clazz = clazz;
        this.method = method;
        this.classes = classes;
    }
    
    @Override
    public void visit(Block block) {
        //Gia kathe statement tou block, kalei xana ton eauto tou
        block.f1.accept(this);
    }
    
    @Override
    public void visit(AssignmentStatement assignmentStatement) {
        String identifier = assignmentStatement.f0.f0.tokenImage;
        
        Type type1 = Type.UNKNOWN;
        //Elenxos an uparxei h metavlith mesa sti methodo pou trexei o visitor
        if(classes.get(clazz).getMethods().get(method).getVariables().containsKey(identifier)) {
            type1 = classes.get(clazz).getMethods().get(method).getVariables().get(identifier).getType();
        //Elenxos an uparxei h metavlith mesa stiS metavlites tis klashs pou trexei o visitor 'h se kapoio progono
        } else {
            
            //Psaxnei tous progonous gia metavlites pou klhronomei.
            for(String progonos = clazz; (progonos != null) && classes.containsKey(progonos); 
                    progonos = classes.get(progonos).getParent()) {
                if(classes.get(progonos).getVariables().containsKey(identifier)) {
                    type1 = classes.get(progonos).getVariables().get(identifier).getType();
                    break;
                }
            }
        }

        //Vriskei ton tupo tou expression pou einai sta dexia (tis anatheshs)
        Type type2 = assignmentStatement.f2.accept(new SecondPassExpressionVisitor(clazz, method, classes));
        
        if(type1.equals(Type.UNKNOWN))
            System.err.println("Error: Grammh " + assignmentStatement.f0.f0.beginLine + ": h metavlith " + identifier +
                    " den exei dhlwthei.");
        else if(type2.equals(Type.UNKNOWN))
            System.err.println("Error: Grammh " + assignmentStatement.f1.beginLine + ": den borei na ginei "
                    + "anathesh giati to dexi meros exei tupo " + type2);
        else if((!type1.isClass()) || (!type2.isClass())) {
            //xekinaei psaximo gia aplous tupous
                
            if(!type1.equals(type2)) {
                System.err.println("Error: Grammh " + assignmentStatement.f1.beginLine + ": den borei na ginei "
                    + "anathesh giati to aristero meros einai " + type1 + " kai to dexi " + type2);
            }
        } else if((!type1.isSuperClass(type2, classes)) && (!type1.equals(type2))) {
            System.err.println("Error: Grammh " + assignmentStatement.f1.beginLine + ": den borei na ginei "
                    + "anathesh giati h klash " + type1 + " den einai uperklash ths " + type2);
        }
    }
    
    @Override
    public void visit(ArrayAssignmentStatement arrayAssignmentStatement) {
        String identifier = arrayAssignmentStatement.f0.f0.tokenImage;
        
        Type type1 = Type.UNKNOWN;
        //Elenxos an uparxei h metavlith mesa sti methodo pou trexei o visitor
        if(classes.get(clazz).getMethods().get(method).getVariables().containsKey(identifier)) {
            type1 = classes.get(clazz).getMethods().get(method).getVariables().get(identifier).getType();
        //Elenxos an uparxei h metavlith mesa stis metavlites tis klashs pou trexei o visitor 'h se kapoio progono
        } else {
        
           //Psaxnei tous progonous gia metavlites pou klhronomei.
            for(String progonos = clazz; (progonos != null) && classes.containsKey(progonos); 
                    progonos = classes.get(progonos).getParent()) {

                if(classes.get(progonos).getVariables().containsKey(identifier)) {
                    type1 = classes.get(progonos).getVariables().get(identifier).getType();
                    break;
                }
            }
        
        }

        //Vriskei ton tupo tou expression pou einai mesa ston telesth tou pinaka []
        Type type2 = arrayAssignmentStatement.f2.accept(new SecondPassExpressionVisitor(clazz, method, classes));
        
        //Vriskei ton tupo tou expression pou einai sta dexia (tis anatheshs)
        Type type3 = arrayAssignmentStatement.f5.accept(new SecondPassExpressionVisitor(clazz, method, classes));
        
        if(type1.equals(Type.UNKNOWN))
            System.err.println("Error: Grammh " + arrayAssignmentStatement.f0.f0.beginLine + ": h metavlith " + identifier +
                    " den exei dhlwthei.");
        else if(!type1.equals(Type.ARRAY))
            System.err.println("Error: Grammh " + arrayAssignmentStatement.f1.beginLine + ": o telesths [] apaitei san "
                    + "prwto orisma pinaka, alla vrike " + type1);
        
        if(!type2.equals(Type.INTEGER))
            System.err.println("Error: Grammh " + arrayAssignmentStatement.f1.beginLine + ": o telesths [] apaitei san "
                    + "deutero orisma akeraio, alla vrike " + type2);
        
        if(!type3.equals(Type.INTEGER))
            System.err.println("Error: Grammh " + arrayAssignmentStatement.f4.beginLine + ": den borei na ginei anathesh"
                    + " giati to dexi meros den einai akeraios, alla " + type3);
    }
    
    @Override
    public void visit(IfStatement ifStatement) {
        //Vriskei ton tupo tou expression pou einai mesa sto if
        Type type = ifStatement.f2.accept(new SecondPassExpressionVisitor(clazz, method, classes));
        
        if(!type.equals(Type.BOOLEAN))
            System.err.println("Error: Grammh " + ifStatement.f1.beginLine + ": to if apaitei san orisma boolean "
                    + "alla vrike " + type);  
        
        //Elenxei to statement pou akolouthei to if, stelnontas ton eauto tou
        ifStatement.f4.accept(this);
        
        //Elenxei to statement pou akolouthei to else, stelnontas ton eauto tou
        ifStatement.f6.accept(this);
    }
    
    @Override
    public void visit(WhileStatement whileStatement) {
        //Vriskei ton tupo tou expression pou einai mesa sto while
        Type type = whileStatement.f2.accept(new SecondPassExpressionVisitor(clazz, method, classes));
        
        if(!type.equals(Type.BOOLEAN))
            System.err.println("Error: Grammh " + whileStatement.f1.beginLine + ": to while apaitei san orisma boolean "
                    + "alla vrike " + type);
        
        //Elenxei to statement pou akolouthei to while, stelnontas ton eauto tou
        whileStatement.f4.accept(this);
    }
    
    @Override
    public void visit(PrintStatement printStatement) {
        Type type = printStatement.f2.accept(new SecondPassExpressionVisitor(clazz, method, classes));
        
        if(type.equals(Type.UNKNOWN))
            System.err.println("Error: Grammh " + printStatement.f1.beginLine + ": to System.out.println apaitei "
                    + "san orisma akeraio, pinaka, boolean 'h klash, alla vrike " + type);
    }
}
