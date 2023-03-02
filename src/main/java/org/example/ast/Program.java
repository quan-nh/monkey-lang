package org.example.ast;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Program implements Node {
    private List<Statement> statements;

    @Override
    public String tokenLiteral() {
        if (statements.size() > 0) {
            return statements.get(0).tokenLiteral();
        } else {
            return "";
        }
    }

    @Override
    public String toString() {
        var out = new StringBuilder();
        for (var s : this.statements) {
            out.append(s.toString());
        }
        return out.toString();
    }
}
