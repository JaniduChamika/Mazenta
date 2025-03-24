/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import model.MySQL;

/**
 *
 * @author ROG STRIX
 */
public class FrontDeskHome extends javax.swing.JPanel {

    SimpleDateFormat stf2 = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat month = new SimpleDateFormat("yyyy-MM");
    DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Creates new form FrontDeskHome
     */
    public FrontDeskHome() {
        initComponents();
        begin();
    }

    private void begin() {
        try {
            ResultSet rs = MySQL.search("SELECT COUNT(`id`) AS `number` FROM `doctor`;");
            rs.next();
            jLabel4.setText(rs.getString("number"));
            ResultSet rs1 = MySQL.search("SELECT COUNT(`id`) AS `number` FROM `appointment` WHERE `appointment`.`appoint_date`>'" + datef.format(new Date()) + "' AND `appointment`.`end_time`>'" + stf2.format(new Date()) + "'");

            rs1.next();
            jLabel2.setText(rs1.getString("number"));

            ResultSet rs2 = MySQL.search("SELECT COUNT(`medical_test_has_tests`.`medical_test_id`) AS `number` FROM `medical_test_has_tests` WHERE `status_id`<>3");

            rs2.next();
            jLabel7.setText(rs2.getString("number"));
            ResultSet rs3 = MySQL.search("SELECT COUNT(`id`) AS `number` FROM `user` ");

            rs3.next();
            jLabel9.setText(rs3.getString("number"));
            ResultSet rs5 = MySQL.search("SELECT *\n"
                    + "FROM `invoice_item`\n"
                    + "INNER JOIN `stock` ON `invoice_item`.`stock_id`=`stock`.`id`"
                    + "INNER JOIN `invoice` ON `invoice_item`.`invoice_id`=`invoice`.`id`"
                    + "WHERE `invoice`.`date_time` LIKE '" + month.format(new Date()) + "%'");
            double total = 0;
            while (rs5.next()) {
                total += Double.valueOf(rs5.getString("stock.selling_price")) * Integer.valueOf(rs5.getString("invoice_item.qty"));
            }
            ResultSet rs8 = MySQL.search("SELECT *\n"
                    + "FROM `appointment`\n"
                    + "WHERE `appointment`.`applydate` LIKE '" + month.format(new Date()) + "%'");
            while (rs8.next()) {
                total += Double.valueOf(rs8.getString("fee"));
            }
            ResultSet rs9 = MySQL.search("SELECT *\n"
                    + "FROM `medical_test`\n"
                    + "INNER JOIN `medical_test_payment` ON `medical_test`.`id`=`medical_test_payment`.`medical_test_id`"
                    + "WHERE `medical_test`.`apply_date` LIKE '" + month.format(new Date()) + "%'");
            while (rs8.next()) {
                total += Double.valueOf(rs9.getString("payment")) - Double.valueOf(rs9.getString("balance"));
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
            ResultSet rs10 = MySQL.search("SELECT *\n"
                    + "FROM `appointment`\n");
            while (rs10.next()) {
                total2 += Double.valueOf(rs10.getString("fee"));
            }
            ResultSet rs11 = MySQL.search("SELECT *\n"
                    + "FROM `medical_test`\n"
                    + "INNER JOIN `medical_test_payment` ON `medical_test`.`id`=`medical_test_payment`.`medical_test_id`");
            while (rs11.next()) {
                total2 += Double.valueOf(rs11.getString("payment")) - Double.valueOf(rs11.getString("balance"));
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

            ResultSet rs12 = MySQL.search("SELECT *\n"
                    + "FROM `grn_item`\n"
                    + "INNER JOIN `stock` ON `grn_item`.stock_id=`stock`.`id`\n"
                    + "INNER JOIN `grn` ON `grn_item`.`grn_id`=`grn`.`id`\n");
            double totalbuyGrand = 0;
            while (rs12.next()) {
                totalbuyGrand += Double.valueOf(rs12.getString("grn_item.buying_price")) * Integer.valueOf(rs12.getString("grn_item.qty"));
            }

            jLabel17.setText("Rs." + df.format(total2 - totalbuyGrand));
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
        jPanel20 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(200, 200));
        setPreferredSize(new java.awt.Dimension(1083, 620));

        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel17.setPreferredSize(new java.awt.Dimension(221, 102));
        jPanel17.setLayout(new java.awt.GridLayout(2, 0));

        jLabel8.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("System Users");
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
        jLabel3.setText("Doctors");
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
        jLabel5.setText("Appointment");
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
        jLabel6.setText("Medical Check-Up");
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

        jPanel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel21.setPreferredSize(new java.awt.Dimension(221, 102));
        jPanel21.setLayout(new java.awt.GridLayout(2, 0));

        jLabel16.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Total Profit");
        jPanel21.add(jLabel16);

        jLabel17.setFont(new java.awt.Font("Arial Nova", 0, 18)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("0");
        jPanel21.add(jLabel17);

        jPanel22.add(jPanel21);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resourse/image/res.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1094, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 502, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, 1094, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    // End of variables declaration//GEN-END:variables
}
