/*
 * @author   hzwuyuedong(hzwuyuedong@corp.netease.com)
 */
NEJ.define([
  'base/util',
  'text!./address.html',
  'pro/components/notify/notify',
  'pro/components/ListComponent',
  'pro/extend/util',
  'pro/widget/layer/address/address',
  'pro/widget/layer/sure.window/sure.window',
  'pro/widget/layer/alert/alert'
], function (_ut, _html, notify, ListComponent, _, AddressWin, SureWindow, AlertWindow, _p, _o, _f, _r) {

  // change事件，组建内容发生变化的时候emit
  // updatelist 重新获取全部地址
  return ListComponent.extend({
    url: '/profile/address/list',
    api: '/user/address/',
    template: _html,
    name: 'wgt-address',
    config: function (data) {
      this.supr(data);
      _.extend(data, {
        viewCount: 3,
        list: []
      })
    },
    getExtraParam: function () {
      return this.data.condition;
    },

    //重写__getList
    __getList: function(){
      var data = this.data;
      this.$request(this.url, {
        data: this.getListParam(),
        method:'POST',
        onload: function(json){
          var result = json.result, currentAddress,
            list = result.list || result;
          _.mergeList(list, data.list, data.key || "id");
          data.total = result.total;
          data.list = list;
          currentAddress = this.getCurrentAddress();
          this.$update();
          this.$emit('change', {'currentAddress': currentAddress});
        }._$bind(this),
        onerror: function(json){
          notify.notify({
            type: "error",
            message: json && json.message || '获取地址失败！'
          });
        }._$bind(this)
      })
    },

    refresh: function (_data) {
      if (!!_data.url) {
        this.url = _data.url;
        delete _data.url;
      }
      this.data.current = 1;
      this.data.condition = _data;
      this.$emit('updatelist');
    },

    //选中
    check: function (item) {
      var data = this.data;
      if(!item.checked){
        for(var i=0,l=data.list.length;i<l;i++){
          if(data.list[i].checked){
            data.list[i].checked = false;
          }
        }
        item.checked = true;
        this.$emit('change', {'currentAddress': item});
      }
    },

    //用户没有选择，或者选中的被删除的时候调用
    getCurrentAddress: function(){
      var data = this.data, currentAddress;
      if(data.list.length > 0){
        for(var i=0,l=data.list.length;i<l;i++){
          if(!!data.list[i] && !!data.list[i].isDefault){
            currentAddress = data.list[i];
            break;
          }
        }
        if(!currentAddress){
          currentAddress = data.list[0];
        }
        currentAddress.checked = true;
      }
      return currentAddress;
    },

    //公用方法_setDefault
    _setDefault: function(item){
      for(var i=0,l=this.data.list.length;i<l;i++){
        if(this.data.list[i].isDefault){
          this.data.list[i].isDefault = false;
        }
      }
      item.isDefault = true;
    },

    //设置默认地址，并选中
    setDefault: function (event, item) {
      event.stopPropgation();
      this.$request('/profile/address/setdefault',{
        query: _ut._$object2query({id:item.id}),
        norest:true,
        type:'json',
        onload:function(){
          this._setDefault(item);
          this.check(item);
        }._$bind(this),
        onerror: function(json){
          notify.notify({
            type: "error",
            message: json && json.message || '设置默认地址失败！'
          });
        }
      })
    },

    //删除
    deleteAddress: function(event, item, index) {
      event.stopPropgation();
      SureWindow._$allocate({onok:function(){
        this.$request('/profile/address/delete',{
          query:_ut._$object2query({id: item.id}),
          norest:true,
          type:'json',
          onload:function(result){
            var currentAddress;
            //先删除model中的item
            this.data.list.splice(index,1);
            this.data.viewCount = this.data.list.length - 1;
            //删除了选中的地址
            if(item.checked){
              currentAddress = this.getCurrentAddress();
              this.$emit('change', {'currentAddress': currentAddress});
            }
          }._$bind(this),
          onerror:function(json){
            notify.notify({
              type: "error",
              message: json && json.message || '删除地址失败！'
            });
          }
        })
      }._$bind(this),clazz:'w-win w-win-1 w-win-1-3'})._$show();
    },

    //编辑默认地址，并选中
    updateAddress: function (event, item, index) {
      event.stopPropgation();
      AddressWin._$allocate({type:1,address:item,onok:function(data){
        // /user/addess/update
        data.id = item.id;
        this.$request('/profile/address/update',{
          data: data,
          method:'POST',
          onload:function(result){
            var item = result.result;
            this.data.list[index] = item;
            //编辑的地址为默认地址
            if(item.isDefault){
              this._setDefault(item);
            }
            this.check(item);
          }._$bind(this),
          onerror:function(json){
            notify.notify({
              type: "error",
              message: json && json.message || '编辑地址失败！'
            });
          }
        })
      }._$bind(this)})._$show();
    },

    //添加默认地址，并选中
    addAddress: function (event) {
      event.stopPropgation();
      if(this.data.list.length >= 10){
        AlertWindow._$$AlertWindow._$allocate({
          text: '最多只能保存10条地址！'
        })._$show();
        return false;
      };
      AddressWin._$allocate({type:0,onok:function(data){
        // /user/addess/add
        this.$request('/profile/address/add',{
          data:data,
          method:'POST',
          onload:function(result){
            var item = result.result;
            this.data.list.push(item);
            (this.data.list.length > 4) && this.viewAll();
            //新加的地址为默认地址
            if(item.isDefault){
              this._setDefault(item);
            }
            this.check(item);
          }._$bind(this),
          onerror:function(json){
            notify.notify({
              type: "error",
              message: json && json.message || '保存地址失败！'
            });
          }
        })
      }._$bind(this)})._$show();
    },

    viewAll: function () {
      this.data.viewCount = this.data.list.length - 1;
    }
  });
});