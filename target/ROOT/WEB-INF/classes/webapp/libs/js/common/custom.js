var URL_PERFIX = $("#prefix").text();
$(function(){
	function createMenuList(d){
		var temp = '<div class="menulist">';
		temp += '<input class="checkbox" name="" type="checkbox" value="' + d.id + '">';
		temp += '<a class="menuname" href="/lab/lab_coding.html?id=' + d.id + '" >' + d.title + '</a>';
		temp += '<a class="toolnormal tool09"></a>';
		temp += '<a class="toolnormal tool08"></a>';
		temp += '</div>';	
		return temp;
	}
	$.ajax({
		url: URL_PERFIX + "/json/lab_getUserStrategyNameList.html",
		type: "get",
		async: true,
		data: {},
		dataType: "json",
		/*timeout: 3000,*/
		success: function(d){
			var arry = d.data;
			var html = '';
			for(var i in arry){
				html += createMenuList(arry[i]);
			}
			$("#menu_list").html(html);
			// 删除
			$("#menu_list").on("click",".tool08",function(){
				if(!confirm("确定要删除吗？")){
					return;
				}
				$obj = $(this);
				var id = $obj.prev().prev().prev().val();
				$.ajax({
					url: URL_PERFIX + "/json/lab_deleteUserStrategyById.html",
					type: "get",
					async: true,
					data: {
						id: id
					},
					dataType: "json",
					/*timeout: 3000,*/
					success: function(d){
						if(d.status == "success"){
							$obj.parent().remove();
							var current_id = $("#id").val();
							if(id == current_id){
								window.location.reload();
							}
						}else{
							alert("删除错误");
						}
					},
					error: function(){
						alert("删除错误");
					}
				})
			})
			// 修改
			$("#menu_list").on("click",".tool09",function(){
				$obj = $(this);
				var id = $obj.prev().prev().val();
				var current_title = $obj.prev().text();
				var title = prompt("输入您要修改的名称",current_title);
				console.log(current_title);
				console.log(title);
				if(title && title != ""){
					$.ajax({
						url: URL_PERFIX + "/json/lab_updateUserStrategy.html",
						type: "post",
						async: true,
						data: {
							id: id,
							strategyName: title
						},
						dataType: "json",
						/*timeout: 3000,*/
						success: function(d){
							if(d.status == "success"){
								$obj.prev().html(title);
							}else{
								alert("更新错误");
							}
						},
						error: function(){
							alert("更新错误");
						}
					})
				}
			})
		}
	})
	$("#silder_list li:first").click(function(){
		$("#strategy_list").toggle(0,function(){
			if(!$(this).is(":hidden")) {
				
			}
		});
	})
	$("#strategy_list .packall").click(function(){
		$(this).parent().toggle();
	})
	$("#strategy_list .menutool .tool06").click(function(){
		var len = $(this).parent().next().find(".menulist").length;
		$.ajax({
			url: URL_PERFIX + "/json/lab_insertUserStrategy.html",
			type: "post",
			async: true,
			data: {
				strategyName: "Untitled" + len
			},
			dataType: "json",
			/*timeout: 3000,*/
			success: function(d){
				if(d.status == "success"){
					var od = {};
					od.id = d.code;
					od.title = "Untitled" + len;
					$("#menu_list").prepend(createMenuList(od));
				}else{
					alert("增加失败");
				}
			},
			error: function(){
				alert("增加失败");
			}
		})
	})
})