package gr.uoa.di.std08169.compilers.hw3.symbols;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author labis
 */
public class Method {
    
    private String name;
    private Type type;
    private int offset; //thesh sto v-table
    private List<Variable> parameters;
    private List<Variable> variables;
    private int temp;
    
    public Method(String name, Type type, int offset) {
        this.name = name;
        this.type = type;
        this.offset = offset;
        parameters = new ArrayList<Variable>();
        variables = new ArrayList<Variable>();
        temp = 1;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
    
    public int getOffset() {
        return offset;
    }

    public List<Variable> getParameters() {
        return parameters;
    }
    
    public List<Variable> getVariables() {
        return variables;
    }
    
    public int getTemp() {
        //to 0 einai to this
        //ta epomena [1 ews variables.size()] einai metablhtes mini java
        //apo variables.size() kai panw einai topikes metavlites piglet.
        return variables.size() + temp++;
    }
}
