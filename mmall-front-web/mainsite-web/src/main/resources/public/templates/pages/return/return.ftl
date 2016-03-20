<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
  <head>
    <#include "../../wrap/common.ftl">
    <@title content="退货申请"/>
    <meta charset="utf-8"/>
    <@css/>
    <!-- @STYLE -->
    <link rel="stylesheet" type="text/css" href="/src/css/page/returnRequestSteps.css">
  </head>
  <body id="index-netease-com">

    <@topbar/>
    <@sidebar/>
    <@navbar index=0/>
    <@module>
    <div class="g-bd">
       <div id="summary"></div>
       <div id="steps-box"></div>



    </div>



      <#-- Page Content Here -->

    </@module>
    <@footer/>

    <@template>

      <textarea name="txt" id="txt-template-steps" class="f-dn">
         <div id="steps">
              <div class="m-crumbs">
                  <a href="/index">首页</a><span>&gt;</span><a href="/profile/index">个人中心</a><span>&gt;</span><a href="/myorder/#state=0">订单管理</a><span>&gt;</span><span class="selected">退货</span>
                </div>
                   <div class="m-requeststeps">
                      <ul class="f-cb" id="tab-box">
                        <li class="itm1" data-value="step1">
                            <span class="icon">1</span>
                            <h3>申请退货</h3>
                        </li>
                        <li class="itm2" data-value="step2">
                            <span class="icon">2</span>
                            <h3>填写退货信息</h3>
                        </li>
                        <li class="itm3" data-value="step3">
                            <span class="icon">3</span>
                            <h3>寄回商品</h3>
                        </li>
                        <li class="itm4" data-value="step4">
                            <span class="icon" >4</span>
                            <h3>客服退款</h3>
                        </li>
                      </ul>
                      <div class="line"></div>
                    </div>
                <div id="step1" class="step">
                </div>
                <div id="step2" class="step">
                </div>
                <div id="step3" class="step">
                </div>
                <div id="step4" class="step">
                </div>
        </div>
      </textarea>


    </@template>


    <script type="text/javascript">
    <#noescape>
      var g_return = ${JsonUtils.toJson(data)};
     </#noescape>
    </script>
    
    <#noparse>
    <!-- @SCRIPT -->
    </#noparse>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/return/index.js"></script>
  </body>
</html>
</@compress>
</#escape>