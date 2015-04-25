package gr.uoa.di.std08169.compilers.hw3;

import gr.uoa.di.std08169.compilers.hw3.parser.MiniJavaParser;
import gr.uoa.di.std08169.compilers.hw3.parser.ParseException;
import gr.uoa.di.std08169.compilers.hw3.symbols.Clazz;
import gr.uoa.di.std08169.compilers.hw3.syntaxtree.Goal;
import gr.uoa.di.std08169.compilers.hw3.visitors.firstpass.FirstPassClassVisitor;
import gr.uoa.di.std08169.compilers.hw3.visitors.firstpass.FirstPassMainClassVisitor;
import gr.uoa.di.std08169.compilers.hw3.visitors.secondpass.SecondPassClassVisitor;
import gr.uoa.di.std08169.compilers.hw3.visitors.secondpass.SecondPassStatementVisitor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author labis
 */
public class Piglet {
    
    public static final int WORD_SIZE = 4;
    private static final String JAVA_EXTENSION = ".java";
    private static final String PIGLET_EXTENSION = ".pg";
    private Goal tree;
    private Clazz mainClass;
    private Map<String, Clazz> classes;
    private PrintWriter printWriter;
    private int label;
    
    //Ftiaxnei to dentro.
    public Piglet(InputStream inputStream, PrintWriter printWriter) throws ParseException {
        tree = new MiniJavaParser(inputStream).Goal();
        classes = new HashMap<String, Clazz>();
        this.printWriter = printWriter;
        label = 0;
    }
    
    public Clazz getMainClass() {
        return mainClass;
    }

    public Map<String, Clazz> getClasses() {
        return classes;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }
    
    public String getLabel() {
        return "label" + label++;
    }
    
    //Kanei to prwto perasma gia na ftiaxei to symbol table.
    private void firstPass() {
        mainClass = tree.f0.accept(new FirstPassMainClassVisitor());
        tree.f1.accept(new FirstPassClassVisitor(), classes);
    }
    
    
    private void secondPass() {
        classes.put(mainClass.getName(), mainClass);

        printWriter.println("MAIN");
        
        //Xekinaei visitor gia ta statements tis main
        tree.f0.f15.accept(new SecondPassStatementVisitor(mainClass.getName(), "main", this));
        
        printWriter.println("END //main");
        
        classes.remove(mainClass.getName()); // h main class den xrhsimopoieitai apo edw kai meta

        //Elenxos twn statement twn methodwn twn klasewn
        tree.f1.accept(new SecondPassClassVisitor(this));
    }
    
    
    public void compile() {
        //koitazei dhlwseis
        firstPass();
        
        //koitazei statements
        secondPass();
    }
    
    public void finish() {
        printWriter.flush();
        printWriter.close();
    }

    public static void main(String[] args) {
        
        if(args.length == 0) {
            System.err.println("Usage: java Piglet <inputFiles>");
            System.exit(-1);
        }
        
        for(String arg : args) {
            try {
                if (arg.endsWith(JAVA_EXTENSION)) {
                    String newName = arg.substring(0, arg.length() - JAVA_EXTENSION.length()) + PIGLET_EXTENSION;
                    Piglet piglet = new Piglet(new FileInputStream(arg), new PrintWriter(newName));
                    
                    try {
                        System.out.println("Elenxos arxeiou: " + arg);
                        piglet.compile();
                        piglet.finish();
                    } catch (CompileException ex) {
                        System.err.println(ex.getMessage());
                        piglet.finish();
                        //Diagrafh arxeiou piglet
                        new File(newName).delete();
                    }
                } else
                    System.err.println("To arxeio " + arg + " den einai arxeio .java");
            } catch (FileNotFoundException ex) {
                System.err.println("To arxeio " + arg + " den uparxei.");
            } catch (ParseException ex) {
                System.err.println("To arxeio " + arg + " exei suntaktika sfalmata:");
                System.err.println(ex.getMessage());
            }
        }
    }
}
