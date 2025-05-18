package com.saasant.firstSpringProject.dao;

import com.saasant.firstSpringProject.vo.ProductInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.saasant.firstSpringProject.util.DBUtil;

public class ProductDao implements ProductDaoInterface{

    public void addProduct(ProductInfo product) {
        String sql = "insert into products (product_id, product_name, product_desc, unit, unit_price, category, taxPercentage, quantity) values (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getInstance().getConnection(); //
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getProductId());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getProductDescription());
            ps.setString(4, product.getUnit());
            ps.setFloat(5, product.getUnitPrice());
            ps.setString(6, product.getCategory());
            ps.setFloat(7, product.getTax());
            ps.setInt(8, product.getQuantity()); 
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); 
            System.out.println("Error in the DB Connectivity during addProduct");
        }
    }

    public ProductInfo getProductById(String productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection(); //
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ProductInfo product = new ProductInfo( //
                        rs.getString("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_desc"),
                        rs.getString("unit"),
                        rs.getFloat("unit_price"),
                        rs.getString("category"),
                        rs.getFloat("taxPercentage")
                );
                if (hasColumn(rs, "quantity")) {
                    product.setQuantity(rs.getInt("quantity"));
                }
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in the DB Connectivity during getProductById");
        }
        return null;
    }

    public List<ProductInfo> getAllProducts() {
        List<ProductInfo> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        // Changed to use singleton instance
        try (Connection conn = DBUtil.getInstance().getConnection(); //
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ProductInfo product = new ProductInfo( //
                		rs.getString("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_desc"),
                        rs.getString("unit"),
                        rs.getFloat("unit_price"),
                        rs.getString("category"),
                        rs.getFloat("taxPercentage")
                );
                
                 if (hasColumn(rs, "quantity")) {
                    product.setQuantity(rs.getInt("quantity"));
                }
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in the DB Connectivity during getAllProducts");
        }
        return products;
    }

    public boolean updateProduct(ProductInfo product) {
        String sql = "UPDATE products SET product_name = ?, product_desc = ?, unit = ?, unit_price = ?, category = ?, taxPercentage = ?, quantity = ? WHERE product_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection(); //
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getProductName());
            ps.setString(2, product.getProductDescription());
            ps.setString(3, product.getUnit());
            ps.setFloat(4, product.getUnitPrice());
            ps.setString(5, product.getCategory());
            ps.setFloat(6, product.getTax());
            ps.setInt(7, product.getQuantity());
            ps.setString(8, product.getProductId());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in the DB Connectivity during updateProduct");
        }
        return false;
    }

    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equalsIgnoreCase(rsmd.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }
}