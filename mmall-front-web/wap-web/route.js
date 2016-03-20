
module.exports = {
	"/a/**": 'index.html',
	'GET /home': function(req, res, next){
		res.send('hello/home')
	}
	
}
