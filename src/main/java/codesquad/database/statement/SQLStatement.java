package codesquad.database.statement;

import codesquad.database.engine.CsvDataBaseEngine;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLStatement {
    ResultSet execute(CsvDataBaseEngine db) throws SQLException;
}
