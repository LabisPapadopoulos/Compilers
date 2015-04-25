package gr.uoa.di.std08169.compilers;

/**
 *
 * @author labis
 */
public class ParseTree {
    
    public enum Operator {
        PLUS,
        MINUS,
        MULT,
        DIV
    }
    
    private int num;
    private Operator operator;
    private ParseTree leftChild;
    private ParseTree rightChild;
    
    public ParseTree(int num) {
        this.num = num;
    }
    
    public ParseTree(Operator operator, ParseTree leftChild, ParseTree rightChild) {
        this.operator = operator;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }
    
    @Override
    public String toString() {
        if(operator == null) {
            return Integer.toString(num);
        } else {
            switch(operator) {
            case PLUS:
                return "+" + leftChild.toString() + rightChild.toString();
            case MINUS:
                return "-" + leftChild.toString() + rightChild.toString();
            case MULT:
                return "*" + leftChild.toString() + rightChild.toString();
            case DIV:
                return "/" + leftChild.toString() + rightChild.toString();
            default:
                return null;
            }
        }
    }
    
    public int evaluate() {
        if(operator == null) {
            return num;
        } else {
            switch(operator) {
                case PLUS:
                    return leftChild.evaluate() + rightChild.evaluate();
                case MINUS:
                    return leftChild.evaluate() - rightChild.evaluate();
                case MULT:
                    return leftChild.evaluate() * rightChild.evaluate();
                case DIV:
                    return leftChild.evaluate() / rightChild.evaluate();
                default:
                    return 0;
            }
        }
    }
}
