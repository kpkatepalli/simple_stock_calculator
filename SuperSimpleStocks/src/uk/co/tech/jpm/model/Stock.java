package uk.co.tech.jpm.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The {@code Stock} class represents Stock entity.
 * 
 * @author KrishnaPrasad
 *
 */
public class Stock {

	public enum StockType {
		COMMON,PREFERRED
	}
	
	private String stockSymbol;
	private StockType stockType = StockType.COMMON;
	private BigDecimal lastDividend = BigDecimal.ZERO;
	private BigDecimal fixedDividend = BigDecimal.ZERO;
	private BigDecimal parValue = BigDecimal.ZERO;
	private BigDecimal marketPrice = BigDecimal.ZERO;
	
	public Stock(){
		//default constructor
	}
	
	public Stock(String stockSymbol, StockType stockType, BigDecimal lastDividend, BigDecimal fixedDividend,
			BigDecimal parValue) {
		super();
		this.stockSymbol = stockSymbol;
		this.stockType = stockType;
		this.lastDividend = lastDividend;
		this.fixedDividend = fixedDividend;
		this.parValue = parValue;
	}

	public String getStockSymbol() {
		return stockSymbol;
	}
	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}
	public StockType getStockType() {
		return stockType;
	}
	public void setStockType(StockType stockType) {
		this.stockType = stockType;
	}
	public BigDecimal getLastDividend() {
		return lastDividend;
	}
	public void setLastDividend(BigDecimal lastDividend) {
		this.lastDividend = lastDividend;
	}
	public BigDecimal getFixedDividend() {
		return fixedDividend;
	}
	public void setFixedDividend(BigDecimal fixedDividend) {
		this.fixedDividend = fixedDividend;
	}
	public BigDecimal getParValue() {
		return parValue;
	}
	public void setParValue(BigDecimal parValue) {
		this.parValue = parValue;
	}
		
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	@Override
	public String toString() {
		return "Stock [stockSymbol=" + stockSymbol + ", stockType=" + stockType + ", lastDividend=" + lastDividend.setScale(2, RoundingMode.HALF_UP)
				+ ", fixedDividend=" + fixedDividend.setScale(2, RoundingMode.HALF_UP) + ", parValue=" + parValue + "]";
	}
	
	
}
