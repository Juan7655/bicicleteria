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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
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
    
    private final Principal principal;
    private DefaultTableModel modeloBici;
    private DefaultTableModel modeloComp;
    private DefaultTableModel modeloAcc;
    
    public Mostrar(Principal principal) {
        
        String[] columnasBici = new String[]{"Ref", "Marca", "Tipo", "Talla","Stock", "Precio"};
        String[] columnasComp = new String[]{"Ref", "Marca", "Tipo", "Material","Stock", "Precio"};
        String[] columnasAcc = new String[]{"Ref", "Marca", "Tipo", "Material","Stock", "Precio"};
        
        this.modeloComp = new DefaultTableModel(null, columnasComp);
        this.modeloBici = new DefaultTableModel(null, columnasBici);
        this.modeloAcc = new DefaultTableModel(null, columnasAcc);
        
        this.principal = principal;
        
        initComponents();
        this.setLocationRelativeTo(null);
        this.ponerImagen();
        this.ponerEschuchar();
        
    }
    
    private void ponerImagen() {
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        String ruta = "./_data/bicicoogle.png";
        Image imagen = tk.createImage(ruta);
        fondo.setIcon(new ImageIcon(imagen.getScaledInstance(fondo.getWidth(), fondo.getHeight(), Image.SCALE_AREA_AVERAGING)));
//        String ruta2 = "./_data/Bicicol.png";
//        Image imagen2 = tk.createImage(ruta2);
//        logo.setIcon(new ImageIcon(imagen2.getScaledInstance(logo.getWidth(), logo.getHeight(), Image.SCALE_AREA_AVERAGING)));

    }
    
    private void ponerEschuchar() {
        
        this.busca.addKeyListener(this);
        
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSalir)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtRes)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(fondo, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(busca)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(rbBici)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbComp)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbAcc)))
                        .addGap(39, 39, 39))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(24, 24, 24))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(129, 129, 129)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbBici)
                            .addComponent(rbComp)
                            .addComponent(rbAcc)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fondo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(21, 21, 21)
                .addComponent(txtRes)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(btnSalir)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSalir;
    private javax.swing.JTextField busca;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel fondo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rbAcc;
    private javax.swing.JRadioButton rbBici;
    private javax.swing.JRadioButton rbComp;
    private javax.swing.JTable tabla;
    private javax.swing.JLabel txtRes;
    // End of variables declaration//GEN-END:variables

}
