/*
 * ------------------------------------------
 * 我的订单首页实现文件
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'pro/widget/module',
    '{pro}page/order/orderList.js?v=20151116',
    'pro/page/order/widget/tab/tab',
    'pro/page/order/widget/search/search',
    'util/placeholder/placeholder',
    'pro/extend/request'
],function(_k,_e,_w,orderList,tab,search,_t0,_request,_p,_o,_f,_r){
    var _pro,
        _baseuri = '/myorder/orderlist';

    _p._$$Index = _k._$klass();
    _pro = _p._$$Index._$extend(_w._$$Module);

    /**
     * 初始化方法
     * @return {Void}
     */
    _pro.__init = function(){
        this.__baseURI = '';
        this.__orderList = new orderList({
            data:{
                onnumchange:this.__onNumChange._$bind(this),
                onscroll:this.__doLazyRefresh._$bind(this)
            }
        });
        this.__orderList.$inject('#order-body'||document.body);
        this.__orderList.$on('delete',this.__onDelete._$bind(this));
        this.__orderList.$on('getGoods',this.__onGetGoods._$bind(this));
        this.__orderList.$on('buyAgain',this.__onBuyAgain._$bind(this));
        this.__doGetNumbers();
        this.__search = new search();
        this.__search.$inject('#search-box');
        this.__search.$on('search',this.__onSearch._$bind(this));
        setTimeout(function(){
            _t0._$placeholder('search','js-placeholder');
        },0)
        this.__super();
    };
    
    _pro.__onDelete = function(value){
    	var that = this;
    	$.message.confirm('确定删除订单吗？','del',function(data){
    		if(data){
    			that.__doDelete(value);
    		}
    	});
    };

    _pro.__doDelete = function(_x){
        this.__deleteURL = '/myorder/delete';
        this.__orderList.$request(this.__deleteURL,{
            data:{orderId:_x.orderId},
            onload:function(_result){
                if (_result.code == 200){
                    // 删除成功刷新页面
                    this.$emit("updatelist")
                }
            }._$bind(this.__orderList),
            onerror:function(){

            }
        })
    };
    
    _pro.__onGetGoods = function(value){
    	var that = this;
    	$.message.confirm('您确定要确认收货吗？','info',function(data){
    		if(data){
    			that.__doGetGoods(value);
    		}
    	});
    };
    
    _pro.__doGetGoods = function(_x){
    	var _id = _x.orderId;
    	this.__orderList.$request('/myorder/confirm?orderId=' + _id,{
            method:'GET',
            type:'JSON',
            onload:function(_result){
                if (_result.code==200) {
                	$.message.alert("收货成功！", "success");
                	window.location = "/myorder/detail?orderId=" + _id;
                }else if(_result.code==201){
                	$.message.alert(_result.message, "fail");
                }
            }
        });
    };
    
    _pro.__onBuyAgain = function(value){
    	var list = value.orderSkuList,
    		temArr = [],
    		orderId = value.orderId;
    	
    	for (var i = 0; i < list.length; i++) {
    		var obj = {"skuid": list[i].skuId,"count": list[i].totalCount};
    		temArr.push(obj);
    	}
    	$.ajax({
            type : "POST",
            url: "/cart/buyAgain",
            async: true,
            contentType: "application/json",
            data : JSON.stringify({
                "cartItemDTOs" : temArr,
                "orderId" : orderId
            }),
            success: function (data) {
        	   if (data.code != 200) {
        		   $.message.alert(data.message, "info");
        	   } else {
        		   window.location.href= '/cartlist/';
        	   }
            }
        });
    };
    
    function buyAgainSuc() {
    	 var dom = "";
    	 dom += "<div class='opacity'></div>"+
    	 		"<div class='box'>"+
    	 			"<i class='close'></i>"+
    	 			"<div class='tit messageCart'>该商品已下架或删除！</div>"+
    	 			"<div class='btn'>"+
    	 				"<a class='cart-list' target='_blank' href='/cartlist/'>去进货单结算</a>"+
    	 				"<span class='goon-buy'>继续购买</span>"+
    	 			"</div>"+
    	 		"</div>";
    	 $(".buy-again-popup").append(dom);
    	 
    	 var windowH = $(window).height(),
			bodyH = $("body").height(),
			bdoyW = $("body").width(),
			opacity = $(".buy-again-popup").find(".opacity"),
			box = $(".buy-again-popup").find(".box"),
			boxH = box.height(),
			boxW = box.width();
    	 
    	 opacity.css({
			width : bdoyW,
			height : bodyH
		});
		box.css({
			left : (bdoyW - boxW)    / 2,
			top  : (windowH - boxH)  / 2
		});
    }
    
    $(".buy-again-popup").on("click", ".close", function () {
    	$(".buy-again-popup").empty();
    });
    $(".buy-again-popup").on("click", ".goon-buy", function () {
    	$(".buy-again-popup").empty();
    });
  
    _pro.__onNumChange = function(){
        _request('/myorder/getOrderNumbers',{
            data:{},
            onload:function(_json){
                if (_json && _json.code == 200){
                    _list = _json.result.list;
                    this.__tab.data.num = _list;
                    this.__tab.$update();
                }
            }._$bind(this),
            onerror:function(err){}
        })
    };

    _pro.__doGetNumbers = function(){
        _request('/myorder/getOrderNumbers',{
            data:{},
            onload:this.__cbGetNumbers._$bind(this),
            onerror:function(err){}
        })
    };

    _pro.__cbGetNumbers = function(_json){
        var _list = [0,0,0,0];
        if (_json && _json.code == 200){
            _list = _json.result.list;
        }
        this.__tab = new tab({
            data:{
                num:_list
            }
        });
        this.__tab.$inject('#tab-box');
        this.__tab.$on('change',this.__onTabChange._$bind(this));
        var mystate = location.hash.split('=')[1];
        this.__tab.go(mystate||0);
        this.__tab.$update();
    }

    /**
     * 查找条件
     * @param  {[type]} _value [description]
     * @return {[type]}        [description]
     */
    _pro.__onSearch = function(_value){
    	var _node = _e._$get("search");
        //if (!_value) return;
    	if(_value){
    		if(isNaN(_value)||_value < 0){
            	_e._$addClassName(_node,"err");
            	return;
            }else{
            	_e._$delClassName(_node,"err");
            }
    	}
        this.__tab.data.selectedIndex = 0;
        this.__tab.$update();
        this.__orderList.url = _baseuri;
        if(!_value){
        	this.__orderList.setCondition({
        		type:0
            });
        }else{
        	this.__orderList.setCondition({
                isSearch:true,
                search:_value
            });
        }
    };

    /**
     * 切换tab
     * @param  {[type]} _index [description]
     * @return {[type]}        [description]
     */
    _pro.__onTabChange = function(_index){
        location.hash = '#state=' + _index;
        this.__orderList.url = _baseuri;
        this.__orderList.setCondition({
            search:'',
            type:_index
        });
    };
    
    // 设置side选中状态
    _pro.__setActive = function (classs) {
    	 var sideList = document.getElementById("m-side").getElementsByTagName("li");
    	 for ( var i = 0; i < sideList.length; i++) {
    		 if (sideList[i].getAttribute("class") == classs) {
    			 sideList[i].className = sideList[i].className + " active";
    		 }
    	 }
    };
    _pro.__setActive("myorder");

    _p._$$Index._$allocate({});

    return _p;
    
  
});