package gr.uoa.di.std08169.compilers.hw3.visitors.secondpass;

import gr.uoa.di.std08169.compilers.hw3.CompileException;
import gr.uoa.di.std08169.compilers.hw3.Piglet;
import gr.uoa.di.std08169.compilers.hw3.symbols.Clazz;
import gr.uoa.di.std08169.compilers.hw3.symbols.Method;
import gr.uoa.di.std08169.compilers.hw3.symbols.Type;
import gr.uoa.di.std08169.compilers.hw3.symbols.Variable;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.AllocationExpression;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.ArrayAllocationExpression;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.BracketExpression;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.FalseLiteral;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.Identifier;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.IntegerLiteral;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.NotExpression;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.ThisExpression;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.TrueLiteral;
import gr.uoa.di.std08169.compilers.hw3.visitor.GJNoArguDepthFirst;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 *
 * @author labis
 */
public class SecondPassPrimaryExpressionVisitor extends GJNoArguDepthFirst<Type> {
    //clazz, method -> dinoun to scope
    private String clazz;
    private String method;
    private Piglet piglet;
    
    public SecondPassPrimaryExpressionVisitor(String clazz, String method, 
            Piglet piglet) {
        this.clazz = clazz;
        this.method = method;
        this.piglet = piglet;
    }

    @Override
    public Type visit(IntegerLiteral integerLiteral) {
        piglet.getPrintWriter().print(integerLiteral.f0.tokenImage + " ");
        return Type.INTEGER;
    }
    
    @Override
    public Type visit(TrueLiteral _) {
        //boolean true se Piglet
        piglet.getPrintWriter().print("1 ");
        return Type.BOOLEAN;
    }
    
    @Override
    public Type visit(FalseLiteral _) {
        //boolean false se Piglet
        piglet.getPrintWriter().print("0 ");
        return Type.BOOLEAN;
    }
    
    @Override
    public Type visit(Identifier identifier) {
        String name = identifier.f0.tokenImage;
    
        int temp = 1; //0 einai to this
        for(Variable variable : piglet.getClasses().get(clazz).getMethods().get(method).getVariables()) {
            if(variable.getName().equals(name)) {
                //antistoixhsh metavliths tis methodou se temp..
                piglet.getPrintWriter().print("TEMP " + temp);
                return variable.getType();
            }
            temp++;
        }
        
//      BEGIN
//          HLOAD TEMP 1 TEMP 0 variableOffset
//          RETURN TEMP 1
//      END
        
        
        List<String> variables = piglet.getClasses().get(clazz).getInheritedVariables(piglet.getClasses());
        int temp1 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        
        for(int i = variables.size() - 1; i >= 0; i--) { //skanarisma ths listas anapoda
            if(variables.get(i).equals(name)) {
                //an vrethei h metavlith stin thesh i fortwnei tin lexh i + 1 tou antikeimenou
                piglet.getPrintWriter().println("BEGIN //identifier " + name);
                                                                                //logo vTable
                piglet.getPrintWriter().println("HLOAD TEMP " + temp1 + " TEMP 0 " + ((i + 1) * Piglet.WORD_SIZE));
                piglet.getPrintWriter().println("RETURN TEMP " + temp1);
                piglet.getPrintWriter().println("END //identifier " + name);
                for (String progonos = clazz; (progonos != null) && piglet.getClasses().containsKey(progonos);
                        progonos = piglet.getClasses().get(progonos).getParent()) {
                    for (Variable variable : piglet.getClasses().get(progonos).getVariables()) {
                        if (variable.getName().equals(variables.get(i)))
                            return variable.getType();
                    }
                            
                }
            }
        }
                
        //Den exei vrei tin metavlith pou thelei
        throw new CompileException("Error: Grammh " + identifier.f0.beginLine + ": h metavlith " + name + 
                " den uparxei!");
    }
    
    @Override
    public Type visit(ThisExpression _) {
        
        piglet.getPrintWriter().print("TEMP 0 ");
        
        return new Type(clazz);        
    }
    
    @Override
    public Type visit(ArrayAllocationExpression arrayAllocationExpression) {
//        BEGIN
//          MOVE TEMP 0 arrayAllocationExpression.f3
//          MOVE TEMP 1 HALLOCATE TIMES 4 PLUS 1 TEMP 0
//          HSTORE TEMP 1 0 TEMP 0
//        RETURN TEMP 1
//        END
        
        int temp0 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        int temp1 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        
        piglet.getPrintWriter().println("BEGIN //Array Allocation Expression");
        piglet.getPrintWriter().print("MOVE TEMP " + temp0 + " ");
        // Elenxos oti to megethos tou pinaka pou periexetai sto expression einai akeraios
        Type type = arrayAllocationExpression.f3.accept(new SecondPassExpressionVisitor(clazz, method, piglet));
        piglet.getPrintWriter().println();
        
        //Desmeush enos pinaka akeraiwn me megethos 4 * N bytes
        piglet.getPrintWriter().println("MOVE TEMP " + temp1 + 
                " HALLOCATE TIMES " + Piglet.WORD_SIZE + " PLUS 1 TEMP " + temp0);
                                                        //thesh 0 tou pinaka
        piglet.getPrintWriter().println("HSTORE TEMP " + temp1 + " 0 TEMP " + temp0);
        piglet.getPrintWriter().println("RETURN TEMP " + temp1);
        piglet.getPrintWriter().println("END //Array Allocation Expression");
        if(Type.INTEGER.equals(type))
            return Type.ARRAY;
        
        throw new CompileException("Error: Grammh " + arrayAllocationExpression.f2.beginLine + ": to megethos tou pinaka prepei"
                + "na einai akeraios, alla vrika " + type);
    }
    
    @Override
    public Type visit(AllocationExpression allocationExpression) {
//      BEGIN
//          MOVE TEMP 0 HALLOCATE size
//          MOVE TEMP 1 HALLOCATE vTableSize //desmeush xwrou gia to v-table (temp0 = 4 bytes)
//          HSTORE TEMP 1 0 Class_method0 //epanalipsh gia kathe methodo
//          HSTORE TEMP 1 4 Class_method1 //epanalipsh gia kathe methodo
//          ...
//          HSTORE TEMP 1 n Class_methodN //epanalipsh gia kathe methodo
//          HSTORE TEMP 0 0 TEMP 1
//          HSTORE TEMP 0 4 Class_variable0
//          HSTORE TEMP 0 8 Class_varianle1
//          ...
//          HSTORE TEMP 0 n Class_variableN
//          RETURN TEMP 0
//      END
        
        String name = allocationExpression.f1.f0.tokenImage;
        
        if(piglet.getClasses().containsKey(name)) {
            //Apo to symbol table sto v-table
            //oles oi klhronomoumenes methodoi gia to vTable
            SortedMap<String, String> vTable = piglet.getClasses().get(name).getInheritedMethods(piglet.getClasses());
            List<String> variables = piglet.getClasses().get(name).getInheritedVariables(piglet.getClasses());
            int temp0 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
            int temp1 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
                  
            piglet.getPrintWriter().println("BEGIN //" + name + " instance");
            //desmeush tou antikeimenou (vTable + metavlites)
            piglet.getPrintWriter().println("MOVE TEMP " + temp0 + " HALLOCATE " + (variables.size() + 1) * Piglet.WORD_SIZE);
            //desmeush tou idiou tou vTable
            piglet.getPrintWriter().println("MOVE TEMP " + temp1 + " HALLOCATE " + vTable.size() * Piglet.WORD_SIZE);
            
            int offset = 0;
            //apothikeush twn methodwn sto vTable
            for(Map.Entry<String, String> entry : vTable.entrySet()) {
                //offset se bytes pou tha bei mesa sto vTable h methodos
                for(String progonos = name; (progonos != null) && (piglet.getClasses().containsKey(progonos));
                        progonos = piglet.getClasses().get(progonos).getParent()) {
                    if(piglet.getClasses().get(progonos).getMethods().containsKey(entry.getKey())) {
                        offset = piglet.getClasses().get(progonos).getMethods().get(entry.getKey()).getOffset() * Piglet.WORD_SIZE;
                        break;
                    }
                }
                 
                piglet.getPrintWriter().println("HSTORE TEMP " + temp1 + " " + offset + " " + entry.getValue());
            }            
            
            //apoithikeush sto antikeimeno to vTable (stin arxh tou)
            piglet.getPrintWriter().println("HSTORE TEMP " + temp0 + " 0 TEMP " + temp1);//offset 0
            
            //to offset twn metavlitwn xekinane apo to 4
            offset = Piglet.WORD_SIZE;
            for(String variable : variables) {
                piglet.getPrintWriter().println("HSTORE TEMP " + temp0 + " " + offset + " 0 // " + variable);
                offset += Piglet.WORD_SIZE;
            }
            piglet.getPrintWriter().println("RETURN TEMP " + temp0);
            piglet.getPrintWriter().println("END //" + name + " instance");
            
            return new Type(allocationExpression.f1.f0.tokenImage);
        }
        throw new CompileException("Error: Grammh " + allocationExpression.f1.f0.beginLine + ": h klassh " + name + 
                    " den exei dhlwthei!");
    }
    
    @Override
    public Type visit(NotExpression notExpression) {
//      BEGIN
//          CJUMP notExpression.f1 label0
//          MOVE TEMP 0 1
//          JUMP label1
//label0    MOVE TEMP 0 0
//label1    NOOP
//      RETURN TEMP 0
//      END
        int temp1 = piglet.getClasses().get(clazz).getMethods().get(method).getTemp();
        String label1 = piglet.getLabel();
        String label2 = piglet.getLabel();
        piglet.getPrintWriter().println("BEGIN //Not Expression");
        piglet.getPrintWriter().print("CJUMP ");
        Type type = notExpression.f1.accept(new SecondPassExpressionVisitor(clazz, method, piglet));
        
        //an einai true, diakladwnei sto label1
        if(Type.BOOLEAN.equals(type)) {
            piglet.getPrintWriter().println(label1);
            //to not expression.f1 einai false, opote to 
            //not expression apothikeuetai stin temp1 true.
            piglet.getPrintWriter().println("MOVE TEMP " + temp1 + " 1");
            piglet.getPrintWriter().println("JUMP " + label2);
            piglet.getPrintWriter().println(label1 + " MOVE TEMP " + temp1 + " 0");
            piglet.getPrintWriter().println(label2 + " NOOP");
            piglet.getPrintWriter().println("RETURN TEMP " + temp1);
            piglet.getPrintWriter().println("END //Not Expression");
            return Type.BOOLEAN;
        }
        
        throw new CompileException("Error: Grammh " + notExpression.f0.beginLine + ": o telesths ! apaitei orisma boolean "
                + "alla vrike " + type);
    }
    
    @Override
    public Type visit(BracketExpression bracketExpression) {
        return bracketExpression.f1.accept(new SecondPassExpressionVisitor(clazz, method, piglet));
    }
}
