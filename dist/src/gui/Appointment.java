/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.MySQL;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author ROG STRIX
 */
public class Appointment extends javax.swing.JPanel {

    /**
     * Creates new form Appointment
     */
    FrontDesk fd;
    SimpleDateFormat stf = new SimpleDateFormat("hh:mm a");
    SimpleDateFormat stf2 = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat minite = new SimpleDateFormat("mm");
    SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat df = new DecimalFormat("0.00");

    Icon successIcon = new ImageIcon("src/resourse/icon/success.png");

    Color b = new Color(84, 110, 122);

    public Appointment() {
        initComponents();
        jTable1.getTableHeader().setFont(new Font("Airal Nova", Font.PLAIN, 14));
        loadSpecialty();
        loadTitle();
        loadDoctor();
        loadDoctor2();
        loadPaymentMethod();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 10; i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        }
        loader();
    }

    public Appointment(FrontDesk fd) {
        initComponents();
        jTable1.getTableHeader().setFont(new Font("Airal Nova", Font.PLAIN, 14));
        loadSpecialty();
        loadTitle();
        loadDoctor();
        loadDoctor2();
        loadPaymentMethod();
        this.fd = fd;
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 10; i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        }
        loader();
    }

    private void loader() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    int i = Integer.valueOf(minite.format(new Date()));
                    if (i % 15 == 0) {
                        loadAppoitment();
                    }
                    try {
                        Thread.sleep(50000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t.start();
    }

    public void loadAppoitment() {
        try {
            String q = "SELECT * FROM `appointment` INNER JOIN `doctor` ON `appointment`.`doctor_id`=`doctor`.`id` INNER JOIN `speciality` ON `doctor`.`speciality_id`=`speciality`.`id` INNER JOIN `reference_no` ON `appointment`.`reference_no_id`=`reference_no`.`id` INNER JOIN `patient` ON `reference_no`.`patient_id`=`patient`.`id` "
                    + "WHERE `appointment`.`appoint_date`>='" + datef.format(new Date()) + "'";
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
                v.add(stf.format(stf2.parse(rs.getString("start_time"))) + " - " + stf.format(stf2.parse(rs.getString("end_time"))));
                v.add(df.format(df.parse(rs.getString("fee"))));
                String today = datef.format(new Date());
                String appointdate = rs.getString("appoint_date");
                String nowTime = stf2.format(new Date());
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");
                if (stf2.parse(nowTime).before(stf2.parse(startTime)) || datef.parse(today).before(datef.parse(appointdate))) {
                    v.add("Pendding");
                } else if (stf2.parse(nowTime).after(stf2.parse(startTime)) && stf2.parse(nowTime).before(stf2.parse(endTime))) {
                    v.add("Active");

                } else {
                    v.add("End");

                }
                if (today.equals(rs.getString("appoint_date"))) {
                    if (stf2.parse(nowTime).before(stf2.parse(endTime))) {
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

    public void resetField() {
        jLabel16.setText(System.currentTimeMillis() + "-" + LogIn.UserId);

        jtitle.setSelectedIndex(0);
        jName.setText("");
        jDoctor.setSelectedIndex(0);
        jPhone.setText("");
        japDate.setText("0000-00-00");
        japStartTime.setText("00:00");
        japEndTime.setText("00:00");
        jfee.setText("0.00");
        jTextField10.setText("");
        jBalance.setText("0.00");
        jBalance.setForeground(b);
        jComboBox16.setSelectedIndex(0);
        jSpecilty.setSelectedIndex(0);
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

            jSearchSpecailty.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPaymentMethod() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `payment_method` WHERE `id`<>3");

            Vector v = new Vector();
            v.add("Select");

            while (rs.next()) {
                v.add(rs.getString("name"));
            }

            jComboBox16.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTitle() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `title`");

            Vector v = new Vector();
            v.add("Select");

            while (rs.next()) {
                v.add(rs.getString("name"));
            }

            jtitle.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDoctor() {
        try {
            String q = "SELECT * FROM `doctor` INNER JOIN `speciality` ON `doctor`.`speciality_id`=`speciality`.`id`";

            ResultSet rs = MySQL.search(q);
            Vector v = new Vector();
            v.add("Select");

            while (rs.next()) {
                v.add(rs.getString("id") + "-" + rs.getString("name"));
            }
            jDoctor.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDoctor(JComboBox getelement, JComboBox setelement) {
        try {
            String specialty = getelement.getSelectedItem().toString();

            String q = "SELECT * FROM `doctor` INNER JOIN `speciality` ON `doctor`.`speciality_id`=`speciality`.`id`";
            if (!specialty.equals("Select")) {
                q += "WHERE `speciality`.`name`='" + specialty + "'";
            }
            ResultSet rs = MySQL.search(q);
            Vector v = new Vector();
            v.add("Select");

            while (rs.next()) {
                v.add(rs.getString("id") + "-" + rs.getString("name"));
            }
            setelement.setModel(new DefaultComboBoxModel(v));

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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPhone = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jtitle = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jDoctor = new javax.swing.JComboBox<>();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        japDate = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        japStartTime = new javax.swing.JLabel();
        japEndTime = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jSpecilty = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jLabel74 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jBalance = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jTextField10 = new javax.swing.JTextField();
        jfee = new javax.swing.JLabel();
        jComboBox16 = new javax.swing.JComboBox<>();
        jLabel54 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jSearch1 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jSearchSpecailty = new javax.swing.JComboBox<>();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel28 = new javax.swing.JLabel();
        jSearchDoctor = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        jMenuItem1.setText("Update");
        jPopupMenu1.add(jMenuItem1);

        setPreferredSize(new java.awt.Dimension(1083, 620));

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Patient Info"));

        jPhone.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jPhoneKeyTyped(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel2.setText("Phone No");

        jName.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel1.setText("Name");

        jtitle.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel3.setText("Title");

        jLabel6.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel6.setText("Reference No");

        jLabel16.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel16.setText("####");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jName))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPhone))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jtitle, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 171, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtitle, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPhone)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Appointment Info"));
        jPanel9.setPreferredSize(new java.awt.Dimension(425, 350));

        jLabel43.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel43.setText("Specialty");

        jLabel44.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel44.setText("Doctor");

        jDoctor.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jDoctor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jDoctorItemStateChanged(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel45.setText("Date");

        jLabel46.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel46.setText("Time");

        japDate.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        japDate.setText("0000-00-00");

        jLabel48.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel48.setText("Start");

        jLabel49.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel49.setText("End");

        japStartTime.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        japStartTime.setText("00:00");

        japEndTime.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        japEndTime.setText("00:00");

        jButton5.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(51, 51, 51));
        jButton5.setText("Change Date & Time");
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jSpecilty.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jSpecilty.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jSpeciltyItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jDoctor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jSpecilty, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(japDate, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(japStartTime, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(japEndTime, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(jSpecilty, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addGap(13, 13, 13)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(japDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel48)
                        .addComponent(japStartTime)
                        .addComponent(jLabel49)
                        .addComponent(japEndTime)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Payment Info"));

        jLabel74.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel74.setText("Balance");

        jLabel77.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel77.setText("Fee");

        jBalance.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jBalance.setText("0.00");

        jLabel73.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel73.setText("Payment");

        jButton11.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(51, 51, 51));
        jButton11.setText("Print Receipt");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jTextField10.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField10KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField10KeyTyped(evt);
            }
        });

        jfee.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jfee.setText("0.00");

        jComboBox16.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jLabel54.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel54.setText("Payment Method");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jComboBox16, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel54)
                            .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jfee, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jfee)
                    .addComponent(jLabel77))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(jComboBox16, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel73))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel74)
                    .addComponent(jBalance))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("New Appointment", jPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(264, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));

        jLabel24.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel24.setText("Search");

        jSearch1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jSearch1.setToolTipText("Search by Appointment ID, Patient Name, Patient Phone No");
        jSearch1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jSearch1KeyReleased(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel27.setText("Date");

        jLabel26.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel26.setText("Specialty");

        jSearchSpecailty.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jSearchSpecailty.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jSearchSpecailtyItemStateChanged(evt);
            }
        });

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
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

        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
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
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2)
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24)
                        .addComponent(jSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSearchSpecailty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel26)
                        .addComponent(jLabel27)
                        .addComponent(jSearchDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel28))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel4.setPreferredSize(new java.awt.Dimension(464, 430));

        jTable1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Appointment ID", "Reference No", "Patient Name", "Patient Phone No", "Specialty", "Docter", "Date", "Time", "Fee", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
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
        jButton1.setForeground(new java.awt.Color(0, 204, 204));
        jButton1.setText("Appointment History");
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setFocusCycleRoot(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jSpeciltyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jSpeciltyItemStateChanged
        loadDoctor(jSpecilty, jDoctor);
    }//GEN-LAST:event_jSpeciltyItemStateChanged

    private void jTextField10KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyTyped
        String price = jTextField10.getText();

        if (!Pattern.compile("0|0[.]|0[.][0-9]{0,1}|0[.][0-9]{0,1}[1-9]{0,1}|[1-9]+|[1-9]+[0-9]+|[1-9]+[.]|[1-9]+[0-9]*[.][0-9]{0,2}").matcher(price + evt.getKeyChar()).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField10KeyTyped

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        String title = jtitle.getSelectedItem().toString();
        String patient = jName.getText();
        String doctor = jDoctor.getSelectedItem().toString();
        String phone_no = jPhone.getText();
        String appointDate = japDate.getText();
        String appointstart = japStartTime.getText();
        String appointend = japEndTime.getText();
        String fee = jfee.getText();
        String payment = jTextField10.getText();
        String balance = jBalance.getText();
        String method = jComboBox16.getSelectedItem().toString();

        if (title.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select title", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (patient.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter patient name", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (patient.length() > 100) {
            JOptionPane.showMessageDialog(this, "Patient name character length must be less than 100", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (phone_no.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter patient phone number", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (!Pattern.compile("07[1,2,4,5,6,7,8][0-9]{7}").matcher(phone_no).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid phone number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (doctor.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select doctor", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (appointDate.equals("0000-00-00")) {
            JOptionPane.showMessageDialog(this, "Please select appointment date & time", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (method.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select payment method", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (payment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Payment amount", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (Double.valueOf(balance) < 0) {
            JOptionPane.showMessageDialog(this, "Payment amount must be more than fee", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            String[] docId = doctor.split("-");
            try {
                ResultSet rs = MySQL.search("SELECT * FROM `title`  WHERE `name`='" + title + "' ");
                rs.next();
                String pid;
                String titleId = rs.getString("id");
                ResultSet rs1 = MySQL.search("SELECT * FROM `patient`  WHERE `name`='" + patient + "' AND `phone_no`='" + phone_no + "' AND `title_id`='" + titleId + "'");
                if (rs1.next()) {
                    pid = rs1.getString("id");
                } else {
                    MySQL.iud("INSERT INTO `patient` (`name`,`phone_no`,`title_id`) VALUES ('" + patient + "','" + phone_no + "','" + titleId + "')");
                    ResultSet rs2 = MySQL.search("SELECT * FROM `patient`  WHERE `name`='" + patient + "' AND `phone_no`='" + phone_no + "' AND `title_id`='" + titleId + "'");
                    rs2.next();
                    pid = rs2.getString("id");

                }
                String uniqeId = jLabel16.getText();
                MySQL.iud("INSERT INTO `reference_no` (`num`,`patient_id`) VALUES ('" + uniqeId + "','" + pid + "')");
                ResultSet rs3 = MySQL.search("SELECT * FROM `reference_no`  WHERE `num`='" + uniqeId + "' ");
                rs3.next();
                String referenceId = rs3.getString("id");

                ResultSet rs4 = MySQL.search("SELECT * FROM `payment_method`  WHERE `name`='" + method + "' ");
                rs4.next();
                String pmethodId = rs4.getString("id");
                MySQL.iud("INSERT INTO `appointment` (`appoint_date`,`start_time`,`end_time`,`doctor_id`,`reference_no_id`,`fee`,`balance`,`payment_method_id`,`applydate`)"
                        + " VALUES ('" + appointDate + "','" + stf2.format(stf.parse(appointstart)) + "','" + stf2.format(stf.parse(appointend)) + "','" + docId[0] + "','" + referenceId + "','" + fee + "','" + balance + "','" + pmethodId + "','" + datef.format(new Date()) + "')");
                resetField();
                loadAppoitment();
                JOptionPane.showMessageDialog(this, "Appointment added success", "Success", JOptionPane.INFORMATION_MESSAGE, successIcon);

                ResultSet rs5 = MySQL.search("SELECT * FROM `doctor` INNER JOIN `speciality` ON `doctor`.`speciality_id`=`speciality`.`id`  WHERE `doctor`.`id`='" + docId[0] + "' ");
                rs5.next();

                String pathStream = "src/reports/Appointment.jasper";

                HashMap parameters = new HashMap();
                parameters.put("reference", uniqeId);
                parameters.put("barcode", uniqeId);
                parameters.put("todate", datef.format(new Date()));
                parameters.put("specialty", rs5.getString("speciality.name"));
                parameters.put("doctor", rs5.getString("doctor.name"));
                parameters.put("pname", patient);
                parameters.put("pphone", phone_no);
                parameters.put("apdate", appointDate);
                parameters.put("aptime", appointstart + " to " + appointend);
                parameters.put("fee", fee);
                parameters.put("payment", df.format(Double.valueOf(payment)));
                parameters.put("balance", balance);
                parameters.put("paymethod", method + " Payment");

                JREmptyDataSource dataSource = new JREmptyDataSource();
                JasperPrint jp = JasperFillManager.fillReport(pathStream, parameters, dataSource);
                JasperViewer.viewReport(jp, false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_jButton11ActionPerformed

    private void jDoctorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jDoctorItemStateChanged

        if (evt.getStateChange() == 1) {
            japDate.setText("0000-00-00");
            japStartTime.setText("00:00");
            japEndTime.setText("00:00");
            if (!jDoctor.getSelectedItem().toString().equalsIgnoreCase("Select")) {

                ChannelTime ct = new ChannelTime(fd, this, true);
                ct.setVisible(true);
                jButton5.setEnabled(true);
                String doc = jDoctor.getSelectedItem().toString();
                String[] docId = doc.split("-");
                try {
                    ResultSet rs = MySQL.search("SELECT * FROM `doctor`  WHERE `id`='" + docId[0] + "'");
                    rs.next();
                    jfee.setText(df.format(df.parse(rs.getString("channel_rate"))));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                jButton5.setEnabled(false);
            }
        }

    }//GEN-LAST:event_jDoctorItemStateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        ChannelTime ct = new ChannelTime(fd, this, true);
        ct.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyReleased

        String fee = jfee.getText();
        String payment = jTextField10.getText();

        if (!Pattern.compile("0|0[.]|0[.][0-9]{0,1}|0[.][0-9]{0,1}[1-9]{0,1}|[1-9]+|[1-9]+[0-9]+|[1-9]+[.]|[1-9]+[0-9]*[.][0-9]{0,2}").matcher(payment).matches()) {
            jTextField10.setText("");
            jBalance.setText("0.00");
            jBalance.setForeground(b);

        } else if (!payment.isEmpty()) {

            Double balance = Double.valueOf(payment) - Double.valueOf(fee);

            if (balance < 0) {
                jBalance.setForeground(Color.RED);

            } else {
                jBalance.setForeground(Color.GREEN);

            }
            jBalance.setText(df.format(balance));

        } else {
            jBalance.setText("0.00");
            jBalance.setForeground(b);

        }


    }//GEN-LAST:event_jTextField10KeyReleased

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
        loadAppoitment();
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        jLabel16.setText(System.currentTimeMillis() + "-" + LogIn.UserId);

    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        PreviousAppointment pa = new PreviousAppointment(fd, true);
        pa.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jDateChooser1.setDate(null);

        loadAppoitment();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jBalance;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox16;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    public javax.swing.JComboBox<String> jDoctor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JTextField jName;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField jPhone;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JTextField jSearch1;
    private javax.swing.JComboBox<String> jSearchDoctor;
    private javax.swing.JComboBox<String> jSearchSpecailty;
    private javax.swing.JComboBox<String> jSpecilty;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField10;
    public javax.swing.JLabel japDate;
    public javax.swing.JLabel japEndTime;
    public javax.swing.JLabel japStartTime;
    private javax.swing.JLabel jfee;
    private javax.swing.JComboBox<String> jtitle;
    // End of variables declaration//GEN-END:variables
}
