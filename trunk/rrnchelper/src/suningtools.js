// ==UserScript==
// @name           suningtools
// @namespace      www.erepublik.com
// @description    tools for suning
// @include        http://www.erepublik.com/*
// ==/UserScript==


var accounts = new Array();
accounts[0] = new Array('Devin XU', 3010076);
accounts[1] = new Array('SUNING UNIVERSAL', 3060005 );
accounts[2] = new Array('SUNING MART', 3148944);
accounts[3] = new Array('SUNING INVESTMENT', 3428299);

for(var i = 0; i <accounts.length; i++){
	;
}
var allLinks, thisLink, uid, ver;
ver = "0.14";

allLinks = document.evaluate(
	"//div[@class='nameholder']/a[@class='dotted']",
	document,
	null,
	XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE,
	null);
for (var i = 0; i < allLinks.snapshotLength; i++) {
	thisLink = allLinks.snapshotItem(i);
	var dlink = document.createElement("a");
	var ind = thisLink.href.lastIndexOf('/');
	uid = thisLink.href.substring(ind+1);
	dlink.href = "http://www.erepublik.com/en/citizen/donate/items/"+uid;
	dlink.setAttribute('target', 'blank');
	dlink.innerHTML = "donate";
	dlink.setAttribute('style', 'font-size:10px;display:block;clear:both;color: #fa4;padding-top:10px;')
	thisLink.parentNode.appendChild(dlink);
}
