To create the database copy/paste the below script in MySQL shell
CREATE DATABASE expense_tracker;
USE expense_tracker;

CREATE TABLE expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(50),
    amount DECIMAL(10,2),
    expense_date DATE,
    note VARCHAR(255)
);

...and add the mysql connector jar file to your project compile and run.
You are good to go!!
