package org.example.repl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import org.example.lexer.Lexer;
import org.example.token.Token;

public class Repl {
    private static final String PROMPT = ">> ";

    public static void start() throws IOException {
        var input = new InputStreamReader(System.in);
        var reader = new BufferedReader(input);

        for (; ; ) {
            System.out.print(PROMPT);

            var line = reader.readLine();
            var l = new Lexer(line);

            for (var tok = l.nextToken(); !Objects.equals(tok.getType(), Token.EOF); tok = l.nextToken()) {
                System.out.println(tok);
            }
        }
    }
}
