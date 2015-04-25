package gr.uoa.di.std08169.compilers.hw2.visitors.secondpass;

import gr.uoa.di.std08169.compilers.hw2.symbols.Clazz;
import gr.uoa.di.std08169.compilers.hw2.symbols.Type;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.AllocationExpression;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.ArrayAllocationExpression;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.BracketExpression;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.FalseLiteral;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.Identifier;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.IntegerLiteral;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.NotExpression;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.ThisExpression;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.TrueLiteral;
import gr.uoa.di.std08169.compilers.hw2.visitor.GJNoArguDepthFirst;
import java.util.Map;

/**
 *
 * @author labis
 */
public class SecondPassPrimaryExpressionVisitor extends GJNoArguDepthFirst<Type> {
    //clazz, method -> dinoun to scope
    private String clazz;
    private String method;
    private Map<String, Clazz> classes;
    
    public SecondPassPrimaryExpressionVisitor(String clazz, String method, Map<String, Clazz> classes) {
        this.clazz = clazz;
        this.method = method;
        this.classes = classes;
    }

    @Override
    public Type visit(IntegerLiteral _) {
        return Type.INTEGER;
    }
    
    @Override
    public Type visit(TrueLiteral _) {
        return Type.BOOLEAN;
    }
    
    @Override
    public Type visit(FalseLiteral _) {
        return Type.BOOLEAN;
    }
    
    @Override
    public Type visit(Identifier identifier) {
        String name = identifier.f0.tokenImage;
        
        //an uparxei to identifier stis metavlites tis methodou
        if(classes.get(clazz).getMethods().get(method).getVariables().containsKey(name)) {
           return classes.get(clazz).getMethods().get(method).getVariables().get(name).getType(); 
        }
        
        //elexnos progonwn gia metavlites pou klhronomountai
        for(String progonos = clazz; (progonos != null) && (classes.containsKey(progonos)); 
                                                    progonos = classes.get(progonos).getParent()) {
            
            //an uparxei to identifier san metavlith klashs
            if(classes.get(progonos).getVariables().containsKey(name)) {
                return classes.get(progonos).getVariables().get(name).getType();
            }
        
        }
        
        //Den exei vrei tin metavlith pou thelei
        System.err.println("Error: Grammh " + identifier.f0.beginLine + ": h metavlith " + name + 
                " den uparxei!");
        
        return Type.UNKNOWN;
    }
    
    @Override
    public Type visit(ThisExpression _) {
        return new Type(clazz);        
    }
    
    @Override
    public Type visit(ArrayAllocationExpression arrayAllocationExpression) {
        // Elenxos oti to megethos tou pinaka pou periexetai sto expression einai akeraios
        Type type = arrayAllocationExpression.f3.accept(new SecondPassExpressionVisitor(clazz, method, classes));
        if(Type.INTEGER.equals(type))
            return Type.ARRAY;
        
        System.err.println("Error: Grammh " + arrayAllocationExpression.f2.beginLine + ": to megethos tou pinaka prepei"
                + "na einai akeraios, alla vrika " + type);
        
        return Type.UNKNOWN;
    }
    
    @Override
    public Type visit(AllocationExpression allocationExpression) {
        String name = allocationExpression.f1.f0.tokenImage;
        
        if(classes.containsKey(name))
            return new Type(allocationExpression.f1.f0.tokenImage);
        
        System.err.println("Error: Grammh " + allocationExpression.f1.f0.beginLine + ": h klassh " + name + 
                    " den exei dhlwthei!");
        return Type.UNKNOWN;
    }
    
    @Override
    public Type visit(NotExpression notExpression) {
        
        Type type = notExpression.f1.accept(new SecondPassExpressionVisitor(clazz, method, classes));
        if(Type.BOOLEAN.equals(type))
            return Type.BOOLEAN;
        
        System.err.println("Error: Grammh " + notExpression.f0.beginLine + ": o telesths ! apaitei orisma boolean "
                + "alla vrike " + type);
        return Type.UNKNOWN;
    }
    
    @Override
    public Type visit(BracketExpression bracketExpression) {
        return bracketExpression.f1.accept(new SecondPassExpressionVisitor(clazz, method, classes));
    }
}
