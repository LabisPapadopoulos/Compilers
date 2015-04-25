package gr.uoa.di.std08169.compilers.hw3.visitors.secondpass;

import gr.uoa.di.std08169.compilers.hw3.CompileException;
import gr.uoa.di.std08169.compilers.hw3.Piglet;
import gr.uoa.di.std08169.compilers.hw3.symbols.Clazz;
import gr.uoa.di.std08169.compilers.hw3.symbols.Type;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.MethodDeclaration;
import gr.uoa.di.std08169.compilers.hw3.visitor.DepthFirstVisitor;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Visitor gia methodous (2o perasma)
 * @author labis
 */
public class SecondPassMethodVisitor extends DepthFirstVisitor {
    private String clazz;
    private Piglet piglet;
    
    public SecondPassMethodVisitor(String clazz, Piglet piglet) {
        this.clazz = clazz;
        this.piglet = piglet;
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration) {
//      clazz_methodDeclaration.f2.f0.tokenImage [ argc ]
//      BEGIN
//      ...
//      MOVE TEMP 1 methodDeclaration.f10
//      RETURN TEMP 1
//      END
        
        
        String name = methodDeclaration.f2.f0.tokenImage;
        int temp1 = piglet.getClasses().get(clazz).getMethods().get(name).getTemp();
        //plithos orismatwn + 1 (logo oti this = temp0)
        int argc = piglet.getClasses().get(clazz).getMethods().get(name).getParameters().size() + 1;
        
        piglet.getPrintWriter().println(clazz + "_" + name + " [ " + argc + " ]");
        piglet.getPrintWriter().println("BEGIN");
        
        //Kalei visitor gia elenxo twn statements
        methodDeclaration.f8.accept(new SecondPassStatementVisitor(clazz, name, piglet));

        //Oti tupo epistrofhs pou exei dhlwthei h sunartish
        Type type1 = piglet.getClasses().get(clazz).getMethods().get(name).getType();

        piglet.getPrintWriter().print("MOVE TEMP " + temp1 + " ");
        
        //Kalei ton visitor gia expressions gia to return Expression
        Type type2 = methodDeclaration.f10.accept(new SecondPassExpressionVisitor(clazz, name, piglet));
        
        piglet.getPrintWriter().println();
        piglet.getPrintWriter().println("RETURN TEMP " + temp1);
        piglet.getPrintWriter().println("END //" + clazz + "_" + name);
        
        //enenxos an h sunartish epistrefei klash
        if(type1.isClass()) {
            //to return epistrefei upoklash ths dhlwmenhs klashs
            if(!type1.isSuperClass(type2, piglet.getClasses()) && (!type1.equals(type2)))
                throw new CompileException("Error: Grammh " + methodDeclaration.f9.beginLine + ": h sunartish epistrefei "
                        + type1 + " alla to return exei tupo " + type2);
        } else if(!type1.equals(type2))//epistrefei kapoio base tupo
            throw new CompileException("Error: Grammh " + methodDeclaration.f9.beginLine + ": h sunartish epistrefei "
                        + type1 + " alla to return statement exei tupo " + type2);
    }
}
