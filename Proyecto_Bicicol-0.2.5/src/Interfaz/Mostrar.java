/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author OscarLopez
 */
public class Mostrar extends javax.swing.JFrame implements KeyListener {

    private Bicicol.DataBase con = new Bicicol.DataBase();
    private Interfaz.Agregar add;

    private final Principal principal;

    private DefaultComboBoxModel modeloCTipoB;
    private DefaultComboBoxModel modeloCTipoC;
    private DefaultComboBoxModel modeloCMarca;
    private DefaultComboBoxModel modeloCMarcaC;
    private DefaultComboBoxModel modeloCTipoA;
    private DefaultComboBoxModel modeloCMarcaA;

    private DefaultTableModel modeloBici;
    private DefaultTableModel modeloComp;
    private DefaultTableModel modeloAcc;

    public Mostrar(Principal principal) {

        String[] columnasBici = new String[]{"Ref", "Marca", "Tipo", "Talla", "Stock", "Precio"};
        String[] columnasComp = new String[]{"Ref", "Marca", "Tipo", "Material", "Stock", "Precio"};
        String[] columnasAcc = new String[]{"Ref", "Marca", "Tipo", "Material", "Stock", "Precio"};

        this.modeloComp = new DefaultTableModel(null, columnasComp);
        this.modeloBici = new DefaultTableModel(null, columnasBici);
        this.modeloAcc = new DefaultTableModel(null, columnasAcc);

        this.modeloCTipoB = new DefaultComboBoxModel(new String[]{});
        this.modeloCMarca = new DefaultComboBoxModel(new String[]{});
        this.modeloCMarcaC = new DefaultComboBoxModel(new String[]{});
        this.modeloCTipoC = new DefaultComboBoxModel(new String[]{});
        this.modeloCTipoA = new DefaultComboBoxModel(new String[]{});
        this.modeloCMarcaA = new DefaultComboBoxModel(new String[]{});

        this.add = new Agregar(principal, 0);
        this.principal = principal;

        initComponents();
        this.setLocationRelativeTo(null);
        this.ponerImagen();
        this.ponerEschuchar();

        this.llenarCBs();

    }

    private void actualizarBici() {

        if (this.biciPrecio.getText().trim().equals("") || this.biciVend.getText().trim().equals("")
                || this.biciStock.getText().trim().equals("") || this.biciGar.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Llene todos los campos");
        } else {

            String ref = this.buscaBici.getText();
            String precio = this.biciPrecio.getText();
            int tipo = con.getPrimaryKeyCB("TipoBicicleta", "WHERE Nombre = '" + this.biciTipo.getSelectedItem().toString() + "'");
            String talla = this.biciTalla.getSelectedItem().toString();
            String vend = this.biciVend.getText();
            String stock = this.biciStock.getText();
            int marca = con.getPrimaryKeyCB("Marca", "WHERE Nombre = '" + this.biciMarca.getSelectedItem().toString() + "'");
            String garantia = this.biciGar.getText();

            String set = "Precio = " + precio + ", "
                    + "Tipo = " + tipo + ", "
                    + "Talla = '" + talla + "', "
                    + "UnidadesVendidas = " + vend + ", "
                    + "UnidadesStock = " + stock + ", "
                    + "Marca = '" + marca + "', "
                    + "Garantia = " + garantia;
            String condicion = "Referencia = " + ref;

            con.update("Bicicleta", set, condicion);
            JOptionPane.showMessageDialog(null, "Actualización exitosa");

        }
    }

    private void actualizarComp() {

        if (this.compPrecio.getText().trim().equals("") || this.compVend.getText().trim().equals("")
                || this.compStock.getText().trim().equals("") || this.compGar.getText().trim().equals("") || this.compCar.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Llene todos los campos");
        } else {
            String ref = this.buscaComp.getText();
            int marca = con.getPrimaryKeyCB("Marca", "WHERE Nombre = '" + this.compMarca.getSelectedItem().toString() + "'");
            String material = "'" + this.compMaterial.getSelectedItem().toString() + "'";
            String color = "'" + this.compColor.getSelectedItem().toString() + "'";
            String stock = this.compStock.getText();
            String vend = this.compVend.getText();
            String gar = this.compGar.getText();
            String precio = this.compPrecio.getText();
            int tipo = con.getPrimaryKeyCB("TipoComponente", "WHERE Nombre = '" + this.compTipo.getSelectedItem().toString() + "'");
            String car = this.compCar.getText();

            String set = "Marca = " + marca + ", "
                    + "Material = " + material + ", "
                    + "Color = " + color + ", "
                    + "UnidadesStock = " + stock + ", "
                    + "UnidadesVendidas = " + vend + ", "
                    + "Garantia = " + gar + ", "
                    + "Precio = " + precio + ", "
                    + "Tipo = " + tipo + ", "
                    + "Caracteristicas = '" + car + "'";
            String condicion = "Referencia = " + ref;

            con.update("Componente", set, condicion);
            JOptionPane.showMessageDialog(null, "Actualización exitosa");
        }
    }

    private void actualizarAcc() {

        if (this.accPrecio.getText().trim().equals("") || this.accVendidas.getText().trim().equals("")
                || this.accStock.getText().trim().equals("") || this.accGar.getText().trim().equals("") || this.accCar.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Llene todos los campos");
        } else {
            String ref = this.buscaAcc.getText();

            int tipo = con.getPrimaryKeyCB("TipoAccesorio", "WHERE Nombre = '" + this.accTipo.getSelectedItem().toString() + "'");
            String precio = this.accPrecio.getText();
            String vend = this.accVendidas.getText();
            String stock = this.accStock.getText();
            String color = "'" + this.accColor.getSelectedItem().toString() + "'";
            String material = "'" + this.accMaterial.getSelectedItem().toString() + "'";
            String car = "'" + this.accCar.getText() + "'";
            String gar = this.accGar.getText();

            String set = "Tipo = " + tipo + ", "
                    + "Precio = " + precio + ", "
                    + "UnidadesVendidas = " + vend + ", "
                    + "UnidadesStock = " + stock + ", "
                    + "Color = " + color + ", "
                    + "Material = " + material + ", "
                    + "Caracteristicas = " + car + ", "
                    + "Garantia = " + gar;
            String condicion = "Referencia = " + ref;

            con.update("Accesorio", set, condicion);
            JOptionPane.showMessageDialog(null, "Actualización exitosa");
        }
    }

    private void editar() {
        int index = tabla.getSelectedRow();

        if (this.rbBici.isSelected()) {
            int idBici = (int) this.tabla.getValueAt(index, 0);
            this.jTabbedPane2.setSelectedIndex(0);
            this.buscaBici.setText("" + idBici);
            this.buscarBici();
            this.jTabbedPane1.setSelectedIndex(1);

        }
        if (this.rbComp.isSelected()) {
            int idComp = (int) this.tabla.getValueAt(index, 0);
            this.jTabbedPane2.setSelectedIndex(1);
            this.buscaComp.setText("" + idComp);
            this.buscarComp();
            this.jTabbedPane1.setSelectedIndex(1);

        }
        if (this.rbAcc.isSelected()) {
            int idAcc = (int) this.tabla.getValueAt(index, 0);
            this.jTabbedPane2.setSelectedIndex(2);
            this.buscaAcc.setText("" + idAcc);
            this.buscarAcc();
            this.jTabbedPane1.setSelectedIndex(1);
        }

    }

    private void eliminar() {

        int index = tabla.getSelectedRow();
        int id = (int) tabla.getValueAt(index, 0);

        if (rbBici.isSelected()) {
            con.delete("Bicicleta_Componente", "RefBicicleta =" + id);
            con.delete("Venta_Bicleta", "RefBicicleta = " + id);
            con.delete("Compra_Bicicleta", "RefBicicleta = " + id);
            con.delete("Bicicleta", "Referencia = " + id);
            JOptionPane.showMessageDialog(null, "Borrado existoso");
            this.llenarTablaBici(this.busca.getText());
        }
        if (rbComp.isSelected()) {
            String condicion = "RefComponente = " + id;
            con.delete("Bicicleta_Componente", condicion);
            con.delete("Venta_Componente", condicion);
            con.delete("Compra_Componente", condicion);
            con.delete("Componente", "Referencia = " + id);
            JOptionPane.showMessageDialog(null, "Borrado existoso");
            this.llenarTablaComp(this.busca.getText());
        }
        if (rbAcc.isSelected()) {
            String condicion = "RefAccesorio = " + id;
            con.delete("Venta_Accesorio", condicion);
            con.delete("Compra_Accesorio", condicion);
            con.delete("Accesorio", "Referencia = " + id);
            JOptionPane.showMessageDialog(null, "Borrado existoso");
            this.llenarTablaAcc(this.busca.getText());
        }

    }

    private void buscarBici() {

        String busca = this.buscaBici.getText();
        String select = "*";
        String condicion = "Referencia = ";

        if (con.existe("Referencia", "Bicicleta", busca)) {

            ArrayList<String> arreglo = con.search(8, select, "Bicicleta", "WHERE " + condicion + busca);
            this.biciPrecio.setText(arreglo.get(1));
            ArrayList<String> arr = con.search(1, "Nombre", "TipoBicicleta", " WHERE IdTipoBic = " + arreglo.get(0));
            this.biciTipo.setSelectedItem(arr.get(0));
            this.biciTalla.setSelectedItem(arreglo.get(3));
            this.biciVend.setText(arreglo.get(4));
            this.biciStock.setText(arreglo.get(5));
            arr = con.search(1, "Nombre", "Marca", "WHERE IdMarca = " + arreglo.get(6));
            this.biciMarca.setSelectedItem(arr.get(0));
            this.biciGar.setText(arreglo.get(7));

        } else {
            JOptionPane.showMessageDialog(null, "No existe bicicleta con esa referencia");
        }

    }

    private void buscarComp() {

        String busca = this.buscaComp.getText();
        if (con.existe("Referencia", "Componente", busca)) {
            ArrayList<String> ar = con.search(10, "*", "Componente", "WHERE Referencia =" + busca);
            ArrayList<String> arr = con.search(1, "Nombre", "Marca", "WHERE IdMarca = " + ar.get(1));
            this.compMarca.setSelectedItem(arr.get(0));
            this.compMaterial.setSelectedItem(ar.get(2));
            this.compColor.setSelectedItem(ar.get(3));
            this.compStock.setText(ar.get(4));
            this.compVend.setText(ar.get(5));
            this.compGar.setText(ar.get(6));
            this.compPrecio.setText(ar.get(7));
            arr = con.search(1, "Nombre", "TipoComponente", "WHERE IdTipoCom = " + ar.get(8));
            this.compTipo.setSelectedItem(arr.get(0));
            this.compCar.setText(ar.get(9));

        } else {
            JOptionPane.showMessageDialog(null, "No existe componente con esa referencia");
        }
    }

    private void buscarAcc() {

        String busca = this.buscaAcc.getText();

        if (con.existe("Referencia", "Accesorio", busca)) {
            ArrayList<String> arreglo = con.search(10, "*", "Accesorio", "WHERE Referencia = " + busca);
            ArrayList<String> arr = con.search(1, "Nombre", "TipoAccesorio", "WHERE IdTipoAcc = " + arreglo.get(1));
            this.accTipo.setSelectedItem(arr.get(0));
            this.accPrecio.setText(arreglo.get(2));
            this.accVendidas.setText(arreglo.get(3));
            this.accStock.setText(arreglo.get(4));
            this.accColor.setSelectedItem(arreglo.get(5));
            this.accMaterial.setSelectedItem(arreglo.get(6));
            arr = con.search(1, "Nombre", "Marca", "WHERE IdMarca = " + arreglo.get(7));
            this.accMarca.setSelectedItem(arr.get(0));
            this.accCar.setText(arreglo.get(8));
            this.accGar.setText(arreglo.get(9));

        } else {
            JOptionPane.showMessageDialog(null, "No existe accesorio con esa referencia");
        }
    }

    private void llenarCBs() {

        this.biciMarca.removeAllItems();
        this.accMarca.removeAllItems();
        this.compMarca.removeAllItems();
        this.biciTipo.removeAllItems();
        this.accTipo.removeAllItems();
        this.compTipo.removeAllItems();

        String select = "Nombre";
        String condicion = "";
        con.llenarCB(select, "TipoBicicleta", "", modeloCTipoB);
        con.llenarCB(select, "Marca", condicion, modeloCMarca);
        con.llenarCB(select, "Marca", condicion, modeloCMarcaC);
        con.llenarCB(select, "TipoComponente", condicion, modeloCTipoC);
        con.llenarCB(select, "TipoAccesorio", condicion, modeloCTipoA);
        con.llenarCB(select, "Marca", condicion, modeloCMarcaA);
    }

    private void ponerImagen() {

        Toolkit tk = Toolkit.getDefaultToolkit();
        String ruta = "./_data/bicicoogle.png";
        Image imagen = tk.createImage(ruta);
        fondo.setIcon(new ImageIcon(imagen.getScaledInstance(fondo.getWidth(), fondo.getHeight(), Image.SCALE_AREA_AVERAGING)));
        String ruta2 = "./_data/Bicicol.png";
        Image imagen2 = tk.createImage(ruta2);
        logo.setIcon(new ImageIcon(imagen2.getScaledInstance(logo.getWidth(), logo.getHeight(), Image.SCALE_AREA_AVERAGING)));

    }

    private void ponerEschuchar() {

        this.busca.addKeyListener(this);
        this.buscaBici.addKeyListener(this);
        this.buscaComp.addKeyListener(this);
        this.buscaAcc.addKeyListener(this);
        this.biciGar.addKeyListener(this);
        this.biciPrecio.addKeyListener(this);
        this.biciStock.addKeyListener(this);
        this.biciVend.addKeyListener(this);
        this.compCar.addKeyListener(this);
        this.compGar.addKeyListener(this);
        this.compPrecio.addKeyListener(this);
        this.compStock.addKeyListener(this);
        this.compVend.addKeyListener(this);
        this.accCar.addKeyListener(this);
        this.accGar.addKeyListener(this);
        this.accPrecio.addKeyListener(this);
        this.accStock.addKeyListener(this);
        this.accVendidas.addKeyListener(this);

    }

    private void llenarTablaBici(String busca) {

        clearTable(tabla, modeloBici);

        TableRowSorter<TableModel> ordena = new TableRowSorter<TableModel>(tabla.getModel());
        tabla.setRowSorter(ordena);

        String select = "Referencia, M.Nombre, T.Nombre, Talla, UnidadesStock, Precio";
        String condicion = "INNER JOIN Marca M ON Bicicleta.Marca = M.IdMarca "
                + "INNER JOIN TipoBicicleta T ON Bicicleta.Tipo = T.IdTipoBic "
                + "WHERE CONCAT( Referencia ,\" \", M.Nombre,\" \", T.Nombre,\" \", Talla,\" \", Precio) "
                + "LIKE \"%" + busca + "%\" ORDER BY Referencia ASC";

        con.llenarTabla(select, "Bicicleta", 6, modeloBici, condicion);

    }

    private void llenarTablaComp(String busca) {

        clearTable(tabla, modeloComp);

        TableRowSorter<TableModel> ordena = new TableRowSorter<TableModel>(tabla.getModel());
        tabla.setRowSorter(ordena);

        String select = "Referencia, M.Nombre, T.Nombre, Material, UnidadesStock, Precio";
        String condicion = "INNER JOIN Marca M ON Componente.Marca = M.IdMarca "
                + "INNER JOIN TipoComponente T ON Componente.Tipo = T.IdTipoCom "
                + "WHERE CONCAT( Referencia ,\" \", M.Nombre,\" \", T.Nombre,\" \", Material,\" \", Precio) "
                + "LIKE \"%" + busca + "%\" ORDER BY Referencia ASC";

        con.llenarTabla(select, "Componente", 6, modeloComp, condicion);

    }

    private void llenarTablaAcc(String busca) {

        clearTable(tabla, modeloAcc);

        TableRowSorter<TableModel> ordena = new TableRowSorter<TableModel>(tabla.getModel());
        tabla.setRowSorter(ordena);

        String select = "Referencia, M.Nombre, T.Nombre, Material, UnidadesStock, Precio";
        String condicion = "INNER JOIN Marca M ON Accesorio.Marca = M.IdMarca "
                + "INNER JOIN TipoAccesorio T ON Accesorio.Tipo = T.IdTipoAcc "
                + "WHERE CONCAT( Referencia ,\" \", M.Nombre,\" \", T.Nombre,\" \", Material,\" \", Precio) "
                + "LIKE \"%" + busca + "%\" ORDER BY Referencia ASC";

        con.llenarTabla(select, "Accesorio", 6, modeloAcc, condicion);

    }

    private void clearTable(JTable tabla, DefaultTableModel modelo) {
        for (int i = 0; i < tabla.getRowCount(); i++) {
            modelo.removeRow(i);
            i -= 1;
        }
    }

    public void btnNewColorA() {

        String nColor = JOptionPane.showInputDialog("Ingrese el nuevo color");
        this.accColor.addItem(nColor);
    }

    public void btnNewMaterialA() {
        String nColor = JOptionPane.showInputDialog("Ingrese el nuevo material");
        this.accMaterial.addItem(nColor);
    }

    public void btnNewMaterialC() {
        String nColor = JOptionPane.showInputDialog("Ingrese el nuevo material");
        this.compMaterial.addItem(nColor);
    }

    public void btnNewColorC() {
        String nColor = JOptionPane.showInputDialog("Ingrese el nuevo color");
        this.compColor.addItem(nColor);
    }

    public void btnNewTipoB() {

        String nomb = JOptionPane.showInputDialog("Ingrese nombre del tipo de bicicleta:");
        String desc = JOptionPane.showInputDialog("Ingrese descripción del tipo de bicicleta");
        if (nomb.equals("")) {
            JOptionPane.showMessageDialog(null, "Escriba un nombre");
        } else {
            if (desc.equals("")) {
                desc = "NA";
            }
            String datos = con.getPrimarykeyDisp("TipoBicicleta") + ",'" + nomb + "','" + desc + "'";
            con.post("TipoBicicleta", datos);
            this.llenarCBs();
        }
    }

    public void btnNewTipoC() {
        String nomb = JOptionPane.showInputDialog("Ingrese nombre del tipo de componente:");
        String desc = JOptionPane.showInputDialog("Ingrese descripción del tipo de componente");
        if (nomb.equals("")) {
            JOptionPane.showMessageDialog(null, "Escriba un nombre");
        } else {
            if (desc.equals("")) {
                desc = "NA";
            }
            String datos = con.getPrimarykeyDisp("TipoComponente") + ",'" + nomb + "','" + desc + "'";
            con.post("TipoComponente", datos);
            this.llenarCBs();
        }
    }

    public void btnNewTipoA() {
        String nomb = JOptionPane.showInputDialog("Ingrese nombre del tipo de accesorio:");
        String desc = JOptionPane.showInputDialog("Ingrese descripción del tipo de accesorio");
        if (nomb.equals("")) {
            JOptionPane.showMessageDialog(null, "Escriba un nombre");
        } else {
            if (desc.equals("")) {
                desc = "NA";
            }
            String datos = con.getPrimarykeyDisp("TipoAccesorio") + ",'" + nomb + "','" + desc + "'";
            con.post("TipoAccesorio", datos);
            this.llenarCBs();
        }
    }

    public void btnNewMarca() {

        String nomb = JOptionPane.showInputDialog("Ingrese nombre de la marca:");
        String desc = JOptionPane.showInputDialog("Ingrese descripción de la marca");
        if (nomb.equals("")) {
            JOptionPane.showMessageDialog(null, "Escriba un nombre");
        } else {
            if (desc.equals("")) {
                desc = "NA";
            }
            String datos = con.getPrimarykeyDisp("Marca") + ",'" + nomb + "','" + desc + "'";
            con.post("Marca", datos);

            this.llenarCBs();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (e.getSource() == this.busca) {
            if (Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_ENTER) || Character.isLetter(c)) {
                if (this.rbBici.isSelected()) {
                    this.txtRes.setText("Resultados de bicicletas:");
                    this.tabla.setModel(modeloBici);
                    this.llenarTablaBici(busca.getText());
                }
                if (this.rbComp.isSelected()) {
                    this.txtRes.setText("Resultados de componentes:");
                    this.tabla.setModel(modeloComp);
                    this.llenarTablaComp(busca.getText());

                }
                if (this.rbAcc.isSelected()) {
                    this.txtRes.setText("Resultados de accesorios");
                    this.tabla.setModel(modeloAcc);
                    this.llenarTablaAcc(busca.getText());
                }
            }
        }
        if (e.getSource() == this.biciPrecio) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }

        if (e.getSource() == this.biciVend) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.biciStock) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.biciGar) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.compStock) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.compVend) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.compGar) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.compPrecio) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.accPrecio) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.accVendidas) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.accStock) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.accGar) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent e
    ) {

    }

    @Override
    public void keyReleased(KeyEvent e
    ) {

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        popup = new javax.swing.JPopupMenu();
        menuEditar = new javax.swing.JMenuItem();
        menuEliminar = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuRadioB = new javax.swing.JMenu();
        cambiaBici = new javax.swing.JMenuItem();
        cambiaComp = new javax.swing.JMenuItem();
        cambiaAcc = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jButton7 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        fondo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        busca = new javax.swing.JTextField();
        rbBici = new javax.swing.JRadioButton();
        rbComp = new javax.swing.JRadioButton();
        rbAcc = new javax.swing.JRadioButton();
        txtRes = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        btnBuscaBici = new javax.swing.JButton();
        buscaBici = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        biciPrecio = new javax.swing.JTextField();
        biciMarca = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        biciTalla = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        biciTipo = new javax.swing.JComboBox<>();
        biciStock = new javax.swing.JTextField();
        biciVend = new javax.swing.JTextField();
        biciGar = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnBuscaComp = new javax.swing.JButton();
        buscaComp = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        compMarca = new javax.swing.JComboBox<>();
        compStock = new javax.swing.JTextField();
        compVend = new javax.swing.JTextField();
        compMaterial = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        compColor = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        compGar = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        compTipo = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        compCar = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        compPrecio = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnBuscaAcc = new javax.swing.JButton();
        buscaAcc = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        accPrecio = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        accMarca = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        accMaterial = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        accColor = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        accTipo = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        accStock = new javax.swing.JTextField();
        accVendidas = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        accGar = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        accCar = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        logo = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

        menuEditar.setText("Editar");
        menuEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditarActionPerformed(evt);
            }
        });
        popup.add(menuEditar);

        menuEliminar.setText("Eliminar");
        menuEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEliminarActionPerformed(evt);
            }
        });
        popup.add(menuEliminar);
        popup.add(jSeparator1);

        menuRadioB.setText("Cambiar a...");

        cambiaBici.setText("Bicicleta");
        cambiaBici.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiaBiciActionPerformed(evt);
            }
        });
        menuRadioB.add(cambiaBici);

        cambiaComp.setText("Componente");
        cambiaComp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiaCompActionPerformed(evt);
            }
        });
        menuRadioB.add(cambiaComp);

        cambiaAcc.setText("Accesorio");
        cambiaAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambiaAccActionPerformed(evt);
            }
        });
        menuRadioB.add(cambiaAcc);

        popup.add(menuRadioB);
        popup.add(jSeparator2);

        jButton7.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton7.setText("Nuevo");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Buscar");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnSalir.setText("Volver");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        fondo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel1.setText("Búsquedas");

        rbBici.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(rbBici);
        rbBici.setSelected(true);
        rbBici.setText("Bicicleta");
        rbBici.setName("bici"); // NOI18N
        rbBici.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbBiciActionPerformed(evt);
            }
        });

        rbComp.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(rbComp);
        rbComp.setText("Componente");
        rbComp.setName("comp"); // NOI18N
        rbComp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCompActionPerformed(evt);
            }
        });

        rbAcc.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(rbAcc);
        rbAcc.setText("Accesorio");
        rbAcc.setName("acc"); // NOI18N
        rbAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAccActionPerformed(evt);
            }
        });

        txtRes.setText("Resultados:");

        tabla.setModel(modeloBici);
        tabla.setComponentPopupMenu(popup);
        jScrollPane1.setViewportView(tabla);

        jLabel3.setText("(?)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(fondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(rbBici)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbComp)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbAcc))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(busca, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)))
                        .addGap(11, 11, 11))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSalir))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(txtRes))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(129, 129, 129)
                                .addComponent(jLabel1)))
                        .addGap(0, 142, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbBici)
                            .addComponent(rbComp)
                            .addComponent(rbAcc)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(fondo, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(21, 21, 21)
                .addComponent(txtRes)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSalir)
                .addContainerGap())
        );

        jTabbedPane1.addTab("", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));

        btnBuscaBici.setText("Buscar");
        btnBuscaBici.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscaBiciActionPerformed(evt);
            }
        });

        jLabel14.setText("Precio");

        biciMarca.setModel(modeloCMarca);

        jLabel15.setText("Marca");

        biciTalla.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "XS", "S", "M", "L", "XL" }));

        jLabel16.setText("Talla");

        jLabel17.setText("Tipo");

        biciTipo.setModel(modeloCTipoB);

        jLabel18.setText("Stock");

        jLabel19.setText("Vendidas");

        jLabel20.setText("Garantía");

        jButton1.setText("Actualizar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton14.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton14.setText("Nuevo");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton15.setText("Nueva");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addComponent(btnBuscaBici)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buscaBici, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(biciMarca, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(biciTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(biciTalla, 0, 92, Short.MAX_VALUE)
                            .addComponent(biciPrecio))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(biciStock, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton14)
                                .addGap(14, 14, 14)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(biciVend, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(biciGar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel19)
                                .addComponent(jLabel18))
                            .addComponent(jLabel20)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscaBici)
                    .addComponent(buscaBici, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(biciPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(biciMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(biciTalla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(biciTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(biciStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(biciVend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(biciGar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20))))
                .addGap(30, 30, 30)
                .addComponent(jButton1)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Bicicleta", jPanel3);

        jPanel4.setBackground(new java.awt.Color(204, 255, 204));

        btnBuscaComp.setText("Buscar");
        btnBuscaComp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscaCompActionPerformed(evt);
            }
        });

        jLabel4.setText("Marca");

        compMarca.setModel(modeloCMarca);

        compMaterial.setEditable(true);
        compMaterial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aluminio", "Acero", "Magnesio", "Cuero", "Caucho" }));

        jLabel9.setText("Vendidas");

        jLabel7.setText("Stock");

        jLabel5.setText("Material");

        jLabel6.setText("Color");

        compColor.setEditable(true);
        compColor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Blanco", "Negro", "Gris", "Amarillo", "Azul", "Rojo", "Verde", "Morado", "Naranja", "Cafe" }));

        jLabel10.setText("Garantía");

        jLabel11.setText("Características:");

        compTipo.setModel(modeloCTipoC);

        jLabel8.setText("Tipo");

        jLabel13.setText("Precio");

        jButton2.setText("Actualizar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton9.setText("Nueva");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton10.setText("Nuevo");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton11.setText("Nuevo");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton12.setText("Nuevo");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(149, 149, 149)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(27, 27, 27)
                                .addComponent(compPrecio))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel8))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(compTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(compMarca, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(compMaterial, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(compColor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton9)
                                    .addComponent(jButton10)
                                    .addComponent(jButton11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(compStock, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel7))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(compVend, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(compGar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel10)
                                            .addComponent(jLabel9)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(2, 2, 2))))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jButton12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                                .addComponent(compCar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBuscaComp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buscaComp, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGap(32, 32, 32))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscaComp)
                    .addComponent(buscaComp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(compPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(compMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(compMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(compColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(compTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(compStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(compVend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(compGar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(compCar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Componente", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        btnBuscaAcc.setText("Buscar");
        btnBuscaAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscaAccActionPerformed(evt);
            }
        });

        jLabel21.setText("Precio");

        jLabel22.setText("Marca");

        accMarca.setModel(modeloCMarca);

        jLabel23.setText("Material");

        accMaterial.setEditable(true);
        accMaterial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Plastico", "Aluminio", "Spandex", "Cuero", "Tela", "Poliestireno" }));

        jLabel24.setText("Color");

        accColor.setEditable(true);
        accColor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Blanco", "Negro", "Gris", "Amarillo", "Azul", "Rojo", "Verde", "Morado", "Naranja", "Cafe" }));

        jLabel25.setText("Tipo");

        accTipo.setModel(modeloCTipoA);

        jLabel26.setText("Stock");

        jLabel27.setText("Vendidas");

        jLabel28.setText("Garantía");

        jLabel29.setText("Características:");

        jButton3.setText("Actualizar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton4.setText("Nueva");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton5.setText("Nuevo");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton6.setText("Nuevo");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jButton8.setText("Nuevo");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel22)
                            .addComponent(jLabel24)
                            .addComponent(jLabel25)
                            .addComponent(jLabel21))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(accPrecio)
                            .addComponent(accTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(accMarca, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(accMaterial, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(accColor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4)
                            .addComponent(jButton5)
                            .addComponent(jButton6)
                            .addComponent(jButton8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(accCar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                    .addComponent(accStock, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel26))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(accVendidas, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(accGar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel28)
                                        .addComponent(jLabel27)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel29)
                                    .addGap(2, 2, 2)))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(btnBuscaAcc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buscaAcc, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(41, 41, 41))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(149, 149, 149))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscaAcc)
                    .addComponent(buscaAcc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(accPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(accMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(accMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(accColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(accTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(accStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(accVendidas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(accGar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28))
                        .addGap(12, 12, 12)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(accCar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Accesorio", jPanel5);

        jLabel2.setText("(?)");

        jLabel12.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel12.setText("Búsquedas");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane2)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12)
                        .addGap(18, 18, 18)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.setVisible(false);
        this.principal.setVisible(true);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void rbBiciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbBiciActionPerformed
        if (tabla.getModel() == modeloComp) {
            clearTable(tabla, modeloComp);
        }
        if (tabla.getModel() == modeloAcc) {
            clearTable(tabla, modeloAcc);
        }

    }//GEN-LAST:event_rbBiciActionPerformed

    private void rbCompActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCompActionPerformed
        if (tabla.getModel() == modeloBici) {
            clearTable(tabla, modeloBici);
        }
        if (tabla.getModel() == modeloAcc) {
            clearTable(tabla, modeloAcc);
        }
    }//GEN-LAST:event_rbCompActionPerformed

    private void rbAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAccActionPerformed
        if (tabla.getModel() == modeloBici) {
            clearTable(tabla, modeloBici);
        }
        if (tabla.getModel() == modeloComp) {
            clearTable(tabla, modeloComp);
        }
    }//GEN-LAST:event_rbAccActionPerformed

    private void menuEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditarActionPerformed
        this.editar();
    }//GEN-LAST:event_menuEditarActionPerformed

    private void btnBuscaBiciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscaBiciActionPerformed
        this.buscarBici();
    }//GEN-LAST:event_btnBuscaBiciActionPerformed

    private void btnBuscaCompActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscaCompActionPerformed
        this.buscarComp();
    }//GEN-LAST:event_btnBuscaCompActionPerformed

    private void btnBuscaAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscaAccActionPerformed
        this.buscarAcc();
    }//GEN-LAST:event_btnBuscaAccActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        btnNewMarca();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        btnNewTipoB();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        btnNewMarca();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        btnNewMaterialC();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        btnNewColorC();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        btnNewTipoC();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        btnNewMarca();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        btnNewMaterialA();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        btnNewColorA();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        btnNewTipoA();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.actualizarBici();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.actualizarComp();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.actualizarAcc();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void menuEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEliminarActionPerformed
        this.eliminar();
    }//GEN-LAST:event_menuEliminarActionPerformed

    private void cambiaBiciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiaBiciActionPerformed
        this.rbBici.setSelected(true);
        if (tabla.getModel() == modeloComp) {
            clearTable(tabla, modeloComp);
        }
        if (tabla.getModel() == modeloAcc) {
            clearTable(tabla, modeloAcc);
        }

    }//GEN-LAST:event_cambiaBiciActionPerformed

    private void cambiaCompActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiaCompActionPerformed
        this.rbComp.setSelected(true);
        if (tabla.getModel() == modeloBici) {
            clearTable(tabla, modeloBici);
        }
        if (tabla.getModel() == modeloAcc) {
            clearTable(tabla, modeloAcc);
        }
    }//GEN-LAST:event_cambiaCompActionPerformed

    private void cambiaAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambiaAccActionPerformed
        this.rbAcc.setSelected(true);
        if (tabla.getModel() == modeloBici) {
            clearTable(tabla, modeloBici);
        }
        if (tabla.getModel() == modeloComp) {
            clearTable(tabla, modeloComp);
        }
    }//GEN-LAST:event_cambiaAccActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField accCar;
    private javax.swing.JComboBox<String> accColor;
    private javax.swing.JTextField accGar;
    private javax.swing.JComboBox<String> accMarca;
    private javax.swing.JComboBox<String> accMaterial;
    private javax.swing.JTextField accPrecio;
    private javax.swing.JTextField accStock;
    private javax.swing.JComboBox<String> accTipo;
    private javax.swing.JTextField accVendidas;
    private javax.swing.JTextField biciGar;
    private javax.swing.JComboBox<String> biciMarca;
    private javax.swing.JTextField biciPrecio;
    private javax.swing.JTextField biciStock;
    private javax.swing.JComboBox<String> biciTalla;
    private javax.swing.JComboBox<String> biciTipo;
    private javax.swing.JTextField biciVend;
    private javax.swing.JButton btnBuscaAcc;
    private javax.swing.JButton btnBuscaBici;
    private javax.swing.JButton btnBuscaComp;
    private javax.swing.JButton btnSalir;
    private javax.swing.JTextField busca;
    private javax.swing.JTextField buscaAcc;
    private javax.swing.JTextField buscaBici;
    private javax.swing.JTextField buscaComp;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JMenuItem cambiaAcc;
    private javax.swing.JMenuItem cambiaBici;
    private javax.swing.JMenuItem cambiaComp;
    private javax.swing.JTextField compCar;
    private javax.swing.JComboBox<String> compColor;
    private javax.swing.JTextField compGar;
    private javax.swing.JComboBox<String> compMarca;
    private javax.swing.JComboBox<String> compMaterial;
    private javax.swing.JTextField compPrecio;
    private javax.swing.JTextField compStock;
    private javax.swing.JComboBox<String> compTipo;
    private javax.swing.JTextField compVend;
    private javax.swing.JLabel fondo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel logo;
    private javax.swing.JMenuItem menuEditar;
    private javax.swing.JMenuItem menuEliminar;
    private javax.swing.JMenu menuRadioB;
    private javax.swing.JPopupMenu popup;
    private javax.swing.JRadioButton rbAcc;
    private javax.swing.JRadioButton rbBici;
    private javax.swing.JRadioButton rbComp;
    private javax.swing.JTable tabla;
    private javax.swing.JLabel txtRes;
    // End of variables declaration//GEN-END:variables

}
