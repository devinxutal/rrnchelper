// ==UserScript==
// @name           suningtools
// @namespace      www.erepublik.com
// @description    tools for suning
// @include        http://www.erepublik.com/*
// ==/UserScript==

var cssNode = document.createElement('style');
cssNode.setAttribute('type', "text/css");
cssNode.innerHTML = 
	'.suninglink:hover{color:#fff !important; background-color:#7EC3DB;}' +
	'ul.suning{height: auto !important; overflow:hidden; background-color:#E9F5FA}'+
	'.suningdonatelink{position: relative; top: 5px; display: inline; width: 25px; height: 25px;}';
document.getElementsByTagName('head').item(0).appendChild(cssNode);


var accounts = new Array();
accounts[0] = new Array('Devin XU', 3010076, 'http://www.erepublik.com/en/citizen/profile/3010076');
accounts[1] = new Array('SUNING UNIVERSAL', 3060005, 'http://www.erepublik.com/en/organization/3060005');
accounts[2] = new Array('SUNING MART', 3148944, 'http://www.erepublik.com/en/organization/3148944' );
accounts[3] = new Array('SUNING INVESTMENT', 3428299, 'http://www.erepublik.com/en/organization/3428299');

var ul = document.createElement("ul");
ul.className = "suning";

for(var i = 0; i <accounts.length; i++){

	var li = document.createElement("li");

	var plink = createlink(accounts[i][0], accounts[i][2]);
	customizeNameLinkInNav(plink);

	var dlink = createlink("donate", "http://www.erepublik.com/en/citizen/donate/items/"+accounts[i][1]);
	customizeDonateLinkInNav(dlink);

	li.appendChild(plink);
	li.appendChild(dlink);
	ul.appendChild(li);

}
document.getElementById("menu").appendChild(ul);

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
	var ind = thisLink.href.lastIndexOf('/');
	uid = thisLink.href.substring(ind+1);
	var dlink = createDonateLink('donate','http://www.erepublik.com/en/citizen/donate/items/'+uid);
	//customizeLinkInMailBox(dlink);
	thisLink.parentNode.appendChild(document.createElement('br'));
	thisLink.parentNode.appendChild(dlink);
}

function customizeLinkInMailBox(link){
	link.setAttribute('style', 'font-size:10px;display:block;clear:both;color: #fa4;padding-top:10px;');
}
function customizeDonateLinkInNav(link){
	link.setAttribute('style', 'font-size:10px;display:block;clear:both;color: #fa4; padding:3px; margin:3px; height:auto;');
}
function customizeNameLinkInNav(link){
	link.setAttribute('style', 'font-size:12px;display:block;clear:both;color: #3C8FA7; padding:3px; margin:3px; height:auto;');
}
function createlink(text, url){
	var link = document.createElement("a");
	link.className = "suninglink";
	link.innerHTML = text;
	link.href = url;
	link.setAttribute('target', 'blank');
	return link;
}
function createDonateLink(text, url){
	var link = document.createElement("a");
	link.className = "suningdonatelink";
	link.innerHTML = '<img width="18px" height="16px" onclick="" src="/images/parts/gold-to.gif" alt="Donate">'
	link.href = url;
	link.setAttribute('target', 'blank');
	link.setAttribute('title', 'Donate');
	return link;
}

