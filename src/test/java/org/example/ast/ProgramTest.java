package org.example.ast;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.example.token.Token;
import org.junit.jupiter.api.Test;

class ProgramTest {

    @Test
    void testToString() {
        var program = new Program();

        var letStmt = new LetStatement(new Token(Token.LET, "let"),
                new Identifier(new Token(Token.IDENT, "myVar"), "myVar"),
                new Identifier(new Token(Token.IDENT, "anotherVar"), "anotherVar"));

        var statements = new ArrayList<Statement>();
        statements.add(letStmt);
        program.setStatements(statements);

        assertEquals("let myVar = anotherVar;", program.toString());
    }
}