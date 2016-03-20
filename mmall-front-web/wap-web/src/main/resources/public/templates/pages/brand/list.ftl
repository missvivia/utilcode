<#escape x as x?html>
<@compress>
<!DOCTYPE html>
 <#include '../../wrap/3g.common.ftl'>
<html>
  <head>
    <title>品牌库</title>
    <@meta/>
    <@css/>
    <link rel="stylesheet" type="text/css" href="/src/css/page/brandList.css">
  </head>
  <body id="brandlist-netease-com">
  	<@topbar title="品牌库"></@topbar>
    <div class="g-bd g-brandlist-bd">
  <div class="m-nav m-section">
  <div class="title"><i class="u-icon u-search"></i><span>快速索引</span></div>
  	<div class="m-ct">
  	 <table class="m-table m-table-1">
      <tr class="tr">
        <td class="active td-first"><a href="#A">A B C D</a></td>
        <td class="td"><a href="#E">E F G</a></td>
        <td class="td"><a href="#H">H I J K</a></td>
        <td class="td"><a href="#L">L M N</a></td>
      </tr>
    </table>

    <table class="m-table m-table-2">
      <tr class="tr">
        <td class="td-first"><a href="#O">O P Q</a></td>
        <td class="td"><a href="#R">R S T</a></td>
        <td class="td"><a href="#U">U V W</a></td>
        <td class="td"><a href="#X">X Y Z</a></td>
        <td class="td"><a href="#other">#</a></td>
      </tr>
    </table>
  	</div>

  </div>

  <div class="m-box m-box-2" id="alpha">
    <!--section class="m-channel m-section">
  	<div class="title"><i class="u-icon u-icon-2 u-alpha u-alphaa">A</i></div>
  	<div class="m-ct">
  	<ul>
  		<li><span class="lang-en">Adiasa</span><span class="lang-ch">阿迪达斯</span></li>
  		<li><span class="lang-en">Adivon</span><span class="lang-ch">阿迪王</span></li>
  	</ul>
  	</div>

  </section>

  <section class="m-channel m-section" >
  	<div class="title"><i class="u-icon u-icon-2">B</i></div>
  	<div class="m-ct"></div>
  </section>

  <section class="m-channel m-section">
  	<div class="title"><i class="u-icon u-icon-2">C</i></div>
  	<div class="m-ct">
  		<ul>
  		<li><span class="lang-en">Christos Costarellos</span></li>
  		<li><span class="lang-en">Cath Kidston</span><span class="lang-ch">凯茜  绮丝敦</span></li>
  	</ul>
  	</div>

  </section-->
  </div>
  


</div>
   
      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->
      <#noparse>
	      <div id="template-box" style="display:none;">
		      <textarea name="jst" id="jst-template-1">
			      {for x in xlist } 
					    <section class="m-channel m-section">
					  	<div class="title"><a class="u-icon u-icon-2" name="{if x_key =="#"}other{else}${x_key}{/if}">${x_key}</a></div>
					  	<div class="m-ct">
					  	 <ul>
					  		{list x as item}
					  			<li><a href="mainbrand/story?id=${item.brandId}"><span class="lang-en">${item.brandNameEn}</span><span class="lang-ch">${item.brandNameZh}</span></a></li>
					  		{/list}
					  	</ul>
					  	</div>
					  	</section>
				{/for}
			</textarea>
	    </div>
	    </#noparse>
  
    <@ga/>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/brand/list.js"></script>
  </body>
</html>
</@compress>
</#escape>