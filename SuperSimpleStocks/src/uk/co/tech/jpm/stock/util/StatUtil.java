package uk.co.tech.jpm.stock.util;

/**
 * 
 * Utility class containing some useful statistical operations.
 * 
 * @author KrishnaPrasad
 *
 */
public class StatUtil {

	/**
	 * Computes geometric mean of a given sequence. 
	 * @param data data array
	 */
	public static double geoMean(double[] data) {	
		
		if(data.length == 0){
			return 0;
		}
		double grandTotal = 1.0;			
		for (int i=0; i<data.length; i++) {
	        double value = data[i];	        
	        grandTotal = grandTotal * value;	        
		}
		return Math.pow(grandTotal, (1.0 / data.length));
	}
}
