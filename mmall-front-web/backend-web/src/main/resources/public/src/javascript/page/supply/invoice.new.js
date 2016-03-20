/**
 * 新建/编辑发货单页 author zzj(hzzhangzhoujie@corp.netease.com)
 */

define(['base/util', 
        'base/event', 
        'base/element', 
        'util/form/form',
		'util/ajax/xdr', 
		'util/encode/json', 
		'{pro}widget/module.js',
		'{pro}widget/datePicker/datePicker.js',
		'{pro}components/supply/skuListInInvoice.js',
		"{pro}components/notify/notify.js", 
		'{pro}lib/jquery/dist/jquery.min.js',
		'{pro}lib/uploadify/jquery.uploadify.min.js' ], 
	function(_ut, _v, _e,_f, _j, JSON, Module, dp, SkuListInInvoice, notify, p) {
	var pro;

	p._$$InvoiceNewModule = NEJ.C();
	pro = p._$$InvoiceNewModule._$extend(Module);

	pro.__init = function(_options) {
		this.__supInit(_options);
		this.__getNodes();
		this.__addEvent();
		this.__skuList = window["g_skuList"] || [];
		if (this.__skuList.length > 0) {
			this.__skuListInInvoice = new SkuListInInvoice({
				data : {
					lists : this.__skuList
				}
			}).$inject("#skuListInInvoice-box");
		}

	};

	pro.__getNodes = function() {
		this.__nextBtn = _e._$get('nextBtn');
		this.__prevBtn = _e._$get('prevBtn');
		this.__exportBtn = _e._$get('exportBtn');
		this.__submitBtn = _e._$get('submitBtn');
		this.__cardCont = _e._$getByClassName(_e._$get('g-bd'), 'card_c');
	};

	pro.__addEvent = function() {
		dp._$$datePickerModule._$allocate({
			pcon : 'searchform'
		});
		this.__form = _f._$$WebForm._$allocate({
			form : _e._$get('searchform')
		});
		_v._$addEvent(this.__nextBtn, 'click', this.__onNextBtnClick
				._$bind(this));
		_v._$addEvent(this.__prevBtn, 'click', this.__onPrevBtnClick
				._$bind(this));
		// _v._$addEvent(this.__exportBtn, 'click',
		// this.__onExportBtnClick._$bind(this,this.__exportBtn));
		_v._$addEvent(this.__submitBtn, 'click', this.__onSubmitBtnClick
				._$bind(this));
		this.__initUpload(this.__exportBtn);
	};

	pro.__onNextBtnClick = function(_event) {
		_v._$stop(_event);
		if (!this.__form._$checkValidity()) {
			notify.showError('带*为必填项，并输入正确的格式');
			return;
		}
		_j._$request('/supply/invoice/submitform.json', {
			headers : {
				"Content-Type" : "application/json;charset=UTF-8"
			},
			data : JSON.stringify({
				shipOrderDTO : this.__form._$data()
			}),
			method : 'post',
			type : 'json',
			onload : function(_data) {
				if (_data && _data.result) {
					_e._$addClassName(this.__cardCont[0], 'f-dn');
					_e._$delClassName(this.__cardCont[1], 'f-dn');
				} else {
					_e._$addClassName(this.__cardCont[0], 'f-dn');
					_e._$delClassName(this.__cardCont[1], 'f-dn');
				}
				this.__shipOrderId = _data.data.shipOrderId;
			}._$bind(this)
		});

	};

	pro.__onPrevBtnClick = function() {
		_e._$delClassName(this.__cardCont[0], 'f-dn');
		_e._$addClassName(this.__cardCont[1], 'f-dn');
	};

	pro.__initUpload = function(_upload) {
		$(_upload).uploadify({
			'swf' : '/src/javascript/lib/uploadify/uploadify.swf',
			'uploader' : '/supply//invoice/import.json', // 开发用
			'cancelImg' : '/src/javascript/lib/uploadify/uploadify-cancel.png',
			'buttonText' : '导入出仓商品明细',
			// 'buttonImg': '/resource/image/upload2.png',
			'auto' : true,
			'multi' : false,
			// width:40,
			// height:40,
			'fileTypeDesc' : 'excel文件',
			'fileTypeExts' : '*.xls;*.xlsx',
			'onUploadError' : function() {

			},
			'onUploadComplete' : function(_file) {

			},
			'onUploadSuccess' : function(_file, _data, _response) {
				var _result = JSON.parse(_data);
				if (_result && _result.data) {
					if (this.__skuListInInvoice) {
						this.__skuListInInvoice.$update(function(data) {
							data.lists = _result.data;
						});
					} else {
						this.__skuListInInvoice = new SkuListInInvoice({
							data : {
								lists : _result.data
							}
						}).$inject("#skuListInInvoice-box");
					}
				}
			}._$bind(this)
		});
	};

	pro.__onSubmitBtnClick = function(_event) {
		_v._$stop(_event);
		_j._$request('/supply/invoice/savesku.json', {
			headers : {
				"Content-Type" : "application/json;charset=UTF-8"
			},
			data : JSON.stringify({shipSkuDTOs:this.__skuListInInvoice.data.lists,shipOrderDTO:{shipOrderId:this.__shipOrderId}}),
			method : 'post',
			type : 'json',
			onload : function(_data) {
				if (_data && _data.result) {
					window.location = '/supply/inList';
				} else {
					notify.showError("提交失败！");
				}
			}._$bind(this)
		});
	};

	p._$$InvoiceNewModule._$allocate();
});