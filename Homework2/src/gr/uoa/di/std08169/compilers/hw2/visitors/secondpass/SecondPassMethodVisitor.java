package gr.uoa.di.std08169.compilers.hw2.visitors.secondpass;

import gr.uoa.di.std08169.compilers.hw2.symbols.Clazz;
import gr.uoa.di.std08169.compilers.hw2.symbols.Type;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.MethodDeclaration;
import gr.uoa.di.std08169.compilers.hw2.visitor.DepthFirstVisitor;
import java.util.Map;

/**
 * Visitor gia methodous (2o perasma)
 * @author labis
 */
public class SecondPassMethodVisitor extends DepthFirstVisitor {
    private String clazz;
    private Map<String, Clazz> classes;

    public SecondPassMethodVisitor(String clazz, Map<String, Clazz> classes) {
        this.clazz = clazz;
        this.classes = classes;
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration) {
        String name = methodDeclaration.f2.f0.tokenImage;
        
        //Kalei visitor gia elenxo twn statements
        methodDeclaration.f8.accept(new SecondPassStatementVisitor(clazz, name, classes));

        //Oti tupo epistrofhs pou exei dhlwthei h sunartish
        Type type1 = classes.get(clazz).getMethods().get(name).getType();

        //Kalei ton visitor gia expressions gia to return Expression
        Type type2 = methodDeclaration.f10.accept(new SecondPassExpressionVisitor(clazz, name, classes));
        
        //enenxos an h sunartish epistrefei klash
        if(type1.isClass()) {
            //to return epistrefei upoklash ths dhlwmenhs klashs
            if(!type1.isSuperClass(type2, classes) && (!type1.equals(type2))) {
                System.err.println("Error: Grammh " + methodDeclaration.f9.beginLine + ": h sunartish epistrefei "
                        + type1 + " alla to return exei tupo " + type2);
            }
        } else if(!type1.equals(type2)) { //epistrefei kapoio base tupo
            System.err.println("Error: Grammh " + methodDeclaration.f9.beginLine + ": h sunartish epistrefei "
                        + type1 + " alla to return statement exei tupo " + type2);
        }
    }
}
