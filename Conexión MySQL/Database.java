package biblioteca;//Cambiar al paquete correspondiente.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JuanDavid
 */
public class Database1 {

    private Connection conn;
    private String DBName;

    public Database1() {
    }

    /**
     * Realiza una busqueda basica en la Base de Datos. Corresponde con la
     * sintaxis MySql de: SELECT campo FROM entidad WHERE atributo LIKE
     * (condicion + "%").
     *
     * Devuelve un arreglo de Strings con el resultado.
     *
     * @param select
     * @param entidad
     * @param atributo
     * @param condicion
     * @return
     * @throws Exception
     */
    public ArrayList<String> search(String select, String entidad, String atributo, String condicion) throws Exception {
        condicion = ((condicion.equals("empty")) || (condicion.equals("0"))) ? null : condicion;
        try {
            ArrayList<String> array;
            getConnection();
            PreparedStatement posted = conn.prepareStatement("USE " + DBName + ";");
            posted.executeUpdate();
            if (entidad == null) {
                throw new NullPointerException("Error, parametro vacio. No se puede leer una entidad Nula.");
            }
            if (condicion == null || condicion.equals("")) {
                condicion = ";";
            } else {
                if (atributo == null || atributo.equals("")) {
                    throw new NullPointerException("Error. No se puede realizar un Query bajo un condicionante "
                            + "definido en una columna nula");
                }
                condicion = "WHERE " + atributo + " LIKE '" + condicion + "%';";
            }
            select = (!select.equals("")) ? select : "*";
            posted = conn.prepareStatement("SELECT " + select
                    + " FROM " + entidad + " " + condicion);
            ResultSet result = posted.executeQuery();
            array = new ArrayList<>();
            while (result.next()) {
                array.add(result.getString(1));
            }
            return array;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Error: " + e);
        }
        return null;
    }

    //TODO Cambiar el usuario, la contraseña y la dirección de la conexión!!

    /**
     * Metodo base para conectar la aplicacion con la Base de Datos. Para
     * realizar cualquier Query, se debe llamar este metodo.
     *
     * @throws Exception
     */
    public void getConnection() throws Exception {
        try {
            String driver = "com.mysql.jdbc.Driver";
            //Esta opciÃ³n es para la base de datos local
            //String url = "jdbc:mysql://127.0.0.1:3306";

            //Esta opciÃ³n es para la Base de datos en Google SQL Cloud
            String url = "jdbc:mysql://104.196.97.175:3306";
            String username = "root";
            String password = "1234";
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            DBName = "Bicicol";
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Not connected: " + e);
        }
    }

    /**
     * Adiciona registros en la Base de Datos. Los atributos y los valores son
     * arreglos a los que se les da el formato correspondiente en una variable
     * de texto para que corresponda con la sintaxis MySql.
     *
     * @param entidad
     * @param atributos
     * @param valores
     * @throws Exception
     */
    public void post(String entidad, ArrayList<String> atributos, ArrayList<String> valores) throws Exception {
        String atrCompuesto = "", valCompuesto = "";
        if (atributos != null) {
            atrCompuesto += "(";
            for (String i : atributos) {
                if (!atrCompuesto.equals("")) {
                    atrCompuesto += ", ";
                }
                atrCompuesto += i;
            }
            atrCompuesto += ") ";
        }
        for (String i : valores) {
            if (!valCompuesto.equals("")) {
                valCompuesto += ", ";
            }
            valCompuesto += i;
        }
        try {
            getConnection();
            PreparedStatement posted = conn.prepareStatement("USE " + DBName + ";");
            posted.executeUpdate();
            posted = conn.prepareStatement("INSERT INTO " + entidad + atrCompuesto + "VALUES(" + valCompuesto + ");");
            posted.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error: " + e);
        } finally {
            System.out.println("Query terminated.");
        }
    }

    /**
     * Este metodo verifica la existencia de un cierto registro en una tabla.
     *
     * @param valor
     * @param entidad
     * @param atributo
     * @return
     */
    public boolean existe(String valor, String entidad, String atributo) {
        try {
            ResultSet result;
            getConnection();
            PreparedStatement posted = conn.prepareStatement("USE " + DBName + ";");
            posted.executeUpdate();
            posted = conn.prepareStatement("SELECT " + atributo + " FROM " + entidad
                    + " WHERE " + atributo + " = '" + valor + "';");
            result = posted.executeQuery();
            return result.wasNull();
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
        return false;
    }

    /**
     *
     * Este metodo retorna el id de un registro dado, teniendo en cuenta que los
     * parametros de atributo y valor coincidan. Hay que tener en cuenta que el
     * campo en la base de datos del id debe ser el nombre de la tabla_id.
     *
     * @param valor
     * @param entidad
     * @param atributo
     * @return
     * @throws Exception
     */
    public int getId(String valor, String entidad, String atributo) throws Exception {
        ResultSet result;
        try {
            getConnection();
            PreparedStatement posted = conn.prepareStatement("USE " + DBName + ";");
            posted.executeUpdate();
            posted = conn.prepareStatement("SELECT " + entidad + "_id FROM " + entidad
                    + " WHERE " + atributo + " = '" + valor + "';");
            result = posted.executeQuery();
            ArrayList<String> temp = new ArrayList<>();
            while (result.next()) {
                temp.add(result.getString(entidad + "_id"));
            }
            return Integer.parseInt(temp.get(0));
        } catch (SQLException | NumberFormatException ex) {
            System.out.println("Error: " + ex);
        }
        return 0;
    }

    /**
     * Cerrar la conexion
     */
    public void close() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Devuelve el nombre de la Base de Datos
     *
     * @return
     */
    public String getDBName() {
        return DBName;
    }
}
