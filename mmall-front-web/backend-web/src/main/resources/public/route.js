module.exports = {
	"GET /home/:id": function(req, res, next){
		res.send( "haha" + req.params.id + 'xxdadsdad')
	},
  "GET /sell/invoiceSearch": function(req, res, next){
    var len = 20, arr = [];
    while(len--){
      arr.push({
          orderId: len,
          userId: len,
          consigneeName:"consigneeName" + len,
          consigneePhone:"consigneePhone" + len,
          title:"title" + len,
          fullAddress:"fullAddress" + len,
          cash:"cash" + len,
          goods:"goods" + len,
          orderDate:"orderDate" + len,
          barCode:"barCode" + len,
          orderDate: new Date(),
          expressCompanyName:"expressCompanyName" + len
      })
    }
    res.send({
      code: 200,
      result: {
        total: 201,
        list: arr
      }
    })
  },
  "POST /sell/invoice/batchSubmit": function(req, res){
    res.send({
      code: 200
    })
  }
}