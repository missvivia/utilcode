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
		  , 'text!./tree.css'
	    , 'text!./tree.html'
	    , '{lib}util/template/jst.js'
		  ],
		function(u, v, e, t, i,e1,css,html,e2,p, o, f, r) {
			var pro,
			_seed_css = e._$pushCSSText(css);
	    var _seed_ui = e1._$parseUITemplate(html),
	    _seed_tree = _seed_ui['seedTree'],
	    _seed_box = _seed_ui['seedBox'];
			/**
			 * 全局状态控件
			 * 
			 * @class {nm.i._$$Tree}
			 * @extends {nej.ui._$$Abstract}
			 */
			p._$$Tree = NEJ.C();
			pro = p._$$Tree._$extend(i._$$Abstract);
			sup = p._$$Tree._$supro;


			/**
			 * 重置控件
			 * 
			 * @param {[type]}
			 *            options [description]
			 * 
			 */
			pro.__reset = function(options) {
				options.parent = options.parent || document.body;
				this.__super(options);
				e2._$render(this.__body, _seed_tree, {items:options.tree});
			};
			
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
				this.__seed_css  = _seed_css;
		    this.__seed_html = _seed_box;
			};
			/**
			 * 节点初使化
			 * 
			 */
			pro.__initNode = function() {
				this.__super();
				//var list = e._$getByClassName(this.__body, 'j-flag');
				v._$addEvent(this.__body, 'click', this.__onTreeBoxClick._$bind(this));
			};
			
			pro.__onTreeBoxClick = function(event){
				var _elm = v._$getElement(event),
						_id = e._$dataset(_elm,'id'),
						_check = _elm.checked,
						_parent = e._$dataset(_elm,'parent');
				if(_elm.type == 'checkbox'){
					this.__recheckTree(_id,_parent,_check);
				}
			}
			pro.__recheckTree = function(_id,_parent,_check){
				var _box = e._$get('tree'),
						_list = e._$getByClassName(_box,'chk');
				//处理孩子节点
				this.__checkChild(_list,_id,_check);
				//处理父节点
				this.__checkParent(_list,_id,_parent,_check);

			}
			pro.__checkChild = function(_list,_id,_check){
				for(var i=0,len=_list.length; i<len; i++){
					var _chkParent = e._$dataset(_list[i],'parent')
							,_chkId = e._$dataset(_list[i],'id');
					if(_chkParent === _id){
						_list[i].checked = _check;
						//递归孩子节点
						this.__checkChild(_list,_chkId,_check);
					}
				}
			}
			pro.__checkParent = function(_list,_id,_parent,_check){
				for(var i=0,len=_list.length; i<len; i++){
					var _chkParent = e._$dataset(_list[i],'parent')
							,_chkId = e._$dataset(_list[i],'id');
					if(_chkId === _parent){
						var _mark = 1;
						if(_check === true){
							for(var j=0,len=_list.length; j<len; j++){
								var _checkParent = e._$dataset(_list[j],'parent');
								if(_checkParent === _parent){
									if(_list[j].checked == false){
										_mark = 0;
									}
								}
							}	
						}
						if(!!_mark){
							_list[i].checked = _check;
						}
						//递归父亲节点
						this.__checkParent(_list,_id,_chkParent,_check);
					}
				}
			}
			pro.__getChecks = function(){
				var data = [],
						_box = e._$get('tree'),
						_list = e._$getByClassName(_box,'chk');
				for(var i=0,len=_list.length; i<len; i++){
					if(_list[i].checked){
						var _item = {},
								_parentId = parseInt(e._$dataset(_list[i],'parent'));
						if(!!_parentId){
							_item['id'] = parseInt(e._$dataset(_list[i],'id'));
							data.push(_item);
						}
					}
				}
				return data;
			}
			pro.__setParent = function(_list,_elm){
				var _parentId = e._$dataset(_elm,'parent');
				for(var i=0,len=_list.length;i<len;i++){
					if(e._$dataset(_list[i],'id') == _parentId){
						_list[i].checked = true;
					}
				}
			};
			//以[{"id":11},{"id":112},{"id":13}]的方式返回
			pro._$getData = function(){
				return this.__getChecks();
			};
			// 重置tree中选中的checkbox
			pro._$clearTree = function(){
				var _box = e._$get('tree'),
						_list = e._$getByClassName(_box,'chk');
				for(var i=0,len=_list.length; i<len; i++){
					_list[i].checked = false;
				}
			}
			pro._$setTree = function(id){
				var _box = e._$get('tree'),
						_list = e._$getByClassName(_box,'chk');
				for(var i=0,len=_list.length;i<len;i++){
					if(e._$dataset(_list[i],'id') == id){
						_list[i].checked = true;
						this.__setParent(_list,_list[i]);
					}
				}
			}


			return p._$$Tree;
		})