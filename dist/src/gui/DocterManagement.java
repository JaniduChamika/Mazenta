/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import java.awt.Font;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.DocReport;
import model.GrnReport;
import model.MySQL;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author ROG STRIX
 */
public class DocterManagement extends javax.swing.JPanel {

    /**
     * Creates new form DocterManagement
     */
    DecimalFormat df = new DecimalFormat("0.00");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat df12 = new SimpleDateFormat("hh:mm:ss a");
    DateFormat df24 = new SimpleDateFormat("HH:mm:ss");
    Icon successIcon = new ImageIcon("src/resourse/icon/success.png");

    public DocterManagement() {
        initComponents();
        jTable1.getTableHeader().setFont(new Font("Airal Nova", Font.PLAIN, 14));

        loadGender();
        loadSpecialty();
        loadDays();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 3; i++) {
            jTable2.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        }
        channelTableListner();
        loadDoctor();
        doctorListner();
    }

    private void doctorListner() {
        ListSelectionListener lsl = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int r = jTable1.getSelectedRow();
                if (r != -1) {

                    try {
                        String docId = jTable1.getValueAt(r, 0).toString();
                        jIDLeb.setText(docId);
                        jName.setText(jTable1.getValueAt(r, 1).toString());
                        jComboBox2.setSelectedItem(jTable1.getValueAt(r, 4).toString());
                        jSpecilty.setSelectedItem(jTable1.getValueAt(r, 6).toString());
                        jDateChooser1.setDate(sdf.parse(jTable1.getValueAt(r, 5).toString()));
                        jPhone.setText(jTable1.getValueAt(r, 2).toString());
                        if (jTable1.getValueAt(r, 3).toString().equals("-")) {
                            jEmail.setText("");

                        } else {
                            jEmail.setText(jTable1.getValueAt(r, 3).toString());

                        }
                        jChannelRate.setText(jTable1.getValueAt(r, 8).toString());

                        jUpdateBtn.setEnabled(true);
                        jAddBtn.setEnabled(false);

                        ResultSet rs = MySQL.search("SELECT * FROM `channel_time` INNER JOIN `day` ON `channel_time`.`day_id`=`day`.`id` WHERE `doctor_id`='" + docId + "'");
                        DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
                        dtm.setRowCount(0);
                        while (rs.next()) {
                            Vector v = new Vector();
                            v.add(rs.getString("day.name"));
                            v.add(df12.format(df24.parse(rs.getString("start_time"))));
                            v.add(df12.format(df24.parse(rs.getString("end_time"))));
                            dtm.addRow(v);
                        }
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            }
        };
        jTable1.getSelectionModel().addListSelectionListener(lsl);
    }

    private void channelTableListner() {
        ListSelectionListener lsl = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int r = jTable2.getSelectedRow();
                if (r != -1) {
                    jButton2.setEnabled(true);
                } else {
                    jButton2.setEnabled(false);

                }
            }
        };
        jTable2.getSelectionModel().addListSelectionListener(lsl);
    }

    private void loadGender() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `gender`");

            Vector v = new Vector();
            v.add("Select");

            while (rs.next()) {
                v.add(rs.getString("name"));
            }

            jComboBox1.setModel(new DefaultComboBoxModel(v));
            jComboBox2.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDays() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `day`");

            Vector v = new Vector();
            v.add("Select");

            while (rs.next()) {
                v.add(rs.getString("name"));
            }

            jDays.setModel(new DefaultComboBoxModel(v));

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

            jSpecilty.setModel(new DefaultComboBoxModel(v));
            jSearchSpecilty.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDoctor() {
        try {
            String q = "SELECT * FROM `doctor` INNER JOIN `gender` ON `doctor`.`gender_id`=`gender`.`id` INNER JOIN `speciality` ON `doctor`.`speciality_id`=`speciality`.`id`";
            String txt = jSearch.getText();
            String type = jSearchSpecilty.getSelectedItem().toString();
            String gender = jComboBox1.getSelectedItem().toString();
            Vector query = new Vector();
            if (!txt.isEmpty() || !type.equals("Select") || !gender.equals("Select")) {
                q += "WHERE ";

            }
            if (!txt.isEmpty()) {
                query.add("(`doctor`.`id` = '" + txt + "' OR `doctor`.`name` LIKE '" + txt + "%' OR `doctor`.`phone_no` LIKE '" + txt + "%' OR `doctor`.`email` LIKE '" + txt + "%')");

            }
            if (!type.equals("Select")) {

                query.add("`speciality`.`name`='" + type + "'");
            }
            if (!gender.equals("Select")) {

                query.add("`gender`.`name`='" + gender + "'");
            }
            for (int i = 0; i < query.size(); i++) {
                q += query.get(i);
                if (i < query.size() - 1) {
                    q += " AND ";
                }
            }
            ResultSet rs = MySQL.search(q + " ORDER BY `doctor`.`id` ASC");

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("doctor.id"));
                v.add(rs.getString("doctor.name"));
                v.add(rs.getString("phone_no"));
                if (rs.getString("email").isEmpty()) {
                    v.add("-");

                } else {
                    v.add(rs.getString("email"));

                }
                v.add(rs.getString("gender.name"));
                v.add(rs.getString("dob"));
                v.add(rs.getString("speciality.name"));
                v.add(rs.getString("reg_date"));
                v.add(df.format(df.parse(rs.getString("channel_rate"))));

                dtm.addRow(v);
            }
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < 9; i++) {
                jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetChannel() {
        jDays.setSelectedIndex(0);
        jStartH.setValue(0);
        jStartM.setValue(0);
        jStartType.setSelectedIndex(0);
        jEndH.setValue(0);
        jEndM.setValue(0);
        jEndType.setSelectedIndex(0);
        jButton2.setEnabled(false);
    }

    public void resetFields() {
        jName.setText("");
        jComboBox2.setSelectedIndex(0);
        jDateChooser1.setDate(null);
        jSpecilty.setSelectedIndex(0);
        jPhone.setText("");
        jEmail.setText("");
        jChannelRate.setText("");
        DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
        dtm.setRowCount(0);
        jUpdateBtn.setEnabled(false);
        jAddBtn.setEnabled(true);
        jIDLeb.setText("#");
        jTable1.clearSelection();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jIDLeb = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jName = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jSpecilty = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPhone = new javax.swing.JTextField();
        jEmail = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jChannelRate = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jDays = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jStartH = new javax.swing.JSpinner();
        jLabel24 = new javax.swing.JLabel();
        jStartM = new javax.swing.JSpinner();
        jLabel25 = new javax.swing.JLabel();
        jStartType = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        jEndH = new javax.swing.JSpinner();
        jEndM = new javax.swing.JSpinner();
        jEndType = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jAddBtn = new javax.swing.JButton();
        jUpdateBtn = new javax.swing.JButton();
        jClearBtn = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jSearch = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jSearchSpecilty = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();

        jPanel2.setPreferredSize(new java.awt.Dimension(464, 430));

        jTable1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Phone No", "Email", "Gender", "DOB", "Specialty", "Reg. Date", "Channeling rate"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setMinimumSize(new java.awt.Dimension(120, 0));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Contact Details"));

        jLabel11.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel11.setText("Doc ID");

        jIDLeb.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jIDLeb.setText("#");

        jLabel13.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel13.setText("Name");

        jLabel16.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel16.setText("Gender");

        jLabel17.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel17.setText("DOB");

        jName.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jDateChooser1.setDateFormatString("yyyy-MM-dd");

        jSpecilty.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel12.setText("Specialty");

        jComboBox2.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jButton3.setFont(new java.awt.Font("Arial Nova", 1, 14)); // NOI18N
        jButton3.setText("+");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jSpecilty, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jName)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jIDLeb, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 202, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jIDLeb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSpecilty)
                        .addComponent(jButton3))
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(115, 115, 115))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Contact Details"));

        jLabel19.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel19.setText("Phone No");

        jLabel20.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel20.setText("Email Address");

        jPhone.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jPhoneKeyTyped(evt);
            }
        });

        jEmail.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPhone, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                    .addComponent(jEmail))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Channeling Info"));

        jChannelRate.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jChannelRate.setToolTipText("Channel rate for 15 min");
        jChannelRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jChannelRateKeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel14.setText("Channeling Rate");

        jLabel15.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel15.setText("Channeling Days");

        jDays.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel18.setText("Start Time");

        jStartH.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jStartH.setModel(new javax.swing.SpinnerNumberModel(0, 0, 12, 1));

        jLabel24.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel24.setText("Hour");

        jStartM.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jStartM.setModel(new javax.swing.SpinnerNumberModel(0, 0, 55, 5));

        jLabel25.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel25.setText("Min");

        jStartType.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jStartType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AM", "PM" }));

        jLabel26.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel26.setText("End Time");

        jEndH.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jEndH.setModel(new javax.swing.SpinnerNumberModel(0, 0, 12, 1));

        jEndM.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jEndM.setModel(new javax.swing.SpinnerNumberModel(0, 0, 55, 5));

        jEndType.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jEndType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AM", "PM" }));

        jLabel27.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel27.setText("Min");

        jLabel28.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel28.setText("Hour");

        jButton1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 51, 51));
        jButton1.setText("Add to table");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable2.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Day", "Start Time", "End time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jButton2.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(51, 51, 51));
        jButton2.setText("Remove");
        jButton2.setEnabled(false);
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
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(jChannelRate, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addComponent(jDays, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(18, 18, 18)
                                .addComponent(jStartH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jStartM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel25))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jEndH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jEndM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel27)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jEndType, 0, 1, Short.MAX_VALUE)
                            .addComponent(jStartType, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jChannelRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jDays, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jStartH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24)
                            .addComponent(jStartM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25)
                            .addComponent(jStartType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(jEndH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28)
                            .addComponent(jEndM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27)
                            .addComponent(jEndType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jAddBtn.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jAddBtn.setForeground(new java.awt.Color(0, 0, 0));
        jAddBtn.setText("Add");
        jAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAddBtnActionPerformed(evt);
            }
        });

        jUpdateBtn.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jUpdateBtn.setForeground(new java.awt.Color(0, 0, 0));
        jUpdateBtn.setText("Update");
        jUpdateBtn.setEnabled(false);
        jUpdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jUpdateBtnActionPerformed(evt);
            }
        });

        jClearBtn.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jClearBtn.setForeground(new java.awt.Color(0, 0, 0));
        jClearBtn.setText("Clear");
        jClearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jClearBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jClearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jAddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jAddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jClearBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Search"));

        jLabel21.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel21.setText("Search");

        jSearch.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jSearch.setToolTipText("Search by Doc ID, Name, Phone No");
        jSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jSearchKeyReleased(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel22.setText("Spacialty");

        jSearchSpecilty.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jSearchSpecilty.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jSearchSpeciltyItemStateChanged(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel23.setText("Gender");

        jComboBox1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(51, 51, 51));
        jButton4.setText("Print Records");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSearchSpecilty, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel23)
                .addGap(27, 27, 27)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSearchSpecilty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel22)
                        .addComponent(jButton4))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(jSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1081, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jPhoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPhoneKeyTyped
        String t = String.valueOf(evt.getKeyChar());
        String old = jPhone.getText();
        String newtext = old + "" + t;
        if (newtext.length() == 1) {
            if (!Pattern.compile("0").matcher(newtext).matches()) {
                evt.consume();

            }

        } else if (newtext.length() == 2) {
            if (!Pattern.compile("07").matcher(newtext).matches()) {
                evt.consume();

            }

        } else if (newtext.length() == 3) {
            if (!Pattern.compile("07[1,2,4,5,6,7,8]").matcher(newtext).matches()) {
                evt.consume();

            }

        } else if (newtext.length() <= 10) {
            if (!Pattern.compile("07[1,2,4,5,6,7,8][0-9]+").matcher(newtext).matches()) {
                evt.consume();

            }

        } else {
            evt.consume();
        }
    }//GEN-LAST:event_jPhoneKeyTyped

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Specialty s = new Specialty(this, true);
        s.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAddBtnActionPerformed
        String name = jName.getText();
        String gender = jComboBox2.getSelectedItem().toString();
        Date dob = jDateChooser1.getDate();
        String specialty = jSpecilty.getSelectedItem().toString();
        String phone_no = jPhone.getText();
        String email = jEmail.getText();
        String rate = jChannelRate.getText();
        int r = jTable2.getRowCount();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter doctor name", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (gender.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select doctor gender", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (dob == null) {
            JOptionPane.showMessageDialog(this, "Please select doctor date of birth", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (specialty.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select specialty", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (phone_no.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter doctor phone number", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (!Pattern.compile("07[1,2,4,5,6,7,8][0-9]{7}").matcher(phone_no).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid phone number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (rate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter channeling rate", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (!Pattern.compile("[1-9]+[0-9]*|[1-9]+[0-9]*[.][0-9]{0,2}|0[.][0-9][1-9]").matcher(rate).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid rate format", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (r == 0) {
            JOptionPane.showMessageDialog(this, "Please add channeling days and time", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {
            try {
                ResultSet rs = MySQL.search("SELECT * FROM `speciality` WHERE `name`='" + specialty + "'");
                rs.next();
                String specialty_id = rs.getString("id");
                ResultSet rs1 = MySQL.search("SELECT * FROM `gender` WHERE `name`='" + gender + "'");
                rs1.next();
                String genderId = rs1.getString("id");
                String regDate = sdf.format(new Date());
                String unqeId = System.currentTimeMillis() + "-" + LogIn.UserId;
                ResultSet rs4 = MySQL.search("SELECT * FROM `doctor` WHERE `name`='" + name + "' AND `phone_no`='" + phone_no + "' AND `dob`='" + sdf.format(dob) + "'");
                if (rs4.next()) {
                    JOptionPane.showMessageDialog(this, name + " is already exist", "Warning", JOptionPane.WARNING_MESSAGE);

                } else {
                    MySQL.iud("INSERT INTO `doctor` (`name`,`speciality_id`,`channel_rate`,`gender_id`,`phone_no`,`email`,`dob`,`reg_date`,`uniqe_id`) "
                            + "VALUES ('" + name + "','" + specialty_id + "','" + rate + "','" + genderId + "','" + phone_no + "','" + email + "','" + sdf.format(dob) + "','" + regDate + "','" + unqeId + "')"
                    );
                    ResultSet rs2 = MySQL.search("SELECT * FROM `doctor` WHERE `uniqe_id`='" + unqeId + "'");
                    rs2.next();
                    String docId = rs2.getString("id");

                    for (int i = 0; i < r; i++) {
                        ResultSet rs3 = MySQL.search("SELECT * FROM `day` WHERE `name`='" + jTable2.getValueAt(i, 0).toString() + "'");
                        rs3.next();
                        String dayid = rs3.getString("id");

                        String starttime = df24.format(df12.parse(jTable2.getValueAt(i, 1).toString()));

                        String endTime = df24.format(df12.parse(jTable2.getValueAt(i, 2).toString()));

                        MySQL.iud("INSERT INTO `channel_time` (`doctor_id`,`day_id`,`end_time`,`start_time`) "
                                + "VALUES ('" + docId + "','" + dayid + "','" + endTime + "','" + starttime + "')");
                    }
                    loadDoctor();
                    resetChannel();
                    resetFields();
                    JOptionPane.showMessageDialog(this, "Doctor registation success", "Success", JOptionPane.INFORMATION_MESSAGE, successIcon);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jAddBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String days = jDays.getSelectedItem().toString();
        String startH = jStartH.getValue().toString();
        String startM = jStartM.getValue().toString();
        String startType = jStartType.getSelectedItem().toString();
        String endH = jEndH.getValue().toString();
        String endM = jEndM.getValue().toString();
        String endType = jEndType.getSelectedItem().toString();
        if (days.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select channeling day", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (startH.equals("0")) {
            JOptionPane.showMessageDialog(this, "Please enter start time", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (endH.equals("0")) {
            JOptionPane.showMessageDialog(this, "Please enter end time", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {
            String StartTime = startH + ":" + startM + ":" + "00 " + startType;
            String endTime = endH + ":" + endM + ":" + "00 " + endType;
            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
            Vector v = new Vector();
            v.add(days);
            try {
                v.add(df12.format(df12.parse(StartTime)));
                v.add(df12.format(df12.parse(endTime)));

            } catch (Exception e) {
                e.printStackTrace();
            }
            dtm.addRow(v);
            resetChannel();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jChannelRateKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jChannelRateKeyTyped
        String price = jChannelRate.getText();

        if (!Pattern.compile("0|0[.]|0[.][0-9]{0,1}|0[.][0-9]{0,1}[1-9]{0,1}|[1-9]+|[1-9]+[0-9]+|[1-9]+[.]|[1-9]+[0-9]*[.][0-9]{0,2}").matcher(price + evt.getKeyChar()).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jChannelRateKeyTyped

    private void jClearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jClearBtnActionPerformed
        resetChannel();
        resetFields();

    }//GEN-LAST:event_jClearBtnActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
        dtm.removeRow(jTable2.getSelectedRow());
        jButton2.setEnabled(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        loadDoctor();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jSearchSpeciltyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jSearchSpeciltyItemStateChanged
        loadDoctor();
    }//GEN-LAST:event_jSearchSpeciltyItemStateChanged

    private void jSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSearchKeyReleased
        loadDoctor();
    }//GEN-LAST:event_jSearchKeyReleased

    private void jUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jUpdateBtnActionPerformed
        String docId = jIDLeb.getText();
        String name = jName.getText();
        String gender = jComboBox2.getSelectedItem().toString();
        Date dob = jDateChooser1.getDate();
        String specialty = jSpecilty.getSelectedItem().toString();
        String phone_no = jPhone.getText();
        String email = jEmail.getText();
        String rate = jChannelRate.getText();
        int r = jTable2.getRowCount();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter doctor name", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (gender.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select doctor gender", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (dob == null) {
            JOptionPane.showMessageDialog(this, "Please select doctor date of birth", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (specialty.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select specialty", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (phone_no.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter doctor phone number", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (!Pattern.compile("07[1,2,4,5,6,7,8][0-9]{7}").matcher(phone_no).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid phone number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (rate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter channeling rate", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (r == 0) {
            JOptionPane.showMessageDialog(this, "Please add channeling days and time", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {
            try {
                ResultSet rs = MySQL.search("SELECT * FROM `speciality` WHERE `name`='" + specialty + "'");
                rs.next();
                String specialty_id = rs.getString("id");
                ResultSet rs1 = MySQL.search("SELECT * FROM `gender` WHERE `name`='" + gender + "'");
                rs1.next();
                String genderId = rs1.getString("id");
                String regDate = sdf.format(new Date());
                String unqeId = System.currentTimeMillis() + "-" + LogIn.UserId;

                MySQL.iud("UPDATE `doctor` SET `name`='" + name + "',`speciality_id`='" + specialty_id + "',`channel_rate`='" + rate + "',`gender_id`='" + genderId + "',`phone_no`='" + phone_no + "',`email`='" + email + "',`dob`='" + sdf.format(dob) + "' WHERE `id`='" + docId + "' ");
                MySQL.iud("DELETE FROM `channel_time` WHERE `doctor_id`='" + docId + "'");
                for (int i = 0; i < r; i++) {
                    ResultSet rs3 = MySQL.search("SELECT * FROM `day` WHERE `name`='" + jTable2.getValueAt(i, 0).toString() + "'");
                    rs3.next();
                    String dayid = rs3.getString("id");

                    String starttime = df24.format(df12.parse(jTable2.getValueAt(i, 1).toString()));

                    String endTime = df24.format(df12.parse(jTable2.getValueAt(i, 2).toString()));

                    MySQL.iud("INSERT INTO `channel_time` (`doctor_id`,`day_id`,`end_time`,`start_time`) "
                            + "VALUES ('" + docId + "','" + dayid + "','" + endTime + "','" + starttime + "')");
                }
                loadDoctor();
                resetChannel();
                resetFields();
                JOptionPane.showMessageDialog(this, "Update success", "Success", JOptionPane.INFORMATION_MESSAGE,successIcon);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jUpdateBtnActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int r = jTable1.getRowCount();

        try {
            //  InputStream pathStream = DocterManagement.class.getResourceAsStream("../reports/DoctorReport.jasper");

            String pathStream = "src/reports/DoctorReport.jasper";
            // JasperReport jr = JasperCompileManager.compileReport(path);
            HashMap parameters = new HashMap();

            Vector<model.DocReport> v = new Vector();
            for (int i = 0; i < r; i++) {
                String id = jTable1.getValueAt(i, 0).toString();
                String name = jTable1.getValueAt(i, 1).toString();
                String contact = jTable1.getValueAt(i, 2).toString();
                String gender = jTable1.getValueAt(i, 4).toString();
                String dob = jTable1.getValueAt(i, 5).toString();
                String specailty = jTable1.getValueAt(i, 6).toString();
                String regDate = jTable1.getValueAt(i, 7).toString();
                String rate = jTable1.getValueAt(i, 8).toString();
                v.add(new DocReport(id, name, contact, gender, dob, regDate, specailty, rate));
            }
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(v);
            JasperPrint jp = JasperFillManager.fillReport(pathStream, parameters, dataSource);
            JasperViewer.viewReport(jp, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jAddBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JTextField jChannelRate;
    private javax.swing.JButton jClearBtn;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JComboBox<String> jDays;
    private javax.swing.JTextField jEmail;
    private javax.swing.JSpinner jEndH;
    private javax.swing.JSpinner jEndM;
    private javax.swing.JComboBox<String> jEndType;
    private javax.swing.JLabel jIDLeb;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JTextField jName;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField jPhone;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jSearch;
    private javax.swing.JComboBox<String> jSearchSpecilty;
    private javax.swing.JComboBox<String> jSpecilty;
    private javax.swing.JSpinner jStartH;
    private javax.swing.JSpinner jStartM;
    private javax.swing.JComboBox<String> jStartType;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton jUpdateBtn;
    // End of variables declaration//GEN-END:variables
}
