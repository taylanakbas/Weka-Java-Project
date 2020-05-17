# Weka-Java-Project
Evaluation 

.xls => ALGORITHM_NAME + TABLE_NAME + (TRAIN || TEST)

FOR THIS EXECUTION:
#of Algorithm : 20
#of Tables : 4
Total .xls file = 20 * 4 * (TRAIN or TEST) = 160


Evaluation 

Best algorithm for entire system 
    
    1 - Calculate to score of TRAIN.xls using all version's (all,a,b,c,d,e) avg Values
    2 - Calculate to score of TEST.xls using all version's (all,a,b,c,d,e) avg Values
    3 - Calculate to score of TEST.xls using TRAIN.xls score and TEST.xls score to find avg value of ALGORITHM_TABLE
    4 - Calculate to score of algorithm using avg of all tables for one algorithm.
    5 - Compare algorithm's avg Values
    6 - Pick the best

Best algorithm for tables

    1 - Calculate to avg value of TRAIN.xls using all version's (all,a,b,c,d,e) avg Values
    2 - Calculate to avg value of TEST.xls using all version's (all,a,b,c,d,e) avg Values
    3 - Calculate to avg value of TEST.xls and TRAIN.xls to find avg value of ALGORITHM_TABLE
    4 - Compare ALGORITHM_TABLE's avg values in table specific 
    5 - Pick the best 

Best version for chosen best algorithm

    1 - Compare all version's avg values
    2 - Pick the best

Best variation of user for chosen best version

    1 - Compare all user's variation values
    2 - Pick the best for all users
 
