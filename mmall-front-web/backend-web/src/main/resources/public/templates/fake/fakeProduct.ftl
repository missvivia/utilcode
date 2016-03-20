<#if RequestParameters.id??>
  <#assign id=RequestParameters.id/>
</#if>

<#if id?? && id=="2">
<#assign productVO = {
  "categoryList" : [
  {
    "id" : 10,
    "name" : "10",
    "list" : [
      {
        "id" : 100,
        "name" : "100",
        "list": [
          {
            "id" : 1100,
            "name" : "1100"
          },
          {
            "id" : 2200,
            "name" : "22100"
          },
          {
            "id" : 3100,
            "name" : "3100"
          },
          {
            "id" : 4200,
            "name" : "4100"
          }
        ]
      },
      {
        "id" : 200,
        "name" : "200",
        "list": [
          {
            "id" : 100,
            "name" : "xx100"
          },
          {
            "id" : 200,
            "name" : "xx100"
          }
        ]
      }
    ]
    },
  {
    "id" : 20,
    "name" : "20",
    "list" : [
      {
        "id" : 200,
        "name" : "200",
        "list": [
          {
            "id" : 2200,
            "name" : "2200"
          },
          {
            "id" : 3200,
            "name" : "3200"
          },
          {
            "id" : 3300,
            "name" : "3300"
          },
          {
            "id" : 3400,
            "name" : "3400"
          }
        ]
      },
      {
        "id" : 200,
        "name" : "xx100",
        "list": [
          {
            "id" : 500,
            "name" : "xx100"
          },
          {
            "id" : 600,
            "name" : "xx100"
          }
        ]
      }
    ]
    }
  ],
  "brandList": [
    {
      "id": 100,
      "name": "200"
    },
    {
      "id": 200,
      "name": "220"
    }
  ],
  "categories": [10,200, 500],
  "goodsNo" : "12345",
  "productName": "xxx",
  "brandId": 123,
  "addedTax": 17,
  "descp": "hello1",
  "colorNum": 100,
  "colorName": "红色",

  "skuList" : [{
    "id": 111,
    "barCode" : "xxx",
    "body" : ["xxx", "xxx", "xxx"]
  }],
  "skuList2" : [{
    "id": 100,
    "barCode" : "xxx",
    "sizeId": 100
  }],
  "sizeType": 3,
  "sizeTemplateId": -1, 
  "template": {
    "sizeTemplate": [
      {
        "id": 100,
        "name": "女码小号",
        "list": [
          {
            "id": 100,
            "showName": "(号型160/80A 胸围100 肩宽34 袖长56)",
            "size": "S"
          },
          {
            "id": 200,
            "showName": "M(号型160/80A 胸围100 肩宽34 袖长56)",
            "size": "M"
          }
        ]
      },
      {
        "id": 200,
        "name": "女码中号",
        "list": [
          {
            "id": 300,
            "size": "S",
            "showName": "L(号型160/80A 胸围100 肩宽34 袖长56)"
          },
          {
            "id": 400,
            "size": "M",
            "showName": "XL(号型160/80A 胸围100 肩宽34 袖长56)"
          }
        ]
      } 
    ],
    "sizeHeader" : [
    {
      "id" : 111,
      "name" : "xxx",
      "isRequired" : true
    },
    {
      "id" : 112,
      "name" : "xxx2",
      "isRequired" : true
    },
    {
      "id" : 113,
      "name" : "xxx3",
      "isRequired" : true
    }
    ]
  },
  "marketPrice": 10,
  "salePrice": 100,
  "basePrice": 1000,
  "customEditHTML": "<p>默认初始止</p>",
  "prodShowPicList ": ["/res/defaults/screen.png", "/res/defaults/screen.png","/res/defaults/screen.png"],
  "listShowPicList ": ["/res/defaults/screen.png", "/res/defaults/screen.png","/res/defaults/screen.png"],
  "detailShowPicList ": ["/res/defaults/screen.png", "/res/defaults/screen.png","/res/defaults/screen.png"],
  "productParamList" : [
    {
    "id" : 10,
    "type" : 1,
    "value": 1000,
    "name": "女装",
    "isRequired":true,
    "list" : [{
        "optId" : 1000,
        "optValue" : "女装1"
      },
      {
        "optId" : 1001,
        "optValue" : "女装2"
      }
      ]
    },{
    "id" : 20,
    "name": "男装",
    "type" : 4,
    "value": '[2000, 2001]',
    "isRequired":false,
    "list" : [{
        "optId" : 2000,
        "optValue" : "男装1"
      },
      {
        "optId" : 2001,
        "optValue" : "男装2"
      },
      {
        "optId" : 2002,
        "optValue" : "男装3"
      }
      ]
    },
    {
    "id" : 30,
    "name": "男装2",
    "type" : 2,
    "value": "dadakda",
    "isRequired":true
    },
    {
    "id" : 40,
    "name": "男装3",
    "value": "dadakda",
    "type" : 3,
    "isRequired":false
    }
  ]
}
  
>

<#else>
<#assign productVO = {
  "brandName": "委会",
  "categoryList" : [
  {
    "id" : 20,
    "name" : "xx1",
    "list" : [
      {
        "id" : 200,
        "name" : "xx200",
        "list": [
          {
            "id" : 200,
            "name" : "xx200"
          },
          {
            "id" : 200,
            "name" : "xx200"
          },
          {
            "id" : 200,
            "name" : "xx200"
          },
          {
            "id" : 200,
            "name" : "xx200"
          }
        ]
      },
      {
        "id" :200,
        "name" : "xx200",
        "list": [
          {
            "id" : 300,
            "name" : "xx200"
          },
          {
            "id" : 400,
            "name" : "xx200"
          }
        ]
      }
    ]
    }
  ],
  "template": {
    "sizeTemplate": [
      {
        "id": 200,
        "name": "女码小号",
        "list": [
          {
            "id": 200,
            "showName": "S(号型160/80A 胸围200 肩宽34 袖长56)",
            "size": "S"
          },
          {
            "id": 200,
            "showName": "M(号型160/80A 胸围200 肩宽34 袖长56)",
            "size": "S"
          }
        ]
      },
      {
        "id": 200,
        "name": "女码中号",
        "list": [
          {
            "id": 300,
            "showName": "L(号型160/80A 胸围200 肩宽34 袖长56)"
          },
          {
            "id": 400,
            "showName": "XL(号型160/80A 胸围200 肩宽34 袖长56)"
          }
        ]
      } 
    ],
    "defaultTemplate": {
      "id": 200,
      "name": "默认尺寸模板",
      "list": [
        {
          "id": 300,
          "showName": "L(号型160/80A 胸围100 肩宽34 袖长56)"
        },
        {
          "id": 400,
          "showName": "XL(号型160/80A 胸围100 肩宽34 袖长56)"
        }
      ]
    },
    "sizeHeader" : [
    {
      "id" : 111,
      "name" : "xxx",
      "isRequired" : true
    },
    {
      "id" : 112,
      "name" : "xxx2",
      "isRequired" : true
    },
    {
      "id" : 113,
      "name" : "xxx3",
      "isRequired" : true
    }
    ]
  },
  "productParamList" : [
    {
    "id" : 20,
    "type" : 1,
    "name": "女装",
    "isRequired":true,
    "list" : [{
        "optId" : 2000,
        "optValue" : "女装1"
      },
      {
        "optId" : 2001,
        "optValue" : "女装2"
      }
      ]
    },{
    "id" : 20,
    "name": "大便",
    "value": [2000,2001,2002],
    "type" : 4,
    "isRequired":true,
    "list" : [{
        "optId" : 2000,
        "optValue" : "男装1"
      },
      {
        "optId" : 2001,
        "optValue" : "男装2"
      },
      {
        "optId" : 2002,
        "optValue" : "男装3"
      }
      ]
    },
    {
    "id" : 30,
    "name": "男装2",
    "type" : 2,
    "isRequired":true
    },
    {
    "id" : 40,
    "name" : "name3",
    "type" : 3,
    "isRequired":false,
    "defaultValue": "默认初始值"
    }
  ]
}
  
>

</#if>


