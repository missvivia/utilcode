/**
 *
 * 倒计时组件
 * author hzzhangweidong(hzzhangweidong@corp.netease.com)
 *
 */

NEJ.define([
  'base/util',
  'base/element',
  'pro/extend/util',
  'pro/widget/layer/login/login',
  'util/chain/chainable',
  'pro/extend/request',
  'util/cache/cookie',
  'pro/widget/BaseComponent',
  'pro/page/activity/layer/lottery/startwindow',
  'pro/page/activity/layer/lottery/resultwindow',
  'pro/page/activity/layer/bubblewindow/bubblewindow',
  'pro/components/countdown/countdown',
  'text!./activitybar.html'
], function (_u,_e,_,login,$,_request,_j,BaseComponent,StartWindow,ResultWindow,BubbleWindow,CountDown,tpl) {

  var ActivityBar = BaseComponent.extend({
    template: tpl,
    config: function (data) {
      _.extend(data, {
        isLogin:_.isLogin(),
        closeFlag:false,
        lotteryNum:0,
        bubbleNum:0,
        lotteryRecords:[],
        phoneNum:"137xxxxx",
        currentEndTime:0,
        nextStartTime:0
      });
    },
    init: function (data) {

    	var that =this;
    	this.$on("inject",function(){
//    		that.initCountDown();
    		that.initUserSummary();
    	})
//    	 this.__findGame=IndexFind._$allocate({});
//
    },
    initUserSummary:function(){
    	var that=this;
    	_request('/activity/summary',{
            query:"t="+new Date(),
            onload:function(_data){
            	that.initComponents(_data);
            },
            onerror:function(_result){
            	console.log(_result);
            }
          })
    },
    initComponents:function(_data){
    	_.extend(this.data,_data.result,true);
    	this.initCountDown();
    	this.createBubbleWindow();
    	if(this.data.lotteryRecords&&this.data.lotteryRecords.length)
    	this.initUserList();
    	this.checkPaySucc();
    	this.$update();
    },
    initCountDown:function(){
    	var that=this,_data=this.data;
    	 this.__countdown=new CountDown({
    		 data:{
	    		 time:_data.currentEndTime?_data.currentEndTime:_data.nextStartTime,
	             content:'<span class="m-cd">{{HH}}</span>:<span class="m-cd">{{mm}}</span>:<span class="m-cd">{{ss}}</span>'
    		 }
    	 });
    	 this.__countdown.$inject(".m-ab-box .u-tip-2");
    },
    initUserList:function(){
      var _warp=$(".m-users-box .m-users-ul")[0];
      var top = 0;
      var id=setInterval(function(){
          top++;
          _warp.style.top="-"+top+"px";
          if(top>40){
          var _firstChild=_e._$getChildren(_warp)[0];
          if (!_firstChild){
              id = clearInterval(id);
              return;
          }
//          _e._$removeByEC(_firstChild);
          _warp.appendChild(_firstChild);
//          $(_warp)._$insert(_firstChild,"bottom");
              top=0;
          }

      },100);
    },
    onLottery:function(){
    	this.showLotteryBox();
  	  // 统计信息
   	 var userName = _.getFullUserName();
   	 _gaq.push(['_trackEvent', 'xinchonghui', 'choujiang','mainpage###'+userName]);
    },
    onLogin:function(){

    	login._$$LoginWindow._$allocate({parent:document.body})._$show();
    	 // 统计信息
   	 var userName = _.getFullUserName();
   	 _gaq.push(['_trackEvent', 'xinchonghui', 'denglu','mainpage###'+userName]);
    	return;
    },
    showLotteryBox:function(){
    	this.__lywindow=StartWindow._$allocate({boxtype:"startbox",onLotteryCallBack:this.updateLotteryNum._$bind(this)})._$show();
    },
    checkPaySucc:function(){
    	if(window.location.href.indexOf("paysucc")!=-1){
    		var _giftType=window["giftType"],_href="ps"+this.getParameterByName("tradeSerialId");
    		if(_j._$cookie(_href))return;
    		_j._$cookie(_href,_href);
    		this.__lywindow=StartWindow._$allocate({boxtype:"startbox",onLotteryCallBack:this.updateLotteryNum._$bind(this)})._$show();
    		if(_giftType&&_giftType==2){
    			this.__resultWindow=ResultWindow._$allocate({result:{type:"1288",phoneNum:this.data.phoneNum}})._$show();

    		}
    	}

    },
    getParameterByName:function(name) {
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
        return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    },
    createBubbleWindow:function(){
//    	window.paopao= {position:3,type:1,id:"4c6b50ce-31ca-4b55-8ff2-f37e80ff37ca"};
    	var _paopao=window["paopao"];
    	if(_paopao){
    		this.__bubbleLayer=BubbleWindow._$allocate({paopao:_paopao,knockCallBack:this.updateNum._$bind(this)})._$show();
    	}

    },
    onCloseIcon:function(){
    	this.data.closeFlag=true;
    	this.$update();
    },
    updateNum:function(_data){
    	this.data.bubbleNum=_data.bubbleNum;
    	this.$update();
    },

    updateLotteryNum:function(_data){
    	this.data.lotteryNum=_data.lotteryNum||0;
    	this.$update();
    }
  });
  return ActivityBar;
});

