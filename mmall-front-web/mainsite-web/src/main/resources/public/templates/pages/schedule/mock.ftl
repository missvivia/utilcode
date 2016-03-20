<#assign leftTime = 587406584 />
<#assign layout = {
    "id":1,
    "brandId":3,
    "scheduleId":2,
    "udImgIds": "11,12,13,14,16,15,4,5",
    "udProductIds": "1,2,3,4,5,6,7,8",
    "udSetting": "[{\"hotspots\":[],\"imgId\":\"11\",\"height\":621,\"spaceTop\":410,\"id\":\"1412859370456\",\"type\":\"2\"},{\"hotspots\":[],\"imgId\":\"12\",\"height\":1195,\"spaceTop\":115,\"id\":\"1412859370491\",\"type\":\"2\"},{\"height\":60,\"spaceTop\":78,\"textContent\":\"文字自定义分类条\",\"textAlign\":\"center\",\"fontFamily\":\"Arial\",\"fontWeight\":\"normal\",\"fontSize\":20,\"fontColor\":\"rgb(0, 0, 0)\",\"bgColor\":\"rgb(229, 229, 229)\",\"opacity\":100,\"id\":\"1412859370524\",\"type\":\"1\"},{\"spaceTop\":10,\"bannerIds\":[\"13\",\"14\"],\"productIds\":[\"\",\"\"],\"id\":\"1412859370545\",\"type\":\"3\"},{\"spaceTop\":10,\"bannerIds\":[\"14\",\"13\"],\"productIds\":[\"\",\"\"],\"id\":\"1412859370595\",\"type\":\"3\"},{\"height\":60,\"spaceTop\":112,\"textContent\":\"文字自定义分类条\",\"textAlign\":\"center\",\"fontFamily\":\"Arial\",\"fontWeight\":\"normal\",\"fontSize\":20,\"fontColor\":\"rgb(0, 0, 0)\",\"bgColor\":\"rgb(229, 229, 229)\",\"opacity\":100,\"id\":\"1412859370581\",\"type\":\"1\"},{\"spaceTop\":10,\"bannerIds\":[\"16\",\"15\",\"4\"],\"productIds\":[\"\",\"\",\"\"],\"id\":\"1412859370629\",\"type\":\"4\"},{\"spaceTop\":10,\"bannerIds\":[\"4\",\"16\",\"15\"],\"productIds\":[\"\",\"\",\"\"],\"id\":\"1412859370672\",\"type\":\"4\"},{\"height\":60,\"spaceTop\":165,\"textContent\":\"文字自定义分类条\",\"textAlign\":\"center\",\"fontFamily\":\"Arial\",\"fontWeight\":\"normal\",\"fontSize\":20,\"fontColor\":\"rgb(0, 0, 0)\",\"bgColor\":\"rgb(229, 229, 229)\",\"opacity\":100,\"id\":\"1412859370717\",\"type\":\"1\"},{\"spaceTop\":10,\"bannerIds\":[\"5\"],\"productIds\":[\"1\",\"2\",\"3\"],\"id\":\"1412859370737\",\"type\":\"5\"},{\"spaceTop\":10,\"bannerIds\":[\"5\"],\"productIds\":[\"4\",\"5\",\"6\"],\"id\":\"1412859370789\",\"type\":\"5\"},{\"height\":60,\"spaceTop\":125,\"textContent\":\"文字自定义分类条\",\"textAlign\":\"center\",\"fontFamily\":\"Arial\",\"fontWeight\":\"normal\",\"fontSize\":20,\"fontColor\":\"rgb(0, 0, 0)\",\"bgColor\":\"rgb(229, 229, 229)\",\"opacity\":100,\"id\":\"1412859370842\",\"type\":\"1\"},{\"spaceTop\":10,\"bannerIds\":[],\"productIds\":[\"7\",\"8\",\"6\",\"3\"],\"id\":\"1412859370856\",\"type\":\"6\"},{\"spaceTop\":10,\"bannerIds\":[],\"productIds\":[\"1\",\"2\",\"3\",\"6\"],\"id\":\"1412859370908\",\"type\":\"6\"}]",
    "bgImgId": "1",
    "bgSetting": "{\"bgColor\":\"#F5F5F5\"}",
    "headerImgId": "2",
    "headerSetting": "{\"top\":0,\"left\":421,\"fontFamily\":\"Arial\",\"fontWeight\":\"normal\",\"fontSize\":20,\"fontColor\":\"rgb(0, 0, 0)\",\"bgColor\":\"rgb(255, 255, 255)\",\"opacity\":50}",
    "allListPartVisiable": true,
    "allListPartOthers": "{\"sortList\":[],\"sortType\":0,\"bgColor\":\"rgb(229, 229, 229)\",\"opacity\":100,\"spaceTop\":70}",
    "mapPartVisiable": true,
    "mapPartOthers": "{}"
}/>
<#assign shops = [
    {
        "id":1,
        "shopName": "杭州金桥家乐福店",
        "shopTel": "50305310",
        "shopZone": "0571",
        "shopContact": "张三",
        "city": "杭州市",
        "district": "滨江区",
        "shopAddr": "金桥碧云路555号金桥家乐福1楼",
        "longitude": 116.3786889372559,
        "latitude": 39.90762965106183
    },
    {
        "id":2,
        "shopName": "杭州金桥家乐福店",
        "shopTel": "50305310",
        "shopZone": "0571",
        "shopContact": "张三",
        "city": "杭州市",
        "district": "滨江区",
        "shopAddr": "金桥碧云路555号金桥家乐福1楼",
        "longitude": 116.38632786853032,
        "latitude": 39.90795884517671
    },
    {
        "id":3,
        "shopName": "杭州金桥家乐福店",
        "shopTel": "50305310",
        "shopZone": "0571",
        "shopContact": "张三",
        "city": "杭州市",
        "district": "滨江区",
        "shopAddr": "金桥碧云路555号金桥家乐福1楼",
        "longitude": 116.39534009082035,
        "latitude": 39.907432133833574
    },
    {
        "id":4,
        "shopName": "杭州金桥家乐福店",
        "shopTel": "50305310",
        "shopZone": "0571",
        "shopContact": "张三",
        "city": "杭州市",
        "district": "滨江区",
        "shopAddr": "金桥碧云路555号金桥家乐福1楼",
        "longitude": 116.40624058825688,
        "latitude": 39.90789300648029
    },
    {
        "id":5,
        "shopName": "杭州金桥家乐福店",
        "shopTel": "50305310",
        "shopZone": "0571",
        "shopContact": "张三",
        "city": "杭州市",
        "district": "滨江区",
        "shopAddr": "金桥碧云路555号金桥家乐福1楼",
        "longitude": 116.41413701159672,
        "latitude": 39.90795884517671
    }
]/>
<#assign images = {
    "0": {
        "width": 1920,
        "height": 687,
        "imgUrl": "/res/test/0.png",
        "imgName": "image imgName 0",
        "imgId": 0
    },
    "1": {
        "width": 1920,
        "height": 700,
        "imgUrl": "/res/test/1.png",
        "imgName": "image imgName 1",
        "imgId": 1
    },
    "2": {
        "width": 1090,
        "height": 390,
        "imgUrl": "/res/test/2.png",
        "imgName": "image imgName 2",
        "imgId": 2
    },
    "4": {
        "width": 352,
        "height": 445,
        "imgUrl": "/res/test/4.jpg",
        "imgName": "image imgName 4",
        "imgId": 4
    },
    "5": {
        "width": 283,
        "height": 426,
        "imgUrl": "/res/test/5.jpg",
        "imgName": "image imgName 5",
        "imgId": 5
    },
    "6": {
        "width": 254,
        "height": 320,
        "imgUrl": "/res/test/6.png",
        "imgName": "image imgName 6",
        "imgId": 6
    },
    "11": {
        "width": 1090,
        "height": 621,
        "imgUrl": "/res/test/11.png",
        "imgName": "image imgName 11",
        "imgId": 11
    },
    "12": {
        "width": 1090,
        "height": 1195,
        "imgUrl": "/res/test/12.png",
        "imgName": "image imgName 12",
        "imgId": 12
    },
    "13": {
        "width": 537,
        "height": 370,
        "imgUrl": "/res/test/13.jpg",
        "imgName": "image imgName 13",
        "imgId": 13
    },
    "14": {
        "width": 537,
        "height": 370,
        "imgUrl": "/res/test/14.jpg",
        "imgName": "image imgName 14",
        "imgId": 14
    },
    "15": {
        "width": 352,
        "height": 445,
        "imgUrl": "/res/test/15.jpg",
        "imgName": "image imgName 15",
        "imgId": 15
    },
    "16": {
        "width": 352,
        "height": 445,
        "imgUrl": "/res/test/16.jpg",
        "imgName": "image imgName 16",
        "imgId": 16
    },
    "17": {
        "width": 283,
        "height": 426,
        "imgUrl": "/res/test/17.jpg",
        "imgName": "image imgName 17",
        "imgId": 17
    }
 }/>
<#assign products = {
    "1": {
        "id": 1,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 100,
            "cartCount": 30
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "2": {
        "id": 2,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "3": {
        "id": 3,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "4": {
        "id": 4,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 100,
            "cartCount": 30
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "5": {
        "id": 5,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 100,
            "cartCount": 30
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "6": {
        "id": 6,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 100,
            "cartCount": 30
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "7": {
        "id": 7,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 100,
            "cartCount": 30
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "8": {
        "id": 8,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 100,
            "cartCount": 30
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "9": {
        "id": 9,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 100,
            "cartCount": 30
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "10": {
        "id": 10,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 100,
            "cartCount": 30
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "11": {
        "id": 11,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 100,
            "cartCount": 30
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "12": {
        "id": 12,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "13": {
        "id": 13,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "14": {
        "id": 14,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 100,
            "cartCount": 30
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "15": {
        "id": 15,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 100,
            "cartCount": 30
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "16": {
        "id": 16,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 100,
            "cartCount": 30
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "17": {
        "id": 17,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 100,
            "cartCount": 30
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "18": {
        "id": 18,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 100,
            "cartCount": 30
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "19": {
        "id": 19,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 100,
            "cartCount": 30
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    },
    "20": {
        "id": 20,
        "productName": "英伦格纹拼接显瘦双排扣风衣",
        "brandId": 2,
        "brandName": "EIFINI 伊芙丽",
        "colorName": "红色",
        "salePrice": 200,
        "marketPrice": 1200,
        "listShowPicList": ["/res/test/zm.jpg", "/res/test/fm.jpg"],
        "skuList": [{
            "id": 1,
            "size": "M",
            "num": 200,
            "soldCount": 100,
            "cartCount": 30
        }, {
            "id": 2,
            "size": "L",
            "num": 200,
            "soldCount": 100,
            "cartCount": 100
        }, {
            "id": 3,
            "size": "XL",
            "num": 200,
            "soldCount": 200,
            "cartCount": 0
        }]
    }
 }/>

