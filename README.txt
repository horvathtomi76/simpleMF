Running the algorithm for simple matrix factorization with the following command:
java -jar MF.jar [parameters]

The parameters must be in the following form and order:

1) DATA=datafile~PARAM_CONSTR=parameter_constraints_file~discretize=number1~validation_randseed=number2~randseed=number3 dbserver database user password number4 parameter_generation_type output

-- datafile: a text file, a sparse representation of a matrix, in which each line contains the row_id, column_id and the cell_value separated by tab

-- parameter_constraints_file: a text file containing the definition of ranges for parameters used in hyper-parameter generation. Each line of the file contains the name of the parameter and the minimal and maximal values as well as the step size in which random values can be generated. An example of the content of the file is the following

hp	min	max	step_size
K	5	200	5
ALPHA	0.005	0.3	0.005
ALPHA_W	0.005	0.3	0.005
ALPHA_H	0.005	0.3	0.005
ALPHA_user_bias	0.005	0.3	0.005
ALPHA_item_bias	0.005	0.3	0.005
LAMBDA	0.05	0.5	0.05
I	100	1000	50
EPSILON	0.0000005	0.000001	0.0000005

-- number1: the integer number of different classes into which cell values should be discretized

-- number2 and number3: integers, random seeds for generating validation sets and the randseed used for other procedures

-- number4: a floating point value, the ratio of the validation set, i.e. 0.01f means that 1% of the data will be used for validation

-- parameter_generation_type: a string which can be "random", "stepwise" or "none"

-output: a strng which defines what should be written on the output, can be "outputIter", "outputW", "outputH" or "outputWH". This parameter can be omitted.

IMPORTANT: instead of using the file for defining the parameters, these can be added into the arguments as is done in case of the "discretize" parameter. An example of calling the program with direct definition of parameters is the following:

java -jar MF.jar DATA=data/A19/A19_PCG2.txt~K=207~ALPHA=0.0016~LAMBDA=0.0428~I=1500~EPSILON=0.0000001~discretize=5 localhost mydatabase root password 0.01f none outputIter

