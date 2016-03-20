/*
 * ------------------------------------------
 * 品牌详情
 * @version  1.0
 * @author   hzzhangweidong(hzzhangweidong@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'pro/extend/request',
    'pro/extend/config',
    'base/event',
    'pro/widget/module',
    'base/util',
    'util/template/jst'
//    'pro/widget/ui/shop/shop'
],function(_k,_e,_req,config,_v,_w,_u,_t,_i,_p,_o,_f,_r){
    var _pro;

    _p._$$BrandList = _k._$klass();
    _pro = _p._$$BrandList._$extend(_w._$$Module);

    /**
     * 初始化方法
     * @return {Void}
     */
    
    _pro.__init=function(_options){
    	this.__super(_options);
    	this.__nodes = _e._$getByClassName(document,'j-node');
    };

    /**
     * 重置方法
     * @param  {Object} _options - 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
//        this.__doInitDomEvent([
//            [this.__nodes[2],'click',this.__goTop._$bind(this)]
//        ]);
        this.__super(_options);
        this.__initBrandAlphaList(_options);
        this.__getBrandList();
        this.__doInitDomEvent([
         [this.__nodes[0],'click',this.__goTop._$bind(this)]
       ]);
    };
    
    _pro.__getBrandList=function(){
    	var that=this;
    	_req(config.DOMAIN_URL+'/brand/3g/list',{
		             type:'json',
		             method:'get',
		             onload:function(_data){
//		            	 var _data={"code":200,"message":null,"result":{"total":21,"hasNext":false,"list":[{"brandId":17246,"brandNameZh":"巴拉巴拉","brandNameEn":"BaLaBaLa","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1415098185264,"brandHead":"B","logo":"http://mmall-photocenter-bucket2.nos.netease.com/d881c7a2-b598-469b-a644-78071ba4f98a","brandVisualImgWeb":null,"brandVisualImgApp":"http://mmall-photocenter-bucket2.nos.netease.com/af11953a-d0f3-4a43-b9ee-45c9dd8742b1","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":330691,"brandNameZh":"z包包","brandNameEn":"Baob","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1418612869639,"brandHead":"B","logo":"http://paopao.nos.netease.com/e220406b-7190-4819-aa87-c0e2c7353a5a","brandVisualImgWeb":null,"brandVisualImgApp":"http://paopao.nos.netease.com/4692dd0b-e6ce-4cac-8455-e0c1df2c242d","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":330660,"brandNameZh":"地球zy测试品牌","brandNameEn":"the earth","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1418612137752,"brandHead":"D","logo":"http://paopao.nos.netease.com/eac74786-d42c-4877-8440-f91db289fec0","brandVisualImgWeb":null,"brandVisualImgApp":"http://paopao.nos.netease.com/78e4cb44-8af0-44d9-affb-7c9095d3c4d2","poCount":0,"favCount":0,"favorited":true,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":368366,"brandNameZh":"二二二","brandNameEn":"222","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1418808608061,"brandHead":"","logo":"http://paopao.nos.netease.com/00d13f75-b4d5-4a30-b492-40126a7d8175","brandVisualImgWeb":null,"brandVisualImgApp":"http://paopao.nos.netease.com/e1e13f77-94d8-4c60-b3a0-d3b6214fcadb","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":27577,"brandNameZh":"GXG","brandNameEn":"GXG","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1415844695854,"brandHead":"G","logo":"http://mmall-photocenter-bucket2.nos.netease.com/1420f5b9-a560-41a9-a73c-fc62b3ebbcc0","brandVisualImgWeb":null,"brandVisualImgApp":"http://mmall-photocenter-bucket2.nos.netease.com/2ea2abcb-39e6-4f0a-94d8-e0056bc5037b","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":18576,"brandNameZh":"忽忽呼呼","brandNameEn":"who","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1415847623058,"brandHead":"H","logo":"http://mmall-photocenter-bucket2.nos.netease.com/145f00a8-ed7d-4f77-a935-9061c44a7e0f","brandVisualImgWeb":null,"brandVisualImgApp":"http://mmall-photocenter-bucket2.nos.netease.com/955b25b4-d8e0-4e9f-8d5e-c28ac59796c7","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":167509,"brandNameZh":"红袖","brandNameEn":"Hopeshow","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1416816685937,"brandHead":"H","logo":"http://paopao.nos.netease.com/d081b6a5-2c14-44b3-8ed4-64a76b233dd5","brandVisualImgWeb":null,"brandVisualImgApp":"http://paopao.nos.netease.com/f83c3c12-084e-467c-bb1a-011d3c5960f3","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":15163,"brandNameZh":"江南布衣-yxq","brandNameEn":"JNBY-yxq","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1414983911623,"brandHead":"J","logo":"http://mmall-photocenter-bucket2.nos.netease.com/37d1a5b8-228a-468a-8e32-549de23dc681","brandVisualImgWeb":null,"brandVisualImgApp":"http://mmall-photocenter-bucket2.nos.netease.com/3ba1b29f-11fa-4cd7-a020-245bd972e405","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":146984,"brandNameZh":"空品牌","brandNameEn":"Kong","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1416637439411,"brandHead":"K","logo":"http://paopao.nos.netease.com/55f52b83-1ada-4ef4-8af5-1fe494852c3e","brandVisualImgWeb":null,"brandVisualImgApp":"http://paopao.nos.netease.com/80a51a2b-5380-4bac-aa48-6248f77f8d1f","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":49444,"brandNameZh":"牛奶","brandNameEn":"milk","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1415855130178,"brandHead":"M","logo":"http://mmall-photocenter-bucket2.nos.netease.com/12e64607-ec7d-48b9-aaf2-eca497c4b38c","brandVisualImgWeb":null,"brandVisualImgApp":"http://mmall-photocenter-bucket2.nos.netease.com/af01c4e9-7e32-4d7b-9054-8ae725bfdc9e","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":49444,"brandNameZh":"牛奶","brandNameEn":"milk","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1415855130178,"brandHead":"N","logo":"http://mmall-photocenter-bucket2.nos.netease.com/12e64607-ec7d-48b9-aaf2-eca497c4b38c","brandVisualImgWeb":null,"brandVisualImgApp":"http://mmall-photocenter-bucket2.nos.netease.com/af01c4e9-7e32-4d7b-9054-8ae725bfdc9e","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":15304,"brandNameZh":"品牌zzz","brandNameEn":"ZZZ","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1414983962193,"brandHead":"P","logo":"http://mmall-photocenter-bucket2.nos.netease.com/a3c9e65b-23d3-40c2-a186-0ab2b13f8ece","brandVisualImgWeb":null,"brandVisualImgApp":"http://mmall-photocenter-bucket2.nos.netease.com/53dc7072-ef07-43ac-9bee-2008df522231","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":15308,"brandNameZh":"秋水伊人","brandNameEn":"qiushuiyiren","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1414984117038,"brandHead":"Q","logo":"http://mmall-photocenter-bucket2.nos.netease.com/98844084-24b4-4736-baa2-37eae8654caf","brandVisualImgWeb":null,"brandVisualImgApp":"http://mmall-photocenter-bucket2.nos.netease.com/6f94264b-af51-455c-aea3-835c84aacecc","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":28108,"brandNameZh":"兔子","brandNameEn":"TU","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1415669919783,"brandHead":"T","logo":"http://mmall-photocenter-bucket2.nos.netease.com/7ac9205a-e309-4a61-aec4-f7045dc176d1","brandVisualImgWeb":null,"brandVisualImgApp":"http://mmall-photocenter-bucket2.nos.netease.com/fa0d0c6d-9f6a-4795-b46a-fdc0dedab5b2","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":330660,"brandNameZh":"地球zy测试品牌","brandNameEn":"the earth","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1418612137752,"brandHead":"T","logo":"http://paopao.nos.netease.com/eac74786-d42c-4877-8440-f91db289fec0","brandVisualImgWeb":null,"brandVisualImgApp":"http://paopao.nos.netease.com/78e4cb44-8af0-44d9-affb-7c9095d3c4d2","poCount":0,"favCount":0,"favorited":true,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":18576,"brandNameZh":"忽忽呼呼","brandNameEn":"who","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1415847623058,"brandHead":"W","logo":"http://mmall-photocenter-bucket2.nos.netease.com/145f00a8-ed7d-4f77-a935-9061c44a7e0f","brandVisualImgWeb":null,"brandVisualImgApp":"http://mmall-photocenter-bucket2.nos.netease.com/955b25b4-d8e0-4e9f-8d5e-c28ac59796c7","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":21532,"brandNameZh":"茵曼","brandNameEn":"yinman","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1416379079887,"brandHead":"Y","logo":"http://mmall-photocenter-bucket2.nos.netease.com/89e51689-e6e6-4a7e-aa54-c058e7e9e9f7","brandVisualImgWeb":null,"brandVisualImgApp":"http://mmall-photocenter-bucket2.nos.netease.com/3449f8ac-fd8f-47a5-a7b5-5b3bb255cf03","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":15304,"brandNameZh":"品牌zzz","brandNameEn":"ZZZ","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1414983962193,"brandHead":"Z","logo":"http://mmall-photocenter-bucket2.nos.netease.com/a3c9e65b-23d3-40c2-a186-0ab2b13f8ece","brandVisualImgWeb":null,"brandVisualImgApp":"http://mmall-photocenter-bucket2.nos.netease.com/53dc7072-ef07-43ac-9bee-2008df522231","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":48235,"brandNameZh":"猪猪熊","brandNameEn":"zhuzhuxiong","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1415848152196,"brandHead":"Z","logo":"http://mmall-photocenter-bucket2.nos.netease.com/ba12d019-3bfe-4ea1-862e-f6607931eedc","brandVisualImgWeb":null,"brandVisualImgApp":"http://mmall-photocenter-bucket2.nos.netease.com/132e3f53-1041-42d2-a9e4-e990ffc824f4","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":99307,"brandNameZh":"ZARA","brandNameEn":"ZARA","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1416211142311,"brandHead":"Z","logo":"http://mmall-photocenter-bucket2.nos.netease.com/5e9779a4-0f1c-4b71-bc20-ad71bd2feace","brandVisualImgWeb":null,"brandVisualImgApp":"http://mmall-photocenter-bucket2.nos.netease.com/88504d43-2d17-478f-90f3-3e0b39106869","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0},{"brandId":346415,"brandNameZh":"zyy自创品牌","brandNameEn":"zyycreate","createDate":0,"createUser":"mmallroottest@163.com","brandUpdateDate":1418697542520,"brandHead":"Z","logo":"http://paopao.nos.netease.com/73a2eedb-55c9-4caa-9473-f384c84cb31e","brandVisualImgWeb":null,"brandVisualImgApp":"http://paopao.nos.netease.com/1f558cda-a977-4056-be16-68c2f2b851b1","poCount":0,"favCount":0,"favorited":false,"nextPoTime":0,"nextPoEndTime":0,"scheduleId":0,"favTime":0,"brandProbability":0}],"lastId":0}};
		            	 that.__brandCallBack(_data);
		             },
		             onerror:function(_error){
		                 // 异常处理
		             }
	             }
	         );
    };
    
    _pro.__initBrandAlphaList=function(_options){
    	var that =this;
    	var _alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ#".split("");
    	this.__alphaList=this.__alphaList||{};
    	_u._$forEach(_alphabet,function(alpha){
    		that.__alphaList[alpha]=[];
    	})
    };
    
    _pro.__brandCallBack=function(_data){
    	
    	this.__formatData(_data.result.list);
    	this.__renderPage();
    	
    };
    
    _pro.__formatData=function(_list){
    	 var that=this;
    	 _u._$forEach(_list,function(_shop){
    		 if(_shop.brandHead){
    			  var _arr = that.__alphaList[_shop.brandHead]||[];
        		 that.__alphaList[_shop.brandHead] = _arr;
                 _arr.push(_shop);
    		 }
    		 else{
    			 that.__alphaList["#"].push(_shop);
    		 }
    			 
    		 
    	 });
    	 //去掉空的。
    	 for (var prop in this.__alphaList) {
    		  if( this.__alphaList.hasOwnProperty( prop ) ) {
    		    if(this.__alphaList[prop].length==0){
    		    	delete this.__alphaList[prop];
    		    }
              } 
        }
    	 
    	
    };
    
    _pro.__renderPage=function(){
    	_t._$render("alpha","jst-template-1",{xlist:this.__alphaList});
    };
    
    _pro.__goTop = function(_event){
        _v._$stop(_event);
        window.scrollTo(0,0);
    };
    


    _p._$$BrandList._$allocate({});
    return _p;
});