/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.InputStream;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JREmptyDataSource;
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
public class test {

    public static void main(String[] args) {
        //        String input = "1:2:12 PM";
        //        //Format of the date defined in the input String
        //        DateFormat df = new SimpleDateFormat("hh:mm:ss aa");
        //        //Desired format: 24 hour format: Change the pattern as per the need
        //        DateFormat outputformat = new SimpleDateFormat("HH:mm:ss");
        //        java.util.Date date = null;
        //        String output = null;
        //        try {
        //            //Converting the input String to Date
        //            date = df.parse(input);
        //            System.out.println(df.format(date));
        //            //Changing the format of date and storing it in String
        //            output = outputformat.format(date);
        //            //Displaying the date
        //            System.out.println(output);
        //        } catch (ParseException pe) {
        //            pe.printStackTrace();
        //        }

        //        if (!Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$").matcher("janchamika1@gmail.com").matches()) {
        //            System.out.println("wrong");
        //        }
        //        System.out.println(System.currentTimeMillis());
        //        SimpleDateFormat stf2 = new SimpleDateFormat("HH:mm:ss");
        //        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        //
        //        try {
        //            //        try {
        ////            if (df.parse("13:00:00").before(df.parse("11:00:00"))) {
        ////                System.out.println("don");
        ////            }else{
        ////                System.out.println("no");
        ////            }
        ////
        ////        } catch (ParseException ex) {
        ////            ex.printStackTrace();
        ////        }
        //            System.out.println(df.parse(df.format(stf2.parse("19:00:00"))));
        //        } catch (ParseException ex) {
        //            ex.printStackTrace();
        //        }
        //        Vector v=new Vector();
        //        v.add("A");
        //        v.add("B");
        //        HashMap hm=new HashMap();
        //        hm.put("A", "st");
        //        
        //        System.out.println(hm.get("A"));
        //        
        ////        System.out.println(v.contains("B"));
        //        if(v.contains("B")){
        //            System.out.println("ok");
        //        }
        //        SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
        //
        //        System.out.println(datef.format(new java.util.Date())); 
//        String reference = "1657259862319-0";
//        String barcode = "1657259862319-0";
//        String todate = "2021-32-22";
//        String specialty = "nofdsa";
//        String doctor = "fdsafds";
//        String pname = "fdafsda";
//        String pphone = "8436285";
//        String apdate = "2021-00-21";
//        String aptime = "4:00PM to 4:00PM";
//        String fee = "100.00";
//        String payment = "200.00";
//        String balance = "3232.00";
//
//        try {
//            String path = "src//reports//GRN.jrxml";
//            JasperReport jr = JasperCompileManager.compileReport(path);
//
//            HashMap parameters = new HashMap();
//            parameters.put("code", reference);
//            parameters.put("barcode", reference);
//            parameters.put("todate", apdate);
//            parameters.put("sname", pname);
//            parameters.put("scontact", pname);
//
//            parameters.put("cphone", pname);
//            parameters.put("branch", pname);
//            parameters.put("address", pname);
//            parameters.put("payment", pname);
//            parameters.put("balance", balance);
//            parameters.put("paymethod", pname);
//
//            Vector v = new Vector();
////            v.add(new (pname, barcode, todate));
//
////            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(v);
//            JREmptyDataSource dataSource = new JREmptyDataSource();
//            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, dataSource);
//            JasperViewer.viewReport(jp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            SimpleDateFormat df = new SimpleDateFormat("hh:mm");
////
//            Calendar cal = Calendar.getInstance();
////            Calendar cal2 = Calendar.getInstance();
////            cal.setTime();
////            cal2.setTime(df.parse("11:00"));
//            java.util.Date st = cal.getTime();
////            java.util.Date et = cal2.getTime();
////            System.out.println(st);
////            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
////            Calendar cal = Calendar.getInstance();
//            System.out.println(st);
//        } catch (Exception e) {
//        }
//        DecimalFormat df = new DecimalFormat("0.00");
//
//        System.out.println(df.format(Double.valueOf("300")));
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
//
//        cal.setTime(new java.util.Date());
//        cal.add(Calendar.DAY_OF_MONTH, 30);
//        String dateAfter = datef.format(cal.getTime());
//        System.out.println(dateAfter);
//        SimpleDateFormat month = new SimpleDateFormat("yyyy-MM");
//
//        System.out.println(month.format(new Date()));
        String d = "erew'rew";
        System.out.println(d.replaceAll("'", "\\\\'"));
        
    }

}
