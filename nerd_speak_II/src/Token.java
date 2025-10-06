public class Token {
    public enum TokenType{
        DICE,
        LANGUAGE,
        IDENTIFIER,
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        KEYWORD,
        EQUAL,
        FUNCTION_CALL,
        TILDE,
        BAG_OF_HOLDING,
        ARRAY_FUNCTION,
        BOOLEAN,
        WOW
    }

    public final TokenType type;
    public final String value;

    public Token(TokenType type, String value){
        this.type = type;
        this.value = value;
    }
}
