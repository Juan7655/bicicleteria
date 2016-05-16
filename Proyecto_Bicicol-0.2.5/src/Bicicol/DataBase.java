/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bicicol;

import Interfaz.Agregar;
import Interfaz.Compras;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author OscarLopez
 */
public class DataBase {

    public DataBase() {

    }

    public Connection getConnection() {
        try {

            System.out.println("Intentando conectar");
            String driver = "com.mysql.jdbc.Driver";

            //Esta opción es para la base de datos local
            String url = "jdbc:mysql://127.0.0.1:3306/";

            //Esta opción es para la Base de datos en Google SQL Cloud
            //String url = "jdbc:mysql://104.196.97.175:3306";
            String username = "root";
            String password = "OscarSQL4596";
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);

            System.out.println("Connected");

            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Not connected: " + e);
        }

        return null;
    }

    public void post(String tabla, String datos) {

        try (Connection conn = getConnection()) {
            PreparedStatement posted = conn.prepareStatement("USE Bicicol;");
            posted.executeUpdate();

            String query = "INSERT INTO " + tabla + " VALUE(" + datos + ");";

            posted = conn.prepareStatement(query);
            posted.executeUpdate();

            System.out.println("Insert completed.");

            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void delete(String tabla, String condicion) {

        try (Connection conn = getConnection()) {
            PreparedStatement posted = conn.prepareStatement("USE  Bicicol;");
            posted.executeUpdate();

            String query = "DELETE FROM " + tabla + " WHERE " + condicion + ";";

            PreparedStatement deleted = conn.prepareStatement(query);
            deleted.executeUpdate();

            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getLastPk(String tabla) {

        int pk = 1;
        try (Connection con = getConnection()) {
            PreparedStatement posted = con.prepareStatement("USE  Bicicol;");
            posted.executeUpdate();
            String sql = "SELECT column_name "
                    + "FROM information_schema.columns "
                    + "WHERE table_schema = 'Bicicol' AND table_name='" + tabla + "';";

            PreparedStatement getNPk = con.prepareStatement(sql);
            ResultSet nomId = getNPk.executeQuery();

            nomId.next();

            String id = nomId.getString(1);

            String sql2 = "SELECT (MAX(" + tabla + "." + id + ")) "
                    + "FROM " + tabla + ";";

            PreparedStatement getPk = con.prepareStatement(sql2);
            ResultSet get = getPk.executeQuery();

            get.next();

            pk = get.getInt(1);
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Agregar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return pk;
    }

    public int getPrimarykeyDisp(String tabla) {

        int pk = 1;

        try (Connection con = getConnection()) {

            PreparedStatement posted = con.prepareStatement("USE Bicicol;");
            posted.executeUpdate();

            String sql = "SELECT column_name "
                    + "FROM information_schema.columns "
                    + "WHERE table_schema = 'Bicicol' AND table_name='" + tabla + "' AND Column_key = 'PRI';";

            PreparedStatement getNPk = con.prepareStatement(sql);
            ResultSet nomId = getNPk.executeQuery();

            nomId.next();

            String id = nomId.getString(1);

            String sql2 = "SELECT (MAX(" + tabla + "." + id + ")+1) "
                    + "FROM " + tabla + ";";

            PreparedStatement getPk = con.prepareStatement(sql2);
            ResultSet get = getPk.executeQuery();

            get.next();

            pk = get.getInt(1);
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Agregar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return pk;
    }

    public int getPrimaryKeyCB(String nTabla, String condicion) {

        int pk = 1;

        try (Connection con = getConnection()) {
            PreparedStatement posted = con.prepareStatement("USE Bicicol;");
            posted.executeUpdate();
            String sql = "SELECT column_name "
                    + "FROM information_schema.columns "
                    + "WHERE table_schema = 'Bicicol' AND table_name='" + nTabla + "' AND Column_key = 'PRI';";

            PreparedStatement getNPk = con.prepareStatement(sql);
            ResultSet nomId = getNPk.executeQuery();

            nomId.next();

            String id = nomId.getString(1);

            String sql2 = "SELECT " + id + " FROM " + nTabla + " " + condicion + ";";

            PreparedStatement getPk = con.prepareStatement(sql2);
            ResultSet get = getPk.executeQuery();

            get.next();

            pk = get.getInt(1);

            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Agregar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return pk;
    }

    public void set() {
        try (Connection con = getConnection()) {
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void llenarTabla(String select, String tabla, int nCol, DefaultTableModel modelo, String condicion) {

        try (Connection con = getConnection()) {

            PreparedStatement posted = con.prepareStatement("USE Bicicol;");
            posted.executeUpdate();
            String sql = "SELECT " + select + " FROM " + tabla + " " + condicion + ";";

            PreparedStatement verTab = con.prepareStatement(sql);
            ResultSet ver = verTab.executeQuery();

            Object data[] = new Object[nCol];

            while (ver.next()) {
                for (int i = 0; i < nCol; i++) {
                    data[i] = ver.getObject(i + 1);
                }
                modelo.addRow(data);
            }

            ver.close();

        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void llenarCB(String select, String tabla, String condicion, DefaultComboBoxModel modelo) {
        try (Connection con = getConnection()) {

            PreparedStatement posted = con.prepareStatement("USE Bicicol;");
            posted.executeUpdate();
            String sql = "SELECT " + select + " FROM " + tabla + " " + condicion + ";";

            PreparedStatement verTipos = con.prepareStatement(sql);
            ResultSet ver = verTipos.executeQuery();

            while (ver.next()) {
                modelo.addElement(ver.getString(1));
            }
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Agregar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void searchProv(String select, String tabla, String condicion) {

        ResultSet ver = null;
        int i = 0;
        String[] prov = new String[6];
        try (Connection con = getConnection()) {

            PreparedStatement posted = con.prepareStatement("USE Bicicol;");
            posted.executeUpdate();

            String sql = "SELECT " + select + " FROM " + tabla + " " + condicion + ";";
            PreparedStatement verProv = con.prepareStatement(sql);
            ver = verProv.executeQuery();

            try {

                while (ver.next()) {
                    for (int j = 0; j < 6; j++) {
                        prov[j] = ver.getString(j + 1);
                    }

                }

                String show = "         Información proveedor\n\n"
                        + "Nit:                     " + prov[0] + "\n"
                        + "Nombre:          " + prov[1] + "\n"
                        + "Teléfono:         " + prov[2] + "\n"
                        + "Ciudad:            " + prov[3] + "\n"
                        + "Dirección:       " + prov[4] + "\n"
                        + "Email:              " + prov[5] + "\n";
                JOptionPane.showMessageDialog(null, show, "Proveedor", 1);

            } catch (SQLException ex) {
                Logger.getLogger(Compras.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Agregar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ArrayList<String> search(int size, String select, String tabla, String condicion) {

        ArrayList<String> array = new ArrayList<String>();
        try (Connection con = getConnection()) {

            PreparedStatement posted = con.prepareStatement("USE Bicicol;");
            posted.executeUpdate();

            String sql = "SELECT " + select + " FROM " + tabla + " " + condicion + ";";
            PreparedStatement verProv = con.prepareStatement(sql);
            ResultSet ver = verProv.executeQuery();

            try {

                while (ver.next()) {
                    for (int j = 0; j < size; j++) {
                        array.add(ver.getString(j + 1));

                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(Compras.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Agregar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return array;
    }

    public void update(String tabla, String set, String condicion) {
        try (Connection con = getConnection()) {
            PreparedStatement posted = con.prepareStatement("USE Bicicol;");
            posted.executeUpdate();
            
            String sql = "UPDATE "+tabla+" "
                    + "SET "+set+" "
                    + "WHERE "+condicion;
            
            System.out.println(sql);
            
            PreparedStatement prep = con.prepareStatement(sql);
            prep.executeUpdate();
            
            System.out.println("Updated");
                    
        } catch (SQLException ex) {
            Logger.getLogger(Agregar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}
