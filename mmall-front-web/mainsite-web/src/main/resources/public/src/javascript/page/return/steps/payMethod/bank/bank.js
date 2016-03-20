/**
 * 
 */

define([
  "pro/extend/util",
  'text!./bank.html',
   "pro/page/return/steps/payMethod/payMethodComponent",
   'base/element',
   'base/util',
   'util/form/form',
   'util/chain/chainable',
    'base/event',
    'pro/widget/layer/address.card/address.city'
  ], function(_,_html,BaseComponent,_e,_u,_t,$,_v,AddressCard){

  var BankComponent = BaseComponent.extend({
    template:_html,
    watchAttrs:["isEdit"],
    config: function (data) {
      _.extend(data, {
        isEdit:true,
        refundType:2,
        bankCard:{
           bankType: "",
          bankBranch: "",
          bankCardOwnerName: "",
          bankCardNO: "",
          bankCardAddress: ""
        }
      }, true);
    },
    init:function(){
      var that = this;
      this.$on('inject',function(){
        that.initForm();
        that.bindEvents();
      })
    },
    initForm:function(){
      var webForm = $(".m-form-bank")[0];
       this.__form = _t._$$WebForm._$allocate({
             form:webForm,
             oninvalid:function(_event){
//                 $(_event.target)._$parent(".fitm")._$children('.flab')._$addClassName("err");
            	 var _node = $(_event.target);
            	 _node._$parent(".fitm")._$children('.flab')._$addClassName("err");
                 var _value = _e._$dataset(_node[0],"error");
                 $(".fitem-error")._$html(_value);
             },
           onvalid:function(_event){
               $(_event.target)._$parent(".fitm")._$children('.flab')._$delClassName("err");
               $(".fitem-error")._$html("");
           }
       });
    },
    bindEvents:function(){
        var that = this;
        $(".arrow")._$on("click",function(_event){
            _v._$stop(_event);
           that.initAddressCard();
        });
        $(".addressslt")._$on("click",function(_event){
            _v._$stop(_event);
           that.initAddressCard();
        });
    },
    addressCardShow:function(_event){
    	var elm = _v._$getElement(_event);
    	_v._$stop(_event);
    	if(this.__card){
    		this.__card = this.__card._$recycle();
    	}
    	this.__card = AddressCard._$allocate({parent:elm.parentNode,clazz:'fipt fipt-addr',onchange:function(_event){
            if(_event.city){
            	elm.value=_event.province+_event.city;
             }
            if(_event.section){
            	elm.value=_event.province+_event.section;
            }
         }});
    },
    initAddressCard:function(){
        var _parent="addrCard";
        var _province="",_city="",that=this;
        this.__card = AddressCard._$allocate({parent:_parent,onchange:function(_event){
           if(_event.city){
                $('[name="bankCardAddress"]')[0].value=_event.province+_event.city;
            }
           if(_event.section){
               $('[name="bankCardAddress"]')[0].value=_event.province+_event.section;
           }
        }});
    },
    getRequestParams:function(){
      return this.__form._$data();
    },
    checkValidity:function(){
      if(this.__form._$checkValidity()&&(!this.data.isEdit)){
        return true;
      }else{
        return false;
      }
    },
    save:function(){
      if(this.__form._$checkValidity()){
        this.data.bankCard=this.__form._$data();
        this.data.isEdit=false;
        this.$emit("checkSubmit");
      }
    }
  });


  return BankComponent;

})