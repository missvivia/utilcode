<#assign pageName="jit-poreport"/>
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
  <link rel="stylesheet" href="/src/css/page/jit/poreturn.css">
</head>
<body>
<@side />
<@wrap>
    <@crumbs parent={"txt":"JIT管理","url":"#"} sub={"txt":"PO单报表","url":"/jit/poreport"} sub2={"txt":"退供物流信息"}/>
  
    <div class="row">
      <div class="col-sm-12">
        <div class="m-card">
          <div id="size-list-box"></div>
        </div>
      </div>
    </div>
         
</@wrap>

<script type="text/javascript">
  <#noescape>
  var g_returnList = ${JsonUtils.toJson(poReturnList![])};
  </#noescape>
</script>

<!-- @SCRIPT -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/jit/poreturn.js"></script>

</body>
</html>
</@compress>
</#escape>