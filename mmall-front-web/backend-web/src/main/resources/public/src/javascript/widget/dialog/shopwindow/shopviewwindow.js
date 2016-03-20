/*
 * --------------------------------------------
 * 店面弹出框基本页面
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define(
    [ '{lib}base/util.js'
        , '{lib}base/event.js'
        , '{lib}base/element.js'
        , '{lib}util/event.js'
        , '{pro}widget/dialog/shopwindow/shopwindow.js'
        , 'text!./shopwindow.html'
        , 'util/chain/chainable'
        , 'util/ajax/xdr'
        , 'base/config'
        , 'util/template/jst'
        , '{lib}util/template/tpl.js'
        ,"pro/components/notify/notify"
    ],
    function (u, v, e, t, ShopWindow, html, $, _ajax, c, _p2, l, notify,p, o, f, r) {
        var pro,
            _seed_html = l._$addNodeTemplate(html);

        /**
         * 所有分类卡片
         *
         * @class   {nm.l._$$ShopViewWin}
         * @extends {nm.l._$$CardWrapper}
         *
         *
         */
        p._$$ShopViewWin = NEJ.C();
        pro = p._$$ShopViewWin._$extend(ShopWindow);

        pro.__reset = function (options) {
            this.__super(options);
            this.__initEventsHandlers(options);
        };
        
        pro.__initAreasData=function(options){
        	var that=this;
        	this.__initSelectedAreasData(options);
        };
        
        
        pro.__initEventsHandlers=function(options){
	      $(".j-action",this.__body)._$remove();
	      var _childs = $(".form-control",this.__body)
	      for(var i=0;i<_childs.length;i++){
	    	  _childs[i].disabled="disabled";
	      }
        };
        

        // 控件重复使用重置过程
        // 控件回收销毁过程
        pro.__destroy = function () {
            this.__super();
        };



        pro.__initXGui = function () {
            this.__seed_html = _seed_html;
        };




        return p._$$ShopViewWin;
    })