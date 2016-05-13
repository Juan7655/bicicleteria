/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Bicicol.Compra_Bici;
import java.awt.Image;
import java.awt.Toolkit;
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
    private int pk_compra;
    private int pk_CompraBici;
    private boolean ok = false;

    private ArrayList<Compra_Bici> Bicicletas = new ArrayList<Compra_Bici>();

    private final Bicicol.DataBase con = new Bicicol.DataBase();

    public Compras(Principal principal) {

        String[] columnasBici = new String[]{"Ref", "Marca", "Tipo", "Talla", "Precio"};
        String[] columnasCarrito = new String[]{"Ref", "Item", "Info", "Precio", "Cantidad"};

        this.pk_compra = con.getPrimarykeyDisp("Compra");
        this.pk_CompraBici = (con.getPrimarykeyDisp("Compra_Bicicleta") - 1);
        this.principal = principal;
        //Combobox
        this.modeloProv = new DefaultComboBoxModel(new String[]{});
        this.modelItem = new DefaultComboBoxModel(new String[]{});
        //Tablas
        this.modeloTBici = new DefaultTableModel(null, columnasBici);
        this.modeloCarrito = new DefaultTableModel(null, columnasCarrito);

        initComponents();
        this.setLocationRelativeTo(null);
        generarCompraInit();
        ponerEscuchar();
        llenar();

    }

    private void generarCompraInit() {

        int idCompra = this.pk_compra;
        float valor = 0;
        Date dt = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        String fechaAct = formato.format(dt);

        String datos = idCompra + "," + valor + ",'" + fechaAct + "'";
        System.out.println(datos);

        con.postInto("Compra", datos);

    }

    private void ponerEscuchar() {
        //Mouse
        this.txtProv.addMouseListener(this);
        //Key
        this.buscaBici.addKeyListener(this);
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

    private void clearTable(JTable tabla, DefaultTableModel modelo) {
        for (int i = 0; i < tabla.getRowCount(); i++) {
            modelo.removeRow(i);
            i -= 1;
        }
    }

    private void addBiciCarrito() {

        int index = this.tbBici.getSelectedRow();

        int IdComBic;
        int IdCompra;
        int RefBicicleta;
        int Cantidad;

        if (index < 0) {
            JOptionPane.showMessageDialog(null, "Seleccione en la tabla la bicicleta que va a agregar", "Alerta", 0);
        } else {

            String id = tbBici.getValueAt(index, 0).toString();

            IdComBic = (this.pk_CompraBici + 1);
            this.pk_CompraBici = IdComBic;

            IdCompra = this.pk_compra;
            RefBicicleta = Integer.parseInt(id);
            Cantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese cantidad"));

            Compra_Bici bici = new Compra_Bici(IdComBic, IdCompra, RefBicicleta, Cantidad);
            System.out.println(bici.toString());
            Bicicletas.add(bici);

            llenarCarrito(Cantidad);

            //Usar el toString
        }

    }

    private void llenarCarrito(int cantidad) {

        TableColumn colum = this.tbCarrito.getColumnModel().getColumn(2);
        colum.setMinWidth(150);

        int index = this.tbBici.getSelectedRow();

        String select = "Referencia, CONCAT(\"Bicicleta\") Item, CONCAT(T.Nombre,\" \", M.Nombre) Info, Precio";
        String condicion = "INNER JOIN Marca M ON Bicicleta.Marca = M.IdMarca "
                + "INNER JOIN TipoBicicleta T ON Bicicleta.Tipo = T.IdTipoBic "
                + "WHERE Referencia = " + this.tbBici.getValueAt(index, 0);

        con.llenarTabla(select, "Bicicleta", 4, modeloCarrito, condicion);
        this.modeloCarrito.setValueAt(cantidad, (this.modeloCarrito.getRowCount() - 1), 4);

    }

    private void deleteFromCarrito() {
        int index = this.tbCarrito.getSelectedRow();
        System.out.println(index);
        if (index < 0) {
            JOptionPane.showMessageDialog(null, "Seleccione la compra a eliminar");
        } else {
            this.modeloCarrito.removeRow(index);
        }
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
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
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
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCarrito = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        jMenuItem1.setText("Eliminar");
        jMenuItem1.setActionCommand("");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Añadir al Carrito");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtProv.setFont(new java.awt.Font("Franklin Gothic Book", 1, 18)); // NOI18N
        txtProv.setText("Proveedor");

        cbProv.setModel(modeloProv);

        jLabel4.setText("Buscar");

        tbBici.setModel(modeloTBici);
        jScrollPane2.setViewportView(tbBici);

        jButton1.setText("Agregar bicicleta");
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 210, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Componente", jPanel1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 210, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Accesorio", jPanel3);

        tbCarrito.setModel(modeloCarrito);
        tbCarrito.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(tbCarrito);

        jLabel2.setText("Compra:");

        jLabel3.setText("Carrito de compras:");

        jButton3.setText("Volver");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
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
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 351, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addGap(51, 51, 51))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProv)
                    .addComponent(cbProv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jButton3)
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
        if (this.ok == false) {
            con.deleteFrom("Compra", "" + this.pk_compra, "IdCompra = " + this.pk_compra);
        }
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField buscaBici;
    private javax.swing.JComboBox<String> cbProv;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tbBici;
    private javax.swing.JTable tbCarrito;
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
