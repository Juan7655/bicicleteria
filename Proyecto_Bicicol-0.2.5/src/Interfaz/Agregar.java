/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Agregar extends javax.swing.JFrame implements KeyListener {

    private final Principal principal;
    private int indi;

    private DefaultComboBoxModel modelCTipoB;
    private DefaultComboBoxModel modelCTipoC;
    private DefaultComboBoxModel modelCMarca;
    private DefaultComboBoxModel modelCMarcaC;
    private DefaultComboBoxModel modelCTipoA;
    private DefaultComboBoxModel modelCMarcaA;
    private final Bicicol.DataBase con = new Bicicol.DataBase();

    public Agregar(Principal principal, int indi) {

        //Inicialización de variables
        this.principal = principal;

        this.indi = indi;
        this.modelCTipoB = new DefaultComboBoxModel(new String[]{});
        this.modelCMarca = new DefaultComboBoxModel(new String[]{});
        this.modelCMarcaC = new DefaultComboBoxModel(new String[]{});
        this.modelCTipoC = new DefaultComboBoxModel(new String[]{});
        this.modelCTipoA = new DefaultComboBoxModel(new String[]{});
        this.modelCMarcaA = new DefaultComboBoxModel(new String[]{});

        //Iniciar interfaz
        initComponents();
        ponerEscuchar();
        this.setLocationRelativeTo(null);
        ponerImagen();
        //Llenar Comboboxes
        this.llenar();

        if (indi == 2) {
            this.jTabbedPane1.setSelectedIndex(0);
            this.jTabbedPane1.setEnabledAt(1, false);
            this.jTabbedPane1.setEnabledAt(2, false);
        }
        if (indi == 3) {
            this.jTabbedPane1.setSelectedIndex(1);
            this.jTabbedPane1.setEnabledAt(0, false);
            this.jTabbedPane1.setEnabledAt(2, false);
        }

        if (indi == 4) {
            this.jTabbedPane1.setSelectedIndex(2);
            this.jTabbedPane1.setEnabledAt(0, false);
            this.jTabbedPane1.setEnabledAt(1, false);
        }

    }

    

    private void llenar() {

        //Limpiar Combobox
        this.cbMarca.removeAllItems();
        this.cbMarcaA.removeAllItems();
        this.cbMarcaC.removeAllItems();
        this.cbTipo.removeAllItems();
        this.cbTipoA.removeAllItems();
        this.cbTipoC.removeAllItems();
//
//        con.llenarcbTipoB(modelCTipoB);
//        con.llenarcbMarca(modelCMarca);
//        con.llenarcbMarcaC(modelCMarcaC);
//        con.llenarcbTipoC(modelCTipoC);
//        con.llenarcbTipoA(modelCTipoA);
//        con.llenarcbMarcaA(modelCMarcaA);

        String select = "Nombre";
        String condicion = "";

        con.llenarCB("Nombre", "TipoBicicleta", "", modelCTipoB);
        con.llenarCB(select, "Marca", condicion, modelCMarca);
        con.llenarCB(select, "Marca", condicion, modelCMarcaC);
        con.llenarCB(select, "TipoComponente", condicion, modelCTipoC);
        con.llenarCB(select, "TipoAccesorio", condicion, modelCTipoA);
        con.llenarCB(select, "Marca", condicion, modelCMarcaA);
    }

    private void ponerEscuchar() {

        this.txtCarC.addKeyListener(this);
        this.txtCaraA.addKeyListener(this);
        this.txtPrecio.addKeyListener(this);
        this.txtPrecioA.addKeyListener(this);
        this.txtPrecioC.addKeyListener(this);
        this.txtVendidas.addKeyListener(this);
        this.txtVendidasC.addKeyListener(this);
        this.txtVendidasA.addKeyListener(this);
        this.txtStock.addKeyListener(this);
        this.txtStockC.addKeyListener(this);
        this.txtStockA.addKeyListener(this);
        this.txtGarantia.addKeyListener(this);
        this.txtGarantiaC.addKeyListener(this);
        this.txtGarantiaA.addKeyListener(this);

    }

    private void btnVolver() {
        if (indi >= 2) {
            this.setVisible(false);
        } else {
            this.setVisible(false);
            this.principal.setVisible(true);
        }
    }

    public void limpiar() {

        //Text field
        this.txtCarC.setText("");
        this.txtCaraA.setText("");
        this.txtPrecio.setText("");
        this.txtPrecioA.setText("");
        this.txtPrecioC.setText("");
        this.txtVendidas.setText("");
        this.txtVendidasC.setText("");
        this.txtVendidasA.setText("");
        this.txtStock.setText("");
        this.txtStockC.setText("");
        this.txtStockA.setText("");
        this.txtGarantia.setText("");
        this.txtGarantiaC.setText("");
        this.txtGarantiaA.setText("");
        //ComboBox
        this.cbMarca.setSelectedIndex(0);
        this.cbTipo.setSelectedIndex(0);
        this.cbColorA.setSelectedIndex(0);
        this.cbMarcaA.setSelectedIndex(0);
        this.cbMaterialA.setSelectedIndex(0);
        this.cbTipoA.setSelectedIndex(0);
        this.cbColorC.setSelectedIndex(0);
        this.cbMarcaC.setSelectedIndex(0);
        this.cbMaterialC.setSelectedIndex(0);
        this.cbTipoC.setSelectedIndex(0);
        this.cbTallaB.setSelectedIndex(0);
    }

    private void btnGuardarBici() {

        if (this.txtPrecio.getText().trim().equals("")
                || this.txtVendidas.getText().trim().equals("") || this.txtStock.getText().trim().equals("")
                || this.txtGarantia.getText().trim().equals("")) {

            JOptionPane.showMessageDialog(null, "Llene todos los espacios");

        } else if (Integer.parseInt(this.txtPrecio.getText()) < 240000) {
            JOptionPane.showMessageDialog(null, "El valor de una bicicleta debe ser mayor que $240.000");
        } else {

            String datos;
            String cond = "WHERE Nombre = ";
            int pk = con.getPrimarykeyDisp("Bicicleta");
            int tipo = con.getPrimaryKeyCB("TipoBicicleta", cond + "'" + this.cbTipo.getSelectedItem().toString() + "'");
            int marca = con.getPrimaryKeyCB("Marca", cond + "'" + this.cbMarca.getSelectedItem().toString() + "'");
            datos = pk + "," + this.txtPrecio.getText() + ","
                    + tipo + ","
                    + "'" + this.cbTallaB.getSelectedItem().toString() + "'" + ","
                    + this.txtVendidas.getText() + ","
                    + this.txtStock.getText() + ","
                    + marca + ","
                    + this.txtGarantia.getText();
            try {
                con.post("Bicicleta", datos);
                JOptionPane.showMessageDialog(null, "Datos agregados");
                System.out.println(datos);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "ERROR " + ex);
            }
            if (this.indi == 1) {
                this.irBiciComp(true);
            } else if (this.indi == 2) {
                this.setVisible(false);
            }

        }
    }

    private void btnGuardarAcc() {
        if (this.txtPrecioA.getText().trim().equals("") || this.txtVendidasA.getText().trim().equals("") || this.txtStockA.getText().trim().equals("")
                || this.txtGarantiaA.getText().trim().equals("")) {

            JOptionPane.showMessageDialog(null, "Llene todos los espacios");

        } else if (Integer.parseInt(this.txtPrecioA.getText()) < 5000) {
            JOptionPane.showMessageDialog(null, "Precio incorrecto");
        } else {

            String carac;
            if (this.txtCaraA.getText().trim().equals("")) {
                carac = "NA";
            } else {
                carac = this.txtCaraA.getText();
            }
            String datos;
            String cond = "WHERE Nombre = ";
            int pk = con.getPrimarykeyDisp("Accesorio");
            int tipo = con.getPrimaryKeyCB("TipoAccesorio", cond + "'" + this.cbTipoA.getSelectedItem().toString() + "'");
            int marca = con.getPrimaryKeyCB("Marca", cond + "'" + this.cbMarcaA.getSelectedItem().toString() + "'");
            datos = pk + "," + tipo + ","
                    + this.txtPrecioA.getText() + ","
                    + this.txtVendidasA.getText() + ","
                    + this.txtStockA.getText() + ","
                    + "'" + this.cbColorA.getSelectedItem().toString() + "'" + ","
                    + "'" + this.cbMaterialA.getSelectedItem().toString() + "'" + ","
                    + marca + ","
                    + "'" + carac + "'" + ","
                    + this.txtGarantiaA.getText();

            con.post("Accesorio", datos);

            JOptionPane.showMessageDialog(null, "Datos agregados");
            System.out.println(datos);
        }
    }

    private void btnGuardarComp() {

        if (this.txtVendidasC.getText().trim().equals("") || this.txtStockC.getText().trim().equals("")
                || this.txtGarantiaC.getText().trim().equals("") || this.txtPrecioC.getText().trim().equals("")
                || this.txtCarC.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Llene todos los espacios");
        } else if (Integer.parseInt(this.txtPrecioC.getText()) < 5000) {
            JOptionPane.showMessageDialog(null, "Precio Incorrecto");
        } else {
            String datos;
            String cond = "WHERE Nombre = ";
            int pk = con.getPrimarykeyDisp("Componente");
            int tipo = con.getPrimaryKeyCB("TipoComponente", cond + "'" + this.cbTipoC.getSelectedItem().toString() + "'");
            int marca = con.getPrimaryKeyCB("Marca", cond + "'" + this.cbMarcaC.getSelectedItem().toString() + "'");

            datos = pk + "," + marca + ","
                    + "'" + this.cbMaterialC.getSelectedItem().toString() + "',"
                    + "'" + this.cbColorC.getSelectedItem().toString() + "',"
                    + this.txtStockC.getText() + ","
                    + this.txtVendidasC.getText() + ","
                    + this.txtGarantiaC.getText() + ","
                    + this.txtPrecioC.getText() + ","
                    + tipo + ","
                    + "'" + this.txtCarC.getText() + "'";

            con.post("Componente", datos);
            JOptionPane.showMessageDialog(null, "Datos agregados");
            System.out.println(datos);
            if (this.indi == 3) {
                this.setVisible(false);
            }
        }

    }

    public void btnNewColorA() {

        String nColor = JOptionPane.showInputDialog("Ingrese el nuevo color");
        this.cbColorA.addItem(nColor);
    }

    public void btnNewMaterialA() {
        String nColor = JOptionPane.showInputDialog("Ingrese el nuevo material");
        this.cbMaterialA.addItem(nColor);
    }

    public void btnNewMaterialC() {
        String nColor = JOptionPane.showInputDialog("Ingrese el nuevo material");
        this.cbMaterialC.addItem(nColor);
    }

    public void btnNewColorC() {
        String nColor = JOptionPane.showInputDialog("Ingrese el nuevo color");
        this.cbColorC.addItem(nColor);
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
            String datos = "'" + nomb + "','" + desc + "'";
            con.post("TipoBicicleta", datos);
            this.llenar();
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
            String datos = "'" + nomb + "','" + desc + "'";
            con.post("TipoComponente", datos);
            this.llenar();
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
            String datos = "'" + nomb + "','" + desc + "'";
            con.post("TipoAccesorio", datos);
            this.llenar();
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
            String datos = "'" + nomb + "','" + desc + "'";
            con.post("Marca", datos);

            this.llenar();
        }
    }

    private void irBiciComp(boolean dist) {
        this.setVisible(false);
        BiciComp bcomp = new BiciComp(this, dist);
        bcomp.setVisible(true);
    }

    private void ponerImagen() {

        Toolkit tk = Toolkit.getDefaultToolkit();
        String ruta = "./_data/Bicicol.png";
        Image imagen = tk.createImage(ruta);
        logo.setIcon(new ImageIcon(imagen.getScaledInstance(logo.getWidth(), logo.getHeight(), Image.SCALE_AREA_AVERAGING)));

    }

    @Override
    public void keyTyped(KeyEvent e) {

        char c = e.getKeyChar();

        if (e.getSource() == this.txtPrecio) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }

        if (e.getSource() == this.txtVendidas) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.txtStock) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.txtGarantia) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.txtStockC) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.txtVendidasC) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.txtGarantiaC) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.txtPrecioC) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.txtPrecioA) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.txtVendidasA) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.txtStockA) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
        if (e.getSource() == this.txtGarantiaA) {
            if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))) {
                getToolkit().beep();//sonido
                JOptionPane.showMessageDialog(null, "Carácter no válido");
                e.consume();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnVolver = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        cbTipo = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbMarca = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        btnGuardarBici = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        nuevoTipoBici = new javax.swing.JButton();
        nuevaMarcaB = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        txtVendidas = new javax.swing.JTextField();
        txtStock = new javax.swing.JTextField();
        txtGarantia = new javax.swing.JTextField();
        cbTallaB = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        cbMarcaC = new javax.swing.JComboBox<>();
        txtPrecioC = new javax.swing.JTextField();
        cbTipoC = new javax.swing.JComboBox<>();
        txtCarC = new javax.swing.JTextField();
        btnGuardaComp = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        nuevaMarcaC = new javax.swing.JButton();
        nuevoTipoCom = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        txtStockC = new javax.swing.JTextField();
        txtVendidasC = new javax.swing.JTextField();
        txtGarantiaC = new javax.swing.JTextField();
        cbMaterialC = new javax.swing.JComboBox<>();
        btnNewMaterialC = new javax.swing.JButton();
        cbColorC = new javax.swing.JComboBox<>();
        btnNewColorC = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        cbTipoA = new javax.swing.JComboBox<>();
        txtPrecioA = new javax.swing.JTextField();
        cbMarcaA = new javax.swing.JComboBox<>();
        txtCaraA = new javax.swing.JTextField();
        nuevoTipoAc = new javax.swing.JButton();
        nuevaMarcaA = new javax.swing.JButton();
        btnGuardaAcc = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        txtVendidasA = new javax.swing.JTextField();
        txtStockA = new javax.swing.JTextField();
        txtGarantiaA = new javax.swing.JTextField();
        cbColorA = new javax.swing.JComboBox<>();
        btnNewColorA = new javax.swing.JButton();
        cbMaterialA = new javax.swing.JComboBox<>();
        btnNewMatA = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        logo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Agregar");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Precio");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Tipo");

        cbTipo.setModel(modelCTipoB);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Talla");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Unidades Vendidas");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Marca");

        cbMarca.setModel(modelCMarca);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Garantía");

        btnGuardarBici.setText("Guardar Bicicleta");
        btnGuardarBici.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarBiciActionPerformed(evt);
            }
        });

        jButton2.setText("Limpiar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        nuevoTipoBici.setText("Nuevo");
        nuevoTipoBici.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoTipoBiciActionPerformed(evt);
            }
        });

        nuevaMarcaB.setText("Nueva");
        nuevaMarcaB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevaMarcaBActionPerformed(evt);
            }
        });

        jButton13.setText("Seleccionar Componentes");
        jButton13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel28.setText("Unidades en Stock");

        cbTallaB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "XS", "S", "M", "L", "XL" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardarBici, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(172, 172, 172))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(193, 193, 193)
                        .addComponent(jButton13))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(95, 95, 95)
                                .addComponent(txtGarantia, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(cbMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(139, 139, 139)
                                                .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addComponent(jLabel8))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nuevoTipoBici)
                                    .addComponent(nuevaMarcaB)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel28)
                                    .addGap(37, 37, 37)
                                    .addComponent(txtStock))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel3))
                                    .addGap(37, 37, 37)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtPrecio)
                                        .addComponent(cbTallaB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtVendidas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(102, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nuevoTipoBici))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cbMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nuevaMarcaB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbTallaB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtVendidas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtGarantia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addComponent(jButton13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardarBici)
                    .addComponent(jButton2))
                .addGap(32, 32, 32))
        );

        jTabbedPane1.addTab("Bicicleta", jPanel2);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Marca");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Material");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Color");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("Unidades Vendidas");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Garantía");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setText("Precio");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setText("Tipo");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText("Características");

        cbMarcaC.setModel(modelCMarcaC);

        cbTipoC.setModel(modelCTipoC);

        btnGuardaComp.setText("Guardar Componente");
        btnGuardaComp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardaCompActionPerformed(evt);
            }
        });

        jButton4.setText("Limpiar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        nuevaMarcaC.setText("Nueva");
        nuevaMarcaC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevaMarcaCActionPerformed(evt);
            }
        });

        nuevoTipoCom.setText("Nuevo");
        nuevoTipoCom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoTipoComActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel29.setText("Unidades en Stock");

        cbMaterialC.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aluminio", "Acero", "Magnesio", "Cuero", "Caucho" }));

        btnNewMaterialC.setText("Nuevo");
        btnNewMaterialC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewMaterialCActionPerformed(evt);
            }
        });

        cbColorC.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Blanco", "Negro", "Gris", "Amarillo", "Azul", "Rojo", "Verde", "Morado", "Naranja", "Cafe" }));

        btnNewColorC.setText("Nuevo");
        btnNewColorC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewColorCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(58, 58, 58)
                        .addComponent(txtCarC, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jLabel16))
                        .addGap(93, 93, 93)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(cbTipoC, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(nuevoTipoCom))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cbColorC, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbMaterialC, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbMarcaC, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(nuevaMarcaC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnNewMaterialC)
                                    .addComponent(btnNewColorC)))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel29)
                            .addComponent(jLabel13)
                            .addComponent(jLabel15))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtGarantiaC)
                            .addComponent(txtVendidasC)
                            .addComponent(txtStockC)
                            .addComponent(txtPrecioC, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(btnGuardaComp)
                        .addGap(30, 30, 30)
                        .addComponent(jButton4)))
                .addContainerGap(97, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(cbTipoC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nuevoTipoCom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cbMarcaC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nuevaMarcaC))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cbMaterialC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewMaterialC))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(cbColorC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewColorC))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtPrecioC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtStockC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txtVendidasC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtGarantiaC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtCarC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardaComp)
                    .addComponent(jButton4))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Componente", jPanel3);

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setText("Tipo");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("Precio");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setText("Unidades Vendidas");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setText("Unidades en Stock");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel23.setText("Color");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setText("Material");

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setText("Marca");

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setText("Características");

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel27.setText("Garantía");

        cbTipoA.setModel(modelCTipoA);

        cbMarcaA.setModel(modelCMarcaA);

        nuevoTipoAc.setText("Nuevo");
        nuevoTipoAc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoTipoAcActionPerformed(evt);
            }
        });

        nuevaMarcaA.setText("Nueva");

        btnGuardaAcc.setText("Guardar Accesorio");
        btnGuardaAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardaAccActionPerformed(evt);
            }
        });

        jButton12.setText("Limpiar");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        cbColorA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Blanco", "Negro", "Gris", "Amarillo", "Azul", "Rojo", "Verde", "Morado", "Naranja", "Cafe" }));

        btnNewColorA.setText("Nuevo");
        btnNewColorA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewColorAActionPerformed(evt);
            }
        });

        cbMaterialA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Plastico", "Aluminio", "Spandex", "Cuero", "Tela", "Poliestireno" }));

        btnNewMatA.setText("Nuevo");
        btnNewMatA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewMatAActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel25)
                            .addComponent(jLabel20)
                            .addComponent(jLabel23))
                        .addGap(90, 90, 90)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtPrecioA, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbColorA, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbMaterialA, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbMarcaA, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbTipoA, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nuevoTipoAc)
                            .addComponent(nuevaMarcaA)
                            .addComponent(btnNewMatA)
                            .addComponent(btnNewColorA)))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel21)
                                .addComponent(jLabel22))
                            .addGap(21, 21, 21)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtVendidasA, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtStockA, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel27)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel26)
                                    .addGap(46, 46, 46)))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtGarantiaA, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                                .addComponent(txtCaraA)))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(btnGuardaAcc)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton12))))
                .addContainerGap(110, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(cbTipoA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nuevoTipoAc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(cbMarcaA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nuevaMarcaA))
                .addGap(19, 19, 19)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(cbMaterialA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewMatA))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(cbColorA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewColorA))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtPrecioA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtVendidasA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStockA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txtGarantiaA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addGap(61, 61, 61))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(txtCaraA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardaAcc)
                            .addComponent(jButton12))
                        .addGap(24, 24, 24))))
        );

        jTabbedPane1.addTab("Accesorio", jPanel4);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText("Agregar Registros");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnVolver)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(28, 28, 28)
                                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(34, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(btnVolver)
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

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed

        this.btnVolver();

    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnGuardarBiciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarBiciActionPerformed

        this.btnGuardarBici();

    }//GEN-LAST:event_btnGuardarBiciActionPerformed

    private void nuevoTipoBiciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoTipoBiciActionPerformed
        this.btnNewTipoB();
    }//GEN-LAST:event_nuevoTipoBiciActionPerformed

    private void btnNewColorCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewColorCActionPerformed
        this.btnNewColorC();
    }//GEN-LAST:event_btnNewColorCActionPerformed

    private void btnNewMaterialCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewMaterialCActionPerformed
        this.btnNewMaterialC();
    }//GEN-LAST:event_btnNewMaterialCActionPerformed

    private void btnGuardaCompActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardaCompActionPerformed

        this.btnGuardarComp();
    }//GEN-LAST:event_btnGuardaCompActionPerformed

    private void btnNewMatAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewMatAActionPerformed
        this.btnNewMaterialA();
    }//GEN-LAST:event_btnNewMatAActionPerformed

    private void btnNewColorAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewColorAActionPerformed
        this.btnNewColorA();
    }//GEN-LAST:event_btnNewColorAActionPerformed

    private void btnGuardaAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardaAccActionPerformed

        this.btnGuardarAcc();
    }//GEN-LAST:event_btnGuardaAccActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.limpiar();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.limpiar();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        this.limpiar();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void nuevaMarcaBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevaMarcaBActionPerformed
        this.btnNewMarca();
    }//GEN-LAST:event_nuevaMarcaBActionPerformed

    private void nuevoTipoComActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoTipoComActionPerformed
        this.btnNewTipoC();
    }//GEN-LAST:event_nuevoTipoComActionPerformed

    private void nuevaMarcaCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevaMarcaCActionPerformed
        this.btnNewMarca();
    }//GEN-LAST:event_nuevaMarcaCActionPerformed

    private void nuevoTipoAcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoTipoAcActionPerformed
        this.btnNewTipoA();
    }//GEN-LAST:event_nuevoTipoAcActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        this.irBiciComp(false);
    }//GEN-LAST:event_jButton13ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardaAcc;
    private javax.swing.JButton btnGuardaComp;
    private javax.swing.JButton btnGuardarBici;
    private javax.swing.JButton btnNewColorA;
    private javax.swing.JButton btnNewColorC;
    private javax.swing.JButton btnNewMatA;
    private javax.swing.JButton btnNewMaterialC;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox<String> cbColorA;
    private javax.swing.JComboBox<String> cbColorC;
    private javax.swing.JComboBox<String> cbMarca;
    private javax.swing.JComboBox<String> cbMarcaA;
    private javax.swing.JComboBox<String> cbMarcaC;
    private javax.swing.JComboBox<String> cbMaterialA;
    private javax.swing.JComboBox<String> cbMaterialC;
    private javax.swing.JComboBox<String> cbTallaB;
    private javax.swing.JComboBox<String> cbTipo;
    private javax.swing.JComboBox<String> cbTipoA;
    private javax.swing.JComboBox<String> cbTipoC;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
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
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel logo;
    private javax.swing.JButton nuevaMarcaA;
    private javax.swing.JButton nuevaMarcaB;
    private javax.swing.JButton nuevaMarcaC;
    private javax.swing.JButton nuevoTipoAc;
    private javax.swing.JButton nuevoTipoBici;
    private javax.swing.JButton nuevoTipoCom;
    private javax.swing.JTextField txtCarC;
    private javax.swing.JTextField txtCaraA;
    private javax.swing.JTextField txtGarantia;
    private javax.swing.JTextField txtGarantiaA;
    private javax.swing.JTextField txtGarantiaC;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtPrecioA;
    private javax.swing.JTextField txtPrecioC;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtStockA;
    private javax.swing.JTextField txtStockC;
    private javax.swing.JTextField txtVendidas;
    private javax.swing.JTextField txtVendidasA;
    private javax.swing.JTextField txtVendidasC;
    // End of variables declaration//GEN-END:variables

}
