<#assign product = {
  "productId": 123,
  "brand":{
    "id": 123,
    "name": "xxx"
  },
  
  "productName" : "女款吊带", 
  "goodsNo" : "simple001", 
  "poId" : 1909, 
  "marketPrice" : 250, 
  "salePrice" : 200.00, 
  "status": 1,
  "skuId":1,
  "sizeSpecList" : [{ 
      "skuId" : 1946,
      "size" : "L",
      "num" : 10,
      "type" : 2,
      "sizeTips" : [{
          "name" : "尺码",
          "value" : "L"
        }, {
          "name" : "号型",
          "value" : "180/90A"
        }, {
          "name" : "体重范围",
          "value" : "45"
        }, {
          "name" : "肩宽",
          "value" : "45"
        }, {
          "name" : "胸围",
          "value" : "90"
        }, {
          "name" : "衣长",
          "value" : "45"
        }, {
          "name" : "袖长",
          "value" : "45"
        }
      ]
    }, {
      "skuId" : 1947,
      "size" : "XL",
      "num" : 10,
      "type" : 1,
      "sizeTips" : [{
          "name" : "尺码",
          "value" : "XL"
        }, {
          "name" : "号型",
          "value" : "185/95A"
        }, {
          "name" : "体重范围",
          "value" : "50"
        }, {
          "name" : "肩宽",
          "value" : "50"
        }, {
          "name" : "胸围",
          "value" : "95"
        }, {
          "name" : "衣长",
          "value" : "50"
        }, {
          "name" : "袖长",
          "value" : "50"
        }
      ]
    }, {
      "skuId" : 1948,
      "size" : "XL",
      "num" : 20,
      "type" : 1,
      "sizeTips" : [{
          "name" : "尺码",
          "value" : "XL"
        }, {
          "name" : "号型",
          "value" : "185/95A"
        }, {
          "name" : "体重范围",
          "value" : "50"
        }, {
          "name" : "肩宽",
          "value" : "50"
        }, {
          "name" : "胸围",
          "value" : "95"
        }, {
          "name" : "衣长",
          "value" : "50"
        }, {
          "name" : "袖长",
          "value" : "50"
        }
      ]
    }, {
      "skuId" : 1949,
      "size" : "XL",
      "num" : 0,
      "type" : 3,
      "sizeTips" : [{
          "name" : "尺码",
          "value" : "XL"
        }, {
          "name" : "号型",
          "value" : "185/95A"
        }, {
          "name" : "体重范围",
          "value" : "50"
        }, {
          "name" : "肩宽",
          "value" : "50"
        }, {
          "name" : "胸围",
          "value" : "95"
        }, {
          "name" : "衣长",
          "value" : "50"
        }, {
          "name" : "袖长",
          "value" : "50"
        }
      ]
    }
  ],
  "productDetail" : [{
      "name" : "季节",
      "value" : "春"
    }, {
      "name" : "洗涤/使用说明",
      "value" : "阿斯顿发"
    }, {
      "name" : "配件备注",
      "value" : "阿斯顿发"
    }, {
      "name" : "商品其他属性",
      "value" : "阿斯顿发"
    }, {
      "name" : "材质/成分",
      "value" : "爱的色放"
    }, {
      "name" : "售后说明",
      "value" : "爱的色放"
    }
  ],
  "productSize" : { 
    "header" : [{
        "id" : 1,
        "name" : "尺码",
        "unit" : "",
        "required" : true
      }, {
        "id" : 2,
        "name" : "号型",
        "unit" : "",
        "required" : false
      }, {
        "id" : 3,
        "name" : "体重范围",
        "unit" : "kg",
        "required" : false
      }, {
        "id" : 4,
        "name" : "肩宽",
        "unit" : "cm",
        "required" : false
      }, {
        "id" : 5,
        "name" : "胸围",
        "unit" : "cm",
        "required" : false
      }, {
        "id" : 6,
        "name" : "衣长",
        "unit" : "cm",
        "required" : false
      }, {
        "id" : 7,
        "name" : "袖长",
        "unit" : "cm",
        "required" : false
      }
    ],
    "body" : [["L", "180/90A", "45", "45", "90", "45", "45"], ["XL", "185/95A", "50", "50", "95", "50", "50"]]
  },
  "prodShowPicList" : ["http://10.240.140.238:8000/res/images/temp/demo.png","http://10.240.140.238:8000/res/images/temp/demo.png"],
  "customEditHTML" : "dajdjaslkdjskadjakld",
  "poCountDownTime" : 1196119115 
}
/>


<#assign activity = {
  "desp":"活动描述", 
  "tipList":[
      { "desp": "红包描述", "name": "红包"},
      { "desp": "减免描述", "name": "红包2"},
      { "desp": "优惠卷描述", "name": "红包3"},
      { "desp": "描述", "name": "红包4"}
  ], 
  "countDown": 1196119115
}
/>


<#assign colors = [{
    "thumb" : "http://10.240.140.238:8000/res/images/temp/demo.png",
    "productURL" : "http://xxx?pid=xxx",
    "productId": 124
  }, {
    "thumb" : "http://10.240.140.238:8000/res/images/temp/demo.png",
    "productURL" : "http://xxx?pid=xxx",
    "productId": 123
  }
]>