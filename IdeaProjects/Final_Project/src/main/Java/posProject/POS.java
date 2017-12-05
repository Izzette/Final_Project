package posProject;

import java.sql.ResultSet;
import java.sql.SQLException;

class POS {

    private static ResultSet resultSetDefault;
    private static ResultSet resultSetFood;
    private static ResultSet resultSetDrinks;
    private static ResultSet resultSetMerchandise;

    private static POSModel posModelDefault;
    private static POSModel posModelFood;
    private static POSModel posModelDrinks;
    private static POSModel posModelMerchandise;
    private static POSGui posGui;



    public static void main(String[] args) {
        start();
    }


    public static void start() {
        try {
            resultSetDefault = DBConfig.setup("default_table");
            resultSetFood = DBConfig.setup("food");
            resultSetDrinks = DBConfig.setup("drinks");
            resultSetMerchandise = DBConfig.setup("merchandise");

            posModelDefault = new POSModel(resultSetDefault);
            posModelFood = new POSModel(resultSetFood);
            posModelDrinks = new POSModel(resultSetDrinks);
            posModelMerchandise = new POSModel(resultSetMerchandise);

            posGui = new POSGui(posModelDefault, posModelFood, posModelDrinks, posModelMerchandise);

        } catch (SQLException sqlee) {
            DBConfig.shutdown();
            System.exit(-1);
        }
    }

    public static void shutdown() {
        DBConfig.shutdown();
        System.exit(0);
    }
}