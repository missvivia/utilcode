<#assign pageName="promotion-packetEdit"/>
<!doctype html>
<html lang="en">
<head>
<#include "/wrap/common.ftl" />
  <meta charset="UTF-8">
  <title>${siteTitle} - ${page.title}</title>
  <#include "/wrap/css.ftl">
</head>
<body>

<@side />
<#-- top and footer need match -->
<@wrap>
<@crumbs parent={"txt":"促销管理","url":'#'} sub={"txt":"编辑红包"}/>
<packet-edit vo={{vo}} editable={{editable}}></packet-edit>
</@wrap>
<script>
  window['__data__'] = {
    vo: ${JsonUtils.toJson(redPacketVO)},
    editable: ${JsonUtils.toJson(editable)}
  };
</script>


<!-- @script -->
<script src="${jslib}define.js?pro=${jspro}"></script>
<script src="${jspro}page/promotion/packetEdit.js?v=1.0.0.0"></script>
</body>
</html>