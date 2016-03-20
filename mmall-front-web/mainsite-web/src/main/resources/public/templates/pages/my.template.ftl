<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "/wrap/common.ftl">
<#include "/wrap/my.common.ftl">
<html>
  <head>
    <@title content="页面标题"/>
    <meta charset="utf-8"/>
    <@css/>
    <!-- @STYLE -->
    <link rel="stylesheet" type="text/css" href="/src/css/page/myAddress.css">
  </head>
  <body id="index-netease-com">
  	<@topbar>
    </@topbar>

    <@navbar index=0/>
    <@crumbs>
    	<#-- 面包屑 -->
		 <a href="/index">首页</a><span>&gt;</span><a href="/profile/index">个人中心</a><span>&gt;</span><span class="selected">我的mmall</span>
    </@crumbs>
    <@myModule sideIndex=3>
		<#-- 主模块内容 -->
    </@myModule>
    <@footer/>

    <@template>

      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->

    </@template>

    <#noparse>
    <!-- @SCRIPT -->
    </#noparse>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script>
NEJ.define([
    'base/klass',
    'pro/widget/module'
],function(_k,_m,_p,_o,_f,_r,_pro){
    /**
     * 页面模块基类
     *
     * @class   _$$Module
     * @extends _$$Module
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);

        // TODO
    };

    // init page
    _p._$$Module._$allocate();
});
   </script>
  </body>
</html>
</@compress>
</#escape>