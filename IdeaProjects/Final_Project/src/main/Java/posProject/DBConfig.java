package posProject;


import java.sql.*;

public class DBConfig {

    private static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "inventory_products";
    private static final String USER = System.getenv("MYSQL_USER");
    private static final String PASSWORD = System.getenv("MYSQL_PASSWORD");

    private static Statement st = null;
    private static Connection con = null;
    private static ResultSet rs = null;
    private static PreparedStatement ps = null;

    //Table names
    final static String DRINK_TABLE_NAME = "Drinks";
    final static String FOOD_TABLE_NAME = "Food";
    final static String MERCHANDISE_TABLE_NAME = "Merchandise";
    final static String DEFAULT_TABLE_NAME = "Default_table";

    //Table columns
    final static String PK_COLUMN = "ID"; //Primary key column. Each item will have a unique ID.
    final static String PRODUCT_NAME_COLUMN = "Product_Name";
    final static String TYPE_COLUMN = "Type";
    final static String PRICE_COLUMN = "Price";

     static ResultSet setup(String tableName) throws SQLException {

        //Loads Java driver
        try {
            String Driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(Driver);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("No database drivers found. Quitting");
            System.exit(-1);  // Crashed
        }

        con = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASSWORD );
        st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);


        startUpData();

        String getStartUpData = "SELECT * FROM " + tableName;
        rs = st.executeQuery(getStartUpData);

        return rs;
    }

    static ResultSet loadData(String tableName) throws SQLException {
        if (rs != null) {
            rs.close();
        }

        String prepStatInsert = "SELECT * FROM" + tableName;

        rs = st.executeQuery(prepStatInsert);

        return rs;
    }

    static void startUpData() throws SQLException {

        //Creates tables if they don't already exist
       /*String prepStatInsert = "CREATE TABLE IF NOT EXISTS ? ( ? int NOT NULL AUTO_INCREMENT, ? varchar(50), ? double, ? varchar(11), PRIMARY KEY(?))";
        ps.setString(1, DRINK_TABLE_NAME);
        ps.setString(2, PK_COLUMN);
        ps.setString(3, PRODUCT_NAME_COLUMN);
        ps.setString(4, PRICE_COLUMN);
        ps.setString(5, TYPE_COLUMN);
        ps.setString(6, PK_COLUMN);
        ps.executeUpdate(prepStatInsert);*/

        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + DRINK_TABLE_NAME + " (" + PK_COLUMN +
                " int NOT NULL AUTO_INCREMENT, " + PRODUCT_NAME_COLUMN + " varchar(50), " + PRICE_COLUMN + " double, "
                + TYPE_COLUMN + " varchar(11), PRIMARY KEY(" + PK_COLUMN + "))";

        st.executeUpdate(createTableSQL);

        createTableSQL = "CREATE TABLE IF NOT EXISTS " + FOOD_TABLE_NAME + " (" + PK_COLUMN +
                " int NOT NULL AUTO_INCREMENT, " + PRODUCT_NAME_COLUMN + " varchar(50), " + PRICE_COLUMN + " double, "
                + TYPE_COLUMN + " varchar(11), PRIMARY KEY(" + PK_COLUMN + "))";

        st.executeUpdate(createTableSQL);

        createTableSQL = "CREATE TABLE IF NOT EXISTS " + MERCHANDISE_TABLE_NAME + " (" + PK_COLUMN +
                " int NOT NULL AUTO_INCREMENT, " + PRODUCT_NAME_COLUMN + " varchar(50), " + PRICE_COLUMN + " double, "
                + TYPE_COLUMN + " varchar(11), PRIMARY KEY(" + PK_COLUMN + "))";

        st.executeUpdate(createTableSQL);

        createTableSQL = "CREATE TABLE IF NOT EXISTS " + DEFAULT_TABLE_NAME + " (" + PK_COLUMN +
                " int NOT NULL AUTO_INCREMENT, " + PRODUCT_NAME_COLUMN + " varchar(50), " + PRICE_COLUMN + " double, "
                + TYPE_COLUMN + " varchar(11), PRIMARY KEY(" + PK_COLUMN + "))";

        st.executeUpdate(createTableSQL);
    }

    static void shutdown() {
        try {
            if (rs != null) {
                rs.close();
                System.out.println("Result set closed");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        try {
            if (st != null) {
                st.close();
                System.out.println("Statement closed");
            }
        } catch (SQLException sqle){
            //Closing the connection could throw an exception too
            sqle.printStackTrace();
        }

        try {
            if (con != null) {
                con.close();
                System.out.println("Database connection closed");
            }
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}
