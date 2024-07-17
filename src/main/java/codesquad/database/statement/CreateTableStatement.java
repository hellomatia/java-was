package codesquad.database.statement;

import codesquad.database.engine.DataBaseEngine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CreateTableStatement implements SQLStatement {
    private final String tableName;
    private final List<String> columns;

    public CreateTableStatement(String tableName, List<String> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    @Override
    public ResultSet execute(DataBaseEngine db) throws SQLException {
        db.createTable(tableName, columns);
        return null;
    }
}
