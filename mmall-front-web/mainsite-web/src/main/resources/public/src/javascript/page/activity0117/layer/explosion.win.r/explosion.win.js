/**
 * 发货确认
 * author yqj(yuqijun@corp.netease.com)
 */


define([
  'text!./explosion.win.html',
  '../activity.win.r.js'
  ], function(tpl,ActivityWin){

  var ExplosionWin = ActivityWin.extend({
    
    content:tpl,
    close: function(){
    	this.$emit('close');
	   this.destroy();
	},

    confirm: function(){
    	this.$emit('confirm');
		this.destroy();
	}

  });


  return ExplosionWin;


})