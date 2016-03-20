/**
 *
 * 倒计时组件
 * author hzzhangweidong(hzzhangweidong@corp.netease.com)
 *
 */

NEJ.define([
  'base/util',
  'pro/extend/util',
  'pro/widget/layer/login/login',
  'pro/extend/request',
  'pro/widget/BaseComponent',
  'pro/extend/config',
  'text!./lotteryresult.html',
  'text!./m-3d-login.html'
], function (_u,_,login,_request,BaseComponent, config,tpl,_m3dl) {

  var LotteryResult = BaseComponent.extend({
    template: tpl,
    config: function (data) {
      _.extend(data, {
        type:400,
        sharetxt:"我刚刚在新宠会中了一个大奖，快来看看吧!",
        phoneNum:"188xxxxx",
        m3dl:_m3dl
      });
    },
    init: function (data) {
    	var _ct=this.data.sharetxt;
    	if(data.type =="cp1"){
    		_ct="#mmall新宠 好礼我有#我刚在mmall上找到了最稀有的彩虹mmall，获得了一次免单特权！真是人品大爆发！上mmall，更多品牌专柜同步新款衣服，整点还可抢免单，赶紧来逛逛吧 http://023.baiwandian.cn/";
    	}
    	else if(data.type == "1288"){
    		_ct="#mmall新宠 好礼我有#我刚在mmall上买了好多新衣服，还获得了时尚的网易登机箱。上mmall，更多品牌专柜同步新款折扣，快来抢吧！ http://023.baiwandian.cn/";
    	}
    	else if(data.type == "105"){
    		_ct="#mmall新宠 好礼我有#我刚在mmall上抽到了“特等奖：巴厘岛双人游”真是碉堡了！上mmall，很多品牌专柜同步新款折扣，下单还可抽奖，快来抢吧！ http://023.baiwandian.cn/";
    	}
    	else if(data.type == "101"){
    		_ct="#mmall新宠 好礼我有#我刚在mmall上抽到了“一等奖：美图KISS自拍神器”真是碉堡了！上mmall，很多品牌专柜同步新款折扣，下单还可抽奖，快来抢吧！ http://023.baiwandian.cn/";
    	}
    	else if(data.type == "102"){
    		_ct="#mmall新宠 好礼我有#我刚在mmall上抽到了“二等奖：美国clarisonic洗脸神器”真是碉堡了！上mmall，很多品牌专柜同步新款折扣，下单还可抽奖，快来抢吧！ http://023.baiwandian.cn/";
    	}
    	else if(data.type == "103"){
    		_ct="#mmall新宠 好礼我有#我刚在mmall上抽到了“三等奖：韩国soc超声波补水神器”真是碉堡了！上mmall，很多品牌专柜同步新款折扣，下单还可抽奖，快来抢吧！ http://023.baiwandian.cn/";
    	}
    		
       this.data.weibourl="http://service.weibo.com/share/share.php?url="+encodeURIComponent(config.DOMAIN_URL)+"&title="+encodeURIComponent(_ct)+"&pic=http://ystore.nos.netease.com/69f25389d44685ad926b5ec8b148ae21.jpg";
       this.$update();
    },
    on104close:function(){
    	var userName = _.getFullUserName();
    	 _gaq.push(['_trackEvent', 'xinchonghui', 'duihuan','activitypage###'+userName]);
    	this.close();
    },
    close:function(){
    	this.$emit("close");
    }
  });
  return LotteryResult;
});

