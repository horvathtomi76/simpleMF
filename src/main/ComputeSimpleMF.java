package main;

import java.sql.SQLException;

import techniques.SimpleMF;
import utilities.Data;
import utilities.DatabaseManager;
import utilities.Parameters;
import utilities.Utils;

/**
 * The main class for simple MF computation
 * @author Tomas Horvath
 *
 */
public class ComputeSimpleMF {

	Data data;
	Parameters parameters;
	DatabaseManager db_manager;
		
	public ComputeSimpleMF(String[] args) throws SQLException {
		parameters = new Parameters(args[0]);
		parameters.readConstraintsFromFile();
		
		if (args[6].equals("stepwise"))
			parameters.generateParametersStepwise();
		else
			if (args[6].equals("random"))
				parameters.generateParametersRandomly();
		
		db_manager = new DatabaseManager(args[1],args[2],args[3],args[4]);
		data = new Data(parameters.DATA);
			
		data.train_instances = Utils.generateValidationRandomly(parameters.validation_randseed, data.num_triples, Float.valueOf(args[5]));
	}
	
	// arguments should be in the following form:
	// DATA=data/A19/A19_PCG2.txt~K=207~ALPHA=0.0016~LAMBDA=0.0428~I=700~EPSILON=0.0000001~discretize=5 dbserver database user password 0.0f random outputIter
	// or
	// DATA=/home/tomi/Desktop/Datasets/CEZIS/A19/A19_PCG2.txt~PARAM_CONSTR=/home/tomi/Desktop/Datasets/CEZIS/A19/hp_constraints~discretize=5 dbserver database user password 0.01f stepwise outputIter
	public static void main(String[] args) throws SQLException {
		ComputeSimpleMF c = new ComputeSimpleMF(args);
		
		if (args.length == 8)
			System.out.println(c.parameters.toString() + "\n");
		
		SimpleMF smf = new SimpleMF(c.data, c.parameters, c.db_manager);
		
		smf.outputIter = args.length == 8 && args[7].equals("outputIter");
		
		c.data.discretize(c.parameters.discretize);
		
		float[] result = smf.factorize();
		
		c.db_manager.writeResultsIntoDB(c.parameters.DATA, "SimpleMF", c.parameters.toString(), result[0], result[1], (int) result[2]);
		
		if (args.length==8) {
			if (args[7].equals("outputW")) {
				smf.outputW();
			} else 
				if (args[7].equals("outputH")) {
					smf.outputH();
				} else {
					if (args[7].equals("outputWH")) {
						System.out.println("W\n");
						smf.outputW();
						System.out.println("\n\n");
						System.out.println("H\n");
						smf.outputH();
					}
				}
		}
	}
	
}
