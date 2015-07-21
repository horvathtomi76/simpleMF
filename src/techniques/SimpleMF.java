package techniques;

import java.util.Date;
import java.util.Map.Entry;

import utilities.Data;
import utilities.DatabaseManager;
import utilities.Parameters;
import utilities.Utils;

/**
 * Class for computing simple matrix factorization
 * @author Tomas Horvath
 *
 */
public class SimpleMF {

	Data data;
	Parameters parameters;
	DatabaseManager db_manager;
	
	float[][] W;
	float[][] H;
	
	int[] random_order;
	
	float train_error;
	float validation_error;
	
	public boolean outputIter = false;
	
	public void outputW() {
		for (Entry<String,Integer> entry : data.user_id_codes.entrySet()) {
			System.out.print(entry.getKey() + "\t");
			for (int j=0; j<W[0].length; j++) {
				System.out.print(W[entry.getValue()][j]);
				if (j<W[0].length-1)
					System.out.print("\t");
			}
			System.out.println();			
		}
	}

	public void outputH() {
		for (Entry<String,Integer> entry : data.item_id_codes.entrySet()) {
			System.out.print(entry.getKey() + "\t");
			for (int j=0; j<H[0].length; j++) {
				System.out.print(H[entry.getValue()][j]);
				if (j<H[0].length-1)
					System.out.print("\t");
			}
			System.out.println();			
		}
	}

	public SimpleMF(Data data, Parameters parameters, DatabaseManager db_manager) {
		this.data = data;
		this.parameters = parameters;
		this.db_manager = db_manager;
		
		this.W = Utils.initializeMatrixGaussian(data.user_id_codes.size(), this.parameters.K, 
				this.parameters.init_mean, this.parameters.init_std_dev, this.parameters.random);
		this.H = Utils.initializeMatrixGaussian(data.item_id_codes.size(), this.parameters.K, 
				this.parameters.init_mean, this.parameters.init_std_dev, this.parameters.random);
		
		random_order = Utils.createShuffledArray(this.parameters.random, this.data.num_triples);
	}
	
	float prediction(int user, int item) {
		float predicted = 0;

		for (int k=0; k<parameters.K; k++)
			predicted += W[user][k] * H[item][k];

		if (Float.isInfinite(predicted) || Float.isNaN(predicted)) {
			db_manager.writeResultsIntoDB(parameters.DATA, "SimpleMF", parameters.toString(), Float.NaN, Float.NaN, -1);

			System.exit(0);
		}
		
		return predicted;
	}
	
	void compute() {
		int iter = 0;
		train_error = 0;

		boolean stop = false;
		
		while (!stop) {
			float old_mse = train_error;		
			
			Utils.shuffleArray(this.parameters.random, random_order);
			
			for (int i=0; i<random_order.length; i++)
				if (data.train_instances[random_order[i]]) {
					float error = data.triple_feedback[random_order[i]] - prediction(data.triple_user[random_order[i]],data.triple_item[random_order[i]]);
					
					update(i,error);
				}

			compute_train_error();

			if (iter++ > parameters.I || Math.abs(train_error-old_mse) < parameters.EPSILON)
				stop = true;
		}
		
		compute_validation_error();
	}
	
	void compute_train_error() {
		train_error = 0;
		int num = 0;
		for (int i=0; i<data.num_triples; i++)
			if (data.train_instances[i]) {
				float error = data.triple_feedback[i] - prediction(data.triple_user[i],data.triple_item[i]);
				train_error += error * error;

				num++;
			}
		
		train_error /= num;
		
		if (outputIter)
			System.out.println(train_error);
	}

	void compute_validation_error() {
		validation_error = 0;
		int num = 0;
		for (int i=0; i<data.num_triples; i++) {
			if (!data.train_instances[i]) {
				float error = data.triple_feedback[i] - prediction(data.triple_user[i],data.triple_item[i]);
				validation_error += error * error;

				num++;
			}
		}
		
		validation_error /= num;
	}
	
	void update(int i, float error) {
		for (int k=0; k<parameters.K; k++) {
			float update_user = parameters.ALPHA * (error * H[data.triple_item[random_order[i]]][k] - parameters.LAMBDA * W[data.triple_user[random_order[i]]][k]);
			float update_item = parameters.ALPHA * (error * W[data.triple_user[random_order[i]]][k] - parameters.LAMBDA * H[data.triple_item[random_order[i]]][k]);
			
			W[data.triple_user[random_order[i]]][k] += update_user;
			H[data.triple_item[random_order[i]]][k] += update_item;
		}		
	}
	
	public float[] factorize() {
		float[] statistics = new float[3];
		
		Date s = new Date();
		long start = s.getTime();

		compute();
		
		Date e = new Date();
		long time = e.getTime() - start;
	
		statistics[0] = train_error;
		statistics[1] = validation_error;
		statistics[2] = (float) time;
		
		return statistics;
	}

}
