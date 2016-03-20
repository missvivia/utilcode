/*
 * --------------------------------------------
 * xx控件实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define(
		[ '{lib}base/util.js'
		  , '{lib}base/event.js'
		  , '{lib}base/element.js'
		  ,	'{lib}util/event.js'
		  , '{lib}ui/base.js'
		  , '{lib}util/template/tpl.js'
	    , 'text!./sizeTmp.html'
	    , '{lib}util/template/jst.js'
      , 'pro/components/notify/notify'
		  ],
		function(u, _v, _e, t, i,e1,html,e2,notify,p) {
			var pro,
			// _seed_css = e._$pushCSSText(css);
	    _seed_ui = e1._$parseUITemplate(html),
	    _seed_head = _seed_ui['size-tmp'],
	    _seed_body = _seed_ui['size-type'],
	    _seed_box = _seed_ui['seedBox'];
			/**
			 * 全局状态控件
			 * 
			 * @class {nm.i._$$SizeTmp}
			 * @extends {nej.ui._$$Abstract}
			 */
			p._$$SizeTmp = NEJ.C();
			pro = p._$$SizeTmp._$extend(i._$$Abstract);
			sup = p._$$SizeTmp._$supro;


			/**
			 * 重置控件
			 * 
			 * @param {[type]}
			 *            options [description]
			 * 
			 */
			pro.__reset = function(options) {
				options.parent = options.parent || document.body;
				if(!options.data){
          options.data = 0;
        }
        this.__sizeTable = options.data;
				this.__sizeTmpBox = options.parent;
        if(!options.data.body.length){
          this.__isAdd = true;
        }else{
          this.__isAdd = false;
        }
				this.__super(options);
				this.__initTmpModule();
			};
			pro.__initTmpModule = function(){
				e2._$render(this.__body, _seed_head, {items:this.__sizeTable.header});
				e2._$render('size-type-box', _seed_body, {liList:this.__sizeTable.body,items:this.__sizeTable.header});
				this.__showSizeTmp(this.__sizeTmpBox); 
			}
			
			/**
			 * 控件销毁
			 * 
			 */
			pro.__destroy = function() {
				this.__super();
			};
			/**
			 * 初使化UI
			 */
			pro.__initXGui = function() {
				// 在正常开发中不太会把样式写在js中，_seed_css写在样式文件中，
				// this.__seed_html如果不设id上去，ui的父类会做一次this.__initNodeTemplate()操作，在样例中把this.__seed_html不设值了
				// 这里的ntp模板可以放在html的模板里，模板一定要parseTemplate才能取到这个id
				// this.__seed_html = 'wgt-ui-xxx';

				// this.__seed_css  = _seed_css;
		    this.__seed_html = _seed_box;
			};
			/**
			 * 节点初使化
			 * 
			 */
			pro.__initNode = function() {
				this.__super();
				//var list = e._$getByClassName(this.__body, 'j-flag');
				// v._$addEvent(this.__body, 'click', this.__onSizeTmpBoxClick._$bind(this));
			};

			pro.__showSizeTmp = function(_box){
        //尺码模版事件监听
        var _tmpList = _e._$getByClassName(_box,'j-tmp');
        this.__sizeUl = _tmpList[0];
        //增删列功能
        // this.__addColumn = _tmpList[1];
        this.__addRow = _tmpList[1];
        _v._$addEvent(this.__sizeUl,'click',this.__onDelBtnClick._$bind(this));
        _v._$addEvent(this.__addColumn,'click',this.__onAddColClick._$bind(this));
        _v._$addEvent(this.__addRow,'click',this.__onAddRowClick._$bind(this));
        if(this.__isAdd){
          _v._$dispatchEvent(this.__addRow,'click');
        }
      };
      // 尺码模版删除
      pro.__onDelBtnClick = function(_event){
        _v._$stop(_event);
        var _elm = _v._$getElement(_event),
          _typeList = this.__saveTypeList(),
          _index,liList;
        //删除一行
        if(_e._$hasClassName(_elm,'j-del')){
          _index = _e._$dataset(_elm,'index');
          if(_typeList.length <= 1){
            notify.show({
              'type':'error',
              'message':'必须保留一行！'
            })
          }else{
            _typeList.splice(_index,1);
          }
          e2._$render('size-type-box',_seed_body,{liList:_typeList,items:this.__sizeTable.header});
        }
        //删除一列
        if(_e._$hasClassName(_elm,'j-delTd')){
          _index = _e._$dataset(_elm,'index');
          this.__sizeTable.header.splice(_index,1);
          for(var i=0,len=this.__sizeTable.body.length;i<len;i++){
            this.__sizeTable.body[i].splice(_index,1);
          }
          this.__initTmpModule();
        }

      };
      // 尺码模版增加一列
      pro.__onAddColClick = function(){
        this.__saveTypeList();
        var _item = {id:0,name:'',required:false,unit:''};
        this.__sizeTable.header.push(_item);
        for(var i=0,len=this.__sizeTable.body.length;i<len;i++){
          this.__sizeTable.body[i].push('');
        }
        this.__initTmpModule();
      }
      // 尺码模版增加一行
      pro.__onAddRowClick = function(_event){
        _v._$stop(_event);
        var _typeList = this.__saveTypeList(),
          _row = [];
        for(var i=0,len=this.__sizeTable.header.length;i<len;i++){
          _row.push('');
        }
        _typeList.push(_row);
        e2._$render('size-type-box',_seed_body,{liList:_typeList,items:this.__sizeTable.header});
      };
      //保存当前尺码模版数据
      pro.__saveTypeList = function(){
        var _data = [],
          _box = _e._$get('size-type-box'),
          _sizeHeadList = _e._$getByClassName(this.__sizeTmpBox,'j-head'),
          _sizeInfoList = _e._$getByClassName(_box,'j-li');
        //save尺码模版body数据
        for(var i=0,len=_sizeInfoList.length;i<len;i++){
          var _items = [],
            _iptList = _sizeInfoList[i].getElementsByTagName('INPUT');
          for(var j=0,len2=_iptList.length;j<len2;j++){
            var _value = _iptList[j].value;
            _items.push(_value);
          }
          _data.push(_items);
          this.__sizeTable.body = _data;
        }
        //save尺码模版header数据
        for(var i=0,len=_sizeHeadList.length;i<len;i++){
          if(!!_sizeHeadList[i].children.length){
         	 this.__sizeTable.header[i].name = _sizeHeadList[i].children[0].value;
          }
        }
        return _data;
      };

      //得到模版数据
      pro._$getData = function(){
        this.__saveTypeList();
      	return this.__sizeTable;
      };

			return p._$$SizeTmp;
		})