package utilities;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Class containing simple utilities
 * @author Tomas Horvath
 *
 */
public class Utils {

	public static List<Map.Entry<Float, Float>> sortByKey(Map<Float, Float> map) {
        List<Map.Entry<Float, Float>> list = new LinkedList<Map.Entry<Float, Float>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Float,Float>>() {

            public int compare(Map.Entry<Float, Float> m1, Map.Entry<Float, Float> m2) {
                return (m1.getKey()).compareTo(m2.getKey());
            }
        });

        return list;
    }

	public static int getCoordinate(int i, int j, int num_items) {
		return (int) (i*(num_items) - sumOfValues(i) + (j-i) -1);
	}
	
	public static int sumOfValues(int num) {
		return (int) (num * (num+1))/2;
	}
	
	public static float generateRandomFloatInRange(float[] constraints, Random rand) {
		return (rand.nextFloat() * (constraints[1] - constraints[0])) + constraints[0];
	}

	public static int generateRandomIntInRange(int[] constraints, Random rand) {
		if (constraints[1] == constraints[0])
			return constraints[0];
		else
			return rand.nextInt(constraints[1] - constraints[0]) + constraints[0];
	}

	public static float generateStepwiseFloatInRange(float[] constraints, Random rand) {
		int num_steps = (int) ((constraints[1]-constraints[0]) / constraints[2]);
		return constraints[0] + rand.nextInt(num_steps+1)*constraints[2];
	}

	public static int generateStepwiseIntInRange(int[] constraints, Random rand) {
		int num_steps = (constraints[1]-constraints[0]) / constraints[2];
		return constraints[0] + rand.nextInt(num_steps+1)*constraints[2];
	}

	public static boolean[] generateValidationRandomly(int randseed, int length, float test_triples_ratio) {
		Random rand = new Random(randseed);
		
		boolean[] train_triples = new boolean[length];
		
		for (int i=0; i<train_triples.length; i++)
			train_triples[i] = true;
		
		int max = (int) (length * test_triples_ratio);
		int num=0;
		while (num < max) {
			int j = rand.nextInt(length);
			
			if (train_triples[j]) {
				train_triples[j] = false;
				num++;
			}
		}
		
		return train_triples;
	}
	
	public static void shuffleArray(Random rand, int[] array) {
		int j;
			
		for (int i=0; i<array.length; i++) {
			j = rand.nextInt(array.length-1);
				
			int temp = array[i];
			array[i] = array[j];
			array[j] = temp;
		}
	}
	
	public static int[] createShuffledArray(Random rand, int length) {
		int[] array = new int[length];
		for (int i=0; i<array.length; i++)
			array[i] = i;
		
		shuffleArray(rand,array);
		
		return array;
	}
	
	public static float[][] initializeMatrixGaussian(int rows, int columns, float mean, float std_dev, Random rand) {
		float[][] matrix = new float[rows][columns];
		
		for (int i=0; i<rows; i++)
			for (int j=0; j<columns; j++)
				matrix[i][j] = mean + ((float) rand.nextGaussian() * std_dev);

		return matrix;
	}

}
