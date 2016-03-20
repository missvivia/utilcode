<#if audit==0>
  <#assign pageName="promotion-packet"/>
<#else>
  <#assign pageName="promotion-packetaudit"/>
</#if>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
  <!-- @STYLE -->
  <link rel="stylesheet" href="/src/css/page/product_manage.css?v=1.0.0.0">
</head>
<body>


<@side />
<#-- top and footer need match -->
<@wrap>

<@crumbs parent={"txt":"促销管理","url":'#'} sub={"txt":"红包列表"}/>

<!-- card -->
<div class="row">
  <div class="col-sm-12">
    <div class="m-card">
      <h2 class="card_b">
        <span class="glyphicon glyphicon-chevron-down pull-right"></span>
        所有红包
        </h2>
      <div class="card_c">
        <#-- packet list -->
        <packet-list audit={{audit}}></packet-list>
      </div>
    </div>
  </div>
</div>

</@wrap>

<script>
  window['__data__'] = {
    audit: ${audit}
  };
</script>

<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/promotion/packet.js?v=1.0.0.0"></script>

</body>
</html>
