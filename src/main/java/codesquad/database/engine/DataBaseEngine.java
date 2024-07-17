package codesquad.database.engine;

import codesquad.database.statement.SelectStatement;
import codesquad.database.jdbc.CsvDataBaseResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.ResultSet;
import java.util.*;

public class DataBaseEngine {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseEngine.class);
    private static final String DATA_DIR = "db_data/";
    private Map<String, List<String>> tableColumns = new HashMap<>();

    public DataBaseEngine() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        loadTableStructures();
    }

    private void loadTableStructures() {
        File dir = new File(DATA_DIR);
        for (File file : Objects.requireNonNull(dir.listFiles((d, name) -> name.endsWith(".csv")))) {
            String tableName = file.getName().replace(".csv", "");
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String headerLine = br.readLine();
                if (headerLine != null) {
                    tableColumns.put(tableName, Arrays.asList(headerLine.split(",")));
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void createTable(String tableName, List<String> columns) {
        if (tableColumns.containsKey(tableName)) {
            logger.error("Table already exists: {}", tableName);
        }
        tableColumns.put(tableName, columns);
        saveTableStructure(tableName, columns);
    }

    private void saveTableStructure(String tableName, List<String> columns) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_DIR + tableName + ".csv"))) {
            writer.println(String.join(",", columns));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void insert(String tableName, List<String> values) {
        List<String> columns = tableColumns.get(tableName);
        if (columns == null) {
            logger.error("Table {} does not exist", tableName);
            return;
        }
        if (columns.size() != values.size()) {
            logger.error("Table {} does not have the same number of columns", tableName);
            return;
        }
        appendRowToCSV(tableName, values);
    }

    private void appendRowToCSV(String tableName, List<String> values) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_DIR + tableName + ".csv", true))) {
            writer.println(String.join(",", values));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public ResultSet executeSelect(SelectStatement selectStatement) {
        List<Map<String, String>> results = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIR + selectStatement.getTableName() + ".csv"))) {
            String line;
            List<String> columns = tableColumns.get(selectStatement.getTableName());
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < columns.size(); i++) {
                    row.put(columns.get(i), values[i]);
                }
                if (matchesWhere(row, selectStatement.getWhereConditions())) {
                    Map<String, String> resultRow = new HashMap<>();
                    for (String column : selectStatement.getColumns()) {
                        resultRow.put(column, row.get(column));
                    }
                    results.add(resultRow);
                }
            }
        } catch(IOException e){
            logger.error("Error reading CSV file", e);
        }
        return new CsvDataBaseResultSet(results);
    }

    private boolean matchesWhere(Map<String, String> row, Map<String, String> whereConditions) {
        for (Map.Entry<String, String> condition : whereConditions.entrySet()) {
            if (!condition.getValue().equals(row.get(condition.getKey()))) {
                return false;
            }
        }
        return true;
    }
}

