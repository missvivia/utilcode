/*
 * ------------------------------------------
 * 首页模块
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'util/dispatcher/module',
    'pro/page/helpcenter/module',
    'util/template/jst',
    'pro/extend/request'
],function(_k,_v,_e,_x,_m,_t,_req,_p,_o,_f,_r,_pro){
    /**
     * 布局模块
     * @class   _$$Module
     * @extends _$$Module
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 构建模块
     * @return {Void}
     */
    _pro.__doBuild = function(){
        this.__super({
            tid:'helpcenter-module-index'
        });
        
       /* _req('/help/leftNav',{
            type:'json',
            method:'GET',
            onload:this.__cbListLoad._$bind(this),
            onerror:this.__cbListLoad._$bind(this)
        });*/
        
    };
    /**
     * 加载列表回调
     * @param {Object} _json
     */
    _pro.__cbListLoad = function(_node,_json){
        _json = _json||_o;
        var _list = [];
        if (_json.code==200){
            _list = (_json.result||_o).list;
            _t._$render(this.__body,'helpcenter-module-index-list',{sideNav:_list});
        }
    };
    /**
     * 显示模块
     * @return {Void}
     */
    _pro.__onShow = function(_options){
        this.__super(_options);
        _req('/help/leftNav',{
            type:'json',
            method:'GET',
            onload:function(json){
            	var _navList = (json.result||_o).list,p = _navList.splice(0,1);
            	_req('/help/article?categoryId=1',{
            		type:'json',
                    method:'GET',
                    onload:function(json){
                    	_t._$render(this.__body,'helpcenter-module-index-list',{problem:json.result.list,sideNav:_navList});
                    }._$bind(this)
            	})
            	
            }._$bind(this),
            onerror:function(){
            	
            }
        });
        
//        var _data = {
//    		    "code":200,
//    		    "result":{
//    		        "list":[
//    		            {
//    		                "id":"1",
//    		                "name":"常见问题",
//    		                "children":[
//    		                   {
//    		                       "articleId":"1",
//    		                       "title":"新云联秀品的商品都是正品吗？",
//    		                       "cont":"<div>绝对是正品</div>" 
//    		                   },
//    		                   {
//    		                       "articleId":"2",
//    		                       "title":"新云联秀品的商品都是电商吗？",
//    		                       "cont":"<div>绝对是正品</div>" 
//    		                   }
//    		                ]
//    		            },
//    		            {
//    		                "id":"2",
//    		                "name":"新手指南",
//    		                "children":[
//    		                   {
//    		                       "id":"31",
//    		                       "name":"登陆注册"
//    		                   },
//    		                   {
//    		                       "id":"32",
//    		                       "name":"抢购商品"
//    		                   },
//    		                   {
//    		                       "id":"3",
//    		                       "name":"支付流程"
//    		                   },
//    		                   {
//    		                       "id":"4",
//    		                       "name":"签收商品"
//    		                   },
//    		                   {
//    		                       "id":"4",
//    		                       "name":"办理退货"
//    		                   }
//    		                ]
//    		            },
//    		            {
//    		                "id":"3",
//    		                "name":"帐号相关",
//    		                "children":[
//    		                   {
//    		                       "id":"31",
//    		                       "name":"订单管理"
//    		                   },
//    		                   {
//    		                       "id":"32",
//    		                       "name":"修改个人资料"
//    		                   },
//    		                   {
//    		                       "id":"33",
//    		                       "name":"修改收货地址"
//    		                   },
//    		                   {
//    		                       "id":"34",
//    		                       "name":"消息推送"
//    		                   }
//    		                ]
//    		            },
//    		            {
//    		                "id":"4",
//    		                "name":"售后服务",
//    		                "children":[
//    		                   {
//    		                       "id":"41",
//    		                       "name":"退货政策"
//    		                   },
//    		                   {
//    		                       "id":"42",
//    		                       "name":"退货流程"
//    		                   },
//    		                   {
//    		                       "id":"43",
//    		                       "name":"退款方式和时效"
//    		                   },
//    		                   {
//    		                       "id":"44",
//    		                       "name":"发票事宜"
//    		                   }
//    		                ]
//    		            },
//    		            {
//    		                "id":"5",
//    		                "name":"物流配送",
//    		                "children":[
//    		                   {
//    		                       "id":"51",
//    		                       "name":"验货与签收"
//    		                   },
//    		                   {
//    		                       "id":"52",
//    		                       "name":"配送方式及时间"
//    		                   },
//    		                   {
//    		                       "id":"53",
//    		                       "name":"配送范围及运费"
//    		                   },
//    		                   {
//    		                       "id":"54",
//    		                       "name":"配送状态查询"
//    		                   }
//    		                ]
//    		            }
//    		            
//    		        ]
//    		    }
//    		};
        
    };
    
    // regist module
    _x._$regist('index',_p._$$Module);
});
