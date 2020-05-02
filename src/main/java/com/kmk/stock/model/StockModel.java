package com.kmk.stock.model;

public class StockModel {

	private final String stockSymbol; // unique representation for a stock
	private final StockEnum type; 
	private final Double lastDividend;
	private final Double fixedDividend;
	private final Double parValue; // price at the time of purchase
	
	public StockModel(final String stockSymbol, final StockEnum type, final Double lastDividend, final Double fixedDividend, final Double parValue) {
		super();
		this.stockSymbol = stockSymbol;
		this.type = type;
		this.lastDividend = lastDividend;
		this.fixedDividend = fixedDividend;
		this.parValue = parValue;
	}
	
	public String getStockSymbol() {
		return stockSymbol;
	}
	
	public Double getLastDividend() {
		return lastDividend;
	}
	
	public Double getFixedDividend() {
		return fixedDividend;
	}
	
	public Double getParValue() {
		return parValue;
	}
	
	public StockEnum getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "Stock Details for " + stockSymbol + ": , type=" + type + ", lastDividend=" + lastDividend
				+ ", fixedDividend=" + fixedDividend + ", parValue=" + parValue;
	}
	
}