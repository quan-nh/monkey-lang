package org.example.lexer;

import org.example.token.Token;

public class Lexer {
    private final String input;
    private int position; // current char
    private int readPosition; // next char
    private char ch;

    public Lexer(String input) {
        this.input = input;
        readChar();
    }

    private void readChar() {
        if (this.readPosition >= this.input.length()) {
            this.ch = 0;
        } else {
            this.ch = this.input.charAt(this.readPosition);
        }
        this.position = this.readPosition;
        this.readPosition++;
    }

    private char peekChar() {
        if (this.readPosition >= this.input.length()) {
            return 0;
        } else {
            return this.input.charAt(this.readPosition);
        }
    }

    public Token nextToken() {
        Token tok = null;

        skipWhitespace();

        switch (this.ch) {
            case '=' -> {
                if (peekChar() == '=') {
                    var ch = this.ch;
                    readChar();
                    var literal = String.valueOf(ch) + this.ch;
                    tok = new Token(Token.EQ, literal);
                } else {
                    tok = new Token(Token.ASSIGN, this.ch);
                }
            }
            case '+' -> tok = new Token(Token.PLUS, this.ch);
            case '-' -> tok = new Token(Token.MINUS, this.ch);
            case '!' -> {
                if (peekChar() == '=') {
                    var ch = this.ch;
                    readChar();
                    var literal = String.valueOf(ch) + this.ch;
                    tok = new Token(Token.NOT_EQ, literal);
                } else {
                    tok = new Token(Token.BANG, this.ch);
                }
            }
            case '/' -> tok = new Token(Token.SLASH, this.ch);
            case '*' -> tok = new Token(Token.ASTERISK, this.ch);
            case '<' -> tok = new Token(Token.LT, this.ch);
            case '>' -> tok = new Token(Token.GT, this.ch);
            case ';' -> tok = new Token(Token.SEMICOLON, this.ch);
            case '(' -> tok = new Token(Token.LPAREN, this.ch);
            case ')' -> tok = new Token(Token.RPAREN, this.ch);
            case ',' -> tok = new Token(Token.COMMA, this.ch);
            case '{' -> tok = new Token(Token.LBRACE, this.ch);
            case '}' -> tok = new Token(Token.RBRACE, this.ch);
            case 0 -> tok = new Token(Token.EOF, "");
            default -> {
                if (isLetter(this.ch)) {
                    String ident = readIdentifier();
                    return new Token((Token.lookupIdent(ident)), ident);
                } else if (isDigit(this.ch)) {
                    return new Token(Token.INT, readNumber());
                } else {
                    tok = new Token(Token.ILLEGAL, this.ch);
                }
            }
        }

        readChar();
        return tok;
    }

    private String readNumber() {
        var pos = this.position;
        while (isDigit(this.ch)) {
            readChar();
        }
        return this.input.substring(pos, this.position);
    }

    private boolean isDigit(char ch) {
        return '0' <= ch && ch <= '9';
    }

    private void skipWhitespace() {
        while (this.ch == ' ' || this.ch == '\t' || this.ch == '\n' || this.ch == '\r') {
            readChar();
        }
    }

    private String readIdentifier() {
        var pos = this.position;
        while (isLetter(this.ch)) {
            readChar();
        }
        return this.input.substring(pos, this.position);
    }

    private boolean isLetter(char ch) {
        return 'a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z' || ch == '_';
    }
}
