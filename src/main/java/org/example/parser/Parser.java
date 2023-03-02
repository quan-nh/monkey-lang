package org.example.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.example.ast.Identifier;
import org.example.ast.LetStatement;
import org.example.ast.Program;
import org.example.ast.ReturnStatement;
import org.example.ast.Statement;
import org.example.lexer.Lexer;
import org.example.token.Token;

public class Parser {
    private Lexer l;
    private Token curToken;
    private Token peekToken;
    private List<String> errors;

    public Parser(Lexer l) {
        this.l = l;
        this.errors = new ArrayList<>();

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
            case Token.RETURN -> parseReturnStatement();
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

    private Statement parseReturnStatement() {
       var stmt = new ReturnStatement();
       stmt.setToken(this.curToken);

       nextToken();

        while (!curTokenIs(Token.SEMICOLON)) {
            nextToken();
        }

       return stmt;
    }

    private boolean curTokenIs(String tokenType) {
        return Objects.equals(this.curToken.getType(), tokenType);
    }

    private boolean peekTokenIs(String tokenType) {
        return Objects.equals(this.peekToken.getType(), tokenType);
    }

    private boolean expectPeek(String tokenType) {
        if (peekTokenIs(tokenType)) {
            nextToken();
            return true;
        } else {
            peekError(tokenType);
            return false;
        }
    }

    public List<String> getErrors() {
        return errors;
    }

    private void peekError(String tokenType) {
        this.errors.add("expected next token to be " + tokenType + ", got " + this.peekToken.getType() + " instead");
    }
}
