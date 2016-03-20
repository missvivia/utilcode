/*
	* get view width set font-size
*/
function setFontSize() {
	var wrapWidth = $(".wrap").width(),
		fontSize = (wrapWidth / 640) * 50;
	$("html").css("font-size", fontSize + "px");
}
setFontSize();

/* banner */
var banner      = $("#banner")
	bannerImg   = banner.find(".img ul"),
	bannerBtn   = banner.find(".btn ul"),
	bannerLen 	= bannerImg.find("li").length;

$(".banner .btn li").eq(0).addClass("active");

bannerImg.on("click", "li", function () {
    var link = $(this).data("link");
    window.location.href = link;
});

var bannerEl    = document.querySelector("#banner"),
    bannerHam   = new Hammer(bannerEl),
    wrapWidth   = $(".wrap").width(),
    flag        = 0;

bannerHam.on("swipeleft", function() {
    if (!bannerImg.is(":animated")) {
        flag++;
        if (flag >= bannerLen) {
            flag = bannerLen - 1;
        } else {
            bannerBtn.find("li").eq(flag).addClass("active").siblings().removeClass("active");
            bannerImg.animate({
                left : -wrapWidth * flag
            }, 1000);
        }
    }
});

bannerHam.on("swiperight", function() {
    if (!bannerImg.is(":animated")) {
        flag--;
        if (flag < 0) {
            flag = 0;
        } else {
            bannerBtn.find("li").eq(flag).addClass("active").siblings().removeClass("active");
            bannerImg.animate({
                left : -wrapWidth * flag
            }, 1000);
        }
    }
});

/* hot module */
var len = $(".hot .list li").length;
$(".hot .list").css({
	width: len * 3.8 + "rem"
});

