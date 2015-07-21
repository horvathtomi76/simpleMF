package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Class for handling parameters of the program
 * @author Tomas Horvath
 *
 */
public class Parameters {

	public int validation_randseed = 0;	// rand seed for validation set -- it must be equal for all the launches of the program to ensure the same test set
	
	public int train_randseed=0;	// rand_seed used in training
	public Random random;	// random number generator
	
	public String DATA;	// data file the triples are stored in
	public String PARAM_CONSTR;	// file the constraints on parameters are defined in

	public int K;	// number of factors
	public int[] K_constraints;
	public float ALPHA;	//step size
	public float[] ALPHA_constraints;
	public float LAMBDA;	// regularization constant
	public float[] LAMBDA_constraints;
	public int I;	// maximal number of iterations
	public int[] I_constraints;
	public float EPSILON;	// minimal convergence error threshold
	public float[] EPSILON_constraints;
	
	public float ALPHA_W;
	public float[] ALPHA_W_constraints;
	public float ALPHA_H;
	public float[] ALPHA_H_constraints;
	public float ALPHA_user_bias;
	public float[] ALPHA_user_bias_constraints;
	public float ALPHA_item_bias;
	public float[] ALPHA_item_bias_constraints;
	
	public float sigmoid_steepness = 1f;
	public float[] sigmoid_steepness_constraints;
	
	public float init_mean=0f;	// mean value for initialization of the factor matrices
	public float init_std_dev=0.01f;	// standard deviation for initialization of the factor matrices
	
	public int discretize;	// number of intervals for discretization
	
	@Override
	public String toString() {
		return "validation_randseed=" + validation_randseed + " randseed=" + train_randseed + " discretize=" + discretize + " K=" + K + " ALPHA="
				+ ALPHA + " LAMBDA=" + LAMBDA + " I=" + I + " EPSILON="
				+ EPSILON + " init_mean=" + init_mean + " init_std_dev="
				+ init_std_dev;
	}
	
	public Parameters(String parameter) {
		String[] parameters = parameter.split("~");
		
		for (String s : parameters) {
			String[] p = s.split("=");
			
			if (p[0].equals("validation_randseed"))
				this.validation_randseed = Integer.valueOf(p[1]);
			else
			if (p[0].equals("train_randseed"))
				this.train_randseed = Integer.valueOf(p[1]);
			else
			if (p[0].equals("discretize"))
				this.discretize = Integer.valueOf(p[1]);
			else
			if (p[0].equals("DATA"))
				this.DATA = p[1];
			else
			if (p[0].equals("PARAM_CONSTR"))
				this.PARAM_CONSTR = p[1];
			else
			if (p[0].equals("K"))
				this.K = Integer.valueOf(p[1]);
			else
			if (p[0].equals("ALPHA"))
				this.ALPHA = Float.valueOf(p[1]);
			else
			if (p[0].equals("LAMBDA"))
				this.LAMBDA = Float.valueOf(p[1]);
			else
			if (p[0].equals("I"))
				this.I = Integer.valueOf(p[1]);
			else
			if (p[0].equals("EPSILON"))
				this.EPSILON = Float.valueOf(p[1]);
			else
			if (p[0].equals("ALPHA_W"))
				this.ALPHA_W = Float.valueOf(p[1]);
			else
			if (p[0].equals("ALPHA_H"))
				this.ALPHA_H = Float.valueOf(p[1]);
			else
			if (p[0].equals("ALPHA_user_bias"))
				this.ALPHA_user_bias = Float.valueOf(p[1]);
			else
			if (p[0].equals("ALPHA_item_bias"))
				this.ALPHA_item_bias = Float.valueOf(p[1]);
			else
			if (p[0].equals("SIGMOID_STEEPNESS"))
				this.sigmoid_steepness = Float.valueOf(p[1]);
		}
		
		this.random = new Random(this.train_randseed);
	}

	public void readConstraintsFromFile() {
		if (PARAM_CONSTR == null || PARAM_CONSTR.equals(""))
			System.out.println("Constraints file was not specified.");
		
		BufferedReader reader = null;
		String row;
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(PARAM_CONSTR))));	
			
			while ((row=reader.readLine()) != null) {				
				String[] splitted = row.split("\t");
				
				if (splitted[0].equals("K")) {
					K_constraints = new int[3];
					for (int i=0; i<3; i++)
						K_constraints[i] = Integer.valueOf(splitted[i+1]);
				}
				else
				if (splitted[0].equals("ALPHA")) {
					ALPHA_constraints = new float[3];
					for (int i=0; i<3; i++)
						ALPHA_constraints[i] = Float.valueOf(splitted[i+1]);
				}
				else
				if (splitted[0].equals("LAMBDA")) {
					LAMBDA_constraints = new float[3];
					for (int i=0; i<3; i++)
						LAMBDA_constraints[i] = Float.valueOf(splitted[i+1]);
				}
				else
				if (splitted[0].equals("I")) {
					I_constraints = new int[3];
					for (int i=0; i<3; i++)
						I_constraints[i] = Integer.valueOf(splitted[i+1]);
				}
				else
				if (splitted[0].equals("EPSILON")) {
					EPSILON_constraints = new float[3];
					for (int i=0; i<3; i++)
						EPSILON_constraints[i] = Float.valueOf(splitted[i+1]);
				}
				else
				if (splitted[0].equals("ALPHA_W")) {
					ALPHA_W_constraints = new float[3];
					for (int i=0; i<3; i++)
						ALPHA_W_constraints[i] = Float.valueOf(splitted[i+1]);
				}
				else
				if (splitted[0].equals("ALPHA_H")) {
					ALPHA_H_constraints = new float[3];
					for (int i=0; i<3; i++)
						ALPHA_H_constraints[i] = Float.valueOf(splitted[i+1]);
				}
				else
				if (splitted[0].equals("ALPHA_user_bias")) {
					ALPHA_user_bias_constraints = new float[3];
					for (int i=0; i<3; i++)
						ALPHA_user_bias_constraints[i] = Float.valueOf(splitted[i+1]);
				}
				else
				if (splitted[0].equals("ALPHA_item_bias")) {
					ALPHA_item_bias_constraints = new float[3];
					for (int i=0; i<3; i++)
						ALPHA_item_bias_constraints[i] = Float.valueOf(splitted[i+1]);
				}				
				else
				if (splitted[0].equals("SIGMOID_STEEPNESS")) {
					sigmoid_steepness_constraints = new float[3];
					for (int i=0; i<3; i++)
						sigmoid_steepness_constraints[i] = Float.valueOf(splitted[i+1]);
				}				
			}
		} catch (Exception e) {
			if (e.getMessage() != null)
				System.err.println(e.getMessage());
		} finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (Exception e) {
                	if (e.getMessage() != null)
                		System.err.println(e.getMessage());
                }
            }
        }		
	}
	
	public void generateParametersRandomly() {
		K = Utils.generateRandomIntInRange(K_constraints, random);
		ALPHA = Utils.generateRandomFloatInRange(ALPHA_constraints, random);
		ALPHA_W = Utils.generateRandomFloatInRange(ALPHA_W_constraints, random);
		ALPHA_H = Utils.generateRandomFloatInRange(ALPHA_H_constraints, random);
		ALPHA_user_bias = Utils.generateRandomFloatInRange(ALPHA_user_bias_constraints, random);
		ALPHA_item_bias = Utils.generateRandomFloatInRange(ALPHA_item_bias_constraints, random);
		LAMBDA = Utils.generateRandomFloatInRange(LAMBDA_constraints, random);
		I = Utils.generateRandomIntInRange(I_constraints, random);
		EPSILON = Utils.generateRandomFloatInRange(EPSILON_constraints, random);
	}
	
	public void generateParametersStepwise() {
		K = Utils.generateStepwiseIntInRange(K_constraints, random);
		ALPHA = Utils.generateStepwiseFloatInRange(ALPHA_constraints, random);
		ALPHA_W = Utils.generateStepwiseFloatInRange(ALPHA_W_constraints, random);
		ALPHA_H = Utils.generateStepwiseFloatInRange(ALPHA_H_constraints, random);
		ALPHA_user_bias = Utils.generateStepwiseFloatInRange(ALPHA_user_bias_constraints, random);
		ALPHA_item_bias = Utils.generateStepwiseFloatInRange(ALPHA_item_bias_constraints, random);
		LAMBDA = Utils.generateStepwiseFloatInRange(LAMBDA_constraints, random);
		I = Utils.generateStepwiseIntInRange(I_constraints, random);
		EPSILON = Utils.generateStepwiseFloatInRange(EPSILON_constraints, random);
	}

}
