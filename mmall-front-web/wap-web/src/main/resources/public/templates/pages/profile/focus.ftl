<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../../wrap/3g.common.ftl">
<html>
  <head>
    <title>个人中心页</title>
    <@meta/>
    <@less />
  </head>
  <body>
  	<div class="wrap clearfix">
         <header class="headerbox">
		     <div class="m-topnav">
		        <a class="curp goBack">
				    <i class="f-fl u-menu"></i>
			    </a>
			    <span class="tt">我的收藏</span>
		  	 </div>
	    </header>
    <@module>
    	<div class="focus-box" id="focus"></div>
    </@module>
   </div>
    	<div id="page" class="load-page"><span></span></div>    
    <@template>
    
      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->
      
    </@template>    
    <script src="/src/frame/jquery-1.11.3.min.js"></script>
    <script>
          function setFontSize() {
				var wrapWidth = $(".wrap").width(),
					fontSize = (wrapWidth / 640) * 50;
				$("html").css("font-size", fontSize + "px");
		  }
		  setFontSize();
		  
		  $(window).resize(function() {
				setFontSize();
		  });
		  
		  $(".goBack").on("click", function () {
				window.history.go(-1);
		  });
	
    </script>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/profile/focus.js"></script>
  </body>
</html>
</@compress>
</#escape>