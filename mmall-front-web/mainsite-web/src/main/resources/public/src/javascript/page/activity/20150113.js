/*
 * ------------------------------------------
 * 20150113预热活动页
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'pro/widget/util/countdown',
    'pro/components/notify/notify'
],function(_k,_y,notify,_p,_o,_f,_r,_pro){
    /**
     * 页面模块基类
     *
     * @class   _$$Module
     * @extends _$$Module
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module.prototype;

    var _tips = [
        ["想认识我么？<br/>我是一个会说话的mmall","知道么，有了我<br/>你会离新时尚更近一步","想获得mmall<br/>给你准备的新年礼物么？"],
        ["摩擦，摩擦！<br/>在光滑的mmall上摩擦","打折，打折，<br/>让专柜新品也都打折","想买专柜新品，<br/>通过测试mmall再送你时尚大礼"],
        ["痒~痒~你在给我挠痒么","拒绝尾品是一种态度","来挑战下赢取<br/>mmall送你的“换新”大礼吧"],
        ["再挠我就破了","还在网购尾货，过季款么？<br/>上mmall，专柜新品也“跪”了","先让mmall来测测你的时尚度! "],
        ["我猜你一定是个美女","如果mmall让专柜新品打折，<br/>你会爱上我吗？","先来测测你<br/>是不是我们找的时尚达人"],
        ["我QQ圆圆的，可爱吧!","知道么，有了mmall<br/>你再也不怕“新品不打折”了","甩尾迎新，mmall给你<br/>提前准备了新衣红包，不过通关才可以拿哦"]
    ];
    var _keys = [
        {id:1,name:"幸福的小事就是每天都有新衣服"},
        {id:2,name:"谁说新品不打折"},
        {id:3,name:"不做尾单承包户"},
        {id:4,name:"时尚新宠美到冒泡"},
        {id:5,name:"专柜新款不用等"}
    ];
    var _playTips = [
        "16日上023.baiwandian.cn<br/>不见不散哦！",
        "你是不是已经爱上我了：）",
        "记住你的暗号，16号别让你的礼包溜走了~",
        "16日mmall华丽变身，<br/>别忘了来戳我！",
        "爱mmall，爱新款！",
        "我发誓不再做尾品承包户！",
        "上mmall，每天10点抢专柜折扣",
        "给一个支点，<br/>mmall为你撬动专柜的价格"
    ];
    var _erroMsg = [
        "你的时尚敏感度还差一丢丢,再试一次吧！",
        "别气馁，时尚度是需要培养的！",
        "你被漫天的网络尾款迷糊了眼，继续加油",
        "无法拯救你啊，快去洗洗眼睛再来"
    ];
    /**
     * 控件初始化
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__init = function(_options){
        // init count down
        this.__initData();
        setTimeout(this.__onProgress._$bind(this),500);
        var _parent = $('count-down-box'),
            _leftTime = 1421373600000-(new Date()).getTime();
        if (!!_parent&&_leftTime>0){
            _y._$countdown(
               'count-down-box',_leftTime,
               '<span class="cd f-fl">${dd}</span><span class="cd f-fl">${HH}</span><span class="cd f-fl">${mm}</span><span class="cd f-fl" style="margin-right:0;">${ss}</span>'
            );
        }
        $('#know-1').append(_tips[this.__indexByTips][0]);
        $('#know-2').append(_tips[this.__indexByTips][1]);
        $('#know-3').append(_tips[this.__indexByTips][2]);
        $('#finger').css({bottom:-(($(window).height()-644)/2+288),left:'10px'}).show();
        $('#count-down-box').css({bottom:-(($(window).height()-644)/2+91)}).show();
        

        //事件绑定
        $('#btn-tp').click(function(){
            $('#circle-animated-1').hide().css({width:'411px',height:'411px',marginLeft:'-205px',marginTop:'-285px'});
            $('#circle-animated-2').hide().css({width:'411px',height:'411px',marginLeft:'-205px',marginTop:'-285px'});
            $('#circle-animated-3').hide().css({width:'411px',height:'411px',marginLeft:'-205px',marginTop:'-285px'});
            $('#btn-tp').fadeOut(function(){
                $('#fashion').animate({width:'0px',height:'0px',opacity:0,marginLeft:'0px',marginTop:'0px'}, 800,"swing");
            });
            $('#circle-animated-4').fadeIn(800,function(){
                    $('#circle-animated-4').animate({width:'527px',height:'527px',marginLeft:'-263px',marginTop:'-263px'}, 1300,"swing",function(){$('.resolve').fadeIn(800);});
                    $('#finger').animate({bottom:-(($(window).height()-644)/2+288),left:'10px'}, 800,"swing",function(){$(this).hide();});
                    $('#count-down-box').animate({bottom:-(($(window).height()-644)/2+91)}, 800,"swing",function(){$(this).hide();});
                });
            _gaq.push(['_trackEvent', 'warm-up', 'joinnow']);
            
        });
        $('#img-left').click(this.__checkQuestion._$bind(this,!0));
        $('#img-left').mouseenter(this.__onMouseEvent._$bind(this,1));
        $('#img-left').mouseleave(this.__onMouseEvent._$bind(this,2));
        $('#img-right').click(this.__checkQuestion._$bind(this,!1));
        $('#img-right').mouseenter(this.__onMouseEvent._$bind(this,1));
        $('#img-right').mouseleave(this.__onMouseEvent._$bind(this,2));
        $('#send-btn').click(this.__sendMsg._$bind(this));
        $('#try-btn').click(this.__tryPlay._$bind(this));
        $('#yixin-btn').click(this.__share._$bind(this,1));
        $('#weixin-btn').click(this.__share._$bind(this,2));
        $('#sina-btn').click(this.__share._$bind(this,3));
        
    };


    _pro.__initData = function(){
        this.__indexByQu = Math.floor(Math.random()*16+1);  //当前题目
        this.__indexByPro =1;  //答题进度
        this.__indexByErro = 0; //答错次数，答对重新置0
        this.__listByRight = [];  //答对的题目列表
        this.__indexByTips = parseInt(5*Math.random());
        this.__indexByKey = parseInt(4*Math.random());
        this.__indexByPlay = parseInt(7*Math.random());

    };

    _pro.__onMouseEvent = function(type,event){
        var _target = $(event.currentTarget);
        if(type==1){
            _target.find('.img-bg-h').fadeIn();
        }else{
            _target.find('.img-bg-h').fadeOut();
        }

    };

    _pro.__share = function(type,event){
        var _url,
            _text = "#调戏有理 mmall有礼# 我刚刚在调戏一个会说话的mmall，通过时尚测试免费赢了118元新宠大礼包！so easy！大家快来测测看！！";
        if(type==1){
            _url = "https://open.yixin.im/share?type=webpage&userdesc="+encodeURIComponent(_text)+"&url="+encodeURIComponent('http://023.baiwandian.cn')+'&title='+encodeURIComponent("调戏mmall 送新宠大礼包")+"&pic="+encodeURIComponent('http://023.baiwandian.cn/res/images/activity/20150113/wb.jpg');
        }else if(type==3){
            _url = "http://service.weibo.com/share/share.php?url="+encodeURIComponent('http://023.baiwandian.cn')+"&title="+encodeURIComponent(_text)+"&pic="+encodeURIComponent('http://023.baiwandian.cn/res/images/activity/20150113/wb.jpg');
        }else if(type==2){
            _url= "http://b.bshare.cn/barCode?site=weixin&type=0&url="+encodeURIComponent('http://023.baiwandian.cn')+'&title='+encodeURIComponent(_text)+"&pic="+encodeURIComponent('http://023.baiwandian.cn/res/images/activity/20150113/wb.jpg');
            $('#tk-code').attr('src',_url);
            $('#win-share').fadeIn();
            $('#win-share-con').fadeIn();
            $('#tk-close').click(function(){ $('#win-share').fadeOut();$('#win-share-con').fadeOut();$(this).unbind()});
            _gaq.push(['_trackEvent', 'warm-up', 'share','wechat']);
            return;
        }
        window.open(_url,'_blank');
        _gaq.push(['_trackEvent', 'warm-up', 'share',type==1?'yixin':(type==2?'wechat':'sinaweibo')]);
    };

    _pro.__tryPlay = function(){
         $('#circle-animated-5').fadeOut(600,function(){
            $('#circle-animated-4').animate({width:'411px',height:'411px',marginLeft:'-205px',marginTop:'-285px',opacity:0.5}, 800,"swing",function(){
                $('#circle-animated-4').fadeOut(500);
                $('#circle-animated-1').fadeIn(500);
                $('#circle-animated-2').fadeIn(500);
                $('#circle-animated-3').fadeIn(500);
                this.__initPlayCircle();

            }._$bind(this));
            $('#finger').show().animate({bottom:-(($(window).height()-644)/2)-12,left:'53px'}, 2000,"swing");
            $('#count-down-box').show().animate({bottom:-(($(window).height()-644)/2)+100}, 800,"swing");   
         }._$bind(this));
    };

    _pro.__initPlayCircle = function(){
         $('#know-4').append(_playTips[this.__indexByPlay]).fadeIn();
         $('#circle-animated-1').bind('click',function(){
             if(++this.__indexByPlay>7){
                this.__indexByPlay=0;
             }
             $('#know-4').empty().append(_playTips[this.__indexByPlay]);
         }._$bind(this));
    };

    _pro.__sendMsg = function(){
        var phoneNum = $('#ipt-phone').val();
        if(!(/^1\d{10}$/.test(phoneNum))){
            notify.notify({
                        type: "error",
                        message: "请正确输入的手机号码！"
                    })
            return;
               
        }
        // $('#send-cont').hide();
        // $('#succ-cont').fadeIn(500);
        $.post('/preheat/msgslogan',{type:this.__indexByKey+1,phoneNum:phoneNum},function(_data){
            if(_data.code == 200){
                $('#send-cont').hide();
                $('#succ-cont').fadeIn(500);
            }else{
                notify.notify({
                            type: "error",
                            message: _data.message||'网络异常'
                        })
                return;
             }
             _gaq.push(['_trackEvent', 'warm-up', 'sentnunber']);      
        }._$bind(this))
    };

    _pro.__checkQuestion = function(_flag,event){
        var _target = $(event.currentTarget);
        if(!!this.__timeout){
            clearTimeout(this.__timeout);
        }
        if(_flag){
            var _node = $('#result-yes');
            _target.parent().hide();
            _target.parent().prev().hide();
            if(this.__indexByPro ==3){ //答完三题
               _node = $('#result-pass');
            }
            _node.fadeIn(500,function(){
                $('#pro-item-'+(this.__indexByPro++)).animate({width:'28px'}, 500,"swing");
            }._$bind(this));
            this.__listByRight.push(this.__indexByQu);
            this.__getNextQueIndex();
            this.__timeout = setTimeout(function(){
                if(this.__indexByPro == 4){
                    $('#code-msg').append(_keys[this.__indexByKey].name);
                    $('#circle-animated-5').fadeIn(500,function(){$(this).find('.child').fadeIn(800)});
                    $('.resolve').fadeOut();
                    _node.fadeOut();
                    _gaq.push(['_trackEvent', 'warm-up', 'pass']);
                    return;
                }
                _node.fadeOut(function(){
                    this.__indexByErro = 0;
                    this.__changeQueImgUrl();
                    setTimeout(function(){_target.parent().fadeIn();},500);
                    _target.parent().prev().fadeIn();
                }._$bind(this));
            }._$bind(this),2500);
            
        }else{
            _target.parent().hide();
            _target.parent().prev().hide();
            $('#erro-msg').empty().append(this.__indexByErro>3?_erroMsg[3]:_erroMsg[this.__indexByErro++]);
            $('#result-no').fadeIn();
            this.__getNextQueIndex();
            this.__timeout = setTimeout(function(){
                 $('#result-no').fadeOut(function(){
                    this.__changeQueImgUrl();
                    setTimeout(function(){_target.parent().fadeIn();},500);
                    _target.parent().prev().fadeIn();
                }._$bind(this));
            }._$bind(this),2500);
        }
        

    };

     _pro.__changeQueImgUrl = function(){
        var _imgList = this.__getQueImgUrl(!0);
        $('#img-left').find('.img-bg').attr('src',_imgList[0]);
        $('#img-right').find('.img-bg').attr('src',_imgList[1]);
        $('#img-left').find('.img-bg-h').attr('src',_imgList[2]);
        $('#img-right').find('.img-bg-h').attr('src',_imgList[3]);
     };

    _pro.__getNextQueIndex = function(){
        var  len ,
             flag = false;
        if(this.__indexByQu == 16){
            this.__indexByQu = 0;
        }
        for(var i=this.__indexByQu+1;i<=16;i++){
            len = this.__listByRight.length;
            for(; len--;){
              if(this.__listByRight[len] == i){ flag=true;break;}
            }
            if(flag){
                flag = false;
                continue;
            }else{
                return this.__indexByQu=i;
            }
        }
    };

    _pro.__getQueImgUrl = function(type){
        //type 1:normal 2:hover
        var rate = this.__indexByQu>8?2:1,
            index = (this.__indexByQu%8)==0?8:(this.__indexByQu%8);
        if(type){
            return [
                    '/res/images/activity/20150113/'+index+'/'+rate+'/'+rate+'_01.png',
                    '/res/images/activity/20150113/'+index+'/'+rate+'/'+rate+'_02.png',
                    '/res/images/activity/20150113/'+index+'/'+rate+'/'+rate+'_h_01.png',
                    '/res/images/activity/20150113/'+index+'/'+rate+'/'+rate+'_h_02.png'
                   ];
        }else{
            return [
                    '/res/images/activity/20150113/'+index+'/'+rate+'/'+rate+'_h_01.png',
                    '/res/images/activity/20150113/'+index+'/'+rate+'/'+rate+'_h_02.png'
                   ];
        }
     };



    _pro.__onProgress = function(_timeout){
        var timeout = timeout || 4800, 
        step = timeout / 60, 
        timeoutid,
        move = 0.01,
        that = this,
        _node = $('#num').get(0);
        function next(){
          move += 0.05
          if(move > 1) {
            move = 1;
           } 
          _node.innerHTML = parseInt(100*move) + '%'; 
          if(move < 1 ) timeoutid = setTimeout(next, step);
          else end();
        }
        function end(){
          clearTimeout(timeoutid);
          that.__onCircleToBig();

        }
        timeoutid = setTimeout(next,step);
    };

    _pro.__onCircleToBig = function(){
        $('#num').fadeOut();
        $('#circle-animated-1').animate({width:'411px',height:'411px',marginLeft:'-205px',marginTop:'-205px'}, 1300,"easeOutElastic");
        $('#circle-animated-2').animate({width:'411px',height:'411px',marginLeft:'-205px',marginTop:'-205px'}, 1300,"easeOutElastic");
        $('#circle-animated-3').animate({width:'411px',height:'411px',marginLeft:'-205px',marginTop:'-205px'}, 1300,"easeOutElastic",this.__initLayout._$bind(this));

        $('#circle-animated-1').animate({width:'411px',height:'411px',marginLeft:'-205px',marginTop:'-285px'}, 800,"swing");
        $('#circle-animated-2').animate({width:'411px',height:'411px',marginLeft:'-205px',marginTop:'-285px'}, 800,"swing");
        $('#circle-animated-3').animate({width:'411px',height:'411px',marginLeft:'-205px',marginTop:'-285px'}, 800,"swing");
    };

    _pro.__initLayout = function(){
        this.__count = 2;
        $('#know-1').fadeIn(1000);
        $('#center-bg').fadeIn(600);
        $('.sc-warp').fadeIn(700);
        $('.shade').fadeIn(700);
        $('#finger').animate({bottom:-(($(window).height()-644)/2)-12,left:'53px'}, 2000,"swing");
        $('#count-down-box').animate({bottom:-(($(window).height()-644)/2)+100}, 800,"swing");
        $('#circle-animated-1').bind('click',function(){
            if(this.__count ==2){
                $('#know-1').hide();
                $('#know-2').fadeIn(1000);
                $('#finger').animate({left:'73px',bottom:-(($(window).height()-644)/2)}, 600,"swing", this.__aniBeforeClick._$bind(this,1));
                $('#finger').animate({left:'53px',bottom:-(($(window).height()-644)/2)-12}, 600,"swing");
            }else if(this.__count ==3){
                $('#know-2').hide();
                $('#know-3').fadeIn(1000);
                $('#finger').animate({left:'73px',bottom:-(($(window).height()-644)/2)}, 600,"swing", this.__aniBeforeClick._$bind(this,2));
                 $('#finger').animate({left:'53px',bottom:-(($(window).height()-644)/2)-12}, 600,"swing");
            }else{
                $('#know-3').hide();
                $('#circle-animated-1').unbind(); 
                $('#fashion').animate({width:'529px',height:'280px',opacity:1,marginLeft:'-264px',marginTop:'-188px'}, 1300,"easeOutBounce",function(){$('#btn-tp').show();});
            }
            this.__count++;
        }._$bind(this));
    };

    _pro.__aniBeforeClick = function(rate){
        var _w = (411+rate*40)+'px',
            _l = '-'+(205+rate*20)+'px',
            _t = '-'+(205+rate*20+80)+'px';
        $('#circle-animated-1').animate({width:_w,height:_w,marginLeft:_l,marginTop:_t}, 800,"swing");
        $('#circle-animated-2').animate({width:_w,height:_w,marginLeft:_l,marginTop:_t}, 800,"swing");
        $('#circle-animated-3').animate({width:_w,height:_w,marginLeft:_l,marginTop:_t}, 800,"swing");
        
    };

    /**
    * 发送统计连接
    */
    _pro.__sendLog=function(_url){
        var _img=new Image();
        if(_url.indexOf("?")!=-1)
            _url+="&time="+new Date().getTime();
        else
            _url+="?time="+new Date().getTime();
        _img.src=_url;
    };


    // init page
    new _p._$$Module();
});