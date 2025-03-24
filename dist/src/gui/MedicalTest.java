/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import model.MySQL;
import model.TestReport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author ROG STRIX
 */
public class MedicalTest extends javax.swing.JPanel {

    /**
     * Creates new form MedicalTest
     */
    SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat df = new DecimalFormat("0.00");
    Color b = new Color(84, 110, 122);
    Icon successIcon = new ImageIcon("src/resourse/icon/success.png");

    public MedicalTest() {
        initComponents();
        jTable1.getTableHeader().setFont(new Font("Airal Nova", Font.PLAIN, 14));

        loadAvailbility();
        loadTest();
        loadTitle();
        loadPaymentMethod();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 3; i++) {
            jTable2.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        }
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 9; i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        }
        loader();
    }

    private void loader() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    loadMedicalTest();

                    try {
                        Thread.sleep(60000 * 5);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t.start();
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
                    + "LEFT JOIN `medical_test_payment` ON `medical_test`.`id`=`medical_test_payment`.`medical_test_id`";
            String txt = jSearch1.getText();
            String test = jtest.getSelectedItem().toString();
            String availblity = jAvailblity.getSelectedItem().toString();

            Vector query = new Vector();
            if (!txt.isEmpty() || !test.equals("Select") || !availblity.equals("Select")) {
                q += "WHERE";
            }
            if (!txt.isEmpty()) {
                query.add("(`reference_no`.`num` LIKE '" + txt + "%' OR `patient`.`name` LIKE '" + txt + "%' OR `patient`.`phone_no` LIKE '" + txt + "%' OR `medical_test`.`id` = '" + txt + "')");

            }

            if (!test.equals("Select")) {

                query.add("`tests`.`name`='" + test + "'");
            }
            if (!availblity.equals("Select")) {

                query.add("`status`.`name`='" + availblity + "'");
            }

            for (int i = 0; i < query.size(); i++) {
                q += query.get(i);
                if (i < query.size() - 1) {
                    q += " AND ";

                }
            }
            ResultSet rs = MySQL.search(q + " ORDER BY `medical_test`.`id` ASC");

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("medical_test_has_tests.id"));
                v.add(rs.getString("reference_no.num"));
                v.add(rs.getString("patient.name"));
                v.add(rs.getString("patient.phone_no"));
                v.add(rs.getString("tests.name"));
                v.add(rs.getString("apply_date"));
                v.add(df.format(Double.valueOf(rs.getString("tests.fee"))));
                if (rs.getString("medical_test_payment.id") == null) {
                    v.add("Not Paid");
                } else {
                    v.add("Paid");
                }
                v.add(rs.getString("status.name"));

                dtm.addRow(v);
            }

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

            jpayMethod.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTest() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `tests`");

            Vector v = new Vector();
            v.add("Select");

            while (rs.next()) {
                v.add(rs.getString("name"));
            }

            jtest.setModel(new DefaultComboBoxModel(v));
            jtest1.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadAvailbility() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `status` WHERE `id` IN (1,2,3)");

            Vector v = new Vector();
            v.add("Select");

            while (rs.next()) {
                v.add(rs.getString("name"));
            }

            jAvailblity.setModel(new DefaultComboBoxModel(v));

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

            jTitle.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetField() {
        jreference.setText("");
        jTitle.setSelectedIndex(0);
        jName.setText("");

        jPhone.setText("");
        jtest1.setSelectedIndex(0);

        DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
        dtm.setRowCount(0);

        jfee.setText("0.00");
        jtotal.setText("0.00");
        jPayment.setText("");
        jBalance.setText("0.00");
        jpayMethod.setSelectedIndex(0);
        jBalance.setForeground(b);
        jName.setEnabled(true);
        jPhone.setEnabled(true);
        jTitle.setEnabled(true);
    }

    public void calTotal() {
        double total = 0;
        for (int i = 0; i < jTable2.getRowCount(); i++) {
            total += Double.valueOf(jTable2.getValueAt(i, 2).toString());
        }
        jtotal.setText(df.format(total));

        String pay = jPayment.getText();
        if (pay.isEmpty()) {
            if (total == 0.0) {
                jBalance.setText("0.00");
                jBalance.setForeground(b);
            } else {
                jBalance.setText(df.format(-1 * total));
                jBalance.setForeground(Color.RED);
            }

        } else {
            double balance = Double.valueOf(pay) - total;
            if (balance < 0) {
                jBalance.setForeground(Color.RED);
            } else {
                jBalance.setForeground(Color.GREEN);
            }
            jBalance.setText(df.format(balance));

        }

    }

//    public static void OpenDocument() {
//        try {
//            File myFile = new File("src/medicalTest/MedicalReport.pdf");
//            Desktop.getDesktop().open(myFile);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
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
        jMenuItem2 = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jSearch1 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jtest = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jAvailblity = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPhone = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jName = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTitle = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jreference = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jfee = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton9 = new javax.swing.JButton();
        jLabel43 = new javax.swing.JLabel();
        jtest1 = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jBalance = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jPrint = new javax.swing.JButton();
        jPayment = new javax.swing.JTextField();
        jtotal = new javax.swing.JLabel();
        jpayMethod = new javax.swing.JComboBox<>();
        jLabel56 = new javax.swing.JLabel();

        jMenuItem1.setText("Issue");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("View");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        setPreferredSize(new java.awt.Dimension(1083, 620));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));

        jLabel24.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel24.setText("Search");

        jSearch1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jSearch1.setToolTipText("Search by Med.Test ID, Patient Name, Patient Phone No");
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

        jLabel28.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel28.setText("Availability");

        jAvailblity.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jAvailblity.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jAvailblityItemStateChanged(evt);
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
                .addComponent(jSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel26)
                .addGap(18, 18, 18)
                .addComponent(jtest, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jAvailblity, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(jAvailblity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jTable1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Med.Test ID", "Reference No", "Patient Name", "Patient Phone No", "Test", "Applied  Date", "Fee", "Payment Status", "Availability"
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
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Patient Info"));

        jPhone.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jPhoneKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel8.setText("Phone No");

        jName.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel9.setText("Name");

        jTitle.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel10.setText("Title");

        jLabel13.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel13.setText("Reference No");

        jreference.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jreference.setToolTipText("Appointment Receit refernce no");
        jreference.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jreferenceKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jName))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPhone))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 138, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(4, 4, 4)
                        .addComponent(jreference)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jreference, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPhone)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Medical Test Info"));
        jPanel9.setPreferredSize(new java.awt.Dimension(425, 350));

        jLabel52.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel52.setText("Fee");

        jfee.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jfee.setText("0.00");

        jTable2.setFont(new java.awt.Font("Arial Nova", 0, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Test", "Fee"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable2);

        jButton9.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(51, 51, 51));
        jButton9.setText("Add");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel43.setText("Test");

        jtest1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jtest1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jtest1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jtest1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jfee, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(jtest1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(jfee))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Payment Info"));

        jLabel83.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel83.setText("Balance");

        jLabel84.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel84.setText("Total");

        jBalance.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jBalance.setText("0.00");

        jLabel86.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel86.setText("Payment");

        jPrint.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jPrint.setForeground(new java.awt.Color(51, 51, 51));
        jPrint.setText("Print Receipt");
        jPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPrintActionPerformed(evt);
            }
        });

        jPayment.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jPayment.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPaymentKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jPaymentKeyTyped(evt);
            }
        });

        jtotal.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jtotal.setText("0.00");

        jpayMethod.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jLabel56.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel56.setText("Payment Method");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel56)
                                    .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jPrint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jpayMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(21, 21, 21))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtotal)
                    .addComponent(jLabel84))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(jpayMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel86))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel83)
                    .addComponent(jBalance))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 374, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 700, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtestItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jtestItemStateChanged
        loadMedicalTest();
    }//GEN-LAST:event_jtestItemStateChanged

    private void jAvailblityItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jAvailblityItemStateChanged
        loadMedicalTest();
    }//GEN-LAST:event_jAvailblityItemStateChanged

    private void jSearch1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSearch1KeyReleased
        loadMedicalTest();

    }//GEN-LAST:event_jSearch1KeyReleased

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

    private void jreferenceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jreferenceKeyReleased
        String reference = jreference.getText();
        if (!reference.isEmpty()) {
            jName.setEnabled(false);
            jPhone.setEnabled(false);
            jTitle.setEnabled(false);
            jName.setText("");
            jPhone.setText("");
            jTitle.setSelectedIndex(0);
            try {
                ResultSet rs = MySQL.search("SELECT * FROM `reference_no`  WHERE `num`='" + reference + "' ");
                if (rs.next()) {
                    String patientId = rs.getString("patient_id");
                    ResultSet rs1 = MySQL.search("SELECT * FROM `patient` INNER JOIN `title` ON `patient`.`title_id`=`title`.`id` WHERE `patient`.`id`='" + patientId + "' ");
                    rs1.next();

                    jName.setText(rs1.getString("patient.name"));
                    jPhone.setText(rs1.getString("phone_no"));
                    jTitle.setSelectedItem(rs1.getString("title.name"));
                } else {
                    jName.setText("");
                    jPhone.setText("");
                    jTitle.setSelectedIndex(0);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            jName.setEnabled(true);
            jPhone.setEnabled(true);
            jTitle.setEnabled(true);
        }
    }//GEN-LAST:event_jreferenceKeyReleased

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        if (evt.getClickCount() == 2) {
            int r = jTable2.getSelectedRow();
            if (r != -1) {
                DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
                dtm.removeRow(r);
                calTotal();
            }
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

        String test = jtest1.getSelectedItem().toString();
        if (test.equalsIgnoreCase("Select")) {
            JOptionPane.showMessageDialog(this, "Please select test", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

            try {
                ResultSet rs = MySQL.search("SELECT * FROM `tests` WHERE `name`='" + test + "'");
                rs.next();
                Vector v = new Vector();
                v.add(rs.getString("id"));
                v.add(rs.getString("name"));
                v.add(rs.getString("fee"));

                boolean isFound = false;
                for (int i = 0; i < jTable2.getRowCount(); i++) {
                    if (jTable2.getValueAt(i, 0).equals(rs.getString("id"))) {
                        isFound = true;
                    }

                }
                if (isFound) {
                    JOptionPane.showMessageDialog(this, "Already exist in the table", "Warning", JOptionPane.WARNING_MESSAGE);

                } else {
                    DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
                    dtm.addRow(v);
                }
                jtest1.setSelectedIndex(0);
                calTotal();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jtest1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jtest1ItemStateChanged
        if (evt.getStateChange() == 1) {

            String test = jtest1.getSelectedItem().toString();
            if (!test.equalsIgnoreCase("Select")) {
                try {
                    ResultSet rs = MySQL.search("SELECT * FROM `tests` WHERE `name`='" + test + "'");
                    rs.next();
                    jfee.setText(df.format(Double.valueOf(rs.getString("fee"))));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                jfee.setText("0.00");

            }
        }
    }//GEN-LAST:event_jtest1ItemStateChanged


    private void jPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPrintActionPerformed
        String reference = jreference.getText();
        String title = jTitle.getSelectedItem().toString();
        String patient = jName.getText();

        String phone_no = jPhone.getText();

        int r = jTable2.getRowCount();
        String total = jtotal.getText();
        String payment = jPayment.getText();
        String balance = jBalance.getText();
        String method = jpayMethod.getSelectedItem().toString();

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
        } else if (r == 0) {
            JOptionPane.showMessageDialog(this, "Please add tests", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (method.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select payment method", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (payment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Payment amount", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (Double.valueOf(balance) < 0) {
            JOptionPane.showMessageDialog(this, "Payment amount must be more than fee", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {

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

                if (reference.isEmpty()) {
                    String uniqeId = System.currentTimeMillis() + "-" + LogIn.UserId;
                    MySQL.iud("INSERT INTO `reference_no` (`num`,`patient_id`) VALUES ('" + uniqeId + "','" + pid + "')");
                    reference = uniqeId;
                }
                ResultSet rs3 = MySQL.search("SELECT * FROM `reference_no`  WHERE `num`='" + reference + "' ");
                rs3.next();
                String referenceId = rs3.getString("id");

                ResultSet rs4 = MySQL.search("SELECT * FROM `payment_method`  WHERE `name`='" + method + "' ");
                rs4.next();
                String pmethodId = rs4.getString("id");

                String uniqeId = System.currentTimeMillis() + "-" + LogIn.UserId;

                MySQL.iud("INSERT INTO `medical_test` (`apply_date`,`reference_no_id`,`uniqe_id`)"
                        + " VALUES ('" + datef.format(new Date()) + "','" + referenceId + "','" + uniqeId + "')");

                ResultSet rs5 = MySQL.search("SELECT * FROM `medical_test`  WHERE `uniqe_id`='" + uniqeId + "' ");
                rs5.next();
                String medtestId = rs5.getString("id");
                for (int i = 0; i < r; i++) {

                    String testId = jTable2.getValueAt(i, 0).toString();

                    MySQL.iud("INSERT INTO `medical_test_has_tests` (`medical_test_id`,`tests_id`)"
                            + " VALUES ('" + medtestId + "','" + testId + "')");
                }

                MySQL.iud("INSERT INTO `medical_test_payment` (`medical_test_id`,`payment_method_id`,`payment`,`balance`)"
                        + " VALUES ('" + medtestId + "','" + pmethodId + "','" + payment + "','" + balance + "')");

                loadMedicalTest();
                JOptionPane.showMessageDialog(this, "Medical Test saved success", "Success", JOptionPane.INFORMATION_MESSAGE, successIcon);

//                InputStream pathStream = MedicalTest.class.getResourceAsStream("../reports/MedicalTest.jasper");
                String pathStream = "src/reports/MedicalTest.jasper";
                //  JasperReport jr = JasperCompileManager.compileReport(path);
                HashMap parameters = new HashMap();
                parameters.put("reference", reference);
                parameters.put("barcode", reference);
                parameters.put("todate", datef.format(new Date()));
                parameters.put("pname", patient);
                parameters.put("pateintid", " ");

                parameters.put("pphone", phone_no);
                parameters.put("total", total);
                parameters.put("payment", df.format(Double.valueOf(payment)));
                parameters.put("balance", balance);
                parameters.put("paymethod", method);
                Vector<model.MedicalTest> v = new Vector();
                for (int i = 0; i < r; i++) {
                    String testId = jTable2.getValueAt(i, 0).toString();
                    String testname = jTable2.getValueAt(i, 1).toString();
                    String testFee = jTable2.getValueAt(i, 2).toString();
                    v.add(new model.MedicalTest(testId, testname, df.format(Double.valueOf(testFee))));
                }
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(v);
                JasperPrint jp = JasperFillManager.fillReport(pathStream, parameters, dataSource);
                JasperViewer.viewReport(jp, false);
                resetField();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jPrintActionPerformed

    private void jPaymentKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPaymentKeyReleased
        String price = jPayment.getText();

        if (!Pattern.compile("0|0[.]|0[.][0-9]{0,1}|0[.][0-9]{0,1}[1-9]{0,1}|[1-9]+|[1-9]+[0-9]+|[1-9]+[.]|[1-9]+[0-9]*[.][0-9]{0,2}").matcher(price).matches()) {
            jPayment.setText("");

        }
        calTotal();
    }//GEN-LAST:event_jPaymentKeyReleased

    private void jPaymentKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPaymentKeyTyped
        String price = jPayment.getText();

        if (!Pattern.compile("0|0[.]|0[.][0-9]{0,1}|0[.][0-9]{0,1}[1-9]{0,1}|[1-9]+|[1-9]+[0-9]+|[1-9]+[.]|[1-9]+[0-9]*[.][0-9]{0,2}").matcher(price + evt.getKeyChar()).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jPaymentKeyTyped

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (jTable1.getSelectedRow() != -1) {
            if (evt.getButton() == 3) {
                String state = jTable1.getValueAt(jTable1.getSelectedRow(), 8).toString();
                if (!state.equalsIgnoreCase("Available")) {

                    jMenuItem1.setEnabled(false);

                }
                if (state.equalsIgnoreCase("Pendding")) {

                    jMenuItem2.setEnabled(false);

                } else {
                    jMenuItem2.setEnabled(true);

                }
                jPopupMenu1.show(jTable1, evt.getX(), evt.getY());

            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        int r = jTable1.getSelectedRow();

        if (r != -1) {

            String tid = jTable1.getValueAt(r, 0).toString();

            try {

                MySQL.iud("UPDATE `medical_test_has_tests` SET `status_id`=3 WHERE `medical_test_has_tests`.`id`='" + tid + "'");
                loadMedicalTest();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        int r = jTable1.getSelectedRow();

        if (r != -1) {

            String tid = jTable1.getValueAt(r, 0).toString();
            String tname = jTable1.getValueAt(r, 4).toString();
            boolean isFound = false;

            try {
                ResultSet rs = MySQL.search("SELECT * FROM `medical_test_has_tests` INNER JOIN `medical_test` ON `medical_test_has_tests`.`medical_test_id`=`medical_test`.`id` INNER JOIN `reference_no` ON `medical_test`.`reference_no_id`=`reference_no`.`id` INNER JOIN `patient` ON `reference_no`.`patient_id`=`patient`.`id` WHERE `medical_test_has_tests`.`id`='" + tid + "'");
                rs.next();
                String pname = rs.getString("patient.name");
                String contactNo = rs.getString("patient.phone_no");
                String referanceNo = rs.getString("num");
                String pathStream = "src/medicalTest/report.jasper";
                //  JasperReport jr = JasperCompileManager.compileReport(path);
                ResultSet rs1 = MySQL.search("SELECT * FROM `test_report`  WHERE `test_report`.`medical_test_has_tests_id`='" + tid + "'");
                Vector v = new Vector();
                while (rs1.next()) {

                    if (rs1.getString("nvalue") != null) {
                        isFound = true;
                    }
                    if (rs1.getString("result") == null) {
                        String topic = rs1.getString("name");
                        v.add(new TestReport(topic, "", "", ""));

                    } else {
                        String test = rs1.getString("name");
                        String result = rs1.getString("result");
                        String nvalue = rs1.getString("nvalue");
                        v.add(new TestReport("", test, result, nvalue));
                    }

                }
                HashMap parameters = new HashMap();
                parameters.put("testname", tname);

                parameters.put("barcode", referanceNo);
                parameters.put("reference", referanceNo);
                parameters.put("todate", datef.format(new Date()));
                parameters.put("pname", pname);
                parameters.put("pphone", contactNo);
                if (isFound) {
                    parameters.put("column3", "Normal Value");
                } else {
                    parameters.put("column3", "");
                }
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(v);
                JasperPrint jp = JasperFillManager.fillReport(pathStream, parameters, dataSource);
                JasperViewer.viewReport(jp, false);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jAvailblity;
    private javax.swing.JLabel jBalance;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JTextField jName;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField jPayment;
    private javax.swing.JTextField jPhone;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JButton jPrint;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jSearch1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JComboBox<String> jTitle;
    private javax.swing.JLabel jfee;
    private javax.swing.JComboBox<String> jpayMethod;
    private javax.swing.JTextField jreference;
    private javax.swing.JComboBox<String> jtest;
    private javax.swing.JComboBox<String> jtest1;
    private javax.swing.JLabel jtotal;
    // End of variables declaration//GEN-END:variables
}
