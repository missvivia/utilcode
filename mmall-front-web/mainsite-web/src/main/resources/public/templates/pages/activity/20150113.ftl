<#escape x as x?html>
<@compress>
<!DOCTYPE html>
<html>
  <head>
    <#include "../../wrap/common.ftl">
    <@title content="调戏mmall 送新宠大礼包"/>
    <meta charset="utf-8"/>
    <@css/>
    <!-- @STYLE -->
    <style>
    body{overflow-y:hidden;height:100%;background-color:#44003b;}
    .fashion .btn, .circle-4 .progress, .circle-4 .progress .item, .circle-5 .share .item, .circle-5 .ct .try-btn{background:url(/res/images/activity/20150113/sprite.png) -9999px -9999px no-repeat;}
    .m-top{height:50%;background-color:#0e0c3e;position:relative;}
    .p-logo{position:absolute;width:118px;height:30px;left:50px;top:50px;background:url(/res/images/activity/20150113/p-logo.png) no-repeat;}
    .m-bottom{height:50%;background-color:#44003b;}
    .m-center{position:absolute;top:50%;left:0;margin-top:-322px;width:100%;height:644px;background:url(/res/images/activity/20150113/center-bg.png) repeat-x;}
    .m-center .center-bg{position:absolute;left:0;top:0;width:100%;height:644px;background:url(/res/images/activity/20150113/cont-bg.png) center top no-repeat;}
    .m-center .cont{position:absolute;left:0;top:0;width:100%;height:644px;}
    .m-center .cont-warp{position:relative;width:1090px;margin:0 auto;height:644px;}
    .m-center .shade{position:absolute;top:50%;left:50%;margin-left:-404px;margin-top:-401px;width:809px;height:803px;background:url(/res/images/activity/20150113/shade2.png) no-repeat;opacity:0.58;}
    .m-center .sc-warp{position:absolute;top:0;left:0;width:100%;height:100%;}
    .sc-warp .sc-1{position:absolute;top:370px;left:932px;width:61px;height:61px;background:url(/res/images/activity/20150113/sc/1.png) no-repeat;}
    .sc-warp .sc-2{position:absolute;top:28px;left:981px;width:74px;height:74px;background:url(/res/images/activity/20150113/sc/2.png) no-repeat;}
    .sc-warp .sc-3{position:absolute;top:217px;left:833px;width:49px;height:49px;background:url(/res/images/activity/20150113/sc/3.png) no-repeat;}
    .sc-warp .sc-4{position:absolute;top:152px;left:37px;width:75px;height:75px;background:url(/res/images/activity/20150113/sc/4.png) no-repeat;}
    .sc-warp .sc-5{position:absolute;top:330px;left:166px;width:50px;height:50px;background:url(/res/images/activity/20150113/sc/5.png) no-repeat;}
    .sc-warp .sc-6{position:absolute;top:18px;left:210px;width:61px;height:61px;background:url(/res/images/activity/20150113/sc/6.png) no-repeat;}
    .sc-warp .sc-7{position:absolute;top:505px;left:802px;width:46px;height:46px;background:url(/res/images/activity/20150113/sc/7.png) no-repeat;}
    .sc-warp .sc-8{position:absolute;top:500px;left:50px;width:35px;height:35px;background:url(/res/images/activity/20150113/sc/8.png) no-repeat;}
    .m-center .finger{position:absolute;bottom:0;left:118px;width:268px;height:288px;background:url(/res/images/activity/20150113/finger-4.png) no-repeat;}
    .m-center .countdown{position:absolute;bottom:30px;left:377px;width:356px;height:41px;padding-top:50px;background:url(/res/images/activity/20150113/countdown.png) no-repeat;}
    .countdown .cd{font-size:26px;color:#fff;letter-spacing:8px;text-align:left;width:44px;margin-right:40px;padding-left:7px;}
    .m-center .circle{position:absolute;top:50%;left:50%;}
    .circle .wd{font-size:22px;color:#fff;line-height:40px;text-align:center;margin-top:-20px;left:0;width:100%;top:50%;}
    .m-center .circle-1{cursor:pointer;margin-left:-63px;margin-top:-63px;width:126px;height:126px;opacity:0.65;}
    .m-center .circle-2{margin-left:-63px;margin-top:-63px;width:126px;height:126px;}
    .m-center .circle-3{margin-left:-63px;margin-top:-63px;width:126px;height:126px;}
    .m-center .circle-4{margin-left:-245px;margin-top:-325px;width:491px;height:491px;}
    .circle-4 .resolve{position:absolute;width:100%;height:100%;left:0;top:0;}
    .circle-4 .question{margin:66px auto 10px;width:304px;height:92px;background:url(/res/images/activity/20150113/question.png) no-repeat;}
    .circle-4 .pic{margin:0 auto;width:485px;height:255px;}
    .circle-4 .pic > p{padding-top:90px;font-size:20px;color:#fa2cd0;text-align:center;}
    .circle-4 .progress-cont{position:absolute;bottom:36px;left:50%;width:158px;margin-left:-79px;}
    .circle-4 .progress{margin:30px auto 0px;width:158px;height:30px;background-position:0 -344px;}
    .circle-4 .progress .item{width:0px;height:25px;background-position:-184px -344px;}
    .circle-4 .progress .item-1{left:1px;top:2px;}
    .circle-4 .progress .item-2{left:64px;top:3px;}
    .circle-4 .progress .item-3{left:129px;top:4px;}
    .circle-4 .result{position:absolute;width:100%;height:100%;left:0;top:0;}
    .circle-4 .result .msg{position:absolute;font-size:20px;color:#fa2cd0;text-align:center;width:100%;top:356px;}
    .circle-4 .result .msg-1{color:#999;}
    .circle-4 .pimg{position:absolute;left:0;top:0;}
    .circle-4 .img{dispaly:block;height:255px;}
    .circle-4 .img-1{width:242px;}
    .circle-4 .img-1{width:243px;}
    .m-center .circle-5{margin-left:-263px;margin-top:-263px;width:527px;height:527px;background:url(/res/images/activity/20150113/circle-s.png) no-repeat;}
    .circle-5 .ct{height:210px;padding-top:170px;text-align:center;}
    .circle-5 .ct .msg-succ{font-size:18px;color:#ea28c2;padding:45px 0 37px;}
    .circle-5 .ct .biger{font-size:26px;}
    .circle-5 .ct .try-btn{display:block;width:168px;height:41px;background-position:0 -110px;margin:0 auto;}
    .circle-5 .ct .try-btn:hover{background-position:0 -160px;}
    .circle-5 .ct .msg{font-size:16px;color:#585757;line-height:24px;padding-bottom:10px;}
    .circle-5 .ct .code{font-size:22px;color:#9144ae;line-height:30px;width:464px;margin:0 auto 40px;}
    .circle-5 .ct .ipt-warp{margin-left:70px;}
    .circle-5 .ct .u-ipt-1{width:276px;padding:9px;border-color:#c3bdd1;-webkit-box-shadow: 0 0 4px #eee inset;box-shadow: 0 0 4px #eee inset;}
    .circle-5 .ct .u-ipt-1 .ipt{width:276px;padding:0;height:20px;border:0;line-height:20px;font-size:14px;color:#333;}
    .circle-5 .ct .tip{color:#333;padding-top:10px;}
    .circle-5 .ct .highline{color:#f559d5;}
    .circle-5 .ct .pre-btn,.circle-5 .ct .pre-btn:hover{color:#666;background-color:#333;position:absolute;top:0;right:0;}
    .circle-5 .share{margin-left:78px;}
    .circle-5 .share .tt{font-size:18px;color:#333;line-height:47px;}
    .circle-5 .share .item{width:47px;height:47px;margin-right:25px;}
    .circle-5 .share .item-1{background-position:0 0}
    .circle-5 .share .item-1:hover{background-position:0 -57px;}
    .circle-5 .share .item-2{background-position:-73px 0}
    .circle-5 .share .item-2:hover{background-position:-73px -57px;}
    .circle-5 .share .item-3{background-position:-143px 0}
    .circle-5 .share .item-3:hover{background-position:-143px -57px;}
    .win-share{width:100%;height:100%;position:absolute;left:0;top:0;background-color:#000;opacity:0.3;filter:alpha(opacity=30)}
    .tk{width:650px;height:372px;position:absolute;left:50%;top:50%;margin-left:-325px;margin-top:-186px;background:url(/res/images/activity/20150113/win-tk.png) no-repeat;}
    .tk .close{right:15px;top:15px;width:22px;height:23px;position:absolute;background:url(/res/images/activity/20150113/tk-close.png) no-repeat;}
    .tk .code{left:252px;top:197px;width:132px;height:132px;position:absolute;}
    .c-dn{display:none;}
    @-webkit-keyframes rotaten1{
      from{-webkit-transform:rotate(0deg);}
      to{-webkit-transform:rotate(360deg);}
    }
    @-moz-keyframes rotaten1{
      from{-moz-transform:rotate(0deg);}
      to{-moz-transform:rotate(360deg);}
    }
    @-ms-keyframes rotaten1{
      from{-ms-transform:rotate(0deg);}
      to{-ms-transform:rotate(360deg);}
    }
    @-o-keyframes rotaten1{
      from{-o-transform:rotate(0deg);}
      to{-o-transform:rotate(360deg);}
    }
     @keyframes rotaten1{
      from{transform:rotate(0deg);}
      to{transform:rotate(360deg);}
    }
     @-webkit-keyframes rotaten2{
      from{-webkit-transform:rotate(0deg);}
      to{-webkit-transform:rotate(-360deg);}
    }
    @-moz-keyframes rotaten2{
      from{-moz-transform:rotate(0deg);}
      to{-moz-transform:rotate(-360deg);}
    }
    @-ms-keyframes rotaten2{
      from{-ms-transform:rotate(0deg);}
      to{-ms-transform:rotate(-360deg);}
    }
    @-o-keyframes rotaten2{
      from{-o-transform:rotate(0deg);}
      to{-o-transform:rotate(-360deg);}
    }
     @keyframes rotaten2{
      from{transform:rotate(0deg);}
      to{transform:rotate(-360deg);}
    }
    .animated-rot1{-webkit-animation: rotaten1 5.3s linear infinite;-moz-animation: rotaten1 5.3s linear infinite;-ms-animation: rotaten1 5.3s linear infinite;-o-animation: rotaten1 5.3s linear infinite;animation: rotaten1 5.3s linear infinite;}
    .animated-rot2{-webkit-animation: rotaten2 6s linear infinite;-moz-animation: rotaten2 6s linear infinite;-ms-animation: rotaten2 6s linear infinite;-o-animation: rotaten2 6s linear infinite;animation: rotaten2 6s linear infinite;}
    .m-center .fashion{width:0px;height:0;opacity:0;position:absolute;top:50%;left:50%;margin-left:0px;margin-top:0px;}
    .fashion .logo{width:529px;height:118px;background:url(/res/images/activity/20150113/fashion.png) no-repeat;}
    .fashion .msg{text-align:center;font-size:36px;color:#fff;}
    .fashion .num{color:#f4f963;}
    .fashion .btn{display:block;width:168px;height:60px;background-position:0 -210px;position:absolute;left:180px;top:210px;}
    .fashion .btn:hover{background-position:0 -278px;}
    .m-center .c-dn{display:none;}
  </style>
  </head>
  <body>
    <div class="m-top"></div>
    <div class="m-bottom"></div>
    <div class="m-center">
      <div class="f-pr" style="width:100%;height:100%;">
        <div class="center-bg" id="center-bg"></div>
        <div class="cont">
          <div class="cont-warp" >
            <div class="shade c-dn"></div>
            <div class="sc-warp c-dn" >
              <div class="f-pr" id="scene" data-invert-x="false" data-invert-y="false">
                <div class="layer" data-depth="0.20"><div class="sc-1"></div></div>
                <div class="layer" data-depth="0.30"><div class="sc-2"></div></div>
                <div class="layer" data-depth="0.10"><div class="sc-3"></div></div>
                <div class="layer" data-depth="0.20"><div class="sc-4"></div></div>
                <div class="layer" data-depth="0.20"><div class="sc-5"></div></div>
                <div class="layer" data-depth="0.40"><div class="sc-6"></div></div>
                <div class="layer" data-depth="0.10"><div class="sc-7"></div></div>
                <div class="layer" data-depth="0.20"><div class="sc-8"></div></div>
              </div>
            </div>
            <div class="finger c-dn" id="finger"></div>
            <div class="countdown f-cb c-dn" id="count-down-box">
            </div>
            <div class=" circle circle-4 c-dn" id="circle-animated-4">
              <div class="f-pr" style="width:100%;height:100%;">
                <img src="/res/images/activity/20150113/circle.png"  width="100%" height="100%">
                <div class="progress-cont">
                  <div class="progress f-pr">
                    <div class="item item-1 f-pa" id="pro-item-1"></div>
                    <div class="item item-2 f-pa" id="pro-item-2"></div>
                    <div class="item item-3 f-pa" id="pro-item-3"></div>
                  </div>
                </div>
                <div class="resolve c-dn">
                  <div class="question"></div>
                  <div class="pic f-cb">
                    <a class="img img-1 f-fl f-pr" id="img-left">
                      <img class="pimg img-bg" src="/res/images/activity/20150113/1/1/1_01.png"  width="242" height="255">
                      <img class="pimg img-bg-h c-dn" src="/res/images/activity/20150113/1/1/1_h_01.png"  width="242" height="255">
                    </a>
                    <a class="img img-2 f-fl f-pr" id="img-right">
                      <img class="pimg img-bg" src="/res/images/activity/20150113/1/1/1_02.png"  width="243" height="255">
                      <img class="pimg img-bg-h c-dn" src="/res/images/activity/20150113/1/1/1_h_02.png"  width="243" height="255">
                    </a>
                  </div>
                </div>
                <div class="result yes c-dn" id="result-yes">
                  <div class="f-pr" style="width:100%;height:100%;">
                    <img src="/res/images/activity/20150113/yes.png"  width="100%" height="100%">
                    <div class="msg">太棒了！相信再来一题肯定难不倒你</div>
                  </div>
                </div>
                <div class="result no c-dn" id="result-no">
                  <div class="f-pr" style="width:100%;height:100%;">
                    <img src="/res/images/activity/20150113/no.png"  width="100%" height="100%">
                    <div class="msg msg-1" id="erro-msg"></div>
                  </div>
                </div>
                <div class="result pass c-dn" id="result-pass">
                  <div class="f-pr" style="width:100%;height:100%;">
                    <img src="/res/images/activity/20150113/pass.png"  width="100%" height="100%">
                  </div>
                </div>
              </div>
            </div>
            <div class=" circle circle-5 c-dn" id="circle-animated-5">
              <div class="child ct c-dn">
                <div class="succ c-dn" id="succ-cont">
                  <p class="msg-succ">信息已发送到您的手机，<span class="biger">1月16号</span>我们不见不散哦~</p>
                  <a href="javascript:void(0);" class="try-btn" id="try-btn"></a>
                </div>
                <div class="send" id="send-cont">
                  <p class="msg">记住下面的暗语，当我破碎的那天用这句话找我换大礼包吧！</p>
                  <p class="code">暗语：“<span id="code-msg"></span>”</p>
                  <div class="ipt-warp f-cb f-pr">
                    <div class="u-ipt u-ipt-1 f-fl"><input type="text" class="ipt" placeholder="输入手机号码" id="ipt-phone"></div>
                    <a href="javascript:void(0);" class="u-btn u-btn-1 f-fl" id="send-btn">发送</a>
                    <!--<span class="u-btn u-btn-1 pre-btn c-dn" id="pre-send"></span>-->
                  </div>
                  <p class="tip">输入手机号码后，<span class="highline">我们将把暗语发送到您的手机并通知您抽奖</span></p>
                </div>
              </div>
              <div class="child share f-cb c-dn">
                <span class="tt f-fl">召唤伙伴来抢礼包：</span>
                <a href="javascript:void(0);" class="item item-1 f-fl"  id="yixin-btn"></a>
                <a href="javascript:void(0);" class="item item-2 f-fl" id="weixin-btn"></a>
                <a href="javascript:void(0);" class="item item-3 f-fl" id="sina-btn"></a>
              </div>
            </div>
            <div class="circle circle-3 animated-rot1" id="circle-animated-3">
              <img src="/res/images/activity/20150113/circle-r3-1.png"  width="100%" height="100%">
            </div>
            <div class="circle circle-2 animated-rot2" id="circle-animated-2">
              <img src="/res/images/activity/20150113/circle-r2-1.png"  width="100%" height="100%">
            </div>
            <div class="circle circle-1" id="circle-animated-1">
              <div class="f-pr" style="width:100%;height:100%;">
                <img src="/res/images/activity/20150113/circle-r1.png"  width="100%" height="100%">
                <p class="wd f-pa c-dn" id="know-1"></p>
                <p class="wd f-pa c-dn" id="know-2"></p>
                <p class="wd f-pa c-dn" id="know-3"></p>
                <p class="wd f-pa c-dn" id="know-4"></p>
                <p class="wd f-pa" id="num"></p>
              </div>
            </div>
            <div class="fashion" id="fashion">
              <!--<div class="msg">赢取<span class="num">118</span>元新宠礼遇</div>-->
              <div class="f-pr" style="width:100%;height:100%;">
                <img src="/res/images/activity/20150113/logo-2.png" width="100%" height="100%"/>
                <a href="javascript:void(0);" class="btn c-dn" id="btn-tp"></a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="p-logo"></div>
    <div class="win-share c-dn" id="win-share"></div>
    <div class="tk c-dn" id="win-share-con">
      <div class="f-pr">
        <div class="close" id="tk-close"></div>
        <img class="code" src="" id="tk-code"/>
      </div>
    </div>
    

    <!-- @noparse -->
    <script type="text/javascript">var _gaq = _gaq || [];_gaq.push(['_setAccount', 'UA1415850288457'],['_setLocalGifPath', '/UA1415850288457/__utm.gif'],['_setLocalServerMode']);_gaq.push(['_addOrganic','baidu','word']);_gaq.push(['_addOrganic','soso','w']);_gaq.push(['_addOrganic','youdao','q']);_gaq.push(['_addOrganic','sogou','query']);_gaq.push(['_addOrganic','so.360.cn','q']);_gaq.push(['_trackPageview']);_gaq.push(['trackPageLoadTime']);(function() {var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;ga.src = 'http://wr.da.netease.com/ga.js';var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);})();
    </script>
    <script src="/src/javascript/lib/jquery/dist/jquery.min.js"></script>
    <script src="/src/javascript/page/activity/jquery.easing.1.3.js"></script>
    <script src="/src/javascript/page/activity/parallax.min.js"></script>
    <script type="text/javascript">
      var scene = document.getElementById('scene');
      var parallax = new Parallax(scene);
    </script>
    <!-- /@noparse -->

    <#noparse>
    <!-- @SCRIPT -->
    </#noparse>
    <script src="${jslib}define.js?${jscnf}"></script>
    <script src="${jspro}page/activity/20150113.js"></script>
  </body>
</html>
</@compress>
</#escape>