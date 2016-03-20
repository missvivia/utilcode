<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
<head>
    <#include "../../wrap/common.ftl">
    <#include "../../wrap/front.ftl">
    <#include "../../wrap/cartcommon.ftl">
    <@title type="orderInfo"/>
    <meta charset="utf-8"/>
    <@css/>
</head>
<body>
	<@topbar />
    <@cartHeader/>
    <div class="wrap write-order">
        <input type="hidden" value="${weekOfDate}" name="weekOfDate" />
        <input type="hidden" value="${currTime}" name="currTime" />
        <form id="payOrder" name="payOrder" method="post" action="/purchase/buy" >
        	<input type="hidden" value="${formToken}" name="formToken" />
            <input type="hidden" value="${cartIds}" name="cartIds" class="cartId" />
            <input type="hidden" name="skusPrice" value="" class="skusPrice" />
            <input type="hidden" value="${requestId}" name="requestId" />
            <input type="hidden" value="5393" name="addressId" />
            <input type="hidden" value="" name="userCouponId" />
            <input type="hidden" value="2" name="payMethod" />
            <input type="hidden" value="" name="receipt" />
            <input type="hidden" value="" name="hbCash" />
            <input type="hidden" value="" name="cityId" />
            <input type="hidden" value="" name="sectionId" />
            <div class="clearfix address">
                <div class="step-tit">
                    <div class="line">
                        <i></i>
                    </div>
                    <span>收货人信息</span>
                </div>
                <div class="step-con">
                    <div class='contact-tel'>信息有误？请联系<em>0571-87651759</em></div>
                    <div class="address-list"></div>
                    <!--<div class="add-btn">
						新增收货地址
                    </div>-->
                </div>
            </div>
            <#-- <div class="clearfix delivery-info">
                <div class="step-tit">
                    <div class="line">
                        <i></i>
                    </div>
                    <span>配送信息</span>
                </div>
                <div class="step-con">
                    <table>
                        <tr>
                            <th width="33%">配送时间</th>
                            <th width="33%">运费</th>
                            <th width="33%">备注</th>
                        </tr>
                        <tr>
                            <td>
                                <input type="text" value="2015年4月14日 19:53:21" class="input-text input-max" />
                                <a href="javascript:void(0);"></a>
                            </td>
                            <td>免运费</td>
                            <td>预计商品下单后1天送达</td>
                        </tr>
                    </table>
                </div>
            </div> -->
            <div class="clearfix pay-type">
                <div class="step-tit">
                    <div class="line">
                        <i></i>
                    </div>
                    <span>支付方式</span>
                </div>
                <div class="step-con">
                    <dl>
                        <dt>支付方式</dt>
                        <dd>
                            <label>
                                <input type="radio" name="pay" value="online" />
                                在线支付  (为了保证及时处理您的订单，请您下单后<span>2小时</span>内付款)
                            </label>
                        </dd>
                        <dd>
                            <label>
                                <input type="radio" name="pay" value="unline" checked='checked' />
                                货到付款
                            </label>
                        </dd>
                    </dl>
                </div>
            </div>
            <div class="clearfix pro-info">
                <div class="step-tit">
                    <div class="line">
                        <i></i>
                    </div>
                    <span>货品信息</span>
                </div>
                <div class="step-con">
                    <p>
                        <#-- <span>配送包裹<em class="store-num"></em>个</span> -->
                        <b>进货单信息</b>
                        <span>商品数<em class="product-num"></em>个</span>
                        <a href="/cartlist/" class="goto-cartlist" target="_blank">修改进货单</a>
                    </p>
                    <div class="list">
                        <ul></ul>
                    </div>
                </div>
            </div>
            <#--<div class="clearfix invoice-info">
                <div class="step-tit">
                    <div class="line">
                        <i></i>
                    </div>
                    <span>发票信息</span>
                </div>
                <div class="step-con">
                    <div class="invoice-tit">
                        <label>
                            <input type="checkbox" />
                            开发票
                        </label>
                    </div>
                    <div class="item">
                        <label>
                            <input type="radio" name="invoice" checked="checked" value="person" />
                            个人
                        </label>
                        <label>
                            <input type="radio" name="invoice" value="firm" />
                            公司
                        </label>
                        <input type="text" class="input-text input-max" placeholder="请填写发票抬头" style="display: none;" />
                        -->
                        <#-- <a href="#">确定</a> -->
                    <#--</div>
                    <p>企业增值税发票请联系客服开具</p>
                </div>
            </div>-->
             <div class="clearfix coupon-info">
                <div class="step-tit">
                    <div class="line">
                        <i></i>
                    </div>
                    <span>优惠券</span>
                </div>
                <div class="step-con">
                    <div class="coupon-tit">
                        <label>
                            <input type="checkbox" />
                            使用优惠券
                        </label>
                        <em>*每次只能使用一张优惠券</em>
                        <a href="/coupon/" target="_blank">查看我的优惠券</a>
                    </div>
                    <table>
                        <tbody>
                        </tbody>
                        <tfoot>
                            <tr>
                                <td colspan="3">
                                    <span class="add-coupon">新增优惠券</span>
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
            <div class="clearfix order-list write-order-list">
                <div class="clearfix order-info">
                    <div class="r">
                        货品金额¥
                        <b class="total-price"></b>元 <!--+
                        运费¥<b class="fee-price"></b>元--> -
                        优惠券¥<b class="coupon-price">0</b>元
                        <span class="result">总计：¥<span class="result-price"></span>元</span>
                    </div>
                </div>
                <div class="order-btn">
                    <a href="javascript:void(0);" class="go-result">提交订单</a>
                </div>
            </div>
        </form>
    </div>
	<@footer />
	<@copyright />
	<@fixedSide />
    <@fixedSideOrder />
    <@fixedSideReplenish />
    <@cityChange />
    <div class="popup"></div>
    <@js />
    <script type="text/javascript">
        $(function () {
            var addAddress = $(".add-btn"),
                deliveryInfo = $(".write-order .delivery-info a"),
                addressListBox = $(".write-order .step-con .address-list"),
                proInfoBox = $(".write-order .pro-info .list ul"),
                cartIds = $(".cartId").val(),
                cartIdArr = cartIds.split(","),
                skusPrice = "";

            // 当前步骤
            $(".m-top .step li").eq(1).addClass('active');

            // 添加地址弹窗
            addAddress.on("click", function () {
                createPopup("max-box");
                createAddress();
                $(".popup").popup("新增收货地址");
                $(".popup").find("table").addClass("add").remove("edit");
            });

            // 编辑地址弹窗
            $(".write-order").on("click", ".edit", function () {
                var id = $(this).closest('.item').data("id");
                createPopup("max-box");
                createAddress();
                $(".popup").popup("编辑地址");
                $(".popup").find("table").addClass("edit").remove("add");
                $(".popup").find("table").attr("data-id", id)
                editAddress(id);
            });

            var provinceID = "",
                flag       = false;
            $(".popup").on("click", ".js-add-sub", function () {
                var table = $(this).closest('div').siblings('table'),
                    id          = table.data("id"),
                    addressInfo = table.find(".address-info").val(),
                    postcode    = table.find(".postcode-info").val(),
                    name        = table.find(".name-info").val(),
                    mobile      = table.find(".mobile-info").val(),
                    area        = table.find(".area-info").val(),
                    phone       = table.find(".phone-info").val(),
                    label       = table.find(".label-info").val(),
                    isDefault   = table.find(".default-address").is(":checked"),
                    obj = {
                        id              : id,
                        address         : addressInfo,
                        zipcode         : postcode,
                        consigneeName   : name,
                        consigneeMobile : mobile,
                        areaCode        : area,
                        consigneeTel    : phone,
                        isDefault       : isDefault,
                        addressComment  : label,
                        cityId          : locationCityID,
                        sectionId       : locationAreaID,
                        provinceId      : provinceID
                    };

                // 新增地址 提交按钮
                if (table.hasClass("add")) {
                    varifyAddress(obj);
                    if (flag) {
                        addAddressFun(obj);
                    }
                };

                // 编辑地址 提交按钮
                if (table.hasClass("edit")) {
                    varifyAddress(obj);
                    if (flag) {
                        updateAddress(obj);
                    }
                }

            });

            // 新增地址/编辑地址 返回按钮
            $(".popup").on("click", ".js-add-cal", function () {
                $(".popup").empty();
            });

            // 删除地址弹窗
           // $(".write-order").on("click", ".del", function () {
           //     $.message.confirm("确认删除地址？", "del", function(flag){
           //         if(flag){
           //             var id = $(".address-list").find(".active").data("id");
           //             delAddress(id);
           //         }
          //      });
          //  });

            $(".write-order").on("click", ".del", function () {
                var dom = "";
                createPopup("min-box");
                $(".popup").popup("确认删除地址？");
                dom +=  "<div class='del-address-box'>"+
                            "<a href='javascript:void(0);' class='btn-base btn-submit js-del-address'>确定</a>"+
                            "<a href='javascript:void(0);' class='btn-base btn-cancel js-add-cal'>取消</a>"+
                        "</div>";
                $(".popup").find(".content").append(dom);
            });

            // 删除地址按钮
            $(".popup").on("click", ".js-del-address", function () {
                var id = $(".address-list").find(".active").data("id");
                if (id == undefined) {
                    return;
                } else {
                    delAddress(id);
                }
            });

            // 创建地址
            function createAddress() {
                var addressDom = "",
                    contentBox = $(".popup").find(".content");
                addressDom +=   "<table class='address-table table'>"+
                                    "<tr>"+
                                        "<th widht='15%'><span class='table-tit'>城市：</span></th>"+
                                        "<td><i class='city-info'>"+ locationCityName + "(" + locationAreaName + ")" +"</i><span>（更换城市，请到顶部切换相应的城市）</span></td>"+
                                    "</tr>"+
                                    "<tr>"+
                                        "<th><span class='table-tit'><em>*</em>详细地址：</span></th>"+
                                        "<td><input type='text' class='input-text input-max address-info' /></td>"+
                                    "</tr>"+
                                    "<tr>"+
                                        "<th><span class='table-tit'>邮政编码：</span></th>"+
                                        "<td><input type='text' class='input-text input-max postcode-info' /></td>"+
                                    "</tr>"+
                                    "<tr>"+
                                        "<th><span class='table-tit'><em>*</em>收货人：</span></th>"+
                                        "<td><input type='text' class='input-text input-max name-info' /></td>"+
                                    "</tr>"+
                                    "<tr>"+
                                        "<th><span class='table-tit'><em>*</em>手机：</span></th>"+
                                        "<td class='address-moblie'>"+
                                            "<input type='text' class='input-text input-max mobile-info' />"+
                                        "</td>"+
                                    "</tr>"+
                                    "<tr>"+
                                        "<th><span class='table-tit'>固定电话：</span></th>"+
                                        "<td class='address-moblie'>"+
                                            "<input type='text' class='input-text input-min area-info' placeholder='区号' />"+
                                            "<span>-</span>"+
                                            "<input type='text' class='input-text phone-info' placeholder='固定电话' />"+
                                        "</td>"+
                                    "</tr>"+
                                    "<tr>"+
                                        "<th><span class='table-tit'>地址标注：</span></th>"+
                                        "<td class='address-label'>"+
                                        "<input type='text' class='input-text input-max label-info' />"+
                                        "<span>家里、公司，最多四个字。</span></td>"+
                                    "</tr>"+
                                    "<tr>"+
                                        "<th></th>"+
                                        "<td><input type='checkbox' class='default-address' />设为默认地址</td>"+
                                    "</tr>"+
                                "</table>"+
                                "<div class='address-btn'>"+
                                    "<a href='javascript:void(0);' class='btn-base btn-submit js-add-sub'>确定</a>"+
                                    "<a href='javascript:void(0);' class='btn-base btn-cancel js-add-cal'>取消</a>"+
                                "</div>";
                contentBox.append(addressDom);
            }

            // 配送信息
            deliveryInfo.on("click", function () {
                createPopup("max-box");
                createDeliveryInfo();
                $(".popup").popup("指定时间");
            });

            // 创建配送信息
            function createDeliveryInfo() {
                var deliveryInfoDom = "",
                    contentBox = $(".popup").find(".content");

                deliveryInfoDom +=  "<table class='delivery-info-table'>"+
                                        "<tr>"+
                                            "<th>时间段</th>"+
                                            "<td><span>3-28</span><span>今天</span></td>"+
                                            "<td><span>3-28</span><span>周日</span></td>"+
                                            "<td><span>3-28</span><span>周一</span></td>"+
                                            "<td><span>3-28</span><span>周二</span></td>"+
                                            "<td><span>3-28</span><span>周三</span></td>"+
                                            "<td><span>3-28</span><span>周四</span></td>"+
                                            "<td><span>3-28</span><span>周五</span></td>"+
                                            "<td><span>3-28</span><span>周六</span></td>"+
                                        "</tr>"+
                                        "<tr class='delivery-info-change'>"+
                                            "<th>09:00-19:00</th>"+
                                            "<td class='disable'></td>"+
                                            "<td class='active'>可选</td>"+
                                            "<td>可选</td>"+
                                            "<td>可选</td>"+
                                            "<td>可选</td>"+
                                            "<td>可选</td>"+
                                            "<td>可选</td>"+
                                            "<td>可选</td>"+
                                        "</tr>"+
                                    "</table>"+
                                    "<p class='delivery-info-tips'>温馨提示：我们会努力按照您指定的时间配送，但因天气、交通等各类因素影响，您的订单有可能会受到延误现象！<p>"+
                                    "<div class='delivery-info-btn'>"+
                                        "<a href='javascript:void(0);' class='btn-base btn-submit'>确定</a>"+
                                        "<a href='javascript:void(0);' class='btn-base btn-cancel'>取消</a>"+
                                    "</div>";
                contentBox.append(deliveryInfoDom);
            }

            // 配送日期选择
            $(".popup").on("click", ".delivery-info-change td", function () {
                if($(this).hasClass("disable")) {
                    return false;
                } else {
                    $(this).html("已选").addClass("active").siblings("td").html("可选").removeClass("active");
                    $(this).siblings(".disable").html(" ");
                };
            });

            // 选中地址
            addressListBox.on("click", ".item", function () {
                //$(this).addClass("active").siblings().removeClass('active');
            });

            // 获取地址列表
            function getAddressFun() {
                $.ajax({
                    type : "POST",
                    url : "/profile/address/list",
                    async: false,
                    dataType: "json",
                    data: {},
                    success : function (data) {
                        if (data.code == 200) {
                            createAddressList(data.result);
                        }
                    }
                });
            }
            getAddressFun();

            function createAddressList(list) {
                var addressListDom = "";
                //if (list) {}
                // for (var i = 0; i < list.length; i++) {
                //     addressListDom +=   "<div data-id='"+ list[i].id +"' class='item ";
                //                             if (i == 0) {
                //                                 addressListDom += "active'";
                //                             } else {
                //                                 addressListDom += "'";
                //                             }
                //                             addressListDom += ">"+
                //                             "<p>"+
                //                                 "<span class='name'>"+ list[i].consigneeName +"</span>(收)"+
                //                                 "<span class='telphone'>"+ list[i].consigneeMobile +"</span>"+
                //                                 //"<a href='#' class='del'>删除</a>"+
                //                                 //"<span class='r'>|</span>"+
                //                                 //"<a href='#' class='edit'>编辑</a>"+
                //                             "</p>"+
                //                             "<p>"+
                //                                 "<span class='province'>"+ list[i].province +"</span>"+
                //                                 "<span class='city'>"+ list[i].city +"</span>"+
                //                                 "<span class='section'>"+ list[i].section +"</span>"+
                //                                 "<span class='street'>"+ list[i].street +"</span>"+
                //                             "</p>"+
                //                             "<p class='address'>"+ list[i].address +"</p>"+
                //                             "<i></i>"+
                //                         "</div>";
                // }
                if (list.length > 0) {
                    var sectionid = list[0].sectionId;
                    addressListDom += "<div data-id='"+ list[0].id +"' data-sectionid='"+sectionid+"' style='float: left; width: 1050px;' class='item ";
                    if (locationAreaID == sectionid) {
                        addressListDom += "active'";
                    } else {
                        addressListDom += "'";
                    }
                    addressListDom += "><div><p>"+
                            "<span class='name'>"+ list[0].consigneeName +"</span>(收)"+
                            "<span class='telphone'>"+ list[0].consigneeMobile +"</span>"+
                        "</p>"+
                        "<p>"+
                            "<span class='province'>"+ list[0].province +"</span>"+
                            "<span class='city'>"+ list[0].city +"</span>"+
                            "<span class='section'>"+ list[0].section +"</span>"+
                            "<span class='street'>"+ list[0].street +"</span>"+
                        "</p>"+
                        "<p class='address'>"+ list[0].address +"</p>"+
                        "</div>";
                    if (locationAreaID != sectionid) {
                        addressListDom += "<div class='out-address'>该地址不在配送范围！</div>";
                    }
                    addressListDom += "<i></i></div>";
                } else {
                    addressListDom += "<div class='item'>还没有收货地址哦！</div>";
                }
                addressListBox.empty();
                addressListBox.append(addressListDom);
            }

            // 新增收货地址
            function addAddressFun(obj) {
                $.ajax({
                    type : "POST",
                    url : "/profile/address/add",
                    async: false,
                    contentType: "application/json",
                    data: JSON.stringify({
                        "cityId"            : obj.cityId,
                        "sectionId"         : obj.sectionId,
                        //"provinceId"        : obj.provinceId,
                        "address"           : obj.address,
                        "zipcode"           : obj.zipcode,
                        "consigneeName"     : obj.consigneeName,
                        "consigneeMobile"   : obj.consigneeMobile,
                        "areaCode"          : obj.areaCode,
                        "consigneeTel"      : obj.consigneeTel,
                        "addressComment"    : obj.addressComment,
                        "isDefault"         : obj.isDefault
                    }),
                    success : function (data) {
                        if (data.code == 200) {
                            $(".popup").empty();
                            getAddressFun();
                            $.message.alert("添加成功！", "success");
                        } else {
                            $.message.alert("添加失败！", "fail");
                        }
                    }
                });
            }

            // 验证地址表单
            function varifyAddress(obj) {
                flag = false;
                if(obj.address == "") {
                    $.message.alert("请填写详细地址！", "info", function(){
                         $(".address-info").focus();
                    });
                } else if (obj.consigneeName == "") {
                    $.message.alert("请填写收件人信息!", "info", function(){
                         $(".name-info").focus();
                    });
                } else if (obj.consigneeMobile == "") {
                    $.message.alert("请填写手机号码！", "info", function(){
                         $(".mobile-info").focus();
                    });
                } else if (obj.consigneeMobile != "") {
                    var mobile = /^(13[0-9]|14[5|7]|15[0-9]|17[6|7]|18[0-9])\d{8}$/;
                    if(mobile.test($(".mobile-info").val()) == false) {
                        $.message.alert("请正确填写手机号码!", "info");
                        $(".mobile-info").focus();
                    } else {
                        flag = true;
                    }
                }
            }

            // 编辑地址
            function editAddress(id) {
                $.ajax({
                    type : "GET",
                    url : "/profile/address/editPage",
                    async: false,
                    contentType: "application/json",
                    data : {
                        addressId : id
                    },
                    success : function (data) {
                        if (data.code == 200) {
                            var obj = data.result;
                            $(".address-info").val(obj.address);
                            $(".postcode-info").val(obj.zipcode);
                            $(".name-info").val(obj.consigneeName);
                            $(".mobile-info").val(obj.consigneeMobile);
                            $(".area-info").val(obj.areaCode);
                            $(".phone-info").val(obj.consigneeTel);
                            $(".label-info").val(obj.addressComment);
                            provinceID = obj.provinceId;
                            if (obj.isDefault) {
                                $(".default-address").attr("checked", true);
                            }
                        }
                    }
                });
            }

            // 更新地址
            function updateAddress(obj) {
                $.ajax({
                    type : "POST",
                    url : "/profile/address/update",
                    async: false,
                    contentType: "application/json",
                    data : JSON.stringify({
                        "id"                : obj.id,
                        "cityId"            : obj.cityId,
                        "sectionId"         : obj.sectionId,
                        "provinceId"        : obj.provinceId,
                        "address"           : obj.address,
                        "zipcode"           : obj.zipcode,
                        "consigneeName"     : obj.consigneeName,
                        "consigneeMobile"   : obj.consigneeMobile,
                        "areaCode"          : obj.areaCode,
                        "consigneeTel"      : obj.consigneeTel,
                        "addressComment"    : obj.addressComment,
                        "isDefault"         : obj.isDefault
                    }),
                    success : function (data) {
                        if (data.code == 200) {
                            $(".popup").empty();
                            getAddressFun();
                            $.message.alert("编辑成功！", "success");
                        } else {
                            $.message.alert("编辑失败", "fail");
                        }
                    }
                });
            }

            // 删除地址
            function delAddress(id) {
                $.ajax({
                    type : "GET",
                    url : "/profile/address/delete",
                    async: false,
                    dataType: "json",
                    data : {
                        "id" : id
                    },
                    success : function (data) {
                        if (data.code == 200) {
                            $(".popup").empty();
                            getAddressFun();
                        }

                    }
                });
            }

            // 包裹信息 并计算包裹总价
            var storeNum = $(".store-num"),
                productNum = $(".product-num"),
                resultNum = $(".result-price"),
                feeNum = $(".fee-price"),
                totalNum = $(".total-price"),
                totalPrice = 0;
            function cartListmini() {
                $.ajax({
                    cache: false,
                    type : "GET",
                    url : "/cart/listmini",
                    async: false,
                    dataType: "json",
                    data : {
                        cartIds : cartIds
                    },
                    success : function (data) {
                        //console.log(data);
                        if (data.code == 200) {
                        	// 防止用户提交订单之后，在返回该页面报错
                        	if (data.result.cartInfoVO != null) {
	                            var list = data.result,
	                                fee = 0;
	                            totalPrice = list.cartInfoVO.totalPrice;
	                            resultPrice = fee + totalPrice;
	                            //console.log(resultPrice);

	                            storeNum.html(list.cartInfoVO.storeNum);
	                            productNum.html(list.cartInfoVO.productNum);
	                            totalNum.html(totalPrice);
	                            feeNum.html(fee);
	                            resultNum.html(resultPrice);
	                            createProInfo(list);
                            }
                        }
                    }
                });
            }
            cartListmini();

            // 创建包裹产品列表
            function createProInfo(list) {
                //console.log(cartIdArr);
                //console.log(list);
                var proList = list.cartStoreList,
                    dom = "";
                for ( var i = 0; i < proList.length; i++) {
                    dom +=  "<li>"+
                                "<p class='clearfix'>"+
                                    "店铺名称：<a href='"+ proList[i].storeUrl +"' class='store-name' target='_blank'>"+ proList[i].storeName +"</a>"+
                                    "总价：￥<span class='total-price'>"+ proList[i].totalPrice +"</span>"+
                                "</p>"+
                                "<table class='table'>"+
                                    "<thead>"+
                                        "<tr>"+
                                        "<th width='55%'>商品</th>"+
                                        "<th width='15%'>数量</th>"+
                                        "<th width='15%'>单价(元)</th>"+
                                        "<th width='15%'>小计(元)</th>"+
                                        "</tr>"+
                                    "</thead>"+
                                    "<tbody>";
                                    var skulist = proList[i].skulist;
                                    for ( var j = 0; j < skulist.length; j++) {
                                        var skulistId = skulist[j].id,
                                            skulistId = skulistId.toString();
                                        if ($.inArray(skulistId, cartIdArr) >= 0) {
                                            skusPrice += skulist[j].id + "|" + skulist[j].cartPrice + ",";
                                           dom +=  "<tr data-id='"+ skulist[j].id +"'>"+
                                                    "<td><a href='/product/detail?skuId="+ skulist[j].productId +"' data-id='"+ skulist[j].id +"' target='_blank'>"+ skulist[j].name +"</a></td>"+
                                                    "<td><b>"+ skulist[j].count +"</b></td>"+
                                                    "<td><b>"+ skulist[j].cartPrice +"</b></td>"+
                                                    "<td><b class='pro-total-price'>"+ (skulist[j].count*skulist[j].cartPrice).toFixed(2) +"</b></td>"+
                                                "</tr>";
                                        }
                                    }
                                dom += "</tbody>"+
                                "</table>"+
                            "</li>";
                }
                proInfoBox.append(dom);
                skusPrice = skusPrice.substring(0, skusPrice.length - 1);
                //console.log(skusPrice);

                $(".write-order .pro-info").find("table").each(function () {
                    var len = $(this).find("tbody tr").length,
                        nullTr = "<tr><td colspan='4' algin='center'>暂无产品！</td></tr>";
                    if (len < 1 ) {
                        $(this).find("tbody").append(nullTr);
                    }
                });
            }

            // 设置标题相对应高度
            function setItemHeight() {
                $(".step-tit").each( function () {
                    var thisH = $(this).siblings(".step-con").outerHeight(true);
                    $(this).css({
                        height : thisH
                    });
                });
            }
            setItemHeight();

            // 发票选择
            $(".invoice-tit input").on("click", function () {
                if ($(this).is(":checked")) {
                    $(".write-order .invoice-info .item").show();
                } else {
                    $(".write-order .invoice-info .item").hide();
                }
            });
            var invoiceVal = "";
            $(".invoice-info label").on("click", function () {
                invoiceVal = $(this).find("input[name='invoice']:checked").val();
                if (invoiceVal == "firm") {
                    $(".invoice-info input[type='text']").show();
                    $(".invoice-info p").show();
                } else {
                    $(".invoice-info input[type='text']").hide();
                    $(".invoice-info p").hide();
                }
            });

            // 优惠券选择
            $(".coupon-tit input").on("click", function () {
                if ($(this).is(":checked")) {
                    $(".write-order .coupon-info table").show();
                } else {
                    $(".write-order .coupon-info table").hide();
                    $(".coupon-price").html(0);
                    $(".result-price").html(totalPrice);
                    $("#payOrder input[name='userCouponId']").val("");
                }
            });

            // 选取优惠券
            $(".write-order .coupon-info table").on("click", "label", function () {
                $(this).closest('tr').addClass('active').siblings().removeClass('active');
                var userCouponId = $(this).closest('tr').data("id"),
                    couponPrice  = $(this).data("price"),
                    couponDiscount = $(this).data("discount");
                if (couponPrice != undefined) {
                    resultCoupon = totalPrice.subtr(couponPrice);
                    if (resultCoupon < 0 ) resultCoupon = 0;
                    $(".coupon-price").html(couponPrice);
                    $(".result-price").html(resultCoupon);
                } else {
                    $(".coupon-price").html(totalPrice.mul(couponDiscount));
                    $(".result-price").html(totalPrice.subtr(totalPrice.mul(couponDiscount)));
                }
                $("#payOrder input[name='userCouponId']").val(userCouponId);
            });

            // 创建优惠券列表
            var couponValArr = [];
            function getCouponList() {
                var districtId = $(".region-select em").data("code"),
                    grossPrice = $(".order-list .result span").html();
                var couponListDom = "",
                    couponListBox = $(".coupon-info tbody");
                $.ajax({
                	cache: false,
                    url : "/mycoupon/data/couponList/canuse/",
                    type : "GET",
                    dataType : "json",
                    data : {
                        districtId : locationAreaID,
                        grossPrice : grossPrice
                    },
                    success : function (data) {
                        //console.log(data);
                        if (data.code == 200) {
                            var list = data.result.list;
                            //console.log(list.length);
                            if (list != null) {
                                couponListBox.empty();
                                if (list.length == 0) {
                                    $(".coupon-tit input").attr("checked", false);
                                    $(".write-order .coupon-info table").hide();
                                    couponListDom = "<tr><td colspan='3'><span class='null-box'>此订单暂无可用的优惠券！</span></td></tr>";
                                } else {
                                    $(".coupon-tit input").attr("checked", "true");
                                    $(".write-order .coupon-info table").show();
                                    for (var i = 0; i < list.length; i++) {
                                        var items       = list[i].items,
                                            items       = JSON.parse(items),
                                            status      = list[i].couponState,
                                            type        = list[i].favorType,
                                            startTime   = list[i].startTime,
                                            endTime     = list[i].endTime,
                                            nowTime     = Date.parse(new Date()),
                                            Sdates      = new Date(startTime),
                                            Syear       = Sdates.getFullYear(),
                                            Smonth      = Sdates.getMonth() + 1,
                                            Sdate       = Sdates.getDate(),
                                            Edates      = new Date(endTime),
                                            Eyear       = Edates.getFullYear(),
                                            Emonth      = Edates.getMonth() + 1,
                                            Edate       = Edates.getDate(),
                                            startTime   = Syear + "-" + Smonth + "-" + Sdate,
                                            endTime     = Eyear + "-" + Emonth + "-" + Edate,
                                            discount     = 0;
                                       //console.log(items);

                                        if (status != 3 && status != 5) {
                                            // 设置提醒过期时间为5天
                                            if ( (Number(list[i].endTime) - Number(nowTime)) / 1000 <= (60 * 60 * 24) * 5 ) {
                                                status = 9;
                                            }
                                        }
                                        couponListDom += "<tr data-id='"+ list[i].userCouponId +"'>"+
                                            "<td width='20%'>";
                                            if (type == 0) {
                                                couponListDom += "<label data-price='"+ items[0].result[0].value +"'><input type='radio' name='coupon'>满"+ items[0].condition.value + items[0].result[0].value +"元</label>";
                                            } else {
                                                //discount = parseFloat(items[0].result[0].value).accDiv(10);
                                                //console.log(discount);
                                                couponListDom += "<label data-discount='"+ items[0].result[0].value +"'><input type='radio' name='coupon'>"+ items[0].result[0].value +"折</label>";
                                            }
                                            couponListDom += "</td>"+
                                            "<td width='15%'>有效期至"+ endTime +"</td>"+
                                            "<td>"+
                                                "<span>券码："+ list[i].couponCode +"</span>";
                                            switch(status)
                                            {
                                            case 2:
                                                couponListDom += "<span class='status'>［已过期］</span>";
                                                break;
                                            case 3:
                                                couponListDom += "<span class='status'>［已被使用］</span>";
                                                break;
                                            case 5:
                                                couponListDom += "<span class='status'>［已失效］</span>";
                                                break;
                                            case 8:
                                                couponListDom += "<span class='status'>［未失效］</span>";
                                                break;
                                            case 9:
                                                couponListDom += "<span class='status'>［即将过期］</span>";
                                                break;
                                            default:
                                                couponListDom += "";
                                            }
                                            couponListDom +="</td></tr>";
                                    couponValArr.push(items[0].result[0].value);
                                    }
                                }

                                couponListBox.append(couponListDom);

								var max = Math.max.apply(null, couponValArr),
									index;
                                for(var i = 0; i < couponValArr.length; i++) {
                                	if (couponValArr[i] == max) {
                                		index = i;
                                	}
                                }
                                $(".write-order .coupon-info table").find("tr").eq(index).addClass("active").find("input").attr("checked","true");
                                $(".write-order .coupon-info table").find("tr").eq(index).find("label").trigger("click");
                            }
                        }
                    }
                });
            }
            getCouponList();

            // 添加优惠券
            var addCouponDom = "",
                timestamp = "";
            $(".add-coupon").on("click", function () {
                createPopup("mid-box");
                $(".popup").popup("新增优惠券");
                createCoupon();
            });

            // 创建绑定优惠券dom
            function createCoupon() {
                addCouponDom = "";
                addCouponDom += "<div class='add-coupon-box'>"+
                                    "<div class='code-box clearfix'>"+
                                        "<input type='text' placeholder='请输入优惠券码' class='input-text coupon-code' />"+
                                        "<input type='text' placeholder='请输入验证码' class='input-text verify-code' />"+
                                        "<img src='/brand/genverifycode' class='verify-img' />"+
                                    "</div>"+
                                    "<div class='btn'>"+
                                        "<a href='javascript:void(0);' class='btn-base btn-submit add-coupon'>确定</a>"+
                                    "</div>"+
                                "</div>";
                $(".popup").find(".content").append(addCouponDom);
            }

            $(".popup").on("click", ".add-coupon", function () {
                var couponCode = $(".popup").find(".coupon-code").val(),
                    verifyCode = $(".popup").find(".verify-code").val();

                if (couponCode == "") {
                    $(".coupon-code").focus().addClass("input-error");
                } else if (verifyCode == "") {
                    $(".coupon-code").removeClass("input-error");
                    $(".verify-code").focus().addClass("input-error");
                } else {
                    $(".coupon-code").removeClass("input-error");
                    $(".verify-code").removeClass("input-error");
                    addCoupon(couponCode, verifyCode);
                }
            });

            function addCoupon(couponCode, verifyCode) {
                $.ajax({
                    url : "/mycoupon/bindCoupon/",
                    type : "GET",
                    dataType : "json",
                    data : {
                        couponCode : couponCode,
                        verifyCode : verifyCode
                    },
                    success : function (data) {
                        //console.log(data);
                        if (data.code == 200) {
                            $(".popup").empty();
                            $.message.alert("添加优惠券成功！", "success");
                            getCouponList();
                        } else {
                            $(".popup").empty();
                            if (data.result == 7) {
                                $.message.alert("该卡已被激活！", "fail");
                            } else {
                                $.message.alert("请输入正确的优惠券码或验证码！", "info");
                            }
                        }
                    }
                });
            }

            // 刷新验证图片
            $(".popup").on("click", ".verify-img", function () {
                timestamp = Date.parse(new Date());
                $(this).attr("src", "/brand/genverifycode?" + timestamp);
            });

            // 提交订单
            $(".go-result").click( function() {
                var addressId = $(".address-list").find(".active").data("id"),
                    sectionId = $(".address-list").find(".active").data("sectionid"),
                    cartIds = "",
                    payMethod = "",
                    receipt = "",
                    hbCash = "",
                    requestId = "";

                // if(locationAreaID != sectionId){
                //     $.message.alert("该地址不在配送范围！","info");
                //     return;
                // } else
                //console.log(addressId);
                if (addressId == undefined) {
                    $.message.alert("请添加收货地址！", "info", function(){
				        window.location.href = "/profile/address";
				    });
                    return;
                }
                $(".pro-info .list tbody tr").each( function () {
                    var id = $(this).data("id");
                    cartIds += id + ",";
                });

                var payVal = $(".pay-type input[name='pay']:checked").val();
                if (payVal == "online") {
                    payMethod = "2";
                } else if (payVal == "unline") {
                    payMethod = "1";
                } else {
                    payMethod = "0";
                }

                // 是否开发票  （如果不开发票，就传空值：“”；如果开发票，个人发票，则传“个人”，公司发票就填用户填写的抬头）
                if($(".invoice-tit input").is(':checked')) {
                    invoiceVal =  $(".invoice-info label input[name='invoice']:checked").val();
                    if (invoiceVal == "firm") {
                        receipt = $(".invoice-info input[type='text']").val();
                        if (receipt == "") {
                            $.message.alert("请填写发票台头！", "info");
                            $(".invoice-info .input-text").focus();
                            return;
                        }
                    } else {
                        receipt = "个人";
                    }
                } else {
                    receipt = "";
                }

                $("#payOrder input[name='addressId']").val(addressId);
                $("#payOrder input[name='cartIds']").val(cartIds);
                $("#payOrder input[name='payMethod']").val(payMethod);
                $("#payOrder input[name='receipt']").val(receipt);
                $("#payOrder input[name='hbCash']").val(hbCash);
                $("#payOrder input[name='skusPrice']").val(skusPrice);

                if (cartIds != "") {
                	$("#payOrder").submit();
                }

            });

        });
    </script>
</body>
</html>
</@compress>
</#escape>