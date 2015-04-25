package gr.uoa.di.std08169.compilers.hw2.symbols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author labis
 */
public class Method {
    
    private String name;
    private Type type;
    private List<Variable> parameters;
    //      Map<String, <Type, Identifier>>
    private Map<String, Variable> variables;
    
    public Method(String name, Type type) {
        this.name = name;
        this.type = type;
        parameters = new ArrayList<Variable>();
        variables = new HashMap<String, Variable>();
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public List<Variable> getParameters() {
        return parameters;
    }
    
    public Map<String, Variable> getVariables() {
        return variables;
    }
}
