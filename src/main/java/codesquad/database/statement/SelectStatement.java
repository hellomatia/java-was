package codesquad.database.statement;

import codesquad.database.engine.CsvDataBaseEngine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SelectStatement implements SQLStatement {
    private final List<String> columns;
    private final String tableName;
    private final Map<String, String> whereConditions;

    public SelectStatement(List<String> columns, String tableName, Map<String, String> whereConditions) {
        this.columns = columns;
        this.tableName = tableName;
        this.whereConditions = whereConditions;
    }

    @Override
    public ResultSet execute(CsvDataBaseEngine db) throws SQLException {
        return db.executeSelect(this);
    }

    public List<String> getColumns() {
        return columns;
    }

    public String getTableName() {
        return tableName;
    }

    public Map<String, String> getWhereConditions() {
        return whereConditions;
    }
}
