<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<#include "/wrap/3g.common.ftl" />
<html>
  <head>
    <title>登录页</title>
    <@meta/>
    <@less />
  </head>
  <body>
    <div class="wrap">
	    <header class="headerbox">
		     <div class="m-topnav">
		        <a class="curp" href="/">
				    <i class="f-fl u-menu"></i>
			    </a>
			    <span class="tt">登录页</span>
		  	 </div>
		    <div class="u-top" id="gotop"></div>
		</header>
	    <div class="m-login" id="m-login-con">
	      <form action="http://denglu.baiwandian.cn/login" method="post" autocomplete="off" id="loginform-p">
	        <div class="form-group form-group-uname j-flag">
	          <div class="login-it">
	            <div class="ln ln-first f-cb">
	              <div class="ipt-con">
	                <div class="ipt-wrap">
	                  <input type="text" class="ipt user" id="username" name="username" autocomplete="off" placeholder="用户名"/>
	                </div>
	                <a class="clear"></a>
	              </div>
	            </div>
	            <div class="ln f-cb">
	              <div class="ipt-con">
	                <div class="ipt-wrap">
	                  <input type="password" class="ipt psd" id="password" autocomplete="off" placeholder="密码" />
	                </div>
	                <i class="show-psd"></i>
	                <!--<a class="clear"></a>-->
	              </div>
	            </div>
	          </div>
	          <ul class="txtlist sug" style="visibility:hidden;">
	              <li class="unit js-selected">qajjc@163.com</li>
	              <li class="unit">qajjc@126.com</li>
	          </ul>
	        </div>
	        <div class="form-group login-psd">
	            <a class="re-psd" href="javascript:void(0);"><i class="u-icon"></i><span class="psd-tip">记住密码</span></a>
	            <a target="_self" href="/password/reset/first/" class="psd-tip forgot-psd" id="psd-forget">忘记密码？</a>
	        </div>
	        <div class="form-group">
	            <a class="btn j-flag commit" id="commit" tabindex="3">登&nbsp;录</a>
	        </div>
	        
	        <input type="hidden" name="password"/>
	        <input type="hidden" name="redirectError" value="http://m.023.baiwandian.cn/login"/>
            <input type="hidden" name="redirectURL" value=""/>
            <input type="hidden" name="type" value="4"/>
	      </form>
	    </div>
	</div>
    
    
    <@template>
    
      <#-- Template Content Here -->
      <#-- Remove @template if no templates -->
      
    </@template>
    <@jsFrame />
    <script src="/src/frame/crypto-js.js"></script>
    <script src="/src/frame/pad-pkcs7.js"></script>
    <script src="/src/frame/md5.js"></script>
    <script type="text/javascript">
          $(function() {
              var username = $("#username"),
                  password = $("#password"),
                  commit = $("#commit"),
                  remember = $(".re-psd"),
                  redirectURL = "http://m.023.baiwandian.cn",
                  url = window.location.href;

              if (url.indexOf("redirectURL") > 0) {
                  redirectURL = decodeURIComponent(url.substring(url.indexOf("redirectURL") + 12, url.length));
              }
              
              $("input[name='redirectURL']").val(redirectURL);
              
              username.val(getCookie("wap-username"));
        	  password.val(getCookie("wap-password"));
              
              remember.click(function(){
                   $(this).toggleClass("fo-psd");
              });
              
              $(".show-psd").click(function(){
                   if($(this).hasClass("hide-psd")){
                        $(this).removeClass("hide-psd");
                        password.attr("type","password");
                   }else{
                        $(this).addClass("hide-psd");
                        password.attr("type","text");
                   }
              });
              
              /**
				 * 加密数据
				 * @param {type} data 待加密的字符串
				 * @param {type} keyStr 秘钥
				 * @param {type} ivStr 向量
				 * @returns {unresolved} 加密后的数据
			 */
			 var aesEncrypt = function(data, keyStr) {
				var sendData = CryptoJS.enc.Utf8.parse(data);
		        var key = CryptoJS.enc.Hex.parse(keyStr);
		        var encrypted = CryptoJS.AES.encrypt(sendData, key, {
		        	mode : CryptoJS.mode.ECB,
		        	padding : CryptoJS.pad.Pkcs7
		        });
				return encrypted.ciphertext.toString(CryptoJS.enc.Hex);
			 };
			
              function check(){
                  if($.trim(username.val()) === ''){
                      $.message.alert("提示","请输入用户名");
                      return false;
		          }else if($.trim(password.val()) === ''){
		              $.message.alert("提示","请输入密码");
		              return false;
		          }
		          
		          var key = (CryptoJS.MD5(username.val()+"|4").toString()).toLowerCase(),
                      psd = password.val();
                      
                  psd = (aesEncrypt(psd, key)).toUpperCase();
                  $("input[name='password']").val(psd);
                  delCookie("err", "true");
                  if(remember.hasClass("fo-psd")){
                      delCookie("wap-username", username.val());
                      delCookie("wap-password", password.val());
                  }else{
                      setCookie("wap-username", username.val());
                      setCookie("wap-password", password.val());
                  }
		          $("#loginform-p").submit();
              }
                  
              commit.click(function(){
                  check();
              });
              
              // 获取cookie错误信息
	          function getCookieInfo() {
	               var errInfo = getCookie("XYLERR");
	               if (errInfo != "" && getCookie("err") == "") {
	                   $.message.alert("提示", errInfo);
	                   setCookie("err", "true");
	               }
	          }
	          getCookieInfo();
	          
	          //获取cookie
	          function getCookie(cname){
					var name = cname + "=";
					var ca = document.cookie.split(';');
					for(var i=0; i<ca.length; i++) {
						var c = ca[i];
						while (c.charAt(0)==' ') c = c.substring(1);
						if (c.indexOf(name) != -1) return decodeURIComponent(c.substring(name.length, c.length));
					}
					return "";
			  };
			  
			  //设置cookie
			  function setCookie(name,value) {
					var Days = 30;
					var exp = new Date();
					exp.setTime(exp.getTime() + Days*24*60*60*1000);
					document.cookie = name + "=" + encodeURIComponent(value) + ";expires=" + exp.toGMTString();
			  };
			  
			  //删除cookie
			  function delCookie(name,value){
					var date = new Date();
					date.setTime(date.getTime() - 10000);
					document.cookie = name + "=" + encodeURIComponent (value) + ";expires=" + date.toGMTString();
			  };
			  
          });
    </script>
  </body>
</html>
</@compress>
</#escape>