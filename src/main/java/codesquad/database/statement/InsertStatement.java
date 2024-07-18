package codesquad.database.statement;

import codesquad.database.engine.CsvDataBaseEngine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InsertStatement implements SQLStatement {
    private final String tableName;
    private final List<String> values;

    public InsertStatement(String tableName, List<String> values) {
        this.tableName = tableName;
        this.values = values;
    }

    @Override
    public ResultSet execute(CsvDataBaseEngine db) throws SQLException {
        db.insert(tableName, values);
        return null;
    }
}
