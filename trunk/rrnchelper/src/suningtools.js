// ==UserScript==
// @name           suningtools
// @namespace      www.erepublik.com
// @description    tools for suning
// @include        http://www.erepublik.com/*
// ==/UserScript==

/* current feeds
 * http://api.erepublik.com/v1/feeds/citizens/{USER_ID}
 * http://api.erepublik.com/v1/feeds/citizens/{USER_NAME}?by_username=true
 * http://api.erepublik.com/v1/feeds/countries
 * http://api.erepublik.com/v1/feeds/countries/{COUNTRY_ID}
 * http://api.erepublik.com/v1/feeds/regions/{REGION_ID}
 * http://api.erepublik.com/v1/feeds/companies/COMPANY_ID}
 * http://api.erepublik.com/v1/feeds/market/{INDUSTRY}/{QUALITY}/{COUNTRY}   e.g. http://api.erepublik.com/v1/feeds/market/food/1/china
 * http://api.erepublik.com/v1/feeds/exchange/{BUY}/{SELL}
 * http://api.erepublik.com/v1/feeds/battle_logs/{BATTLE_ID}/{PAGE}
 * http://api.erepublik.com/v1/feeds/war/{WAR_ID}
 */

// set accounts;
var accounts = new Array();
accounts[0] = new Array('Devin XU', 3010076, 'http://www.erepublik.com/en/citizen/profile/3010076');
accounts[1] = new Array('SUNING UNIVERSAL', 3060005, 'http://www.erepublik.com/en/organization/3060005');
accounts[2] = new Array('SUNING MART', 3148944, 'http://www.erepublik.com/en/organization/3148944');
accounts[3] = new Array('SUNING INVESTMENT', 3428299, 'http://www.erepublik.com/en/organization/3428299');
//exchages
var exchanges = new Array();
exchanges.push(new Array('GOLD', 'CNY'));
exchanges.push(new Array('GOLD', 'GBP'));
//friends
var friendsToTrack = new Array();
friendsToTrack.push(3471797);
friendsToTrack.push(3053396);
//goods to monitor
var productsToMonitor = new Array();
productsToMonitor.push(new Array("food", 2));
productsToMonitor.push(new Array("food", 3));
productsToMonitor.push(new Array("food", 4));
productsToMonitor.push(new Array("food", 5));
productsToMonitor.push(new Array("gift", 1));
productsToMonitor.push(new Array("weapon", 1));
var countryToMonitor = new Array();
countryToMonitor.push("china");
countryToMonitor.push("united-kingdom");
countryToMonitor.push("poland");
countryToMonitor.push("argentina");
var EXCHANGE_RATES = new Object;
EXCHANGE_RATES["china"] = 0.02;
EXCHANGE_RATES["united-kingdom"]= 0.026;
EXCHANGE_RATES["poland"] = 0.02;
EXCHANGE_RATES["argentina"] = 0.02;
/////////////////////////////////////
/////////////////////////////////////
addCss();
addToolbox();
addQuickLinks();
addFriendTracks();
addExchangeRates();
addLowestPriceSection();
addAddFriendSection();
addDonateLinksInMailBox();


function addCss(){
    var cssNode = document.createElement('style');
    cssNode.setAttribute('type', "text/css");
    cssNode.innerHTML = '.suninglink:hover{color:#fff !important; background-color:#7EC3DB;}' +
    
    'ul.suning{height: auto !important; overflow:hidden; background-color:#E9F5FA}' +
    '.suningdonatelink{position: relative; top: 1px;display: inline; width: 25px; height: 25px;}' +
    '.suningdonatelink{position: relative; top: 1px;display: inline; width: 25px; height: 25px;}' +
    '.suning_toolbox{color:#3C8FA7; background-color:#fff; border-right: 1px solid #7EC3DB;  border-left: 1px solid #7EC3DB; border-top: 1px solid #7EC3DB;position:fixed; right:10px; top:220px;}' +
    '.suning_toolbox li{border-bottom:1px dashed #E9F5FA;}' +
    '.suning_toolbox .namelink{ width:150px; display:inline-block; height:auto;font-size:12px; padding:3px;}' +
    '.suning_toolbox .namelink:hover{color:#d95 !important; }' +
	'.suning_toolbox .section-container{border-bottom: 1px solid #7EC3DB; padding:5px;}' +
	'.suning_toolbox .section_title{ border-bottom: 1px solid #7EC3DB; background-color:#E9F5FA; color: #90BCC9; font-size:14px; font-weight:bold; text-align:center;display:block; padding:3px;}' +
	'.suning_toolbox .section_title a{ border: 1px solid #7EC3DB; display: inline-block; float:right; height:12px; width:12px; font-size:10px; font-weight:normal; margin:2px;}' +
	'.suning_toolbox .section_title a:hover{ border: 1px solid #d95; }' +
	'.suning_toolbox a{padding:0px; }' +
    '.suning_toolbox a:hover{color:#d95; }' +
    '.suning_toolbox a img{height:10px; width: 12px; margin-right:5px;}' +
    '.suning_toolbox table{color:#90BCC9; font-size:10px;}' +
    '.suning_toolbox td{border-bottom:dotted 1px #E9F5FA; padding:2px; color:#3C8FA7;}' +
    '.suning_toolbox table thead th{border-bottom:solid 1px #E9F5FA; padding:2px; font-weight:bold; color:#3C8FA7;}';
    
    
    document.getElementsByTagName('head').item(0).appendChild(cssNode);
}

function addScript(){
	 var scriptnode = document.createElement('script');
	 scriptnode.setAttribute('type','text/javascript');
	 scriptnode.innerHTML = 'function startAddFriends(){' +
	 						'	var ref = document.getElementById("friend-ref").value;'+
	 						'	var f = document.getElementById("friend-from").value;'+
	 						'	var t = document.getElementById("friend-to").value;'+
	 						'	alert(ref +" "+f+" "+t);'+
							'	var sReq = "http://api.erepublik.com/v1/feeds/citizens/"+ref+".json";'+
							'	alert(sReq);'+
							'	var all = new Array();'+
							'	GM_xmlhttpRequest({method: "GET", url: sReq, onload: function(json){alert("hello world"); eval("jsonObj = " + json.responseText);for(var i=0; i< jsonObj["friends"].length; i++){	all.push(jsonObj["friends"][i]["id"]);}	compareAndAdd(all, f, t);}});'+
							'}'+
							''+
							'function compareAndAdd(all, from, to){'+
							'	for (var i = from; i < all.length && i < to; i++) {'+
							'		var link = "http://www.erepublik.com/citizen/friends/add/"+all[i];'+
							'		alert(link);'+
							'		window.open(link,"friend-"+all[i],"");'+
							'	}'+
							'}';

}

function addAddFriendSection(){
	document.getElementById('suning-toolbox').appendChild(createSection('friend-add', 'Add Friends',true));
    var d = document.createElement("div");
    d.innerHTML = '<span>Add friends of&nbsp;</span><input id="friend-ref" type="text" /><br><br>from<input id="friend-from" style="width:80px;" type="text" />&nbsp;to<input id="friend-to" type="text" style="width:80px;" />&nbsp<a id="testlink" href="javascript:void(0)" onclick="startAddFriends()">GO</a>';
    document.getElementById("friend-add").appendChild(d);
	
	 var lk = document.getElementById("testlink");
	 
	 lk.addEventListener("click", function() {
	 	var ref = document.getElementById("friend-ref").value;
	 	var f = document.getElementById("friend-from").value;
	 	var t = document.getElementById("friend-to").value;
		startAddFriends("http://api.erepublik.com/v1/feeds/citizens/"+ref+".json", f,t);
	 },false);
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
    document.getElementById('suning-toolbox').appendChild(createSection('exchange-table', 'Exchange Monitor',false));
    var tb = document.createElement('table');
    tb.setAttribute('cellpadding', '2px');
    tb.setAttribute('cellspacing', '1px');
    tb.setAttribute('width', '100%');
    var tbody = document.createElement('tbody');
    tb.innerHTML = '<thead><th>Type</th><th>Amount</th><th>Rate</th><th>Account</th><th>Link</th></thead>'
    tb.appendChild(tbody);
    document.getElementById('exchange-table').appendChild(tb);
    var tr = document.createElement('tr');
    var td = document.createElement('td');
    td.setAttribute("colspan", "5");
    td.setAttribute("style", "text-align:center;")
    tr.appendChild(td);

    var refreshlink = createScriptLink("refresh");
    td.appendChild(refreshlink);
    refreshlink.addEventListener("click",
    		function (){
    			while(tbody.firstChild != tr){
    				tbody.removeChild(tbody.firstChild);
    			}
		    	for (var i = 0; i < exchanges.length; i++) {
		    		tbody.insertBefore(createExchagngeRateEntry(exchanges[i][0], exchanges[i][1]), tr);
		    		tbody.insertBefore(createExchagngeRateEntry(exchanges[i][1], exchanges[i][0]), tr);
		        }
    		},
    		false
    );
    tbody.appendChild(tr);
    
}

function createExchagngeRateEntry(buy, sell){
    var tr = document.createElement('tr');
    
    var sReq = "http://api.erepublik.com/v1/feeds/exchange/" + buy + "/" + sell + ".json";
    GM_xmlhttpRequest({
        method: 'GET',
        url: sReq,
        onload: function(json){
            eval("jsonObj = " + json.responseText);
            var amount = jsonObj[0]["offer"]["value"];
            var rate = jsonObj[0]["offer"]["for"];
            var user = jsonObj[0]["seller"]["name"];
            //type
            var td = document.createElement('td');
            td.innerHTML = buy + '->' + sell;
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


function addLowestPriceSection(){
    document.getElementById('suning-toolbox').appendChild(createSection('lowest-price', 'Lowest Prices',false));
    var tb = document.createElement('table');
    tb.setAttribute('cellpadding', '2px');
    tb.setAttribute('cellspacing', '1px');
    tb.setAttribute('width', '100%');
    var tbody = document.createElement('tbody');
    tb.innerHTML = '<thead><th>Product</th><th>Price</th><th>Amount</th><th>Country</th><th>Link</th></thead>'
    tb.appendChild(tbody);
    document.getElementById('lowest-price').appendChild(tb);
    var tr = document.createElement('tr');
    var td = document.createElement('td');
    td.setAttribute("colspan", "5");
    td.setAttribute("style", "text-align:center;")
    tr.appendChild(td);

    var refreshlink = createScriptLink("refresh");
    td.appendChild(refreshlink);
    refreshlink.addEventListener("click",
    		function (){
    			while(tbody.firstChild != tr){
    				tbody.removeChild(tbody.firstChild);
    			}
		    	for (var i = 0; i < productsToMonitor.length; i++) {
					findLowestPrice(productsToMonitor[i][0],productsToMonitor[i][1],tr);
		        }
    		},
    		false
    );
    tbody.appendChild(tr);
    
}

function findLowestPrice(productType, quality, insertbeforeElement){
	var country_json_array = new Array();
	for (var m = 0; m < countryToMonitor.length; m++) {
		var current_country = countryToMonitor[m];
		getPriceJsonObject(productType,quality, insertbeforeElement, current_country,country_json_array);
	}
}

function getPriceJsonObject(productType, quality, insertbeforeElement, country, country_json_array){
	var addr = "http://api.erepublik.com/v1/feeds/market/"+productType+"/"+quality+"/"+country+".json";
	GM_xmlhttpRequest({
        method: 'GET',
        url: addr,
        onload: function(json){
            eval("jsonObj = " + json.responseText);
			country_json_array.push(new Array(country, jsonObj));
			if(country_json_array.length >= countryToMonitor.length){
				var lid = 0;
				var lprice = 100; //100G
				for(var j = 0; j<countryToMonitor.length; j++){
					var jobj = country_json_array[j][1];
					var ctry = country_json_array[j][0];
					var price = jobj[0]["offer"]["price"];
					var quantity = jobj[0]["offer"]["quantity"];
					var prc = price.slice(0, price.indexOf(' '));
					prc = prc*EXCHANGE_RATES[ctry];
					if(prc < lprice ){
						lid = j;
						lprice = prc;
					}
				}
				var ele = createLowestPriceEntry(productType, quality,country_json_array[lid][0] , country_json_array[lid][1]);
				insertbeforeElement.parentNode.insertBefore(ele, insertbeforeElement); 
			}
        }
    });
}
function createLowestPriceEntry(productType, quality, country , jsonObj){
    var tr = document.createElement('tr');
 
    //product type
    var td = document.createElement('td');
    td.innerHTML = "Q"+quality + " "+productType;
    tr.appendChild(td);
    //price
    td = document.createElement('td');
	var price_local = jsonObj[0]["offer"]["price"];
	price_local = price_local.slice(0, price_local.indexOf(' '));
	var price_g = price_local*EXCHANGE_RATES[country];
    td.innerHTML = Number(price_local).toFixed(2)+ " ("+price_g.toFixed(4)+"G)";
	var test = "343";
	
    tr.appendChild(td);
    //quantity
    td = document.createElement('td');
    td.innerHTML = jsonObj[0]["offer"]["quantity"];
    tr.appendChild(td);
    //country
    td = document.createElement('td');
    td.innerHTML = country
    tr.appendChild(td);
    //link
    td = document.createElement('td');
    td.innerHTML = 'none';
    tr.appendChild(td);
    return tr;
}

function addFriendTracks(){
    document.getElementById('suning-toolbox').appendChild(createSection('friends-track', 'Track Friends',false));
    var tb = document.createElement('table');
    tb.setAttribute('cellpadding', '2px');
    tb.setAttribute('cellspacing', '1px');
    tb.setAttribute('width', '100%');
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
    
    var sReq = "http://api.erepublik.com/v1/feeds/citizens/" + fid + ".json";
    GM_xmlhttpRequest({
        method: 'GET',
        url: sReq,
        onload: function(json){
            eval("jsonObj = " + json.responseText);
            // name
            var td = document.createElement('td');
            td.innerHTML = '<a href="' + ('http://www.erepublik.com/en/citizen/profile/' + fid) + '">' + jsonObj["name"] + '</a>';
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
            td.appendChild(createDonateLink('Donate', "http://www.erepublik.com/en/citizen/donate/items/" + fid));
            td.appendChild(createMessageLink(fid));
            
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

function createScriptLink(text){
    var link = document.createElement("a");
    link.innerHTML = text;
    link.href = "javascript:void(0)";
    return link;
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
    link.href = 'http://www.erepublik.com/en/messages/compose/' + id;
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
        onload: function(json){
            eval("jsonObj = " + json.responseText);
            var rate = jsonObj[0]["offer"]["for"];
        }
    });
}

function addToolbox(){
    var div = document.createElement('div');
    div.id = 'suning-toolbox';
    div.className = 'suning_toolbox';
    
    div.appendChild(createSection('quick-link', 'Quick Links',false));
    
    
    //add
    document.getElementById('container').parentNode.appendChild(div);
}

function createSection(idstr, name, collapsed){
    var div = document.createElement('div');
    div.className = 'section';
    
    var span = document.createElement('span');
    span.className = 'section_title';
    span.innerHTML = name;
    div.appendChild(span);
    var a = document.createElement('a');
    a.innerHTML = "+";
    a.href = "javascript:void(0)";
    span.appendChild(a);
    
    var div1 = document.createElement('div');
    div1.id = idstr;
    div1.className='section-container';
    div.appendChild(div1);
    
    a.addEventListener("click", 
    		function(){
    			if(a.innerHTML === "+"){
    				a.innerHTML = "-";
    				div1.setAttribute("style","display:block;");
    			}else{
    				a.innerHTML = "+";
    				div1.setAttribute("style","display:none;");
    			}
    		}, false);
    
    if(collapsed){
    	a.innerHTML = "+";
		div1.setAttribute("style","display:none;");
    }else{
    	a.innerHTML = "-";
		div1.setAttribute("style","display:block;");
    }
    return div;
}

function startAddFriends(sreq, from, to){
	var all = new Array();
	GM_xmlhttpRequest({
        method: 'GET',
        url: sreq,
        onload: function(json){
            eval("jsonObj = " + json.responseText);
			for(var i=0; i< jsonObj["friends"].length; i++){
				all.push(jsonObj["friends"][i]["id"]);
			}
			compareAndAdd(all, from, to);
			
        }
    });
}

function compareAndAdd(all, from, to){
	for (var i = from; i < all.length && i < to; i++) {
		var link = 'http://www.erepublik.com/citizen/friends/add/'+all[i];
		alert(link);
		window.open(link,"friend-"+all[i],"");
	}
}

