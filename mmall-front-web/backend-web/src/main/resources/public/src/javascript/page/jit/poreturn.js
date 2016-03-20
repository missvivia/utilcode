/**
 * 商家平台-jit管理——po退供物流信息
 * author：hzzhengff(hzzhengff@corp.netease.com)
 */

define([
    '{pro}widget/module.js', 
    '{pro}components/jit/poreturnlist.js'
    ],
    function(Module,SizeList,p) {
		var pro;
	    p._$$ReturnModule = NEJ.C();
	    pro = p._$$ReturnModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
//            window["g_returnList"] = [
//	                                   {"id": 1,"serialNumber":"ff80808146222d5c014640b016ee00e2","returnStatus":"货物已交华南志鸿物流(退供)承运商，预计06-19到贵司仓库，请安排收货","returnFormNo":"CDRT200021500101","poOrderId":"200025931","returnQuantity":12,"objectSize":1.6200,"objectWeight":0.0000,"brandName":"秋水伊人","shipAdd":"西南仓","receiveAdd":"浙江省杭州市萧山区桥南区块鸿兴路374号电商一楼QS销退组","carrierName":"西南百世物流","deliveryNO":"BTH140612077597","shipInfoList":[{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"}]},
//	                                   {"id": 2,"serialNumber":"ff80808146222d5c014640b016ee00e2","returnStatus":"货物已交华南志鸿物流(退供)承运商，预计06-19到贵司仓库，请安排收货","returnFormNo":"CDRT200021500101","poOrderId":"200025931","returnQuantity":12,"objectSize":1.6200,"objectWeight":0.0000,"brandName":"秋水伊人","shipAdd":"西南仓","receiveAdd":"浙江省杭州市萧山区桥南区块鸿兴路374号电商一楼QS销退组","carrierName":"西南百世物流","deliveryNO":"BTH140612077597","shipInfoList":[{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"}]},
//	                                   {"id": 3,"serialNumber":"ff80808146222d5c014640b016ee00e2","returnStatus":"货物已交华南志鸿物流(退供)承运商，预计06-19到贵司仓库，请安排收货","returnFormNo":"CDRT200021500101","poOrderId":"200025931","returnQuantity":12,"objectSize":1.6200,"objectWeight":0.0000,"brandName":"秋水伊人","shipAdd":"西南仓","receiveAdd":"浙江省杭州市萧山区桥南区块鸿兴路374号电商一楼QS销退组","carrierName":"西南百世物流","deliveryNO":"BTH140612077597","shipInfoList":[{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"}]},
//	                                   {"id": 4,"serialNumber":"ff80808146222d5c014640b016ee00e2","returnStatus":"货物已交华南志鸿物流(退供)承运商，预计06-19到贵司仓库，请安排收货","returnFormNo":"CDRT200021500101","poOrderId":"200025931","returnQuantity":12,"objectSize":1.6200,"objectWeight":0.0000,"brandName":"秋水伊人","shipAdd":"西南仓","receiveAdd":"浙江省杭州市萧山区桥南区块鸿兴路374号电商一楼QS销退组","carrierName":"西南百世物流","deliveryNO":"BTH140612077597","shipInfoList":[{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"}]},
//	                                   {"id": 5,"serialNumber":"ff80808146222d5c014640b016ee00e2","returnStatus":"货物已交华南志鸿物流(退供)承运商，预计06-19到贵司仓库，请安排收货","returnFormNo":"CDRT200021500101","poOrderId":"200025931","returnQuantity":12,"objectSize":1.6200,"objectWeight":0.0000,"brandName":"秋水伊人","shipAdd":"西南仓","receiveAdd":"浙江省杭州市萧山区桥南区块鸿兴路374号电商一楼QS销退组","carrierName":"西南百世物流","deliveryNO":"BTH140612077597","shipInfoList":[{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"}]},
//	                                   {"id": 6,"serialNumber":"ff80808146222d5c014640b016ee00e2","returnStatus":"货物已交华南志鸿物流(退供)承运商，预计06-19到贵司仓库，请安排收货","returnFormNo":"CDRT200021500101","poOrderId":"200025931","returnQuantity":12,"objectSize":1.6200,"objectWeight":0.0000,"brandName":"秋水伊人","shipAdd":"西南仓","receiveAdd":"浙江省杭州市萧山区桥南区块鸿兴路374号电商一楼QS销退组","carrierName":"西南百世物流","deliveryNO":"BTH140612077597","shipInfoList":[{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"}]},
//	                                   {"id": 7,"serialNumber":"ff80808146222d5c014640b016ee00e2","returnStatus":"货物已交华南志鸿物流(退供)承运商，预计06-19到贵司仓库，请安排收货","returnFormNo":"CDRT200021500101","poOrderId":"200025931","returnQuantity":12,"objectSize":1.6200,"objectWeight":0.0000,"brandName":"秋水伊人","shipAdd":"西南仓","receiveAdd":"浙江省杭州市萧山区桥南区块鸿兴路374号电商一楼QS销退组","carrierName":"西南百世物流","deliveryNO":"BTH140612077597","shipInfoList":[{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"},{"actTime":1410502727855,"actDesc":"唯品会成都仓，已出仓","actPerson":"曾建敏"}]}
//                                   ];
            this.__returnList = window["g_returnList"]||[];
            
            this.__initSizeComponent();
        };
        
        pro.__initSizeComponent = function(){
            if(this.__returnList.length >0 ){
            	if(!this.__sizeList){
                    this.__sizeList = new SizeList({
                        data: {lists:this.__returnList}
                    }).$inject("#size-list-box");
                }
            }
        };

        p._$$ReturnModule._$allocate();
 });