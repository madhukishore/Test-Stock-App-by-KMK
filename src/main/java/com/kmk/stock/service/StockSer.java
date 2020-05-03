package com.kmk.stock.service;

import java.util.Map;

import com.kmk.stock.model.StockModel;

public interface StockSer {

	public double calculateDividendYield(StockModel stockModel, double marketPrice);
	
	public double calculatePriceToEarningRatio(double marketPrice, double dividendYield);
	
	public Double allShareIndex(Map<String, StockModel> stockModels);
}