/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Bicicol.Compra_Acc;
import Bicicol.Compra_Bici;
import Bicicol.Compra_Comp;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author OscarLopez
 */
public class Compras extends javax.swing.JFrame implements MouseListener, KeyListener {

    private final Principal principal;
    private final DefaultComboBoxModel modeloProv;
    private final DefaultComboBoxModel modelItem;

    private DefaultTableModel modeloCarrito;
    private DefaultTableModel modeloTBici;
    private DefaultTableModel modeloTComp;
    private DefaultTableModel modeloTAcc;
    private int pk_compra;
    private int pk_CompraBici;
    private int pk_CompraComp;
    private int pk_CompraAcc;
    

    private ArrayList<Compra_Bici> Bicicletas = new ArrayList<Compra_Bici>();
    private ArrayList<Compra_Comp> Componentes = new ArrayList<Compra_Comp>();
    private ArrayList<Compra_Acc> Accesorios = new ArrayList<Compra_Acc>();

    private final Bicicol.DataBase con = new Bicicol.DataBase();

    public Compras(Principal principal) {

        String[] columnasBici = new String[]{"Ref", "Marca", "Tipo", "Talla", "Precio"};
        String[] columnasComp = new String[]{"Ref", "Marca", "Tipo", "Material", "Precio"};
        String[] columnasAcc = new String[]{"Ref", "Marca", "Tipo", "Material", "Precio"};
        String[] columnasCarrito = new String[]{"Ref", "Item", "Info", "Precio", "Cantidad", "Total"};

        this.pk_compra = con.getPrimarykeyDisp("Compra");
        System.out.println(pk_compra);
        this.pk_CompraBici = (con.getPrimarykeyDisp("Compra_Bicicleta") - 1);
        this.pk_CompraComp = (con.getPrimarykeyDisp("Compra_Componente") - 1);
        this.pk_CompraAcc = (con.getPrimarykeyDisp("Compra_Accesorio") - 1);
        this.principal = principal;
        //Combobox
        this.modeloProv = new DefaultComboBoxModel(new String[]{});
        this.modelItem = new DefaultComboBoxModel(new String[]{});
        //Tablas
        this.modeloTComp = new DefaultTableModel(null, columnasComp);
        this.modeloTBici = new DefaultTableModel(null, columnasBici);
        this.modeloTAcc = new DefaultTableModel(null, columnasAcc);
        this.modeloCarrito = new DefaultTableModel(null, columnasCarrito);

        initComponents();
        this.setLocationRelativeTo(null);
        this.ponerImagen();
        //generarCompraInit();
        ponerEscuchar();
        llenar();

    }

    private void generarCompraInit() {

        int idCompra = this.pk_compra;

        float valor = precioTotal();

        System.out.println(valor);

        Date dt = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        String fechaAct = formato.format(dt);

        String datos = idCompra + "," + valor + ",'" + fechaAct + "'";
        System.out.println(datos);

        con.postInto("Compra", datos);
        System.out.println("Compra insertada");

    }

    private void generarCompraProv() {

        int IdComPro = con.getPrimarykeyDisp("Compra_Proveedor");
        int idCompra = this.pk_compra;
        String select = "NitEmpresa";
        String condicion = "WHERE NombreEmpresa = '" + this.cbProv.getSelectedItem().toString() + "'";
        int NitEmpresa = Integer.parseInt(con.search(1, select, "Proveedor", condicion).get(0));
        float valor = precioTotal();

        String datos = IdComPro + "," + idCompra + "," + NitEmpresa + "," + valor;

        con.postInto("Compra_Proveedor", datos);
        System.out.println("compra_proveedor insertada");

    }

    private void ponerEscuchar() {
        //Mouse
        this.txtProv.addMouseListener(this);
        //Key
        this.buscaBici.addKeyListener(this);
        this.buscaComp.addKeyListener(this);
        this.buscaAcc.addKeyListener(this);
    }

    private void llenar() {
        con.llenarcbProveedor(modeloProv);
    }

    private void buscaProv() {
        String select = "*";
        String tabla = "Proveedor";
        String nomProv = this.cbProv.getSelectedItem().toString();
        String condicion = "WHERE NombreEmpresa = '" + nomProv + "'";

        con.searchProv(select, tabla, condicion);

    }

    private void Comprar() {

        Compra_Bici bici;
        Compra_Comp comp;
        Compra_Acc acc;

        this.generarCompraInit();
        this.generarCompraProv();

        if (!Bicicletas.isEmpty()) {

            for (int i = 0; i < Bicicletas.size(); i++) {
                bici = this.Bicicletas.get(i);
                String datos = bici.toString();
                con.postInto("Compra_Bicicleta", datos);

            }
            Bicicletas.clear();
            System.out.println("Bicicletas insertadas");
        }
        if (!Componentes.isEmpty()) {
            for (int i = 0; i < Componentes.size(); i++) {
                comp = this.Componentes.get(i);
                String datos = comp.toString();
                con.postInto("Compra_Componente", datos);
            }
            Componentes.clear();
            System.out.println("Componentes insertados");
        }
        if (!Accesorios.isEmpty()) {
            for (int i = 0; i < Accesorios.size(); i++) {
                acc = this.Accesorios.get(i);
                String datos = acc.toString();
                con.postInto("Compra_Accesorio", datos);
            }
            Accesorios.clear();
            System.out.println("Accesorios insertados");
        }

        this.pk_compra = this.pk_compra + 1;
        JOptionPane.showMessageDialog(null, "Datos ingresados satisfactoriamente");
        
    }

    private void llenarTablaBici(String busca) {

        clearTable(tbBici, modeloTBici);

        TableRowSorter<TableModel> ordena = new TableRowSorter<TableModel>(tbBici.getModel());
        tbBici.setRowSorter(ordena);

        String select = "Referencia, M.Nombre, T.Nombre, Talla, Precio";
        String condicion = "INNER JOIN Marca M ON Bicicleta.Marca = M.IdMarca "
                + "INNER JOIN TipoBicicleta T ON Bicicleta.Tipo = T.IdTipoBic "
                + "WHERE CONCAT( Referencia ,\" \", M.Nombre,\" \", T.Nombre,\" \", Talla,\" \", Precio) "
                + "LIKE \"%" + busca + "%\" ORDER BY Referencia ASC";

        con.llenarTabla(select, "Bicicleta", 5, modeloTBici, condicion);

    }

    private void llenarTablaComp(String busca) {

        clearTable(tbComp, modeloTComp);

        TableRowSorter<TableModel> ordena = new TableRowSorter<TableModel>(tbComp.getModel());
        tbComp.setRowSorter(ordena);

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
        Compra_Bici bici;
        Compra_Comp comp;
        Compra_Acc acc;
        for (int i = 0; i < this.modeloCarrito.getRowCount(); i++) {
            if (this.modeloCarrito.getValueAt(i, 0).equals(Ref) && this.modeloCarrito.getValueAt(i, 1).equals(item)) {

                this.modeloCarrito.setValueAt((Integer.parseInt(this.modeloCarrito.getValueAt(i, 4).toString()) + Cantidad), i, 4);
                float total = (Integer.parseInt(this.modeloCarrito.getValueAt(i, 4).toString()))
                        * (Float.parseFloat(this.modeloCarrito.getValueAt(i, 3).toString()));

                this.modeloCarrito.setValueAt(total, i, 5);

                if (item.equals("Bicicleta")) {
                    for (int j = 0; j < Bicicletas.size(); j++) {
                        bici = Bicicletas.get(j);
                        bici.setCantidad(Cantidad);
                        System.out.println("Bici setted");
                    }
                }
                if (item.equals("Componente")) {
                    for (int j = 0; j < Componentes.size(); j++) {
                        comp = Componentes.get(j);
                        comp.setCantidad(Cantidad);
                        System.out.println("Comp setted");
                    }
                }
                if (item.equals("Accesorio")) {
                    for (int j = 0; j < Accesorios.size(); j++) {
                        acc = Accesorios.get(j);
                        acc.setCantidad(Cantidad);
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

        int index = this.tbBici.getSelectedRow();

        int IdComBic;
        int IdCompra;
        int RefBicicleta;
        int Cantidad;

        String select = "Referencia, CONCAT(\"Bicicleta\") Item, CONCAT(T.Nombre,\" \", M.Nombre) Info, Precio";
        String condicion = "INNER JOIN Marca M ON Bicicleta.Marca = M.IdMarca "
                + "INNER JOIN TipoBicicleta T ON Bicicleta.Tipo = T.IdTipoBic "
                + "WHERE Referencia = ";

        if (index < 0) {
            JOptionPane.showMessageDialog(null, "Seleccione en la tabla la bicicleta que va a agregar", "Alerta", 0);
        } else {

            String id = tbBici.getValueAt(index, 0).toString();

            IdComBic = (this.pk_CompraBici + 1);
            this.pk_CompraBici = IdComBic;

            IdCompra = this.pk_compra;
            RefBicicleta = Integer.parseInt(id);
            Cantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese cantidad"));

            if (this.verificar(RefBicicleta, Cantidad, "Bicicleta")) {

                Compra_Bici bici = new Compra_Bici(IdComBic, IdCompra, RefBicicleta, Cantidad);
                System.out.println(bici.toString());
                Bicicletas.add(bici);
                llenarCarrito(Cantidad, this.tbBici, select, condicion, "Bicicleta");
                this.precioTotal();
            }

        }

    }

    private void addCompCarrito() {

        int index = this.tbComp.getSelectedRow();

        int IdComComp;
        int IdCompra;
        int RefComponente;
        int Cantidad;

        String select = "Referencia, CONCAT(\"Componente\") Item, CONCAT(T.Nombre,\" \", M.Nombre, \" \", Material) Info, Precio";
        String condicion = "INNER JOIN Marca M ON Componente.Marca = M.IdMarca "
                + "INNER JOIN TipoComponente T ON Componente.Tipo = T.IdTipoCom "
                + "WHERE Referencia = ";

        if (index < 0) {
            JOptionPane.showMessageDialog(null, "Seleccione en la tabla el componente que va a agregar", "Alerta", 0);
        } else {

            String id = tbComp.getValueAt(index, 0).toString();

            IdComComp = (this.pk_CompraComp + 1);
            this.pk_CompraComp = IdComComp;

            IdCompra = this.pk_compra;
            RefComponente = Integer.parseInt(id);
            Cantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese cantidad"));

            if (verificar(RefComponente, Cantidad, "Componente")) {

                Compra_Comp comp = new Compra_Comp(IdComComp, IdCompra, RefComponente, Cantidad);
                System.out.println(comp.toString());
                Componentes.add(comp);

                llenarCarrito(Cantidad, this.tbComp, select, condicion, "Componente");
                this.precioTotal();
            }
        }

    }

    private void addAccCarrito() {
        int index = this.tbAcc.getSelectedRow();

        int IdComAcc;
        int IdCompra;
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

            IdComAcc = (this.pk_CompraAcc + 1);
            this.pk_CompraAcc = IdComAcc;

            IdCompra = this.pk_compra;
            RefAccesorio = Integer.parseInt(id);
            Cantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese cantidad"));

            if (verificar(RefAccesorio, Cantidad, "Accesorio")) {
                Compra_Acc acc = new Compra_Acc(IdComAcc, IdCompra, RefAccesorio, Cantidad);
                System.out.println(acc.toString());
                Accesorios.add(acc);

                llenarCarrito(Cantidad, this.tbAcc, select, condicion, "Accesorio");

                this.precioTotal();
            }
        }
    }

    private void llenarCarrito(int cantidad, JTable tabla, String select, String condi, String nTabla) {

        TableColumn colum = this.tbCarrito.getColumnModel().getColumn(2);
        colum.setMinWidth(150);

        int index = tabla.getSelectedRow();

        String condicion = condi + tabla.getValueAt(index, 0);

        con.llenarTabla(select, nTabla, 4, modeloCarrito, condicion);

        float total = Float.parseFloat(this.modeloCarrito.getValueAt((this.modeloCarrito.getRowCount() - 1), 3).toString()) * cantidad;

        this.modeloCarrito.setValueAt(cantidad, (this.modeloCarrito.getRowCount() - 1), 4);
        this.modeloCarrito.setValueAt(total, (this.modeloCarrito.getRowCount() - 1), 5);

    }

    private void deleteFromCarrito() {
        int index = this.tbCarrito.getSelectedRow();

        System.out.println(index);
        if (index < 0) {
            JOptionPane.showMessageDialog(null, "Seleccione la compra a eliminar");
        } else {
            this.modeloCarrito.removeRow(index);
        }

        precioTotal();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == this.txtProv) {
            buscaProv();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (e.getSource() == this.buscaBici) {
            if (Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_ENTER) || Character.isLetter(c)) {
                llenarTablaBici(this.buscaBici.getText());
            }
        }
        if (e.getSource() == this.buscaComp) {

            if (Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_ENTER) || Character.isLetter(c)) {
                llenarTablaComp(this.buscaComp.getText());
            }

        }
        if (e.getSource() == this.buscaAcc) {
            if (Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_ENTER) || Character.isLetter(c)) {
                llenarTablaAcc(this.buscaAcc.getText());
            }
        }
    }

    private void ponerImagen() {

        Toolkit tk = Toolkit.getDefaultToolkit();
        String ruta = "./_data/Bicicol.png";
        Image imagen = tk.createImage(ruta);
        logo.setIcon(new ImageIcon(imagen.getScaledInstance(logo.getWidth(), logo.getHeight(), Image.SCALE_AREA_AVERAGING)));

    }

    private float precioTotal() {

        float valor = 0;

        for (int i = 0; i < this.modeloCarrito.getRowCount(); i++) {
            valor = valor
                    + (Float.parseFloat(this.modeloCarrito.getValueAt(i, 5).toString()));
        }
        this.txtPrecioTotal.setText("" + valor);
        return valor;
    }
    
    public void limpiar(){
        
        this.clearTable(tbCarrito, modeloCarrito);
        
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupCarrito = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem4 = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPopupMenu3 = new javax.swing.JPopupMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        txtProv = new javax.swing.JLabel();
        cbProv = new javax.swing.JComboBox<>();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        buscaBici = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbBici = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        buscaComp = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbComp = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        buscaAcc = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbAcc = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCarrito = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        logo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtPrecioTotal = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();

        jMenuItem1.setText("Eliminar");
        jMenuItem1.setActionCommand("");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        popupCarrito.add(jMenuItem1);
        popupCarrito.add(jSeparator1);

        jMenuItem4.setText("Cambiar Cantidad");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        popupCarrito.add(jMenuItem4);

        jMenuItem2.setText("Añadir al Carrito");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem2);

        jMenuItem3.setText("Añadir al Carrito");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu3.add(jMenuItem3);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtProv.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        txtProv.setText("Proveedor");

        cbProv.setModel(modeloProv);

        jLabel4.setText("Buscar");

        tbBici.setModel(modeloTBici);
        tbBici.setComponentPopupMenu(jPopupMenu2);
        jScrollPane2.setViewportView(tbBici);

        jButton1.setText("Nueva bicicleta");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Añadir al carrito -->");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buscaBici, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buscaBici, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Bicicleta", jPanel4);

        jLabel5.setText("Buscar");

        jButton5.setText("Nuevo Componente");
        jButton5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        tbComp.setModel(modeloTComp);
        tbComp.setComponentPopupMenu(jPopupMenu3);
        jScrollPane3.setViewportView(tbComp);

        jButton6.setText("Añadir al carrito -->");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jButton6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buscaComp, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buscaComp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Componente", jPanel1);

        jLabel6.setText("Buscar");

        jButton7.setText("Nuevo Accesorio");
        jButton7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        tbAcc.setModel(modeloTAcc);
        tbAcc.setComponentPopupMenu(jPopupMenu3);
        jScrollPane4.setViewportView(tbAcc);

        jButton8.setText("Añadir al carrito -->");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jButton8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buscaAcc, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buscaAcc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Accesorio", jPanel3);

        tbCarrito.setModel(modeloCarrito);
        tbCarrito.setComponentPopupMenu(popupCarrito);
        jScrollPane1.setViewportView(tbCarrito);

        jLabel2.setText("Compra:");

        jLabel3.setText("Carrito de compras:");

        jButton3.setText("Volver");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(153, 255, 153));
        jButton4.setText("Comprar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel1.setText("Precio total:");

        jLabel7.setText("$");

        txtPrecioTotal.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jButton9.setText("Limpiar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtProv)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbProv, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPrecioTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProv)
                    .addComponent(cbProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jLabel7)
                                .addComponent(txtPrecioTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton9)
                                .addComponent(jButton3)))))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        addBiciCarrito();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.setVisible(false);
        this.principal.setVisible(true);
       
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Agregar add = new Agregar(principal, 2);
        add.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        deleteFromCarrito();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        addBiciCarrito();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Comprar();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Agregar add = new Agregar(principal, 3);
        add.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        addCompCarrito();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        this.addCompCarrito();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        Agregar add = new Agregar(principal, 4);
        add.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        this.addAccCarrito();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        int index = this.tbCarrito.getSelectedRow();
        Compra_Bici bici;
        Compra_Comp comp;
        Compra_Acc acc;
        String item = this.modeloCarrito.getValueAt(index, 1).toString();
        try {
            if (index < 0) {
                JOptionPane.showMessageDialog(null, "Seleccione el registro a modificar");
            } else {
                int cant = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad"));
                this.modeloCarrito.setValueAt(cant, index, 4);
                float total = (Integer.parseInt(this.modeloCarrito.getValueAt(index, 4).toString()))
                        * (Float.parseFloat(this.modeloCarrito.getValueAt(index, 3).toString()));

                this.modeloCarrito.setValueAt(total, index, 5);
                if (item.equals("Bicicleta")) {
                    for (int j = 0; j < Bicicletas.size(); j++) {
                        bici = Bicicletas.get(j);
                        bici.setCantidad(cant);
                        System.out.println("Bici setted");
                    }
                }
                if (item.equals("Componente")) {
                    for (int j = 0; j < Componentes.size(); j++) {
                        comp = Componentes.get(j);
                        comp.setCantidad(cant);
                        System.out.println("Comp setted");
                    }
                }
                if (item.equals("Accesorio")) {
                    for (int j = 0; j < Accesorios.size(); j++) {
                        acc = Accesorios.get(j);
                        acc.setCantidad(cant);
                        System.out.println("Acc setted");
                    }
                    
                }
                this.precioTotal();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ingreso inválido");
        }

    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
limpiar();
    }//GEN-LAST:event_jButton9ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField buscaAcc;
    private javax.swing.JTextField buscaBici;
    private javax.swing.JTextField buscaComp;
    private javax.swing.JComboBox<String> cbProv;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel logo;
    private javax.swing.JPopupMenu popupCarrito;
    private javax.swing.JTable tbAcc;
    private javax.swing.JTable tbBici;
    private javax.swing.JTable tbCarrito;
    private javax.swing.JTable tbComp;
    private javax.swing.JLabel txtPrecioTotal;
    private javax.swing.JLabel txtProv;
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
