package gr.uoa.di.std08169.compilers.hw2.symbols;

import java.util.HashMap;
import java.util.Map;

/**
 * Clazz: Klassh pou antiproswpeuei mia klash
 * @author labis
 */
public class Clazz {

    private String name;
    private String parent;
    private Map<String, Variable> variables;
    private Map<String, Method> methods;
    
    public Clazz(String name) {
        this(name, null);
    }
    
    public Clazz(String name, String parent) {
        this.name = name;
        this.parent = parent;
        variables = new HashMap<String, Variable>();
        methods = new HashMap<String, Method>();
    }

    public String getName() {
        return name;
    }

    public String getParent() {
        return parent;
    }
    
    public Map<String, Variable> getVariables() {
        return variables;
    }
    
    public Map<String, Method> getMethods() {
        return methods;
    }
}
