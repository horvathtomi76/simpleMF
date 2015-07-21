package utilities;

/**
 * The class for data manipulation for CSV files separated by a blank character " ".
 * @author Tomas Horvath
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class Data {
	
	public int[] triple_user;	// users part of triples
	public int[] triple_item;	// item part of triples
	public float[] triple_feedback;	// feedback part of triples
	
	public int num_triples;	// number of triples in the data
	
	public boolean[] train_instances;	// indicating which triples are used for training the model (1) and which for testing (0)
	
	public HashMap<String,Integer> user_id_codes;	// user ids in the data mapped to numbers
	public HashMap<String,Integer> item_id_codes;	// item ids in the data mapped to numbers
	
	public HashMap<Integer,String> reversed_user_id_codes;	// reverse of the user_id_codes map
	public HashMap<Integer,String> reversed_item_id_codes;	// reverse of the item_id_codes map
	
	float[] borders;	// for discretization 
	
	public Data(String data_file) {
		this.num_triples = getNumberOfTriples(data_file);
		
		triple_user = new int[num_triples];
		triple_item = new int[num_triples];
		triple_feedback = new float[num_triples];

		this.user_id_codes = new HashMap<String,Integer>();
		this.item_id_codes = new HashMap<String,Integer>();
		
		readFromFile(data_file);
	}
	
	public void createReverseUserCodes() {
		reversed_user_id_codes = new HashMap<Integer,String>(); 
		for (Entry<String,Integer> e : user_id_codes.entrySet())
			reversed_user_id_codes.put(e.getValue(), e.getKey());
	}

	public void createReverseItemCodes() {
		reversed_item_id_codes = new HashMap<Integer,String>(); 
		for (Entry<String,Integer> e : item_id_codes.entrySet())
			reversed_item_id_codes.put(e.getValue(), e.getKey());
	}

	public void discretize(int num_final_values) {
		float[] sorted_feedbacks = new float[num_triples];
		for (int i=0; i<triple_feedback.length; i++)
			sorted_feedbacks[i] = triple_feedback[i];
		
		Arrays.sort(sorted_feedbacks);
		
		borders = new float[num_final_values];
		int num_remaining_triples = num_triples;
		int num_remaining_intervals = num_final_values;
		
		for (int border=0; border<num_final_values-1; border++) {
			int step = num_remaining_triples / num_remaining_intervals;
			int index = (border+1)*step;
			
			while (sorted_feedbacks[index] == sorted_feedbacks[++index]);
			
			borders[border] = sorted_feedbacks[index];
			
			if (index >= ((border+2)*step)) {
				num_remaining_triples -= index;
				num_remaining_intervals--;
			}
		}
		
		borders[num_final_values-1] = Float.MAX_VALUE;

		for (int i=0; i<triple_feedback.length; i++)
			for (int j=0; j<borders.length; j++)
				if (triple_feedback[i] < borders[j]) {
					triple_feedback[i] = j+1;
					
					break;
				}
	}
	
	int getNumberOfTriples(String data_file) {
		if (data_file == null)
			System.err.println("Data file was not specified.");
		
		BufferedReader reader = null;
		@SuppressWarnings("unused")
		String row;
		
		int num_triples = 0;
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(data_file))));	
			
			while ((row=reader.readLine()) != null) {				
				num_triples++;
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
		
		return num_triples;
	}
	
	void readFromFile(String data_file) {
		BufferedReader reader = null;
		String row;
		
		int num_triple = 0;
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(data_file))));	
			
			while ((row=reader.readLine()) != null) {
				if (!row.equals("")) {				
//					String[] s = row.split("\t");
					String[] s = row.split(" ");
					
					if (user_id_codes.containsKey(s[0]))
						triple_user[num_triple] = user_id_codes.get(s[0]);
					else {
						triple_user[num_triple] = user_id_codes.size();
						user_id_codes.put(s[0],triple_user[num_triple]);
					}
					
					if (item_id_codes.containsKey(s[1]))
						triple_item[num_triple] = item_id_codes.get(s[1]);
					else {
						triple_item[num_triple] = item_id_codes.size();
						item_id_codes.put(s[1],triple_item[num_triple]);
					}
					
					triple_feedback[num_triple] = Float.valueOf(s[2]);
					
					num_triple++;
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

}
