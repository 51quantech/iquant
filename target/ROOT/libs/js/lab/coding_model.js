$(function(){
	$(".nav_model li").click(function(){
		$(this).siblings().find("a").removeClass("selected");
		$(this).find("a").addClass("selected");
		var index = $(this).index();
		$("#menu_con .tag").hide();
		$("#menu_con .tag").eq(index).show();
		$("#menu_con .tag").find(".fixedtable").show();
		$("#menu_con .tag").find(".fixedrow").show();
	})
})