package org.example.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.example.ast.Expression;
import org.example.ast.ExpressionStatement;
import org.example.ast.Identifier;
import org.example.ast.InfixExpression;
import org.example.ast.IntegerLiteral;
import org.example.ast.LetStatement;
import org.example.ast.PrefixExpression;
import org.example.ast.ReturnStatement;
import org.example.ast.Statement;
import org.example.lexer.Lexer;
import org.junit.jupiter.api.Test;

class ParserTest {

    @Test
    void testLetStatements() {
        var input = """
                let x = 5;
                let y = 10;
                let foobar = 838383;""";
        var l = new Lexer(input);
        var p = new Parser(l);

        var program = p.parseProgram();
        checkParseErrors(p);
        assertNotNull(program, "parseProgram() returned null");
        assertEquals(3, program.getStatements().size(), "program.Statements does not contain 3 statements.");

        List<String> expectedIdentifier = Arrays.asList("x", "y", "foobar");
        for (int i = 0; i < expectedIdentifier.size(); i++) {
            var stmt = program.getStatements().get(i);
            testLetStatement(stmt, expectedIdentifier.get(i));
        }
    }

    @Test
    void testReturnStatements() {
        var input = """
                return 5;
                return 10;
                return 993322;""";
        var l = new Lexer(input);
        var p = new Parser(l);
        var program = p.parseProgram();
        checkParseErrors(p);

        assertEquals(3, program.getStatements().size(), "program.Statements does not contain 3 statements.");

        for (var stmt : program.getStatements()) {
            var returnStmt = (ReturnStatement) stmt;
            assertEquals("return", returnStmt.tokenLiteral());
        }
    }

    @Test
    void testIdentifierExpression() {
        var input = "foobar;";
        var l = new Lexer(input);
        var p = new Parser(l);
        var program = p.parseProgram();
        checkParseErrors(p);

        assertEquals(1, program.getStatements().size(), "program has not enough statements.");

        assertTrue(program.getStatements().get(0) instanceof ExpressionStatement);
        var stmt = (ExpressionStatement) program.getStatements().get(0);

        assertTrue(stmt.getExpression() instanceof Identifier);
        var ident = (Identifier) stmt.getExpression();

        assertEquals("foobar", ident.getValue());
        assertEquals("foobar", ident.tokenLiteral());
    }

    @Test
    void testIntegerLiteralExpression() {
        var input = "5;";
        var l = new Lexer(input);
        var p = new Parser(l);
        var program = p.parseProgram();
        checkParseErrors(p);

        assertEquals(1, program.getStatements().size(), "program has not enough statements.");

        assertTrue(program.getStatements().get(0) instanceof ExpressionStatement);
        var stmt = (ExpressionStatement) program.getStatements().get(0);

        testIntegerLiteral(stmt.getExpression(), 5);
    }

    @Test
    void testParsingPrefixExpression() {
        var input = "!5;";
        var l = new Lexer(input);
        var p = new Parser(l);
        var program = p.parseProgram();
        checkParseErrors(p);

        assertEquals(1, program.getStatements().size(), "program has not enough statements.");

        assertTrue(program.getStatements().get(0) instanceof ExpressionStatement);
        var stmt = (ExpressionStatement) program.getStatements().get(0);

        assertTrue(stmt.getExpression() instanceof PrefixExpression, "exp not PrefixExpression.");
        var exp = (PrefixExpression) stmt.getExpression();

        assertEquals("!", exp.getOperator());
        testIntegerLiteral(exp.getRight(), 5);
    }

    @Test
    void testParsingInfixExpression() {
        var input = "5 + 5;";
        var l = new Lexer(input);
        var p = new Parser(l);
        var program = p.parseProgram();
        checkParseErrors(p);

        assertEquals(1, program.getStatements().size(), "program has not enough statements.");

        assertTrue(program.getStatements().get(0) instanceof ExpressionStatement);
        var stmt = (ExpressionStatement) program.getStatements().get(0);

        assertTrue(stmt.getExpression() instanceof InfixExpression, "exp not InfixExpression.");
        var exp = (InfixExpression) stmt.getExpression();

        testIntegerLiteral(exp.getLeft(), 5);
        assertEquals("+", exp.getOperator());
        testIntegerLiteral(exp.getRight(), 5);
    }

    @Test
    void testOperatorPrecedenceParsing() {
        var tests = new HashMap<String, String>();
        tests.put("-a * b", "((-a) * b)");
        tests.put("!-a", "(!(-a))");
        tests.put("a + b + c", "((a + b) + c)");
        tests.put("a + b - c", "((a + b) - c)");
        tests.put("a * b * c", "((a * b) * c)");
        tests.put("a * b / c", "((a * b) / c)");
        tests.put("a + b / c", "(a + (b / c))");
        tests.put("a + b * c + d / e - f", "(((a + (b * c)) + (d / e)) - f)");
        tests.put("3 + 4; -5 * 5", "(3 + 4)((-5) * 5)");
        tests.put("5 > 4 == 3 < 4", "((5 > 4) == (3 < 4))");
        tests.put("5 < 4 != 3 > 4", "((5 < 4) != (3 > 4))");
        tests.put("3 + 4 * 5 == 3 * 1 + 4 * 5", "((3 + (4 * 5)) == ((3 * 1) + (4 * 5)))");

        for (var k : tests.keySet()) {
            var l = new Lexer(k);
            var p = new Parser(l);
            var program = p.parseProgram();
            checkParseErrors(p);

            assertEquals(tests.get(k), program.toString());
        }
    }

    private void testIntegerLiteral(Expression exp, long value) {
        assertTrue(exp instanceof IntegerLiteral, "exp not IntegerLiteral");
        var literal = (IntegerLiteral) exp;

        assertEquals(value, literal.getValue());
        assertEquals(String.valueOf(value), literal.tokenLiteral());
    }

    private void checkParseErrors(Parser p) {
        var errors = p.getErrors();
        if (errors.size() == 0) {
            return;
        }

        System.out.println("parser has " + errors.size() + " errors");
        for (var msg : errors) {
            System.out.println("parse error: " + msg);
        }

        fail();
    }

    private void testLetStatement(Statement stmt, String name) {
        assertEquals("let", stmt.tokenLiteral());

        LetStatement letStmt = (LetStatement) stmt;
        assertEquals(name, letStmt.getName().getValue());
        assertEquals(name, letStmt.getName().tokenLiteral());
    }
}