/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package gui;

import java.awt.Image;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.MySQL;

/**
 *
 * @author ROG STRIX
 */
public class ChannelTime extends javax.swing.JDialog {

    FrontDesk fd;
    Appointment ap;
    JPanel p;
    /**
     * Creates new form ChannelTime
     */
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat stf = new SimpleDateFormat("hh:mm a");
    SimpleDateFormat stf2 = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dayf = new SimpleDateFormat("EEEE");

    public ChannelTime(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadAvailbleTime();
        
        ImageIcon i = new ImageIcon("src/resourse/image/3.png");
        Image x = i.getImage();
        setIconImage(x);

    }

    public ChannelTime(FrontDesk parent, Appointment ap, boolean modal) {
        super(parent, modal);
        this.fd = parent;
        this.ap = ap;
        initComponents();
        loadAvailbleTime();
        
        ImageIcon i = new ImageIcon("src/resourse/image/3.png");
        Image x = i.getImage();
        setIconImage(x);

    }

 

    private void loadAvailbleTime() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 4; i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        }
        try {

            String doc;
           
                doc = ap.jDoctor.getSelectedItem().toString();

          
            String[] docId = doc.split("-");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            LocalDate currentDate = LocalDate.now();
            dtm.setRowCount(0);

            ResultSet rs = MySQL.search("SELECT * FROM `channel_time` INNER JOIN `day` ON `channel_time`.`day_id`=`day`.`id` WHERE `doctor_id`='" + docId[0] + "'");
            Vector dayVec = new Vector();
            HashMap startTmap = new HashMap();
            while (rs.next()) {
                dayVec.add(rs.getString("day.name"));
                startTmap.put(rs.getString("day.name") + "S", rs.getString("start_time"));
                startTmap.put(rs.getString("day.name") + "E", rs.getString("end_time"));

            }

            //for time
          
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm");

            //for time
            for (int i = 0; i < 28; i++) {
                //date adding 
                String date = currentDate.plusDays(i).toString();
                String dayName = dayf.format(sdf.parse(date));

                if (dayVec.contains(dayName)) {
                    String chanellEnd = date + " " + startTmap.get(dayName + "E").toString();

                    String chanellStart = date + " " + startTmap.get(dayName + "S").toString();

                    Date stime = df2.parse(chanellStart);
                    Calendar cal = Calendar.getInstance();

                    Calendar cal2 = Calendar.getInstance();
                    cal.setTime(stime);
                    cal2.setTime(stime);
                    int x = 0;
                    Date endTime = df2.parse(chanellStart);
              
                    while (endTime.before(df2.parse(chanellEnd))) {
                       

                        //addition time 15 min
                        cal.add(Calendar.MINUTE, x);
                        cal2.add(Calendar.MINUTE, 15);
                        //addition time 15 min
                        Date st = cal.getTime();
                        Date et = cal2.getTime();

          
                        endTime = et;

                        Vector v = new Vector();

                        if (et.after(new Date())) {
                            v.add(date);
                            v.add(stf.format(st));
                            v.add(stf.format(et));
                            ResultSet rs1 = MySQL.search("SELECT * FROM `appointment` WHERE (`doctor_id`='" + docId[0] + "' AND `appoint_date`='" + date + "') AND (`start_time`='" + stf2.format(st) + "' OR `end_time`='" + stf2.format(et) + "')");
                            if (rs1.next()) {
                                v.add("Reserved");

                            } else {
                                v.add("Available");

                            }
                            dtm.addRow(v);
                        }

                        x = 15;

                    }

                }

            }


        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Channeling Time");
        setResizable(false);

        jTable1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Date", "Start Time", "End Time", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel1.setText("Date");

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel2.setText("Date");

        jComboBox1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Available", "Reserved" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2) {
            int r = jTable1.getSelectedRow();
            if (jTable1.getValueAt(r, 3).equals("Available")) {
            
                    this.ap.japDate.setText(jTable1.getValueAt(r, 0).toString());
                    this.ap.japStartTime.setText(jTable1.getValueAt(r, 1).toString());
                    this.ap.japEndTime.setText(jTable1.getValueAt(r, 2).toString());
                    this.dispose();
             
            } else {
                JOptionPane.showMessageDialog(this, "Already have appointment at this selected time", "Warning", JOptionPane.WARNING_MESSAGE);

            }

        }
    }//GEN-LAST:event_jTable1MouseClicked

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
            java.util.logging.Logger.getLogger(ChannelTime.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChannelTime.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChannelTime.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChannelTime.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ChannelTime dialog = new ChannelTime(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
