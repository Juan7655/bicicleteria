/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Bicicol.DataBase;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Icon;
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
public class BiciComp extends javax.swing.JFrame implements MouseListener {

    private final Bicicol.DataBase con = new Bicicol.DataBase();
    private final Agregar agregar;

    private DefaultTableModel modeloTabla1;
    private DefaultTableModel modeloTabla2;
    private DefaultTableModel modeloTabla1b;
    private DefaultTableModel modeloTabla2b;
    private DefaultTableModel modeloTabla3b;

    private boolean dist;

    public BiciComp(Agregar agregar, boolean dist) {

        this.agregar = agregar;
        this.dist = dist;

        String[] columnas1 = new String[]{"Referencia", "Tipo", "Marca", "Material", "Precio"};
        this.modeloTabla1 = new DefaultTableModel(null, columnas1);
        String[] columnas2 = new String[]{"Referencia", "Tipo", "Marca", "Material", "Precio", "Cantidad"};
        this.modeloTabla2 = new DefaultTableModel(null, columnas2);
        String[] columnas1b = new String[]{"Referencia", "Marca", "Tipo", "Talla", "Precio"};
        this.modeloTabla1b = new DefaultTableModel(null, columnas1b);
        String[] columnas3 = new String[]{"Referencia", "Marca", "Tipo", "Talla", "Precio", "Cantidad"};
        this.modeloTabla2b = new DefaultTableModel(null, columnas1);
        this.modeloTabla3b = new DefaultTableModel(null, columnas3);

        initComponents();
        ponerEscuchar();
        this.setLocationRelativeTo(null);
        ponerImagen();
        llenarTabla1(columnas1);//Componentes
        llenarTabla2(); //Componentes en Bicicleta 
        llenarTabla1b(); //Bicicletas
        llenarTabla2b();

        if (this.dist == false) {
            this.tabbed.setSelectedIndex(1);
            this.tabbed.setEnabledAt(0, false);
        }

    }

    private void llenarTabla1(String[] colum) {

        TableRowSorter<TableModel> ordena = new TableRowSorter<TableModel>(tabla1.getModel());
        tabla1.setRowSorter(ordena);

        String select = "Referencia, T.Nombre, M.Nombre, Material, Precio";
        String condicion = "INNER JOIN Marca M ON Componente.Marca = M.IdMarca "
                + "INNER JOIN TipoComponente T ON Componente.Tipo = T.IdTipoCom";

        con.llenarTabla(select, "Componente", colum.length, modeloTabla1, condicion);

    }

    private void llenarTabla2() {

        TableRowSorter<TableModel> ordena = new TableRowSorter<TableModel>(tabla2.getModel());
        tabla2.setRowSorter(ordena);

        String select = "Referencia, T.Nombre, M.Nombre, Material, Precio, BC.Cantidad";
        String condicion = "INNER JOIN Marca M ON Componente.Marca = M.IdMarca "
                + "INNER JOIN TipoComponente T ON Componente.Tipo = T.IdTipoCom "
                + "INNER JOIN Bicicleta_Componente  BC ON Componente.Referencia = BC.RefComponente "
                + "WHERE BC.RefBicicleta = " + con.getLastPk("Bicicleta");
        con.llenarTabla(select, "Componente", 6, modeloTabla2, condicion);

    }

    private void llenarTabla1b() {

        TableRowSorter<TableModel> ordena = new TableRowSorter<TableModel>(tabla1b.getModel());
        tabla1b.setRowSorter(ordena);

        String select = "Referencia, M.Nombre, T.Nombre, Talla, Precio";
        String condicion = "INNER JOIN Marca M ON Bicicleta.Marca = M.IdMarca "
                + "INNER JOIN TipoBicicleta T ON Bicicleta.Tipo = T.IdTipoBic "
                + "ORDER BY Referencia ASC";

        con.llenarTabla(select, "Bicicleta", 5, modeloTabla1b, condicion);
    }

    private void llenarTabla2b() {

        TableRowSorter<TableModel> ordena = new TableRowSorter<TableModel>(tabla2b.getModel());
        tabla2b.setRowSorter(ordena);

        String select = "Referencia, T.Nombre, M.Nombre, Material, Precio";
        String condicion = "INNER JOIN Marca M ON Componente.Marca = M.IdMarca "
                + "INNER JOIN TipoComponente T ON Componente.Tipo = T.IdTipoCom ";

        con.llenarTabla(select, "Componente", 5, modeloTabla2b, condicion);
    }

    private void llenarTabla3b(String idBic) {

        TableRowSorter<TableModel> ordena = new TableRowSorter<TableModel>(tabla3b.getModel());
        tabla3b.setRowSorter(ordena);

        String select = "Referencia, M.Nombre, T.Nombre, Material, Precio, BC.Cantidad";
        String condicion = "INNER JOIN Marca M ON Componente.Marca = M.IdMarca "
                + "INNER JOIN TipoComponente T ON Componente.Tipo = T.IdTipoCom "
                + "INNER JOIN Bicicleta_Componente  BC ON Componente.Referencia = BC.RefComponente "
                + "WHERE BC.RefBicicleta = " + idBic + " "
                + "ORDER By Referencia ASC";
        con.llenarTabla(select, "Componente", 6, modeloTabla3b, condicion);

    }

    private void clearTable(JTable tabla, DefaultTableModel modelo) {
        for (int i = 0; i < tabla.getRowCount(); i++) {
            modelo.removeRow(i);
            i -= 1;
        }
    }

    private void agregar() {
        int index = this.tabla1.getSelectedRow();

        if (index < 0) {
            JOptionPane.showMessageDialog(null, "Seleccione el componente que va a agregar");
        } else {

            int idComp = (int) tabla1.getValueAt(index, 0);
            int idBici = con.getLastPk("Bicicleta");
            try {
                Integer cant = Integer.parseInt(JOptionPane.showInputDialog("Ingrese cantidad:"));

                int pk = con.getPrimarykeyDisp("Bicicleta_Componente");
                String datos = pk + "," + idComp + "," + idBici + "," + cant;

                if (this.verificar(modeloTabla2, idComp, idBici, "Bicicleta_componente", cant) == false) {
                    con.post("Bicicleta_Componente", datos);
                }

                this.clearTable(tabla2, modeloTabla2);
                this.llenarTabla2();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida");
            }

        }
    }

    private void agregar2() {

        int index1 = this.tabla1b.getSelectedRow();
        int index2 = this.tabla2b.getSelectedRow();

        if (index1 < 0 || index2 < 0) {
            JOptionPane.showMessageDialog(null, "Seleccione una bicicleta y el componente que va a agregar");
        } else {

            int idComp = (int) tabla2b.getValueAt(index2, 0);
            int idBici = (int) tabla1b.getValueAt(index1, 0);

            try {

                Integer cant = Integer.parseInt(JOptionPane.showInputDialog("Ingrese cantidad:"));
                if (cant.toString().equals("")) {
                    cant = 1;
                }
                int pk = con.getPrimarykeyDisp("Bicicleta_Componente");
                String datos = pk + "," + idComp + "," + idBici + "," + cant;
                if(this.verificar(modeloTabla3b, idComp, idBici, "Bicicleta_Componente", cant)==false){
                con.post("Bicicleta_Componente", datos);
                }
                this.clearTable(tabla3b, modeloTabla3b);

                this.llenarTabla3b("" + idBici);
                //this.tabla1b.changeSelection(this.tabla1b.getSelectedRow(), this.tabla1b.getSelectedColumn(), true, false);
                this.tabla2b.changeSelection(this.tabla2b.getSelectedRow(), this.tabla2b.getSelectedColumn(), true, false);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Ingrese una cantidad válida");
            }

        }

    }

    private void eliminar() {
        int index = this.tabla2.getSelectedRow();

        if (index < 0) {
            JOptionPane.showMessageDialog(null, "Seleccione el componente que va a eliminar");
        } else {

            String cid = tabla2.getValueAt(index, 0).toString();
            int bid = con.getLastPk("Bicicleta");
            String cond = "RefComponente = " + cid + " AND RefBicicleta = " + bid;
            con.delete("Bicicleta_Componente", cond);

            this.clearTable(tabla2, modeloTabla2);
            this.llenarTabla2();
            this.tabla2.changeSelection(-1, -1, true, false);

        }
    }

    private void eliminar2() {

        int index = this.tabla3b.getSelectedRow();
        int index2 = this.tabla1b.getSelectedRow();
        if (index < 0) {
            JOptionPane.showMessageDialog(null, "Seleccione el componente que va a eliminar");
        } else {

            String cid = tabla3b.getValueAt(index, 0).toString();
            String bid = tabla1b.getValueAt(index2, 0).toString();

            String cond = "RefComponente = " + cid + " AND RefBicicleta = " + bid;
            con.delete("Bicicleta_Componente", cond);

            this.clearTable(tabla3b, modeloTabla3b);
            this.llenarTabla3b(bid);
            this.tabla3b.changeSelection(this.tabla3b.getSelectedRow(), this.tabla3b.getSelectedColumn(), true, false);

        }
    }

    private boolean verificar(DefaultTableModel modelo, int idComp, int idBici, String tabla, int cantidad) {
        boolean ok = false;
        if (modelo.getRowCount() > 0) {
            for (int i = 0; i < modelo.getRowCount(); i++) {
                if (modelo.getValueAt(i, 0).equals(idComp)) {
                    ok = true;
                    con.update(tabla, "Cantidad = Cantidad +" + cantidad, "RefComponente = " + modelo.getValueAt(i, 0) + " AND RefBicicleta = " + idBici);
                }
            }
        }
        return ok;

    }

    private void ponerEscuchar() {

        this.tabla1.addMouseListener(this);
        this.aiudaa.addMouseListener(this);
        this.aiudaa2.addMouseListener(this);
        this.tabla1b.addMouseListener(this);
        this.tabla2b.addMouseListener(this);
        this.tabla3b.addMouseListener(this);
    }

    private void ponerImagen() {

        Toolkit tk = Toolkit.getDefaultToolkit();
        String ruta = "./_data/Bicicol.png";
        Image imagen = tk.createImage(ruta);
        logo.setIcon(new ImageIcon(imagen.getScaledInstance(logo.getWidth(), logo.getHeight(), Image.SCALE_AREA_AVERAGING)));

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        int index = this.tabla1b.getSelectedRow();
        if (e.getSource() == this.aiudaa) {
            JOptionPane.showMessageDialog(null, "Seleccione de la tabla de arriba el componente que desea agregar,\n"
                    + "Posteriormente pulse el botón 'Agregar componente', el componente \n"
                    + "se añadirá a la última bicicleta agregada");
        }
        if (e.getSource() == this.aiudaa2) {
            JOptionPane.showMessageDialog(null, "Seleccione de la tabla de la izquierda la bicicleta que usará,\n"
                    + "a continuación selecione el componente a agregar y posteriormente presione 'agregar componente' \n"
                    + "en la tabla de abajo se mostrará el componente agregado, más los que ya tenía la bicicleta.\n"
                    + "\n Para eliminar un componente de una bicicleta, seleccione la bicicleta de la tabla de la izquierda\n "
                    + "y el componente de la tabla de abajo, final mente presione 'eliminar componente'");
        }
        if (e.getSource() == this.tabla1b) {
            this.clearTable(tabla3b, modeloTabla3b);
            String id = tabla1b.getValueAt(index, 0).toString();
            llenarTabla3b(id);

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel3 = new javax.swing.JPanel();
        tabbed = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla1 = new javax.swing.JTable();
        boton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla2 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtBiciAct = new javax.swing.JLabel();
        aiudaa = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabla1b = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabla2b = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabla3b = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        aiudaa2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        logo = new javax.swing.JLabel();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tabla1.setModel(modeloTabla1);
        jScrollPane1.setViewportView(tabla1);

        boton.setText("Agregar Componente");
        boton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonActionPerformed(evt);
            }
        });

        tabla2.setModel(modeloTabla2);
        jScrollPane2.setViewportView(tabla2);

        jLabel2.setText("Componentes");

        jLabel3.setText("Componentes en bicicleta");

        jLabel4.setText("Bicicleta actual:");

        txtBiciAct.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N

        aiudaa.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        aiudaa.setText("(?)");

        jButton2.setText("Eliminar componente");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(174, 174, 174)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(boton)))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(aiudaa, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(jButton2))
                            .addComponent(jLabel4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBiciAct, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boton)
                    .addComponent(aiudaa)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtBiciAct, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                        .addGap(108, 108, 108))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        tabbed.addTab("", jPanel1);

        tabla1b.setModel(modeloTabla1b);
        jScrollPane3.setViewportView(tabla1b);

        tabla2b.setModel(modeloTabla2b
        );
        jScrollPane4.setViewportView(tabla2b);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel5.setText("+");

        tabla3b.setModel(modeloTabla3b);
        jScrollPane5.setViewportView(tabla3b);

        jButton3.setText("Agregar Componente");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Eliminar Componente");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel6.setText("Bicicleta");

        jLabel7.setText("Componente");

        jLabel8.setText("Componente en bicicleta");

        aiudaa2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        aiudaa2.setText("(?)");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(196, 196, 196)
                        .addComponent(jButton3)
                        .addGap(43, 43, 43)
                        .addComponent(aiudaa2)
                        .addGap(37, 37, 37)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)
                        .addGap(361, 361, 361)
                        .addComponent(jLabel7)))
                .addContainerGap(169, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(132, 132, 132))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(jLabel5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(aiudaa2))
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        tabbed.addTab("", jPanel2);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel1.setText("Bicicletas y Componentes");

        jButton1.setText("Volver");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tabbed, javax.swing.GroupLayout.PREFERRED_SIZE, 769, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addGap(378, 378, 378)
                    .addComponent(jButton1)
                    .addGap(348, 348, 348))
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(108, 108, 108)
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(12, 12, 12)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(71, Short.MAX_VALUE)
                .addComponent(tabbed, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(391, 391, 391))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(414, 414, 414)))
                    .addComponent(jButton1)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonActionPerformed
        this.agregar();
    }//GEN-LAST:event_botonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setVisible(false);
        this.agregar.setVisible(true);
        this.agregar.limpiar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.eliminar();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.agregar2();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.eliminar2();
    }//GEN-LAST:event_jButton4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aiudaa;
    private javax.swing.JLabel aiudaa2;
    private javax.swing.JButton boton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel logo;
    private javax.swing.JTabbedPane tabbed;
    private javax.swing.JTable tabla1;
    private javax.swing.JTable tabla1b;
    private javax.swing.JTable tabla2;
    private javax.swing.JTable tabla2b;
    private javax.swing.JTable tabla3b;
    private javax.swing.JLabel txtBiciAct;
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
}
