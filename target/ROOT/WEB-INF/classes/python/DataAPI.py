#!/usr/bin/python
# -*- coding: utf-8 -*-
import sys
import os
from datetime import datetime
import time
import types
import urllib2
import json
import calendar as cal
import numpy as np
from pandas import Series,DataFrame
import pandas as pd
import Util

def TradeCalGet(exchangeCD, beginDate="1990-12-19", endDate="2016-12-31", field=[], pandas=1):
	url = "http://10.13.85.179:8831/?key=getTradeCalendar&exchangeCD=" + exchangeCD + "&starttime=" + beginDate + "&endtime=" + endDate
	jsonData = Util.registerUrl(url)
	value = json.loads(jsonData)
	datavalue = value["data"]
	datavalue.reverse()
	data = {}
	if not isinstance(field, list):
		field = field.split(",")
	
	if pandas:
		data["calendarDateTime"] = []
		for item in datavalue:
			keylist = item.keys()
		
			for key in keylist:
				itemvalue = item[key] if Util.isNum(item[key]) else item[key].encode("utf-8")
				if not data.has_key(key):
					data[key.encode("utf-8")] = []
				data[key.encode("utf-8")].append(itemvalue)
			data["calendarDateTime"].append(datetime.strptime(item["calendarDate"], "%Y-%m-%d"))
				
		if field == []:
			return DataFrame(data)
		else:
			return DataFrame(data, columns=field)
	else:
		pass

def MktStockFactorsOneDayGet(tradeDate, secID, field=[], pandas=1):
	stockStr = ""
        if isinstance(secID, list):
                stockStr = (',').join(secID)
        else :
                stockStr = secID

	url = "http://10.13.85.179:8831/?key=cacheStock_Factor&stockCode=" + stockStr + "&endtime=" + tradeDate	
	jsonData = Util.registerUrl(url)
        value = json.loads(jsonData)
        datavalue = value["data"]
        datavalue.reverse()
        data = {}
	if not isinstance(field, list):
                field = field.split(",")

	if pandas:
		for item in datavalue:
			keylist = []
                        if field == []:
                                keylist = item.keys()
                        else:
                                keylist = field

                        for key in keylist:
                                itemvalue = item[key] if Util.isNum(item[key]) else item[key].encode("utf-8")
                                if not data.has_key(key):
                                        data[key.encode("utf-8")] = []
                                data[key.encode("utf-8")].append(itemvalue)

		if field == []:
                        return DataFrame(data)
                else:
                        return DataFrame(data, columns=field)
	else:
		pass

def MktEqudAdjGet(secID, beginDate, endDate, field=[], pandas=1):
	stockStr = ""
	if isinstance(secID, list):
		stockStr = (',').join(secID)
	else :
		stockStr = secID

	url = "http://10.13.85.179:8831/?key=getAllSock_RecoveryByDbUtil&stockCode=" + stockStr + "&starttime=" + beginDate + "&endtime=" + endDate
	jsonData = Util.registerUrl(url)
	value = json.loads(jsonData)
        datavalue = value["data"]
        data = {}
	if not isinstance(field, list):
                field = field.split(",")

        if pandas:
		for (stock, item) in datavalue.items():
			item.reverse()
			for item1 in item:				
                        	keylist = []
                        	if field == []:
                                	keylist = item1.keys()
                        	else:
					keylist = field

                        	for key in keylist:
					itemvalue = None
					if key == "stock_code":
						itemvalue = stock
					else:
                                		itemvalue = item1[key] if Util.isNum(item1[key]) else item1[key].encode("utf-8")
                                	if not data.has_key(key):
                                        	data[key.encode("utf-8")] = []
                                	data[key.encode("utf-8")].append(itemvalue)

		if field == []:
                        return DataFrame(data)
                else:
                        return DataFrame(data, columns=field)
	else:
		pass		

def MktFutdVolGet(secID, beginDate, endDate, field=[], pandas=1):
	url = "http://10.13.85.179:8831/?key=getStock_Index_Futures&productid=" + secID + "&starttime=" + beginDate + "&endtime=" + endDate
	jsonData = Util.registerUrl(url)
        value = json.loads(jsonData)
        datavalue = value["data"]
        data = {}
	if not isinstance(field, list):
                field = field.split(",")

	if pandas:
		for item in datavalue:
			keylist = []
                        if field == []:
                                keylist = item.keys()
                        else:
                                keylist = field

                        for key in keylist:
				if key != "delta" and key != "segma":
					if key == "tradingday":
						itemvalue = time.strftime("%Y-%m-%d", time.strptime(item["tradingday"], "%Y%m%d"))
					elif key == "expiredate":
						itemvalue = time.strftime("%Y-%m-%d", time.strptime(item["expiredate"], "%Y%m%d"))
					else:
                                		itemvalue = float(item[key]) if Util.isNum(item[key]) else item[key].encode("utf-8")
                                	if not data.has_key(key):
                                        	data[key.encode("utf-8")] = []
                                	data[key.encode("utf-8")].append(itemvalue)

                if field == []:
                        return DataFrame(data)
                else:
                        return DataFrame(data, columns=field)	
	else:
		pass	

if __name__ == "__main__":
	print MktFutdVolGet(secID="IF", beginDate="2011-07-01", endDate="2011-07-30", field=[], pandas=1)
