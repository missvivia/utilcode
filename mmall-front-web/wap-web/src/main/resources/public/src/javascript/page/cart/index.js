/**
 * Created by hzwuyuedong.
 */

define([
    '{lib}base/klass.js',
    '{pro}widget/module.js',
    '{pro}page/cart/cart.js?v=1.0.0.1',
    'util/chain/NodeList'
  ],
  function (k, t, CartModule,$, _$, p, o, f, r, pro) {
    p.$$CartModule = k._$klass();
    pro = p.$$CartModule._$extend(t._$$Module);

    pro.__init = function (_options) {
      this.__supInit(_options);
      this.__cart = new CartModule();
      this.__cart.$inject('#cart-body' || document.body);
      this.__cart.$emit('updatewgt');
      this.__bindEvent();
    };
    pro.__bindEvent = function(){
//    	$(".skuCheck")._$on("click",function(){
//    		
//    	});
    };
    p.$$CartModule._$allocate();

    return p;
  });