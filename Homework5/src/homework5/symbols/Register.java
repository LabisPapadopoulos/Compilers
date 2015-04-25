package homework5.symbols;

/**
 *
 * @author labis
 */
public enum Register {

    //kataxwrhtes genikou skopou
    T0("t0"),
    T1("t1"),
    T2("t2"),
    T3("t3"),
    T4("t4"),
    T5("t5"),
    T6("t6"),
    T7("t7"),
    T8("t8"),
    T9("t9"),
    S0("s0"),
    S1("s1"),
    S2("s2"),
    S3("s3"),
    S4("s4"),
    S5("s5"),
    S6("s6"),
    S7("s7");

    public static final int T_REGISTERS = 10;
    public static final int S_REGISTERS = 8;
    
    private final String name;
    
    private Register(final String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
       return name;
    }
}
