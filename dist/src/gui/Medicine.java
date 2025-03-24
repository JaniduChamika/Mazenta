/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import java.awt.Font;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
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
public class Medicine extends javax.swing.JPanel {

    Phamarcy p;
    GRN grn;
    MedicineSelect ms;
    Icon successIcon = new ImageIcon("src/resourse/icon/success.png");

    /**
     * Creates new form Medicine
     */
    public Medicine() {
        initComponents();
    }

    public Medicine(Phamarcy p) {
        initComponents();
        jTable1.getTableHeader().setFont(new Font("Airal Nova", Font.PLAIN, 14));

        this.p = p;

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 4; i++) {
            if (i != 1) {
                jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        loadCategory();
        loadBrand();

        loadMedicine();
        Listner();
    }

    public Medicine(Phamarcy p, GRN grn, MedicineSelect ms) {
        initComponents();
        jTable1.getTableHeader().setFont(new Font("Airal Nova", Font.PLAIN, 14));

        this.p = p;
        this.grn = grn;
        this.ms = ms;
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 4; i++) {
            if (i != 1) {

                jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

        }
        loadCategory();
        loadBrand();
        loadMedicine();
        Listner();
    }

    private void Listner() {
        ListSelectionListener lsl = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int r = jTable1.getSelectedRow();
                if (r != -1) {
                    jName.setText(jTable1.getValueAt(r, 1).toString());
                    jCategory1.setSelectedItem(jTable1.getValueAt(r, 2).toString());
                    jBrand1.setSelectedItem(jTable1.getValueAt(r, 3).toString());
                    jDesc.setText(jTable1.getValueAt(r, 4).toString());
                    jButton2.setEnabled(false);
                    jButton3.setEnabled(true);
                }
            }
        };
        jTable1.getSelectionModel().addListSelectionListener(lsl);
    }

    public void loadCategory() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `category` ORDER BY `name` ASC");

            Vector v = new Vector();
            v.add("Select");

            while (rs.next()) {
                v.add(rs.getString("name"));
            }

            jCategory1.setModel(new DefaultComboBoxModel(v));
            jCategory2.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadBrand() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `brand` ORDER BY `name` ASC");

            Vector v = new Vector();
            v.add("Select");

            while (rs.next()) {
                v.add(rs.getString("name"));
            }

            jBrand1.setModel(new DefaultComboBoxModel(v));
            jBrand2.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadMedicine() {
        String category = jCategory2.getSelectedItem().toString();
        String brand = jBrand2.getSelectedItem().toString();
        String name = jSearch1.getText();

        try {
            String q = "SELECT * FROM `product` INNER JOIN `category` ON `product`.`category_id`=`category`.`id` INNER JOIN `brand` ON `product`.`brand_id`=`brand`.`id` ";
            if (!category.equals("Select") || !name.isEmpty() || !brand.equals("Select")) {
                q += " WHERE ";
            }

            Vector queryVector = new Vector();
            if (!category.equals("Select")) {
                queryVector.add("`category`.`name`='" + category + "'");
            }
            if (!brand.equals("Select")) {
                queryVector.add("`brand`.`name`='" + brand + "'");
            }
            if (!name.isEmpty()) {
                queryVector.add("`product`.`name` LIKE '" + name.replaceAll("'", "\\\\'") + "%'");

            }
            for (int i = 0; i < queryVector.size(); i++) {
                q += queryVector.get(i);
                if (i < queryVector.size() - 1) {
                    q += " AND ";

                }
            }
           
            ResultSet rs = MySQL.search(q + "ORDER BY `product`.`name`");
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("product.id"));
                v.add(rs.getString("product.name"));
                v.add(rs.getString("category.name"));
                v.add(rs.getString("brand.name"));
                v.add(rs.getString("description"));
                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetFeild() {
        jName.setText("");
        jCategory1.setSelectedIndex(0);
        jBrand1.setSelectedIndex(0);
        jDesc.setText("");
        jButton2.setEnabled(true);
        jButton3.setEnabled(false);
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
        jLabel24 = new javax.swing.JLabel();
        jSearch1 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jCategory2 = new javax.swing.JComboBox<>();
        jBrand2 = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jCategory1 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jName = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jDesc = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jBrand1 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();

        setToolTipText("");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));

        jLabel24.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel24.setText("Search");

        jSearch1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jSearch1.setToolTipText("Search by name or id");
        jSearch1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jSearch1KeyReleased(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel26.setText("Category");

        jCategory2.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jCategory2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCategory2ItemStateChanged(evt);
            }
        });

        jBrand2.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jBrand2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jBrand2ItemStateChanged(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel27.setText("Brand");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel26)
                .addGap(18, 18, 18)
                .addComponent(jCategory2, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel27)
                .addGap(18, 18, 18)
                .addComponent(jBrand2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBrand2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(jCategory2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jTable1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Category", "Brand", "Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(100);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane10)
                .addContainerGap())
        );

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Medicine Details"));

        jLabel9.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel9.setText("Description");

        jCategory1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel10.setText("Category");

        jLabel13.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel13.setText("Name");

        jName.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jName.setToolTipText("Appointment Receit refernce no");
        jName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jNameKeyReleased(evt);
            }
        });

        jButton1.setForeground(new java.awt.Color(51, 51, 51));
        jButton1.setText("+");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jDesc.setColumns(20);
        jDesc.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jDesc.setLineWrap(true);
        jDesc.setRows(5);
        jScrollPane1.setViewportView(jDesc);

        jButton2.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(51, 51, 51));
        jButton2.setText("Register");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(51, 51, 51));
        jButton3.setText("Update");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(51, 51, 51));
        jButton4.setText("Clear");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setForeground(new java.awt.Color(51, 51, 51));
        jButton5.setText("+");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jBrand1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel11.setText("Brand");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jName, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jCategory1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jBrand1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCategory1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jBrand1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(688, 688, 688))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 98, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel3);

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
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(jScrollPane2)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jSearch1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSearch1KeyReleased
        loadMedicine();
    }//GEN-LAST:event_jSearch1KeyReleased

    private void jCategory2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCategory2ItemStateChanged
        loadMedicine();
    }//GEN-LAST:event_jCategory2ItemStateChanged

    private void jNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jNameKeyReleased

    }//GEN-LAST:event_jNameKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        CategoryRegistation cr = new CategoryRegistation(this.p, true, this);
        cr.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String name = jName.getText();
        String des = jDesc.getText();
        String category = jCategory1.getSelectedItem().toString();
        String brand = jBrand1.getSelectedItem().toString();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter medicine name", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (name.length() > 100) {
            JOptionPane.showMessageDialog(this, "Medicine name character length must be less than 100", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (category.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select category ", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (brand.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select brand ", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {
            try {
                ResultSet rs = MySQL.search("SELECT * FROM `category` WHERE `name`='" + category.replaceAll("'", "\\\\'") + "'");
                rs.next();
                String categoryId = rs.getString("id");
                ResultSet rs1 = MySQL.search("SELECT * FROM `brand` WHERE `name`='" + brand.replaceAll("'", "\\\\'") + "'");
                rs1.next();
                String brandid = rs1.getString("id");
                MySQL.iud("INSERT INTO `product` (`name`,`description`,`category_id`,`brand_id`) VALUES ('" + name.replaceAll("'", "\\\\'") + "','" + des.replaceAll("'", "\\\\'") + "','" + categoryId + "','" + brandid + "')");
                loadMedicine();
                resetFeild();
                JOptionPane.showMessageDialog(this, "New medicine registered success", "Success", JOptionPane.INFORMATION_MESSAGE,successIcon);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (ms != null) {
            if (evt.getClickCount() == 2) {
                int r = jTable1.getSelectedRow();
                if (r != -1) {

                    this.grn.jMediId.setText(jTable1.getValueAt(r, 0).toString());
                    this.grn.jLabel21.setText(jTable1.getValueAt(r, 1).toString());
                    this.grn.jLabel22.setText(jTable1.getValueAt(r, 2).toString());

                    ms.dispose();
                }

            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String name = jName.getText();
        String des = jDesc.getText();
        String category = jCategory1.getSelectedItem().toString();
        String id = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
        String brand = jBrand1.getSelectedItem().toString();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter medicine name", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (name.length() > 100) {
            JOptionPane.showMessageDialog(this, "Medicine name character length must be less than 100", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (category.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select category", "Warning", JOptionPane.WARNING_MESSAGE);

        } else if (brand.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select brand ", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {
            try {
                ResultSet rs = MySQL.search("SELECT * FROM `category` WHERE `name`='" + category.replaceAll("'", "\\\\'") + "'");
                rs.next();
                String categoryId = rs.getString("id");
                ResultSet rs1 = MySQL.search("SELECT * FROM `brand` WHERE `name`='" + brand.replaceAll("'", "\\\\'") + "'");
                rs1.next();
                String brandid = rs1.getString("id");
                MySQL.iud("UPDATE `product` SET `name`='" + name.replaceAll("'", "\\\\'") + "',`description`='" + des.replaceAll("'", "\\\\'") + "',`category_id`='" + categoryId + "',`brand_id`='" + brandid + "' WHERE `id`='" + id + "'");
                loadMedicine();
                resetFeild();
                JOptionPane.showMessageDialog(this, "Medicine updated success", "Success", JOptionPane.INFORMATION_MESSAGE,successIcon);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        resetFeild();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        BrandRegistration br = new BrandRegistration(this.p, true, this);
        br.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jBrand2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jBrand2ItemStateChanged
        loadMedicine();
    }//GEN-LAST:event_jBrand2ItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jBrand1;
    private javax.swing.JComboBox<String> jBrand2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jCategory1;
    private javax.swing.JComboBox<String> jCategory2;
    private javax.swing.JTextArea jDesc;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jName;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jSearch1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
