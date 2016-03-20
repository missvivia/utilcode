/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/order/orderlist.js'],
    function(ut,v,e,Module,OrderList,AddUserWin,p) {
        var pro;

        p._$$AccountModule = NEJ.C();
        pro = p._$$AccountModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__initList();
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
        };
        pro.__initList = function(){
            if(!this.__orderList){
                this.__orderList = new OrderList({})
                    .$inject('.j-list')
            }
        }
        pro.__getNodes = function(){
            
        }
        pro.__addEvent = function(){

        }

        p._$$AccountModule._$allocate();
    });