/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import java.awt.Font;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.MySQL;

/**
 *
 * @author ROG STRIX
 */
public class PhamarcyHome extends javax.swing.JPanel {

    SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat month = new SimpleDateFormat("yyyy-MM");
    DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Creates new form PhamarcyHome
     */
    public PhamarcyHome() {
        initComponents();
        jTable1.getTableHeader().setFont(new Font("Airal Nova", Font.PLAIN, 14));
        jTable2.getTableHeader().setFont(new Font("Airal Nova", Font.PLAIN, 14));
        jTable4.getTableHeader().setFont(new Font("Airal Nova", Font.PLAIN, 14));

        start();
        loadLowQtyProduct();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 4; i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            jTable2.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            jTable4.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        }
        loadExpierSoonProduct();
        loadExpierProduct();
    }

    private void start() {
        try {

            ResultSet rs = MySQL.search("SELECT COUNT(`id`) AS `number` FROM `product`;");
            rs.next();
            jLabel9.setText(rs.getString("number"));
            ResultSet rs2 = MySQL.search("SELECT COUNT(`id`) AS `number` FROM `stock` WHERE `exd`<'" + datef.format(new Date()) + "';");
            rs2.next();
            jLabel4.setText(rs2.getString("number"));
            Calendar cal = Calendar.getInstance();

            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, 30);
            String dateAfter = datef.format(cal.getTime());
            ResultSet rs3 = MySQL.search("SELECT COUNT(`id`) AS `number` FROM `stock` WHERE `exd`<'" + dateAfter + "' AND `exd`>'" + datef.format(new Date()) + "'");
            rs3.next();
            jLabel2.setText(rs3.getString("number"));

            ResultSet rs4 = MySQL.search("SELECT COUNT(`id`) AS `number` FROM `stock` WHERE `qty`<'5'");
            rs4.next();
            jLabel7.setText(rs4.getString("number"));
            ResultSet rs5 = MySQL.search("SELECT *\n"
                    + "FROM `invoice_item`\n"
                    + "INNER JOIN `stock` ON `invoice_item`.`stock_id`=`stock`.`id`"
                    + "INNER JOIN `invoice` ON `invoice_item`.`invoice_id`=`invoice`.`id`"
                    + "WHERE `invoice`.`date_time` LIKE '" + month.format(new Date()) + "%'");
            double total = 0;
            while (rs5.next()) {
                total += Double.valueOf(rs5.getString("stock.selling_price")) * Integer.valueOf(rs5.getString("invoice_item.qty"));
            }

            jLabel11.setText("Rs." + df.format(total));

            ResultSet rs6 = MySQL.search("SELECT *\n"
                    + "FROM `invoice_item`\n"
                    + "INNER JOIN `stock` ON `invoice_item`.`stock_id`=`stock`.`id`"
                    + "INNER JOIN `invoice` ON `invoice_item`.`invoice_id`=`invoice`.`id`");
            double total2 = 0;
            while (rs6.next()) {
                total2 += Double.valueOf(rs6.getString("stock.selling_price")) * Integer.valueOf(rs6.getString("invoice_item.qty"));
            }

            jLabel13.setText("Rs." + df.format(total2));

            ResultSet rs7 = MySQL.search("SELECT *\n"
                    + "FROM `grn_item`\n"
                    + "INNER JOIN `stock` ON `grn_item`.stock_id=`stock`.`id`\n"
                    + "INNER JOIN `grn` ON `grn_item`.`grn_id`=`grn`.`id`\n"
                    + "WHERE `grn`.`date_time` LIKE '" + month.format(new Date()) + "%'");
            double totalbuy = 0;
            while (rs7.next()) {
                totalbuy += Double.valueOf(rs7.getString("grn_item.buying_price")) * Integer.valueOf(rs7.getString("grn_item.qty"));
            }

            jLabel15.setText("Rs." + df.format(total - totalbuy));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLowQtyProduct() {
        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        dtm.setRowCount(0);
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `stock` INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` WHERE `qty`<'5' AND `status_id`<>'7'");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("stock.id"));
                v.add(rs.getString("product.name"));
                v.add(rs.getString("stock.exd"));
                v.add(rs.getString("stock.qty"));
                dtm.addRow(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadExpierSoonProduct() {
        DefaultTableModel dtm = (DefaultTableModel) jTable4.getModel();
        dtm.setRowCount(0);
        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 30);
        String dateAfter = datef.format(cal.getTime());
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `stock` INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` WHERE `exd`<'" + dateAfter + "' AND `exd`>'" + datef.format(new Date()) + "' AND `status_id`<>'7'");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("stock.id"));
                v.add(rs.getString("product.name"));
                v.add(rs.getString("stock.exd"));
                v.add(rs.getString("stock.qty"));
                dtm.addRow(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadExpierProduct() {
        DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
        dtm.setRowCount(0);
        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 30);
        String dateAfter = datef.format(cal.getTime());
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `stock` INNER JOIN `product` ON `stock`.`product_id`=`product`.`id` WHERE `exd`<'" + datef.format(new Date()) + "' AND `status_id`<>'7'");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("stock.id"));
                v.add(rs.getString("product.name"));
                v.add(rs.getString("stock.exd"));
                v.add(rs.getString("stock.qty"));
                dtm.addRow(v);
            }
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
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPopupMenu3 = new javax.swing.JPopupMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jPanel22 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();

        jMenuItem1.setText("Delete");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Delete");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu2.add(jMenuItem2);

        jMenuItem3.setText("Delete");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu3.add(jMenuItem3);

        setMinimumSize(new java.awt.Dimension(1083, 624));
        setPreferredSize(new java.awt.Dimension(1083, 624));

        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel17.setPreferredSize(new java.awt.Dimension(221, 102));
        jPanel17.setLayout(new java.awt.GridLayout(2, 0));

        jLabel8.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Total Medicines");
        jPanel17.add(jLabel8);

        jLabel9.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("0");
        jPanel17.add(jLabel9);

        jPanel22.add(jPanel17);

        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel12.setFocusable(false);
        jPanel12.setMinimumSize(new java.awt.Dimension(221, 102));
        jPanel12.setPreferredSize(new java.awt.Dimension(221, 102));
        jPanel12.setRequestFocusEnabled(false);
        jPanel12.setPreferredSize(new java.awt.Dimension(221, 102));
        jPanel12.setLayout(new java.awt.GridLayout(2, 0));

        jLabel3.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Expired Medicine");
        jPanel12.add(jLabel3);

        jLabel4.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("0");
        jPanel12.add(jLabel4);

        jPanel22.add(jPanel12);

        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel15.setPreferredSize(new java.awt.Dimension(221, 102));
        jPanel15.setLayout(new java.awt.GridLayout(2, 0));

        jLabel5.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Expire soon Medicine");
        jPanel15.add(jLabel5);

        jLabel2.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("0");
        jPanel15.add(jLabel2);

        jPanel22.add(jPanel15);

        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel16.setPreferredSize(new java.awt.Dimension(221, 102));
        jPanel16.setLayout(new java.awt.GridLayout(2, 0));

        jLabel6.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Low Quantity Medicine");
        jPanel16.add(jLabel6);

        jLabel7.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("0");
        jPanel16.add(jLabel7);

        jPanel22.add(jPanel16);

        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel18.setPreferredSize(new java.awt.Dimension(221, 102));
        jPanel18.setLayout(new java.awt.GridLayout(2, 0));

        jLabel10.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Monthly Earning");
        jPanel18.add(jLabel10);

        jLabel11.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("0");
        jPanel18.add(jLabel11);

        jPanel22.add(jPanel18);

        jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel19.setPreferredSize(new java.awt.Dimension(221, 102));
        jPanel19.setLayout(new java.awt.GridLayout(2, 0));

        jLabel12.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Total Earning");
        jPanel19.add(jLabel12);

        jLabel13.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("0");
        jPanel19.add(jLabel13);

        jPanel22.add(jPanel19);

        jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel20.setPreferredSize(new java.awt.Dimension(221, 102));
        jPanel20.setLayout(new java.awt.GridLayout(2, 0));

        jLabel14.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Monthly Profit");
        jPanel20.add(jLabel14);

        jLabel15.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("0");
        jPanel20.add(jLabel15);

        jPanel22.add(jPanel20);

        jPanel1.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        jTable1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock ID", "Medicine Name", "EXD", "Qty"
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
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(90);
            jTable1.getColumnModel().getColumn(3).setMaxWidth(50);
        }

        jLabel19.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Low Quantity Medicines");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);

        jTable4.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Stock ID", "Medicine Name", "EXD", "Qty"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable4);
        if (jTable4.getColumnModel().getColumnCount() > 0) {
            jTable4.getColumnModel().getColumn(0).setMaxWidth(90);
            jTable4.getColumnModel().getColumn(3).setMaxWidth(50);
        }

        jLabel18.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Expire Soon Medicines");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel5);

        jTable2.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Stock ID", "Medicine Name", "EXD", "Qty"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
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
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMaxWidth(90);
            jTable2.getColumnModel().getColumn(3).setMaxWidth(50);
        }

        jLabel16.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Expired Medicines");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1083, Short.MAX_VALUE)
            .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (jTable1.getSelectedRow() != -1) {
            if (evt.getButton() == 3 && !LogIn.userType.equals("Pharmacy Cashier")) {
                jPopupMenu1.show(jTable1, evt.getX(), evt.getY());

            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (jTable1.getSelectedRow() != -1) {

            String id = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();

            MySQL.iud("UPDATE `stock` SET `status_id`='7' WHERE `id`='" + id + "'");
            loadLowQtyProduct();

        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
        if (jTable4.getSelectedRow() != -1) {
            if (evt.getButton() == 3 && !LogIn.userType.equals("Pharmacy Cashier")) {
                jPopupMenu2.show(jTable4, evt.getX(), evt.getY());

            }
        }
    }//GEN-LAST:event_jTable4MouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        if (jTable4.getSelectedRow() != -1) {

            String id = jTable4.getValueAt(jTable4.getSelectedRow(), 0).toString();

            MySQL.iud("UPDATE `stock` SET `status_id`='7' WHERE `id`='" + id + "'");
            loadExpierSoonProduct();
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        if (jTable2.getSelectedRow() != -1) {
            if (evt.getButton() == 3 && !LogIn.userType.equals("Pharmacy Cashier")) {
                jPopupMenu3.show(jTable2, evt.getX(), evt.getY());

            }
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        if (jTable2.getSelectedRow() != -1) {

            String id = jTable2.getValueAt(jTable2.getSelectedRow(), 0).toString();

            MySQL.iud("UPDATE `stock` SET `status_id`='7' WHERE `id`='" + id + "'");
            loadExpierProduct();
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable4;
    // End of variables declaration//GEN-END:variables
}
