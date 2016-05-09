package uk.co.tech.jpm.stock.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.co.tech.jpm.exceptions.RecordTradeException;
import uk.co.tech.jpm.exceptions.StockNotFoundException;
import uk.co.tech.jpm.exceptions.StockServiceException;
import uk.co.tech.jpm.model.Trade;
import uk.co.tech.jpm.stock.depository.CSVStockStoreImpl;
import uk.co.tech.jpm.stock.depository.StockStore;
import uk.co.tech.jpm.stock.service.SimpleStockService;
import uk.co.tech.jpm.stock.service.SimpleStockServiceImpl;

public class TestSimpleStockService {

	private static SimpleStockService service;

	@BeforeClass
	public static void setup(){
		StockStore stockStore = new CSVStockStoreImpl();		
		service = new SimpleStockServiceImpl(stockStore);			
	}
	
	@Test(expected=StockNotFoundException.class)
	public void testCalculateDividendYield_case1(){
		String stockSymbol = "";
		BigDecimal marketPrice = new BigDecimal(3.78);		
		service.calculateDividendYield(stockSymbol, marketPrice);						
	}
	
	@Test
	public void testCalculateDividendYield_case2(){
		String stockSymbol = "TEA";
		BigDecimal marketPrice = BigDecimal.ZERO;
		try {
			BigDecimal dividendYield = service.calculateDividendYield(stockSymbol, marketPrice);			
			assertEquals(BigDecimal.ZERO, dividendYield);
		} catch (StockServiceException e) {
			// TODO Auto-generated catch block
			fail("Exception received --> " + e);
		}
	}
	
	@Test
	public void testCalculateDividendYield_case3(){
		String stockSymbol = "TEA";
		BigDecimal marketPrice = new BigDecimal(3);
		try {
			BigDecimal dividendYield = service.calculateDividendYield(stockSymbol, marketPrice);			
			assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), dividendYield);
		} catch (StockNotFoundException e) {
			// TODO Auto-generated catch block
			fail("Exception received -->" + e);
		}
	}
	
	@Test
	public void testCalculateDividendYield_case4(){
		String stockSymbol = "POP";
		BigDecimal marketPrice = new BigDecimal(3);
		try {
			BigDecimal dividendYield = service.calculateDividendYield(stockSymbol, marketPrice);			
			assertEquals(new BigDecimal(2.67).setScale(2, RoundingMode.HALF_UP), dividendYield);
		} catch (StockNotFoundException e) {
			// TODO Auto-generated catch block
			fail("Exception received -->" + e);
		}
	}
	
	@Test
	public void testCalculateDividendYield_case5(){
		String stockSymbol = "GIN";
		BigDecimal marketPrice = new BigDecimal(3);
		try {
			BigDecimal dividendYield = service.calculateDividendYield(stockSymbol, marketPrice);			
			assertEquals(new BigDecimal(0.67).setScale(2, RoundingMode.HALF_UP), dividendYield);
		} catch (StockNotFoundException e) {
			// TODO Auto-generated catch block
			fail("Exception received -->" + e);
		}
	}
	
	@Test(expected=StockServiceException.class)
	public void testCalculatePbyERatio_case1(){
		String stockSymbol = "TEA";
		BigDecimal marketPrice = new BigDecimal(35);
		try {
			BigDecimal pbyERatio = service.calculatePbyERatio(stockSymbol, marketPrice);						
		} catch (StockNotFoundException e) {
			// TODO Auto-generated catch block
			fail("Exception received -->" + e);
		}
	}
	
	@Test
	public void testCalculatePbyERatio_case2(){
		String stockSymbol = "GIN";
		BigDecimal marketPrice = new BigDecimal(43.25);
		try {
			BigDecimal pbyERatio = service.calculatePbyERatio(stockSymbol, marketPrice);
			assertEquals(new BigDecimal(5.41).setScale(2, RoundingMode.HALF_UP), pbyERatio);
		} catch (StockNotFoundException e) {
			// TODO Auto-generated catch block
			fail("Exception received -->" + e);
		}
	}
	@Test
	public void calculateVolumeWeightedStockPrice_case1(){
		String stockSymbol = "GIN";
		int noOfMinutes = 15;
		try{
			BigDecimal stockPrice = service.calculateVolumeWeightedStockPrice(stockSymbol, noOfMinutes);
			assertEquals(new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP), stockPrice);
		} catch (StockNotFoundException e) {
			// TODO Auto-generated catch block
			fail("Exception received -->" + e);
		}
	}
	
	@Test
	public void calculateVolumeWeightedStockPrice_case2(){
		String stockSymbol = "GIN";
		int noOfMinutes = 15;
		try{
			recordTrades();
			BigDecimal stockPrice = service.calculateVolumeWeightedStockPrice(stockSymbol, noOfMinutes);
			assertEquals(new BigDecimal(25.00).setScale(2, RoundingMode.HALF_UP), stockPrice);
		} catch (StockNotFoundException e) {
			// TODO Auto-generated catch block
			fail("Exception received -->" + e);
		}
	}
	@Test
	public void testCalculateGBCEAllShareIndex_case1(){		
		double shareIndex = service.calculateGBCEAllShareIndex();				
		assertEquals(new BigDecimal(38.91).setScale(2, RoundingMode.HALF_UP),new BigDecimal(shareIndex).setScale(2, RoundingMode.HALF_UP));
	}
	
	@Test(expected = StockNotFoundException.class)
	public void testRecordTrade_case1(){
		Trade trade = new Trade();
		trade.setStockSymbol("GYM");
		trade.setQuantityOfShares(20L);
		trade.setTradeIndicator(Trade.TradeType.BUY);
		trade.setTradePrice(new BigDecimal(2.5));
		service.recordTrade(trade);
	}
	
	@Test(expected = RecordTradeException.class)	
	public void testRecordTrade_case2(){
		Trade trade = new Trade();
		trade.setStockSymbol("POP");
		trade.setQuantityOfShares(20L);
		trade.setTradeIndicator(Trade.TradeType.BUY);
		trade.setTradePrice(BigDecimal.ZERO);
		service.recordTrade(trade);
	}
	
	@Test
	public void testRecordTrade_case3(){
		Trade trade = new Trade();
		trade.setStockSymbol("POP");
		trade.setQuantityOfShares(20L);
		trade.setTradeIndicator(Trade.TradeType.BUY);
		trade.setTradePrice(new BigDecimal(24.78));
		boolean isRecorded = service.recordTrade(trade);
		assertEquals(Boolean.TRUE, isRecorded);
	}
	
	private void recordTrades(){
		Trade trade = null;
		Long qty = 10L;
		BigDecimal tradePrice = new BigDecimal(10.00);
		for(int i=0;i<=5;i++){
			trade = new Trade();
			trade.setStockSymbol("GIN");
			trade.setQuantityOfShares(qty*(i+1));
			trade.setTradeIndicator(Trade.TradeType.BUY);
			trade.setTradePrice(tradePrice.multiply(new BigDecimal(2.5)));
			service.recordTrade(trade);
		}
	}
	
}
