/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import com.formdev.flatlaf.IntelliJTheme;
import java.awt.Color;
import java.awt.Image;
import java.io.InputStream;
import javax.swing.ImageIcon;

/**
 *
 * @author ROG STRIX
 */
public class Phamarcy extends javax.swing.JFrame {

    Color bg = new Color(0, 204, 204);
    Color fc = new Color(255, 255, 255);
    Color fcd = new Color(51, 51, 51);

    /**
     * Creates new form Phamarcy
     */
    public Phamarcy() {
        initComponents();
        PhamarcyHome panel = new PhamarcyHome();
        jPanel1.removeAll();
        jPanel1.add(panel);
        jPanel1.revalidate();
        jPanel1.repaint();
        jButton1.setBackground(bg);
        jButton1.setForeground(fc);
        jButton1.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/homew.png")));

        ImageIcon i = new ImageIcon("src/resourse/image/3.png");
        Image x = i.getImage();
        setIconImage(x);

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
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Phamarcy");
        setMinimumSize(new java.awt.Dimension(1110, 740));
        setPreferredSize(new java.awt.Dimension(1110, 740));

        jPanel2.setLayout(new java.awt.GridLayout(1, 0, 5, 5));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Arial Nova", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 51, 51));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resourse/icon/homeb.png"))); // NOI18N
        jButton1.setText("Home");
        jButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 204), 1, true));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Arial Nova", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(51, 51, 51));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resourse/icon/medb.png"))); // NOI18N
        jButton3.setText("Medicine");
        jButton3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 204), 1, true));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setFont(new java.awt.Font("Arial Nova", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(51, 51, 51));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resourse/icon/stockb.png"))); // NOI18N
        jButton6.setText("Stock");
        jButton6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 204), 1, true));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6);

        jButton7.setBackground(new java.awt.Color(255, 255, 255));
        jButton7.setFont(new java.awt.Font("Arial Nova", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(51, 51, 51));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resourse/icon/supb.png"))); // NOI18N
        jButton7.setText("Supplier");
        jButton7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 204), 1, true));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton7);

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Arial Nova", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(51, 51, 51));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resourse/icon/grnb.png"))); // NOI18N
        jButton5.setText("GRN");
        jButton5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 204), 1, true));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5);

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setFont(new java.awt.Font("Arial Nova", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(51, 51, 51));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resourse/icon/bilb.png"))); // NOI18N
        jButton4.setText("Billing");
        jButton4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 204), 1, true));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4);

        jButton8.setBackground(new java.awt.Color(255, 255, 255));
        jButton8.setFont(new java.awt.Font("Arial Nova", 1, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(51, 51, 51));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resourse/icon/loutb.png"))); // NOI18N
        jButton8.setText("Logout");
        jButton8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 204), 1, true));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton8);

        jPanel1.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        PhamarcyHome panel = new PhamarcyHome();
        jPanel1.removeAll();
        jPanel1.add(panel);
        jPanel1.revalidate();
        jPanel1.repaint();

        jButton1.setBackground(bg);

        jButton1.setForeground(fc);
        jButton1.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/homew.png")));

        jButton3.setForeground(fcd);
        jButton4.setForeground(fcd);
        jButton5.setForeground(fcd);
        jButton6.setForeground(fcd);
        jButton7.setForeground(fcd);

        jButton3.setBackground(null);
        jButton4.setBackground(null);
        jButton5.setBackground(null);
        jButton6.setBackground(null);
        jButton7.setBackground(null);

        jButton3.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/medb.png")));
        jButton4.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/bilb.png")));
        jButton5.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/grnb.png")));
        jButton6.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/stockb.png")));
        jButton7.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/supb.png")));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Medicine panel = new Medicine(this);
        jPanel1.removeAll();
        jPanel1.add(panel);
        jPanel1.revalidate();
        jPanel1.repaint();

        jButton3.setBackground(bg);

        jButton3.setForeground(fc);
        jButton3.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/medw.png")));

        jButton1.setForeground(fcd);
        jButton4.setForeground(fcd);
        jButton5.setForeground(fcd);
        jButton6.setForeground(fcd);
        jButton7.setForeground(fcd);

        jButton1.setBackground(null);
        jButton4.setBackground(null);
        jButton5.setBackground(null);
        jButton6.setBackground(null);
        jButton7.setBackground(null);

        jButton1.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/homeb.png")));
        jButton4.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/bilb.png")));
        jButton5.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/grnb.png")));
        jButton6.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/stockb.png")));
        jButton7.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/supb.png")));
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Billing panel = new Billing(this);
        jPanel1.removeAll();
        jPanel1.add(panel);
        jPanel1.revalidate();
        jPanel1.repaint();

        jButton4.setBackground(bg);

        jButton4.setForeground(fc);
        jButton4.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/bilw.png")));

        jButton3.setForeground(fcd);
        jButton1.setForeground(fcd);
        jButton5.setForeground(fcd);
        jButton6.setForeground(fcd);
        jButton7.setForeground(fcd);

        jButton3.setBackground(null);
        jButton1.setBackground(null);
        jButton5.setBackground(null);
        jButton6.setBackground(null);
        jButton7.setBackground(null);

        jButton3.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/medb.png")));
        jButton1.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/homeb.png")));
        jButton5.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/grnb.png")));
        jButton6.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/stockb.png")));
        jButton7.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/supb.png")));
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        GRN panel = new GRN(this);
        jPanel1.removeAll();
        jPanel1.add(panel);
        jPanel1.revalidate();
        jPanel1.repaint();

        jButton5.setBackground(bg);

        jButton5.setForeground(fc);
        jButton5.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/grnw.png")));

        jButton3.setForeground(fcd);
        jButton4.setForeground(fcd);
        jButton1.setForeground(fcd);
        jButton6.setForeground(fcd);
        jButton7.setForeground(fcd);

        jButton3.setBackground(null);
        jButton4.setBackground(null);
        jButton1.setBackground(null);
        jButton6.setBackground(null);
        jButton7.setBackground(null);

        jButton3.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/medb.png")));
        jButton4.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/bilb.png")));
        jButton1.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/homeb.png")));
        jButton6.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/stockb.png")));
        jButton7.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/supb.png")));
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        Stock panel = new Stock();
        jPanel1.removeAll();
        jPanel1.add(panel);
        jPanel1.revalidate();
        jPanel1.repaint();

        jButton6.setBackground(bg);

        jButton6.setForeground(fc);
        jButton6.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/stockw.png")));

        jButton3.setForeground(fcd);
        jButton4.setForeground(fcd);
        jButton5.setForeground(fcd);
        jButton1.setForeground(fcd);
        jButton7.setForeground(fcd);

        jButton3.setBackground(null);
        jButton4.setBackground(null);
        jButton5.setBackground(null);
        jButton1.setBackground(null);
        jButton7.setBackground(null);

        jButton3.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/medb.png")));
        jButton4.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/bilb.png")));
        jButton5.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/grnb.png")));
        jButton1.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/homeb.png")));
        jButton7.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/supb.png")));
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        Supplier panel = new Supplier(this);
        jPanel1.removeAll();
        jPanel1.add(panel);
        jPanel1.revalidate();
        jPanel1.repaint();

        jButton7.setBackground(bg);

        jButton7.setForeground(fc);
        jButton7.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/supw.png")));

        jButton3.setForeground(fcd);
        jButton4.setForeground(fcd);
        jButton5.setForeground(fcd);
        jButton6.setForeground(fcd);
        jButton1.setForeground(fcd);

        jButton3.setBackground(null);
        jButton4.setBackground(null);
        jButton5.setBackground(null);
        jButton6.setBackground(null);
        jButton1.setBackground(null);

        jButton3.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/medb.png")));
        jButton4.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/bilb.png")));
        jButton5.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/grnb.png")));
        jButton6.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/stockb.png")));
        jButton1.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/homeb.png")));
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        new LogIn().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        InputStream is = LogIn.class.getResourceAsStream("/theam/Material Lighter.theme.json");
        IntelliJTheme.setup(is);
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Phamarcy().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    public javax.swing.JButton jButton3;
    public javax.swing.JButton jButton4;
    public javax.swing.JButton jButton5;
    public javax.swing.JButton jButton6;
    public javax.swing.JButton jButton7;
    public javax.swing.JButton jButton8;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
