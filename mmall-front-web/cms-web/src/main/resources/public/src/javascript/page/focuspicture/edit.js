/*
 * ------------------------------------------
 * 添加/编辑焦点图
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */

NEJ.define([
  'base/util',
  'pro/extend/util',
  'pro/components/window/base',
  'pro/components/datepicker/datepicker',
  'pro/components/upload/imgUpload',
  'pro/components/notify/notify',
  'util/form/form'
], function(_u,_, BaseModal, dp, imgUpload,notify,webForm, p,o,f,r){

  var contentModal = BaseModal.extend({
        url: '/focuspicture/checkpo',
        watchedAttr: ['title', 'scheduleId', 'activityUrl', 'platforms' , 'imgUrl', 'startTime', 'endTime','citys'],
        rule: ['Require','Require','Require','PlatformCheckbox', 'Url', 'Require' ,'Require','RequireCheckbox'],
        config: function(data){
          if (data.spread.scheduleId === 0){
            data.spread.scheduleId = '';
          }
          _.extend(data, {
            spread:{
              'promotionType': 0,
              'allcity':0,
              'platformType':''
            },
            form: {},
            cityList:[],
            citys:{'001':false,'002':false,'003':false},
            platforms:{'ios':false,'android':false},
            platformsList:['ios','android'],
            citysname:{'001':'北京','002':'上海','003':'杭州'}
          });
          // form需要检查的字段
          for (var i= 0, l=this.watchedAttr.length;i<l; i++){
            var name = this.watchedAttr[i];
            data.form[name] = {};
          }
          // 转换为对象可以watch
          if (!!data.spread.platformType){
            _u._$forEach(data.spread.platformType.split(','),function(item){
                data.platforms[item] = true;
            });
          }
          // 转换为对象可以watch
          // _u._$forEach(data.spread.citys,function(item){
          //     data.citys[item.id] = true;
          // });
          // _u._$forIn(data.citys,function(_v,_k){
          //     data.cityList.push(_k);
          // });
        },

        init: function(){
          // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
        },

        checkPo: function(scheduleId){
          this.$request(this.url, {
            data: {poId:scheduleId},
            onload: function(json){
              if (json.code == 200){
                this.data.poInvalidate = false;
              }else{
                this.data.poInvalidate = true;
              }
            },
            // test
            onerror: function(json){
              this.data.poInvalidate = true;
            }
          })
        },

        //重写confirm
        confirm: function(event){
          event.preventDefault();
          if (this.checkForm()){
              // 同步站点和平台的状态
              // this.data.spread.citys = [];
              // _u._$forIn(this.data.citys,function(v,key){
              //   if (v){
              //     var temp = {};
              //     temp.id = key;
              //     temp.name = this.data.citysname[key];
              //     this.data.spread.citys.push(temp)
              //   }
              // }._$bind(this));
              this.data.spread.platformType = [];
              _u._$forIn(this.data.platforms,function(v,key){
                if (v){
                  this.data.spread.platformType.push(key)
                }
              }._$bind(this));
              this.data.spread.platformType = this.data.spread.platformType.join(',');
              // 直接提交spread
              this.$emit('confirm', this.data.spread);
              this.destroy();
          }
        },

        checkForm: function(){
          var spread = this.data.spread,
              data = this.data,
              form = this.data.form;
          var findCheckbox = function(list){
            var hasckd = false;
              _u._$forIn(list,function(item){
                if (!!item){
                  hasckd = true;
                  return false;
                }
              });
              return hasckd;
          };
          var fun = {
            isPlatformCheckbox:function(){
              return findCheckbox(data.platforms);
            },
            isRequireCheckbox:function(){
              return true;
              // return findCheckbox(data.citys);
            },
            isUrl: function(value){
              return /^(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?$/.test(value);
            },
            isRequire: function(value){
              return !!value　? (''+value).replace(/(^\s*)|(\s*$)/g,'') !== '' : false;
            },
            is: function(){
              return true;
            }
          }
          _u._$forEach(this.watchedAttr,function(_key,_index){
            var _value = this.data.spread[_key];
            var _suffix = this.rule[_index];
            // 两种情况不用检查
            if ( (_index == 1 && spread.promotionType == 1) || (_index == 2 && spread.promotionType == 0)) return;
            form[_key].$inValidate = !fun['is' + _suffix](_value);
          }._$bind(this));
          //结束日期<开始日期
          if(new Date(spread.endTime).getTime() <　new Date(spread.startTime).getTime()){
            form['endTime'].$inValidate = true;
          }
          if(form['title'].$inValidate || form['platforms'].$inValidate || form['imgUrl'].$inValidate || form['startTime'].$inValidate || form['endTime'].$inValidate ||
            (spread.promotionType == 0)&&(form['scheduleId'].$inValidate || this.data.poInvalidate) ||  (spread.promotionType == 1)&&form['activityUrl'].$inValidate){
            form.$inValidate = true;
          }else{
            form.$inValidate = false;
          }
          return !form.$inValidate;
        },

        //所有城市的选择和反选
        allCity:function(flag){
          if (!flag){
            this.data.spread.allcity = 1;
          }else{
            this.data.spread.allcity = 0;
          }
        }
    })

    return contentModal;
});