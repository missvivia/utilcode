var helpers = (function createHelpers(len){
  var rest = [];
  for(var i = 0 ;i < len; i++){
    rest.push({
      "id": i,
      "name": "助手名称"+i,
      "haxis": {
        "name": '体重kg',
        "list": [1,2,3,4,5,6,7,8,9]
      },
      "vaxis": {
        "name": "身高cm",
        "list": [111,112,113,114,115,116,117]
      },
      "body": [
        ['','','s', 's', 's', 's', 's', 's', 's'],
        ['','','s', 'm', 'm', 'm', 'm', 'm', 'm'],
        ['','','s', 'l', 'l', 'l', 'l', 'l', 'l'],
        ['','','xl', 'xl', 'xl', 'xl', 'xl', 'xl', 'xl'],
        ['','','xxl', 'xxl', 'xxl', 'xxl', 'xxl', 'xxl', 'xxl'],
        ['','','xs', 'xs', 'xs', 'xs', 'xs', 'xs', 'xs'],
        ['','xl','xxs', 'xxs', 'xxs', 'xxs', 'xxs', 'xxs', 'xxs']
      ]
    })
  } 
  return rest;
})(1017);

var invoices = (function createHelpers(len){
  var rest = [];
  for(var i = 0 ;i < len; i++){
    rest.push({
      "id":  i,
      "status": i % 2+1,
      "sonum": 1234566 + i,
      "firm": "乐得科技有限公司",
      "address": "浙江省.杭州市.滨江区.江汉路1785号",
      "price":"￥"+125.50+i,
      "po":"商品1；商品2；商品3;.........",
      "time":1410436682969+i*1000,
      "epnum":"xx",
      "express":"xx"
    })
  } 
  return rest;
})(1017)


module.exports = {
  "GET /rest/helpers": function(req, res){
    var offset = parseInt(req.query.offset) || 0;
    var limit = parseInt(req.query.limit) || 10;
    res.json({
      code: 200,
      result: {
        total: helpers.length,
        list: helpers.slice(offset, offset+limit)
      }
    })
  },
  "GET /rest/invoices": function(req, res){
    var offset = parseInt(req.query.offset) || 0;
    var limit = parseInt(req.query.limit) || 10;
    var status = parseInt(req.query.status) || 1;
    var list = invoices.filter(function(item){
      return item.status == status
    });
    res.json({
      code: 200,
      result: {
        total: list.length,
        list: list.slice(offset, offset+limit)
      }
    })
  },
  "POST /rest/invoices/submit": function(req, res){
    res.json({
      code: 200,
      result: true
    })
  },
  "DELETE /rest/helpers/:id": function(req, res){
    res.json({
      code: 200,
      result: true
    })
  },
  "POST /rest/helpers": function(req, res){
    res.json({
      code: 200,
      result: true
    })
  },
  "PUT /rest/helpers/:id": function(req, res){
    res.json({
      code: 200,
      result: true
    })
  },
  "GET /detail": function(req, res){
    res.json({
      code: 200,
      result: {
        haha: 200,
        result: {}
      }
    })
  },
  "POST /previewUnSavePage": function(req, res){
    res.json({
      code: 200,
      result: {
        haha: 200,
        result: {}
      }
    })
  }
}