package com.iquant.util;

import com.iquant.common.DateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CountKRIUtil {

	/**
	 *  计算整体风险指标数据
	 * @param output
	 * @return
	 */
	public static Map<String, Object> countKRI(String output) {
		Map<String, Object> data = new HashMap<String, Object>();
		KriUtil kriUtil = new KriUtil();
		JSONObject outputJson = JSONObject.fromObject(output);
		JSONArray strategyReturnRateJson = JSONArray.fromObject(outputJson
				.getString("account"));
		JSONArray benchmarkReturnRateJson = JSONArray.fromObject(outputJson
				.getString("benchmark"));
		JSONArray worthJson = JSONArray.fromObject(outputJson
				.getString("worth"));
		JSONArray orderJson = JSONArray.fromObject(outputJson
				.getString("order"));
//		JSONArray buyAdjustJson = JSONArray.fromObject(outputJson
//				.getString("buyAdjust"));
		JSONArray logJson = JSONArray.fromObject(outputJson
				.getString("log"));

		List<Double> strategyReturnRateArray = new ArrayList<Double>();
		for (int i = 0; i < strategyReturnRateJson.size(); i++) {
			String[] str = strategyReturnRateJson.getString(i).split(":");
			strategyReturnRateArray.add(Double.parseDouble(str[1]) / 100);
		}

		List<Double> strategyWorth = new ArrayList<Double>();
		List<Double> strategyGain = new ArrayList<Double>();
		for (int i = 0; i < worthJson.size(); i++) {
			String[] str = worthJson.getString(i).split(":");
			String[] str2 = str[1].split(",");
			strategyWorth.add(Double.parseDouble(str2[0]));
			if (i > 0)
				strategyGain.add(strategyWorth.get(i)
						/ strategyWorth.get(i - 1) - 1);
		}

		List<Double> benchmarkWorth = new ArrayList<Double>();
		List<Double> benchmarkGain = new ArrayList<Double>();
		for (int i = 0; i < benchmarkReturnRateJson.size(); i++) {
			String[] str = benchmarkReturnRateJson.getString(i).split(":");
			String[] str2 = str[1].split(",");
			if (i == 0) {
				benchmarkReturnRateJson.set(i, str[0] + ":" + str2[0]);

				benchmarkWorth.add(Double.parseDouble(str2[2]));
			} else {
				String[] lastStr = benchmarkReturnRateJson.getString(i - 1)
						.split(":");
				String[] lastStr2 = lastStr[1].split(",");
				double newReturnRate = ((1 + Double.parseDouble(lastStr2[0]) / 100) * (1 + Double.parseDouble(str2[0]) / 100)
						- 1) * 100;
				benchmarkReturnRateJson.set(i, str[0] + ":" + newReturnRate);
				benchmarkWorth.add(Double.parseDouble(str2[2]));

				benchmarkGain.add(benchmarkWorth.get(i)
						/ benchmarkWorth.get(i - 1) - 1);
			}
		}

		double buy = 0.0;
		double sell = 0.0;
		// order[] 数组
		for (int i = 0; i < orderJson.size(); i++) {
			// order的对象
			JSONObject dataObject = JSONObject.fromObject(orderJson.get(i));
			
			for (Iterator it2 = dataObject.keys(); it2.hasNext();) {
				
				 // 时间日期的key值
				String key2 = (String) it2.next();
				
				// 日期下的时间值
				JSONObject dataObject2 = JSONObject.fromObject(dataObject
						.getString(key2));
				
				// 遍历日期下的所有分钟
				for (Iterator it3 = dataObject2.keys(); it3.hasNext();) {
					
					// 分钟的key值
					String key3 = (String) it3.next();
//					String[] str = dataObject2.getString(key3).split(",");
//					double tradePrice = Double.parseDouble(str[2]);
//					if (tradePrice > 0)
//						buy += tradePrice;
//					else
//						sell += tradePrice;
					
					JSONObject dataObject3 = dataObject2.getJSONObject(key3);
					for(Iterator it4 = dataObject3.keys(); it4.hasNext();){
						
						String key4 = (String) it4.next();
						String[] str = dataObject3.getString(key4).split(",");
						double tradePrice = Double.parseDouble(str[2]);
						if (tradePrice > 0)
							buy += tradePrice;
						else
							sell += tradePrice;						
						
					}
					
					
				}
			}

		}

		double strategyReturnRate = 0.0;
		if(strategyWorth != null && strategyWorth.size() > 0){
			strategyReturnRate = kriUtil.annualizedReturnRate(
					strategyWorth.get(0),
					strategyWorth.get(strategyWorth.size() - 1),
					strategyWorth.size());
		}
		
		double benchmarkReturnRate = 0.0;
		if(benchmarkWorth != null && benchmarkWorth.size() > 0){
			benchmarkReturnRate	= kriUtil.annualizedReturnRate(
				benchmarkWorth.get(0),
				benchmarkWorth.get(benchmarkWorth.size() - 1),
				benchmarkWorth.size());
		}
		double beta = kriUtil.beta(strategyGain, benchmarkGain);
		double alpha = kriUtil.alpha(beta, strategyReturnRate,
				benchmarkReturnRate);
		double volatility = kriUtil.annualizedVolatility(strategyGain);
		double sharpeRatio = kriUtil
				.sharpeRatio(volatility, strategyReturnRate);
		double informationRatio = kriUtil.annualizedInformationRatio(
				strategyReturnRate, benchmarkReturnRate, strategyGain,
				benchmarkGain);
		double maxDrawdown = kriUtil.maxDrawdown(strategyWorth);
		double turnoverRate = kriUtil.turnoverRate(buy, sell, strategyWorth);

		String logStr = logJson.toString();
//		String logStr = "";
//		for (int i = 0; i < logJson.size(); i++) {
//			String temp = logJson.getString(i);
//			String[] str1 = temp.split(":");
//			if(str1.length > 1){
//				String[] str2 = str1[1].split(",");
//				if (Integer.parseInt(str2[1]) > 0) {
//					logStr += str1[0] + " 09:30:00 - INFO - 原始下单数量为" + str2[1]
//							+ "，买入股票数量必须是100的倍数，调整为" + str2[2] + "<br>";
//					logStr += str1[0] + " 09:30:00 - INFO - Buying " + str2[0]
//							+ "<br>";
//				} else if (Integer.parseInt(str2[1]) < 0) {
//					logStr += str1[0] + " 09:30:00 - INFO - Selling " + str2[0]
//							+ "<br>";
//				}
//			}
//		}

		data.put("strategy", strategyReturnRateJson);
		data.put("benchmark", benchmarkReturnRateJson);
		data.put("strategyReturnRate", strategyReturnRate);
		data.put("benchmarkReturnRate", benchmarkReturnRate);
		data.put("alpha", alpha);
		data.put("beta", beta);
		data.put("sharpeRatio", sharpeRatio);
		data.put("volatility", volatility);
		data.put("informationRatio", informationRatio);
		data.put("maxDrawdown", maxDrawdown);
		data.put("turnoverRate", turnoverRate);
		data.put("log", logStr);

		return data;
	}

	/**
	 *  计算风险指标按月详细数据
	 * @param output
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> countKRIDetail(String output) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		KriUtil kriUtil = new KriUtil();
		DecimalFormat precentFormat = new DecimalFormat("0.00%");
		SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM");

		JSONObject outputJson = JSONObject.fromObject(output);
		JSONArray strategyReturnRateJson = JSONArray.fromObject(outputJson
				.getString("account"));
		JSONArray benchmarkReturnRateJson = JSONArray.fromObject(outputJson
				.getString("benchmark"));
		JSONArray worthJson = JSONArray.fromObject(outputJson
				.getString("worth"));
		JSONArray orderJson = JSONArray.fromObject(outputJson
				.getString("order"));
//		JSONArray buyAdjustJson = JSONArray.fromObject(outputJson
//				.getString("buyAdjust"));
		JSONArray logJson = JSONArray.fromObject(outputJson
				.getString("log"));
		String start = outputJson.getString("start");
		String end = outputJson.getString("end");

		/*
		 * 将原始数据分割成每月一份放入对应MAP中
		 */
		Map<Date, List<Double>> strategyReturnRateMap = new HashMap<Date, List<Double>>();
		List<Double> strategyReturnRateTemp = new ArrayList<Double>();
		int strategyReturnRateMonth = 0;
		for (int i = 0; i < strategyReturnRateJson.size(); i++) {
			String[] str = strategyReturnRateJson.getString(i).split(":");
			Date date = DateUtil.getString2Date(str[0], "yyyy-MM-dd");
			if (i == 0) {
				strategyReturnRateMonth = DateUtil.getDateMonth(date);
				strategyReturnRateTemp.add(Double.parseDouble(str[1]) / 100);
			} else {
				if (DateUtil.getDateMonth(date) == strategyReturnRateMonth) {
					strategyReturnRateTemp
							.add(Double.parseDouble(str[1]) / 100);
				} else {
					strategyReturnRateMap.put(
							DateUtil.getDatePreMonthFirstDay(date),
							strategyReturnRateTemp);
					strategyReturnRateMonth = DateUtil.getDateMonth(date);
					strategyReturnRateTemp = new ArrayList<Double>();
					strategyReturnRateTemp
							.add(Double.parseDouble(str[1]) / 100);
				}
			}
			if (i == strategyReturnRateJson.size() - 1)
				strategyReturnRateMap.put(
						DateUtil.getDateThisMonthFirstDay(date),
						strategyReturnRateTemp);
		}

		List<Double> strategyWorth = new ArrayList<Double>();
		Map<Date, List<Double>> strategyWorthMap = new HashMap<Date, List<Double>>();
		Map<Date, List<Double>> strategyGainMap = new HashMap<Date, List<Double>>();
		List<Double> strategyWorthTemp = new ArrayList<Double>();
		List<Double> strategyGainTemp = new ArrayList<Double>();
		int strategyMonth = 0;
		for (int i = 0; i < worthJson.size(); i++) {
			String[] str = worthJson.getString(i).split(":");
			String[] str2 = str[1].split(",");
			strategyWorth.add(Double.parseDouble(str2[0]));
			Date date = DateUtil.getString2Date(str[0], "yyyy-MM-dd");
			if (i == 0) {
				strategyMonth = DateUtil.getDateMonth(date);
				strategyWorthTemp.add(Double.parseDouble(str2[0]));
			} else {
				double gain = strategyWorth.get(i) / strategyWorth.get(i - 1)
						- 1;
				if (DateUtil.getDateMonth(date) == strategyMonth) {
					strategyWorthTemp.add(Double.parseDouble(str2[0]));
					strategyGainTemp.add(gain);
				} else {
					strategyWorthMap.put(
							DateUtil.getDatePreMonthFirstDay(date),
							strategyWorthTemp);
					strategyGainMap.put(DateUtil.getDatePreMonthFirstDay(date),
							strategyGainTemp);
					strategyWorthTemp = new ArrayList<Double>();
					strategyGainTemp = new ArrayList<Double>();
					strategyWorthTemp.add(Double.parseDouble(str2[0]));
					strategyGainTemp.add(gain);
					strategyMonth = DateUtil.getDateMonth(date);
				}
			}

			if (i == worthJson.size() - 1) {
				strategyWorthMap.put(DateUtil.getDateThisMonthFirstDay(date),
						strategyWorthTemp);
				strategyGainMap.put(DateUtil.getDateThisMonthFirstDay(date),
						strategyGainTemp);
			}
		}

		List<Double> benchmarkWorth = new ArrayList<Double>();
		Map<Date, List<Double>> benchmarkWorthMap = new HashMap<Date, List<Double>>();
		Map<Date, List<Double>> benchmarkGainMap = new HashMap<Date, List<Double>>();
		List<Double> benchmarkWorthTemp = new ArrayList<Double>();
		List<Double> benchmarkGainTemp = new ArrayList<Double>();
		int benchmarkMonth = 0;
		for (int i = 0; i < benchmarkReturnRateJson.size(); i++) {
			String[] str = benchmarkReturnRateJson.getString(i).split(":");
			String[] str2 = str[1].split(",");
			Date date = DateUtil.getString2Date(str[0], "yyyy-MM-dd");
			if (i == 0) {
				benchmarkMonth = DateUtil.getDateMonth(date);
				benchmarkReturnRateJson.set(i, str[0] + ":" + str2[0]);
				double worth = Double.parseDouble(str2[2]);
				benchmarkWorthTemp.add(worth);
				benchmarkWorth.add(worth);
			} else {
				String[] lastStr = benchmarkReturnRateJson.getString(i - 1)
						.split(":");
				String[] lastStr2 = lastStr[1].split(",");
				double newReturnRate = ((1 + Double.parseDouble(lastStr2[0]) / 100) * (1 + Double.parseDouble(str2[0]) / 100)
						- 1) * 100;
				benchmarkReturnRateJson.set(i, str[0] + ":" + newReturnRate);
				double worth = Double.parseDouble(str2[2]);
				benchmarkWorth.add(worth);
				double gain = benchmarkWorth.get(i) / benchmarkWorth.get(i - 1)
						- 1;

				if (DateUtil.getDateMonth(date) == benchmarkMonth) {
					benchmarkWorthTemp.add(worth);
					benchmarkGainTemp.add(gain);
				} else {
					benchmarkWorthMap.put(
							DateUtil.getDatePreMonthFirstDay(date),
							benchmarkWorthTemp);
					benchmarkGainMap.put(
							DateUtil.getDatePreMonthFirstDay(date),
							benchmarkGainTemp);
					benchmarkMonth = DateUtil.getDateMonth(date);
					benchmarkWorthTemp = new ArrayList<Double>();
					benchmarkGainTemp = new ArrayList<Double>();
					benchmarkWorthTemp.add(worth);
					benchmarkGainTemp.add(gain);
				}
			}
			if (i == benchmarkReturnRateJson.size() - 1) {
				benchmarkWorthMap.put(DateUtil.getDateThisMonthFirstDay(date),
						benchmarkWorthTemp);
				benchmarkGainMap.put(DateUtil.getDateThisMonthFirstDay(date),
						benchmarkGainTemp);
			}
		}

		/*
		 * 计算1个月、3个月、6个月、12个月的指标
		 */
		List<String> returnRateList = new ArrayList<String>();
		List<String> betaList = new ArrayList<String>();
		List<String> alphaList = new ArrayList<String>();
		List<String> volatilityList = new ArrayList<String>();
		List<String> sharpeRatioList = new ArrayList<String>();
		List<String> informationRatioList = new ArrayList<String>();
		List<String> maxDrawdownList = new ArrayList<String>();

		Date startDate = DateUtil.getDateThisMonthFirstDay(DateUtil
				.getString2Date(start, "yyyy-MM-dd"));
		Date endDate = DateUtil.getDateThisMonthFirstDay(DateUtil
				.getString2Date(end, "yyyy-MM-dd"));

		for (Date tempDate = new Date(startDate.getTime()); tempDate.getTime() <= endDate
				.getTime(); tempDate = DateUtil
				.getDateNextMonthFirstDay(tempDate)) {
			String[] strategyReturnRate = { "--", "--", "--", "--" };
			String[] benchmarkReturnRate = { "--", "--", "--", "--" };
			String[] diffReturnRate = { "--", "--", "--", "--" };
			String[] beta = { "--", "--", "--", "--" };
			String[] alpha = { "--", "--", "--", "--" };
			String[] volatility = { "--", "--", "--", "--" };
			String[] sharpeRatio = { "--", "--", "--", "--" };
			String[] informationRatio = { "--", "--", "--", "--" };
			String[] maxDrawdown = { "--", "--", "--", "--" };

			if (DateUtil.getMonthDiff(tempDate, startDate) >= 0) {
				List<Double> strategyWorthArray = strategyWorthMap
						.get(tempDate);
				List<Double> strategyGainArray = strategyGainMap.get(tempDate);
				List<Double> benchmarkWorthArray = benchmarkWorthMap
						.get(tempDate);
				List<Double> benchmarkGainArray = benchmarkGainMap
						.get(tempDate);

				if (strategyWorthArray != null && strategyWorthArray.size() > 0) {
					strategyReturnRate[0] = kriUtil.returnRate(
							strategyWorthArray.get(0), strategyWorthArray
									.get(strategyWorthArray.size() - 1))
							+ "";
					benchmarkReturnRate[0] = kriUtil.returnRate(
							benchmarkWorthArray.get(0), benchmarkWorthArray
									.get(benchmarkWorthArray.size() - 1))
							+ "";
					diffReturnRate[0] = precentFormat.format(Double
							.parseDouble(strategyReturnRate[0])
							- Double.parseDouble(benchmarkReturnRate[0]));

					beta[0] = kriUtil.beta(strategyGainArray,
							benchmarkGainArray) + "";
					alpha[0] = kriUtil.alpha(Double.parseDouble(beta[0]),
							Double.parseDouble(strategyReturnRate[0]),
							Double.parseDouble(benchmarkReturnRate[0]))
							+ "";
					volatility[0] = kriUtil.volatility(strategyGainArray) + "";
					sharpeRatio[0] = kriUtil.sharpeRatio(
							Double.parseDouble(volatility[0]),
							Double.parseDouble(strategyReturnRate[0]))
							+ "";
					informationRatio[0] = kriUtil.informationRatio(
							Double.parseDouble(strategyReturnRate[0]),
							Double.parseDouble(benchmarkReturnRate[0]),
							strategyGainArray, benchmarkGainArray) + "";
					maxDrawdown[0] = kriUtil.maxDrawdown(strategyWorthArray)
							+ "";

					strategyReturnRate[0] = precentFormat.format(Double
							.parseDouble(strategyReturnRate[0]));
					benchmarkReturnRate[0] = precentFormat.format(Double
							.parseDouble(benchmarkReturnRate[0]));
					alpha[0] = precentFormat.format(Double
							.parseDouble(alpha[0]));
					volatility[0] = precentFormat.format(Double
							.parseDouble(volatility[0]));
					maxDrawdown[0] = precentFormat.format(Double
							.parseDouble(maxDrawdown[0]));
				}
			}
			if (DateUtil.getMonthDiff(tempDate, startDate) >= 2) {
				List<Double> strategyWorthArray = new ArrayList<Double>();
				List<Double> strategyGainArray = new ArrayList<Double>();
				List<Double> benchmarkWorthArray = new ArrayList<Double>();
				List<Double> benchmarkGainArray = new ArrayList<Double>();

				for (int i = -2; i <= 0; i++) {
					Date temp = DateUtil.getDateNumMonthFirstDay(tempDate, i);
					if (strategyWorthMap.containsKey(temp)) {
						strategyWorthArray.addAll(strategyWorthMap.get(temp));
						strategyGainArray.addAll(strategyGainMap.get(temp));
						benchmarkWorthArray.addAll(benchmarkWorthMap.get(temp));
						benchmarkGainArray.addAll(benchmarkGainMap.get(temp));
					}
				}

				if (strategyWorthArray != null && strategyWorthArray.size() > 0) {
					strategyReturnRate[1] = kriUtil.returnRate(
							strategyWorthArray.get(0), strategyWorthArray
									.get(strategyWorthArray.size() - 1))
							+ "";
					benchmarkReturnRate[1] = kriUtil.returnRate(
							benchmarkWorthArray.get(0), benchmarkWorthArray
									.get(benchmarkWorthArray.size() - 1))
							+ "";
					diffReturnRate[1] = precentFormat.format(Double
							.parseDouble(strategyReturnRate[1])
							- Double.parseDouble(benchmarkReturnRate[1]));

					beta[1] = kriUtil.beta(strategyGainArray,
							benchmarkGainArray) + "";
					alpha[1] = kriUtil.alpha(Double.parseDouble(beta[1]),
							Double.parseDouble(strategyReturnRate[1]),
							Double.parseDouble(benchmarkReturnRate[1]))
							+ "";
					volatility[1] = kriUtil.volatility(strategyGainArray) + "";
					sharpeRatio[1] = kriUtil.sharpeRatio(
							Double.parseDouble(volatility[1]),
							Double.parseDouble(strategyReturnRate[1]))
							+ "";
					informationRatio[1] = kriUtil.informationRatio(
							Double.parseDouble(strategyReturnRate[1]),
							Double.parseDouble(benchmarkReturnRate[1]),
							strategyGainArray, benchmarkGainArray) + "";
					maxDrawdown[1] = kriUtil.maxDrawdown(strategyWorthArray)
							+ "";

					strategyReturnRate[1] = precentFormat.format(Double
							.parseDouble(strategyReturnRate[1]));
					benchmarkReturnRate[1] = precentFormat.format(Double
							.parseDouble(benchmarkReturnRate[1]));
					alpha[1] = precentFormat.format(Double
							.parseDouble(alpha[1]));
					volatility[1] = precentFormat.format(Double
							.parseDouble(volatility[1]));
					maxDrawdown[1] = precentFormat.format(Double
							.parseDouble(maxDrawdown[1]));
				}

			}
			if (DateUtil.getMonthDiff(tempDate, startDate) >= 5) {
				List<Double> strategyWorthArray = new ArrayList<Double>();
				List<Double> strategyGainArray = new ArrayList<Double>();
				List<Double> benchmarkWorthArray = new ArrayList<Double>();
				List<Double> benchmarkGainArray = new ArrayList<Double>();
				for (int i = -5; i <= 0; i++) {
					Date temp = DateUtil.getDateNumMonthFirstDay(tempDate, i);
					if (strategyWorthMap.containsKey(temp)) {
						strategyWorthArray.addAll(strategyWorthMap.get(temp));
						strategyGainArray.addAll(strategyGainMap.get(temp));
						benchmarkWorthArray.addAll(benchmarkWorthMap.get(temp));
						benchmarkGainArray.addAll(benchmarkGainMap.get(temp));
					}
				}

				if (strategyWorthArray != null && strategyWorthArray.size() > 0) {
					strategyReturnRate[2] = kriUtil.returnRate(
							strategyWorthArray.get(0), strategyWorthArray
									.get(strategyWorthArray.size() - 1))
							+ "";
					benchmarkReturnRate[2] = kriUtil.returnRate(
							benchmarkWorthArray.get(0), benchmarkWorthArray
									.get(benchmarkWorthArray.size() - 1))
							+ "";

					diffReturnRate[2] = precentFormat.format(Double
							.parseDouble(strategyReturnRate[2])
							- Double.parseDouble(benchmarkReturnRate[2]));

					beta[2] = kriUtil.beta(strategyGainArray,
							benchmarkGainArray) + "";
					alpha[2] = kriUtil.alpha(Double.parseDouble(beta[2]),
							Double.parseDouble(strategyReturnRate[2]),
							Double.parseDouble(benchmarkReturnRate[2]))
							+ "";
					volatility[2] = kriUtil.volatility(strategyGainArray) + "";
					sharpeRatio[2] = kriUtil.sharpeRatio(
							Double.parseDouble(volatility[2]),
							Double.parseDouble(strategyReturnRate[2]))
							+ "";
					informationRatio[2] = kriUtil.informationRatio(
							Double.parseDouble(strategyReturnRate[2]),
							Double.parseDouble(benchmarkReturnRate[2]),
							strategyGainArray, benchmarkGainArray) + "";
					maxDrawdown[2] = kriUtil.maxDrawdown(strategyWorthArray)
							+ "";

					strategyReturnRate[2] = precentFormat.format(Double
							.parseDouble(strategyReturnRate[2]));
					benchmarkReturnRate[2] = precentFormat.format(Double
							.parseDouble(benchmarkReturnRate[2]));
					alpha[2] = precentFormat.format(Double
							.parseDouble(alpha[2]));
					volatility[2] = precentFormat.format(Double
							.parseDouble(volatility[2]));
					maxDrawdown[2] = precentFormat.format(Double
							.parseDouble(maxDrawdown[2]));
				}
			}
			if (DateUtil.getMonthDiff(tempDate, startDate) >= 11) {
				List<Double> strategyWorthArray = new ArrayList<Double>();
				List<Double> strategyGainArray = new ArrayList<Double>();
				List<Double> benchmarkWorthArray = new ArrayList<Double>();
				List<Double> benchmarkGainArray = new ArrayList<Double>();
				for (int i = -11; i <= 0; i++) {
					Date temp = DateUtil.getDateNumMonthFirstDay(tempDate, i);
					if (strategyWorthMap.containsKey(temp)) {
						strategyWorthArray.addAll(strategyWorthMap.get(temp));
						strategyGainArray.addAll(strategyGainMap.get(temp));
						benchmarkWorthArray.addAll(benchmarkWorthMap.get(temp));
						benchmarkGainArray.addAll(benchmarkGainMap.get(temp));
					}
				}

				if (strategyWorthArray != null && strategyWorthArray.size() > 0) {
					strategyReturnRate[3] = kriUtil.returnRate(
							strategyWorthArray.get(0), strategyWorthArray
									.get(strategyWorthArray.size() - 1))
							+ "";
					benchmarkReturnRate[3] = kriUtil.returnRate(
							benchmarkWorthArray.get(0), benchmarkWorthArray
									.get(benchmarkWorthArray.size() - 1))
							+ "";

					diffReturnRate[3] = precentFormat.format(Double
							.parseDouble(strategyReturnRate[3])
							- Double.parseDouble(benchmarkReturnRate[3]));

					beta[3] = kriUtil.beta(strategyGainArray,
							benchmarkGainArray) + "";
					alpha[3] = kriUtil.alpha(Double.parseDouble(beta[3]),
							Double.parseDouble(strategyReturnRate[3]),
							Double.parseDouble(benchmarkReturnRate[3]))
							+ "";
					volatility[3] = kriUtil.volatility(strategyGainArray) + "";
					sharpeRatio[3] = kriUtil.sharpeRatio(
							Double.parseDouble(volatility[3]),
							Double.parseDouble(strategyReturnRate[3]))
							+ "";
					informationRatio[3] = kriUtil.informationRatio(
							Double.parseDouble(strategyReturnRate[3]),
							Double.parseDouble(benchmarkReturnRate[3]),
							strategyGainArray, benchmarkGainArray) + "";
					maxDrawdown[3] = kriUtil.maxDrawdown(strategyWorthArray)
							+ "";

					strategyReturnRate[3] = precentFormat.format(Double
							.parseDouble(strategyReturnRate[3]));
					benchmarkReturnRate[3] = precentFormat.format(Double
							.parseDouble(benchmarkReturnRate[3]));
					alpha[3] = precentFormat.format(Double
							.parseDouble(alpha[3]));
					volatility[3] = precentFormat.format(Double
							.parseDouble(volatility[3]));
					maxDrawdown[3] = precentFormat.format(Double
							.parseDouble(maxDrawdown[3]));
				}
			}

			returnRateList.add(sformat.format(tempDate) + ":"
					+ strategyReturnRate[0] + "&" + benchmarkReturnRate[0]
					+ "&" + diffReturnRate[0] + "," + strategyReturnRate[1]
					+ "&" + benchmarkReturnRate[1] + "&" + diffReturnRate[1]
					+ "," + strategyReturnRate[2] + "&"
					+ benchmarkReturnRate[2] + "&" + diffReturnRate[2] + ","
					+ strategyReturnRate[3] + "&" + benchmarkReturnRate[3]
					+ "&" + diffReturnRate[3]);
			alphaList.add(sformat.format(tempDate) + ":" + alpha[0] + ","
					+ alpha[1] + "," + alpha[2] + "," + alpha[3]);
			betaList.add(sformat.format(tempDate) + ":" + beta[0] + ","
					+ beta[1] + "," + beta[2] + "," + beta[3]);
			volatilityList.add(sformat.format(tempDate) + ":" + volatility[0]
					+ "," + volatility[1] + "," + volatility[2] + ","
					+ volatility[3]);
			sharpeRatioList.add(sformat.format(tempDate) + ":" + sharpeRatio[0]
					+ "," + sharpeRatio[1] + "," + sharpeRatio[2] + ","
					+ sharpeRatio[3]);
			informationRatioList.add(sformat.format(tempDate) + ":"
					+ informationRatio[0] + "," + informationRatio[1] + ","
					+ informationRatio[2] + "," + informationRatio[3]);
			maxDrawdownList.add(sformat.format(tempDate) + ":" + maxDrawdown[0]
					+ "," + maxDrawdown[1] + "," + maxDrawdown[2] + ","
					+ maxDrawdown[3]);

		}

		String logStr = logJson.toString();
//		String logStr = "";
//		for (int i = 0; i < logJson.size(); i++) {
//			String temp = logJson.getString(i);
//			String[] str1 = temp.split(":");
//			String[] str2 = str1[1].split(",");
//			if (Integer.parseInt(str2[1]) > 0) {
//				logStr += str1[0] + " 09:30:00 - INFO - 原始下单数量为" + str2[1]
//						+ "，买入股票数量必须是100的倍数，调整为" + str2[2] + "<br>";
//				logStr += str1[0] + " 09:30:00 - INFO - Buying " + str2[0]
//						+ "<br>";
//			} else if (Integer.parseInt(str2[1]) < 0) {
//				logStr += str1[0] + " 09:30:00 - INFO - Selling " + str2[0]
//						+ "<br>";
//			}
//		}
		data.put("returnRateList", returnRateList);
		data.put("alphaList", alphaList);
		data.put("betaList", betaList);
		data.put("volatilityList", volatilityList);
		data.put("sharpeRatioList", sharpeRatioList);
		data.put("informationRatioList", informationRatioList);
		data.put("maxDrawdownList", maxDrawdownList);
		data.put("order", orderJson);
		data.put("worth", worthJson);
		data.put("log", logStr);

		return data;
	}
	
}
