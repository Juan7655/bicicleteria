/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Bicicol.Compra_Acc;
import Bicicol.Compra_Bici;
import Bicicol.Compra_Comp;
import Bicicol.Venta_Acc;
import Bicicol.Venta_Bici;
import Bicicol.Venta_Comp;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class Ventas extends javax.swing.JFrame implements MouseListener, KeyListener {

    private final Principal principal;
    private final DefaultComboBoxModel modelClie;
    private final DefaultComboBoxModel modelItem;
    private final DefaultComboBoxModel modeloEm;

    private DefaultTableModel modelocarrito;
    private DefaultTableModel modeloTBici;
    private DefaultTableModel modeloTComp;
    private DefaultTableModel modeloTAcc;

    private int pk_venta;
    private int pk_VentaBici;
    private int pk_VentaComp;
    private int pk_VentaAcc;

    private ArrayList<Venta_Bici> Bicicletas = new ArrayList<Venta_Bici>();
    private ArrayList<Venta_Comp> Componentes = new ArrayList<Venta_Comp>();
    private ArrayList<Venta_Acc> Accesorios = new ArrayList<Venta_Acc>();

    private final Bicicol.DataBase con = new Bicicol.DataBase();

    public Ventas(Principal principal) {
        String[] columnasBici = new String[]{"Ref", "Marca", "Tipo", "Talla", "Precio"};
        String[] columnasComp = new String[]{"Ref", "Marca", "Tipo", "Material", "Precio"};
        String[] columnasAcc = new String[]{"Ref", "Marca", "Tipo", "Material", "Precio"};
        String[] columnasCarrito = new String[]{"Ref", "Item", "Info", "Precio", "Cantidad", "Total"};

        this.pk_venta = con.getPrimarykeyDisp("venta");
        System.out.println(pk_venta);
        this.pk_VentaBici = (con.getPrimarykeyDisp("venta_Bicleta") - 1);
        this.pk_VentaComp = (con.getPrimarykeyDisp("venta_Componente") - 1);
        this.pk_VentaAcc = (con.getPrimarykeyDisp("venta_Accesorio") - 1);
        this.principal = principal;
        //Combobox
        this.modelClie = new DefaultComboBoxModel(new String[]{});
        this.modelItem = new DefaultComboBoxModel(new String[]{});
        this.modeloEm = new DefaultComboBoxModel(new String[]{});
        //Tablas
        this.modeloTComp = new DefaultTableModel(null, columnasComp);
        this.modeloTBici = new DefaultTableModel(null, columnasBici);
        this.modeloTAcc = new DefaultTableModel(null, columnasAcc);
        this.modelocarrito = new DefaultTableModel(null, columnasCarrito);

        initComponents();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        ponerEscuchar();
        llenar();
        llenarEm();
    }

    private void generarVentaInit() {

        int idVenta = con.getPrimarykeyDisp("Venta");

        Date dt = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        String Fecha = formato.format(dt);
        String select = "Documento";
        String condicion = "WHERE Nombre = '" + this.CbClie.getSelectedItem().toString() + "'";
        int Documento = Integer.parseInt(con.search(1, select, "Cliente", condicion).get(0));
        String select1 = "Documento";
        String condicion1 = "WHERE Nombre = '" + this.cbem.getSelectedItem().toString() + "'";
        int Documento1 = Integer.parseInt(con.search(1, select1, "Empleado", condicion1).get(0));

        String datos = idVenta + "," + Documento + ",'" + Fecha + "'," + Documento1 + "";
        System.out.println(datos);

        con.post("Venta", datos);
        System.out.println("Venta insertada");

    }

    private void ponerEscuchar() {
        //Mouse

        this.txtemp.addMouseListener(this);

        //Key
        this.BuscaBici.addKeyListener(this);
        this.BuscaComp.addKeyListener(this);
        this.buscaAcc.addKeyListener(this);
    }

    private void llenar() {
        con.llenarCB("Nombre", "Cliente", "", modelClie);
    }

    private void llenarEm() {
        con.llenarCB("Nombre", "Empleado", "", modeloEm);
    }

    private void buscaCli() {
        String select = "*";
        String tabla = "Cliente";
        String nomClie = this.CbClie.getSelectedItem().toString();
        String condicion = "WHERE Nombre = '" + nomClie + "'";

        con.searchProv(select, tabla, condicion);

    }


    private void Vender() {

        Venta_Bici bici;
        Venta_Comp comp;
        Venta_Acc acc;

        if (this.TbCarrito.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Seleccione los ítems que va a comprar");
        } else {
            this.generarVentaInit();

            if (!Bicicletas.isEmpty()) {

                for (int i = 0; i < Bicicletas.size(); i++) {
                    bici = this.Bicicletas.get(i);
                    String datos = bici.toString();
                    con.post("venta_Bicleta", datos);
                    con.update("Bicicleta", "UnidadesVendidas = UnidadesVendidas +" + bici.getCantidad(), "Referencia = " + bici.getRefBicicleta());
                    con.update("Bicicleta", "UnidadesStock = UnidadesStock -" + bici.getCantidad(), "Referencia = " + bici.getRefBicicleta());

                }
                Bicicletas.clear();
                System.out.println("Bicicletas insertadas");
            }
            if (!Componentes.isEmpty()) {
                for (int i = 0; i < Componentes.size(); i++) {
                    comp = this.Componentes.get(i);
                    String datos = comp.toString();
                    con.post("Venta_Componente", datos);
                    con.update("Componente", "UnidadesVendidas = UnidadesVendidas +" + comp.getCantidad(), "Referencia = " + comp.getRefComponente());
                    con.update("Componente", "UnidadesStock = UnidadesStock -" + comp.getCantidad(), "Referencia = " + comp.getRefComponente());
                }
                Componentes.clear();
                System.out.println("Componentes insertados");
            }
            if (!Accesorios.isEmpty()) {
                for (int i = 0; i < Accesorios.size(); i++) {
                    acc = this.Accesorios.get(i);
                    String datos = acc.toString();
                    con.post("venta_Accesorio", datos);
                    con.update("Accesorio", "UnidadesVendidas = UnidadesVendidas +" + acc.getCantidad(), "Referencia = " + acc.getRefAccesorio());
                    con.update("Accesorio", "UnidadesStock = UnidadesStock +" + acc.getCantidad(), "Referencia = " + acc.getRefAccesorio());
                }
                Accesorios.clear();
                System.out.println("Accesorios insertados");
            }

            this.pk_venta = this.pk_venta + 1;
            JOptionPane.showMessageDialog(null, "Datos ingresados satisfactoriamente\n Presione limpiar para hacer otra venta");
            this.Vender.setEnabled(false);
        }
    }

    private void llenarTablaBici(String busca) {

        clearTable(tbbic, modeloTBici);

        TableRowSorter<TableModel> ordena = new TableRowSorter<TableModel>(tbbic.getModel());
        tbbic.setRowSorter(ordena);

        String select = "Referencia, M.Nombre, T.Nombre, Talla, Precio";
        String condicion = "INNER JOIN Marca M ON Bicicleta.Marca = M.IdMarca "
                + "INNER JOIN TipoBicicleta T ON Bicicleta.Tipo = T.IdTipoBic "
                + "WHERE CONCAT( Referencia ,\" \", M.Nombre,\" \", T.Nombre,\" \", Talla,\" \", Precio) "
                + "LIKE \"%" + busca + "%\" ORDER BY Referencia ASC";

        con.llenarTabla(select, "Bicicleta", 5, modeloTBici, condicion);

    }

    private void llenarTablaCom(String busca) {

        clearTable(tbcompo, modeloTComp);

        TableRowSorter<TableModel> ordena = new TableRowSorter<TableModel>(tbcompo.getModel());
        tbcompo.setRowSorter(ordena);

        String select = "Referencia, M.Nombre, T.Nombre, Material, Precio";
        String condicion = "INNER JOIN Marca M ON Componente.Marca = M.IdMarca "
                + "INNER JOIN TipoComponente T ON Componente.Tipo = T.IdTipoCom "
                + "WHERE CONCAT( Referencia ,\" \", M.Nombre,\" \", T.Nombre,\" \", Material,\" \", Precio) "
                + "LIKE \"%" + busca + "%\" ORDER BY Referencia ASC";

        con.llenarTabla(select, "Componente", 5, modeloTComp, condicion);

    }

    private void llenarTablaAcc(String busca) {

        clearTable(tbAcc, modeloTAcc);

        TableRowSorter<TableModel> ordena = new TableRowSorter<TableModel>(tbAcc.getModel());
        tbAcc.setRowSorter(ordena);

        String select = "Referencia, M.Nombre, T.Nombre, Material, Precio";
        String condicion = "INNER JOIN Marca M ON Accesorio.Marca = M.IdMarca "
                + "INNER JOIN TipoAccesorio T ON Accesorio.Tipo = T.IdTipoAcc "
                + "WHERE CONCAT( Referencia ,\" \", M.Nombre,\" \", T.Nombre,\" \", Material,\" \", Precio) "
                + "LIKE \"%" + busca + "%\" ORDER BY Referencia ASC";

        con.llenarTabla(select, "Accesorio", 5, modeloTAcc, condicion);

    }

    private void clearTable(JTable tabla, DefaultTableModel modelo) {
        for (int i = 0; i < tabla.getRowCount(); i++) {
            modelo.removeRow(i);
            i -= 1;
        }
    }

    private boolean verificar(int Ref, int Cantidad, String item) {

        boolean ok = true;
        Venta_Bici bicic;
        Venta_Comp comp;
        Venta_Acc acc;

        for (int i = 0; i < this.modelocarrito.getRowCount(); i++) {
            if (this.modelocarrito.getValueAt(i, 0).equals(Ref) && this.modelocarrito.getValueAt(i, 1).equals(item)) {

                this.modelocarrito.setValueAt((Integer.parseInt(this.modelocarrito.getValueAt(i, 4).toString()) + Cantidad), i, 4);
                float total = (Integer.parseInt(this.modelocarrito.getValueAt(i, 4).toString()))
                        * (Float.parseFloat(this.modelocarrito.getValueAt(i, 3).toString()));

                this.modelocarrito.setValueAt(total, i, 5);

                if (item.equals("Bicicleta")) {
                    for (int j = 0; j < Bicicletas.size(); j++) {
                        bicic = Bicicletas.get(j);

                        bicic.setCantidad(Cantidad);
                        System.out.println("Bici setted");
                    }
                }
                if (item.equals("Componente")) {
                    for (int j = 0; j < Componentes.size(); j++) {
                        comp = Componentes.get(j);
                        // comp.setCantidad(Cantidad);
                        System.out.println("Comp setted");
                    }
                }
                if (item.equals("Accesorio")) {
                    for (int j = 0; j < Accesorios.size(); j++) {
                        acc = Accesorios.get(j);
                        //  acc.setCantidad(Cantidad);
                        System.out.println("Acc setted");
                    }
                }

                ok = false;

                this.precioTotal();
            }
        }
        return ok;
    }

    private void addBiciCarrito() {

        int index = this.tbbic.getSelectedRow();

        int IdVenBic;
        int IdVenta;
        int RefBicicleta;
        int Cantidad;

        String select = "Referencia, CONCAT(\"Bicicleta\") Item, CONCAT(T.Nombre,\" \", M.Nombre) Info, Precio";
        String condicion = "INNER JOIN Marca M ON Bicicleta.Marca = M.IdMarca "
                + "INNER JOIN TipoBicicleta T ON Bicicleta.Tipo = T.IdTipoBic "
                + "WHERE Referencia = ";

        if (index < 0) {
            JOptionPane.showMessageDialog(null, "Seleccione en la tabla la bicicleta que va a agregar", "Alerta", 0);
        } else {

            String id = tbbic.getValueAt(index, 0).toString();

            IdVenBic = (this.pk_VentaBici + 1);
            this.pk_VentaBici = IdVenBic;

            IdVenta = this.pk_venta;
            RefBicicleta = Integer.parseInt(id);
            Cantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese cantidad"));

            if (this.verificar(RefBicicleta, Cantidad, "Bicicleta")) {

                Venta_Bici bici = new Venta_Bici(IdVenBic, IdVenta, RefBicicleta, Cantidad);
                System.out.println(bici.toString());
                Bicicletas.add(bici);
                llenarCarrito(Cantidad, this.tbbic, select, condicion, "Bicicleta");
                this.precioTotal();
            }
            ArrayList<String> arry = con.search(1, "RefComponente", "Bicicleta_Componente", "WHERE RefBicicleta = " + id);
            ArrayList<String> arry2 = con.search(1, "Cantidad", "Bicicleta_Componente", "WHERE RefBicicleta = " + id);

            int IdVenComp;
            int IdVentaC;
            int RefComponente;
            int CantidadC;

            for (int i = 0; i < arry.size(); i++) {

                if (this.verificar(Integer.parseInt(arry.get(i)), Integer.parseInt(arry2.get(i)) * Cantidad, "Componente")) {

                    String select1 = "Referencia, CONCAT(\"Componente\") Item, CONCAT(T.Nombre,\" \", M.Nombre, \" \", Material) Info, Precio";
                    String condicion1 = "INNER JOIN Marca M ON Componente.Marca = M.IdMarca "
                            + "INNER JOIN TipoComponente T ON Componente.Tipo = T.IdTipoCom "
                            + "WHERE Referencia = "+Integer.parseInt(arry.get(i));

                    IdVenComp = (this.pk_VentaComp + 1);
                    this.pk_VentaComp = IdVenComp;
                    IdVentaC = this.pk_venta;
                    CantidadC = Integer.parseInt(arry2.get(i)) * Cantidad;
                    RefComponente = Integer.parseInt(arry.get(i));

                    Venta_Comp ven = new Venta_Comp(IdVenComp, IdVentaC, RefComponente, CantidadC);
                    System.out.println(ven.toString());
                    Componentes.add(ven);
                    llenarCarritoComp(CantidadC, select1, condicion1, "Componente");
                    this.precioTotal();

                }
            }

        }

    }

    private void addCompCarrito() {

        int index = this.tbcompo.getSelectedRow();

        int IdVenComp;
        int IdVenta;
        int RefComponente;
        int Cantidad;

        String select = "Referencia, CONCAT(\"Componente\") Item, CONCAT(T.Nombre,\" \", M.Nombre, \" \", Material) Info, Precio";
        String condicion = "INNER JOIN Marca M ON Componente.Marca = M.IdMarca "
                + "INNER JOIN TipoComponente T ON Componente.Tipo = T.IdTipoCom "
                + "WHERE Referencia = ";

        if (index < 0) {
            JOptionPane.showMessageDialog(null, "Seleccione en la tabla el componente que va a agregar", "Alerta", 0);
        } else {

            String id = tbcompo.getValueAt(index, 0).toString();

            IdVenComp = (this.pk_VentaComp + 1);
            this.pk_VentaComp = IdVenComp;

            IdVenta = this.pk_venta;
            RefComponente = Integer.parseInt(id);
            Cantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese cantidad"));

            if (verificar(RefComponente, Cantidad, "Componente")) {

                Venta_Comp ven = new Venta_Comp(IdVenComp, IdVenta, RefComponente, Cantidad);
                System.out.println(ven.toString());
                Componentes.add(ven);

                llenarCarrito(Cantidad, this.tbcompo, select, condicion, "Componente");
                this.precioTotal();
            }
        }

    }

    private void addAccCarrito() {
        int index = this.tbAcc.getSelectedRow();

        int IdVenAcc;
        int IdVenta;
        int RefAccesorio;
        int Cantidad;

        String select = "Referencia, CONCAT(\"Accesorio\") Item, CONCAT(T.Nombre,\" \", M.Nombre, \" \", Material) Info, Precio";
        String condicion = "INNER JOIN Marca M ON Accesorio.Marca = M.IdMarca "
                + "INNER JOIN TipoAccesorio T ON Accesorio.Tipo = T.IdTipoAcc "
                + "WHERE Referencia = ";

        if (index < 0) {
            JOptionPane.showMessageDialog(null, "Seleccione en la tabla el componente que va a agregar", "Alerta", 0);
        } else {

            String id = tbAcc.getValueAt(index, 0).toString();

            IdVenAcc = (this.pk_VentaAcc + 1);
            this.pk_VentaAcc = IdVenAcc;

            IdVenta = this.pk_venta;
            RefAccesorio = Integer.parseInt(id);
            Cantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese cantidad"));

            if (verificar(RefAccesorio, Cantidad, "Accesorio")) {
                Venta_Acc acc = new Venta_Acc(IdVenAcc, IdVenta, RefAccesorio, Cantidad);
                System.out.println(acc.toString());
                Accesorios.add(acc);

                llenarCarrito(Cantidad, this.tbAcc, select, condicion, "Accesorio");

                this.precioTotal();
            }
        }
    }

    private void llenarCarrito(int cantidad, JTable tabla, String select, String condi, String nTabla) {

        TableColumn colum = this.TbCarrito.getColumnModel().getColumn(2);
        colum.setMinWidth(150);

        int index = tabla.getSelectedRow();

        String condicion = condi + tabla.getValueAt(index, 0);

        con.llenarTabla(select, nTabla, 4, modelocarrito, condicion);

        float total = Float.parseFloat(this.modelocarrito.getValueAt((this.modelocarrito.getRowCount() - 1), 3).toString()) * cantidad;

        this.modelocarrito.setValueAt(cantidad, (this.modelocarrito.getRowCount() - 1), 4);
        this.modelocarrito.setValueAt(total, (this.modelocarrito.getRowCount() - 1), 5);

    }

    private void llenarCarritoComp(int cantidad, String select, String condi, String nTabla) {

        TableColumn colum = this.TbCarrito.getColumnModel().getColumn(2);
        colum.setMinWidth(150);

       

        con.llenarTabla(select, nTabla, 4, modelocarrito, condi);

        float total = Float.parseFloat(this.modelocarrito.getValueAt((this.modelocarrito.getRowCount() - 1), 3).toString()) * cantidad;

        this.modelocarrito.setValueAt(cantidad, (this.modelocarrito.getRowCount() - 1), 4);
        this.modelocarrito.setValueAt(total, (this.modelocarrito.getRowCount() - 1), 5);

    }

    private void deleteFromCarrito() {
        int index = this.TbCarrito.getSelectedRow();

        System.out.println(index);
        if (index < 0) {
            JOptionPane.showMessageDialog(null, "Seleccione la compra a eliminar");
        } else {
            this.modelocarrito.removeRow(index);
        }

        precioTotal();
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == this.txtemp) {
            buscaCli();
        }
    }

    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        if (e.getSource() == this.BuscaBici) {
            if (Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_ENTER) || Character.isLetter(c)) {
                llenarTablaBici(this.BuscaBici.getText());
            }
        }
        if (e.getSource() == this.BuscaComp) {

            if (Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_ENTER) || Character.isLetter(c)) {
                llenarTablaCom(this.BuscaComp.getText());
            }

        }
        if (e.getSource() == this.buscaAcc) {
            if (Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_ENTER) || Character.isLetter(c)) {
                llenarTablaAcc(this.buscaAcc.getText());
            }
        }
    }

    private float precioTotal() {

        float valor = 0;

        for (int i = 0; i < this.modelocarrito.getRowCount(); i++) {
            valor = valor
                    + (Float.parseFloat(this.modelocarrito.getValueAt(i, 5).toString()));
        }
        this.txtPrecioTotal.setText("" + valor);
        return valor;
    }

    public void limpiar() {

        this.clearTable(TbCarrito, modelocarrito);

        this.Vender.setEnabled(true);

    }

    private void btnNewCliente() {

        String doc = JOptionPane.showInputDialog("Ingrese Documento");
        String nom = JOptionPane.showInputDialog("Ingrese nombre del cliente: ");
        String tel = JOptionPane.showInputDialog("Ingrese Telefono");

        if (doc.equals("")) {
            JOptionPane.showMessageDialog(null, "Escriba un documento");
        }
        if (nom.equals("")) {
            JOptionPane.showMessageDialog(null, "Escriba un Nombre");
        } else {
            if (tel.equals("")) {
                tel = "NA";
            }
            String datos = "'" + doc + "','" + nom + "','" + tel + "'";
            con.post("Cliente", datos);
            this.llenar();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txtemp = new javax.swing.JLabel();
        CbClie = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbbic = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        BuscaBici = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        tbcl = new javax.swing.JPanel();
        tbcom = new javax.swing.JScrollPane();
        tbcompo = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        BuscaComp = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        buscaAcc = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbAcc = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        Vender = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        txtPrecioTotal = new javax.swing.JLabel();
        txtCli1 = new javax.swing.JLabel();
        cbem = new javax.swing.JComboBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        TbCarrito = new javax.swing.JTable();

        jMenuItem1.setText("Sacar de carrito");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        txtemp.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        txtemp.setText("Empleado");

        CbClie.setModel(modelClie);
        CbClie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CbClieActionPerformed(evt);
            }
        });

        jButton1.setText("Crear");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Venta:");

        tbbic.setModel(modeloTBici);
        jScrollPane3.setViewportView(tbbic);

        jLabel7.setText("Buscar");

        jButton2.setText("Añadir al carrito");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(96, 96, 96)
                                .addComponent(jButton2))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BuscaBici, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 68, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(BuscaBici, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(9, 9, 9))
        );

        jTabbedPane2.addTab("Bicicleta", jPanel3);

        tbcompo.setModel(modeloTComp
        );
        tbcom.setViewportView(tbcompo);

        jLabel8.setText("Buscar");

        jButton6.setText("Añadir al carrito");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tbclLayout = new javax.swing.GroupLayout(tbcl);
        tbcl.setLayout(tbclLayout);
        tbclLayout.setHorizontalGroup(
            tbclLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tbclLayout.createSequentialGroup()
                .addGroup(tbclLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tbclLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tbcom, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(tbclLayout.createSequentialGroup()
                        .addGroup(tbclLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tbclLayout.createSequentialGroup()
                                .addGap(102, 102, 102)
                                .addComponent(jButton6))
                            .addGroup(tbclLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(BuscaComp, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 75, Short.MAX_VALUE)))
                .addContainerGap())
        );
        tbclLayout.setVerticalGroup(
            tbclLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tbclLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(tbclLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(BuscaComp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(tbcom, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addGap(7, 7, 7))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbcl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tbcl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Componente", jPanel5);

        jLabel3.setText("Buscar");

        jButton3.setText("Añadir al carrito");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        tbAcc.setModel(modeloTAcc);
        jScrollPane1.setViewportView(tbAcc);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(jButton3)
                        .addGap(0, 93, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscaAcc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(buscaAcc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Accesorio", jPanel6);

        jLabel4.setText("Carrito de ventas:");

        jLabel5.setText("Precio total");

        jLabel6.setText("$");

        Vender.setText("Vender");
        Vender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VenderActionPerformed(evt);
            }
        });

        jButton4.setText("Limpiar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Volver");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        txtCli1.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        txtCli1.setText("Cliente");

        cbem.setModel(modeloEm);

        TbCarrito.setModel(modelocarrito);
        TbCarrito.setComponentPopupMenu(jPopupMenu1);
        TbCarrito.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane4.setViewportView(TbCarrito);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(txtCli1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CbClie, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1))
                    .addComponent(jLabel1)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(txtemp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbem, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txtPrecioTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Vender)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jScrollPane4)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtemp)
                    .addComponent(CbClie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(cbem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCli1))
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(jLabel6)
                                .addComponent(txtPrecioTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(Vender)
                                .addComponent(jButton4)
                                .addComponent(jButton5))))
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(679, 679, 679)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.setVisible(false);
        this.principal.setVisible(true);

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        limpiar();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.addAccCarrito();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        addBiciCarrito();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        addCompCarrito();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void VenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VenderActionPerformed
        Vender();
    }//GEN-LAST:event_VenderActionPerformed

    private void CbClieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbClieActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CbClieActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.btnNewCliente();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        this.deleteFromCarrito();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BuscaBici;
    private javax.swing.JTextField BuscaComp;
    private javax.swing.JComboBox CbClie;
    private javax.swing.JTable TbCarrito;
    private javax.swing.JButton Vender;
    private javax.swing.JTextField buscaAcc;
    private javax.swing.JComboBox cbem;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable tbAcc;
    private javax.swing.JTable tbbic;
    private javax.swing.JPanel tbcl;
    private javax.swing.JScrollPane tbcom;
    private javax.swing.JTable tbcompo;
    private javax.swing.JLabel txtCli1;
    private javax.swing.JLabel txtPrecioTotal;
    private javax.swing.JLabel txtemp;
    // End of variables declaration//GEN-END:variables
     @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
