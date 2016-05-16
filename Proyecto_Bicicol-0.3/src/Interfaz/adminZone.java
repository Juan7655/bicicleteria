/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import java.awt.Image;
import java.awt.Toolkit;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.*;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.axis.*;
import org.jfree.data.xy.*;



/**
 *
 * @author OscarLopez
 */
public class adminZone extends javax.swing.JFrame {

    private Bicicol.DataBase con = new Bicicol.DataBase();
    private DefaultTableModel modeloCliDes;
    private DefaultTableModel modeloVentasMes;

    public adminZone() {
        String[] columna = new String[]{"Nombre", "Dinero Gastado", "Tipo de Cliente"};
        String[] ventasM = new String[]{"Mes", "Venta Total"};
        this.modeloCliDes = new DefaultTableModel(null, columna);
        this.modeloVentasMes = new DefaultTableModel(null, ventasM);

        initComponents();
        this.setLocationRelativeTo(null);
        ponerImagen();

        llenar();
        this.graficar();
    }

    private void llenar() {

        NumberFormat formatter = new DecimalFormat("###.###");

        String select = " (Cl.nombre)Nombre,(SUM(Va.Cantidad*Acc.precio)+SUM(Vc.Cantidad*Com.precio)+ "
                + " SUM(Vb.Cantidad*Bi.precio))'Dinero Gastado', "
                + " CASE WHEN (SUM(Va.Cantidad*Acc.precio)+SUM(Vc.Cantidad*Com.precio)+ "
                + " SUM(Vb.Cantidad*Bi.precio))>(SELECT AVG(VV)  "
                + " FROM (SELECT (SUM(Vaa.Cantidad*Ac.precio)+SUM(Vcc.Cantidad*Comm.precio)+ "
                + "SUM(Vbb.Cantidad*Bii.precio)) AS VV "
                + " " + " FROM Venta_Accesorio Vaa "
                + "INNER JOIN Accesorio Ac "
                + "INNER JOIN Venta Ve "
                + "INNER JOIN Cliente Cll "
                + "INNER JOIN Venta_Componente Vcc "
                + "INNER JOIN Componente Comm "
                + "INNER JOIN Venta_Bicleta Vbb "
                + "INNER JOIN Bicicleta Bii "
                + "ON Vaa.refaccesorio=Ac.referencia AND Vcc.refcomponente=Comm.referencia AND Vbb.refbicicleta=Bii.referencia "
                + "AND Ve.idventa=Vaa.idventa AND Ve.idventa=Vcc.idventa "
                + "AND Ve.idventa=Vbb.idventa AND Ve.Cliente=Cll.Documento "
                + "GROUP BY Cll.Documento)TAB)THEN 'Cliente Premium' ELSE 'Cliente Normal' END TipoCliente";
        String tabla = " Venta_Accesorio Va ";
        String condicion = "INNER JOIN Accesorio Acc "
                + "INNER JOIN Venta V "
                + "INNER JOIN Cliente Cl "
                + "INNER JOIN Venta_Componente Vc "
                + "INNER JOIN Componente Com "
                + "INNER JOIN Venta_Bicleta Vb "
                + "INNER JOIN Bicicleta Bi "
                + "ON Va.refaccesorio=Acc.referencia "
                + "AND Vc.refcomponente=Com.referencia "
                + "AND Vb.refbicicleta=Bi.referencia "
                + "AND V.idventa=Va.idventa "
                + "AND V.idventa=Vc.idventa "
                + "AND V.idventa=Vb.idventa "
                + "AND V.Cliente=Cl.documento "
                + "GROUP BY Cl.Documento";
        this.con.llenarTabla(select, tabla, 3, modeloCliDes, condicion);
        for (int i = 0; i < this.modeloCliDes.getRowCount(); i++) {
            ;
            this.modeloCliDes.setValueAt(formatter.format(this.modeloCliDes.getValueAt(i, 1)), i, 1);
        }
        select = "MONTH(Fecha) Mes, SUM(ValorTotal)";
        condicion = "GROUP BY Mes";
        con.llenarTabla(select, "Venta", 2, modeloVentasMes, condicion);

    }
    
    private void graficar(){
        ChartPanel panel;
        JFreeChart chart = null;
        int validar = 1;
        XYSplineRenderer renderer = new XYSplineRenderer();
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        ValueAxis x = new NumberAxis();
        ValueAxis y = new NumberAxis();
        
        XYSeries serie = new XYSeries("Ventas");
        
        XYPlot plot;
        
        lineas.removeAll();
        
        try{
            for (int fila = 0; fila < this.modeloVentasMes.getRowCount(); fila++) {
            serie.add(Float.parseFloat(String.valueOf(this.modeloVentasMes.getValueAt(fila, 0))),
                    Float.parseFloat(String.valueOf(modeloVentasMes.getValueAt(fila, 1))));
            
            }
        }catch (Exception es){
            validar=0; 
        }
        if(validar == 1){
            dataset.addSeries(serie);
            
            x.setLabel("Mes");
            y.setLabel("Ventas");
            plot = new XYPlot(dataset, x, y, renderer);
            chart = new JFreeChart(plot);
            chart.setTitle("Cambio en las Ventas 2016");
        }
        panel = new ChartPanel(chart);
        panel.setBounds(5, 10, 229, 219);
        
        lineas.add(panel);
        lineas.repaint();
        
    }

    private void ponerImagen() {

        Toolkit tk = Toolkit.getDefaultToolkit();
        String ruta = "./_data/Bicicol.png";
        Image imagen = tk.createImage(ruta);
        logo.setIcon(new ImageIcon(imagen.getScaledInstance(logo.getWidth(), logo.getHeight(), Image.SCALE_AREA_AVERAGING)));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCliDes = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        datos = new javax.swing.JTable();
        lineas = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jButton5.setText("Salir");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        tablaCliDes.setModel(modeloCliDes);
        jScrollPane1.setViewportView(tablaCliDes);

        jLabel1.setText("Clientes destacados:");

        jButton1.setText("Abrir Skype");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 42, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(23, 23, 23))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(23, 23, 23))
        );

        jTabbedPane1.addTab("Clientes", jPanel1);

        datos.setModel(modeloVentasMes);
        jScrollPane2.setViewportView(datos);

        javax.swing.GroupLayout lineasLayout = new javax.swing.GroupLayout(lineas);
        lineas.setLayout(lineasLayout);
        lineasLayout.setHorizontalGroup(
            lineasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 229, Short.MAX_VALUE)
        );
        lineasLayout.setVerticalGroup(
            lineasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel2.setText("Ventas por mes");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lineas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
            .addComponent(lineas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Ventas", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Principal prin = new Principal();
        this.setVisible(false);
        prin.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Runtime aplicacion = Runtime.getRuntime();
        try {
            aplicacion.exec("C:/Program Files (x86)/Skype/Phone/Skype.exe");
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable datos;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel lineas;
    private javax.swing.JLabel logo;
    private javax.swing.JTable tablaCliDes;
    // End of variables declaration//GEN-END:variables
}
