<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "../../wrap/3g.common.ftl">
<html>
  <head>
    <title>个人资料</title>
    <@meta/>
    <@less />
  </head>
  <body>
      <div class="wrap">
	    	 <header class="headerbox">
			     <div class="m-topnav">
			        <a class="curp back">
					    <i class="f-fl u-menu"></i>
				    </a>
				    <span class="tt">个人资料</span>
			  	 </div>
		    </header>
			<section class="g-bd m-pbox info-box">
		    	<ul class="m-actionblock m-otab">
		    		<li>账号<span class="tip">${(profile.account)?default('')}</span></li>
		    		<li>昵称<span class="tip">${(profile.nickname)?default('')}</span></li>
		    		<li>性别<span class="tip"> <#if (profile.gender)?exists && (profile.gender)==1>女<#else>男</#if></span></li>
		    		<li>生日<span class="tip">${(profile.birthYear)?default('')}年${(profile.birthMonth)?default('')}月${(profile.birthDay)?default('')}日</span></li>
		    		<li>邮箱<span class="tip">${(profile.email)?default('')}</span></li>
		    		<li><a href="/profile/mobile">手机<i class="u-more"></i><span class="tip">${(profile.phone)?default('')}</span></a></li>
		    	</ul>
		    	<ul class="m-actionblock m-otab">
		    		<li><a href="/password/password" id="modifyPsd">修改密码<i class="u-more"></i></a></li>
		    	</ul>
	        </section>
     </div>
    
    <@template>
    
      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->
      
    </@template>
    <@jsFrame />
    <script type="text/javascript">
          $(function() {
              $('.back').on('click', function(){
                    window.location.href = "/profile/index";
              });
			
          });
    </script>
  </body>
</html>
</@compress>
</#escape>