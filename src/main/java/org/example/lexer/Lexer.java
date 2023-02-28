package org.example.lexer;

import org.example.token.Token;

public class Lexer {
    private String input;
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

    public Token nextToken() {
        Token tok = null;

        skipWhitespace();

        switch (this.ch) {
            case '=':
                tok = new Token(Token.ASSIGN, this.ch);
                break;
            case ';':
                tok = new Token(Token.SEMICOLON, this.ch);
                break;
            case '(':
                tok = new Token(Token.LPAREN, this.ch);
                break;
            case ')':
                tok = new Token(Token.RPAREN, this.ch);
                break;
            case ',':
                tok = new Token(Token.COMMA, this.ch);
                break;
            case '+':
                tok = new Token(Token.PLUS, this.ch);
                break;
            case '{':
                tok = new Token(Token.LBRACE, this.ch);
                break;
            case '}':
                tok = new Token(Token.RBRACE, this.ch);
                break;
            case 0:
                tok = new Token(Token.EOF, "");
                break;
            default:
                if (isLetter(this.ch)) {
                    String ident = readIdentifier();
                    return new Token((Token.lookupIdent(ident)), ident);
                } else if (isDigit(this.ch)) {
                    return new Token(Token.INT, readNumber());
                } else {
                    tok = new Token(Token.ILLEGAL, this.ch);
                }
                break;
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
