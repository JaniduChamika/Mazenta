/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.GrnReport;
import model.MySQL;
import model.PreviousGRN;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author ROG STRIX
 */
public class PreviousGrn extends javax.swing.JDialog {

    Phamarcy ph;

    DecimalFormat df = new DecimalFormat("0.00");

    SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Creates new form PreviousGrn
     */
    public PreviousGrn(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        jTable1.getTableHeader().setFont(new Font("Airal Nova", Font.PLAIN, 14));

        loadSupplier();
        loadGrn();

        ImageIcon i = new ImageIcon("src/resourse/image/3.png");
        Image x = i.getImage();
        setIconImage(x);
    }

    public PreviousGrn(Phamarcy ph, boolean modal) {
        super(ph, modal);
        initComponents();
        jTable1.getTableHeader().setFont(new Font("Airal Nova", Font.PLAIN, 14));

        loadSupplier();
        loadGrn();
        this.ph = ph;
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 9; i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        }

        ImageIcon i = new ImageIcon("src/resourse/image/3.png");
        Image x = i.getImage();
        setIconImage(x);
    }

    private void loadSupplier() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `supplier`");

            Vector v = new Vector();
            v.add("Select");

            while (rs.next()) {
                v.add(rs.getString("name"));
            }

            jSupplier.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadGrn() {

        String supplier = jSupplier.getSelectedItem().toString();
        String grnid = jSearch1.getText();
        Date date = jDateChooser1.getDate();
    
        try {
            String q = "SELECT * \n"
                    + "FROM `grn_item`\n"
                    + "INNER JOIN `grn` ON `grn_item`.`grn_id` =`grn`.`id`\n"
                    + "INNER JOIN `supplier` ON `grn`.`supplier_id`=`supplier`.`id`\n"
                    + "INNER JOIN `company_branch` ON `supplier`.`company_branch_id`=`company_branch`.`id`";

            if (!supplier.equals("Select") || !grnid.isEmpty() || date != null) {
                q += " WHERE ";
            }
            Vector queryVector = new Vector();
            if (!supplier.equals("Select")) {
                queryVector.add("`supplier`.`name`='" + supplier + "'");
            }
            if (!grnid.isEmpty()) {
                queryVector.add("`grn`.`unique_id`='" + grnid + "'");
            }
            if (date != null) {
                queryVector.add("`grn`.`date_time`='" + datef.format(date) + "'");
            }
            for (int i = 0; i < queryVector.size(); i++) {
                q += queryVector.get(i);
                if (i < queryVector.size() - 1) {
                    q += " AND ";

                }
            }
            ResultSet rs = MySQL.search(q + " GROUP BY `grn`.`id` ");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("grn.unique_id"));
                v.add(rs.getString("date_time"));
                v.add(rs.getString("supplier.name"));
                v.add(rs.getString("supplier.phone_no"));
                v.add(rs.getString("company_branch.name"));
                String q2 = "SELECT *\n"
                        + "FROM `grn_item`\n"
                        + " WHERE `grn_item`.`grn_id`='" + rs.getString("grn.id") + "'";
                ResultSet rs2 = MySQL.search(q2);

                double total = 0;
                while (rs2.next()) {
                    total += Double.valueOf(rs2.getString("buying_price")) * Integer.valueOf(rs2.getString("qty"));
                }
                v.add(df.format(total));
                String q3 = "SELECT *,SUM( `grn_payment`.`payment`) AS `totpayment` FROM `grn_payment`"
                        + "INNER JOIN `payment_method` ON `grn_payment`.`payment_method_id`=`payment_method`.`id` "
                        + " WHERE `grn_payment`.`grn_id`='" + rs.getString("grn.id") + "'";
                ResultSet rs3 = MySQL.search(q3);
                rs3.next();
                v.add(rs3.getString("payment_method.name"));
                v.add(df.format(Double.valueOf(rs3.getString("totpayment"))));
                v.add(df.format(Double.valueOf(rs3.getString("totpayment")) - total));

                dtm.addRow(v);
            }
            cal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cal() {

        int r = jTable1.getRowCount();
        double total = 0;
        double payment = 0;
        double balance = 0;
        for (int i = 0; i < r; i++) {
            total += Double.valueOf(jTable1.getValueAt(i, 5).toString());
            payment += Double.valueOf(jTable1.getValueAt(i, 7).toString());
            balance += Double.valueOf(jTable1.getValueAt(i, 8).toString());
        }
        jLabel2.setText(df.format(total));
        jLabel4.setText(df.format(payment));
        jLabel5.setText(df.format(balance));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSupplier = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel27 = new javax.swing.JLabel();
        jSearch1 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("GRN History");

        jTable1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "GRN ID", "Date", "Supplier", "Supplier Contact No", "Branch", "Total", "Paid Mehod", "Paid", "Balance"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(204, 204, 204));
        jTable1.setShowGrid(false);
        jTable1.setShowHorizontalLines(true);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(jTable1);

        jSupplier.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jSupplier.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jSupplierItemStateChanged(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel26.setText("Supplier");

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel27.setText("Date");

        jSearch1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jSearch1.setToolTipText("Search by grn code");
        jSearch1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jSearch1KeyReleased(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel24.setText("GRN Code");

        jButton1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 51, 51));
        jButton1.setText("Clear");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(51, 51, 51));
        jButton2.setText("Print");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel1.setText("Total GRN Price");

        jLabel2.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel2.setText("0.00");

        jLabel3.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel3.setText("Total Payments");

        jLabel4.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel4.setText("0.00");

        jLabel5.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel5.setText("0.00");

        jLabel6.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel6.setText("Total Balance");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel26)
                        .addGap(9, 9, 9)
                        .addComponent(jSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel26)
                        .addComponent(jLabel24)
                        .addComponent(jSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel27))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jLabel5))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)))
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
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jSupplierItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jSupplierItemStateChanged
        loadGrn();
    }//GEN-LAST:event_jSupplierItemStateChanged

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        if (jDateChooser1.getDate() != null) {

            loadGrn();
        }
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jSearch1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSearch1KeyReleased
        loadGrn();
    }//GEN-LAST:event_jSearch1KeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2) {
            if (jTable1.getSelectedRow() != -1) {

                String unid = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
                GrnItem gi = new GrnItem(ph, true, unid);
                gi.grncode.setText(unid);
                gi.setVisible(true);
            }

        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jDateChooser1.setDate(null);
        jSupplier.setSelectedIndex(0);
        jSearch1.setText("");
        loadGrn();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        String Grnadtotal = jLabel2.getText();
        String Grnadpayment = jLabel4.getText();
        String Grnadbalance = jLabel5.getText();
        try {
//            InputStream pathStream = PreviousGrn.class.getResourceAsStream("../reports/PreviousGrn.jasper");
            String pathStream = "src/reports/PreviousGrn.jasper";

            HashMap parameters = new HashMap();
            parameters.put("total", "Rs. " + Grnadtotal);
            parameters.put("payment", "Rs. " + Grnadpayment);
            parameters.put("balance", "Rs. " + Grnadbalance);

            Vector<model.PreviousGRN> v = new Vector();
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                String id = jTable1.getValueAt(i, 0).toString();
                String date = jTable1.getValueAt(i, 1).toString();
                String supplier = jTable1.getValueAt(i, 2).toString();
                String scontact = jTable1.getValueAt(i, 3).toString();
                String branch = jTable1.getValueAt(i, 4).toString();
                String total = jTable1.getValueAt(i, 5).toString();
                String method = jTable1.getValueAt(i, 6).toString();
                String paid = jTable1.getValueAt(i, 7).toString();
                String balance = jTable1.getValueAt(i, 8).toString();
                v.add(new PreviousGRN(id, date, supplier, scontact, branch, total, method, paid, balance));
            }
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(v);
            JasperPrint jp = JasperFillManager.fillReport(pathStream, parameters, dataSource);
            JasperViewer.viewReport(jp, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dispose();

    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PreviousGrn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PreviousGrn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PreviousGrn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PreviousGrn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PreviousGrn dialog = new PreviousGrn(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JTextField jSearch1;
    private javax.swing.JComboBox<String> jSupplier;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
