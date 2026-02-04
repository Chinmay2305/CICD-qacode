**INVENTORIES Filter check for row grouping:**

**CHILD-ROWS		FILTER OPTION(Y/N)?**

Serial Number		Y

QTY Available		Y

 	QTY OH			N

 	QTY Reserved		N

Application Code	Y

Condition Code		Y

Status			Y

 	MSN/ESN			N

Stock Visible		Y

 	Stock Line		N

 	WHS Description		N



**SO WHAT TO TEST???**

\- Common fields in Parent and Child Row - Is the filtered data correct at both places?	***DONE***

  -Serial number, Application Code, Condition Code, Status, MSN / ESN, Stock Visible

  -Y		  Y		    Y		    Y	    N	       Y		 - WHICH HAS A FILTER AND WHICH DOES NOT??



\- Fields only in Child row - Are they filtered correctly?

  -QTY OH, QTY Reserved, Stock Line, WHS Description - ***THESE ALL DON'T HAVE FILTER OPTIONS SO IGNORE***





\- For QTY in Stock - Is it equal to total of QTY Available from child rows?



**STEPS**

1. First, apply a normal filter to get lesser data (if possible, test on 3-5 rows only)
2. Provide a loop to click on each expand icon and simultaneously, check child rows, like this:

 	Check Parent row data

 	Click to expand

 	Check Child row data

 	Click again to collapse

 	Go to next row

3\. Make different methods for each test case written above



EXCEL FORMULA TO REMOVE DIGITS FROM " +" ONWARDS

=IF(ISNUMBER(VALUE(RIGHT(J1159,LEN(J1159) - FIND("+",J1159))) \* (LEFT(RIGHT(J1159,LEN(J1159) - FIND("+",J1159)),1)<>" ")), LEFT(J1159,FIND("+",J1159)-1), J1159)

