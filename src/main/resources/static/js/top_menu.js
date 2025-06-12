// top_nav
$(document).ready(function() {	
	$('#top_nav li').hover(function() {
		$('ul', this).slideDown(200);
		$(this).children('a:first').addClass("hov");
	}, function() {
		$('ul', this).slideUp(100);
		$(this).children('a:first').removeClass("hov");		
	});

	pfMenu = $(".menu_pane_wrap");
	pfBtn = $("#pfBtn");
	pfWrap = $(".menu_pane_content");
	m = 0;  
	
	/* 닫기 */
	function navClose() { 
		$("body").removeClass("open");
		pfMenu.removeClass("nav-active");
		pfBtn.removeClass("active");
		$('.menu_pane_content ul li').animate({"margin-top":"50px","opacity":"0"},500,'easeOutQuart');
		pfWrap.fadeOut(250).animate({"opacity":"0"});
	}
	
	pfBtn.click(function(){
		m++; 
		if(m%2 == 1){ 
			/* 열기 */
			$("body").addClass("open");
			pfMenu.addClass("nav-active");
			pfBtn.addClass("active");
			$('.menu_pane_content ul li').delay(600).animate({"margin-top":"0","opacity":"1"},800,'easeOutQuart');
			pfWrap.show(250).animate({"opacity":"1"},800,'easeOutQuart'); 	
		}else{
			navClose(); 
		}; 
	});
});