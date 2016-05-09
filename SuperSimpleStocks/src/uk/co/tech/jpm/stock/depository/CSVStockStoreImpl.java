package uk.co.tech.jpm.stock.depository;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.log4j.Logger;

import uk.co.tech.jpm.exceptions.StockStoreException;
import uk.co.tech.jpm.model.Stock;
import uk.co.tech.jpm.stock.util.CSVDataReader;

/**
 * 
 * This class provides stock store entity management using csv file.
 * When an instance of this class is created the stock store will be initialised from
 * the csv file.  
 * 
 * @author KrishnaPrasad
 *
 */
public class CSVStockStoreImpl extends StockStoreImpl {
	
	private static Logger logger = Logger.getLogger(CSVStockStoreImpl.class);
	
	/**
	 * Creates the stock entity store using the stocks.csv file which is included in the solution jar.
	 */
	public CSVStockStoreImpl(){
		
		InputStream in = getClass().getResourceAsStream("/stocks.csv"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));  				

		try {
			List<Stock> stocks = CSVDataReader.loadStocksFromsCSVFile(reader);
			for(Stock stock : stocks){
				addStock(stock);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
			throw new StockStoreException(e.getMessage());
		}
	}
	
	/**
	 * Creates the stock entity store using the file path provided by the user.
	 * @param filepath
	 */
	public CSVStockStoreImpl(String filepath){
		try {
			File file = new File(filepath);	
			List<Stock> stocks = CSVDataReader.loadStocksFromsCSVFile(file);
			for(Stock stock : stocks){
				addStock(stock);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
			throw new StockStoreException(filepath + " not found");
		}
	}		
}
