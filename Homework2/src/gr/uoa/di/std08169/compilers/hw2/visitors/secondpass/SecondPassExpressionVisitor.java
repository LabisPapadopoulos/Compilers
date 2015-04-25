package gr.uoa.di.std08169.compilers.hw2.visitors.secondpass;

import gr.uoa.di.std08169.compilers.hw2.symbols.Clazz;
import gr.uoa.di.std08169.compilers.hw2.symbols.Method;
import gr.uoa.di.std08169.compilers.hw2.symbols.Type;
import gr.uoa.di.std08169.compilers.hw2.symbols.Variable;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.AndExpression;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.ArrayLength;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.ArrayLookup;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.CompareExpression;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.MessageSend;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.MinusExpression;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.PlusExpression;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.PrimaryExpression;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.TimesExpression;
import gr.uoa.di.std08169.compilers.hw2.visitor.GJNoArguDepthFirst;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Visitor gia expression
 * @author labis
 */
public class SecondPassExpressionVisitor extends GJNoArguDepthFirst<Type> {
    
    private String clazz;
    private String method;
    private Map<String, Clazz> classes;

    public SecondPassExpressionVisitor(String clazz, String method, Map<String, Clazz> classes) {
        this.clazz = clazz;
        this.method = method;
        this.classes = classes;
    }
    
    @Override
    public Type visit(AndExpression andExpression) {
        
        //Vriskei ton tupo tou 1ou orismatos (PrimaryExpression)
        Type type1 = andExpression.f0.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, classes));
        if(!type1.equals(Type.BOOLEAN))
            System.err.println("Error: Grammh " + andExpression.f1.beginLine + ": o telesths && apaitei san prwto orisma "
                    + "boolean, alla vrike " + type1);
        
        //Vriskei ton tupo tou 2ou orismatos (PrimaryExpression)
        Type type2 = andExpression.f2.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, classes));
        if(!type2.equals(Type.BOOLEAN))
            System.err.println("Error: Grammh " + andExpression.f1.beginLine + ": o telesths && apaitei san deutero orisma "
                    + "boolean, alla vrike " + type2);
        
        //Prepei na einai kai oi duo boolean, alliws lathos
        return (Type.BOOLEAN.equals(type1) && Type.BOOLEAN.equals(type2)) ? Type.BOOLEAN : Type.UNKNOWN;
    }
    
    @Override
    public Type visit(CompareExpression compareExpression) {
        
        Type type1 = compareExpression.f0.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, classes));
        if(!type1.equals(Type.INTEGER))
            System.err.println("Error: Grammh " + compareExpression.f1.beginLine + ": o telesths < apaitei san prwto orisma "
                    + "akeraio, alla vrike " + type1);
        
        Type type2 = compareExpression.f2.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, classes));
        if(!type2.equals(Type.INTEGER))
            System.err.println("Error: Grammh " + compareExpression.f1.beginLine + ": o telesths < apaitei san deutero orisma "
                    + "akeraio, alla vrike " + type2);
        
        //prepei na einai kai oi duo akeraioi, alliws lathos
        return (Type.INTEGER.equals(type1) && Type.INTEGER.equals(type2)) ? Type.BOOLEAN : Type.UNKNOWN;
    }
    
    @Override
    public Type visit(PlusExpression plusExpression) {
        
        Type type1 = plusExpression.f0.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, classes));
        if(!type1.equals(Type.INTEGER))
            System.err.println("Error: Grammh " + plusExpression.f1.beginLine + ": o telesths + apaitei san prwto orisma "
                    + "akeraio, alla vrike " + type1);
        
        Type type2 = plusExpression.f2.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, classes));
        if(!type2.equals(Type.INTEGER))
            System.err.println("Error: Grammh " + plusExpression.f1.beginLine + ": o telesths + apaitei san deutero orisma "
                    + "akeraio, alla vrike " + type2);
        
        //prepei na einai kai oi duo akeraioi, alliws lathos
        return (Type.INTEGER.equals(type1) && Type.INTEGER.equals(type2)) ? Type.INTEGER : Type.UNKNOWN;
        
    }
    
    @Override
    public Type visit(MinusExpression minusExpression) {
        Type type1 = minusExpression.f0.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, classes));
        if(!type1.equals(Type.INTEGER))
            System.err.println("Error: Grammh " + minusExpression.f1.beginLine + ": o telesths - apaitei san prwto orisma "
                    + "akeraio, alla vrike " + type1);
        
        Type type2 = minusExpression.f2.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, classes));
        if(!type2.equals(Type.INTEGER))
            System.err.println("Error: Grammh " + minusExpression.f1.beginLine + ": o telesths - apaitei san deutero orisma "
                    + "akeraio, alla vrike " + type2);
        
        //prepei na einai kai oi duo akeraioi, alliws lathos
        return (Type.INTEGER.equals(type1) && Type.INTEGER.equals(type2)) ? Type.INTEGER : Type.UNKNOWN;
    }
    
    @Override
    public Type visit(TimesExpression timesExpression) {
        Type type1 = timesExpression.f0.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, classes));
        if(!type1.equals(Type.INTEGER))
            System.err.println("Error: Grammh " + timesExpression.f1.beginLine + ": o telesths * apaitei san prwto orisma "
                    + "akeraio, alla vrike " + type1);
        
        Type type2 = timesExpression.f2.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, classes));
        if(!type2.equals(Type.INTEGER))
            System.err.println("Error: Grammh " + timesExpression.f1.beginLine + ": o telesths * apaitei san deutero orisma "
                    + "akeraio, alla vrike " + type2);
        
        //prepei na einai kai oi duo akeraioi, alliws lathos
        return (Type.INTEGER.equals(type1) && Type.INTEGER.equals(type2)) ? Type.INTEGER : Type.UNKNOWN;
    }
    
    @Override
    public Type visit(ArrayLookup arrayLookup) {
        
        Type type1 = arrayLookup.f0.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, classes));
        if(!type1.equals(Type.ARRAY))
            System.err.println("Error: Grammh " + arrayLookup.f1.beginLine + ": o telesths [] apaitei san prwto orisma "
                    + "pinaka, alla vrike " + type1);
        
        Type type2 = arrayLookup.f2.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, classes));
        if(!type2.equals(Type.INTEGER))
            System.err.println("Error: Grammh " + arrayLookup.f1.beginLine + ": o telesths [] apaitei san deutero orisma "
                    + "akeraio, alla vrike " + type2);
        
        //prepei to prwto na einai pinakas kai to deutero akeraios
        return (Type.ARRAY.equals(type1) && Type.INTEGER.equals(type2)) ? Type.INTEGER : Type.UNKNOWN;
    }
    
    
    @Override
    public Type visit(ArrayLength arrayLength) {
        
        Type type = arrayLength.f0.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, classes));
        
        if(type.equals(Type.ARRAY))
            return Type.INTEGER; //epistrefei to mhkos tou pinaka
        
        System.err.println("Error: Grammh " + arrayLength.f1.beginLine + ": o telesths length apaitei san orisma "
                    + "pinaka, alla vrike " + type);
        return Type.UNKNOWN;
    }
    
    @Override
    public Type visit(MessageSend messageSend) {
        Type type = messageSend.f0.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, classes));
        
        String identifier = messageSend.f2.f0.tokenImage;
        
        if(!type.isClass()) { //vasikos typos
            System.err.println("Error: Grmmh " + messageSend.f1.beginLine + ": o telesths . efarmozetai mono "
                    + "se antikeimena kai oxi se " + type);
            return Type.UNKNOWN;
        }
        
        //Psaxnei na vrei tin methodo, xekinwntas apo auth tin klassh kai sunexizontas stous progonous tou
        for(Clazz clazz = classes.get(type.toString()); clazz != null; clazz = classes.get(clazz.getParent())) {
            if(clazz.getMethods().containsKey(identifier)) {
                Method method = clazz.getMethods().get(identifier);
                List<Variable> parameters = method.getParameters();
                List<Type> types = messageSend.f4.accept(new SecondPassExpressionListVisitor(this.clazz, this.method, classes));
                
                //den etrexe kanenas SecondPassExpressionListVisitor epeidh den uphrxan orismata
                if (types == null)
                    types = new ArrayList<Type>();
                
                //elenxos tou plithous twn orismatwn
                if(types.size() != parameters.size()) {
                    System.err.println("Error: Grmmh " + messageSend.f2.f0.beginLine + ": h methodos " + identifier + 
                            " dexetai " + parameters.size() + " orismata, alla klhthike me " + types.size());
                    return Type.UNKNOWN;
                }
                
                boolean correct = true;
                
                //elenxos twn tupwn twn orismatwn ena pros ena
                for(int i = 0; i < parameters.size(); i++) {
                    
                    Type type1 = parameters.get(i).getType();
                    Type type2 = types.get(i);
                    
                    //elenxos an h methodos thelei klash gia to i-osto orisma
                    if(type1.isClass()) {
                        //elenxos an to orisma pou dothike einai upoklash tis klashs pou dhlwthike to orisma
                        if((!type1.isSuperClass(type2, classes)) && (!type1.equals(type2))) {
                            System.err.println("Error: Grmmh " + messageSend.f2.f0.beginLine + ": h methodos " + identifier + 
                                " xreiazetai gia orisma " + (i + 1) + " ena " + type1 + " alla vrike "
                                + type2 + " pou den einai upoklash tou " + type1);
                        
                            correct = false;
                        }
                        
                    } else if(!type2.equals(type1)) {
                        System.err.println("Error: Grmmh " + messageSend.f2.f0.beginLine + ": h methodos " + identifier + 
                            " xreiazetai gia orisma " + (i + 1) + " ena " + type1 + " alla vrike "
                            + type2);
                        
                        correct = false;
                    }
                }
                
                if(correct)
                    return method.getType();
                else
                    return Type.UNKNOWN;
            }
        }
        
        System.err.println("Error: Grmmh " + messageSend.f1.beginLine + ": h methodos " + identifier + 
                " den exei dhlwthei oute stin klash " + classes.get(type.toString()).getName() + 
                " oute se kapoio progono ths");
        return Type.UNKNOWN;
    }
    
    
    @Override
    public Type visit(PrimaryExpression primaryExpression) {
        //kalei ton PrimaryExpressionVisitor gia na elenxei tin ekfrash kai na vrei to typo.
        return primaryExpression.accept(new SecondPassPrimaryExpressionVisitor(clazz, method, classes));
    }
}
