// 空格前代表 请求mthod  后代表path
module.exports = {
  //尝试  http://localhost:8000/xx/list/1 访问
  "/xx/list/:id": function(req, res, next){
    var query = req.query // ?之后的参数列表
    var params = req.params // 在url里的参数
    res.json({
      code: 200,
      result: []
    })
  },
  // 再比如POST创建操作
  "POST /xx/product": function(req, res, next){

    res.json({
      code: 200,
      result: "我是创建"
    })
  },
  // PUT 更新操作
  "PUT /xx/product/:id": function(req, res, next){
    res.json({
      code: 200,
      result: "我是更新"
    })
  },
  // DELETE 删除操作
  "DELETE /xx/product/:id": function(){
    res.json({
      code: 200,
      result: "我是删除"
    })
  },
  "GET /rest/s"
}