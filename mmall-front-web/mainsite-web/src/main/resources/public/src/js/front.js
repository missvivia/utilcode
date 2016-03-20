/**
    *
    * @author E盘 http://fuzhongkuo.com
    *
**/

var windowH = $(window).height(),
	windowW = $(window).width(),
	bodyH = $("body").height(),
	bdoyW = $("body").width();

/**
	*
	* @ 分类2级3级菜单
	*
**/

var firstDom = "",
	secondDom = "",
	categorySideBox = $(".category-side ul"),
	categoryNavBox = $(".category-nav ul");

function getCategoryData() {
	if(categorySideBox.length > 0 || categoryNavBox.length > 0){
		$.ajax({
			type : "GET",
			url: "/index",
			async: false,
			dataType: "json",
			data: {},
		    success: function(data) {
				locationCode = data.locationCode.id;
				locationName = data.locationCode.name;
				categoryList = data.categoryList;
				if(categoryList != null) {
					CreateCategory(categoryList);
				}
		    }
		});
	}
}
//getCategoryData();

function CreateCategory(categoryList) {
	for ( var i = 0; i < categoryList.length; i++ ) {
		firstDom += "<li class='itme"+ (i+1) +"' data-id='"+ categoryList[i].id +"'>"+
						"<i></i>"+
						"<span>"+
							"<a href='javascript:void(0);' data-id='"+ categoryList[i].id +"' data-level='"+ categoryList[i].level +"'>"+ categoryList[i].name +"</a>"+
						"</span>"+
						"<em></em>"+
					"</li>";
		secondDom += "<li>";

		var secondList = categoryList[i].subCategoryContentDTOs;
		if (secondList != null) {
			for ( var j = 0; j < secondList.length; j++) {
				secondDom += "<dl><dt><a href='javascript:void(0);' data-id='"+ secondList[j].id +"' data-level='"+ + secondList[j].level +"'>"+ secondList[j].name +"</a></dt>";
				var thirdList = secondList[j].subCategoryContentDTOs;
				if (thirdList != null) {
					for (var k = 0; k < thirdList.length; k++) {
						secondDom += "<dd><a href='javascript:void(0);' data-id='"+ thirdList[k].id + "'>"+ thirdList[k].name +"</a></dd><dd>|</dd>";
					}
				}
				secondDom += "</dl>";
			}
			secondDom += "</li>";
		}
	}

	categorySideBox.append(firstDom);
	categoryNavBox.append(secondDom);

	// 删除最后一个分隔符
	categoryNavBox.find("dl").each( function () {
		$(this).find("dd:last").remove();
	});
}

// 商品分类绑定事件
$(".category-nav").on("click", "a", function () {
	var id = $(this).data("id");
	window.location.href = "/list/" + id;
});

// 分类菜单
$(function () {
	var categorySide = $(".category-side"),
    	categoryNav = $(".category-nav"),
    	categoryWrap = $(".category-wrap"),
    	categoryBox = $(".main-category-box"),
    	categoryBoxTop = $(".main-category-top");

	categorySide.on("mousemove", "li", function () {
		var index = $(this).index();
		$(this).addClass("active").siblings().removeClass("active");
		$(this).closest(".main-category").find(".category-nav").show().find("li").eq(index).addClass("active").siblings().removeClass("active");
	});

	categoryWrap.on("mouseleave", function () {
		categorySide.find("li").removeClass("active");
		$(this).find(".category-nav").hide().find("li").removeClass();
	});

	categoryBox.find("dt").on("mousemove", function () {
		$(this).closest('.main-category').find(".category-side").show();
	});

	categoryBox.find(".category-wrap").on("mouseleave", function () {
		$(this).find(".category-side, .category-nav").hide();
	});

	categoryBoxTop.find("dt").on("mousemove", function () {
		$(this).closest('.main-category').find(".category-side").show();
	});

	categoryBoxTop.find(".category-wrap").on("mouseleave", function () {
		$(this).find(".category-side, .category-nav").hide();
	});
});
/******************************************************************** E 分类2级3级菜单 ********************************************************************/


/**
	* 搜索
**/
$(".sub-input").on("click", function () {
	var val = $.trim($(this).closest(".search").find("input").val());
	searchVal(val);
});

$(document).on("keyup", function (e) {
	var code = e.which;
	if (code == 13) {
		var focus= $(".top .search input").is(":focus"),
			barFocus = $(".fixed-top .search .sear-input").is(":focus");  
		if (focus) {
			var val = $.trim($(".top .search input").val());
			searchVal(val);
		} else if (barFocus) {
			var val = $.trim($(".fixed-top .search .sear-input").val());
			searchVal(val);
		}
	}
});

function searchVal(val) {
	if (val == "") {
		return;
	} else if (val.indexOf("<") >= 0 && val.indexOf(">") >= 0) {
		$.message.alert("不能输入非法字符！", "info");
	} else {
		val =  encodeURIComponent(encodeURIComponent(val));
		window.location.href = "/search/"+ val;
	}
}
/******************************************************************** E 搜索 ******************************************************************/