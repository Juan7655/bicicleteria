/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bicicol;

import Interfaz.Agregar;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private String DBName;

    public DataBase() {

    }

    public Connection getConnection() {
        try {

            System.out.println("Intentando conectar");
            String driver = "com.mysql.jdbc.Driver";

            //Esta opción es para la base de datos local
            //String url = "jdbc:mysql://127.0.0.1:3306/";
            
            //String url = "jdbc:mysql://104.196.97.175:3306";
            //Esta opción es para la Base de datos en Google SQL Cloud
            String url = "jdbc:mysql://104.196.97.175:3306";
            String username = "root";
            String password = "OZKR";
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);
            

            DBName = "Bicicol";

            System.out.println("Connected");

            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Not connected: " + e);
        }

        return null;
    }

    public void post(String tabla, String datos) {

        try (Connection conn = getConnection()) {
            PreparedStatement posted = conn.prepareStatement("USE " + DBName + ";");
            posted.executeUpdate();

            String valores = getPrimarykeyDisp(tabla) + "," + datos;
            String query = "INSERT INTO " + tabla + " VALUE(" + valores + ");";

            posted = conn.prepareStatement(query);
            posted.executeUpdate();
            System.out.println("Insert completed.");
            JOptionPane.showMessageDialog(null, "Inserción exitosa");
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void delete(String tabla, String cId, int bId) {
        try (Connection conn = getConnection()) {
            PreparedStatement posted = conn.prepareStatement("USE " + DBName + ";");
            posted.executeUpdate();

            String sql = "SELECT column_name "
                    + "FROM information_schema.columns "
                    + "WHERE table_schema = 'Bicicol' AND table_name='" + tabla + "';";

            PreparedStatement getNPk = conn.prepareStatement(sql);
            System.out.println("Preparé el statment");
            ResultSet nomId = getNPk.executeQuery();
            System.out.println("Ejecuté el Query");

            nomId.next();
            nomId.next();

            String idComp = nomId.getString(1);
            nomId.next();

            String idBici = nomId.getString(1);
            System.out.println(idComp);
            System.out.println(idBici);

            String query = "DELETE FROM " + tabla + " WHERE " + idComp + " = " + cId + " AND " + idBici + " = " + bId + ";";

            PreparedStatement deleted = conn.prepareStatement(query);
            deleted.executeUpdate();

            JOptionPane.showMessageDialog(null, "Eliminación exitosa");

            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getLastPk(String tabla) {

        int pk = 1;
        try (Connection con = getConnection()) {
            PreparedStatement posted = con.prepareStatement("USE " + DBName + ";");
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
            PreparedStatement posted = con.prepareStatement("USE " + DBName + ";");
            posted.executeUpdate();

            String sql = "SELECT column_name "
                    + "FROM information_schema.columns "
                    + "WHERE table_schema = 'Bicicol' AND table_name='" + tabla + "';";

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

    public int getPrimaryKey(String nTabla, String nTipo) {

        int pk = 1;

        try (Connection con = getConnection()) {
            PreparedStatement posted = con.prepareStatement("USE " + DBName + ";");
            posted.executeUpdate();
            String sql = "SELECT column_name "
                    + "FROM information_schema.columns "
                    + "WHERE table_schema = 'Bicicol' AND table_name='" + nTabla + "';";

            PreparedStatement getNPk = con.prepareStatement(sql);
            ResultSet nomId = getNPk.executeQuery();

            nomId.next();

            String id = nomId.getString(1);

            String sql2 = "SELECT " + id + " FROM " + nTabla + " WHERE Nombre = '" + nTipo + "';";

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

    public void llenarTabla(String select, String tabla, int nCol, DefaultTableModel modelo, String condicion) {

        try (Connection con = getConnection()) {

            PreparedStatement posted = con.prepareStatement("USE " + DBName + ";");
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

    public void llenarcbTipoB(DefaultComboBoxModel modelCTipoB) {

        try (Connection con = getConnection()) {

            PreparedStatement posted = con.prepareStatement("USE " + DBName + ";");
            posted.executeUpdate();
            String sql = "SELECT * FROM TipoBicicleta;";

            PreparedStatement verTipos = con.prepareStatement(sql);
            ResultSet ver = verTipos.executeQuery();

            while (ver.next()) {
                modelCTipoB.addElement(ver.getString(2));
            }
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Agregar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void llenarcbMarca(DefaultComboBoxModel modelCMarca) {

        try (Connection con = getConnection()) {
            PreparedStatement posted = con.prepareStatement("USE " + DBName + ";");
            posted.executeUpdate();
            String sql = "SELECT * FROM Marca;";
            PreparedStatement verMarca = con.prepareStatement(sql);
            ResultSet ver = verMarca.executeQuery();

            while (ver.next()) {
                modelCMarca.addElement(ver.getString(2));
            }
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Agregar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void llenarcbMarcaC(DefaultComboBoxModel modelCMarcaC) {

        try (Connection con = getConnection()) {
            PreparedStatement posted = con.prepareStatement("USE " + DBName + ";");
            posted.executeUpdate();
            String sql = "SELECT * FROM Marca;";
            PreparedStatement verMarca = con.prepareStatement(sql);
            ResultSet ver = verMarca.executeQuery();

            while (ver.next()) {
                modelCMarcaC.addElement(ver.getString(2));
            }
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Agregar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void llenarcbTipoC(DefaultComboBoxModel modelCTipoC) {

        try (Connection con = getConnection()) {
            PreparedStatement posted = con.prepareStatement("USE " + DBName + ";");
            posted.executeUpdate();
            String sql = "SELECT * FROM TipoComponente;";
            PreparedStatement verTipos = con.prepareStatement(sql);
            ResultSet ver = verTipos.executeQuery();

            while (ver.next()) {
                modelCTipoC.addElement(ver.getString(2));
            }
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Agregar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void llenarcbTipoA(DefaultComboBoxModel modelCTipoA) {

        try (Connection con = getConnection()) {
            PreparedStatement posted = con.prepareStatement("USE " + DBName + ";");
            posted.executeUpdate();
            String sql = "SELECT * FROM TipoAccesorio;";
            PreparedStatement verTipos = con.prepareStatement(sql);
            ResultSet ver = verTipos.executeQuery();

            while (ver.next()) {
                modelCTipoA.addElement(ver.getString(2));
            }
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Agregar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void llenarcbMarcaA(DefaultComboBoxModel modelCMarcaA) {

        try (Connection con = getConnection()) {
            PreparedStatement posted = con.prepareStatement("USE " + DBName + ";");
            posted.executeUpdate();
            String sql = "SELECT * FROM Marca;";
            PreparedStatement verMarca = con.prepareStatement(sql);
            ResultSet ver = verMarca.executeQuery();

            while (ver.next()) {
                modelCMarcaA.addElement(ver.getString(2));
            }
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Agregar.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

}
