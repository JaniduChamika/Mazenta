/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.MySQL;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author ROG STRIX
 */
public class Laboratory extends javax.swing.JFrame {
    
    SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat df = new DecimalFormat("0.00");
    Color b = new Color(84, 110, 122);

    /**
     * Creates new form Laboratory
     */
    public Laboratory() {
        initComponents();
        jTable1.getTableHeader().setFont(new Font("Airal Nova", Font.PLAIN, 14));
        
        loadTest();
        
        loadMedicalTest();
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < 4; i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            
        }
        Listner();
        
        ImageIcon i = new ImageIcon("src/resourse/image/3.png");
        Image x = i.getImage();
        setIconImage(x);
        this.setAlwaysOnTop(false);
    }
    
    private void Listner() {
        ListSelectionListener lsl = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int r = jTable1.getSelectedRow();
                if (r != -1) {
                    jtestId.setText(jTable1.getValueAt(r, 0).toString());
                    jTeasetName.setText(jTable1.getValueAt(r, 2).toString());
                    
                    jsetbtn.setEnabled(true);
                    
                }
            }
        };
        jTable1.getSelectionModel().addListSelectionListener(lsl);
    }
    
    public void loadMedicalTest() {
        try {
            String q = "SELECT *\n"
                    + "FROM `medical_test_has_tests`\n"
                    + "INNER JOIN `medical_test` ON `medical_test_has_tests`.`medical_test_id`=`medical_test`.`id`\n"
                    + "INNER JOIN `tests` ON `medical_test_has_tests`.`tests_id`=`tests`.`id`\n"
                    + "INNER JOIN `reference_no` ON `medical_test`.`reference_no_id`=`reference_no`.`id`\n"
                    + "INNER JOIN `patient` ON `reference_no`.`patient_id`=`patient`.`id`\n"
                    + "INNER JOIN `status` ON `medical_test_has_tests`.`status_id`=`status`.`id`\n"
                    + "LEFT JOIN `medical_test_payment` ON `medical_test`.`id`=`medical_test_payment`.`medical_test_id`"
                    + " WHERE `medical_test_has_tests`.`status_id`='1'";
            String txt = jSearch1.getText();
            String test = jtest.getSelectedItem().toString();
            
            Vector query = new Vector();
            
            if (!txt.isEmpty()) {
                query.add("(`reference_no`.`num` LIKE '" + txt + "%' OR `medical_test_has_tests`.`id`='" + txt + "')");
                
            }
            
            if (!test.equals("Select")) {
                
                query.add(" `tests`.`name`='" + test + "'");
            }
            
            for (int i = 0; i < query.size(); i++) {
                if (i < query.size()) {
                    q += " AND ";
                    
                }
                q += query.get(i);
                
            }
            ResultSet rs = MySQL.search(q + " ORDER BY `medical_test`.`id` ASC");
            
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v = new Vector();
                
                v.add(rs.getString("medical_test_has_tests.id"));
                v.add(rs.getString("reference_no.num"));
                
                v.add(rs.getString("tests.name"));
                v.add(rs.getString("patient.name"));
                v.add(rs.getString("patient.phone_no"));
                v.add(rs.getString("apply_date"));
                
                dtm.addRow(v);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadTest() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `tests` ORDER BY `name` ASC");
            
            Vector v = new Vector();
            v.add("Select");
            
            while (rs.next()) {
                v.add(rs.getString("name"));
            }
            
            jtest.setModel(new DefaultComboBoxModel(v));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        jPanel2 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jSearch1 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jtest = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel84 = new javax.swing.JLabel();
        jsetbtn = new javax.swing.JButton();
        jtestId = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jTeasetName = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Laborotory");
        setMinimumSize(new java.awt.Dimension(1110, 740));

        jPanel1.setPreferredSize(new java.awt.Dimension(1083, 620));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));

        jLabel24.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel24.setText("Search");

        jSearch1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jSearch1.setToolTipText("Search by Med.Test ID or Reference No");
        jSearch1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jSearch1KeyReleased(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel26.setText("Test");

        jtest.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jtest.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jtestItemStateChanged(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 51, 51));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resourse/icon/loutb.png"))); // NOI18N
        jButton1.setText("Logout");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel26)
                .addGap(18, 18, 18)
                .addComponent(jtest, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTable1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Med.Test ID", "Reference No", "Test", "Patient", "Patient Contact No", "Applied  Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(204, 204, 204));
        jTable1.setMinimumSize(new java.awt.Dimension(120, 0));
        jTable1.setShowGrid(false);
        jTable1.setShowHorizontalLines(true);
        jScrollPane10.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Payment Info"));

        jLabel84.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel84.setText("Med Test ID");

        jsetbtn.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jsetbtn.setForeground(new java.awt.Color(51, 51, 51));
        jsetbtn.setText("Enter Report Details");
        jsetbtn.setEnabled(false);
        jsetbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jsetbtnActionPerformed(evt);
            }
        });

        jtestId.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jtestId.setText("None");

        jLabel56.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel56.setText("Test");

        jTeasetName.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jTeasetName.setText("None");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel56)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabel84, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtestId, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTeasetName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jsetbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtestId)
                    .addComponent(jLabel84))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(jTeasetName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsetbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1098, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 728, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jSearch1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSearch1KeyReleased
        loadMedicalTest();
    }//GEN-LAST:event_jSearch1KeyReleased

    private void jtestItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jtestItemStateChanged
        loadMedicalTest();
    }//GEN-LAST:event_jtestItemStateChanged

    private void jsetbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jsetbtnActionPerformed
        String tid = jtestId.getText();
        String tname = jTeasetName.getText();


        TestSubmission ts = new TestSubmission(this, true, tid, tname);
        ts.setVisible(true);
    }//GEN-LAST:event_jsetbtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new LogIn().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(Laboratory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Laboratory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Laboratory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Laboratory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Laboratory().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JTextField jSearch1;
    private javax.swing.JTable jTable1;
    public javax.swing.JLabel jTeasetName;
    public javax.swing.JButton jsetbtn;
    private javax.swing.JComboBox<String> jtest;
    public javax.swing.JLabel jtestId;
    // End of variables declaration//GEN-END:variables
}
