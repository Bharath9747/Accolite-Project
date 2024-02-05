# Symmetric Pairs

You are given a table, Functions, containing two columns: X and Y. Two pairs (X1, Y1) and (X2, Y2) are said to be symmetric pairs if X1 = Y2 and X2 = Y1.

| Column | Type    |
| ------ | ------- |
| X      | Integer |
| Y      | Integer |

Write a query to output all such symmetric pairs in ascending order by the value of X. List the rows such that X1 <= Y1.

### Sample Input

| X   | Y   |
| --- | --- |
| 1   | 2   |
| 3   | 4   |
| 5   | 6   |
| 2   | 1   |

### Sample Output

| X   | Y   |
| --- | --- |
| 1   | 2   |
