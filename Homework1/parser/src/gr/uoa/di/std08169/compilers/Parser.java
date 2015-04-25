package gr.uoa.di.std08169.compilers;

/**
 *
 * @author labis
 */
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser {

    private int lookaheadToken;
    private Reader input;
    
    private static final int EOF = -1;

    public Parser(Reader input) throws IOException {
	this.input = input;
        //Diavazei ton prwto xarakthra
	lookaheadToken = input.read();
    }

    //Pernaei 'h oxi to kathe epomeno token
    private void consume(int token) throws IOException, ParseException {
	if (lookaheadToken != token)
	    throw new ParseException("Perimena " + tokenToString(token) + " alla vrika: " + tokenToString(lookaheadToken), 0);
	lookaheadToken = input.read();
    }

    
    private ParseTree parseExp() throws IOException, ParseException {
        switch(lookaheadToken) {
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            int num = lookaheadToken - '0';
            consume(lookaheadToken);
            ParseTree leftChild = new ParseTree(num);
            leftChild = parseTerm2(leftChild);
            return parseExp2(leftChild);
        case '(':
            consume('(');
            leftChild = parseExp();
            consume(')');
            leftChild = parseTerm2(leftChild);
            return parseExp2(leftChild);
        default:
            throw new ParseException("Perimena na vrw 0-9, 'h '(' alla vrika " + tokenToString(lookaheadToken), 0);
        }
    }
    
    private ParseTree parseExp2(ParseTree leftChild) throws IOException, ParseException {
        switch(lookaheadToken) {
            case '+':
                consume(lookaheadToken);
                ParseTree rightChild = parseTerm();
                ParseTree currentNode = new ParseTree(ParseTree.Operator.PLUS, leftChild, rightChild);
                return parseExp2(currentNode);
            case '-':
                //katanalwnei to arxiko pou vrike
                consume(lookaheadToken);
                rightChild = parseTerm();
                currentNode = new ParseTree(ParseTree.Operator.MINUS, leftChild, rightChild);
                return parseExp2(currentNode);
            case ')':
            case EOF:
            case '\n':
                return leftChild;
            default:
                throw new ParseException("Perimena +,-, ')' 'h EOF alla vrika " + tokenToString(lookaheadToken), 0);
        }
    }
    
    private ParseTree parseTerm() throws IOException, ParseException {
        switch(lookaheadToken) {
        //case num
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            int num = lookaheadToken - '0';
            ParseTree leftChild = new ParseTree(num);
            consume(lookaheadToken);
            return parseTerm2(leftChild);
        case '(':
            consume('(');
            leftChild = parseExp();
            consume(')');
            return parseTerm2(leftChild);
        default:
            throw new ParseException("Perimena 0-9 'h '(', alla vrika " + tokenToString(lookaheadToken), 0);
        }
    }
    
    //orisma aristero paidi, gia na koumpwnei komvous epanw tou
    private ParseTree parseTerm2(ParseTree leftChild) throws IOException, ParseException {
        switch(lookaheadToken) {
        case '*':
            consume(lookaheadToken);
            //sunexizei me ta upoloipa
            ParseTree rightChild = parseFactor();
            ParseTree currentNode = new ParseTree(ParseTree.Operator.MULT, leftChild, rightChild);
            return parseTerm2(currentNode); //p.x: 1*2*3*4
        case '/':
            consume(lookaheadToken);
            //sunexizei me ta upoloipa
            rightChild = parseFactor();
            currentNode = new ParseTree(ParseTree.Operator.DIV, leftChild, rightChild);
            return parseTerm2(currentNode); //p.x: 1*2*3*4
        case '+':
        case '-':
        case ')':
        case EOF:
        case '\n':
            //tha uparxei sigoura, opote epistrefetai mono auto
            return leftChild;
        default:
            throw new ParseException("Perimena na vrw +,-,*,/,')' 'h EOF alla vrika " + tokenToString(lookaheadToken), 0);
        }
    }
    
    private ParseTree parseFactor() throws IOException, ParseException {
        switch (lookaheadToken) {
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            int num = lookaheadToken - '0'; //metatroph se akeraio
            consume(lookaheadToken);
            return new ParseTree(num);
        case '(': //den xreiazetai na kratiountai oi parentheseis sto dentro
            consume('(');
            ParseTree parseTree = parseExp();
            consume(')');
            return parseTree;
        default:
            throw new ParseException("Perimena 0-9 'h '(' alla vrika " + tokenToString(lookaheadToken), 0);
        }
    }
    
    private String tokenToString(int token) {
        return (token == EOF || token == '\n') ? "EOF" : Character.toString((char) token);
    }

    public ParseTree parse() throws IOException, ParseException {
        ParseTree tree = parseExp();
        
        switch (lookaheadToken) {
        case EOF:
            return tree;
        case '\n':
            return tree;
        default: //parsare to expression alla meinane pragmata pou periseuoun
            throw new ParseException("Perimena na vrw EOF, alla perisepsan: " + tokenToString(lookaheadToken), 0);
        }
    }
    

    public static void main(String[] args) {
        
        try {
            Parser parser = new Parser(new InputStreamReader(System.in));
            ParseTree tree = parser.parse();
            System.out.println("prefix notation: " + tree.toString());
            System.out.println("apotelesma: " + tree.evaluate());
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch(ParseException e){
            e.printStackTrace();
        }
    }
}
