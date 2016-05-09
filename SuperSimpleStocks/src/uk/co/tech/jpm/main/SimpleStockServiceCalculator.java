package uk.co.tech.jpm.main;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import uk.co.tech.jpm.exceptions.RecordTradeException;
import uk.co.tech.jpm.exceptions.StockNotFoundException;
import uk.co.tech.jpm.model.Stock;
import uk.co.tech.jpm.model.Trade;
import uk.co.tech.jpm.stock.depository.CSVStockStoreImpl;
import uk.co.tech.jpm.stock.depository.StockStore;
import uk.co.tech.jpm.stock.service.SimpleStockService;
import uk.co.tech.jpm.stock.service.SimpleStockServiceImpl;

public class SimpleStockServiceCalculator {

	private static Logger logger = Logger.getLogger(SimpleStockServiceCalculator.class);
	private SimpleStockService service;
	private StockStore stocksStore;
	
	/**
	 * 
	 * @param fileName
	 */
	private  void init(String fileName){	
		
		stocksStore =  (fileName == null ? new CSVStockStoreImpl() : new CSVStockStoreImpl(fileName));	
		service = new SimpleStockServiceImpl(stocksStore);			
	}
	
	private void displayMenu(){
		boolean quit = false;        
        Scanner s =  new Scanner(System.in);
        try{         
			do{
				int menuItem = 3;  
				try{	
					displayMessage("Welcome to Super Simple Stock Service");
					displayMessage("=====================================");						
					displayMessage("\t1. Stock Operations");
					displayMessage("\t2. Calculate GBCE All Share Index");
					displayMessage("\t3. Quit");			
					displayMessage("Please choose one of the above option [1-3]");
					try{
						menuItem = s.nextInt();
					}catch(InputMismatchException ime){
						displayMessage("Invalid option entered");
						logger.error(ime,ime);
					}
					switch(menuItem){
						case 1 :
							stockOperationsMenu(s);
							break;				
						case 2 :							
							displayMessage("GBCEAllShareIndex = " + service.calculateGBCEAllShareIndex());
							break;					
						case 3 : 
							displayMessage("Exiting the calculator");
							quit = true;
							break;		
					   default : displayMessage("Invalid input");
					}				
				}catch(Exception e){				
					displayMessage("Invalid input");
					logger.error(e,e);					
				}				
			}while(!quit);
        }finally{
        	s.close();
        }
	}
	
	/**
	 * 
	 * @param scanner
	 */
	private void stockOperationsMenu(Scanner scanner){
		boolean quit = false;
        int subMenuItem = 5;		
		String stockSymbol;
		Stock stock = null;
		BigDecimal marketPrice;
		
        displayMessage("Stock Operations");
        displayMessage("============================");       
		try{
			displayMessage("Enter Stock Symbol : ");
			stockSymbol = scanner.next();
			stock = stocksStore.retrieveStock(stockSymbol.toUpperCase());		
			displayMessage(stock);
			displayMessage("Enter Market Price : ");
			marketPrice = scanner.nextBigDecimal();
			stock.setMarketPrice(marketPrice);
		}catch(InputMismatchException ime){
			displayMessage("Invalid input");
			logger.error(ime,ime);
			return;
		}catch(StockNotFoundException sfe){
			displayMessage("Invalid stock symbol, returning to Menu");	
			return;
		}
		do{
			try{				
				displayMessage("Following options are available for the given stock.");
				displayMessage("\t1. Calculate Dividend Yield");
				displayMessage("\t2. Calculate P/E Ratio");
				displayMessage("\t3. Record Trade");
				displayMessage("\t4. Calculate volume weighted stock price.(on trades recorded in past 15 minutes)");
				displayMessage("\t5. Return to Main Menu");
				displayMessage("Please choose one of the above option [1-5]");	
				try{
					subMenuItem = scanner.nextInt();
				}catch(InputMismatchException ime){
					displayMessage("Invalid option entered");
					logger.error(ime,ime);
					return;
				}
				switch(subMenuItem){
					case 1 :						
						displayMessage("Dividend Yield = " + service.calculateDividendYield(stock.getStockSymbol(), stock.getMarketPrice()));
						break;				
					case 2 :								
						displayMessage("P/E Ratio = " + service.calculatePbyERatio(stock.getStockSymbol(), stock.getMarketPrice()));
						break;
					case 3 :
						recordTrade(scanner,stock);
						break;
					case 4 :
						int noOfMinutes = 15;
						displayMessage("Volume weighted Stock Price value = " + service.calculateVolumeWeightedStockPrice(stock.getStockSymbol(), noOfMinutes));	
						break;					
					case 5 : 
						displayMessage("Returning to Main menu.");
						quit = true;
						break;		
				   default : displayMessage("Invalid input");
				}					
			}catch(RecordTradeException sfe){
				displayMessage(sfe.getMessage() + " -- returning to Menu");
				logger.error(sfe,sfe);
				quit = true;
			}catch(Exception e){				
				displayMessage("Invalid input. returning to Menu");
				logger.error(e,e);
				quit = true;
			}			
		}while(!quit);	
	}
	
	 void recordTrade(Scanner s,Stock stock){		
		displayMessage("Enter Quantity : ");
		int quantity = s.nextInt();
		displayMessage("Buy or Sell [B/S] : ");
		String buyOrSell = s.next();
		displayMessage("Enter Trade Price : ");
		BigDecimal tradePrice = s.nextBigDecimal();
		Trade trade = new Trade();
		trade.setTimestamp(new Date());
		trade.setStockSymbol(stock.getStockSymbol());
		trade.setQuantityOfShares(new Long(quantity));
		if(buyOrSell.equalsIgnoreCase("B")){
			trade.setTradeIndicator(Trade.TradeType.BUY);
		}else if(buyOrSell.equalsIgnoreCase("S")){
			trade.setTradeIndicator(Trade.TradeType.SELL);
		}
		trade.setTradePrice(tradePrice);
		service.recordTrade(trade);
		displayMessage("Trade recorded");
	}
	
/*	 void calculateDividendYield(Stock stock){
		BigDecimal dividendYield = service.calculateDividendYield(stock.getStockSymbol(), stock.getMarketPrice());		
		displayMessage("Dividend Yield = " + dividendYield);
	
	}
	
	 void calculatePByERatio(Stock stock){		
		BigDecimal pbyE = service.calculatePbyERatio(stock.getStockSymbol(), stock.getMarketPrice());		
		displayMessage("P/E Ratio = " + pbyE);
	}
	
	 void calculateVolumeWeightedStockPrice(Stock stock){		
		 int noOfMinutes = 15;
		BigDecimal value = service.calculateVolumeWeightedStockPrice(stock.getStockSymbol(), noOfMinutes);
		displayMessage("Volume weighted Stock Price value = " + value);	
	}
	

	 
	void calculateGBCEAllShareIndex(){
		double index = service.calculateGBCEAllShareIndex();
		displayMessage("GBCEAllShareIndex = " + index);
	}*/
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleStockServiceCalculator calc = new SimpleStockServiceCalculator();
		String fileName = null;
		if(args.length > 0){
			fileName = args[0];
			if(fileName != null){
				File file = new File(fileName);
				if(!file.exists()){
					System.out.println("Input file "+ fileName + " not found ");
					System.exit(0);
				}
			}
		}
		
		calc.init(fileName);
		calc.displayMenu();
		
	}
	
	private void displayMessage(Object msg){
		System.out.println(msg);
	}
	
}
