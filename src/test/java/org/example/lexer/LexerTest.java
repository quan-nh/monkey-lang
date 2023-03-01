package org.example.lexer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.example.token.Token;
import org.junit.jupiter.api.Test;

class LexerTest {

    @Test
    void nextToken() {
        var input = """
let five = 5;
let ten = 10;

let add = fn(x, y) {
  x + y;
};

let result = add(five, ten);

!-/*5;
5 < 10 > 5;

if (5 < 10) {
  return true;
} else {
  return false;
}

10 == 10;
10 != 9;
""";
        
        var expectedTokens = new ArrayList<Token>();
        expectedTokens.add(new Token(Token.LET, "let"));
        expectedTokens.add(new Token(Token.IDENT, "five"));
        expectedTokens.add(new Token(Token.ASSIGN, "="));
        expectedTokens.add(new Token(Token.INT, "5"));
        expectedTokens.add(new Token(Token.SEMICOLON, ";"));
        expectedTokens.add(new Token(Token.LET, "let"));
        expectedTokens.add(new Token(Token.IDENT, "ten"));
        expectedTokens.add(new Token(Token.ASSIGN, "="));
        expectedTokens.add(new Token(Token.INT, "10"));
        expectedTokens.add(new Token(Token.SEMICOLON, ";"));
        expectedTokens.add(new Token(Token.LET, "let"));
        expectedTokens.add(new Token(Token.IDENT, "add"));
        expectedTokens.add(new Token(Token.ASSIGN, "="));
        expectedTokens.add(new Token(Token.FUNCTION, "fn"));
        expectedTokens.add(new Token(Token.LPAREN, "("));
        expectedTokens.add(new Token(Token.IDENT, "x"));
        expectedTokens.add(new Token(Token.COMMA, ","));
        expectedTokens.add(new Token(Token.IDENT, "y"));
        expectedTokens.add(new Token(Token.RPAREN, ")"));
        expectedTokens.add(new Token(Token.LBRACE, "{"));
        expectedTokens.add(new Token(Token.IDENT, "x"));
        expectedTokens.add(new Token(Token.PLUS, "+"));
        expectedTokens.add(new Token(Token.IDENT, "y"));
        expectedTokens.add(new Token(Token.SEMICOLON, ";"));
        expectedTokens.add(new Token(Token.RBRACE, "}"));
        expectedTokens.add(new Token(Token.SEMICOLON, ";"));
        expectedTokens.add(new Token(Token.LET, "let"));
        expectedTokens.add(new Token(Token.IDENT, "result"));
        expectedTokens.add(new Token(Token.ASSIGN, "="));
        expectedTokens.add(new Token(Token.IDENT, "add"));
        expectedTokens.add(new Token(Token.LPAREN, "("));
        expectedTokens.add(new Token(Token.IDENT, "five"));
        expectedTokens.add(new Token(Token.COMMA, ","));
        expectedTokens.add(new Token(Token.IDENT, "ten"));
        expectedTokens.add(new Token(Token.RPAREN, ")"));
        expectedTokens.add(new Token(Token.SEMICOLON, ";"));
        expectedTokens.add(new Token(Token.BANG, "!"));
        expectedTokens.add(new Token(Token.MINUS, "-"));
        expectedTokens.add(new Token(Token.SLASH, "/"));
        expectedTokens.add(new Token(Token.ASTERISK, "*"));
        expectedTokens.add(new Token(Token.INT, "5"));
        expectedTokens.add(new Token(Token.SEMICOLON, ";"));
        expectedTokens.add(new Token(Token.INT, "5"));
        expectedTokens.add(new Token(Token.LT, "<"));
        expectedTokens.add(new Token(Token.INT, "10"));
        expectedTokens.add(new Token(Token.GT, ">"));
        expectedTokens.add(new Token(Token.INT, "5"));
        expectedTokens.add(new Token(Token.SEMICOLON, ";"));
        expectedTokens.add(new Token(Token.IF, "if"));
        expectedTokens.add(new Token(Token.LPAREN, "("));
        expectedTokens.add(new Token(Token.INT, "5"));
        expectedTokens.add(new Token(Token.LT, "<"));
        expectedTokens.add(new Token(Token.INT, "10"));
        expectedTokens.add(new Token(Token.RPAREN, ")"));
        expectedTokens.add(new Token(Token.LBRACE, "{"));
        expectedTokens.add(new Token(Token.RETURN, "return"));
        expectedTokens.add(new Token(Token.TRUE, "true"));
        expectedTokens.add(new Token(Token.SEMICOLON, ";"));
        expectedTokens.add(new Token(Token.RBRACE, "}"));
        expectedTokens.add(new Token(Token.ELSE, "else"));
        expectedTokens.add(new Token(Token.LBRACE, "{"));
        expectedTokens.add(new Token(Token.RETURN, "return"));
        expectedTokens.add(new Token(Token.FALSE, "false"));
        expectedTokens.add(new Token(Token.SEMICOLON, ";"));
        expectedTokens.add(new Token(Token.RBRACE, "}"));
        expectedTokens.add(new Token(Token.INT, "10"));
        expectedTokens.add(new Token(Token.EQ, "=="));
        expectedTokens.add(new Token(Token.INT, "10"));
        expectedTokens.add(new Token(Token.SEMICOLON, ";"));
        expectedTokens.add(new Token(Token.INT, "10"));
        expectedTokens.add(new Token(Token.NOT_EQ, "!="));
        expectedTokens.add(new Token(Token.INT, "9"));
        expectedTokens.add(new Token(Token.SEMICOLON, ";"));
        expectedTokens.add(new Token(Token.EOF, ""));

        var l = new Lexer(input);

        for (var expectedToken:expectedTokens) {
            var tok = l.nextToken();

            assertEquals(expectedToken.getType(), tok.getType());
            assertEquals(expectedToken.getLiteral(), tok.getLiteral());
        }
    }
}