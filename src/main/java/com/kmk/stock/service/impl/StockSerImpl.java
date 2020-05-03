package com.kmk.stock.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.kmk.stock.dao.StockDao;
import com.kmk.stock.exception.InvalidValException;
import com.kmk.stock.model.StockEnum;
import com.kmk.stock.model.StockModel;
import com.kmk.stock.service.StockSer;

@Service
public class StockSerImpl implements StockSer {
	
	
	private static Logger LOGGER = LoggerFactory.getLogger(StockSerImpl.class);
	private final StockDao stockDao;
	
	@Autowired
	public StockSerImpl(final StockDao stockDao) {
		this.stockDao = stockDao;
	}
	
	/**
	 * Dividend yield for a stock and its market price
	 */
	@Override
	public double calculateDividendYield(StockModel stockModel, double marketPrice) {
		validateStockDetails(ImmutableList.of(stockModel), ImmutableList.of(marketPrice));
		LOGGER.info("Evaluating divident yield for {} & market Price {}", stockModel, marketPrice);
		
		final double dividendYield;
		if(stockModel.getType().equals(StockEnum.COMMON)) 
			dividendYield = stockModel.getLastDividend() / marketPrice;
		else 
			dividendYield = (stockModel.getFixedDividend() * stockModel.getParValue()) / marketPrice;

		return dividendYield;
	}
	
	/**
	 * P/E ratio for a market price and dividend value
	 */
	
	@Override
	public double calculatePriceToEarningRatio(double marketPrice, double dividendYield) {
	
		validateStockDetails(ImmutableList.of(), ImmutableList.of(marketPrice));
		double priceToEarningRation = 0.0;
		LOGGER.info("Evaluating PriceToEarningRation for a stock with dividend: {} and marketPrice: {} ", dividendYield, marketPrice);
		if(dividendYield > 0.0) {
			priceToEarningRation = marketPrice / dividendYield; 
		}
		return priceToEarningRation;
	}
	
	/**
	 * All share index for all the stocks 
	 */
	@Override
	public Double allShareIndex(Map<String, StockModel> stockModels) {
		validateStockDetails(ImmutableList.copyOf(stockModels.values()), ImmutableList.of());
		
		LOGGER.info("Evaluating all share index for Stocks Provided in test data of GBCE & market Price assumed ");
		final Double allShareIndex;
		allShareIndex = stockModels.values().stream()
				.mapToDouble(stock -> stockDao.findMarketPriceByStockSymbol(stock.getStockSymbol())).sum();
		return Math.pow(allShareIndex, 1.0 / stockModels.size());
	}
	

	/**
	 * Validate basic stock details like Par value, Last Dividend, Fixed Dividend & for stock market price
	 */
	private void validateStockDetails(List<StockModel> stockModels, List<Double> stockMarketPrices) {
		
		LOGGER.info("Validating Stock Details ");
		
		stockModels.forEach((stock -> {
			if(stock.getParValue() <= 0.0) {
				throw new InvalidValException("Invalid value of Par Value");
			}
			
			if(stock.getLastDividend() < 0.0) {
				throw new InvalidValException("Invalid value of Last Dividend");
			}
			
			if(stock.getFixedDividend() < 0.0) {
				throw new InvalidValException("Invalid value of Fixed Dividend");
				
			}
			
		}));
		
		stockMarketPrices.forEach(( price ->{
			if(price <= 0.0) {
				throw new InvalidValException("Invalid value of Market Price");
			}
		}));
	}
}
