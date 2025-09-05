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
        LPAREN,
        RPAREN,
        COMMA,
        DRIVEL
    }

    public final TokenType type;
    public final String value;

    public Token(TokenType type, String value){
        this.type = type;
        this.value = value;
    }
}
