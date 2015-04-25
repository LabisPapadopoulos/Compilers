package gr.uoa.di.std08169.compilers.hw3.symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Clazz: Klassh pou antiproswpeuei mia klash
 * @author labis
 */
public class Clazz {

    private String name;
    private String parent;
    private List<Variable> variables;
    private SortedMap<String, Method> methods;
    
    public Clazz(String name) {
        this(name, null);
    }
    
    public Clazz(String name, String parent) {
        this.name = name;
        this.parent = parent;
        variables = new ArrayList<Variable>();
        methods = new TreeMap<String, Method>();
    }

    public String getName() {
        return name;
    }

    public String getParent() {
        return parent;
    }
    
    public List<Variable> getVariables() {
        return variables;
    }
    
    public SortedMap<String, Method> getMethods() {
        return methods;
    }
    
    public SortedMap<String, String> getInheritedMethods(Map<String, Clazz> classes) {
        //Sorted Map apo <methodo, klash_methodos>
        SortedMap<String, String> inheritedMethods = new TreeMap<String, String>();
        
        //Gemisma twn dikwn tou methwdwn kai twn progonwn tou
        for(String clazz = name; (clazz != null) && classes.containsKey(clazz) ; clazz = classes.get(clazz).getParent()) {
            for(Map.Entry<String, Method> entry : classes.get(clazz).getMethods().entrySet()) {
                if(!inheritedMethods.containsKey(entry.getKey()))
                    inheritedMethods.put(entry.getKey(), clazz + "_" + entry.getKey());
            }
        }
        
        return inheritedMethods;
    }
    
    //Lista me metavlites twn progonwn arxika kai meta tou kathe antikeimenou
    public List<String> getInheritedVariables(Map<String, Clazz> classes) {
        //Sorted Map apo <methodo, klash_methodos>
        List<String> inheritedVariables = new ArrayList<String>();
        
        getInheritedVariables(classes, inheritedVariables);
        
        return inheritedVariables;
    }
    
    private void getInheritedVariables(Map<String, Clazz> classes, List<String> inheritedVariables) {
        //vazei tis metavlites tou patera anadromika
        if ((parent != null) && classes.containsKey(parent))
            classes.get(parent).getInheritedVariables(classes, inheritedVariables);
        
        //vazei tis dikes tou metavlites
        for(Variable variable : variables)
            inheritedVariables.add(variable.getName());
    }
}
