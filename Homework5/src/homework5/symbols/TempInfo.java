package homework5.symbols;

/**
 * Diasthma pou einai zwntanh mia metavlith kai xrwma pou pairnei apo ton
 * algorithmo chaitin
 * @author labis
 */
public class TempInfo {
    private int begin;
    private int end;
    private Register colour;
    private int stackOffset; //seira sth stoiva gia tous axrwmatistous registers

    public TempInfo(int begin) {
        this.begin = begin;
        this.end = -1; //den exei xrhsimopoihthei akoma h metavlith
        this.colour = null;
        this.stackOffset = -1;
    }
    
    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(final int end) {
        this.end = end;
    }

    public Register getColour() {
        return colour;
    }

    public void setColour(final Register colour) {
        this.colour = colour;
    }

    public int getStackOffset() {
        return stackOffset;
    }

    public void setStackOffset(final int stackOffset) {
        this.stackOffset = stackOffset;
    }
}
