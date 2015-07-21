package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class for providing some histograms from data
 * @author Tomas Horvath
 *
 */
public class Histogram {

	public static void main(String[] args) {
		HashMap<String,Integer> histogram = new HashMap<String,Integer>();
		
		HashSet<String> users = new HashSet<String>();
		HashSet<String> items = new HashSet<String>();

		int num = 0;
		
		BufferedReader reader = null;
		String row;

		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(args[0]))));	
			
			while ((row=reader.readLine()) != null) {
				if (!row.equals("")) {				
					String[] s = row.split(" ");
					
					if (histogram.containsKey(s[2])) {
						int i = histogram.get(s[2]) + 1;
						histogram.put(s[2],i);
					}
					else {
						histogram.put(s[2],1);
					}
					
					users.add(s[0]);
					items.add(s[1]);
					
					num++;
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
		
        List<Map.Entry<String, Integer>> sorted_histogram = new LinkedList<Map.Entry<String, Integer>>(histogram.entrySet());

        Collections.sort(sorted_histogram, new Comparator<Map.Entry<String,Integer>>() {
            public int compare(Map.Entry<String, Integer> m1, Map.Entry<String, Integer> m2) {
                return (m2.getValue()).compareTo(m1.getValue());
            }
        });

        System.out.println("histogram");
        
        for (Map.Entry<String, Integer> e : sorted_histogram)
        	System.out.println(e.getKey() + "\t" + e.getValue());

        System.out.println(".");
        
        System.out.println("# users: " + users.size());
        System.out.println("# items: " + items.size());
        System.out.println("# feedbacks: " + num);
        double sparsity = ((double) (users.size()*items.size() - num ) / (double) (users.size()*items.size())) * 100;
        System.out.println("sparsity = " + sparsity);
	}

}
