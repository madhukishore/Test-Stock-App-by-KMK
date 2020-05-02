package com.kmk.stock.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.google.common.collect.ImmutableMap;
import com.kmk.stock.model.StockModel;
import com.kmk.stock.model.StockEnum;

@Repository
public class StockDao {
	public Map<String, StockModel> getSymbolAndStockMap() {

		return ImmutableMap.of("TEA", new StockModel("TEA", StockEnum.COMMON, 0.0, 0.0, 100.0),
				"POP", new StockModel("POP", StockEnum.COMMON, 8.0, 0.0, 100.0), 
				"ALE", new StockModel("ALE", StockEnum.COMMON, 23.0, 0.0, 60.0), 
				"GIN", new StockModel("GIN", StockEnum.PREFERRED, 8.0, 0.2, 100.0), 
				"JOE", new StockModel("JOE", StockEnum.COMMON, 13.0, 0.0, 250.0));

	}
	
	/*
	 * This method returns the map of Stock symbol and current market price of the stock
	 */
	public Map<String, Double> getStockMarketPriceMap() {
		return ImmutableMap.of("TEA", 100.10, 
				"POP", 99.10,
				"ALE", 62.10,
				"GIN", 101.10,
				"JOE", 255.10);
	}
	
	public StockModel findByStockSymbol(String stockSymbol) {
		return getSymbolAndStockMap().get(stockSymbol);
	}
	public Double findMarketPriceByStockSymbol(String stockSymbol) {
		return getStockMarketPriceMap().get(stockSymbol);
	}
	
	public StockModel getAnInvalidStock() {
		return new StockModel("POP", StockEnum.COMMON, -8.0, -0.0, 100.0);
	}
	
	
}
