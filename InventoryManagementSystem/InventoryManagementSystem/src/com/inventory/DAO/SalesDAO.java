/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.DAO;

import com.inventory.DTO.SalesDTO;
import com.inventory.Database.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author akshitha
 */
public class SalesDAO {
    
    Connection conn = null;
    PreparedStatement prepStatement= null;
    Statement statement = null;
    ResultSet resultSet = null;
    
    public SalesDAO() {
        try {
            conn = new ConnectionFactory().getConn();
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void addSalesDAO(SalesDTO salesDTO) {
        try {
            String query = "SELECT * FROM customers WHERE customercode='"
                    +salesDTO.getCustomerId()
                    + "' AND productcode='"
                    +salesDTO.getProductId()
                    + "' AND salesid='"
                    +salesDTO.getSalesId()
                    + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next())
                JOptionPane.showMessageDialog(null, "Order already placed");
            else
                addFunction(salesDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addFunction(SalesDTO sales) {
        try {
            String query = "INSERT INTO salesinfo VALUES(null,?,?,?,?,?,?,?,?)";
            prepStatement = (PreparedStatement) conn.prepareStatement(query);
            prepStatement.setInt(1, sales.getSuppid());
            prepStatement.setString(2, sales.getDate());
            prepStatement.setInt(3, sales.getProductId());
            prepStatement.setInt(4, sales.getCustomerId());
            prepStatement.setDouble(5, sales.getQuantity());
            prepStatement.setDouble(6, sales.getAmount());
            prepStatement.setString(7, sales.getDeliveryAgent());
            prepStatement.setString(8, " To be Processed");
  
//            String query2 = "INSERT INTO currentstock VALUES(?,?)";
//            prepStatement2 = conn.prepareStatement(query2);
//            prepStatement2.setString(1, productDTO.getProdCode());
//            prepStatement2.setInt(2, productDTO.getQuantity());

            prepStatement.executeUpdate();
//            prepStatement2.executeUpdate();
            JOptionPane.showMessageDialog(null, "Product ordered.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    
    public void updateSalesQuantity( int sid,int val, int single) {
        try {
            String query ="UPDATE salesinfo SET quantity =?, amount=? WHERE salesid =?";        
//            statement.executeQuery(query);
            prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, val);
            prepStatement.setInt(2, val*single);
            prepStatement.setInt(3, sid);
            prepStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Quantity has been updated");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
//        return resultSet;
    }
    
    
    public DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        Vector<String> columnNames = new Vector<String>();
        int colCount = metaData.getColumnCount();

        for (int col=1; col <= colCount; col++){
            columnNames.add(metaData.getColumnName(col).toUpperCase(Locale.ROOT));
        }

        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (resultSet.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int col=1; col<=colCount; col++) {
                vector.add(resultSet.getObject(col));
            }
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }
    
    public ResultSet getQueryResult() {
        try {
            String query = "SELECT salesid,customercode,productcode,amount FROM salesinfo ORDER BY salesid";
            resultSet = statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }
    
    public ResultSet getQueryResult_sid(int cust) {
        try {
            String query = "SELECT A.salesid,A.userid,B.productname,A.quantity,A.amount,B.Brand,A.status from salesinfo A \n" +
"INNER JOIN products B on A.productid=B.pid WHERE userid ='"+cust+"'";
            resultSet = statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet getQueryResult_delivered(int cust) {
        try {
            String query = "SELECT A.salesid,A.userid,B.productname,A.quantity,A.amount,B.Brand from salesinfo A \n" +
"INNER JOIN products B on A.productid=B.pid AND A.status= 'DELIVERED' AND  A.userid ='"+cust+"'";
            resultSet = statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }
    public void addRating(int salesid,int userid, String productname, String brand, int rate) {
        try {
            String query = "SELECT * FROM ratings WHERE salesid='"
                    +salesid
                    + "' AND userid='"
                    +userid
                    + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next())
                UpdateRating(salesid,rate);
            else
                addFunctionRating(salesid,userid,productname,brand,rate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void UpdateRating(int salesid,int rate) {
        try {
            String query ="UPDATE ratings SET rate =? WHERE salesid =?";        
//            statement.executeQuery(query);
            prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, rate);
            prepStatement.setInt(2, salesid);
            prepStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Thank you for Rating");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
//        return resultSet;
    }
    
    public void addFunctionRating(int salesid,int userid, String productname, String brand, int rate) {
        try {
            String query = "INSERT INTO ratings VALUES(?,?,?,?,?)";
            prepStatement = (PreparedStatement) conn.prepareStatement(query);
            prepStatement.setInt(1, salesid);
//            prepStatement.setDouble(1, sales.getSalesId());
            prepStatement.setInt(2, userid);
            prepStatement.setString(3, productname);
            prepStatement.setString(4,brand);
            prepStatement.setInt(5,rate );
            prepStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Thank you for the Rating.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    
}
