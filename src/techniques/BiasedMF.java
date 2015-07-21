package techniques;

import utilities.Data;
import utilities.DatabaseManager;
import utilities.Parameters;

/**
 * Class for computing biased matrix factorization
 * @author tomi
 *
 */
public class BiasedMF extends SimpleMF {

	float[] user_bias;
	float[] item_bias;
	float average;

	public BiasedMF(Data data, Parameters parameters, DatabaseManager db_manager) {
		super(data,parameters,db_manager);

		initBiases();
	}
	
	void initBiases() {
		user_bias = new float[data.user_id_codes.size()+1];
		item_bias = new float[data.item_id_codes.size()+1];
		average = 0;

		float[] num_user_feedback = new float[user_bias.length];
		float[] num_item_feedback = new float[item_bias.length];

		for (int i=0; i<data.num_triples; i++) {
			average += data.triple_feedback[i];
			user_bias[data.triple_user[i]] += data.triple_feedback[i];
			item_bias[data.triple_item[i]] += data.triple_feedback[i];
			
			num_user_feedback[data.triple_user[i]]++;
			num_item_feedback[data.triple_item[i]]++;
		}
		
		average /= data.num_triples;
			
		for (int i=0; i<user_bias.length; i++) {
			user_bias[i] /= num_user_feedback[i];
			user_bias[i] -= average;
		}
		
		for (int i=0; i<item_bias.length; i++) {
			item_bias[i] /= num_item_feedback[i];
			item_bias[i] -= average;
		}			
	}

	float prediction(int user, int item) {
		return super.prediction(user, item) + average + user_bias[user] + item_bias[item]; 
	}
	
	void update(int i, float error) {
		average += parameters.ALPHA * error;
		user_bias[data.triple_user[random_order[i]]] += parameters.ALPHA_user_bias * (error - parameters.LAMBDA * user_bias[data.triple_user[random_order[i]]]);
		item_bias[data.triple_item[random_order[i]]] += parameters.ALPHA_item_bias * (error - parameters.LAMBDA * item_bias[data.triple_item[random_order[i]]]);				

		for (int k=0; k<parameters.K; k++) {
			float update_user = parameters.ALPHA_W * (error * H[data.triple_item[random_order[i]]][k] - parameters.LAMBDA * W[data.triple_user[random_order[i]]][k]);
			float update_item = parameters.ALPHA_H * (error * W[data.triple_user[random_order[i]]][k] - parameters.LAMBDA * H[data.triple_item[random_order[i]]][k]);
			
			W[data.triple_user[random_order[i]]][k] += update_user;
			H[data.triple_item[random_order[i]]][k] += update_item;
		}
	}
	
}
