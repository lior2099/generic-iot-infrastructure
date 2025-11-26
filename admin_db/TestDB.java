package admin_db;

// import org.junit.jupiter.api.Test; // Commented out as junit is not available
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

public class TestDB {

    // Helper method to replace assertEquals
    private static void assertEquals(Object expected, Object actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || !expected.equals(actual)) {
            throw new AssertionError("Expected: " + expected + ", but was: " + actual);
        }
    }

    public void CreateTest() {
        AdminDB adminDB;

        try {
            adminDB = new AdminDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        AdminDB.Company company = new AdminDB.Company("lior", "isreal", "here", "not here", "dudu", "maya", "052");
        AdminDB.Company company2 = new AdminDB.Company("lior2", "isreal", "Why", "not here", "dudu", "ido", "052");
        AdminDB.Company company3 = new AdminDB.Company("lior5", "isreal", "Why", "not here", "dudu", "ido", "052");
        adminDB.RegisterCompany(company);
        adminDB.RegisterCompany(company2);
        adminDB.RegisterCompany(company3);

        AdminDB.Product product = new AdminDB.Product("lior", "1025", "V1");
        AdminDB.Product product2 = new AdminDB.Product("lior5", "X144", "V1");
        AdminDB.Product product3 = new AdminDB.Product("lior5", "X155", "V1");
        adminDB.RegisterProduct(product);
        adminDB.RegisterProduct(product2);
        adminDB.RegisterProduct(product3);

        // Test initial city for company2 (lior2)
        AdminDB.Company companyCopy2 = adminDB.getCompanyInfo("lior2");

        assertEquals(companyCopy2.companyName, "lior2");
        assertEquals(companyCopy2.contactPhone, "052");
        assertEquals(companyCopy2.city, "Why");  // Assert initial city is "Why"

        Collection<AdminDB.Product> products = adminDB.getProductsInfo("lior5");

        for (AdminDB.Product iterProduct : products) {
            System.out.println("companyName : " + iterProduct.companyName);
            System.out.println("model : " + iterProduct.model);
            System.out.println("version : " + iterProduct.version);
        }

        // Update the city
        adminDB.upDateCompanyInfo("lior2", "city", "+pata-tikva");
        AdminDB.Company companyafterupdata = adminDB.getCompanyInfo("lior2");

        System.out.println("companyName : " + companyafterupdata.companyName);
        System.out.println("city : " + companyafterupdata.city);

        // Assert city changed (not "Why" anymore, now "+pata-tikva")
        assertEquals(companyafterupdata.city, "+pata-tikva");
        if ("Why".equals(companyafterupdata.city)) {
            throw new AssertionError("City should not be 'Why' after update! It is: " + companyafterupdata.city);
        }

        System.out.println("\nTest completed successfully!");

        // Clean up test data
        cleanupTestData();
        System.out.println("Test data cleaned up successfully!");

    }

    // Helper method to clean up test data
    private static void cleanupTestData() {
        String url = "jdbc:mysql://localhost:3306/admindb"; // use the real DB name here
        String user = "root";
        String pass = "Aa123456";

        try (Connection conn = DriverManager.getConnection(url, user, pass); Statement stmt = conn.createStatement()) {

            // 1) Delete products for your test companies
            stmt.executeUpdate(
                    "DELETE FROM products "
                    + "WHERE companyID IN ("
                    + "   SELECT companyID FROM companies "
                    + "   WHERE companyName IN ('lior', 'lior2', 'lior5')"
                    + ")"
            );

            // 2) Delete the companies themselves
            stmt.executeUpdate(
                    "DELETE FROM companies "
                    + "WHERE companyName IN ('lior', 'lior2', 'lior5')"
            );

            System.out.println("Test data cleaned up successfully from MySQL!");

        } catch (SQLException e) {
            System.err.println("Warning: Failed to clean up test data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("Running TestDB...\n");
        try {
            TestDB testDB = new TestDB();
            testDB.CreateTest();
        } catch (Exception e) {
            System.err.println("Test failed with error:");
            e.printStackTrace();
        } finally {
            cleanupTestData();
        }

    }
}
