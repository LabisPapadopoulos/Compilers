package gr.uoa.di.std08169.compilers.hw2.symbols;

import gr.uoa.di.std08169.compilers.hw2.syntaxtree.ArrayType;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.BooleanType;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.Identifier;
import gr.uoa.di.std08169.compilers.hw2.syntaxtree.IntegerType;
import java.util.Map;

/**
 *
 * @author labis
 */
public class Type {

    private static final String ARRAY_TYPE = "int[]";
    private static final String BOOLEAN_TYPE = "boolean";
    private static final String INTEGER_TYPE = "int";
    
    public static final Type ARRAY = new Type(ARRAY_TYPE);
    public static final Type BOOLEAN = new Type(BOOLEAN_TYPE);
    public static final Type INTEGER = new Type(INTEGER_TYPE);
    public static final Type UNKNOWN = new Type((String) null);
    
    private String type;
    
    public Type(String type) {
        this.type = type;
    }

    public Type(gr.uoa.di.std08169.compilers.hw2.syntaxtree.Type type) {
        if (type.f0.choice instanceof ArrayType)
            this.type = ARRAY_TYPE;
        else if(type.f0.choice instanceof BooleanType)
            this.type = BOOLEAN_TYPE;
        else if(type.f0.choice instanceof IntegerType)
            this.type = INTEGER_TYPE;
        else if(type.f0.choice instanceof Identifier)
            this.type = ((Identifier)type.f0.choice).f0.tokenImage;
    }
    
    public boolean isClass() {
        return (type != null) && (!type.equals(ARRAY_TYPE)) && 
                (!type.equals(BOOLEAN_TYPE)) && (!type.equals(INTEGER_TYPE));
    }
    
    public boolean isSuperClass(Type subtype, Map<String, Clazz> classes) {
        //exasfalish oti h klash this.type kai subtype einai uparktes klasseis
        if (isClass() && classes.containsKey(type) && subtype.isClass() && classes.containsKey(subtype.type)) {
            
                //progonos tou subtype
            for(String progonos = classes.get(subtype.type).getParent();
                    (progonos != null) && classes.containsKey(progonos);
                    progonos = classes.get(progonos).getParent()) {
                //einai uperklash tou subtype
                if(progonos.equals(type))
                    return true;
            }
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        return (type == null) ? "<agnwsto>" : type;
    }
    
    @Override
    public boolean equals(Object object) {
        if(object == null)
            return false;
        
        if(object instanceof Type) {
            Type type = (Type)object;
            
            if (this.type == null)
                return (type.type == null);
            
            return this.type.equals(type.type);
        }
        
        return false;
    }
    
    @Override
    public int hashCode() {
        return (type == null) ? 0 : type.hashCode();
    }
}
