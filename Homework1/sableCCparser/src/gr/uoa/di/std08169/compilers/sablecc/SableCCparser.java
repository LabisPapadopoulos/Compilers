package gr.uoa.di.std08169.compilers.sablecc;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PushbackReader;

import gr.uoa.di.std08169.compilers.sablecc.lexer.Lexer;
import gr.uoa.di.std08169.compilers.sablecc.lexer.LexerException;
import gr.uoa.di.std08169.compilers.sablecc.parser.Parser;
import gr.uoa.di.std08169.compilers.sablecc.parser.ParserException;

/**
 *
 * @author labis
 */
public class SableCCparser {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
	    Parser p = new Parser(new Lexer(new PushbackReader(new InputStreamReader(System.in), 1024)));

	    p.parse();
	} catch(ParserException ex){
            ex.printStackTrace();
	} catch(LexerException ex){
            ex.printStackTrace();
	} catch(IOException ex){
            ex.printStackTrace();
	} catch(Exception ex){
            ex.printStackTrace();
	}
    }
}
