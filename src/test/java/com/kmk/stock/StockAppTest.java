package com.kmk.stock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.kmk.stock.dao.StockDao;
import com.kmk.stock.dao.TradeDao;
import com.kmk.stock.exception.InvalidValException;
import com.kmk.stock.model.StockModel;
import com.kmk.stock.model.StockEnum;
import com.kmk.stock.model.TradeModel;
import com.kmk.stock.model.TradeEnum;
import com.kmk.stock.service.StockSer;
import com.kmk.stock.service.TradeSer;


/**
 * Unit tests for simple SuperSimpleStockApp.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {StockConfig.class})
public class StockAppTest {

	@Rule
    public ExpectedException thrown = ExpectedException.none();
	private Map<String, StockModel> stockKeyAndStockMap;
	@Autowired
	private StockDao stockDao;
	@Autowired
	private StockSer stockSer;
	@Autowired
	private TradeSer tradeSer;
	@Autowired
	private TradeDao tradeDao;

	/*
	 * @throws Exception
	 */
	@Before
	public void setUpAndCleanUp() throws Exception{
		stockDao.getStockMarketPriceMap();
		stockKeyAndStockMap = stockDao.getSymbolAndStockMap();
		if(tradeDao.getAllTrades() !=null && !tradeDao.getAllTrades().isEmpty())  
			tradeDao.getAllTrades().clear();
	}
	
	
	@Test
	public void testGivenInvalidStockData_shouldThrowException() {
		thrown.expect(InvalidValException.class);
	    thrown.expectMessage("Invalid value of Last Dividend");
	    StockModel invalidStockDetail  = new StockModel("POP", StockEnum.COMMON, -9.0, -0.0, 100.0);
		stockSer.calculateDividendYield(invalidStockDetail, -180.00);
		
	}
	
	@Test
	public void testGivenInvalidTradeDetails_shouleThrowException() {
		thrown.expect(InvalidValException.class);
	    thrown.expectMessage("A valid stock is required for trading");
	    TradeModel anInvalidTradeDetails = new TradeModel(TradeEnum.BUY, 0, -100.00, LocalDateTime.now(), null);
	    tradeSer.addStockTrade(anInvalidTradeDetails);
	}
	
	@Test
	public void testGivenValidStockData_shouldCheckDividendYieldValue() {
		StockModel sampleStockDetailsForTEA = stockDao.findByStockSymbol("TEA");
		double dividendYieldForTEA = stockSer.calculateDividendYield( sampleStockDetailsForTEA, 110) ;
		assertThat( dividendYieldForTEA, is(0.0));
		
		StockModel sampleStockDetailsForGIN = stockDao.findByStockSymbol("GIN");
		double dividendYieldForGIN = stockSer.calculateDividendYield(sampleStockDetailsForGIN, 90) ;
		assertThat( dividendYieldForGIN, is(0.2222222222222222));
	}
	
	@Test
	public void testGivenValidStockData_shouldCheckPnERatioValue() {
		
		StockModel sampleStockDetailsForTEA = stockDao.findByStockSymbol("TEA");
		double dividendYieldForTEA = stockSer.calculateDividendYield( sampleStockDetailsForTEA, 110) ;
		double pAndERationForTEA = stockSer.calculatePriceToEarningRatio(110, dividendYieldForTEA);
		
		assertThat(pAndERationForTEA, is(0.0));
		
		StockModel sampleStockDetailsForGIN = stockDao.findByStockSymbol("GIN");
		double dividendYieldForGIN = stockSer.calculateDividendYield(sampleStockDetailsForGIN, 90) ;
		double pAndERationForGIN = stockSer.calculatePriceToEarningRatio(90, dividendYieldForGIN);
		assertThat(pAndERationForGIN, is(405.0));//0.0
		
		
	}
	
	@Test
	public void testGivenValidTradeData_shouldCheckSaveStockTrading() {
		
		TradeModel popTrade = new TradeModel(TradeEnum.BUY, 2, 100.00, LocalDateTime.now(), stockDao.findByStockSymbol("POP"));
		TradeModel aleTrade = new TradeModel(TradeEnum.SELL, 2, 60.00, LocalDateTime.now(), stockDao.findByStockSymbol("ALE"));
		tradeDao.addTrade(popTrade);
		tradeDao.addTrade(aleTrade);

		assertThat(tradeDao.getAllTrades().size(), is(2));
		assertThat(tradeDao.getAllTrades().stream().filter(trade -> trade.getType().equals(TradeEnum.BUY)).count(), is(1L));
		assertThat(tradeDao.getAllTrades().stream().filter(trade -> trade.getType().equals(TradeEnum.SELL)).count(), is(1L));
		
	}
	/**
	 * this method takes given stock with their market price and evaluates allStockIndexValue and assertWithExpectedValue
	 */
	@Test
	public void testGivenValidStockData_shouldCheckAllStockIndexValue() {
		double allShareIndexValue = Math.round(stockSer.allShareIndex(stockKeyAndStockMap) * 100D) / 100D;
		assertThat(allShareIndexValue, is(3.62));
		
	}
	
	@Test
	public void testGivenValidTradeData_shouldCheckVolumeWeightedStockPrice_forLast15minsTrades() {
		
		TradeModel popTrade = new TradeModel(TradeEnum.BUY, 2, 100.00, LocalDateTime.now().minusMinutes(20), stockDao.findByStockSymbol("POP"));
		TradeModel aleTrade = new TradeModel(TradeEnum.SELL, 2, 60.00, LocalDateTime.now(), stockDao.findByStockSymbol("ALE"));
		TradeModel joeTrade = new TradeModel(TradeEnum.SELL, 2, 60.00, LocalDateTime.now(), stockDao.findByStockSymbol("JOE"));
		// the popTrade happened more than 15 minutes before therefore will not be picked up for VWSP Evaluation
		tradeDao.addTrade(popTrade); 
		tradeDao.addTrade(aleTrade);
		tradeDao.addTrade(joeTrade);
		
		double volumeWeightedStockPrice = tradeSer.calculateVolumeWeightedStockPriceInTimeRange();
		assertThat(volumeWeightedStockPrice, is(60.0));
	}
	
}
