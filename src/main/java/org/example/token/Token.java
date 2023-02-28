package org.example.token;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    public static final String ILLEGAL = "ILLEGAL";
    public static final String EOF = "EOF";

    // Identifiers + literals
    public static final String IDENT = "IDENT"; // add, foobar, x, y, ...
    public static final String INT = "INT"; // 1343456

    // Operators
    public static final String ASSIGN = "=";
    public static final String PLUS = "+";

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

    private static final Map<String, String> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("fn", FUNCTION);
        keywords.put("let", LET);
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
