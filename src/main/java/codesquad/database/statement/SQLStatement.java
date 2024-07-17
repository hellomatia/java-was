package codesquad.database.statement;

import codesquad.database.engine.DataBaseEngine;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLStatement {
    ResultSet execute(DataBaseEngine db) throws SQLException;
}
