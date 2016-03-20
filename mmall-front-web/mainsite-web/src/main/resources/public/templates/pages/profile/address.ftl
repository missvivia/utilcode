<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
    <head>
        <#include "/wrap/common.ftl">
        <#include "/wrap/my.common.ftl">
        <@title type="address"/>
        <@css/>
    </head>
    <body>
        <@fixedTop />
        <@topbar />
        <@top />
        <@mainCategory />
        <div class="m-crumbs wrap">
            <a href="/">首页</a><span>&gt;</span><a href="/profile/basicinfo">个人中心</a><span>&gt;</span><span class="selected">收货地址</span>
        </div>
        <div class="m-wrap clearfix">
            <@mSide />
            <div class="m-main r">
                <div class="title">
                    <b>收货地址</b>
                </div>
                <div class="content">
                    <div class="add-has">
                        <div class="add-tips">
                            信息有误？请联系0571-87651759
                        </div>
                        <table width="100%">
                            <thead>
                                <tr>
                                    <th width="10%">地址类型</th>
                                    <th>地址信息</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <div class="add-null">
                        <p>
                            您暂时没有收货地址
                        </p>
                        <p>
                            <span class="add-address">新增地址</span>
                        </p>
                    </div>
                </div>
            </div>
        </div>
        <@footer/>
        <@copyright/>
        <@cityChange />
        <div class="popup"></div>
        <@js />
        <script type="text/javascript">
            $(function () {

                // 设置侧边栏选中
                $(".m-side li").eq(5).addClass("active");

                // 获取地址信息
                function getAddress() {
                    $.ajax({
                        url : "/profile/address/list",
                        type: "POST",
                        cache: false,
                        contentType: "application/json",
                        data : JSON.stringify({
                           limit: "10",
                           offset: "0"
                        }),
                        success: function (data) {
                            //console.log(data);
                            if (data.code == 200) {
                                if (data.result.length > 0) {
                                    $(".add-has").show();
                                    var list = data.result;
                                    createAddressList(list);
                                } else {
                                    $(".add-null").show();
                                }
                            }
                        }
                    });
                }
                getAddress();

                // 获取当前区域
                var onlineAreas = "";
                function onlineArea() {
					$.ajax({
						type : "GET",
						url: " /location/onlineArea",
						async: false,
						dataType: "json",
						data :{
						},
						success: function (data) {
                            if (data.code == 200) {
                                onlineAreas = data.result;
                            } else {
                                return;
                            }
                            //console.log(onlineAreas);
						}
					});
				}
				onlineArea();

                // 创建地址列表
                var addressList = "";
                function createAddressList(list) {
                    addressList = "";
                    for (var i = 0; i < list.length; i++) {
                        addressList += "<tr>";
                        if (list[i].isDefault) {
                            addressList += "<td>默认地址</td>";
                        } else {
                            addressList += "<td></td>";
                        }
                        addressList +=
                            "<td>"+
                                "<p>"+ list[i].province + list[i].city + list[i].section + list[i].address + "</p>"+
                                "<p><span>"+ list[i].consigneeName + "</span><span>"+ list[i].consigneeMobile + "</span></p>"+
                                //"<p><span>"+ list[i].areaCode + "-" + list[i].consigneeTel + "</span><span>" + list[i].addressComment + "</span><span>" + list[i].zipcode +"</span></p>"+
                            "</td>"+
                        "</tr>";
                    }
                    $(".add-has tbody").empty();
                    $(".add-has tbody").append(addressList);
                }

                // 添加地址弹窗
                var addAddress = $(".add-address");
                addAddress.on("click", function () {
                    createPopup("max-box");
                    createAddress();
                    $(".popup").popup("新增收货地址");
                    $(".popup").find("table").addClass("add").remove("edit");
                });

                // 创建地址
                function createAddress() {
                    var addressDom = "",
                        contentBox = $(".popup").find(".content");
                    addressDom +=   "<table class='address-table table'>"+
                                        // "<tr>"+
                                        //     "<th widht='15%'><span class='table-tit'>城市：</span></th>"+
                                        //     "<td><i class='city-info'>"+ locationCityName + "(" + locationAreaName + ")" +"</i><span>（更换城市，请到顶部切换相应的城市）</span></td>"+
                                        // "</tr>"+
                                        "<tr>"+
                                            "<th widht='15%'><span class='table-tit'>城市：</span></th>"+
                                            "<td>"+
                                                "<select>"+
                                                    "<option>请选择地区</option>";
                                                    for(var i = 0; i < onlineAreas.length; i++) {
                                                        addressDom += "<option value='"+ onlineAreas[i].code +"'>"+ onlineAreas[i].name +"</option>";
                                                    }
                    addressDom += "</select>"+
                                            "</td>"+
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

                // 删除地址按钮
                $(".popup").on("click", ".js-del-address", function () {
                    var id = $(".address-list").find(".active").data("id");
                    if (id == undefined) {
                        return;
                    } else {
                        delAddress(id);
                    }
                });

                var provinceID = "",
                    flag       = false,
                    sectionId = "";
                $(".popup").on("change", "select", function () {
                    sectionId = $(this).val();
                });

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
                            //cityId          : locationCityID,
                            //sectionId       : locationAreaID,
                            sectionId       : sectionId
                            //provinceId      : provinceID
                        };

                    // 新增地址 提交按钮
                    if (table.hasClass("add")) {
                        varifyAddress(obj);
                        if (flag) {
                            addAddressFun(obj);
                        }
                    };
                });

                // 新增地址/编辑地址 返回按钮
                $(".popup").on("click", ".js-add-cal", function () {
                    $(".popup").empty();
                });

                // 验证地址表单
                function varifyAddress(obj) {
                    flag = false;
                    if (obj.sectionId == "") {
                        $.message.alert("请选择配送地区", "info");
                    } else if(obj.address == "") {
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

                // 新增收货地址
                function addAddressFun(obj) {
                    $.ajax({
                        type : "POST",
                        url : "/profile/address/add",
                        async: false,
                        contentType: "application/json",
                        data: JSON.stringify({
                            //"cityId"            : obj.cityId,
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
                                getAddress();
                                $.message.alert("添加成功！", "success", function (){
                                	$(".add-null").hide();
                                    //window.location.href = "/profile/address";
                                });
                            } else {
                                $.message.alert("添加失败！", "fail");
                            }
                        }
                    });
                }

            });
        </script>
    </body>
</html>
</@compress>
</#escape>