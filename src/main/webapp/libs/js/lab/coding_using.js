var editor;
var Run;

function run() {
	
	var _isRun = false;
	
	function openRun(){
		_isRun = true;
		$(".toolnormal.tool05").attr("data-target","#");
	};
	
	function closeRun(){
		$(".toolnormal.tool05").attr("data-target","#myModal");
		_isRun = false;
	}
	
	function getRun(){
		
		return _isRun;
	}
	
	return { 
		
		GobalCloseRun : function(){
			closeRun();
		},
		
		openModal: function(){
			closeRun();
		},
		
		run: function() {
			
			if(getRun()){
				console.log("程序正在运行，请运行完成后再试");
				return;
			}
			
			openRun();
			
			var codeStr = editor.getValue();
			var id = $("#id").val();
			showChart();
			
			var front_chart = $('#result').highcharts()
			var back_chart = $('#tab01_chart').highcharts()
			if(front_chart && back_chart){
				front_chart.showLoading('正在拼命加载中...');
				back_chart.showLoading('正在拼命加载中...');
		    }else{
		    	return;
		    }
			
			$.ajax({
				url : URL_PERFIX + "/json/lab_compilePython.html", // 后台处理程序
				timeout : 150000,
				type : "post", // 数据接收方式
				async : true,
				data : {
					"codeStr" : codeStr,
					"id" : id
				},
				dataType : "text", // 接受数据格式
				success : function(text) {
					
//					var text = text.toString().replace(/NaN/g,0).replace(/Infinity/g,0);
//					var json = $.parseJSON(text)
//					
//					showStrategyResult(json,codeStr);
					echo("1");
				},
				error : function() {
					var data = {};
					setChartDesc(data);
//					data.strategy = [];
//					data.benchmark = [];
					if(front_chart && back_chart){
						front_chart.showLoading('服务器没有返回数据，可能服务器忙，请重试');
						back_chart.showLoading('服务器没有返回数据，可能服务器忙，请重试');
				    }
					console.log("服务器没有返回数据，可能服务器忙，请重试");
				}
			})
			
			
			/**
			setTimeout(function(){
				$.ajax({
					url : URL_PERFIX + "/json/lab_backTestDetail.html", // 后台处理程序
					timeout : 150000,
					type : "post", // 数据接收方式
					async : true,
					data : {
						"codeStr" : codeStr,
						"id" : id
					},
					dataType : "json", // 接受数据格式
					success : function(json) {
						
						if(json.status == "fail"){
							closeRun();
							return;
						}
						
						showStrategyDetailResult(json);
						
						closeRun();
					},
					error : function() {
						console.log("回测指标服务发生错误");
						closeRun();
//						alert("服务器没有返回数据，可能服务器忙，请重试");
					}
				})
			},1000)
			*/

		}
	}
	
	
}

function showStrategyResult(json,codeStr){
	
	var front_chart = $('#result').highcharts()
	var back_chart = $('#tab01_chart').highcharts()
	
	if(json.status  == "success"){
		$("#result_log").html(json.data.log);
	}else{
		$("#result_log").text(json.status);
	}
	
	
	$("#tab04 .log").html(json.status);
	
	console.log(json.error);
	
	if(json.error != ""){
		if(front_chart && back_chart){
			front_chart.showLoading('服务器没有返回数据，可能服务器忙，请重试');
			back_chart.showLoading('服务器没有返回数据，可能服务器忙，请重试');
	    }
		$("#tab04 .log").html(json.status + "<br/>" + json.error.toString().replace(/[^]+(Error)/g, "*********"));
		$("#result_log").html(json.error.toString().replace(/[^]+(Error)/g, "*********"));
		return;
	}
	
	var temp = '';
	
//	var codes = codeStr.split("\n");
//	for(var i in codes){
//		if(codes[i].indexOf("start") != -1 || codes[i].indexOf("end") != -1 || codes[i].indexOf("benchmark") != -1 || codes[i].indexOf("capital_base") != -1){
//			temp += codes[i] + '<br/>';
//		}
//	}
	$("#tab05_con").html(temp);
	//$("#result_desc").html(json.data);

	setChartDesc(json.data);
	loadChartData(json.data);
}

function showStrategyDetailResult(json){
	
	var data = json.data;
	
	var tab03_html = '';
	for(var i3 in data.worth){
		tab03_html += "<tr>";
		var o = data.worth[i3].split(":")[0];
		var ov =  data.worth[i3].split(":")[1].split(",");
		tab03_html += "<td>" + o + "</td>"
		tab03_html += "<td>" + ov[0] + "</td>"
		tab03_html += "<td>" + ov[1] + "</td>"
		tab03_html += "<td>" + ov[2] + "</td>"
		tab03_html += "</tr>";
	}
	$("#tab03 tbody").html(tab03_html);
	
	var tab02_html = '';
	for(var i2 in data.order){
		tab02_html += "";
		
		for(var time in data.order[i2]){
			var vs = data.order[i2][time];

			var temp_tab03_html = '';
			var temp_sum = 0;
			var temp_index = 0;
			for(var st in vs){
				var v = vs[st].split(",");
				temp_tab03_html += '</tr>';
				temp_tab03_html +=  '<tr class="openrow">';
				temp_tab03_html +=  '<td>' + time + '</td>';
				temp_tab03_html +=  '<td>' + st + '</td>';
				temp_tab03_html +=  '<td><span class="' + (v[2] > 0 ? 'redfont' : 'greenfont') + '">' + (v[2] > 0 ? '买入' : '卖出') + '</span></td>';
				temp_tab03_html +=  '<td class="textright">' + v[0] + '</td>';
				temp_tab03_html +=  '<td class="textright">市价/' + v[1] + '</td>';
				temp_tab03_html +=  '<td class="textright">' + v[2] + '</td>';
				temp_tab03_html +=  '<td class="textright">无</td>';
				temp_tab03_html +=  '<td>全部成交</td>';
				temp_sum += parseFloat(v[2]);
				temp_index += 1;
			}
			
			tab02_html += '<tr>';
			tab02_html += '<td colspan="8"><a class="toolopen"></a><span class="scrolltime">' + time + '</span><span class="' + (temp_sum > 0 ? 'redfont' : 'greenfont') + '">' + temp_sum + '</span>共' + temp_index +'个订单</td>';
			tab02_html += temp_tab03_html;
			
		}

		tab02_html +=  '</tr>';
		
	}
	$("#tab02 tbody").html(tab02_html);
	
	var tab06_html = '';
	var tab07_html = '';
	var tab08_html = '';
	var tab09_html = '';
	var tab10_html = '';
	var tab11_html = '';
	var tab12_html = '';
	
		
	for(i1 in data.returnRateList){
		var k = data.returnRateList[i1].split(":")[0];
		var v = data.returnRateList[i1].split(":")[1].split(",");
		
		tab06_html += '<tr>';
		tab06_html += '<td>' + k + '</td>';
		for(var iv in v){
			tab06_html += '<td>' + changeValue(v[iv].split("&")[0]) + '</td>';
			tab06_html += '<td>' + changeValue(v[iv].split("&")[1]) + '</td>';
			tab06_html += '<td>' + changeValue(v[iv].split("&")[2]) + '</td>';
		}
		tab06_html += '</tr>'
	}
	for(i2 in data.alphaList){
		var k = data.alphaList[i2].split(":")[0];
		var v = data.alphaList[i2].split(":")[1].split(",");
		tab07_html += '<tr>';
		tab07_html += '<td>' + k + '</td>';
		tab07_html += '<td>' + changeValue(v[0]) + '</td>';
		tab07_html += '<td>' + changeValue(v[1]) + '</td>';
		tab07_html += '<td>' + changeValue(v[2]) + '</td>';
		tab07_html += '<td>' + changeValue(v[3]) + '</td>';
		tab07_html += '</tr>'
	}
	for(i3 in data.betaList){
		var k = data.betaList[i3].split(":")[0];
		var v = data.betaList[i3].split(":")[1].split(",");
		tab08_html += '<tr>';
		tab08_html += '<td>' + k + '</td>';
		tab08_html += '<td>' + fixedValue(changeValue(v[0])) + '</td>';
		tab08_html += '<td>' + fixedValue((v[1])) + '</td>';
		tab08_html += '<td>' + fixedValue((v[2])) + '</td>';
		tab08_html += '<td>' + fixedValue((v[3])) + '</td>';
		tab08_html += '</tr>'
	}
	for(i4 in data.sharpeRatioList){
		var k = data.sharpeRatioList[i4].split(":")[0];
		var v = data.sharpeRatioList[i4].split(":")[1].split(",");
		tab09_html += '<tr>';
		tab09_html += '<td>' + k + '</td>';
		tab09_html += '<td>' + fixedValue(changeValue(v[0])) + '</td>';
		tab09_html += '<td>' + fixedValue(changeValue(v[1])) + '</td>';
		tab09_html += '<td>' + fixedValue(changeValue(v[2])) + '</td>';
		tab09_html += '<td>' + fixedValue(changeValue(v[3])) + '</td>';
		tab09_html += '</tr>'
	}
	for(i5 in data.volatilityList){
		var k = data.volatilityList[i5].split(":")[0];
		var v = data.volatilityList[i5].split(":")[1].split(",");
		tab10_html += '<tr>';
		tab10_html += '<td>' + k + '</td>';
		tab10_html += '<td>' + changeValue(v[0]) + '</td>';
		tab10_html += '<td>' + changeValue(v[1]) + '</td>';
		tab10_html += '<td>' + changeValue(v[2]) + '</td>';
		tab10_html += '<td>' + changeValue(v[3]) + '</td>';
		tab10_html += '</tr>'
	}
	for(i6 in data.informationRatioList){
		var k = data.informationRatioList[i6].split(":")[0];
		var v = data.informationRatioList[i6].split(":")[1].split(",");
		tab11_html += '<tr>';
		tab11_html += '<td>' + k + '</td>';
		tab11_html += '<td>' + fixedValue(changeValue(v[0])) + '</td>';
		tab11_html += '<td>' + fixedValue(changeValue(v[1])) + '</td>';
		tab11_html += '<td>' + fixedValue(changeValue(v[2])) + '</td>';
		tab11_html += '<td>' + fixedValue(changeValue(v[3])) + '</td>';
		tab11_html += '</tr>'
	}
	for(i7 in data.maxDrawdownList){
		var k = data.maxDrawdownList[i7].split(":")[0];
		var v = data.maxDrawdownList[i7].split(":")[1].split(",");
		tab12_html += '<tr>';
		tab12_html += '<td>' + k + '</td>';
		tab12_html += '<td>' + changeValue(v[0]) + '</td>';
		tab12_html += '<td>' + changeValue(v[1]) + '</td>';
		tab12_html += '<td>' + changeValue(v[2]) + '</td>';
		tab12_html += '<td>' + changeValue(v[3]) + '</td>';
		tab12_html += '</tr>'
	}
	
	$("#tab06 tbody").html(tab06_html);
	$("#tab07 tbody").html(tab07_html);
	$("#tab08 tbody").html(tab08_html);
	$("#tab09 tbody").html(tab09_html);
	$("#tab10 tbody").html(tab10_html);
	$("#tab11 tbody").html(tab11_html);
	$("#tab12 tbody").html(tab12_html);
}

function setChartDesc(data){
	var flag = true;
	for(var i in data){
		flag = false;
		break;
	}
	var strategyReturnRate = flag ? '' : (Number(data.strategyReturnRate) * 100).toFixed(2) + "%" ;
	var benchmarkReturnRate =  flag ? '' : (Number(data.benchmarkReturnRate) * 100).toFixed(2) + "%";
	var alpha =  flag ? '' : (Number(data.alpha) * 100).toFixed(2) + "%";
	var beta =  flag ? '' : Number(data.beta).toFixed(2);//
	var sharpeRatio =  flag ? '' : Number(data.sharpeRatio).toFixed(2);//
	var volatility =  flag ? '' : (Number(data.volatility) * 100).toFixed(2) + "%";
	var informationRatio =  flag ? '' : Number(data.informationRatio).toFixed(2);//
	var maxDrawdown =  flag ? '' : (Number(data.maxDrawdown) * 100).toFixed(2) + "%";
	var turnoverRate =  flag ? '' : Number(data.turnoverRate).toFixed(2);//
	
	var desc_html = '';
	desc_html += '<li><span class="result-title">年化收益率</span><span class="value">' + strategyReturnRate + '</span></li>';
	desc_html += '<li><span class="result-title">基准年化收益率</span><span class="value">' + benchmarkReturnRate + '</span></li>';
	desc_html += '<li><span class="result-title">阿尔法</span><span class="value">' + alpha + '</span></li>';
	desc_html += '<li><span class="result-title">贝塔</span><span class="value">' + beta + '</span></li>';
	desc_html += '<li><span class="result-title">夏普率比</span><span class="value">' + sharpeRatio + '</span></li>';
	desc_html += '<li><span class="result-title">收益波动率</span><span class="value">' + volatility + '</span></li>';
	desc_html += '<li><span class="result-title">信息比率</span><span class="value">' + informationRatio + '</span></li>';
	desc_html += '<li><span class="result-title">最大回撤</span><span class="value">' + maxDrawdown + '</span></li>';
	desc_html += '<li><span class="result-title">换手率</span><span class="value">' + turnoverRate + '</span></li>';
	$("#result_desc").html(desc_html);
	$("#result_strategy_desc").html(desc_html);
}

function changeValue(v){
	if(v == Infinity || v == -Infinity || v.toString() == "NaN" ){
		return 0;
	}
	return v;
}

function fixedValue(v){
	if(!isNaN(v)){
		return parseFloat(v).toFixed(2);
	}
	return v;
}

function save() {
	
	var id = $("#strategy_id").val();
	var codeStr = editor.getValue();
	
	$.ajax({
		url : URL_PERFIX +"/json/lab_updateUserStrategy.html", // 后台处理程序
		type : "post", // 数据接收方式
		async : false,
		data : {
			"id": id,
			"strategyText": codeStr
		},
		dataType : "json", // 接受数据格式
		success : function(d) {
			if(d.status == "success"){
				alert("保存成功");
			}else{
				alert("保存失败");
			}
		},
		error : function() {
			alert("保存失败");
		}
	})
}

function loadChartData(data){
	
	var seriesCounter = 0 ,
	names = ['strategy', 'benchmark'];
	
//	var front_chart = $('#result').highcharts();
//	var back_chart = $('#tab01_chart').highcharts();
	 
	var seriesOptions = [];
	
	$.each(names, function (i, name) {
		
//	    $.getJSON('http://www.hcharts.cn/datas/jsonp.php?filename=' + name.toLowerCase() + '-c.json&callback=?',function (data) {
	
		var order = data[name];
		var orderData = [];
		for(var index in order){
			var temp = [];
			
			var obj = order[index];
			
			var array_value = obj.split(":");
			
			var timeStr = array_value[0].replace(/-/g,'/');
			var time = new Date(timeStr).getTime();
			var value = 0.0;
			if(parseFloat(array_value[1])){
				value = parseFloat(array_value[1]);
			}
			temp.push(time);
			temp.push(value);
			
			orderData.push(temp);
		}
		
    	seriesOptions[i] = {
            name: name,
            data: orderData
        };

        // As we're loading the data asynchronously, we don't know what order it will arrive. So
        // we keep a counter and create the chart when all the data is loaded.
        seriesCounter += 1;

        if (seriesCounter === names.length) {
        	
        	createChart(seriesOptions);
        	
//            if(front_chart && back_chart){
//            	while(front_chart.series.length > 0){
//            		front_chart.series[0].remove();
//            		back_chart.series[0].remove();
//            	}
//            	
//            	for(var i in seriesOptions){
//            		front_chart.addSeries(seriesOptions[i], false);
//                	back_chart.addSeries(seriesOptions[i], false);	
//            	}
            	
//            	for(var i in seriesOptions){
//        			front_chart.series[i].setData(seriesOptions[i].data);
//        			back_chart.series[i].setData(seriesOptions[i].data);	
//        		}
            	
//	        	front_chart.redraw();
//	        	back_chart.redraw();
	        	
//	        	setChartYmd(front_chart);
//	        	setChartYmd(back_chart);
	        	
//            	front_chart.hideLoading();
//            	back_chart.hideLoading();
            	
//            }
        }
        
	});

}

function setChartYmd(chart){
	var chartMin = chart.xAxis[1].min;
    var chartMax = chart.xAxis[1].max;
    var min = chartMax - 2592000 * 12 * 1000;

    if (chartMin < min) {
    	chart.xAxis[0].setExtremes(min, chartMax);
    }else{
    	chart.xAxis[0].setExtremes(chartMin, chartMax);
    }
}

function showChart(){
	
//	data = data.output;
	
	var seriesOptions = [{
		name:"strategy",
		data: [],
//		dataGrouping: {
//			enabled: false
//		}
	},{
		name:"benchmark",
		data: [],
//		dataGrouping: {
//			enabled: false
//		}
	}];
//    seriesCounter = 0;
    // create the chart when all data is loaded
	createChart(seriesOptions);
}

function createChart(seriesOptions) {
	var default_options = {
			
		credits: {
			enabled: false
		},
		
		exporting: {
			enabled: true
		},
		
        yAxis: {
            labels: {
               formatter: function () {
                   return (this.value > 0 ? ' + ' : '') + this.value;
               }
            },
            plotLines: [{
                value: 0,
                width: 2,
                color: 'silver'
            }]
        },

        plotOptions: {
//	                series: {
//	                   compare: 'percent'
//	                }
        },

        tooltip: {
        	//({point.change}%)
            pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> <br/>',
            valueDecimals: 2
        },
        
        navigator : {
            adaptToUpdatedData: false
        },
        
        rangeSelector: {
        	
        	enabled: true,
        	inputEnabled: true,
        	
        	//定义一组buttons,下标从0开始
        	buttons: [
            	{
	            	type: 'week',
	            	count: 1,
	            	text: '1周'
            	}, {
            		type: 'month',
            		count: 1,
            		text: '1月'
            	}, {
            		type: 'month',
            		count: 3,
            		text: '3月'
            	}, {
            		type: 'month',
            		count: 6,
            		text: '6月'
            	}, 
//            	{
//            		type: 'ytd',
//            		text: '1年',
//            		enabled: false
//            	}, 
            	{
            		type: 'year',
            		count: 1,
            		text: '1年'
            	}, {
            		type: 'all',
            		text: '全部'
            	}
            ],

        	buttonTheme: {
            	width: 36,
            	height: 16,
            	padding: 1,
            	r: 0,
            	stroke: '#68A',
            	zIndex: 7
        	},
        	
        	inputDateFormat: '%Y-%m-%d',
        	inputEditDateFormat: '%Y-%m-%d',
        	//表示以上定义button的index,从0开始
        	selected: 5
        	
        },

        series: seriesOptions
    };
	
	$('#result').highcharts('StockChart', $.extend({},default_options));
	$('#tab01_chart').highcharts('StockChart', $.extend(default_options, {
		exporting: {
			enabled: false
		},
		rangeSelector: {
        	enabled: false,
        	inputEnabled: false,
		}
	}));
	
}

var dragMinWidth = 400;
var dragMinHeight = 125;
function resize(oParent, handle, isLeft, isTop, lockX, lockY)
{
	
	handle.onmousedown = function (event)
	{
		var event = event || window.event;
		var disX = event.clientX - handle.offsetLeft;
		var disY = event.clientY - handle.offsetTop;	
		var iParentTop = oParent.offsetTop;
		var iParentLeft = oParent.offsetLeft;
		var iParentWidth = oParent.offsetWidth;
		var iParentHeight = oParent.offsetHeight;
		
		document.onmousemove = function (event)
		{
			var event = event || window.event;
			
			var iL = event.clientX - disX;
			var iT = event.clientY - disY;
			var maxW = document.documentElement.clientWidth - oParent.offsetLeft - 2;
			var maxH = document.documentElement.clientHeight - oParent.offsetTop - 2;			
			var iW = isLeft ? iParentWidth - iL : handle.offsetWidth + iL;
			var iH = isTop ? iParentHeight - iT : handle.offsetHeight + iT;
			
			isLeft && (oParent.style.left = iParentLeft + iL + "px");
			isTop && (oParent.style.top = iParentTop + iT + "px");
			
			iW < dragMinWidth && (iW = dragMinWidth);
			iW > maxW && (iW = maxW);
			lockX || (oParent.style.width = iW + "px");
			
			iH < dragMinHeight && (iH = dragMinHeight);
			iH > maxH && (iH = maxH);
			lockY || (oParent.style.height = iH + "px");
			
			if((isLeft && iW == dragMinWidth) || (isTop && iH == dragMinHeight)) document.onmousemove = null;
			
			return false;	
		};
		document.onmouseup = function ()
		{
			document.onmousemove = null;
			document.onmouseup = null;
		};
		return false;
	}
};

/* 
Native FullScreen JavaScript API
-------------
Assumes Mozilla naming conventions instead of W3C for now
*/

(function() {
	var fullScreenApi = { 
			supportsFullScreen: false,
			isFullScreen: function() { return false; }, 
			requestFullScreen: function() {}, 
			cancelFullScreen: function() {},
			fullScreenEventName: '',
			prefix: ''
		},
		browserPrefixes = 'webkit moz o ms khtml'.split(' ');
	
	// check for native support
	if (typeof document.cancelFullScreen != 'undefined') {
		fullScreenApi.supportsFullScreen = true;
	} else {	 
		// check for fullscreen support by vendor prefix
		for (var i = 0, il = browserPrefixes.length; i < il; i++ ) {
			fullScreenApi.prefix = browserPrefixes[i];
			
			if (typeof document[fullScreenApi.prefix + 'CancelFullScreen' ] != 'undefined' ) {
				fullScreenApi.supportsFullScreen = true;
				
				break;
			}
		}
	}
	
	// update methods to do something useful
	if (fullScreenApi.supportsFullScreen) {
		fullScreenApi.fullScreenEventName = fullScreenApi.prefix + 'fullscreenchange';
		
		fullScreenApi.isFullScreen = function() {
			switch (this.prefix) {	
				case '':
					return document.fullScreen;
				case 'webkit':
					return document.webkitIsFullScreen;
				default:
					return document[this.prefix + 'FullScreen'];
			}
		}
		fullScreenApi.requestFullScreen = function(el) {
			return (this.prefix === '') ? el.requestFullScreen() : el[this.prefix + 'RequestFullScreen']();
		}
		fullScreenApi.cancelFullScreen = function(el) {
			return (this.prefix === '') ? document.cancelFullScreen() : document[this.prefix + 'CancelFullScreen']();
		}		
	}

	// jQuery plugin
	if (typeof jQuery != 'undefined') {
		jQuery.fn.requestFullScreen = function() {
	
			return this.each(function() {
				var el = jQuery(this);
				if (fullScreenApi.supportsFullScreen) {
					fullScreenApi.requestFullScreen(el);
				}
			});
		};
	}

	// export api
	window.fullScreenApi = fullScreenApi;	
})();

var isScreenFull = false;
function initFullScreen(){
	
	// do something interesting with fullscreen support
	var fsButton = document.getElementById('fsbutton'),
		fsElement = document.getElementById('specialstuff');
		//	fsStatus = document.getElementById('fsstatus');


	if (window.fullScreenApi.supportsFullScreen) {
		//	fsStatus.innerHTML = 'YES: Your browser supports FullScreen';
		//	fsStatus.className = 'fullScreenSupported';
		
		//	handle button click
		fsButton.addEventListener('click', function() {
			window.fullScreenApi.requestFullScreen(fsElement);
		}, true);
		
		fsElement.addEventListener(fullScreenApi.fullScreenEventName, function() {
			if (fullScreenApi.isFullScreen()) {
				initEditorWrap(true);
				//	editor.resize();
				isScreenFull = true;
				//	fsStatus.innerHTML = 'Whoa, you went fullscreen';
			} else {
				initEditorWrap(false);
				//	editor.resize();
				isScreenFull = false;
				//	fsStatus.innerHTML = 'Back to normal';
			}
		}, true);
		
	} else {
		alert("SORRY: Your browser does not support FullScreen");
		//	fsStatus.innerHTML = 'SORRY: Your browser does not support FullScreen';
	}
	
}

function initEditorWrap(isfullScreen){
	var editorWidth = $("#specialstuff").width();
	var windowWidth = $(window).width();
	var windowHeight = $(window).height();
	var writeLeftToolHeight = $(".write .writetop").height();
	var runToolHeight = $(".run .tool").height();
	var navbarHeight = isfullScreen ? 0 : $("#navbar").height();
	var editorCodeHeight = (windowHeight - writeLeftToolHeight - navbarHeight - 2) + "px";
//	var editorHeight = (windowHeight - writeLeftToolHeight - navbarHeight - 12) + "px";
	var editorLineHeight = (windowHeight - navbarHeight) + "px";
	var editorRunHeight = (windowHeight - runToolHeight - navbarHeight - 2) + "px";
	
	$(".writebottom").height(editorCodeHeight);
	$(".writecenter").height(editorLineHeight);
//	$("#editor").height(editorHeight);
//	$(".write").height(editorHeight);
	$(".write").height(editorLineHeight);
	$(".runbottom").height(editorRunHeight);
	
	var writeleftWidth = $(".write").width() - 8;
	var runWidth = (isfullScreen ? windowWidth : windowWidth - $(".sidebar").width() - 40 ) -  writeleftWidth;
	$(".run").width(runWidth - 10 + "px");
	$(".writeleft").width(writeleftWidth + "px");
}

function initAce(){
	ace.require("ace/ext/language_tools");
	editor = ace.edit("editor");
	editor.setOptions({
		autoScrollEditorIntoView: true,
//		enableBasicAutocompletion : true,
		enableSnippets : true,
		enableLiveAutocompletion : true
	});
	//editor.setTheme("ace/theme/monokai");
	editor.getSession().setMode("ace/mode/python");
	editor.getSession().setUseWrapMode(true);
//	editor.renderer.setScrollMargin(10, 10, 10, 10);
}


$(function(){

	initFullScreen();
	initEditorWrap(false);
	initAce();
	
//	var lParetn = $(".writeleft").get(0);
//	var rParent = $(".run").get(0);
//	var oLR = $("#moveLine").get(0);
//
//	resizeLeftAndRight(lParetn,rParent,oLR);
	
})

$(document).keydown(function(e){
	// ctrl + s
	if( e.ctrlKey  == true && e.keyCode == 83 ){
		save();
//		console.log('ctrl+s');
		return false; // 截取返回false就不会保存网页了
	}
	if( e.ctrlKey  == true && e.keyCode == 13 ){
		Run.run();
		return false; // 截取返回false就不会保存网页了
	}
});

$(window).resize(function(){
	wrapWidth = $("#specialstuff").width();
	initEditorWrap(isScreenFull);
});

function init() {
    Run = new run();
    Highcharts.setOptions({
		global: {
			useUTC: false
		},
		lang: {
			contextButtonTitle:"图表导出菜单",
			decimalPoint:".",
			downloadJPEG:"下载JPEG图片",
			downloadPDF:"下载PDF文件",
			downloadPNG:"下载PNG文件",
			downloadSVG:"下载SVG文件",
			drillUpText:"返回 {series.name}",			
			loading:"加载中",
			months:["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
//			noData:"没有数据",
			noData:"",
			numericSymbols: [ "千" , "兆" , "G" , "T" , "P" , "E"],
			printChart:"打印图表",
			rangeSelectorFrom:"从",
			rangeSelectorTo:"到",
			rangeSelectorZoom:"选择区间：",
			resetZoom:"恢复缩放",
			resetZoomTitle:"恢复图表",
			shortMonths: ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
			thousandsSep:",",
			weekdays: ["星期一", "星期二", "星期三", "星期四", "星期五", "星期六","星期天"]
		}
	});
}

 var wrapWidth;
 $(function(){
    var clickX, leftOffset, leftWidth;
    var dragging  = false;
    var doc       = document;
    var labBtn    = $(".writecenter");
    var leftWrap  = $(".write");
    var leftWriteWrap  = $(".writeleft");
    var leftBottomWrap  = $(".writebottom");
    var rightWrap  = $(".run");
    wrapWidth = $("#specialstuff").width();

    labBtn.mousedown(function(){
            dragging   = true;
            leftOffset = $("#specialstuff").offset().left;
        }
    );

    $(doc).mousemove(function(e) {
        if (dragging) {

            //--------------------------------------------
            clickX = e.pageX;
            
            // 按钮不出左右边界
            if(clickX > leftOffset + 400 && clickX < $(window).width() - 400) {
                
            	// labBtn.css('left', clickX - leftOffset + 'px');//按钮移动
                leftWidth = clickX - leftOffset;
                leftWrap.width( leftWidth + 8 + 'px');
                leftWriteWrap.width( leftWidth -2  + 'px');
                leftBottomWrap.width( leftWidth - 2 + 'px');
                
                rightWrap.width( wrapWidth - leftWidth - 8 + 'px');
                rightWrap.css('left', leftWidth + 8 + 'px');   
                
            } else {
                // labBtn.css('left', '0px');
            }

//            // 按钮右边不出界 
//            if(clickX > (labBtn.offset().left + 8)) {
//                //第一个按钮右边不出界
//                labBtn.css('left', parseFloat(labBtn.css('left')) -11 + 'px');
//                //第一个按钮，左右容器不出界
//                leftWrap.width( labBtn.offset().left + 6 - leftOffset + 11 + 'px' );
//                rightWrap.width( '0px' );
//            } 

        }
    });

    $(doc).mouseup(function(e) {
    	dragging = false;
    	e.cancelBubble = true;
    	if(e.toElement.id == "moveLine"){
    		editor.resize(true);
    		$("#result").highcharts() && $("#result").highcharts().reflow();
    		
    	}
    })
    
    init();
    
    $("#match").click(function(){
    	var id = $("#id").val();
    	$.ajax({
			url : URL_PERFIX + "/json/lab_attendMatch.html", // 后台处理程序
			timeout : 150000,
			type : "post", // 数据接收方式
			async : true,
			data : {
				"id" : id
			},
			dataType : "json", // 接受数据格式
			success : function(d) {
				if(d.code > 0){
					alert("参赛成功");
				}else if(d.code == -1){
					alert("请先确保回测完成之后再尝试参赛");
				}else{
					alert("参赛失败");
				}
			},
			error : function() {
				console.log("服务发生错误");
			}
		})
    })
    
    var id = $("#id").val();
    $.ajax({
		url : URL_PERFIX + "/json/lab_attemptData.html", // 后台处理程序
		timeout : 150000,
		type : "post", // 数据接收方式
		async : true,
		data : {
			"id" : id
		},
		dataType : "text", // 接受数据格式
		success : function(text) {
			
			var text = text.toString().replace(/NaN/g,0).replace(/Infinity/g,0);
			var json = $.parseJSON(text)
			
			if(json.status == "1"){
				showStrategyResult($.parseJSON(json.strategy),json.code);
				showStrategyDetailResult($.parseJSON(json.strategyDetail));
				Run.openModal();
			}
		},
		error : function() {
			console.log("初始化获取图表数据发生错误");
		}
	})
    
	$(".persent").click(function(){
		$(".resource").slideToggle();
	})
	$(".resource .resource-close").click(function(){
		$(".resource").hide();
	})
	
	/**
	setInterval(function(){
		$.ajax({
			url : URL_PERFIX + "/json/lab_coding_stats.html", // 后台处理程序
			timeout : 3000,
			type : "get", // 数据接收方式
			async : true,
			data : {
				"id" : id
			},
			dataType : "json", // 接受数据格式
			success : function(result) {
				if(result.stats){
					var d = result.stats;
					var arry = d.split(" ");
					
					var cpu = arry[0];
					var memory = arry[1];
					var memory_current = memory.split("/")[0];
					var memory_ratio = arry[2];
					
					$('.persent li').eq(0).html('<span class="cpu"></span>' + cpu);
					$('.resource .cpurow').eq(0).find('.progress-bar').width(cpu).html('<span class="sr-only">' + cpu + ' Complete</span>');
					$('.resource .cpurow').eq(0).find('.progress').next().html(cpu);
					$('.persent li').eq(2).html('<span class="cpu"></span>' + memory_current);
					$('.resource .cpurow').eq(1).find('.progress-bar').width(memory_ratio).html('<span class="sr-only">' + memory + ' Complete</span>');
					$('.resource .cpurow').eq(1).find('.progress').next().html(memory);
				}
				
			},
			error : function() {
				console.log("获取资源失败");
			}
		})
	},6000)
	*/
	
	updateUrl("/websocket.mvc?inquiryId=" + $("#user_account").val() + "&empNo=" + $("#id").val());
    connect();
	
})

var ws;
var ws_url;
  
function connect() {
	
	if (!ws_url) {  
		alert('Please set the URL address.');  
		return;  
	}
	
	if (!window.WebSocket) {
	     if (window.MozWebSocket) {
	         window.WebSocket = window.MozWebSocket;
	     } else {
	         alert("Your browser doesn't support WebSockets.");
	         return;
	     }
	}
	ws = new WebSocket(ws_url);
	
	ws.onopen = function () {  
		console.log('Info: connection opened.');  
	};  
	ws.onmessage = function (event) {  
		console.log('Received: ' + event.data);  
		var text = event.data;
		
		if(text.toString().indexOf("[finish]") > -1){
			
			var d = text.toString().substring(8,text.toString().length);
			d = d.replace(/NaN/g,0).replace(/Infinity/g,0);
			var json = $.parseJSON(d)
			
			if(json.status == "fail"){
				Run.GobalCloseRun();
				return;
			}
			
			showStrategyDetailResult(json);
			
			Run.GobalCloseRun();
			return;
		}
		
		text = text.toString().replace(/NaN/g,0).replace(/Infinity/g,0);
		var json = $.parseJSON(text)
		
		showStrategyResult(json,"");
	};  
	ws.onclose = function (event) {  
		console.log('Info: connection closed.');  
		console.log(event);  
	};  
}  

function disconnect() {  
	if (ws != null) {  
		ws.close();  
		ws = null;  
	}  
}  
  
function echo(message) {  
	if (ws != null) {  
		console.log('Sent: ' + message);  
		ws.send(message);  
	} else {  
		alert('connection not established, please connect.');  
	}  
}

function updateUrl(urlPath) {   
	if (window.location.protocol == 'http:') {  
		ws_url = 'ws://' + window.location.host + urlPath;  
	} else {  
		ws_url = 'wss://' + window.location.host + urlPath;  
	}  
}  