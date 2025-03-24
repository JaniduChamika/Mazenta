/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import java.awt.Font;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import model.MySQL;

/**
 *
 * @author ROG STRIX
 */
public class UserManagement extends javax.swing.JPanel {

    /**
     * Creates new form UserManagement
     */
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Icon successIcon = new ImageIcon("src/resourse/icon/success.png");

    public UserManagement() {
        initComponents();
        jTable1.getTableHeader().setFont(new Font("Airal Nova", Font.PLAIN, 14));

        loadGender();
        loadUserType();
        loadUser();
        tableListner();
    }

    private void tableListner() {
        ListSelectionListener lsl = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int r = jTable1.getSelectedRow();
                if (r != -1) {
                    loadUserType();

                    jIDLeb.setText(jTable1.getValueAt(r, 0).toString());
                    jName.setText(jTable1.getValueAt(r, 1).toString());
                    jUsername.setText(jTable1.getValueAt(r, 2).toString());
                    jPasswordField1.setText(jTable1.getValueAt(r, 3).toString());
                    jPhone.setText(jTable1.getValueAt(r, 4).toString());
                    jEmail.setText(jTable1.getValueAt(r, 5).toString());
                    jComboBox2.setSelectedItem(jTable1.getValueAt(r, 6).toString());

                    try {
                        jDateChooser1.setDate(sdf.parse(jTable1.getValueAt(r, 7).toString()));
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }

                    if (!jTable1.getValueAt(r, 0).toString().equals("1")) {
                        jUsertype.setSelectedItem(jTable1.getValueAt(r, 9).toString());
                        jUsertype.setEnabled(true);

                    } else {
                        jUsertype.addItem("Super Admin");
                        jUsertype.setSelectedItem("Super Admin");
                        jUsertype.setEnabled(false);

                    }
                    jAddBtn.setEnabled(false);
                    jUpdateBtn.setEnabled(true);

                }
            }
        };
        jTable1.getSelectionModel().addListSelectionListener(lsl);
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

    private void loadUserType() {
        try {
            ResultSet rs;
            if (LogIn.userType.equalsIgnoreCase("Admin")) {
                rs = MySQL.search("SELECT * FROM `user_type` WHERE  `id` NOT IN (1,2)");

            } else {
                rs = MySQL.search("SELECT * FROM `user_type` WHERE  `id`<>1");

            }

            Vector v = new Vector();
            v.add("Select");

            while (rs.next()) {

                v.add(rs.getString("name"));
            }

            jUsertype.setModel(new DefaultComboBoxModel(v));
            jSearchUsertype.setModel(new DefaultComboBoxModel(v));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUser() {
        try {
            String q = "SELECT * FROM `user` INNER JOIN `gender` ON `user`.`gender_id`=`gender`.`id` INNER JOIN `user_type` ON `user`.`user_type_id`=`user_type`.`id` INNER JOIN `status` ON `user`.`status_id`=`status`.`id`";
            String txt = jSearch.getText();
            String type = jSearchUsertype.getSelectedItem().toString();
            String gender = jComboBox1.getSelectedItem().toString();
            Vector query = new Vector();
            if (!txt.isEmpty() || !type.equals("Select") || !gender.equals("Select")) {
                q += "WHERE ";

            }
            if (!txt.isEmpty()) {
                query.add("(`user`.`id` = '" + txt + "' OR `user`.`name` LIKE '" + txt + "%' OR `user`.`phone_no` LIKE '" + txt + "%' OR `user`.`email` LIKE '" + txt + "%'OR `user`.`username` LIKE '" + txt + "%') ");

            }
            if (!type.equals("Select")) {

                query.add("`user_type`.`name`='" + type + "'");
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
            ResultSet rs = MySQL.search(q + " ORDER BY `user`.`id` ASC");

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("user.id"));
                v.add(rs.getString("user.name"));
                v.add(rs.getString("username"));
                v.add(rs.getString("password"));
                v.add(rs.getString("phone_no"));
                v.add(rs.getString("email"));
                v.add(rs.getString("gender.name"));
                v.add(rs.getString("dob"));
                v.add(rs.getString("reg_date"));
                v.add(rs.getString("user_type.name"));
                v.add(rs.getString("status.name"));
                dtm.addRow(v);
            }
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < 11; i++) {
                jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void resetField() {
        jIDLeb.setText("#");
        jName.setText("");
        jPhone.setText("");
        jEmail.setText("");
        jDateChooser1.setDate(null);
        jUsername.setText("");
        jPasswordField1.setText("");
        jComboBox2.setSelectedIndex(0);
        jUsertype.setSelectedIndex(0);
        jAddBtn.setEnabled(true);
        jUpdateBtn.setEnabled(false);
        jUsertype.setEnabled(true);
        loadUserType();
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jIDLeb = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jName = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jUsertype = new javax.swing.JComboBox<>();
        jUsername = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPhone = new javax.swing.JTextField();
        jEmail = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jUpdateBtn = new javax.swing.JButton();
        jAddBtn = new javax.swing.JButton();
        jClearBtn = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jSearch = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jSearchUsertype = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();

        jMenuItem1.setText("Activate");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Deactivate");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        setFont(new java.awt.Font("Arial Nova", 0, 12)); // NOI18N

        jPanel2.setPreferredSize(new java.awt.Dimension(464, 430));

        jTable1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Username", "Password", "Phone No", "Email", "Gender", "DOB", "Reg. Date", "User Type", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setMinimumSize(new java.awt.Dimension(120, 0));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));

        jLabel11.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel11.setText("User ID");

        jIDLeb.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jIDLeb.setText("#");

        jLabel13.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel13.setText("Name");

        jLabel16.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel16.setText("Gender");

        jLabel17.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel17.setText("DOB");

        jLabel18.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel18.setText("User Type");

        jName.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jUsertype.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jUsername.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jLabel24.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel24.setText("Username");

        jLabel25.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel25.setText("Password");

        jPasswordField1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jComboBox2.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel13))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jIDLeb, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(jName))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addGap(25, 25, 25)
                        .addComponent(jPasswordField1))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jUsertype, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(22, 22, 22)
                        .addComponent(jUsername)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jIDLeb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jUsertype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Phone No");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPhone, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                    .addComponent(jEmail))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jUpdateBtn.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jUpdateBtn.setForeground(new java.awt.Color(0, 0, 0));
        jUpdateBtn.setText("Update");
        jUpdateBtn.setEnabled(false);
        jUpdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jUpdateBtnActionPerformed(evt);
            }
        });

        jAddBtn.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jAddBtn.setForeground(new java.awt.Color(0, 0, 0));
        jAddBtn.setText("Add");
        jAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAddBtnActionPerformed(evt);
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
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jClearBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jAddBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jUpdateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jAddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jUpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jClearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setText("Search");

        jSearch.setToolTipText("Search by user ID, Name, Phone No, Email Address, Username");
        jSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jSearchKeyReleased(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setText("User Type");

        jSearchUsertype.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jSearchUsertypeItemStateChanged(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Gender");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
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
                .addComponent(jSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSearchUsertype, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSearchUsertype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel22))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(jSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 719, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jClearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jClearBtnActionPerformed
        resetField();
    }//GEN-LAST:event_jClearBtnActionPerformed

    private void jAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAddBtnActionPerformed

        String name = jName.getText();

        String mobile = jPhone.getText();
        String email = jEmail.getText();
        Date dob = jDateChooser1.getDate();
        String username = jUsername.getText();
        char[] pw = jPasswordField1.getPassword();
        String password = String.valueOf(pw);

        String gender = jComboBox2.getSelectedItem().toString();
        String uType = jUsertype.getSelectedItem().toString();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter password", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (password.length() < 5) {
            JOptionPane.showMessageDialog(this, "Password length must be at least 5 character", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (gender.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select gender", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (dob == null) {
            JOptionPane.showMessageDialog(this, "Invaild Date of birth", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (uType.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select user type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter phone number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("07[1,2,4,5,6,7,8][0-9]{7}").matcher(mobile).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid phone number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email address", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$").matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid email address", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            String dob1 = sdf.format(dob);
            try {
                ResultSet rs = MySQL.search("SELECT * FROM `user_type` WHERE `name`='" + uType + "'");
                rs.next();
                String userTypeId = rs.getString("id");
                ResultSet rs1 = MySQL.search("SELECT * FROM `gender` WHERE `name`='" + gender + "'");
                rs1.next();
                String genderId = rs1.getString("id");
                //insert data to user table
                String regDate = sdf.format(new Date());
                ResultSet rs2 = MySQL.search("SELECT * FROM `user` WHERE `username`='" + username + "'");
                if (rs2.next()) {
                    JOptionPane.showMessageDialog(this, "Already exist user with this username", "Warning", JOptionPane.WARNING_MESSAGE);

                } else {
                    MySQL.iud("INSERT INTO `user` (`name`,`username`,`password`,`email`,`phone_no`,`gender_id`,`user_type_id`,`reg_date`,`dob`) "
                            + "VALUES ('" + name + "','" + username + "','" + password + "','" + email + "','" + mobile + "','" + genderId + "','" + userTypeId + "','" + regDate + "','" + dob1 + "')"
                    );

                    loadUser();
                    resetField();
                    JOptionPane.showMessageDialog(this, "User registerd success", "Success", JOptionPane.INFORMATION_MESSAGE,successIcon);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_jAddBtnActionPerformed

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

    private void jUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jUpdateBtnActionPerformed
        String uid = jIDLeb.getText();

        String name = jName.getText();

        String mobile = jPhone.getText();
        String email = jEmail.getText();
        Date dob = jDateChooser1.getDate();
        String username = jUsername.getText();
        char[] pw = jPasswordField1.getPassword();
        String password = String.valueOf(pw);

        String gender = jComboBox2.getSelectedItem().toString();
        String uType = jUsertype.getSelectedItem().toString();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter password", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (gender.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select gender", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (dob == null) {
            JOptionPane.showMessageDialog(this, "Invaild Date of birth", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (uType.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select user type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter phone number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("07[1,2,4,5,6,7,8][0-9]{7}").matcher(mobile).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid phone number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email address", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$").matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid email address", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            String dob1 = sdf.format(dob);
            try {
                ResultSet rs = MySQL.search("SELECT * FROM `user_type` WHERE `name`='" + uType + "'");
                rs.next();
                String userTypeId = rs.getString("id");
                ResultSet rs1 = MySQL.search("SELECT * FROM `gender` WHERE `name`='" + gender + "'");
                rs1.next();
                String genderId = rs1.getString("id");
                //insert data to user table

                MySQL.iud("UPDATE `user` SET `name`='" + name + "',`username`='" + username + "',`password`='" + password + "',`email`='" + email + "',`phone_no`='" + mobile + "',`gender_id`='" + genderId + "',`user_type_id`='" + userTypeId + "',`dob`='" + dob1 + "' WHERE `id`='" + uid + "'");
                loadUser();
                resetField();
                JOptionPane.showMessageDialog(this, "User update success", "Success", JOptionPane.INFORMATION_MESSAGE,successIcon);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_jUpdateBtnActionPerformed

    private void jSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSearchKeyReleased
        loadUser();
    }//GEN-LAST:event_jSearchKeyReleased

    private void jSearchUsertypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jSearchUsertypeItemStateChanged
        loadUser();
    }//GEN-LAST:event_jSearchUsertypeItemStateChanged

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        loadUser();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        String uid = jIDLeb.getText();
        MySQL.iud("UPDATE `user` SET `status_id`=4 WHERE `id`='" + uid + "'");
        loadUser();

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        String uid = jIDLeb.getText();
        MySQL.iud("UPDATE `user` SET `status_id`=5 WHERE `id`='" + uid + "'");
        loadUser();

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (jTable1.getSelectedRow() != -1) {
            if (evt.getButton() == 3) {
                String state = jTable1.getValueAt(jTable1.getSelectedRow(), 10).toString();
                if (state.equalsIgnoreCase("Active")) {
                    jMenuItem1.setVisible(false);
                    jMenuItem2.setVisible(true);

                } else {
                    jMenuItem1.setVisible(true);
                    jMenuItem2.setVisible(false);
                }

                jPopupMenu1.show(jTable1, evt.getX(), evt.getY());

            }
        }
    }//GEN-LAST:event_jTable1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jAddBtn;
    private javax.swing.JButton jClearBtn;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JTextField jEmail;
    private javax.swing.JLabel jIDLeb;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JTextField jName;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jPhone;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jSearch;
    private javax.swing.JComboBox<String> jSearchUsertype;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton jUpdateBtn;
    private javax.swing.JTextField jUsername;
    private javax.swing.JComboBox<String> jUsertype;
    // End of variables declaration//GEN-END:variables
}
