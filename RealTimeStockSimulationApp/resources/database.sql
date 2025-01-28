-- Create a file named database.sql in the resources/ directory for setting up the database schema.
-- Database: stock
CREATE DATABASE IF NOT EXISTS stock;

USE stock;

-- Table: stocks
CREATE TABLE IF NOT EXISTS stocks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL
);

