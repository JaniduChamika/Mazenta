/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class FrontDesk extends javax.swing.JFrame {

    Color bg = new Color(0, 204, 204);
    Color fc = new Color(255, 255, 255);
    Color fcd = new Color(51, 51, 51);

    /**
     * Creates new form FrontDesk
     */
    public FrontDesk() {
        initComponents();
        Appointment panel = new Appointment(this);
        jPanel1.removeAll();
        jPanel1.add(panel);
        jPanel1.revalidate();
        jPanel1.repaint();
        jButton3.setBackground(bg);
        jButton3.setForeground(fc);
        jButton3.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/apw.png")));
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
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Front Desk");
        setMinimumSize(new java.awt.Dimension(1110, 740));
        setPreferredSize(new java.awt.Dimension(1110, 740));

        jPanel2.setLayout(new java.awt.GridLayout(1, 0, 5, 5));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Arial Nova", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(51, 51, 51));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resourse/icon/apb.png"))); // NOI18N
        jButton3.setText("Appointment");
        jButton3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 204), 1, true));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setFont(new java.awt.Font("Arial Nova", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(51, 51, 51));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resourse/icon/labb.png"))); // NOI18N
        jButton4.setText("Medical Test");
        jButton4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 204), 1, true));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4);

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Arial Nova", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(51, 51, 51));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resourse/icon/loutb.png"))); // NOI18N
        jButton5.setText("Logout");
        jButton5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 204, 204), 1, true));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5);

        jPanel1.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1110, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Appointment panel = new Appointment(this);
        jPanel1.removeAll();
        jPanel1.add(panel);
        jPanel1.revalidate();
        jPanel1.repaint();
        jButton3.setBackground(bg);
        jButton3.setForeground(fc);
        jButton4.setForeground(fcd);
        jButton5.setForeground(fcd);
        jButton4.setBackground(null);
        jButton5.setBackground(null);
        jButton3.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/apw.png")));
        jButton4.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/labb.png")));

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        MedicalTest panel = new MedicalTest();
        jPanel1.removeAll();
        jPanel1.add(panel);
        jPanel1.revalidate();
        jPanel1.repaint();
        jButton4.setBackground(bg);
        jButton4.setForeground(fc);
        jButton3.setForeground(fcd);
        jButton5.setForeground(fcd);
        jButton3.setBackground(null);
        jButton5.setBackground(null);
        jButton4.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/labw.png")));
        jButton3.setIcon(new ImageIcon(getClass().getResource("/resourse/icon/apb.png")));

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        new LogIn().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        InputStream is = LogIn.class.getResourceAsStream("/theam/Material Lighter.theme.json");
        IntelliJTheme.setup(is);

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrontDesk().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
