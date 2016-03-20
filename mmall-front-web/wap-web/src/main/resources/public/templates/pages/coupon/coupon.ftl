<#escape x as x?html>
    <@compress>
    <!DOCTYPE html>
    <#include '../../wrap/3g.common.ftl'>
    <html>
    <head>
	    <title>优惠券</title>
	    <@meta/>
	    <@less />
    </head>
    <body>
         <div class="wrap">
             <header class="headerbox">
			     <div class="m-topnav">
			        <a class="curp goBack">
					    <i class="f-fl u-menu"></i>
				    </a>
				    <span class="tt">优惠券</span>
				    <i class="add-btn"></i>
			  	 </div>
		    </header>
		    <div class="coupon-box">
		            <!-- <li>
		                 <span class="time">全场券&nbsp;&nbsp;<em class="start">15/09/19</em>-<em class="end">15/10/19</em><em class="expire-tip">即将过期</em></span>
		                 <div class="info">
		                     <em class="reduce">￥</em>
		                     <span class="value">500</span><em class="discount">折</em>
		                     <span class="cd">[满<em>3000</em>可用]</span>
		                 </div>
		             </li>-->
		    </div>
		    <div class="loading">
                <span>正在加载...</span>
            </div>
         </div>
         
	    <@template>
	    
	      <#-- Template Content Here -->
	      <#-- Remove @template if no templates -->
	      
	    </@template>
	    <@jsFrame />
	    <script>
		       
	          var pageBox = $(".page"),
    			  total = "",
    			  pageLimit = 5,
    			  scrollCurrent = 1,
    			  couponListBox = $(".coupon-box"),
    			  flag = true;
    		  
    		//function refreshImg(){
	    	//   var timestamp = Date.parse(new Date());
   			//   $(".verify-img").attr("src", "/brand/genverifycode?" + timestamp);
	        //}

            $('.goBack').on('click', function(){
                window.location.href = "/profile/index";
            });
              
    		$(".add-btn").on("click",function(){
    		    $.message.addCoupon("新增优惠券",function(couponCode){
    		        if(couponCode){
    		            addCoupon(couponCode);
    		        }
    		    });
    		});
    		
    		function addCoupon(couponCode) {
    			$.ajax({
	    			url : "/mycoupon/bindCoupon/",
	    			type : "GET",
	    			dataType : "json",
	    			data : {
	    				couponCode : couponCode
	    			},
	    			success : function (data) {
	    				if (data.code == 200) {
	    					$.message.alert("提示","添加优惠券成功！");
	    					couponListBox.empty();
	    					scrollCurrent = 1;
	    					getCouponList(pageLimit, scrollCurrent-1);
	    				} else {
	    					if (data.result == 7) {
	    						$.message.alert("提示", "您已拥有该优惠券！");
	    					} else {
	    						$.message.alert("提示", "请输入正确的优惠券码！");
	    					}
	    				}
	    			}
	    		});
    		}

    		// 获取优惠券列表
    		function getCouponList(pageLimit, pageIndex) {
    			var couponListDom = "";
    				
	    		$.ajax({
	    		 	cache: false,
	    			url : "/mycoupon/data/couponList/",
	    			type : "GET",
	    			dataType : "json",
	    			data : {
	    				limit : pageLimit,
	    				offset : pageIndex
	    			},
	    			beforeSend: function () {
	    			    flag = false;
                        if (scrollCurrent > 1) {
                            $(".loading").show().find("span").html("正在加载...");
                        }
                    },
	    			success : function (data) {
	    				if (data.code == 200) { 
		    				var list = data.result.list;
		    				if (list != null) {
		    					//couponListBox.empty();
		    					for (var i = 0; i < list.length; i++) {
		    						var items 		= list[i].items,
		    							items 		= JSON.parse(items),
		    							status 		= list[i].couponState,
		    							type 		= list[i].favorType,
		    							startTime 	= list[i].startTime,
		    							endTime 	= list[i].endTime,
		    							Sdates 		= new Date(startTime),
		    							Syear 		= Sdates.getFullYear(),
		    							Smonth  	= Sdates.getMonth() + 1,
		    							Sdate		= Sdates.getDate(),
		    							Edates  	= new Date(endTime),
		    							Eyear 		= Edates.getFullYear(),
		    							Emonth  	= Edates.getMonth() + 1,
		    							Edate		= Edates.getDate(),
		    							startTime 	= Syear + "/" + Smonth + "/" + Sdate,
		    							endTime   	= Eyear + "/" + Emonth + "/" + Edate,
		    							nowTime		= new Date().getTime();
		    							
		    						if (status == 0) {
                                        // 设置提醒过期时间为5天
                                        if ( (Number(list[i].endTime) - Number(nowTime)) / 1000 <= (60 * 60 * 24) * 5 ) {
                                            status = 9;
                                        } else {
                                        	status = 0;
                                        }
                                    }

		    						// 根据状态设置不同的字体颜色
		    						if (status == 2 || status == 3) {
					    				// 失效 或 已使用
					    				couponListDom += "<ul><li class='used'>";
					    			} else{
					    			    couponListDom += "<ul><li>";
					    			}
					    			
					    			couponListDom += "<span class='time'>全场券&nbsp;&nbsp;<em class='start'>" + startTime + "</em>-"+
					    			 "<em class='end'>"+ endTime + "</em>";
					    			if(status == 9){
					    			     couponListDom += "<em class='expire-tip'>即将过期</em></span>";
					    			}else if(status == 2){
					    			     couponListDom += "<em class='tip'>已失效</em></span>";
					    			}else if(status == 3){
					    			     couponListDom += "<em class='tip'>已使用</em></span>";
					    			}else{
					    			     couponListDom += "</span>";
					    			}
					    			
					    			couponListDom += "<div class='info'>";

		    						// 根据优惠券类型设置显示不同内容
		    						if (type == 0) {
		    							couponListDom += "<em class='reduce'>￥</em>"+
					    					"<span class='value'>"+ items[0].result[0].value +"</span>";
		    						} else {
		    							couponListDom += "<span class='value'>"+ items[0].result[0].value +
		    							"</span><em class='discount'>折</em>";
		    						}
		    						var condition = items[0].condition.value,
		    							condition = condition.substr(0, condition.length - 1);
					    			couponListDom += "<span class='cd'>[满<em>" + condition + "</em>可用]</span>"+
					    			    "</div></li></ul>";
					    					
		    					}
		    					couponListBox.append(couponListDom);
		    					total = Math.ceil(data.result.total / pageLimit);
		    					scrollCurrent ++;
		    					flag = true;
		    				} else {
		    					couponListBox.html("<div class='err_page'>"+
			    					  "<img src='/res/3g/images/new/no_coupon.png'/>"+
			                          "<span>抱歉，您还没有优惠券哦~</span>"+ 
                                      "</div>");
                                total = null;
		    				}
	    				}
	    			}
	    		});
    		}
    		getCouponList(pageLimit, scrollCurrent-1);
    		
    		//页面向下滚动获取数据
    		$(window).scroll(function () {
    		    if(flag){
    		        var scrollTop = $(this).scrollTop(),
		            	scrollHeight = $(document).height(),
		            	windowHeight = $(this).height(),
	    		    	remainHeight = scrollHeight - scrollTop - windowHeight;
	    		    if(remainHeight<=100){
		    		    if (total != null) {
	                        if (scrollCurrent <= total) {
	    		                getCouponList(pageLimit, (scrollCurrent-1)*pageLimit);
	                        } else {
	                            $(".loading").show().find("span").html("没有更多...");
	                        }
	                    } else {
	                        return;
	                    }
	    		    }
    		    }
    		});

	    </script>
    </body>
    </html>
    </@compress>
</#escape>