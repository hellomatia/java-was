package codesquad.database;

import codesquad.database.statement.CreateTableStatement;
import codesquad.database.statement.SQLStatement;
import codesquad.database.statement.SelectStatement;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLParser {
    private static final Pattern PATTERN = Pattern.compile("\\s*(=|,|\\(|\\)|['\"].*?['\"]|\\S+)\\s*");
    private String sql;
    private int position;
    private List<String> tokens;

    public SQLParser(String sql) {
        this.sql = sql;
        this.position = 0;
        this.tokens = tokenize(sql);
    }

    private List<String> tokenize(String sql) {
        tokens = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(sql);
        while (matcher.find()) {
            tokens.add(matcher.group(1));
        }
        return tokens;
    }

    public SQLStatement parse() throws SQLException {
        if (match("SELECT")) {
            return parseSelect();
        }
        throw new SQLException("Unsupported SQL statement");
    }

    private boolean match(String expected) {
        if (peek() != null && Objects.requireNonNull(peek()).equalsIgnoreCase(expected)) {
            consume();
            return true;
        }
        return false;
    }

    private String peek() {
        return position < tokens.size() ? tokens.get(position) : null;
    }

    private String consume() {
        return tokens.get(position++);
    }

    private CreateTableStatement parseCreateTable() throws SQLException {
        if (!match("TABLE")) {
            throw new SQLException("Expected TABLE keyword");
        }
        String tableName = consume();
        if (!match("(")) {
            throw new SQLException("Expected ( after table name");
        }
        List<String> columns = new ArrayList<>();
        do {
            columns.add(consume());
        } while (match(","));
        if (!match(")")) {
            throw new SQLException("Expected ) after column list");
        }
        return new CreateTableStatement(tableName, columns);
    }

    private SelectStatement parseSelect() throws SQLException {
        List<String> columns = new ArrayList<>();
        String tableName = "";
        Map<String, String> whereConditions = new HashMap<>();

        do {
            columns.add(consume());
        } while (match(","));

        if (!match("FROM")) {
            throw new SQLException("Expected FROM clause");
        }

        tableName = consume();

        if (match("WHERE")) {
            do {
                String column = consume();
                if (!match("=")) {
                    throw new SQLException("Expected = in WHERE clause");
                }
                String value = consume();
                whereConditions.put(column, value);
            } while (match("AND"));
        }

        return new SelectStatement(columns, tableName, whereConditions);
    }
}
