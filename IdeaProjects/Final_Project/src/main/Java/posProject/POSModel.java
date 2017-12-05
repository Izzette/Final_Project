package posProject;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

class POSModel extends AbstractTableModel {

    private int rowCount = 0;
    private int columnCount = 0;
    private ResultSet resultSet;

    public POSModel(ResultSet rs) {
        this.resultSet = rs;
        setup();
    }

    private void setup() {
        countRows();

        try {
            columnCount = resultSet.getMetaData().getColumnCount();

        }catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    @Override
    public String getColumnName(int index) {
        String[] columnNames = {"ID", "Product Name", "Price", "Type"};
        return columnNames[index];

    }

    public void updateResultSet(ResultSet rs){
        resultSet = rs;
        setup();
    }

    private void countRows() {
        rowCount = 0;

        try {
            resultSet.beforeFirst();

            while (resultSet.next()) {
                rowCount++;
            }
        }catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    @Override
    public int getRowCount() {
        countRows();
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            resultSet.absolute(rowIndex + 1);
            Object getValue = resultSet.getObject(columnIndex + 1);
            return getValue.toString();

        } catch (SQLException sqle) {
            return sqle.getErrorCode();
        }
    }

    public void deleteByID(int ID) {
        try {
            resultSet.beforeFirst();
            while (resultSet.next()) {
                Object getID = resultSet.getObject("ID");
                if (String.valueOf(getID).equals(String.valueOf(ID))) {
                    resultSet.deleteRow();
                    fireTableDataChanged();
                }
            }
        } catch (SQLException sqle) {
            sqle.getErrorCode();
        }
    }

    public void deleteProduct(int row) {
        try {
            resultSet.absolute(row + 1);
            resultSet.deleteRow();
            fireTableDataChanged();

        } catch (SQLException sqle) {
            sqle.getErrorCode();
        }
    }

    public void addProduct(String name, double price, String type) {
        try {
            resultSet.moveToInsertRow();
            resultSet.updateString(DBConfig.PRODUCT_NAME_COLUMN, name);
            resultSet.updateDouble(DBConfig.PRICE_COLUMN, price);
            resultSet.updateString(DBConfig.TYPE_COLUMN, type);
            resultSet.insertRow();
            resultSet.moveToCurrentRow();
            fireTableDataChanged();

        } catch (SQLException sqle) {
            sqle.getErrorCode();
        }
    }
}