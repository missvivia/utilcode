<#assign pageName="index-phones"/>
<#escape x as x?html>
<@compress single_line=true>
<!doctype html>
<html lang="zh-cn">
<head>
  <#include "/wrap/common.ftl" />
  <meta charset="UTF-8" />
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/jit/phones.css">
</head>
<body>
<@side />
<@wrap>
    <@crumbs parent={"txt":"提醒设置"} />
    <div class="row">
      <div class="col-sm-12">
        <div class="m-card">
          <h2 class="card_b"><span class="glyphicon glyphicon-chevron-down pull-right"></span>短信号码绑定</h2>
          <div class="card_c">
            <div class="form-horizontal">
              <#if phones?exists>
              <#list phones as phone>
              <#if phone_index == 0>
              <div class="form-group">
                <label class="col-sm-1 control-label">商家ID：</label>
                <div class="col-sm-10">
                  <p class="form-control-static">${(phone.businessAccount)?default('')}</p>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-1 control-label">公司名称：</label>
                <div class="col-sm-10">
                  <p class="form-control-static">${(phone.supplierName)?default('')}</p>
                </div>
              </div>
              </#if>
              </#list>
              <h3>导单提醒</h3>
              <#list phones as phone>
              <form method="POST" class="form-group" id="saveform${phone_index+1}">
                <label class="col-sm-1 control-label">号码${phone_index+1}：</label>
                <div class="col-sm-2">
                  <input type="hidden" value="" name="credential" class="ztag"/>
				  <input type="hidden" value="" name="expiredTime" class="ztag"/>
				  <input type="hidden" value="" name="sign" class="ztag"/>
				  
               	  <input type="hidden" value="${(phone.orderId)?default('')}" name="orderId" />
               	  <input type="hidden" value="${(phone.phone)?default('')}" name="oldphone" />
                  <input type="text" class="form-control ztag" placeholder="请输入11位手机号码" maxlength="11" value="${(phone.phone)?default('')}" name="newphone"/>
                </div>
                <div class="col-sm-1">
                  <a href="#" class="btn btn-default ztag" role="button">获取验证码</a>
                </div>
                <div class="col-sm-2">
                  <input type="text" class="form-control ztag" placeholder="请输入验证码" name="yzm"/>
                </div>
                <div class="col-sm-1">
                  <button class="btn btn-primary btn-block ztag" type="button">保存</button>
                </div>
                <div class="col-sm-3">
                  <div class="m-info ztag"></div>
                </div>
              </form>
              </#list>
              </#if>
            </div>
            <div class="hr-dashed"></div>
            <h3>操作日志</h3>
            <#--<m-sizelist></m-sizelist>-->
            <div id="size-list-box"></div>
          </div>
        </div>
      </div>
    </div>
         
</@wrap>

<script type="text/javascript">
  <#noescape>
  var g_actList = ${JsonUtils.toJson(log![])};
  </#noescape>
</script>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/jit/phones.js"></script>

</body>
</html>
</@compress>
</#escape>