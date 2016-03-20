<#if RequestParameters.id??>
  <#assign id=RequestParameters.id/>
</#if>

<#if id??>
<#assign activityVO = {
  "name": "活动名称",
  "desc": "活动描述",
  "startTime": 1411047423442,
  "endTime": 1411047423442,
  "labels": [
    {"desc": "红包描述", "type": 1, "select": true },
    {"desc": "减免描述", "type": 2},
    { "desc": "优惠卷描述", "type": 3},
    {"desc": "描述", "type": 4}
  ],
  "selectedProvince": ["1", "2", "4"],
  "provinceList": [
    {"code": "1", "name": "站点1"},
    {"code": "2", "name": "站点2"},
    {"code": "3", "name": "站点3"},
    {"code": "4", "name": "站点4"}
  ],
  "items": [
    {
      "id":100,
      "schedules":[
      {
        "id": 1000,
        "province":{
          "code": "100",
          "name": "浙江省"
        },
        "brand":{
          "id": 1000,
          "brandNameZh": "春水鸟人"
        },
        "startTime": 1411047423442,
        "endTime": 1411047423442,
        "title": "活动名称"
      }
      ],
      "condition": {
        "type": 1,
        "value": "1000-"
      }, 
      "result": [
        { 
          "type": 1,
          "value": "xxx"
        }
      ]
    },
    {
      "id": 100,
      "schedules":[{
        "id": 1000,
        "province":{
          "code": "100",
          "name": "浙江省"
        },
        "brand":{
          "id": 1000,
          "brandNameZh": "秋水伊人"
        },
        "startTime": 1411047423442
      }],
      "condition": {
        "type": 1,
        "value": "1-3"
      }, 
      "result": [{
        "type": 4,
        "value": {"id": 1000, "name": "哈哈"}
      }]
    }
  ]
  }
>

<#else>
  <#assign activityVO = {
    "labels": [
      { "desc": "红包描述", "type": 1},
      { "desc": "减免描述", "type": 2},
      { "desc": "优惠卷描述", "type": 3},
      { "desc": "描述", "type": 4}
    ],
    "provinceList": [
      {"code": "1", "name": "站点1"},
      {"code": "2", "name": "站点2"},
      {"code": "3", "name": "站点3"},
      {"code": "4", "name": "站点4"}
    ]
  } />
</#if>



