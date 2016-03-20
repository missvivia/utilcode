/**
 * author hzwuyuedong@corp.netease.com
 */

define([
  "{pro}extend/util.js?v=1.0.0.0",
  "{pro}widget/BaseComponent.js?v=1.0.0.0",
  "{pro}components/datepicker/datepicker.js?v=1.0.0.0",
  "{pro}components/window/base.js?v=1.0.0.0",
  "{pro}components/window/spread.js?v=1.0.0.0",
  '{pro}components/notify/notify.js?v=1.0.0.0',
  'text!./list.html?v=1.0.0.0',
  'text!../window/spread.html?v=1.0.0.0'
], function (_, BaseComponent, dp, win0, win1, notify, tpl, tpl1) {


  var contentList = BaseComponent.extend({
    url: {
      'getList': '/content/spread/list/',
      'getProvince': '/content/getProvinceList',
      'order': '/content/spread/order',
      'delete': '/content/spread/delete'
    },
    name: "m-content-spread",
    template: tpl,
    config: function (data) {
      _.extend(data, {
        provinceId: '-1',
        searchTime: +new Date,
        result: [],
        currentCategory: [],
        promotionTypeMap: ['po', '活动/其他'],
        statusMap: {
          'ONLINE': '上线',
          'OFFLINE': '下线'
        }
      });
    },
    init: function () {
      // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
      //this.$on("updatelist", this.getList.bind(this));
      this.initProvince();
    },

    initProvince: function () {
      var data = this.data;
      this.$request(this.url['getProvince'], {
        data: {},
        onload: function (json) {
          data.provinceList = json.result;
        },
        onerror: function (json) {
          notify.showError('获取站点失败！');
        }
      });
    },

    setCurrentCategory: function (item) {
      this.data.currentCategory = item;
    },

    delete: function (index) {
      var modal = new win0({
        data: {
          'content': '是否删除推广位',
          'title': '删除推广位'
        }
      });

      modal.$on('confirm', function () {
        //确认删除
        var data = this.data,
          currentCategory = data.currentCategory;
        this.$request(this.url['delete'], {
          data: {
            'provinceId': data.provinceId,
            'searchTime': data.searchTime,
            'cid': currentCategory.list[index].id,
            'positionId': currentCategory.type
          },
          progress: true,
          onload: function (json) {
            currentCategory.list.splice(index, 1);
          }._$bind(this),
          onerror: function (json) {
            notify.showError('删除推广位失败！');
          }
        });

      }._$bind(this));
      modal.$inject('body');
    },


    move: function (index, type) {  // type: ture 上移
      var data = this.data,
        list = _.extend([], data.currentCategory.list),
        moveItem = list.splice(index, 1)[0],
        sequence = moveItem.sequence;
      if (!!type) {
        list.splice(index - 1, 0, moveItem);
      } else {
        list.splice(index + 1, 0, moveItem);
      }
      moveItem.sequence = list[index].sequence;
      list[index].sequence = sequence;

      this.$request(this.url['order'], {
        data: {
          'list': list,
          'positionId': this.data.currentCategory.type,
          'provinceId': data.provinceId,
          'searchTime': data.searchTime
        },
        method: 'post',
        progress: true,
        onload: function (json) {
          data.currentCategory.list = json.result;
        },
        onerror: function (json) {
          notify.showError('排序失败！');
        }
      });
    },

    //获取数据
    getList: function () {
      var data = this.data;
      if (data.provinceId == -1) {
        notify.showError('请选择站点！');
        return false;
      }
      this.$request(this.url['getList'], {
        data: {
          'provinceId': data.provinceId,
          'searchTime': data.searchTime
        },
        progress: true,
        onload: function (json) {
          data.result = json.result;
          data.currentCategory = json.result[0];
        },
        onerror: function (json) {
          notify.showError('获取数据失败！');
        }
      });
    },

    showModal: function (args) {
      var data = this.data;
      //copy
      var spread = _.extend({}, args, true);
      var modal = new win1({
        data: {
          'content': tpl1,
          'title': !spread ? '新建推广位' : '编辑推广位',
          'spread': spread,
          'tabType': data.currentCategory.type,
          'account': this.data.currentCategory.list.length,
          'provinceId': data.provinceId,
          'searchTime': data.searchTime,
          'positionId': data.currentCategory.type
        }
      });

      modal.$on('confirm', function (list) {
        data.currentCategory.list = list;
        this.$update();
      }._$bind(this));
      modal.$inject('body');
    }
  })

  return contentList;

})