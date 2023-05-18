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
                JOptionPane.showMessageDialog(null, "Customer already exists.");
            else
                addFunction(salesDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addFunction(SalesDTO sales) {
        try {
            String query = "INSERT INTO products VALUES(null,?,?,?,?,?)";
            prepStatement = (PreparedStatement) conn.prepareStatement(query);
            prepStatement.setDouble(1, sales.getSalesId());
            prepStatement.setString(2, sales.getDate());
            prepStatement.setString(3, sales.getProductId());
            prepStatement.setString(4, sales.getCustomerId());
            prepStatement.setDouble(5, sales.getQuantity());
            prepStatement.setString(5, sales.getDeliveryAgent());
            

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
    
}
