package com.base.framework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Simple DB helper for inserting test results into MySQL.
 * Reads DB config from ConfigReader:
 *   db.url, db.user, db.password
 *
 * Note: For production or heavy load use a connection pool (HikariCP).
 */
public class DbManager {


	
    private static final String INSERT_SQL = 
        "INSERT INTO test_results " +
        		"(run_id, test_name, status, browser, duration_ms, screenshot_path, error_message, executed_by, extent_report_path, created_at) " +
        		"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    
    
    private static String url = ConfigReader.get("db.url");
    private static String user = ConfigReader.get("db.user");
    private static String password = ConfigReader.get("db.password");

    // Helper: create connection
    private static Connection getConnection() throws SQLException {
    	System.out.println(ConfigReader.get("db.url"));
        return DriverManager.getConnection(url, user, password);
    }
    
    public static Timestamp nowUtc() {
        return Timestamp.from(Instant.now());
    }    

    /**
     * Insert a single test result row. This method intentionally
     * catches exceptions and prints to stderr so it does not fail tests.
     */
    public static void insertTestResult(String runId,
                                        String testName,
                                        String status,
                                        String browser,
                                        long durationMs,
                                        String screenshotPath,
                                        String errorMessage,
                                        String executedBy,
                                        String extentReportPath
    									) {
    	
        // Basic validation: avoid NullPointer in JDBC
        if (testName == null) testName = "UNKNOWN_TEST";
        if (status == null) status = "UNKNOWN";
        if (browser == null) browser = ConfigReader.get("browser"); // fallback

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setString(1, runId);
            ps.setString(2, testName);
            ps.setString(3, status);
            ps.setString(4, browser);
            ps.setLong(5, durationMs);
            ps.setString(6, screenshotPath);
            ps.setString(7, errorMessage);
            ps.setString(8, executedBy);
            ps.setString(9, extentReportPath);
            ps.setTimestamp(10, nowUtc());
            
            ps.executeUpdate();

        } catch (SQLException e) {
            // Log but don't throw - DB write should not fail tests
            System.err.println("DbManager - Failed to insert test result: " + e.getMessage());
            // For debugging you may print stack trace:
            // e.printStackTrace();
        } catch (Exception e) {
            System.err.println("DbManager - Unexpected exception: " + e.getMessage());
        }
    }
}
