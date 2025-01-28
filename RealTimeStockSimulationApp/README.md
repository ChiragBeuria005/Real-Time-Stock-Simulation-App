# Real-Time Stock Simulation App  

## Description  
The Real-Time Stock Simulation App is a Java-based GUI application that simulates a stock trading environment. It allows users to buy and sell stocks, track price changes in real time, and manage their portfolio. The application is backed by a MySQL database for persistence and features a user-friendly interface with real-time updates.  

---

## Features  
- **Real-Time Stock Price Updates:** Stock prices fluctuate dynamically every second.  
- **Buy and Sell Stocks:** Users can trade stocks, and their portfolio is updated accordingly.  
- **Balance Management:** Users can add or withdraw funds.  
- **Database Integration:** All transactions are stored in a MySQL database.  
- **Dark-Themed GUI:** Modern and user-friendly interface using the Nimbus look and feel.  

---

## Prerequisites  
Before running the project, ensure you have the following installed:
- Java Development Kit (JDK) (version 8 or higher)
- MySQL Server
- IDE (e.g., IntelliJ IDEA, Eclipse, or NetBeans)

---


Set up the MySQL database:
Import the resources/database.sql file to create the stock database and the stocks table.
Update the database connection credentials in the Java file (RealTimeStockSimulationApp.java):

String jdbcURL = "jdbc:mysql://localhost:3306/stock";
String username = "root"; // Replace with your MySQL username
String password = "yourpassword"; // Replace with your MySQL password

Compile and run the application