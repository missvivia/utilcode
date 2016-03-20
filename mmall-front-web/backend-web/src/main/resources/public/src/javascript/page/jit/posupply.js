/**
 * 商家平台-jit管理——po来货物流信息
 * author：hzzhengff(hzzhengff@corp.netease.com)
 */

define([
    '{pro}widget/module.js', 
    '{pro}components/jit/posupplylist.js'
    ],
    function(Module,SizeList,p) {
		var pro;
	    p._$$SupplyModule = NEJ.C();
	    pro = p._$$SupplyModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
//            window["g_supplyList"] = [
//                                   {"id": 1,"shipOrderId":"2000259340-024","poOrderId":"200025931","shipTime":1410502727855,"expectedArrivalTime":1410502727855,"actualArrivalTime":1410502727855,"courierCompanyName":"其他","waybillNO":"903922878987","shippedQTY":5,"deliverBoxQTY":2,"deliverTotalQTY":108,"deliverSkuTypeQTY":56,"shipInfoList":[{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"}]},
//                                   {"id": 2,"shipOrderId":"2000259340-024","poOrderId":"200025931","shipTime":1410502727855,"expectedArrivalTime":1410502727855,"actualArrivalTime":1410502727855,"courierCompanyName":"其他","waybillNO":"903922878987","shippedQTY":5,"deliverBoxQTY":2,"deliverTotalQTY":108,"deliverSkuTypeQTY":56,"shipInfoList":[{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"}]},
//                                   {"id": 3,"shipOrderId":"2000259340-024","poOrderId":"200025931","shipTime":1410502727855,"expectedArrivalTime":1410502727855,"actualArrivalTime":1410502727855,"courierCompanyName":"其他","waybillNO":"903922878987","shippedQTY":5,"deliverBoxQTY":2,"deliverTotalQTY":108,"deliverSkuTypeQTY":56,"shipInfoList":[{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"}]},
//                                   {"id": 4,"shipOrderId":"2000259340-024","poOrderId":"200025931","shipTime":1410502727855,"expectedArrivalTime":1410502727855,"actualArrivalTime":1410502727855,"courierCompanyName":"其他","waybillNO":"903922878987","shippedQTY":5,"deliverBoxQTY":2,"deliverTotalQTY":108,"deliverSkuTypeQTY":56,"shipInfoList":[{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"}]},
//                                   {"id": 5,"shipOrderId":"2000259340-024","poOrderId":"200025931","shipTime":1410502727855,"expectedArrivalTime":1410502727855,"actualArrivalTime":1410502727855,"courierCompanyName":"其他","waybillNO":"903922878987","shippedQTY":5,"deliverBoxQTY":2,"deliverTotalQTY":108,"deliverSkuTypeQTY":56,"shipInfoList":[{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"}]},
//                                   {"id": 6,"shipOrderId":"2000259340-024","poOrderId":"200025931","shipTime":1410502727855,"expectedArrivalTime":1410502727855,"actualArrivalTime":1410502727855,"courierCompanyName":"其他","waybillNO":"903922878987","shippedQTY":5,"deliverBoxQTY":2,"deliverTotalQTY":108,"deliverSkuTypeQTY":56,"shipInfoList":[{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"}]},
//                                   {"id": 7,"shipOrderId":"2000259340-024","poOrderId":"200025931","shipTime":1410502727855,"expectedArrivalTime":1410502727855,"actualArrivalTime":1410502727855,"courierCompanyName":"其他","waybillNO":"903922878987","shippedQTY":5,"deliverBoxQTY":2,"deliverTotalQTY":108,"deliverSkuTypeQTY":56,"shipInfoList":[{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"}]}
//                                  ];
            this.__supplyList = window["g_supplyList"]||[];
            
            this.__initSizeComponent();
        };
        
        pro.__initSizeComponent = function(){
            if(this.__supplyList.length >0 ){
            	if(!this.__sizeList){
                    this.__sizeList = new SizeList({
                        data: {lists:this.__supplyList}
                    }).$inject("#size-list-box");
                }
            }
        };

        p._$$SupplyModule._$allocate();
 });