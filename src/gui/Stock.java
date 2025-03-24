/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import model.MySQL;

/**
 *
 * @author ROG STRIX
 */
public class Stock extends javax.swing.JPanel {

    DecimalFormat df = new DecimalFormat("0.00");
    SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
    Color b = new Color(84, 110, 122);

    Phamarcy ph;
    Billing bil;
    StockSelect ss;

    /**
     * Creates new form Stock
     */
    public Stock() {
        initComponents();
        jTable1.getTableHeader().setFont(new Font("Airal Nova", Font.PLAIN, 14));
        loadCategory();
        loadBrand();
        loadstock();

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 7; i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        }
        Listner();
        if (LogIn.userType.equals("Pharmacy Cashier")) {
            jPanel3.setVisible(false);
            jButton5.setVisible(false);

        }
     
    }

    public Stock(Phamarcy ph, Billing bil, StockSelect ss) {
        initComponents();
        jTable1.getTableHeader().setFont(new Font("Airal Nova", Font.PLAIN, 14));

        loadCategory();
        loadBrand();
        loadstock();
        this.ph = ph;
        this.bil = bil;
        this.ss = ss;
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 7; i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        }
        Listner();
        if (LogIn.userType.equals("Pharmacy Cashier")) {
            jPanel3.setVisible(false);
            jButton5.setVisible(false);
        }
       
    }

    private void Listner() {
        ListSelectionListener lsl = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int r = jTable1.getSelectedRow();
                if (r != -1) {
                    jStockId.setText(jTable1.getValueAt(r, 0).toString());
                    jsellingPrice.setText(jTable1.getValueAt(r, 6).toString());

                    jButton2.setEnabled(true);

                }
            }
        };
        jTable1.getSelectionModel().addListSelectionListener(lsl);
    }

    private void loadCategory() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `category` ORDER BY `name` ASC");

            Vector v = new Vector();
            v.add("Select");

            while (rs.next()) {
                v.add(rs.getString("name"));
            }

            jCategory1.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBrand() {
        try {
            ResultSet rs = MySQL.search("SELECT * FROM `brand` ORDER BY `name` ASC");

            Vector v = new Vector();
            v.add("Select");

            while (rs.next()) {
                v.add(rs.getString("name"));
            }

            jBrand1.setModel(new DefaultComboBoxModel(v));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadstock() {
        String category = jCategory1.getSelectedItem().toString();
        String brand = jBrand1.getSelectedItem().toString();
        String name = jName.getText();
        Date exdfrom = jexdfrom.getDate();
        Date exdto = jexdto.getDate();
        Date mfdfrom = jmfdfrom.getDate();
        Date mfdto = jmfdto.getDate();
        String minprice = jMinprice.getText();
        String maxprice = jMaxprice.getText();
        String sortBy = jsortby.getSelectedItem().toString();
        try {
            String q = "SELECT * FROM `stock` INNER JOIN `product` ON `stock`.`product_id` =`product`.`id` INNER JOIN `category` ON `product`.`category_id`=`category`.`id` INNER JOIN `brand` ON `product`.`brand_id`=`brand`.`id` INNER JOIN `grn_item` ON `stock`.`id`=`grn_item`.`stock_id` WHERE `stock`.`status_id`='4' AND `stock`.`exd` > '" + datef.format(new Date()) + "' AND `stock`.`qty`>'0' ";

            Vector queryVector = new Vector();
            if (!category.equals("Select")) {
                queryVector.add("`category`.`name`='" + category + "'");
            }
            if (!brand.equals("Select")) {
                queryVector.add("`brand`.`name`='" + brand + "'");
            }
            if (!name.isEmpty()) {
                queryVector.add("`product`.`name` LIKE '" + name + "%' OR `stock`.`id`='" + name + "'");

            }
            if (!minprice.isEmpty()) {
                queryVector.add("`stock`.`selling_price`>='" + Double.valueOf(minprice) + "'");

            }
            if (!maxprice.isEmpty()) {
                queryVector.add("`stock`.`selling_price`<='" + Double.valueOf(maxprice) + "'");

            }
            if (mfdfrom != null) {
                queryVector.add("`stock`.`mfd`>='" + datef.format(mfdfrom) + "'");

            }
            if (mfdto != null) {
                queryVector.add("`stock`.`mfd`<='" + datef.format(mfdto) + "'");

            }
            if (exdfrom != null) {
                queryVector.add("`stock`.`exd`>='" + datef.format(exdfrom) + "'");

            }
            if (exdto != null) {
                queryVector.add("`stock`.`exd`<='" + datef.format(exdto) + "'");

            }
            String order = "product`.`name` ASC";
            if (sortBy.equalsIgnoreCase("ASC By Name")) {
                order = "`product`.`name` ASC";
            }
            if (sortBy.equalsIgnoreCase("DESC By Name")) {
                order = "`product`.`name` DESC";
            }
            if (sortBy.equalsIgnoreCase("ASC By Quantity")) {
                order = "`stock`.`qty` ASC";
            }
            if (sortBy.equalsIgnoreCase("DESC By Quantity")) {
                order = "`stock`.`qty` DESC";
            }
            if (sortBy.equalsIgnoreCase("ASC By EXD")) {
                order = "`stock`.`exd` ASC";
            }
            if (sortBy.equalsIgnoreCase("DESC By EXD")) {
                order = "`stock`.`exd` DESC";
            }
            if (sortBy.equalsIgnoreCase("ASC By Selling Price")) {
                order = "`stock`.`selling_price` ASC";
            }
            if (sortBy.equalsIgnoreCase("DESC By Selling Price")) {
                order = "`stock`.`selling_price` DESC";
            }

            for (int i = 0; i < queryVector.size(); i++) {

                q += " AND ";

                q += queryVector.get(i);

            }
            ResultSet rs = MySQL.search(q + " ORDER BY " + order);
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();

                v.add(rs.getString("stock.id"));
                v.add(rs.getString("product.name"));
                v.add(rs.getString("category.name"));
                v.add(rs.getString("stock.qty"));
                v.add(rs.getString("mfd"));
                v.add(rs.getString("exd"));

                v.add(df.format(Double.valueOf(rs.getString("selling_price"))));
                dtm.addRow(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reset() {
        jStockId.setText("#");
        jsellingPrice.setText("");
        jButton2.setEnabled(false);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jName = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jCategory1 = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        jMinprice = new javax.swing.JTextField();
        jMaxprice = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jsortby = new javax.swing.JComboBox<>();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jmfdfrom = new com.toedter.calendar.JDateChooser();
        jLabel33 = new javax.swing.JLabel();
        jmfdto = new com.toedter.calendar.JDateChooser();
        jexdto = new com.toedter.calendar.JDateChooser();
        jLabel35 = new javax.swing.JLabel();
        jexdfrom = new com.toedter.calendar.JDateChooser();
        jLabel36 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jBrand1 = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jsellingPrice = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        jStockId = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        jMenuItem1.setText("Delete");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Nova", 0, 12))); // NOI18N

        jLabel24.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel24.setText("Search");

        jName.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jName.setToolTipText("");
        jName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jNameKeyReleased(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel26.setText("Category");

        jCategory1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jCategory1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCategory1ItemStateChanged(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel27.setText("Selling Price");

        jMinprice.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jMinprice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jMinpriceKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jMinpriceKeyTyped(evt);
            }
        });

        jMaxprice.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jMaxprice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jMaxpriceKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jMaxpriceKeyTyped(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel28.setText("Max");

        jLabel29.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel29.setText("Sort By");

        jsortby.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jsortby.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ASC By Name", "DESC By Name", "ASC By Quantity", "DESC By Quantity", "ASC By EXD", "DESC By EXD", "ASC By Selling Price", "DESC By Selling Price" }));
        jsortby.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jsortbyItemStateChanged(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel30.setText("Min");

        jLabel31.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel31.setText("Manufacture Date");

        jmfdfrom.setDateFormatString("yyyy-MM-dd");
        jmfdfrom.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jmfdfrom.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jmfdfromPropertyChange(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel33.setText("To");

        jmfdto.setDateFormatString("yyyy-MM-dd");
        jmfdto.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jmfdto.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jmfdtoPropertyChange(evt);
            }
        });

        jexdto.setDateFormatString("yyyy-MM-dd");
        jexdto.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jexdto.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jexdtoPropertyChange(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel35.setText("To");

        jexdfrom.setDateFormatString("yyyy-MM-dd");
        jexdfrom.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jexdfrom.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jexdfromPropertyChange(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel36.setText("Expire Date");

        jButton1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 51, 51));
        jButton1.setText("Reset");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jBrand1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jBrand1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jBrand1ItemStateChanged(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel32.setText("Brand");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addGap(18, 18, 18)
                        .addComponent(jsortby, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jexdfrom, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jexdto, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jmfdfrom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jmfdto, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addGap(13, 13, 13)
                        .addComponent(jMinprice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jMaxprice))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36)
                            .addComponent(jLabel27)
                            .addComponent(jLabel31))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(jLabel24)
                            .addComponent(jLabel32))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jBrand1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jCategory1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jName))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCategory1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBrand1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jMaxprice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jMinprice, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel30)
                        .addComponent(jLabel28)))
                .addGap(10, 10, 10)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jmfdto, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jmfdfrom, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jexdto, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jexdfrom, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jsortby, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock ID", "Medicine Name", "Category", "Quantity", "Manufacture Date", "Expier Date", "Selling Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(204, 204, 204));
        jTable1.setShowGrid(false);
        jTable1.setShowHorizontalLines(true);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTable1MouseEntered(evt);
            }
        });
        jScrollPane10.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
        }

        jButton5.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 204, 204));
        jButton5.setText("Recycle Bin");
        jButton5.setContentAreaFilled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 719, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton5)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jButton5)
                .addGap(11, 11, 11)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Update Selling Price", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Nova", 0, 12))); // NOI18N

        jLabel34.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel34.setText("New Price");

        jsellingPrice.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jsellingPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jsellingPriceKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jsellingPriceKeyTyped(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(51, 51, 51));
        jButton2.setText("Update Stock");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jLabel37.setText("Stock ID");

        jStockId.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jStockId.setText("#");

        jButton3.setFont(new java.awt.Font("Arial Nova", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(51, 51, 51));
        jButton3.setText("Clear");
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
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addComponent(jLabel37))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jsellingPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                            .addComponent(jStockId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jStockId, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jsellingPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jNameKeyReleased
        loadstock();
    }//GEN-LAST:event_jNameKeyReleased

    private void jCategory1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCategory1ItemStateChanged
        loadstock();
    }//GEN-LAST:event_jCategory1ItemStateChanged

    private void jsortbyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jsortbyItemStateChanged
        loadstock();
    }//GEN-LAST:event_jsortbyItemStateChanged

    private void jsellingPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jsellingPriceKeyTyped
        String price = jsellingPrice.getText();

        if (!Pattern.compile("0|0[.]|0[.][0-9]{0,1}|0[.][0-9]{0,1}[1-9]{0,1}|[1-9]+|[1-9]+[0-9]+|[1-9]+[.]|[1-9]+[0-9]*[.][0-9]{0,2}").matcher(price + evt.getKeyChar()).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jsellingPriceKeyTyped

    private void jsellingPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jsellingPriceKeyReleased
        String price = jsellingPrice.getText();

        if (!Pattern.compile("0|0[.]|0[.][0-9]{0,1}|0[.][0-9]{0,1}[1-9]{0,1}|[1-9]+|[1-9]+[0-9]+|[1-9]+[.]|[1-9]+[0-9]*[.][0-9]{0,2}").matcher(price).matches()) {
            jsellingPrice.setText("");

        }
    }//GEN-LAST:event_jsellingPriceKeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (bil != null) {
            if (evt.getClickCount() == 2) {
                int r = jTable1.getSelectedRow();
                try {
                    Date exd = datef.parse(jTable1.getValueAt(r, 5).toString());
                    Calendar cal = Calendar.getInstance();

                    cal.setTime(new Date());
                    cal.add(Calendar.DAY_OF_MONTH, 30);
                    Date dateAfter = cal.getTime();
                    if (exd.before(dateAfter)) {
                        int opt = JOptionPane.showConfirmDialog(this, "This Product will be expired within 30 days. Ensure customer awareness. Is cutomer still want to buy this product?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (opt == JOptionPane.YES_OPTION) {
                            this.bil.jStockId.setText(jTable1.getValueAt(r, 0).toString());
                            this.bil.jMedicine.setText(jTable1.getValueAt(r, 1).toString());
                            this.bil.jLabel28.setText(jTable1.getValueAt(r, 2).toString());
                            int qty = Integer.valueOf(jTable1.getValueAt(r, 3).toString());
                            this.bil.jLabel29.setText(jTable1.getValueAt(r, 3).toString());
                            this.bil.jLabel30.setText(jTable1.getValueAt(r, 4).toString());
                            this.bil.jLabel31.setText(jTable1.getValueAt(r, 5).toString());
                            this.bil.jLabel32.setText(jTable1.getValueAt(r, 6).toString());
                            this.bil.jSpinner1.setModel(new javax.swing.SpinnerNumberModel(1, 1, qty, 1));
                            this.bil.jLabel31.setForeground(Color.red);
                            ss.dispose();
                        }
                    } else {
                        this.bil.jStockId.setText(jTable1.getValueAt(r, 0).toString());
                        this.bil.jMedicine.setText(jTable1.getValueAt(r, 1).toString());
                        this.bil.jLabel28.setText(jTable1.getValueAt(r, 2).toString());
                        int qty = Integer.valueOf(jTable1.getValueAt(r, 3).toString());
                        this.bil.jLabel29.setText(jTable1.getValueAt(r, 3).toString());
                        this.bil.jLabel30.setText(jTable1.getValueAt(r, 4).toString());
                        this.bil.jLabel31.setText(jTable1.getValueAt(r, 5).toString());
                        this.bil.jLabel32.setText(jTable1.getValueAt(r, 6).toString());
                        this.bil.jSpinner1.setModel(new javax.swing.SpinnerNumberModel(1, 1, qty, 1));
                        this.bil.jLabel31.setForeground(b);

                        ss.dispose();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        if (jTable1.getSelectedRow() != -1) {
            if (evt.getButton() == 3 && !LogIn.userType.equals("Pharmacy Cashier")) {
                jPopupMenu1.show(jTable1, evt.getX(), evt.getY());

            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String id = jStockId.getText();
        String price = jsellingPrice.getText();

        if (price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter new selling price", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {
            MySQL.iud("UPDATE `stock` SET `selling_price`='" + price + "' WHERE `id`='" + id + "'");
            reset();
            loadstock();

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        reset();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMinpriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jMinpriceKeyTyped
        String price = jMinprice.getText();

        if (!Pattern.compile("0|0[.]|0[.][0-9]{0,1}|0[.][0-9]{0,1}[1-9]{0,1}|[1-9]+|[1-9]+[0-9]+|[1-9]+[.]|[1-9]+[0-9]*[.][0-9]{0,2}").matcher(price + evt.getKeyChar()).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jMinpriceKeyTyped

    private void jMaxpriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jMaxpriceKeyTyped
        String price = jMaxprice.getText();

        if (!Pattern.compile("0|0[.]|0[.][0-9]{0,1}|0[.][0-9]{0,1}[1-9]{0,1}|[1-9]+|[1-9]+[0-9]+|[1-9]+[.]|[1-9]+[0-9]*[.][0-9]{0,2}").matcher(price + evt.getKeyChar()).matches()) {
            evt.consume();
        }
    }//GEN-LAST:event_jMaxpriceKeyTyped

    private void jMinpriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jMinpriceKeyReleased
        String price = jMinprice.getText();

        if (!Pattern.compile("0|0[.]|0[.][0-9]{0,1}|0[.][0-9]{0,1}[1-9]{0,1}|[1-9]+|[1-9]+[0-9]+|[1-9]+[.]|[1-9]+[0-9]*[.][0-9]{0,2}").matcher(price).matches()) {
            jMinprice.setText("");

        }
        loadstock();
    }//GEN-LAST:event_jMinpriceKeyReleased

    private void jMaxpriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jMaxpriceKeyReleased
        String price = jMaxprice.getText();

        if (!Pattern.compile("0|0[.]|0[.][0-9]{0,1}|0[.][0-9]{0,1}[1-9]{0,1}|[1-9]+|[1-9]+[0-9]+|[1-9]+[.]|[1-9]+[0-9]*[.][0-9]{0,2}").matcher(price).matches()) {
            jMaxprice.setText("");

        }
        loadstock();
    }//GEN-LAST:event_jMaxpriceKeyReleased

    private void jmfdfromPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jmfdfromPropertyChange
        loadstock();
    }//GEN-LAST:event_jmfdfromPropertyChange

    private void jmfdtoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jmfdtoPropertyChange
        loadstock();
    }//GEN-LAST:event_jmfdtoPropertyChange

    private void jexdfromPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jexdfromPropertyChange
        loadstock();
    }//GEN-LAST:event_jexdfromPropertyChange

    private void jexdtoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jexdtoPropertyChange
        loadstock();
    }//GEN-LAST:event_jexdtoPropertyChange

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jCategory1.setSelectedIndex(0);
        jName.setText("");
        jexdfrom.setDate(null);
        jexdto.setDate(null);
        jmfdfrom.setDate(null);
        jmfdto.setDate(null);
        jMinprice.setText("");
        jMaxprice.setText("");
        jsortby.setSelectedIndex(0);
        jBrand1.setSelectedIndex(0);
        loadstock();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jBrand1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jBrand1ItemStateChanged
        loadstock();
    }//GEN-LAST:event_jBrand1ItemStateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        new StockRecycleBin(ph, true, this).setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (jTable1.getSelectedRow() != -1) {

            String id = jStockId.getText();

            MySQL.iud("UPDATE `stock` SET `status_id`='7' WHERE `id`='" + id + "'");

            reset();
            loadstock();

        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jTable1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseEntered


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jBrand1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jCategory1;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JTextField jMaxprice;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JTextField jMinprice;
    private javax.swing.JTextField jName;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JLabel jStockId;
    private javax.swing.JTable jTable1;
    private com.toedter.calendar.JDateChooser jexdfrom;
    private com.toedter.calendar.JDateChooser jexdto;
    private com.toedter.calendar.JDateChooser jmfdfrom;
    private com.toedter.calendar.JDateChooser jmfdto;
    private javax.swing.JTextField jsellingPrice;
    private javax.swing.JComboBox<String> jsortby;
    // End of variables declaration//GEN-END:variables
}
