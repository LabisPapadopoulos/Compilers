package gr.uoa.di.std08169.compilers.hw2;

import gr.uoa.di.std08169.compilers.hw2.parser.MiniJavaParser;
import gr.uoa.di.std08169.compilers.hw2.parser.ParseException;
import gr.uoa.di.std08169.compilers.hw2.symbols.Clazz;
import gr.uoa.di.std08169.compilers.hw2.symbols.Method;
import gr.uoa.di.std08169.compilers.hw2.symbols.Type;
import gr.uoa.di.std08169.compilers.hw2.symbols.Variable;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.Goal;
import gr.uoa.di.std08169.compilers.hw2.visitors.firstpass.FirstPassClassVisitor;
import gr.uoa.di.std08169.compilers.hw2.visitors.firstpass.FirstPassMainClassVisitor;
import gr.uoa.di.std08169.compilers.hw2.visitors.secondpass.SecondPassClassVisitor;
import gr.uoa.di.std08169.compilers.hw2.visitors.secondpass.SecondPassStatementVisitor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author labis
 */
class Driver {
    
    public static void main (String [] args) {
	if(args.length == 0){
	    System.err.println("Usage: java Driver <inputFiles>");
	    System.exit(1);
	}
        
        for(String argument : args) {
            try {
                System.out.println("Elenxos arxeiou: " + argument);
                check(new FileInputStream(argument));
            } catch (FileNotFoundException ex) {
                System.err.println("Den vrika to arxeio " + argument);
            } catch (ParseException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
    
    public static void check(InputStream inputStream) throws ParseException {
        MiniJavaParser parser = new MiniJavaParser(inputStream);
        Goal tree = parser.Goal();

        FirstPassMainClassVisitor firstPassMainClassVisitor = new FirstPassMainClassVisitor();

        Clazz mainClass = tree.f0.accept(firstPassMainClassVisitor);

        Map<String, Clazz> classes = new HashMap<String, Clazz>();

        //Me to pou dhmiourghthei o FirstPassClassVisitor, trexei epanw stis klaseis (f1), 
        //oxi sti main class
        tree.f1.accept(new FirstPassClassVisitor(), classes);


        //Elenxos stin Main
        //elenxos oti uparxoun oi klaseis twn metavlitwn tis methodou
        for(Map.Entry<String, Variable> variableEntry: mainClass.getMethods().get("main").getVariables().entrySet()) {

            Type type = variableEntry.getValue().getType();

            if(type.isClass() && (!classes.containsKey(type.toString()))) {
                System.err.println("Error: h metavlhth " + variableEntry.getKey() + " ths methodou main"
                    + " ths main klashs " + mainClass.getName() +  " exei tupo tin klash " 
                    + variableEntry.getValue().getType() + " h opoia den uparxei.");
            }
        }

        //Elenxos klasewn
        for(Map.Entry<String, Clazz> classEntry : classes.entrySet()) {
            String parent = classEntry.getValue().getParent();

            //Elenxos gia to an uparxei o goneas kathe klasshs
            if(parent != null) { //H klash hthele patera
                if(!classes.containsKey(parent)) { // den uparxei o pateras
                    System.err.println("Error: h klash " + classEntry.getKey() + 
                            " hthele gia patera tin klash " + parent + " alla den uparxei");
                }

                //elenxos an uparxei kuklos
                //Diasxish olwn twn progonwn (san sundedemenh lista)
                String progonos = parent;
                while (progonos != null) {

                    //elenxos an uparxei progonos idios me tin trexousa klash (den einai soi tou eautou tou)
                    if(progonos.equals(classEntry.getKey())) {
                        System.err.println("Error: h klash " + classEntry.getKey() + " exei progono ton eauto ths");
                        break;
                    }

                    //an den uparxei kai o progonos san klash, stamataei to psaximo
                    if (!classes.containsKey(progonos))
                        break;

                    progonos = classes.get(progonos).getParent();
                }
            }

            //Elenxos metavlhtwn klashs
            for(Map.Entry<String, Variable> variableEntry : classEntry.getValue().getVariables().entrySet()) {

                Type type = variableEntry.getValue().getType();
                //An phgene gia klash kai den uparxei klash sugkekrimenou tupou
                if(type.isClass() && (!classes.containsKey(type.toString()))) {       
                    System.err.println("Error: h metavlhth " + variableEntry.getKey() + " ths klashs " +  classEntry.getKey()
                            + " exei tupo tin klash " + variableEntry.getValue().getType() + " h opoia den uparxei.");

                }
            }

            //Elenxos methodwn klashs
            for(Map.Entry<String, Method> methodEntry : classEntry.getValue().getMethods().entrySet()) {

                //elenxos oti uparxei h klash pou epistrefei h methodos
                Type type = methodEntry.getValue().getType();

                                     //An den uparxei klash sugkekrimenou tupou
                if(type.isClass() && (!classes.containsKey(type.toString()))) {
                    System.err.println("Error: h methodos " + methodEntry.getKey() + " ths klashs " + classEntry.getKey() + 
                            " exei tupo tin klash " + methodEntry.getValue().getType() + " h opoia den uparxei.");

                }

                //elenxos oti uparxoun oi klaseis twn metavlitwn tis methodou
                for(Map.Entry<String, Variable> variableEntry : methodEntry.getValue().getVariables().entrySet()) {

                    type = variableEntry.getValue().getType();

                    if(type.isClass() && (!classes.containsKey(type.toString()))) {
                        System.err.println("Error: h metavlhth " + variableEntry.getKey() + " ths methodou "
                                + methodEntry.getKey() + " ths klashs " + classEntry.getKey() +  " exei tupo tin klash " 
                                + variableEntry.getValue().getType() + " h opoia den uparxei.");
                    }
                }

                //elexos gia override
                String progonos = parent;
                while ((progonos != null) && classes.containsKey(progonos)) {
                    //o progonos exei epishs tin idia methodo, tote ginetai override
                    if(classes.get(progonos).getMethods().containsKey(methodEntry.getKey())) {
                        Method progonosMethod = classes.get(progonos).getMethods().get(methodEntry.getKey());

                        //elenxos tupou epistrofhs twn methodwn
                        if(!progonosMethod.getType().equals(methodEntry.getValue().getType())) {
                            System.err.println("Error: h methodos " + methodEntry.getKey() + " ths klashs " + 
                                    classEntry.getKey() +  " kanei override tin methodo " + methodEntry.getKey() +
                                    " ths klashs " + progonos + " alla epistrefei " + methodEntry.getValue().getType() + 
                                    " anti gia " + progonosMethod.getType());
                        }

                        int progonosParametersSize = progonosMethod.getParameters().size();
                        int parametersSize = methodEntry.getValue().getParameters().size();

                        //elenxos plithous orismatwn twn methodwn
                        if(progonosParametersSize != parametersSize) {
                            System.err.println("Error: h methodos " + methodEntry.getKey() + " ths klashs " + 
                                    classEntry.getKey() +  " kanei override tin methodo " + methodEntry.getKey() +
                                    " ths klashs " + progonos + " alla dexetai " + parametersSize + 
                                    " orismata anti gia " + progonosParametersSize);

                        }

                        int minSize = (progonosParametersSize < parametersSize) ? progonosParametersSize : parametersSize;

                        //elenxo tupwn orismatwn twn methodwn
                        for(int i = 0; i < minSize; i++) {
                            //an den einai idiou tupou to i-osto orisma
                            if(!progonosMethod.getParameters().get(i).getType().equals(
                                methodEntry.getValue().getParameters().get(i).getType())) {
                                System.err.println("Error: h methodos " + methodEntry.getKey() + " ths klashs " + 
                                    classEntry.getKey() +  " kanei override tin methodo " + methodEntry.getKey() +
                                    " ths klashs " + progonos + " alla to orisma " + (i + 1) + " exei tupo " +
                                    methodEntry.getValue().getParameters().get(i).getType() + " anti gia " +
                                    progonosMethod.getParameters().get(i).getType());
                            }
                        }

                        break;
                    }
                    //o pateras tou progonou
                    progonos = classes.get(progonos).getParent();
                }

            } 
        } // Telos 1ou perasmatos

        classes.put(mainClass.getName(), mainClass);

        //Xekinaei visitor gia ta statements tis main
        tree.f0.f15.accept(new SecondPassStatementVisitor(mainClass.getName(), "main", classes));
        
        classes.remove(mainClass.getName()); // h main class den xrhsimopoieitai apo edw kai meta

        //Elenxos twn statement twn methodwn twn klasewn
        tree.f1.accept(new SecondPassClassVisitor(classes));
    }
}
