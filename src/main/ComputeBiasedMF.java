package main;

import java.sql.SQLException;

import techniques.BiasedMF;
import utilities.Utils;

/**
 * The main class for computing biased MF
 * @author Tomas Horvath
 *
 */
public class ComputeBiasedMF extends ComputeSimpleMF{

	public ComputeBiasedMF(String[] args) throws SQLException {
		super(args);
	}
	
	// arguments should be in the similar form as for ComputeSimpleMF:
	public static void main(String[] args) throws SQLException {
		ComputeBiasedMF c = new ComputeBiasedMF(args);
		c.data.train_instances = Utils.generateValidationRandomly(c.parameters.train_randseed, c.data.num_triples, Float.valueOf(args[5]));
		
		BiasedMF bmf = new BiasedMF(c.data, c.parameters, c.db_manager);
		float[] result = bmf.factorize();

		c.db_manager.writeResultsIntoDB(c.parameters.DATA, "BiasedMF", c.parameters.toString(), result[0], result[1], (int) result[2]);		
	}

}
