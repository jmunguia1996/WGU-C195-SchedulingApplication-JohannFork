package dataAccessObject;

import applicationObject.Appointment;
import applicationObject.Customer;
import applicationTools.CChoulesDevTools;
import applicationTools.JDBTools;
import controller.Reports;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class CustomerDAO {

    public static ObservableList<Customer> getAllCustomers() throws SQLException {

        String query = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, customers.Division_ID, first_level_divisions.Division, customers.Create_Date from customers INNER JOIN  first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID";



        String queryForCountryName = "SELECT Country FROM countries WHERE Country_ID = (SELECT Country_ID FROM first_level_divisions WHERE Division_ID = ?)";

        PreparedStatement preparedStatement = JDBTools.getConnection().prepareStatement(query);
        PreparedStatement preparedStatementForCountryName = JDBTools.getConnection().prepareStatement(queryForCountryName);
        ResultSet resultSet = preparedStatement.executeQuery();

        ObservableList<Customer> customersObservableList = FXCollections.observableArrayList();

        //TODO [l]TRY Lambda expression here
        while (resultSet.next()) {
            int customerId = resultSet.getInt("Customer_ID");
            int divisionId = resultSet.getInt("Division_ID");
            String customerName = resultSet.getString("Customer_Name");
            String customerAddress = resultSet.getString("Address");
            String customerPostalCode = resultSet.getString("Postal_Code");
            String customerPhone = resultSet.getString("Phone");
            String divisionName = resultSet.getString("Division");
            preparedStatementForCountryName.setString(1, String.valueOf(divisionId));
            ResultSet resultSetCountry = preparedStatementForCountryName.executeQuery();

            LocalDateTime createDate;
            try {
                createDate = resultSet.getTimestamp("Create_Date").toLocalDateTime();
            } catch (NullPointerException e) {
                CChoulesDevTools.println(e.toString());
                createDate = null;
            }

            String countryName = "No Country Found";
            while (resultSetCountry.next()) {
                countryName = resultSetCountry.getString("Country");
                //TODO [Extra] set a marker to check for multiple values in case of a bug
            }

            Customer customer = new Customer(
                            customerId,
                            customerName,
                            customerAddress,
                            customerPostalCode,
                            customerPhone,
                            divisionId,
                            divisionName,
                            countryName,
                            createDate
            );

            customersObservableList.add(customer);

        }



        return customersObservableList;

    }

    public static int deleteCustomer(int customerId, Connection connection) throws SQLException {
        String query = "DELETE FROM customers WHERE Customer_ID=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, customerId);
        int result = preparedStatement.executeUpdate();
        preparedStatement.close();
        return result;
    }

    public static int updateCustomer(Customer updatedCustomer, Connection connection) throws SQLException {
        String query = "UPDATE customers SET " +
                "Customer_Name=?, " +
                "Address=?, " +
                "Postal_Code=?, " +
                "Phone=?, " +
                "Division_ID=? " +
                "Create_Date=? " +
                "WHERE Customer_ID=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, updatedCustomer.getCustomerName());
        preparedStatement.setString(2, updatedCustomer.getCustomerAddress());
        preparedStatement.setString(3, updatedCustomer.getCustomerPostalCode());
        preparedStatement.setString(4, updatedCustomer.getCustomerPhoneNumber());
        preparedStatement.setInt(5, updatedCustomer.getCustomerDivisionId());
        preparedStatement.setString(6, String.valueOf(Timestamp.valueOf(updatedCustomer.getCreateDate())));
//TODO [ip] time stamp value of not setting this value
        preparedStatement.setInt(7, updatedCustomer.getCustomerId());

        int result = preparedStatement.executeUpdate();

        //Closing to not tie up DB Resources
        preparedStatement.close();
        return result;
    }
    public static int addCustomer(Customer addedCustomer, Connection connection) throws SQLException {
        String query = "INSERT INTO customers (" +
                "Customer_Name, " +
                "Address, " +
                "Postal_Code, " +
                "Phone, " +
                "Division_ID) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, addedCustomer.getCustomerName());
        preparedStatement.setString(2, addedCustomer.getCustomerAddress());
        preparedStatement.setString(3, addedCustomer.getCustomerPostalCode());
        preparedStatement.setString(4, addedCustomer.getCustomerPhoneNumber());
        preparedStatement.setInt(5, addedCustomer.getCustomerDivisionId());


        int result = preparedStatement.executeUpdate();

        //Closing to not tie up DB Resources
        preparedStatement.close();
        return result;
    }
}

