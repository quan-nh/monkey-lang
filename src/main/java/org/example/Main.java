package org.example;

import java.io.IOException;
import org.example.repl.Repl;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("This is the Monkey programming language!");
        System.out.println("Feel free to type in commands");
        Repl.start();
    }
}
