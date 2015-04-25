package gr.uoa.di.std08169.compilers.hw3.visitors.secondpass;

import gr.uoa.di.std08169.compilers.hw3.CompileException;
import gr.uoa.di.std08169.compilers.hw3.Piglet;
import gr.uoa.di.std08169.compilers.hw3.symbols.Clazz;
import gr.uoa.di.std08169.compilers.hw3.symbols.Method;
import gr.uoa.di.std08169.compilers.hw3.symbols.Type;
import gr.uoa.di.std08169.compilers.hw3.symbols.Variable;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.AndExpression;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.ArrayLength;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.ArrayLookup;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.CompareExpression;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.MessageSend;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.MinusExpression;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.PlusExpression;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.PrimaryExpression;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.TimesExpression;
import gr.uoa.di.std08169.compilers.hw3.visitor.GJNoArguDepthFirst;
import java.util.ArrayList;
import java.util.List;

/**
 * Visitor gia expression
 * @author labis
 */
public class SecondPassExpressionVisitor extends GJNoArguDepthFirst<Type> {
    
    private String clazz;
    private String method;
    private Piglet piglet;
    
    public SecondPassExpressionVisitor(String clazz, String method, 
            Piglet piglet) {
        this.clazz = clazz;
        this.method = method;
        this.piglet = piglet;
    }
    
    @Override
    public Type visit(AndExpression andExpression) {
        
//      BEGIN
//          CJUMP LT andExpression.f0 1  label0 //andExpression.f0 < 1 -> 0 (false)
//          MOVE TEMP 0 0 //temp0 = false
//          JUMP label1
//label0    CJUMP LT andExpression.f2 1 label2
//          MOVE TEMP 0 0 //temp0 = false
//          JUMP label1
//label2    MOVE TEMP 0 1 //temp0 = true
//label1    NOOP
//          RETURN TEMP 0
//      END
        
        int temp0 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        String label0 = piglet.getLabel();
        String label1 = piglet.getLabel();
        String label2 = piglet.getLabel();
        
        piglet.getPrintWriter().println("BEGIN //And Expression");
        piglet.getPrintWriter().print("CJUMP LT ");
        
        //Vriskei ton tupo tou 1ou orismatos (PrimaryExpression)
        Type type1 = andExpression.f0.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, piglet));
        if(!type1.equals(Type.BOOLEAN))
            throw new CompileException("Error: Grammh " + andExpression.f1.beginLine + ": o telesths && apaitei san prwto orisma "
                    + "boolean, alla vrike " + type1);
        
        piglet.getPrintWriter().println(" 1 " + label0);
        piglet.getPrintWriter().println("MOVE TEMP " + temp0 + " 0");
        piglet.getPrintWriter().println("JUMP " + label1);
        piglet.getPrintWriter().print(label0 + " CJUMP LT ");
        
        //Vriskei ton tupo tou 2ou orismatos (PrimaryExpression)
        Type type2 = andExpression.f2.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, piglet));
        if(!type2.equals(Type.BOOLEAN))
            throw new CompileException("Error: Grammh " + andExpression.f1.beginLine + ": o telesths && apaitei san deutero orisma "
                    + "boolean, alla vrike " + type2);
        
        piglet.getPrintWriter().println(" 1 " + label2);
        piglet.getPrintWriter().println("MOVE TEMP " + temp0 + " 0");
        piglet.getPrintWriter().println("JUMP " + label1);
        piglet.getPrintWriter().println(label2 + " MOVE TEMP " + temp0 + " 1");
        piglet.getPrintWriter().println(label1 + " NOOP");
        piglet.getPrintWriter().println("RETURN TEMP " + temp0);
        piglet.getPrintWriter().println("END //And Expression");
        return Type.BOOLEAN;
    }
    
    @Override
    public Type visit(CompareExpression compareExpression) {
        
        piglet.getPrintWriter().print("LT ");
        
        Type type1 = compareExpression.f0.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, piglet));
        if(!type1.equals(Type.INTEGER))
            throw new CompileException("Error: Grammh " + compareExpression.f1.beginLine + ": o telesths < apaitei san prwto orisma "
                    + "akeraio, alla vrike " + type1);
        
        piglet.getPrintWriter().print(" ");
        
        Type type2 = compareExpression.f2.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, piglet));
        if(!type2.equals(Type.INTEGER))
            throw new CompileException("Error: Grammh " + compareExpression.f1.beginLine + ": o telesths < apaitei san deutero orisma "
                    + "akeraio, alla vrike " + type2);
        
        return Type.BOOLEAN;
    }
    
    @Override
    public Type visit(PlusExpression plusExpression) {
        
        //Operator Exp Exp
        piglet.getPrintWriter().print("PLUS ");
        
        Type type1 = plusExpression.f0.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, piglet));
        if(!type1.equals(Type.INTEGER))
            throw new CompileException("Error: Grammh " + plusExpression.f1.beginLine + ": o telesths + apaitei san prwto orisma "
                    + "akeraio, alla vrike " + type1);
        piglet.getPrintWriter().print(" ");
        Type type2 = plusExpression.f2.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, piglet));
        if(!type2.equals(Type.INTEGER))
            throw new CompileException("Error: Grammh " + plusExpression.f1.beginLine + ": o telesths + apaitei san deutero orisma "
                    + "akeraio, alla vrike " + type2);
        
        return Type.INTEGER;
        
    }
    
    @Override
    public Type visit(MinusExpression minusExpression) {
        
        piglet.getPrintWriter().print("MINUS ");
        
        Type type1 = minusExpression.f0.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, piglet));
        if(!type1.equals(Type.INTEGER))
            throw new CompileException("Error: Grammh " + minusExpression.f1.beginLine + ": o telesths - apaitei san prwto orisma "
                    + "akeraio, alla vrike " + type1);
        piglet.getPrintWriter().print(" ");
        Type type2 = minusExpression.f2.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, piglet));
        if(!type2.equals(Type.INTEGER))
            throw new CompileException("Error: Grammh " + minusExpression.f1.beginLine + ": o telesths - apaitei san deutero orisma "
                    + "akeraio, alla vrike " + type2);
        
        return Type.INTEGER;
    }
    
    @Override
    public Type visit(TimesExpression timesExpression) {
        
        piglet.getPrintWriter().print("TIMES ");
        
        Type type1 = timesExpression.f0.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, piglet));
        if(!type1.equals(Type.INTEGER))
            throw new CompileException("Error: Grammh " + timesExpression.f1.beginLine + ": o telesths * apaitei san prwto orisma "
                    + "akeraio, alla vrike " + type1);
        piglet.getPrintWriter().print(" ");
        Type type2 = timesExpression.f2.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, piglet));
        if(!type2.equals(Type.INTEGER))
            throw new CompileException("Error: Grammh " + timesExpression.f1.beginLine + ": o telesths * apaitei san deutero orisma "
                    + "akeraio, alla vrike " + type2);
        
        return Type.INTEGER;
    }
    
    @Override
    public Type visit(ArrayLookup arrayLookup) {        
//      BEGIN
//          MOVE TEMP 0 arrayLookup.f0
//          MOVE TEMP 1 arrayLookup.f2
//          CJUMP LT TEMP 0 1 label0 //to arrayLookup.f0 einai to 0 (null)
//          ERROR //NullPointerException
//label0    HLOAD TEMP 2 TEMP 0 0 //Diavasma tou megethous tou pinaka apo tin thesh 0 (tou pinaka)
//          CJUMP LT TEMP 1 TEMP 2 label1 //to arrayLookup.f2 einai mikrotero tou megethous tou pinaka
//          HLOAD TEMP 3 
//                  PLUS TEMP 0 
//                  TIMES PLUS TEMP 1 1 WORD_SIZE 0 //diavazei tin thesh tou pinaka kai tin apothikeuei sto temp1
//          JUMP label2
//label1    ERROR //IndexOutOfBoundsException
//label2    NOOP
//        RETURN TEMP 3
//        END  
        
        int temp0 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        int temp1 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        int temp2 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        int temp3 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        String label0 = piglet.getLabel();
        String label1 = piglet.getLabel();
        String label2 = piglet.getLabel();
        
        piglet.getPrintWriter().println("BEGIN //Array Lookup");
        piglet.getPrintWriter().print("MOVE TEMP " + temp0 + " ");
        
        //pinakas      
        Type type1 = arrayLookup.f0.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, piglet));
        if(!type1.equals(Type.ARRAY))
            throw new CompileException("Error: Grammh " + arrayLookup.f1.beginLine + ": o telesths [] apaitei san prwto orisma "
                    + "pinaka, alla vrike " + type1);
        
        piglet.getPrintWriter().println();
        
        piglet.getPrintWriter().print("MOVE TEMP " + temp1 + " ");
        
        //index (thesh) tou pinaka
        Type type2 = arrayLookup.f2.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, piglet));
        if(!type2.equals(Type.INTEGER))
            throw new CompileException("Error: Grammh " + arrayLookup.f1.beginLine + ": o telesths [] apaitei san deutero orisma "
                    + "akeraio, alla vrike " + type2);

        piglet.getPrintWriter().println();
        
        piglet.getPrintWriter().println("CJUMP LT TEMP " + temp0 + " 1 " + label0);
        piglet.getPrintWriter().println("ERROR //NullPointerException");
        piglet.getPrintWriter().println(label0 + " HLOAD TEMP " + temp2 + " TEMP " + temp0 + " 0");
        piglet.getPrintWriter().println("CJUMP LT TEMP " + temp1 + " TEMP " + temp2 + " " + label1);
        piglet.getPrintWriter().println("HLOAD TEMP " + temp3 + " PLUS TEMP " + temp0 + " TIMES PLUS TEMP " + temp1 + " 1 " + Piglet.WORD_SIZE + " 0");
        piglet.getPrintWriter().println("JUMP " + label2);
        piglet.getPrintWriter().println(label1 + " ERROR //IndexOutOfBoundsException");
        piglet.getPrintWriter().println(label2 + " NOOP");
        piglet.getPrintWriter().println("RETURN TEMP " + temp3);
        piglet.getPrintWriter().println("END //Array Lookup");
        return Type.INTEGER;
    }
    
    
    @Override
    public Type visit(ArrayLength arrayLength) {
//      BEGIN
//          MOVE TEMP 0 arrayLength.f0
//          CJUMP LT TEMP 0 1 label0 //an to arrayLookup.f0 einai < 1 -> 0 (null)
//          ERROR //NullPointerException
//label0    HLOAD TEMP 1 TEMP 0 0 //diavazei to megethos tou pinaka
//          RETURN TEMP 1
//      END
        
        int temp0 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        int temp1 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        String label0 = piglet.getLabel();
        
        piglet.getPrintWriter().println("BEGIN //Array Length");
        piglet.getPrintWriter().print("MOVE TEMP " + temp0 + " ");

        Type type = arrayLength.f0.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, piglet));
        
        if(type.equals(Type.ARRAY)) {
            
            piglet.getPrintWriter().println("CJUMP LT TEMP " + temp0 + " 1 " + label0);
            piglet.getPrintWriter().println("ERROR //NullPointerException");
            piglet.getPrintWriter().println(label0 + " HLOAD TEMP " + temp1 + " TEMP " + temp0 + " 0");
            piglet.getPrintWriter().println("RETURN TEMP " + temp1);
            piglet.getPrintWriter().println("END //Array Length");
            
            return Type.INTEGER; //epistrefei to mhkos tou pinaka
        }
        
        throw new CompileException("Error: Grammh " + arrayLength.f1.beginLine + ": o telesths length apaitei san orisma "
                    + "pinaka, alla vrike " + type);
    }
    
    @Override
    public Type visit(MessageSend messageSend) { //TODO
//      BEGIN  
//          MOVE TEMP 0 messageSend.f0 //to antikeimeno (prin apo tin teleia)
//          CJUMP LT TEMP 0 1 label0 //an einai null to temp 0 (temp0 < 1)
//              ERROR //NullPointerException
//label0    HLOAD TEMP 1 TEMP 0 0 //apothikeush tis dieuthinshs tou v-table
//          HLOAD TEMP 2 TEMP 1 n * 4 //fortwsh tis n-osths methodou tou v-table
//          MOVE TEMP 3 CALL TEMP 2 ( TEMP 0 messageSend.f4 ) //(this, expressionList)
//          RETURN TEMP 3
//      END
        
        int temp0 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        int temp1 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        int temp2 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        int temp3 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        String label0 = piglet.getLabel();

        String identifier = messageSend.f2.f0.tokenImage;
        
        piglet.getPrintWriter().println("BEGIN //Message Send: "  + identifier);
        piglet.getPrintWriter().print("MOVE TEMP " + temp0 + " ");
        Type type = messageSend.f0.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, piglet));
        piglet.getPrintWriter().println();
        if(!type.isClass()) //vasikos typos
            throw new CompileException("Error: Grmmh " + messageSend.f1.beginLine + ": o telesths . efarmozetai mono "
                    + "se antikeimena kai oxi se " + type);

        //Psaxnei na vrei tin methodo, xekinwntas apo auth tin klassh kai sunexizontas stous progonous tou
        for(Clazz clazz = piglet.getClasses().get(type.toString()); clazz != null; clazz = piglet.getClasses().get(clazz.getParent())) {
            if(clazz.getMethods().containsKey(identifier)) {
                Method method = clazz.getMethods().get(identifier);
                List<Variable> parameters = method.getParameters();

                piglet.getPrintWriter().println("CJUMP LT TEMP " + temp0 + " 1 " + label0);
                
                piglet.getPrintWriter().println("ERROR //NullPointerException");

                piglet.getPrintWriter().println(label0 + " HLOAD TEMP " + temp1 + " TEMP " + temp0 + " 0");

                int offset = piglet.getClasses().get(type.toString()).getMethods().get(identifier).getOffset();

                piglet.getPrintWriter().println("HLOAD TEMP " + temp2 + " TEMP " + temp1 + " " + (offset * Piglet.WORD_SIZE));

                piglet.getPrintWriter().print("MOVE TEMP " + temp3 + " CALL TEMP " + temp2 + " ( TEMP " + temp0 + " ");
                
                List<Type> types = messageSend.f4.accept(new SecondPassExpressionListVisitor(this.clazz, this.method, piglet));
                
                piglet.getPrintWriter().println(")");
                
                piglet.getPrintWriter().println("RETURN TEMP " + temp3);
                piglet.getPrintWriter().println("END //Message Send: "  + identifier);
                
                //den etrexe kanenas SecondPassExpressionListVisitor epeidh den uphrxan orismata
                if (types == null)
                    types = new ArrayList<Type>();
                
                //elenxos tou plithous twn orismatwn
                if(types.size() != parameters.size())
                    throw new CompileException("Error: Grmmh " + messageSend.f2.f0.beginLine + ": h methodos " + identifier + 
                            " dexetai " + parameters.size() + " orismata, alla klhthike me " + types.size());
                
                //elenxos twn tupwn twn orismatwn ena pros ena
                for(int i = 0; i < parameters.size(); i++) {
                    
                    Type type1 = parameters.get(i).getType();
                    Type type2 = types.get(i);
                    
                    //elenxos an h methodos thelei klash gia to i-osto orisma
                    if(type1.isClass()) {
                        //elenxos an to orisma pou dothike einai upoklash tis klashs pou dhlwthike to orisma
                        if((!type1.isSuperClass(type2, piglet.getClasses())) && (!type1.equals(type2)))
                            throw new CompileException("Error: Grmmh " + messageSend.f2.f0.beginLine + ": h methodos " + identifier + 
                                " xreiazetai gia orisma " + (i + 1) + " ena " + type1 + " alla vrike "
                                + type2 + " pou den einai upoklash tou " + type1);
                        
                    } else if(!type2.equals(type1))
                        throw new CompileException("Error: Grmmh " + messageSend.f2.f0.beginLine + ": h methodos " + identifier + 
                            " xreiazetai gia orisma " + (i + 1) + " ena " + type1 + " alla vrike "
                            + type2);
                }
                
                
                return method.getType();
            }
        }
        
        throw new CompileException("Error: Grmmh " + messageSend.f1.beginLine + ": h methodos " + identifier + 
                " den exei dhlwthei oute stin klash " + piglet.getClasses().get(type.toString()).getName() + 
                " oute se kapoio progono ths");
    }
    
    
    @Override
    public Type visit(PrimaryExpression primaryExpression) {
        //kalei ton PrimaryExpressionVisitor gia na elenxei tin ekfrash kai na vrei to typo.
        return primaryExpression.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, piglet));
    }
}
