//(function() {
//	var myScroll, nav = document.getElementById('nav'),
//		navNode = document.getElementsByTagName('li')[0],
//		conNode = document.getElementById('l1');
//	nav.addEventListener('click', function(e){
//		if(navNode == e.target)return;
//		navNode.className = '';
//		conNode.style.display = 'none';
//		navNode = e.target;
//		conNode = document.getElementById(navNode.attributes.name.nodeValue);
//		navNode.className = 'on';
//		conNode.style.display = 'block';
//		if (myScroll) {
//			myScroll.scrollTo(0,0,100);//�������ö���
//			setTimeout(function () { myScroll.refresh() }, 0);//ˢ�¹���������
//		}
//	},true);
//	function loaded() {
//		document.addEventListener('touchmove', function (e) { 
//			e.preventDefault(); 
//		}, false);
//		myScroll = new iScroll('wrapper');
//	}
//	document.addEventListener('DOMContentLoaded', function () { 
//		setTimeout(loaded, 100); 
//	}, false);
//})();
(function() {
	var myScroll;
	function loaded() {
		document.addEventListener('touchmove', function (e) { 
			e.preventDefault(); 
		}, false);
		myScroll = new iScroll('wrapper');
	}
	document.addEventListener('DOMContentLoaded', function () { 
		setTimeout(loaded, 100); 
	}, false);
})();