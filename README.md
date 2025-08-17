# Library Management System 2.0

A comprehensive desktop application for managing a library's books, members, and transactions. Built with Java Swing and connected to a MySQL database, this system provides distinct interfaces for administrators and members.
An improved version compared to version 1.0 (C++).

## Features

- **User Roles**: Separate login and dashboards for Admins and Members.
- **Graphical User Interface**: A user-friendly and intuitive GUI built with Java Swing.
- **Database Integration**: All data is stored and managed in a MySQL database.

### Admin Functionality
- **Member Management**:
  - Add, update, delete, and view all library members.
  - Search for specific members by ID.
- **Book Management**:
  - Add, update, delete, and view the entire book inventory.
  - Search for specific books by ID.
- **Transaction Management**:
  - Process book borrowing and returns on behalf of members.
  - View and delete transaction records.

### Member Functionality
- **Book Transactions**:
  - Borrow available books from the library.
  - Return borrowed books.
- **View History**:
  - View a complete history of personal transactions.

## Tech Stack

- **Frontend**: Java Swing
- **Backend**: Java
- **Database**: MySQL with JDBC driver

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- MySQL Server
- An IDE (e.g., IntelliJ IDEA, Eclipse)
- MySQL Connector/J (JDBC Driver)

## Setup and Installation

### 1. Clone the Repository

```bash
git clone https://github.com/dedric07/library-management-2.0.git
```

### 2. Database Configuration

1.  Connect to your MySQL server and create a new database named `member_schema`.
    ```sql
    CREATE DATABASE member_schema;
    ```
2.  Use the created database.
    ```sql
    USE member_schema;
    ```
3.  Execute the following SQL commands to create the required tables:

    **Admin Table**
    ```sql
    CREATE TABLE admin (
        admin_id INT PRIMARY KEY AUTO_INCREMENT,
        admin_name VARCHAR(45) NOT NULL UNIQUE,
        admin_password VARCHAR(45) NOT NULL
    );
    ```

    **Member Table**
    ```sql
    CREATE TABLE member (
        member_id INT PRIMARY KEY,
        member_name VARCHAR(45) NOT NULL,
        contact_details VARCHAR(45),
        member_password VARCHAR(45) NOT NULL
    );
    ```

    **Book Table**
    ```sql
    CREATE TABLE book (
        book_id INT PRIMARY KEY AUTO_INCREMENT,
        book_title VARCHAR(45) NOT NULL,
        book_author VARCHAR(45),
        publish_year INT,
        book_quantity INT,
        book_genre VARCHAR(45)
    );
    ```

    **Transaction Table**
     ```sql
    CREATE TABLE transaction (
        transaction_id INT PRIMARY KEY AUTO_INCREMENT,
        member_id INT,
        book_title VARCHAR(45),
        borrow_quantity INT,
        borrow_date VARCHAR(45),
        return_date VARCHAR(45),
        status VARCHAR(45),
        FOREIGN KEY (member_id) REFERENCES member(member_id)
            ON DELETE CASCADE
    );
    ```
4.  (Optional) Create a default admin user to log in for the first time:
    ```sql
    INSERT INTO ADMIN (admin_name, admin_password) VALUES ('admin', 'admin123');
    ```

### 3. Configure Database Connection

1.  Open the project in your IDE.
2.  Navigate to the `Database.java` file.
3.  Update the `URL`, `User`, and `Password` constants with your MySQL database credentials.

    ```java
    // File: Database.java
    public class Database {
        private static final String URL = "jdbc:mysql://127.0.0.1:3306/member_schema"; // Ensure host and port are correct
        private static final String User = "your_mysql_username"; // Replace with your username
        private static final String Password = "your_mysql_password"; // Replace with your password
        // ...
    }
    ```

### 4. Add JDBC Driver

Download the MySQL Connector/J and add the `.jar` file to your project's build path or classpath in your IDE.

## How to Run the Application

1.  Ensure all setup steps above are completed.
2.  Compile the project files.
3.  Run the `main` method in the `LoginInterface.java` file.
4.  The login window will appear. You can log in using the admin credentials you created (e.g., `admin` / `admin123`) or after creating a member through the admin dashboard.
