package codesquad.database.parser;

import codesquad.database.statement.CreateTableStatement;
import codesquad.database.statement.InsertStatement;
import codesquad.database.statement.SQLStatement;
import codesquad.database.statement.SelectStatement;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SQLParserTest {
    @Test
    void INSERT문_파싱을_성공한다() throws SQLException {
        String sql = "INSERT INTO test VALUES ( test1 , test2 , test3 )";
        SQLParser sqlParser = new SQLParser(sql);
        SQLStatement statement = sqlParser.parse();

        assertNotNull(statement);
        assertInstanceOf(InsertStatement.class, statement);
    }

    @Test
    void CREATE문_파싱을_성공한다() throws SQLException {
        String sql = "CREATE TABLE  test ( test1 , test2 , test3 )";
        SQLParser sqlParser = new SQLParser(sql);
        SQLStatement statement = sqlParser.parse();

        assertNotNull(statement);
        assertInstanceOf(CreateTableStatement.class, statement);
    }

    @Test
    void SELECT문_파싱을_성공한다() throws SQLException {
        String sql = "SELECT test1 , test2 , test3 FROM test";
        SQLParser sqlParser = new SQLParser(sql);
        SQLStatement statement = sqlParser.parse();

        assertNotNull(statement);
        assertInstanceOf(SelectStatement.class, statement);
    }
}
