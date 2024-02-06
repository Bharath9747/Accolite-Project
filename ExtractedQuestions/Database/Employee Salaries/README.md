# Employee Salaries

Write a query that prints a list of employee names (i.e.: the name attribute) for employees in Employee having a salary greater than $2500 per month who have been employees for less than 10 months. Sort your result by ascending employee_id.

### Input Format

The Employee table containing employee data for a company is described as follows:
| Column | Type |
| -------- | ------- |
| employee_id | Integer |
| name | String |
| months | Integer |
| salary | Integer |

where employee_id is an employee's ID number, name is their name, months is the total number of months they've been working for the company, and salary is the their monthly salary.

### Sample Input

| Employee ID | Name        | Months | Salary |
| ----------- | ----------- | ------ | ------ |
| 1           | John Doe    | 12     | 4500   |
| 2           | Jane Smith  | 8      | 3000   |
| 3           | Bob Johnson | 15     | 5000   |
| 4           | Alice Brown | 5      | 2500   |

### Sample Output

|             |
| ----------- |
| Jane Smith  |
| Alice Brown |
