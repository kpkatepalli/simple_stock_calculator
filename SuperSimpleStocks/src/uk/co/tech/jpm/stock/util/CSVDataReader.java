package uk.co.tech.jpm.stock.util;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import uk.co.tech.jpm.model.Stock;
/**
 * Utility class to read the csv file containing stocks information.
 * Expected csv file in the format stockSymbol,stockType,lastDividend,fixedDividend,parValue
 * and in the same order.
 * 
 * @author KrishnaPrasad
 *
 */
public class CSVDataReader {	
	private static Logger logger = Logger.getLogger(CSVDataReader.class);
	private static final String QUOTE = "\"";	
	private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
	
	/**
	 * 
	 * @param dataFileforRead
	 * @return
	 * @throws Exception
	 */
	public static List<Stock> loadStocksFromsCSVFile(File dataFileforRead) throws Exception{
		FileReader dataFileReader =  new FileReader(dataFileforRead);
		return loadStocksFromsCSVFile(dataFileReader);
	}
	
	/**
	 * 
	 * @param dataFileReader
	 * @return
	 * @throws Exception
	 */
	public static List<Stock> loadStocksFromsCSVFile(Reader dataFileReader) throws Exception{
		List<Stock> stocks = new ArrayList<Stock>();
		int noOfColumns = 5;		
		LineNumberReader dataReader = null;			
		dataReader = new LineNumberReader(dataFileReader);
		// read past first line (for now...assuming the file contains header)
		String sLine = dataReader.readLine();		
		// try retrieve data from file
		try {
			Stock stock;
			while ((sLine = dataReader.readLine()) != null) {
				List<String> data = getDataValues(sLine, noOfColumns);
				String stockSymbol = data.get(0).trim();
				String stockType =  data.get(1).trim();
				String lastDividend = data.get(2).trim();
				String fixedDividend = data.get(3).trim();
				String parValue = data.get(4).trim();				
			
				stock = new Stock();
			
				stock.setStockSymbol(stockSymbol.toUpperCase());					  
				stock.setStockType(stockType.equalsIgnoreCase("Common") ? Stock.StockType.COMMON : Stock.StockType.PREFERRED);
				if(!lastDividend.isEmpty()){
					stock.setLastDividend(new BigDecimal(lastDividend));
				}else{
					stock.setLastDividend(BigDecimal.ZERO);
				}
				if(!fixedDividend.isEmpty()){
					if(fixedDividend.endsWith("%")){
						fixedDividend = fixedDividend.replace('%', ' ').trim();
						stock.setFixedDividend(new BigDecimal(fixedDividend).divide(ONE_HUNDRED));
					}else{
						stock.setFixedDividend(new BigDecimal(fixedDividend));
					}
				}else{
					stock.setFixedDividend(BigDecimal.ZERO);
				}
				if(!parValue.isEmpty()){
					stock.setParValue(new BigDecimal(parValue));
				}else{
					stock.setParValue(BigDecimal.ZERO);
				}
				stocks.add(stock);	
				logger.debug(stock);
			}			
		
		}finally{
			dataReader.close();
		}
		return stocks;
	}
	
			
	  private static List<String> getDataValues(String record, int fieldCount) {

		    // based on splitLine function as described in:
		    // http://www.cafeconleche.org/books/xmljava/chapters/ch04s03.html
		    // - modified to allow for known number of fields on entry
		    // - blank entries added at end where insufficient data values present
		    // - any quotes around values are included in results
		    // - returns ArrayList of values

		    // /////record = record.trim();
		    // System.out.println("record.length()="+record.length());
		    int index = 0;
		    // int index = -1;
		    int valueCount = 0;
		    List<String> result = new ArrayList<String>();

		    for (int i = 0; i < fieldCount; i++) {
		      // find the next comma
		      StringBuffer sb = new StringBuffer();
		      char c;
		      boolean inString = false;
		      boolean quotedString = false;
		      // while (true) {
		      while (index < record.length()) {
		        c = record.charAt(index);
		        if (!inString && c == '"') {
		          inString = true;
		          quotedString = true;
		        } else if (inString && c == '"')
		          inString = false;
		        else if (!inString && c == ',')
		          break;
		        else
		          sb.append(c);
		        index++;
		        if (index == record.length())
		          break;
		      }
		      String s;
		      if (quotedString) {
		        s = QUOTE + sb.toString() + QUOTE;
		      } else {
		        s = sb.toString().trim();
		      }
		      // System.out.println("#### getDataValues - item "+i+": "+s);
		      result.add(i, s);
		      valueCount++;
		      index++;
		      if (index == record.length())
		        break;
		    }
		    // add items to result for any values not found by end of record
		    if (valueCount < fieldCount) {

		      for (int i = valueCount; i < fieldCount; i++) {
		        // System.out.println("#### getDataValues - item "+i+": (added)");
		        result.add(i, "");
		      }
		    }
		    return result;
		  }  
	  
}
