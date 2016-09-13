#!//bin/python
# -*- coding: utf-8 -*-

import types
import urllib2
import json        
import datetime
import time
import DataAPI
import pandas as pd
import tushare as ts

benchmarkMap = {"SH":"000001", "SH50":"000016", "SH180":"000010", "ZZ500":"000905", "SZ":"399001", "ZXB":"399101", "HS300":"399300", "FUND_SH":"000011", "FUND_SZ"
:"399305"}
ts.set_token('8a72d90cf841a0dcc4d2d4cf55e48c603e0b3d21295c5b0f93f4be846158d903')
mt = ts.Master()

def registerUrl(url):
        try:
                data = urllib2.urlopen(url).read()
                return data
        except Exception,e:
                print e

def isNum(value):
        try:
                x = int(value)
        except TypeError:
                return False
        except ValueError:
                return False
        except Exception, e:
                return False
        else:
                return True

def getBenchmarkHistory(start, end, benchmark):
	result = []
	
	df = pd.DataFrame()
	if benchmarkMap.has_key(benchmark):
		df = ts.get_h_data(benchmarkMap[benchmark], start=start, end=end, index=True)
	else:
		df = ts.get_h_data(benchmark, start=start, end=end, autype=None)
	
	lastClose = getLastEndprice(start, benchmark)

	df = df.sort_index()	
	for index, row in df.iterrows():
		upDownRatio = (row["close"] - lastClose) / lastClose * 100
		lastClose = row["close"] 
		temp = str(index.date()) + ":" + str(upDownRatio) + "," + str(row["open"]) + "," + str(row["close"])
		result.append(temp) 

        return result

def getDateList(start, end):
        result = []
	start = start.replace("-", "")
	end = end.replace("-", "")
	df = mt.TradeCal(exchangeCD='XSHG', beginDate=start, endDate=end, field='calendarDate,isOpen,prevTradeDate')        
	
	for index, row in df.iterrows():
		result.append(str(row["calendarDate"]))
	
	return result

def getStockTradeHistory(start, end, stockcode):
	url = "http://10.13.85.179:8831/?key=getStockInfo&stockCode=" + stockcode + "&starttime=" + start + "&endtime=" + end
        jsonData = registerUrl(url)
        value = json.loads(jsonData)

        result = {}
        datavalue = value["data"]
        for item in datavalue:
                keylist = item.keys()
                hashmap = {}
                for key in keylist:
                        hashmap[key.encode("utf-8")] = item[key] if isNum(item[key]) else item[key].encode("utf-8")
                result[item["trade_day"].encode("utf-8")] = hashmap

        return result	

def getStockHistory(start, end, stockcode):
        url = "http://10.13.85.179:8831/?key=getStock_Recovery&stockCode=" + stockcode + "&starttime=" + start + "&endtime=" + end
        jsonData = registerUrl(url)
        value = json.loads(jsonData)

        result = {}
        datavalue = value["data"][stockcode]
        for item in datavalue:
                keylist = item.keys()
                hashmap = {}
                for key in keylist:
                        hashmap[key.encode("utf-8")] = item[key] if isNum(item[key]) else item[key].encode("utf-8")
                result[item["trade_day"].encode("utf-8")] = hashmap

        return result

def getMinuteHistory(date, universe):
        stockStr = (',').join(universe)
	url = "http://10.13.85.179:8831/?key=AllDay_StockByMinute&stockCode=" + stockStr + "&tradeDay=" + date
	jsonData = registerUrl(url)
        value = json.loads(jsonData)
	
	result = {}
	datavalue = value["data"]
	for (stock,data) in  datavalue.items():
		stockData = {}
		for item in data:
			keylist = item.keys()
                	hashmap = {}
                	for key in keylist:
                		hashmap[key.encode("utf-8")] = item[key] if isNum(item[key]) else item[key].encode("utf-8")
               		stockData[item["tradeTime"].encode("utf-8")] = hashmap	
		result[stock.encode("utf-8")] = stockData

	return result

def getStockHistoryList(start, end, stockcode):
	url = "http://10.13.85.179:8831/?key=getStock_Recovery&stockCode=" + stockcode + "&starttime=" + start + "&endtime=" + end
        jsonData = registerUrl(url)
        value = json.loads(jsonData)

        result = []
        datavalue = value["data"][stockcode]
        for item in datavalue:
                keylist = item.keys()
                hashmap = {}
                for key in keylist:
                        hashmap[key.encode("utf-8")] = item[key] if isNum(item[key]) else item[key].encode("utf-8")
                result.append(hashmap)

	result.reverse()
        return result

def getHistory(start, end, universe):
        result = {}
	for stock in universe:
		stockData = {}
		df = ts.get_h_data(stock, start=start, end=end)
		for index, row in df.iterrows():
			hashmap = {}
			for col in df.columns:
				hashmap[col] = row[col]
			stockData[str(index.date())] = hashmap
		result[stock] = stockData	

        return result

def getHistoryList(start, end, universe):
        result = {}
        stockStr = (',').join(universe)
        url = "http://10.13.85.179:8831/?key=getStock_Recovery&stockCode=" + stockStr + "&starttime=" + start + "&endtime=" + end
        jsonData = registerUrl(url)
        value = json.loads(jsonData)
        datavalue = value["data"]

	for (stock, item) in datavalue.items():
                stockData = []
                item.reverse()
                for item1 in item:
                        keylist = item1.keys()
                        hashmap = {}
                        for key in keylist:
                                hashmap[key.encode("utf-8")] = item1[key] if isNum(item1[key]) else item1[key].encode("utf-8")
                        stockData.append(hashmap)
                result[stock.encode("utf-8")] = stockData

        return result

def getLastEndprice(dateStr, stockcode):
	date = getPreTradedate(dateStr.replace('-',''))
	df = pd.DataFrame()
        if benchmarkMap.has_key(stockcode):
                df = ts.get_h_data(benchmarkMap[stockcode], start=date, end=date, index=True)
        else:
                df = ts.get_h_data(stockcode, start=date, end=date, autype=None)

	return df.loc[date, 'close']
	
def getPreDayStockHistoryList(day, end, stockcode):
	url = "http://10.13.85.179:8831/?key=getStockByDays&stockCode=" + stockcode + "&endtime=" + end + "&days=" + str(day)
	jsonData = registerUrl(url)
        value = json.loads(jsonData)
	result = []

	datavalue = value["data"]
        for item in datavalue:
                keylist = item.keys()
                hashmap = {}
                for key in keylist:
                        hashmap[key.encode("utf-8")] = item[key] if isNum(item[key]) else item[key].encode("utf-8")
                result.append(hashmap)

        result.reverse()
        return result	

def getPreDayHistoryList(day, end, universe):
        result = {}
        for stock in universe:
                url = "http://10.13.85.179:8831/?key=getStockByDays&stockCode=" + stock + "&endtime=" + end + "&days=" + str(day)
                jsonData = registerUrl(url)
                value = json.loads(jsonData)

                stockData = []
                datavalue = value["data"]
                for item in datavalue:
                        keylist = item.keys()
                        hashmap = {}
                        for key in keylist:
                                hashmap[key.encode("utf-8")] = item[key] if isNum(item[key]) else item[key].encode("utf-8")
                        stockData.append(hashmap)
                stockData.reverse()
                result[stock] = stockData

        return result

def getStart5MAvgPrice(stock, date):
	url = "http://10.13.85.179:8831/?key=Get_Minute_StockInfo&stockCode=" + stock + "&StartTradeDay=" + date + "&tradeDay=" + date
	jsonData = registerUrl(url)
	value = json.loads(jsonData)
	datavalue = value["data"]

	return datavalue[0]["average_price"]

def getPreTradedate(date):
	cal = mt.TradeCal(exchangeCD="XSHG",beginDate='19901219',endDate=date)
	cal = cal[cal['isOpen']==1]
	date = cal['calendarDate'].values[-1]
    	return date

def set_universe(symbol, date="2016-01-01"):
        url = "http://10.13.85.179:8831/?key=cacheSock_Code&reqsh=" + symbol
        jsonData = registerUrl(url)
        value = json.loads(jsonData)
        datavalue = value["data"]
        data = []

        for item in datavalue:
                data.append(item["stock_code"].encode("utf-8"))

        return data

if __name__ == '__main__':
	print getHistory("2016-01-01", "2016-02-01", ["000001", "600346"])
