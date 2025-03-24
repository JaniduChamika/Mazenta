/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.MySQL;
import model.PreviousAppointmentR;
import model.PreviousGRN;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author ROG STRIX
 */
public class PreviousAppointment extends javax.swing.JDialog {
    
    SimpleDateFormat stf = new SimpleDateFormat("hh:mm a");
    SimpleDateFormat stf2 = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat df = new DecimalFormat("0.00");
    Color b = new Color(84, 110, 122);

    /**
     * Creates new form PreviousAppointment
     */
    public PreviousAppointment(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        jTable1.getTableHeader().setFont(new Font("Airal Nova", Font.PLAIN, 14));
        loadSpecialty();
        loadDoctor2();
        loadAppoitment();
        
        ImageIcon i = new ImageIcon("src/resourse/image/3.png");
        Image x = i.getImage();
        setIconImage(x);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int y = 0; y < 9; y++) {
            jTable1.getColumnModel().getColumn(y).setCellRenderer(centerRenderer);
            
        }
    }
    
    public void loadAppoitment() {
        try {
            String q = "SELECT * FROM `appointment` INNER JOIN `doctor` ON `appointment`.`doctor_id`=`doctor`.`id` INNER JOIN `speciality` ON `doctor`.`speciality_id`=`speciality`.`id` INNER JOIN `reference_no` ON `appointment`.`reference_no_id`=`reference_no`.`id` INNER JOIN `patient` ON `reference_no`.`patient_id`=`patient`.`id` "
                    + "WHERE `appointment`.`appoint_date`<='" + datef.format(new Date()) + "' ";
            String txt = jSearch1.getText();
            String specailty = jSearchSpecailty.getSelectedItem().toString();
            String doctor = jSearchDoctor.getSelectedItem().toString();
            Date selDate = jDateChooser1.getDate();
            
            Vector query = new Vector();
            
            if (!txt.isEmpty()) {
                query.add("(`reference_no`.`num` LIKE '" + txt + "%' OR `patient`.`name` LIKE '" + txt + "%' OR `patient`.`phone_no` LIKE '" + txt + "%')");
                
            }
            if (!specailty.equals("Select")) {
                
                query.add("`speciality`.`name`='" + specailty + "'");
            }
            if (!doctor.equals("Select")) {
                
                query.add("`doctor`.`name`='" + doctor + "'");
            }
            if (selDate != null) {
                
                query.add("`appointment`.`appoint_date`='" + datef.format(selDate) + "'");
            }
            for (int i = 0; i < query.size(); i++) {
                q += " AND ";
                q += query.get(i);
//                if (i < query.size() - 1) {
//                    
//                }
            }
            ResultSet rs = MySQL.search(q + " ORDER BY `appointment`.`appoint_date` ,`appointment`.`start_time` ASC");
            
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v = new Vector();
                
                v.add(rs.getString("appointment.id"));
                v.add(rs.getString("reference_no.num"));
                v.add(rs.getString("patient.name"));
                v.add(rs.getString("patient.phone_no"));
                
                v.add(rs.getString("speciality.name"));
                v.add(rs.getString("doctor.name"));
                v.add(rs.getString("appoint_date"));
                v.add(stf.format(stf2.parse(rs.getString("start_time"))) + "-" + stf.format(stf2.parse(rs.getString("end_time"))));
                v.add("Rs. " + df.format(Double.valueOf(rs.getString("fee"))));
                String nowTime = stf2.format(new Date());
                String today = datef.format(new Date());
                String endTime = rs.getString("end_time");
                if (today.equals(rs.getString("appoint_date"))) {
                    if (stf2.parse(nowTime).after(stf2.parse(endTime))) {
                        dtm.addRow(v);
                        
                    }
                } else {
                    dtm.addRow(v);
                    
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadSpecialty() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `speciality`");
            
            Vector v = new Vector();
            v.add("Select");
            
            while (rs.next()) {
                v.add(rs.getString("name"));
            }
            
            jSearchSpecailty.setModel(new DefaultComboBoxModel(v));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadDoctor2() {
        try {
            
            String specialty2 = jSearchSpecailty.getSelectedItem().toString();
            String q = "SELECT * FROM `doctor` INNER JOIN `speciality` ON `doctor`.`speciality_id`=`speciality`.`id`";
            if (!specialty2.equals("Select")) {
                q += "WHERE `speciality`.`name`='" + specialty2 + "'";
            }
            ResultSet rs = MySQL.search(q);
            Vector v = new Vector();
            v.add("Select");
            
            while (rs.next()) {
                v.add(rs.getString("name"));
            }
            
            jSearchDoctor.setModel(new DefaultComboBoxModel(v));
            
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

        jPanel4 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jSearch1 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jSearchSpecailty = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jSearchDoctor = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Appointment History");
        setResizable(false);

        jPanel4.setPreferredSize(new java.awt.Dimension(464, 430));

        jTable1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Appointment ID", "Reference No", "Patient Name", "Patient Phone No", "Specialty", "Docter", "Date", "Time", "Fee"
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
        jTable1.setMinimumSize(new java.awt.Dimension(120, 0));
        jTable1.setShowGrid(false);
        jTable1.setShowHorizontalLines(true);
        jScrollPane10.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 51, 51));
        jButton1.setText("Clear");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jSearch1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jSearch1.setToolTipText("Search by Appointment ID, Patient Name, Patient Phone No");
        jSearch1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jSearch1KeyReleased(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel24.setText("Search");

        jLabel26.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel26.setText("Specialty");

        jSearchSpecailty.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jSearchSpecailty.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jSearchSpecailtyItemStateChanged(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel28.setText("Docter");

        jSearchDoctor.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jSearchDoctor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jSearchDoctorItemStateChanged(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel27.setText("Date");

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26)
                        .addGap(9, 9, 9)
                        .addComponent(jSearchSpecailty, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSearchDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24)
                        .addComponent(jSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSearchSpecailty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel26)
                        .addComponent(jLabel27)
                        .addComponent(jSearchDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel28))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jSearch1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSearch1KeyReleased
        loadAppoitment();
    }//GEN-LAST:event_jSearch1KeyReleased

    private void jSearchSpecailtyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jSearchSpecailtyItemStateChanged
        loadAppoitment();
    }//GEN-LAST:event_jSearchSpecailtyItemStateChanged

    private void jSearchDoctorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jSearchDoctorItemStateChanged
        loadAppoitment();
    }//GEN-LAST:event_jSearchDoctorItemStateChanged

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        if (jDateChooser1.getDate() != null) {
            
            loadAppoitment();
        }
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
//            InputStream pathStream = PreviousGrn.class.getResourceAsStream("../reports/AppointmentHistory.jasper");
            String pathStream = "src/reports/AppointmentHistory.jasper";
            
            HashMap parameters = new HashMap();
            
            Vector<model.PreviousAppointmentR> v = new Vector();
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                String id = jTable1.getValueAt(i, 0).toString();
                String reference = jTable1.getValueAt(i, 1).toString();
                String pname = jTable1.getValueAt(i, 2).toString();
                String pcontact = jTable1.getValueAt(i, 3).toString();
                String spacialty = jTable1.getValueAt(i, 4).toString();
                String doctor = jTable1.getValueAt(i, 5).toString();
                String date = jTable1.getValueAt(i, 6).toString();
                String time = jTable1.getValueAt(i, 7).toString();
                String fee = jTable1.getValueAt(i, 8).toString();
                v.add(new PreviousAppointmentR(id, reference, pname, pcontact, spacialty, doctor, date, time, fee));
            }
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(v);
            JasperPrint jp = JasperFillManager.fillReport(pathStream, parameters, dataSource);
            JasperViewer.viewReport(jp, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jSearch1.setText("");
        jSearchSpecailty.setSelectedIndex(0);
        jSearchDoctor.setSelectedIndex(0);
        jDateChooser1.setDate(null);
        loadAppoitment();
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
            java.util.logging.Logger.getLogger(PreviousAppointment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PreviousAppointment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PreviousAppointment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PreviousAppointment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PreviousAppointment dialog = new PreviousAppointment(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JTextField jSearch1;
    private javax.swing.JComboBox<String> jSearchDoctor;
    private javax.swing.JComboBox<String> jSearchSpecailty;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
