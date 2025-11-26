/*
 FileName: AdminDB.java
 Author: Lior Shalom
 Date: 25/08/24
 reviewer:Haiam
*/


package admin_db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;


public class AdminDB {
    private static final String DB_URL = "jdbc:mysql://localhost/";
    private static final String USER = "root";
    private static final String PASS = "Aa123456";
    private static final String CREATE = "CREATE DATABASE IF NOT EXISTS AdminDB";
    private static final String USE_DB = "USE AdminDB";
    private static final String ADD_SPLIT = "','";
    private static final String TABLE_COMP;
    private static final String TABLE_PROD;
    private static final String TRIGGER;
    private static final String TRIGGER_2;
    private static final String TRIGGER_3;

    // Load MySQL JDBC driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            try {
                // Try older driver class name for compatibility
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                System.err.println("MySQL JDBC Driver not found. Please add mysql-connector-java.jar to your classpath.");
                System.err.println("Download from: https://dev.mysql.com/downloads/connector/j/");
                throw new RuntimeException("MySQL JDBC Driver not found", ex);
            }
        }
    }

    public AdminDB() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(CREATE);
            stmt.executeUpdate(USE_DB);
            stmt.executeUpdate(TABLE_COMP);
            stmt.executeUpdate(TABLE_PROD);
            stmt.executeUpdate(TRIGGER);
            stmt.executeUpdate(TRIGGER_2);
            stmt.executeUpdate(TRIGGER_3);

            System.out.println("DB is ON ");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        TABLE_COMP = "CREATE TABLE IF NOT EXISTS Companies " +
                "(companyID INT AUTO_INCREMENT PRIMARY KEY, " +
                " companyName VARCHAR(50) UNIQUE not NULL, " +
                " country VARCHAR(50) not NULL, " +
                " city VARCHAR(50) not NULL, " +
                " address VARCHAR(50) not NULL, " +
                " contactName VARCHAR(50) not NULL, " +
                " contactMail VARCHAR(50) not NULL, " +
                " contactPhone VARCHAR(50) not NULL, " +
                " createdAt DATE not NULL, " +
                " updateAt DATETIME not NULL )";

        TABLE_PROD = "CREATE TABLE IF NOT EXISTS Products " +
                "(productID INT PRIMARY KEY AUTO_INCREMENT, " +
                " companyID INT NOT NULL, " +
                " model VARCHAR(50) NOT NULL, " +
                " version VARCHAR(50) NOT NULL, " +
                " registerAt DATETIME NOT NULL, " +
                " FOREIGN KEY (companyID) REFERENCES Companies(companyID) )";

        TRIGGER = "CREATE TRIGGER IF NOT EXISTS created_at_trigger " +
                " BEFORE INSERT ON Companies FOR EACH ROW SET NEW.createdAt = CURRENT_DATE() ,  NEW.updateAt = NOW() ";

        TRIGGER_2 = "CREATE TRIGGER IF NOT EXISTS start_registerAt_trigger" +
                " BEFORE INSERT ON Products FOR EACH ROW SET NEW.registerAt = NOW()";

        TRIGGER_3 = "CREATE TRIGGER IF NOT EXISTS update_at_trigger " +
                " BEFORE UPDATE ON Companies FOR EACH ROW SET NEW.updateAt = NOW() ";
    }

    public void RegisterCompany(Company comp) {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO Companies");
        sql.append(" (companyName , country, city, address, contactName, contactMail, contactPhone) VALUE ('");
        sql.append(comp.companyName);
        sql.append(ADD_SPLIT);
        sql.append(comp.country);
        sql.append(ADD_SPLIT);
        sql.append(comp.city);
        sql.append(ADD_SPLIT);
        sql.append(comp.address);
        sql.append(ADD_SPLIT);
        sql.append(comp.contactName);
        sql.append(ADD_SPLIT);
        sql.append(comp.contactMail);
        sql.append(ADD_SPLIT);
        sql.append(comp.contactPhone);
        sql.append("')");

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(USE_DB);
            stmt.executeUpdate(sql.toString());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void RegisterProduct(Product prod) {
        StringBuilder sql = new StringBuilder();

        String SQL = "INSERT INTO Products (companyID, model, version) VALUES(?, ?, ?)";
        String QUERY_GET_ID = "SELECT companyID FROM Companies WHERE companyName = '" + prod.companyName + "'";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(USE_DB);

            ResultSet rs = stmt.executeQuery(QUERY_GET_ID);
            rs.next();
            pstmt.setInt(1, rs.getInt(1));
            pstmt.setString(2, prod.model);
            pstmt.setString(3, prod.version);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Company upDateCompanyInfo(String companyName, String field, String value) {

        String SQL = "UPDATE Companies SET " + field + " = ? WHERE companyName = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(USE_DB);

            pstmt.setString(1, value);
            pstmt.setString(2, companyName);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                return getCompanyInfo(companyName);
            } else {
                throw new SQLException("No company found by that name" + companyName);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Company getCompanyInfo(String companyName) {
        String[] columns = {"companyName", "country", "city", "address", "contactName", "contactMail", "contactPhone"};
        String companyField = "SELECT companyName, country, city, address, contactName,contactMail, contactPhone " +
                "FROM Companies WHERE companyName = '" + companyName + "'";
        String[] values = new String[columns.length];
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(USE_DB);

            ResultSet rs = stmt.executeQuery(companyField);

            while (rs.next()) {
                for (int i = 0; i < values.length; ++i) {
                    values[i] = rs.getString(columns[i]);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Company(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
    }

    public Collection<Product> getProductsInfo(String companyName) {

        Collection<Product> products = new ArrayList<>();
        String queryGetId = "SELECT companyID FROM Companies WHERE companyName = ?";  //do wit companyName
        String queryGetProducts = "SELECT model, version FROM Products WHERE companyID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            PreparedStatement pstmtCompany = conn.prepareStatement(queryGetId);
            PreparedStatement pstmtProducts = conn.prepareStatement(queryGetProducts)) {

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(USE_DB);

            pstmtCompany.setString(1, companyName);
            ResultSet rsCompany = pstmtCompany.executeQuery();

            if (rsCompany.next()) {
                int companyID = rsCompany.getInt(1);

                pstmtProducts.setInt(1, companyID);
                ResultSet rsProducts = pstmtProducts.executeQuery();

                while (rsProducts.next()) {
                    String model = rsProducts.getString("model");
                    String version = rsProducts.getString("version");
                    products.add(new Product(companyName, model, version));
                }
            } else {
                throw new SQLException("didnt find company Name");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    protected static class Company {
        protected String companyName;
        protected String country;
        protected String city;
        protected String address;
        protected String contactName;
        protected String contactMail;
        protected String contactPhone;

        protected Company(String companyName, String country, String city, String address, String contactName,
                          String contactMail, String contactPhone) {
            this.companyName = companyName;
            this.country = country;
            this.city = city;
            this.address = address;
            this.contactName = contactName;
            this.contactMail = contactMail;
            this.contactPhone = contactPhone;
        }
    }

    protected static class Product {
        protected String companyName;
        protected String model;
        protected String version;

        protected Product(String companyName, String model, String version) {
            this.companyName = companyName;
            this.model = model;
            this.version = version;
        }
    }


    public static void main(String[] args) throws SQLException {


        AdminDB adminDB = new AdminDB();


        AdminDB.Company company = adminDB.getCompanyInfo("asdsad");

        System.out.println(company.companyName + "   " + company.address);



    }
}





