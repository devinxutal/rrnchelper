// ==UserScript==
// @name           suningtools
// @namespace      www.erepublik.com
// @description    tools for suning
// @include        http://www.erepublik.com/*
// ==/UserScript==



// set accounts;
var accounts = new Array();
accounts[0] = new Array('Devin XU', 3010076, 'http://www.erepublik.com/en/citizen/profile/3010076');
accounts[1] = new Array('SUNING UNIVERSAL', 3060005, 'http://www.erepublik.com/en/organization/3060005');
accounts[2] = new Array('SUNING MART', 3148944, 'http://www.erepublik.com/en/organization/3148944' );
accounts[3] = new Array('SUNING INVESTMENT', 3428299, 'http://www.erepublik.com/en/organization/3428299');

var exchanges = new Array();
exchanges.push(new Array('GOLD', 'CNY'));
exchanges.push(new Array('GOLD', 'GBP'));
//friends
var friendsToTrack = new Array();
friendsToTrack.push(3471797);
friendsToTrack.push(3053396);

addCss();
addToolbox();
addQuickLinks();
addFriendTracks();
if (window.location.href == 'http://www.erepublik.com/en') {
	addExchangeRates();
}
addDonateLinksInMailBox();


function addCss(){
	var cssNode = document.createElement('style');
	cssNode.setAttribute('type', "text/css");
	cssNode.innerHTML = '.suninglink:hover{color:#fff !important; background-color:#7EC3DB;}' +
	
	'ul.suning{height: auto !important; overflow:hidden; background-color:#E9F5FA}' +
	'.suningdonatelink{position: relative; top: 1px;display: inline; width: 25px; height: 25px;}' +
	'.suningdonatelink{position: relative; top: 1px;display: inline; width: 25px; height: 25px;}' +
	'.suning_toolbox{background-color:#fff; border: 1px solid #7EC3DB; position:fixed; right:10px; top:220px;}' +
	'.suning_toolbox li{padding-left:10px; padding-right:10px;border-bottom:1px dashed #E9F5FA;}' +
	'.suning_toolbox .namelink{ width:150px; display:inline-block; height:auto;font-size:12px; padding:3px;}' +
	'.suning_toolbox .namelink:hover{color:#d95 !important; }' +
	'.suning_toolbox a{padding:0px; }' +
	'.suning_toolbox a:hover{color:#d95; }' +
	'.suning_toolbox a img{height:10px; width: 12px; margin-right:5px;}' +
	'.suning_toolbox .section_title{background-color:#E9F5FA; color: #90BCC9; font-size:14px; font-weight:bold; text-align:center;display:block; padding:3px;}'+
	'.suning_toolbox table{color:#90BCC9; font-size:10px;}'+
	'.suning_toolbox td{border-bottom:dotted 1px #E9F5FA; padding:2px; color:#3C8FA7;}'+
	'.suning_toolbox table thead th{border-bottom:solid 1px #E9F5FA; padding:2px; font-weight:bold; color:#3C8FA7;}';
	
	
	document.getElementsByTagName('head').item(0).appendChild(cssNode);
}

function addQuickLinks(){
	var ul = document.createElement("ul");
	//ul.className = "suning";
	
	for (var i = 0; i < accounts.length; i++) {
	
		var li = document.createElement("li");
		
		var plink = createNameLink(accounts[i][0], accounts[i][2]);
		var dlink = createDonateLink("Donate", "http://www.erepublik.com/en/citizen/donate/items/" + accounts[i][1]);
		
		li.appendChild(plink);
		li.appendChild(dlink);
		ul.appendChild(li);
	}
	document.getElementById("quick-link").appendChild(ul);
}

function addExchangeRates(){
	document.getElementById('suning-toolbox').appendChild(createSection('exchange-table', 'Exchange Monitor'));
	var tb = document.createElement('table');
	tb.setAttribute('cellpadding','2px');
	tb.setAttribute('cellspacing','1px');
	tb.setAttribute('width','100%');
	var tbody = document.createElement('tbody');
	tb.innerHTML = '<thead><th>Type</th><th>Amount</th><th>Rate</th><th>Account</th><th>Link</th></thead>'
	tb.appendChild(tbody);
	document.getElementById('exchange-table').appendChild(tb);
	for (var i = 0; i < accounts.length; i++) {
		tbody.appendChild(createExchagngeRateEntry(exchanges[i][0],exchanges[i][1]));
		tbody.appendChild(createExchagngeRateEntry(exchanges[i][1],exchanges[i][0]));
	}
}

function createExchagngeRateEntry(buy, sell){	
	var tr = document.createElement('tr');
	
	var sReq = "http://api.erepublik.com/v1/feeds/exchange/"+buy+"/"+sell+".json";
	GM_xmlhttpRequest({
		method: 'GET',
		url: sReq,
		onload: function (json) {
			eval("jsonObj = " + json.responseText);
			var amount = jsonObj[0]["offer"]["value"];
			var rate = jsonObj[0]["offer"]["for"];
			var user = jsonObj[0]["seller"]["name"];
			//type
			var td = document.createElement('td');
			td.innerHTML = buy+'->'+sell;
			tr.appendChild(td);
			//amount
			td = document.createElement('td');
			td.innerHTML = amount;
			tr.appendChild(td);
			//rate
			td = document.createElement('td');
			td.innerHTML = rate;
			tr.appendChild(td);
			//user
			td = document.createElement('td');
			td.innerHTML = user;
			tr.appendChild(td);
			//link
			td = document.createElement('td');
			td.innerHTML = 'none';
			tr.appendChild(td);
		}
	});
	return tr;
}


function addFriendTracks(){
	document.getElementById('suning-toolbox').appendChild(createSection('friends-track', 'Track Friends'));
	var tb = document.createElement('table');
	tb.setAttribute('cellpadding','2px');
	tb.setAttribute('cellspacing','1px');
	tb.setAttribute('width','100%');
	var tbody = document.createElement('tbody');
	tb.innerHTML = '<thead><th>Name</th><th>Lvl.</th><th>Exp.</th><th>Wel.</th><th>Fights</th><th>Go</th></thead>'
	tb.appendChild(tbody);
	document.getElementById('friends-track').appendChild(tb);
	for (var i = 0; i < friendsToTrack.length; i++) {
		tbody.appendChild(createFriendTrackEntry(friendsToTrack[i]));
	}
}

function createFriendTrackEntry(fid){
	var tr = document.createElement('tr');
	
	var sReq = "http://api.erepublik.com/v1/feeds/citizens/"+fid+".json";
	GM_xmlhttpRequest({
		method: 'GET',
		url: sReq,
		onload: function (json) {
			eval("jsonObj = " + json.responseText);
			// name
			var td = document.createElement('td');
			td.innerHTML = '<a href="'+('http://www.erepublik.com/en/citizen/profile/'+fid)+'">'+jsonObj["name"]+'</a>';
			tr.appendChild(td);
			// level
			td = document.createElement('td');
			td.innerHTML = jsonObj["level"];
			tr.appendChild(td);
			// exp
			td = document.createElement('td');
			td.innerHTML = jsonObj["experience_points"];
			tr.appendChild(td);
			// wellness
			td = document.createElement('td');
			td.innerHTML = jsonObj["wellness"];
			tr.appendChild(td);
			// fights
			td = document.createElement('td');
			td.innerHTML = jsonObj["fights"];
			tr.appendChild(td);
			// link
			td = document.createElement('td');
			td.appendChild(createDonateLink('Donate',"http://www.erepublik.com/en/citizen/donate/items/"+fid ));
			td.appendChild(createMessageLink(fid ));
			
			tr.appendChild(td);
		}
	});
	return tr;
}


function addDonateLinksInMailBox(){
	var allLinks, thisLink, uid;
	
	allLinks = document.evaluate("//div[@class='nameholder']/a[@class='dotted']", document, null, XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE, null);
	for (var i = 0; i < allLinks.snapshotLength; i++) {
		thisLink = allLinks.snapshotItem(i);
		var ind = thisLink.href.lastIndexOf('/');
		uid = thisLink.href.substring(ind + 1);
		var dlink = createDonateLink('donate', 'http://www.erepublik.com/en/citizen/donate/items/' + uid);
		
		thisLink.parentNode.appendChild(document.createElement('br'));
		thisLink.parentNode.appendChild(dlink);
	}
}

function customizeLinkInMailBox(link){
	link.setAttribute('style', 'font-size:10px;display:block;clear:both;color: #fa4;padding-top:10px;');
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
function createMessageLink(id){
	var link = document.createElement("a");
	link.className = "suningmsglink";
	link.innerHTML = '<img width="18px" height="16px" onclick="" src="/images/parts/btn-icon_send-message.gif" alt="Donate">'
	link.href = 'http://www.erepublik.com/en/messages/compose/'+id;
	link.setAttribute('target', 'blank');
	link.setAttribute('title', 'Send a message');
	return link;
}


function createNameLink(text, url){
	var link = document.createElement("a");
	link.className = "namelink";
	link.innerHTML = text;
	link.href = url;
	link.setAttribute('target', 'blank');
	return link;
}

function exchangeNotify(){
	var sReq = 'http://api.erepublik.com/v1/feeds/exchange/GOLD/CNY.json';
		GM_xmlhttpRequest({
		method: 'GET',
		url: sReq,
		onload: function (json) {
			eval("jsonObj = " + json.responseText);
			var rate = jsonObj[0]["offer"]["for"];
		}
	});
}

function addToolbox(){
	var div = document.createElement('div');
	div.id = 'suning-toolbox';
	div.className = 'suning_toolbox';
	
	div.appendChild(createSection('quick-link', 'Quick Links'));
	
	
	//add
	document.getElementById('container').parentNode.appendChild(div);
}

function createSection(idstr, name){
	var div = document.createElement('div');
	div.className = 'section';
	
	var span = document.createElement('span');
	span.className = 'section_title';
	span.innerHTML = name;
	div.appendChild(span);
	
	var div1 = document.createElement('div');
	div1.id = idstr;
	div.appendChild(div1);
	return div;
}


