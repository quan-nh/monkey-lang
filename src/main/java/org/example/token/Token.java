package org.example.token;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Token {
    public static final String ILLEGAL = "ILLEGAL";
    public static final String EOF = "EOF";

    // Identifiers + literals
    public static final String IDENT = "IDENT"; // add, foobar, x, y, ...
    public static final String INT = "INT"; // 1343456

    // Operators
    public static final String ASSIGN = "=";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String BANG = "!";
    public static final String ASTERISK = "*";
    public static final String SLASH = "/";
    public static final String LT = "<";
    public static final String GT = ">";
    public static final String EQ = "==";
    public static final String NOT_EQ = "!=";

    // Delimiters
    public static final String COMMA = ",";
    public static final String SEMICOLON = ";";

    public static final String LPAREN = "(";
    public static final String RPAREN = ")";
    public static final String LBRACE = "{";
    public static final String RBRACE = "}";

    // Keywords
    public static final String FUNCTION = "FUNCTION";
    public static final String LET = "LET";
    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";
    public static final String IF = "IF";
    public static final String ELSE = "ELSE";
    public static final String RETURN = "RETURN";

    private static final Map<String, String> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("fn", FUNCTION);
        keywords.put("let", LET);
        keywords.put("true", TRUE);
        keywords.put("false", FALSE);
        keywords.put("if", IF);
        keywords.put("else", ELSE);
        keywords.put("return", RETURN);
    }

    private String type;
    private String literal;

    public Token(String type, char ch) {
        this.type = type;
        this.literal = String.valueOf(ch);
    }

    public static String lookupIdent(String ident) {
        if (keywords.containsKey(ident)) {
            return keywords.get(ident);
        }
        return IDENT;
    }
}
