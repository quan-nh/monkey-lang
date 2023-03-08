package org.example.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import org.example.ast.Expression;
import org.example.ast.ExpressionStatement;
import org.example.ast.Identifier;
import org.example.ast.IntegerLiteral;
import org.example.ast.LetStatement;
import org.example.ast.PrefixExpression;
import org.example.ast.Program;
import org.example.ast.ReturnStatement;
import org.example.ast.Statement;
import org.example.lexer.Lexer;
import org.example.token.Token;

public class Parser {
    private enum Precedence {
        LOWEST,
        EQUALS,// ==
        LESSGREATER,// > or <
        SUM,// +
        PRODUCT, // *
        PREFIX, // -X or !X
        CALL, // myFunction(X)
    }

    private Lexer l;
    private List<String> errors;

    private Token curToken;
    private Token peekToken;

    private Map<String, Supplier<Expression>> prefixParseFns;
    private Map<String, UnaryOperator<Expression>> infixParseFns;
    private Supplier<Expression> parseIdentifier = () -> new Identifier(this.curToken, this.curToken.getLiteral());

    private Supplier<Expression> parseIntegerLiteral = () -> {
        var lit = new IntegerLiteral();
        lit.setToken(this.curToken);

        try {
            var value = Long.parseLong(this.curToken.getLiteral());
            lit.setValue(value);
        } catch (Exception ex) {
            this.errors.add("could not parse " + this.curToken.getLiteral() + " as integer");
            return null;
        }
        return lit;
    };

    private Supplier<Expression> parsePrefixExpression = () -> {
        var exp = new PrefixExpression();
        exp.setToken(this.curToken);
        exp.setOperator(this.curToken.getLiteral());

        nextToken();

        exp.setRight(parseExpression(Precedence.PREFIX));

        return exp;
    };

    public Parser(Lexer l) {
        this.l = l;
        this.errors = new ArrayList<>();
        this.prefixParseFns = new HashMap<>();
        registerPrefix(Token.IDENT, parseIdentifier);
        registerPrefix(Token.INT, parseIntegerLiteral);
        registerPrefix(Token.BANG, parsePrefixExpression);
        registerPrefix(Token.MINUS, parsePrefixExpression);

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

        while (!Objects.equals(this.curToken.getType(), Token.EOF)) {
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
            default -> parseExpressionStatement();
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

    private ExpressionStatement parseExpressionStatement() {
        var stmt = new ExpressionStatement();
        stmt.setToken(this.curToken);
        stmt.setExpression(parseExpression(Precedence.LOWEST));

        while (peekTokenIs(Token.SEMICOLON)) {
            nextToken();
        }

        return stmt;
    }

    private Expression parseExpression(Precedence precedence) {
        var prefix = this.prefixParseFns.get(this.curToken.getType());
        if (prefix == null) {
            noPrefixParseFnError(this.curToken.getType());
            return null;
        }
        var leftExp = prefix.get();

        return leftExp;
    }

    private void noPrefixParseFnError(String type) {
        this.errors.add("no prefix parse function for " + type + " found");
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

    private void registerPrefix(String tokenType, Supplier<Expression> prefixParseFn) {
        this.prefixParseFns.put(tokenType, prefixParseFn);
    }

    private void registerInfix(String tokenType, UnaryOperator<Expression> infixParseFn) {
        this.infixParseFns.put(tokenType, infixParseFn);
    }
}
