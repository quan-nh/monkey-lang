package org.example.parser;

import java.util.ArrayList;
import java.util.Objects;
import org.example.ast.Identifier;
import org.example.ast.LetStatement;
import org.example.ast.Program;
import org.example.ast.Statement;
import org.example.lexer.Lexer;
import org.example.token.Token;

public class Parser {
    private Lexer l;
    private Token curToken;
    private Token peekToken;

    public Parser(Lexer l) {
        this.l = l;

        // Read two tokens, so curToken and peekToken are both set
        nextToken();
        nextToken();
    }

    private void nextToken() {
        this.curToken = this.peekToken;
        this.peekToken = this.l.nextToken();
    }

    public Program parseProgram() {
        var program = new Program();
        var statements = new ArrayList<Statement>();

        while (this.curToken.getType() != Token.EOF) {
            var stmt = parseStatement();
            if (stmt != null) {
                statements.add(stmt);
            }
            this.nextToken();
        }

        program.setStatements(statements);
        return program;
    }

    private Statement parseStatement() {
        return switch (this.curToken.getType()) {
            case Token.LET -> parseLetStatement();
            default -> null;

        };
    }

    private LetStatement parseLetStatement() {
        var stmt = new LetStatement();
        stmt.setToken(this.curToken);

        if (!expectPeek(Token.IDENT)) {
            return null;
        }

        stmt.setName(new Identifier(this.curToken, this.curToken.getLiteral()));

        if (!expectPeek(Token.ASSIGN)) {
            return null;
        }

        while (!curTokenIs(Token.SEMICOLON)) {
            nextToken();
        }

        return stmt;
    }

    private boolean curTokenIs(String tokenType) {
        return Objects.equals(this.curToken.getType(), tokenType);
    }

    private boolean peekTokenIs(String tokenType) {
        return Objects.equals(peekToken.getType(), tokenType);
    }

    private boolean expectPeek(String tokenType) {
        if (peekTokenIs(tokenType)) {
            nextToken();
            return true;
        } else {
            return false;
        }
    }
}
