import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
    public static ArrayList<Token> token_list;
    public static int counter = 0;
    public static Stack<Token> the_stack_tm = new Stack<Token>();
    public static void main(String[] args){
        //Grab the token list from the lexer
        Lexer lex = new Lexer();
        lex.lex_text();
        token_list = lex.tokens;
        the_stack_tm = make_stack();
    }
    //create the stack for the interpreter
    public static Stack<Token> make_stack(){
        token_list = reverse_list(token_list);
        for(int i = 0; i < token_list.size(); i++){
            the_stack_tm.push(token_list.get(i));
        }
        return the_stack_tm;
    }
    //prepare the list to be made into a stack
    public static ArrayList<Token> reverse_list(ArrayList<Token> token_list){
        ArrayList<Token> temp = new ArrayList<Token>();
        for(int i = token_list.size() -1; i > 0; i--){
            temp.add(token_list.get(i));
        }
        return temp;
    }
}
