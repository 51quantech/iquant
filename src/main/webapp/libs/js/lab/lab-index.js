$(function(){

    var highlight = ace.require("ace/ext/static_highlight")
    var dom = ace.require("ace/lib/dom")
    function qsa(sel) {
        return Array.apply(null, document.querySelectorAll(sel));
    }

/**
	$("#modal_first_btn").click(function(){
		$(".modal-first").addClass("hide");
		$(".modal-second").removeClass("hide");
		
	})
	
	$("#modal_second_btn").click(function(){
		$(".modal-first").removeClass("hide");
		$(".modal-second").addClass("hide");
		
	})

*/
    // 创建股票结点
    function createStockRow(name,value){
    	var html = '<tr data-stock="' + value + '">';
		html += '<td><span class="bf"><b>' + name + '</b><i>' + value + '</i></span><span class="bg"></span></td>';
		html += '<td><input name="" value="0" type="text" class="themes-input"><span class="visible-lg-inline-block">%</span><a class="toolnormal tool08"></a></td>';
		html += '</tr>';
		return html;
    }
    
    // 计算余额
    function calBalance(){
    	var num = 0;
    	$(".stock-table input.themes-input").each(function(i,el){
			var value = $(this).val();
			num += parseInt(value);
		})
		$("#able_money").html(100 - num + "%");
    }

	function createFirstStrategy(){
		var startDay = $("input[name='startDay']").val();
		var endDay = $("input[name='endDay']").val();
		var money = $("input[name='money']").val();
		var ratio = $("input[name='ratio']").val();
		
		$.ajax({
			url: URL_PERFIX + "/file/first_strategy.txt",
			dataType: "text",
			success: function(result){
				var d = result.replace(/{start}/, startDay).replace(/{end}/, endDay).replace(/{capital_base}/, money).replace(/{refresh_rate}/, ratio);
				$("#code1").text(d);
				qsa("#code1").forEach(function (codeEl) {
			        highlight(codeEl, {
			            mode: codeEl.getAttribute("ace-mode"),
			            theme: codeEl.getAttribute("ace-theme"),
			            startLineNumber: 1,
			            showGutter: codeEl.getAttribute("ace-gutter"),
			            trim: true
			        }, function (highlighted) {
			            
			        });
			    });
    
			}
		});
	}
	
	function createSecondStrategy(){
		var startDay = $("input[name='startDay']").val();
		var endDay = $("input[name='endDay']").val();
		var money = $("input[name='money']").val();
		var ratio = $("input[name='ratio']").val();
		
		$.ajax({
			url: URL_PERFIX + "/file/second_strategy.txt",
			dataType: "text",
			success: function(result){
				var d = result.replace(/{start}/, startDay).replace(/{end}/, endDay).replace(/{capital_base}/, money).replace(/{refresh_rate}/, ratio);
				$("#code2").text(d);
				qsa("#code2").forEach(function (codeEl) {
			        highlight(codeEl, {
			            mode: codeEl.getAttribute("ace-mode"),
			            theme: codeEl.getAttribute("ace-theme"),
			            startLineNumber: 1,
			            showGutter: codeEl.getAttribute("ace-gutter"),
			            trim: true
			        }, function (highlighted) {
			            
			        });
			    });
    
			}
		});
	}
	
	function createFinalStrategy(){
		var startDay = $("input[name='startDay']").val();
		var endDay = $("input[name='endDay']").val();
		var money = $("input[name='money']").val();
		var ratio = $("input[name='ratio']").val();
	
		var universe = ''; 
		var weight = '';
		
		$(".stock-table input.themes-input").each(function(i,el){
			if(i > 0){
				universe += ",";
				weight += ",";
			}
			var name = $(this).parent().prev().find("span i").text();
			var value = $(this).val();
			
			universe += '"' + name + '"';
			weight += parseInt(value) / 100;
			
		})
		
		$.ajax({
			url: URL_PERFIX + "/file/final_strategy.txt",
			dataType: "text",
			success: function(result){
				var d = result.replace(/{start}/, startDay).replace(/{end}/, endDay).replace(/{capital_base}/, money).replace(/{refresh_rate}/, ratio).replace(/{universe}/, universe).replace(/{weight}/, weight);
				$("#code2").text(d);
				qsa("#code2").forEach(function (codeEl) {
			        highlight(codeEl, {
			            mode: codeEl.getAttribute("ace-mode"),
			            theme: codeEl.getAttribute("ace-theme"),
			            startLineNumber: 1,
			            showGutter: codeEl.getAttribute("ace-gutter"),
			            trim: true
			        }, function (highlighted) {
			            
			        });
			    });
    
			}
		});
	}
	
	// 初始化第一个策略
	createFirstStrategy();
	// 初始化第二个策略
	createSecondStrategy();
	
	
	// 绑定上一步，下一步事件
	$("#modal_first_btn,#modal_second_btn").click(function(){
		//.modal_first,.modal_second
		$(this).closest(".modal-wrap").toggleClass("hide").siblings().toggleClass("hide");
	})
	
	// 第一步骤值变化时的事件
	$("input[name='startDay'],input[name='endDay'],input[name='money'],input[name='ratio']").change(function(){
		var name = $(this).attr("name");
		if(name == "money" ){
			if(isNaN($(this).val())){
				$(this).val(1000000);
			}else if(parseInt($(this).val()) < 0){
				$(this).val(1000000);
			}
		}
		if(name == "ratio"){
			if(isNaN($(this).val())){
				$(this).val(5);
			}else if(parseInt($(this).val()) < 0){
				$(this).val(5);
			}
		}
		createFirstStrategy();
		createSecondStrategy();
	})
	
	// 第二步骤中，获取热门主题列表
	$.getJSON(URL_PERFIX + "/json/stock_getPlateInfo.html", {
		
	}, function(data, status, xhr) {
		var html = '';
		for(var index in data.data){
			
			var fontColor = 'redfont'
			if(data.data[index].avgUpDownRaito.indexOf("-") != -1){
				fontColor = 'greenfont';
			}
			
			html += '<li data-value="' + data.data[index].bkId + '" data-name="' + data.data[index].bkName + '">';
			html += '<a href="#" class="text-left">' +'<span class="' + fontColor + ' text-right visible-lg-inline-block">' + data.data[index].avgUpDownRaito +'</span>'+ data.data[index].bkName +  '</a>';
			html += '</li>';
		}
		$("#sel_ul").html(html);
	})

	// 绑定第二步骤中选择热门模板的事件
	$("#sel_ul").on("click","li",function(){
		var name = $(this).data("name");
		var value = $(this).data("value");
		$("#sel_btn").text(name);
		// 根据板块查找股票
		$.getJSON(URL_PERFIX + "/json/stock_getStockInfo.html", {
			"value": value
		}, function(data, status, xhr) {
			var html = '';
			var num = 0;
			for(var index in data){
				if(num++ > 4){
					break;
				}
				var name = data[index].stock_name;
				var value = data[index].stock_code;
				html += createStockRow(name,value);
			}
			$(".stock-table tbody").html(html);
			// 设置股票数量
			$("#stock_num").text($(".stock-table tbody tr").length);
			// 设置可用现金
			$("#able_money").text("100%");
			// 生成最终代码
			createFinalStrategy();
		})
		
	})
	
	
	// 第二步骤中，获得所有股票信息，并增加股票的搜索事件
	$.getJSON(URL_PERFIX + "/json/stock_getStockInfoAll.html", {
		
	}, function(data, status, xhr) {
		
		var stock_array = [];
		// 获得所有股票数据
		for ( var index in data.data) {
			var ogg = data.data[index];
			var obj = {};
			obj.label = ogg.infoName;
			obj.value = ogg.infoId;
			stock_array.push(obj);
		}
		
//		console.log(stock_array);
		// 绑定第二步骤中添加股票的查询事件
		$("#stock_add_select").autocomplete({
			delay : 300, // 延迟时间
			minLength : 1, // 最少输入几个字的时候触发
//			source : function(request, response) {
//				var term = request.term;
//				$.getJSON("http://service1.mrd.sohuno.com/stockinfo/?key=stockInfo&parame=all", {
//					textsearch : term
//				}, function(data, status, xhr) {
	//
//					var arry_data = [];
//					for ( var index in data.data) {
//						var ogg = data.data[index];
//						var obj = {};
//						obj.label = ogg.infoName;
//						obj.value = ogg.infoId;
//						arry_data.push(obj);
//					}
//					response(arry_data);
//				});
//			},
			source : function(request, response){
				var term = request.term;
				var temp_array = [];
				if(isNaN(term)){
					for(var si in stock_array){
						var tempObj = stock_array[si];
						if(tempObj.label.indexOf(term) != -1){
							var obj = {};
							obj.label = tempObj.label;
							obj.value = tempObj.value;
							temp_array.push(obj);
						}
					}
				}else{
					for(var si in stock_array){
						var tempObj = stock_array[si];
						if(tempObj.value.indexOf(term) != -1){
							var obj = {};
							obj.label = tempObj.label;
							obj.value = tempObj.value;
							temp_array.push(obj);
						}
					}
				}
				response(temp_array)
			},
			focus : function(event, ui) {  
				$(this).val(ui.item.label);
				event.preventDefault(); 
            },  
			select : function(event, ui) {
				$(this).val(ui.item.label);
				$("#stock_value").val(ui.item.label + ":" + ui.item.value);
				event.preventDefault(); 
			},
		}).data( "ui-autocomplete" )._renderItem = function(ul, item){
        	return $( "<li>" )
//        	.data("item.autocomplete", item)
			.append( $( "<a>" ).text( item.label + ':' + item.value ) )
			.appendTo( ul );
		};

	});
	
	
	// 绑定第二步骤中添加股票的事件
	$("#stock_add").click(function(){
		var val = $("#stock_value").val();
		if($.trim(val) == ""){
			return;
		}
		var name = val.split(":")[0];
		var value = val.split(":")[1];
		var flag = false;
		$(".stock-table tbody tr").each(function(i, el){
			if($(el).data("stock") == value){
				flag = true;
			}
		})
		if(flag){
			return;
		}
		var html = createStockRow(name,value);
		$(".stock-table tbody").append(html);
		// 设置股票数量
		$("#stock_num").text($(".stock-table tbody tr").length);
		// 可用金额计算
		calBalance();
		// 更新策略
		createFinalStrategy();
	})
	
	// 绑定第二步骤中删除股票的事件
	$(".stock-table").on("click","a.toolnormal.tool08",function(){
		$(this).parent().parent().remove();
		// 设置股票数量
		$("#stock_num").text($(".stock-table tbody tr").length);
		// 可用金额计算
		calBalance();
		// 更新策略
		createFinalStrategy();
	})
	
	// 绑定第二步骤中修改股票的事件和百分比背景色事件
	$(".stock-table").on("change","input.themes-input",function(){
		var value = $(this).val();
		if(isNaN(value)){
			$(this).val(0)
			return;
		}
		
		value = parseInt(value);
		$(this).val(value);
		
		if(value < 0) {
			value = 0;
			$(this).val(0);
		}
		
		var sum_ratio = 0;
		$(".stock-table tbody tr input.themes-input").each(function(i, el){
			var v = $(this).val();
			sum_ratio += parseInt(v);
		})
		
		if(sum_ratio > 100){
			value = 100 - sum_ratio +value
			$(this).val(value);
		}
		
		$(this).parent().prev().find("span.bg").width(value + "%");
		// 可用金额计算
		calBalance();
		// 更新策略
		createFinalStrategy();
		
	})
	
	function createNewCode(code,$obj){
	    $.ajax({
			url : URL_PERFIX + "/json/lab_coding_create.html", // 后台处理程序
//				timeout : 15000,
			type : "post", // 数据接收方式
			async : false,
			data : {
				"code" : code
			},
			dataType : "json", // 接受数据格式
			success : function(d) {
				
				if(d.id > 0){
					window.location.href = URL_PERFIX + "/lab/lab_coding.html?id=" + d.id;
				}else{
					alert("创建策略失败！");
				}
				$obj.attr("disabled",false)
			},
			error : function() {
				alert("抱歉！服务器发生错误");
				$obj.attr("disabled",false)
			}
		})
	}
	
	// 提交事件
	$("#submit_code").click(function(){
		$obj = $(this);
		var code = $("#code2").text();
		$obj.attr("disabled",true)
		if($.trim(code) != ""){
//			CodeForm.code.value = code;
//			CodeForm.submit();
			createNewCode(code,$obj);
		}
	})
	
	// 创建空白策略
	$("#submit_code_init").click(function(){
		$obj = $(this);
		$obj.attr("disabled",true)
		createNewCode("",$obj);
	})
	
	// 日期插件
	$("#datetimepicker1,#datetimepicker2").datetimepicker({
		language: 'zh-CN',
		format: 'yyyy-mm-dd',
		minView: 2
	});

    
})