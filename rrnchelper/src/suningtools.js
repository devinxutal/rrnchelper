// ==UserScript==
// @name           suningtools
// @namespace      www.erepublik.com
// @description    tools for suning
// @include        http://www.erepublik.com/*
// @require        http://jqueryjs.googlecode.com/files/jquery-1.3.1.min.js
// ==/UserScript==



// path C:\Users\Devin\AppData\Roaming\Mozilla\Firefox\Profiles\5cov6sv8.default\gm_scripts\suningtools

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
//friends track
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
productsToMonitor.push(new Array("gift", 2));
productsToMonitor.push(new Array("weapon", 1));
var countryToMonitor = new Array();
countryToMonitor.push("china");
countryToMonitor.push("united-kingdom");
countryToMonitor.push("poland");
countryToMonitor.push("argentina");
var EXCHANGE_RATES = new Object;
EXCHANGE_RATES["china"] = 0.02;
EXCHANGE_RATES["united-kingdom"] = 0.026;
EXCHANGE_RATES["poland"] = 0.02;
EXCHANGE_RATES["argentina"] = 0.02;
//add friends
var currentFriends = new Array();
var friendsToAdd = new Array();
/////////////////////////////////////
/////////////////////////////////////

checkAutoPost();

addCss();
addToolbox();
addQuickLinks();
addFriendTracks();
addExchangeRates();
addLowestPriceSection();
addAddFriendSection();
addExchangeDealerSection();
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
    scriptnode.setAttribute('type', 'text/javascript');
    scriptnode.innerHTML = 'function startAddFriends(){' +
    '	var ref = document.getElementById("friend-ref").value;' +
    '	var f = document.getElementById("friend-from").value;' +
    '	var t = document.getElementById("friend-to").value;' +
    '	alert(ref +" "+f+" "+t);' +
    '	var sReq = "http://api.erepublik.com/v1/feeds/citizens/"+ref+".json";' +
    '	alert(sReq);' +
    '	var all = new Array();' +
    '	GM_xmlhttpRequest({method: "GET", url: sReq, onload: function(json){alert("hello world"); eval("jsonObj = " + json.responseText);for(var i=0; i< jsonObj["friends"].length; i++){	all.push(jsonObj["friends"][i]["id"]);}	compareAndAdd(all, f, t);}});' +
    '}' +
    '' +
    'function compareAndAdd(all, from, to){' +
    '	for (var i = from; i < all.length && i < to; i++) {' +
    '		var link = "http://www.erepublik.com/citizen/friends/add/"+all[i];' +
    '		alert(link);' +
    '		window.open(link,"friend-"+all[i],"");' +
    '	}' +
    '}';
    
}

function addAddFriendSection(){
    document.getElementById('suning-toolbox').appendChild(createSection('friend-add', 'Add Friends', true));
    var d = document.createElement("div");
    d.innerHTML = '<span id="friend-num">This account have 0 friends now.</span><br><br><span>Self ID&nbsp;<input id="self-id" style="width:40px;" type="text" />&nbsp;max friends&nbsp;<input id="max-friend" style="width:30px;" type="text" /><a id="refresh-friend" href="javascript:void(0)" onclick="startAddFriends()">refresh</a></span>' +
    '<br><br><br><span>Add friends of&nbsp;</span><input id="friend-ref" type="text" /><br><br>from<input id="friend-from" style="width:80px;" type="text" />&nbsp;to<input id="friend-to" type="text" style="width:80px;" />&nbsp<a id="add-friend" href="javascript:void(0)">GO</a>';
    document.getElementById("friend-add").appendChild(d);
    
    var lk = document.getElementById("add-friend");
    lk.addEventListener("click", function(){
        var ref = document.getElementById("friend-ref").value;
        var f = document.getElementById("friend-from").value;
        var t = document.getElementById("friend-to").value;
        startAddFriends("http://api.erepublik.com/v1/feeds/citizens/" + ref + ".json", f, t);
    }, false);
    
    var lk1 = document.getElementById("refresh-friend");
    lk1.addEventListener("click", function(){
        var max = document.getElementById("max-friend").value;
        max = Number(max);
        var selfid = document.getElementById("self-id").value;
        startRefreshFriends(selfid, max);
    }, false);
}

var autoDealerInterval = null;

function addExchangeDealerSection(){
    document.getElementById('suning-toolbox').appendChild(createSection('exchange-dealer', 'Exchange Dealer', true));
    var d = document.createElement("div");
    d.innerHTML = '<span id="dealer-info">Dealer is not running.</span><br><br><span>Sell&nbsp;<input id="dealer-sell" style="width:40px;" type="text" />&nbsp;Buy&nbsp;<input id="dealer-buy" style="width:40px;" type="text" />&nbsp;Threshold&nbsp;<input id="dealer-threshold" style="width:40px;" type="text" /></span>' +
    '<br><br><span>Interval&nbsp;</span><input id="dealer-interval" type="text" style="width:40px;" type="text" />&nbsp;<a id="dealer-start" href="javascript:void(0)">GO</a>';
    document.getElementById("exchange-dealer").appendChild(d);
    
    var lk = document.getElementById("dealer-start");
    lk.addEventListener("click", function(){

        var itvl = Number(document.getElementById("dealer-interval").value);
        if (autoDealerInterval) {
            //stop it
            lk.innerHTML = "GO";
            clearInterval(autoDealerInterval);
            autoDealerInterval = null;
        }
        else {
            autoDealerInterval = setInterval(dealerCallBack, itvl * 1000);
            lk.innerHTML = "STOP";
        }
    }, false);
}

function dealerCallBack(){
	//window.open("http://www.erepublik.com/en/exchange/create?account_type=citizen#buy_currencies=29;sell_currencies=62;page=1;autopost","post-new-offer");
	populateOfferListsz("page=1");
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
    document.getElementById('suning-toolbox').appendChild(createSection('exchange-table', 'Exchange Monitor', false));
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
    refreshlink.addEventListener("click", function(){
        while (tbody.firstChild != tr) {
            tbody.removeChild(tbody.firstChild);
        }
        for (var i = 0; i < exchanges.length; i++) {
            tbody.insertBefore(createExchagngeRateEntry(exchanges[i][0], exchanges[i][1]), tr);
            tbody.insertBefore(createExchagngeRateEntry(exchanges[i][1], exchanges[i][0]), tr);
        }
    }, false);
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
    document.getElementById('suning-toolbox').appendChild(createSection('lowest-price', 'Lowest Prices', false));
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
    refreshlink.addEventListener("click", function(){
        while (tbody.firstChild != tr) {
            tbody.removeChild(tbody.firstChild);
        }
        for (var i = 0; i < productsToMonitor.length; i++) {
            findLowestPrice(productsToMonitor[i][0], productsToMonitor[i][1], tr);
        }
    }, false);
    tbody.appendChild(tr);
    
}

function findLowestPrice(productType, quality, insertbeforeElement){
    var country_json_array = new Array();
    for (var m = 0; m < countryToMonitor.length; m++) {
        var current_country = countryToMonitor[m];
        getPriceJsonObject(productType, quality, insertbeforeElement, current_country, country_json_array);
    }
}

function getPriceJsonObject(productType, quality, insertbeforeElement, country, country_json_array){
    var addr = "http://api.erepublik.com/v1/feeds/market/" + productType + "/" + quality + "/" + country + ".json";
    GM_xmlhttpRequest({
        method: 'GET',
        url: addr,
        onload: function(json){
            eval("jsonObj = " + json.responseText);
            country_json_array.push(new Array(country, jsonObj));
            if (country_json_array.length >= countryToMonitor.length) {
                var lid = 0;
                var lprice = 100; //100G
                for (var j = 0; j < countryToMonitor.length; j++) {
                    var jobj = country_json_array[j][1];
                    var ctry = country_json_array[j][0];
                    var price = jobj[0]["offer"]["price"];
                    var quantity = jobj[0]["offer"]["quantity"];
                    var prc = price.slice(0, price.indexOf(' '));
                    prc = prc * EXCHANGE_RATES[ctry];
                    if (prc < lprice) {
                        lid = j;
                        lprice = prc;
                    }
                }
                var ele = createLowestPriceEntry(productType, quality, country_json_array[lid][0], country_json_array[lid][1]);
                insertbeforeElement.parentNode.insertBefore(ele, insertbeforeElement);
            }
        }
    });
}

function createLowestPriceEntry(productType, quality, country, jsonObj){
    var tr = document.createElement('tr');
    
    //product type
    var td = document.createElement('td');
    td.innerHTML = "Q" + quality + " " + productType;
    tr.appendChild(td);
    //price
    td = document.createElement('td');
    var price_local = jsonObj[0]["offer"]["price"];
    price_local = price_local.slice(0, price_local.indexOf(' '));
    var price_g = price_local * EXCHANGE_RATES[country];
    td.innerHTML = Number(price_local).toFixed(2) + " (" + price_g.toFixed(4) + "G)";
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
    document.getElementById('suning-toolbox').appendChild(createSection('friends-track', 'Track Friends', false));
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
    
    div.appendChild(createSection('quick-link', 'Quick Links', false));
    
    
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
    div1.className = 'section-container';
    div.appendChild(div1);
    
    a.addEventListener("click", function(){
        if (a.innerHTML === "+") {
            a.innerHTML = "-";
            div1.setAttribute("style", "display:block;");
        }
        else {
            a.innerHTML = "+";
            div1.setAttribute("style", "display:none;");
        }
    }, false);
    
    if (collapsed) {
        a.innerHTML = "+";
        div1.setAttribute("style", "display:none;");
    }
    else {
        a.innerHTML = "-";
        div1.setAttribute("style", "display:block;");
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
            for (var i = 0; i < jsonObj["friends"].length; i++) {
                all.push(jsonObj["friends"][i]["id"]);
            }
            compareAndAdd(all, from, to);
        }
    });
}

function compareAndAdd(all, from, to){
    friendsToAdd.length = 0;
    for (var i = from; i < all.length && i < to; i++) {
        var fid = all[i];
        var exist = false;
        for (var j = 0; j < currentFriends.length; j++) {
            //alert(currentFriends[j]+" " +fid+" " + (currentFriends[j] === fid));
            if (Number(currentFriends[j]) === Number(fid)) {
                exist = true;
                break;
            }
        }
        if (exist) {
            //alert("The friend "+ fid+" already exists");
        }
        else {
            var link = 'http://www.erepublik.com/citizen/friends/add/' + all[i];
            //alert("add friend "+link);
            friendsToAdd.push(link);
            
        }
    }
    var step = 0;
    var intervalID = setInterval(function(){
        window.open(friendsToAdd[step], "friend-add");
        step++;
        if (step >= friendsToAdd.length) {
            window.clearInterval(intervalID);
        }
    }, 5000);
}

function startRefreshFriends(selfid, max){
    currentFriends.length = 0;
    var addr_t = "http://www.erepublik.com/citizen/friends/" + selfid + "/";
    for (var i = 0; i <= (max / 12); i++) {
        var addr = addr_t + i;
        GM_xmlhttpRequest({
            method: 'GET',
            url: addr,
            onload: function(json){
                eval("jsonObj = " + json.responseText);
                var count = 0;
                for (var n = 0; n < jsonObj.length; n++) {
                    var urlstr = jsonObj[n]["url"];
                    var reg = /\d+/;
                    var result = urlstr.match(reg);
                    if (result) {
                        currentFriends.push(result);
                        count++;
                    }
                }
                document.getElementById("friend-num").innerHTML = "This account have " + currentFriends.length + " friends now.";
            }
        });
    }
}




///////////////////////////////////////////////////
/// copied from papillon///////////////////////////
///////////////////////////////////////////////////



function buyfromexchange(){
    var sell_currency = $('#sell_currency').val();
    var buy_currency = $('#buy_currency').val();
    $('#pagination_pager').attr("style", "display: none;");
    var havesell = Number($('#sell_currency_account_amount').text() + $('#sell_currency_account_decimal').text());
    var havebuy = Number($('#buy_currency_account_amount').text() + $('#buy_currency_account_decimal').text());
    var firstorderamount = Number($("table.offers tr:eq(3) td:eq(1) span:eq(0)").text());
    var firstorderprice = Number($("table.offers tr:eq(3) td:eq(2) span:eq(2)").text());
    var secondorderprice = Number($("table.offers tr:eq(4) td:eq(2) span:eq(2)").text());
    //	var firstorderid = $("table.offers tr:eq(3) td:eq(2) span:eq(2)").attr("id").substring(22,29);
    var firstordercost = firstorderamount * firstorderprice;
	//alert("the first order: "+firstorderamount+", "+firstorderprice);
    if (sell_currency == 62) {
        var topprice = 0.1;
    }
    else {
        var topprice = 120;
    }
	
    if (havesell < 5) {
        window.open("http://www.erepublik.com/en/exchange#buy_currencies=" + sell_currency + ";sell_currencies=" + buy_currency + ";page=1");
    }
    if (firstorderprice / secondorderprice < 0.5 && firstorderprice < topprice) {
		//alert("i will buy this bill now");
        if (firstorderamount > havesell / firstorderprice) {
            var amounttobuy = (Math.floor(havesell / firstorderprice * 100)) / 100;
        }
        else {
            var amounttobuy = firstorderamount;
        }
        //	var amounttobuy = 0.01
        $("table.offers tr:eq(3) td:eq(3) input:eq(0)").attr("value", amounttobuy);
        var patton = $("table.offers tr:eq(3) td:eq(3) input:eq(1)"); //this is the button!
        acceptOffer_onClicksz(patton);
    }
    else {
        //alert("nothing to buy");
    }
}

function populateOfferListsz(page){
    var select_page = $('#select_page').val();
    var url_for_list = $('a#ajax_action_select_link').attr('href');
    var account_type = $('#account_type').val();
    var action_path = $('#action_path').val();
    var div_table_populate = $('#table_list_offers');
    var pagination_pager = $('#pagination_pager');
    var pagination_ajax_loader = $('#list_offers_ajax_loader');
    var sell_currency_id = $('#sell_currency').val();
    var buy_currency_id = $('#buy_currency').val();
    div_table_populate.fadeOut('slow');
    pagination_pager.fadeOut('slow');
    pagination_ajax_loader.fadeIn('slow');
	
	$.ajax({
        type: 'GET',
        url: url_for_list,
        dataType: 'html',
        data: {
            select_page: select_page,
            buy_currency_history_id: "buy_currencies="+buy_currency_id,
            sell_currency_history_id: "sell_currencies="+sell_currency_id,
            account_type: account_type,
            action_path: action_path,
            page: page
        },
        beforeSend: function(){
        },
        success: function(html){
            $('#populateOffers').replaceWith(html);
			buyfromexchange();
        },
        error: function(){
            // jalert("An error has occurred. Please try again.");
        },
        complete: function(){
        }
    });
}

function acceptOffer_onClicksz(object){
    var offer_id_array = object.attr("id").split('_');
    var offer_id = offer_id_array[3];
    var submit_amount_form = $('#form_amount_accept_' + offer_id).val();
    var submit_amount = $('#amount_to_accept');
    var buy_currency_name = $('#offer_buy_currency_name_' + offer_id).val();
    var sell_currency_name = $('#offer_sell_currency_name_' + offer_id).val();
    var exchange_rate = $('#offer_exchange_rate_' + offer_id).val();
    var account_type = $('#account_type').val()
    var account_type_accept = $('#account_type_accept');
    var accept_confirm_buy_message_part = $('#accept_confirm_buy_message_part').val();
    var accept_confirm_for_message_part = $('#accept_confirm_for_message_part').val();
    var accept_button = $('#submit_form_accept_' + offer_id);
    
    submit_amount.val(submit_amount_form);
    account_type_accept.val(account_type);
    
    var value = parseFloat(submit_amount_form * exchange_rate);
    
    var submit_message = accept_confirm_buy_message_part + " " + submit_amount.val() + " " + buy_currency_name + " " + accept_confirm_for_message_part + " " + value.toFixed(2) + " " + sell_currency_name + " ?";
    
    if (validate_amountsz(object)) {
        accept_button.hide();
        sendAcceptsz(object);
    }
    else {
        return false;
    }
}


function sendAcceptsz(object){
    var offer_id_array = object.attr("id").split('_');
    var offer_id = offer_id_array[3];
    var url_for_accept = $('a#url_for_accept').attr('href');
    var account_type = $('#account_type').val();
    var amount_to_accept = $('#amount_to_accept').val();
    var sell_currency = $('#sell_currency').val();
    var buy_currency = $('#buy_currency').val();
    var exchange_rate = $("#offer_exchange_rate_" + offer_id).val();
    var initial_amount = $('#initial_amount_' + offer_id).html();
    var _token = $('#_token').val();
    var buy_currency_history_id = $('a#buy_selector').attr('title');
    var sell_currency_history_id = $('a#sell_selector').attr('title');
    var select_page = $('#select_page').val();
    var company_id = $('#company_id').val();
    var action_path = $('#action_path').val();
    var page = $('#page_in_list').val();
    var buy_currency_id = buy_currency;
    var sell_currency_id = sell_currency;
    
    $.ajax({
        type: 'POST',
        url: url_for_accept,
        dataType: 'html',
        data: {
            select_page: select_page,
            account_type: account_type,
            page: page,
            offer_id: offer_id,
            amount_to_accept: amount_to_accept,
            sell_currency: sell_currency,
            company_id: company_id,
            buy_currency: buy_currency,
            exchange_rate: exchange_rate,
            initial_amount: initial_amount,
            action_path: action_path,
            _token: _token,
            buy_currency_history_id: buy_currency_id,
            sell_currency_history_id: sell_currency_id
        },
        
        beforeSend: function(){
        },
        success: function(html){
            $('#populateOffers').replaceWith(html);
        },
        error: function(){
            // jalert("An error has occurred. Please try again.");
        },
        complete: function(){
        }
    });
}

function amountValidate_onBlursz(object){
    validate_amountsz(object);
}

function validate_amountsz(object){
    var offer_id_array = object.attr("id").split('_');
    var offer_id = offer_id_array[3];
    var initial_amount = $('#initial_amount_' + offer_id).html();
    var form_amount_value = $('#form_amount_accept_' + offer_id).val();
    var error_for_form_amount = $('#error_for_amount_to_accept');
    var sell_currency_account_amount = $('#sell_currency_account_float_amount').val();
    var exchange_value_amount = $('#exchange_value_amount_' + offer_id).html();
    var empty_amount_error = $('#empty_amount_error').val();
    var valid_amount_error = $('#valid_amount_error').val();
    var valid_decimal_amount_error = $('#valid_decimal_amount_error').val();
    var max_amount_offered_error = $('#max_amount_offered_error').val();
    var not_enough_currency_error = $('#not_enough_currency_error').val();
    var negative_amount_accept_error = $('#negative_amount_accept_error').val();
    
    error_for_form_amount.hide();
    
    if (parseFloat(form_amount_value) <= 0) {
        error_for_form_amount.html(negative_amount_accept_error);
        error_for_form_amount.show();
        return false;
    }
    
    if ("" == form_amount_value) {
        error_for_form_amount.html(empty_amount_error);
        error_for_form_amount.show();
        return false;
    }
    
    if (form_amount_value.match(/^[0-9]{1}([0-9]{0,})\.?[0-9]{0,2}$/)) {
        if (form_amount_value.match(/^[0-9]{0,}\.$/)) {
            error_for_form_amount.html(valid_amount_error);
            error_for_form_amount.show();
            return false;
        }
    }
    else {
        error_for_form_amount.show();
        error_for_form_amount.html(valid_decimal_amount_error);
        return false;
    }
    
    if (parseFloat(initial_amount) < parseFloat(form_amount_value)) {
        error_for_form_amount.html(max_amount_offered_error);
        error_for_form_amount.show();
        return false;
    }
    
    if (parseFloat(sell_currency_account_amount) < parseFloat(form_amount_value * exchange_value_amount)) {
        error_for_form_amount.html(not_enough_currency_error);
        error_for_form_amount.show();
        return false;
    }
    
    error_for_form_amount.hide();
    return true;
}

function refreshValues_onAcceptsz(){
    var accepted_buy_value = $('#accepted_buy_value').val();
    var accepted_sell_value = $('#accepted_sell_value').val();
    var sup_accepted_buy_value = $('#sup_accepted_buy_value').val();
    var sup_accepted_sell_value = $('#sup_accepted_sell_value').val();
    var currency_account_for_buy = $('#buy_currency_account_amount');
    var currency_account_for_sell = $('#sell_currency_account_amount');
    var buy_currency_account_decimal = $('#buy_currency_account_decimal');
    var sell_currency_account_decimal = $('#sell_currency_account_decimal');
    var ajax_buy_currency_name = $('#ajax_hidden_buy_currency_name').html();
    var ajax_sell_currency_name = $('#ajax_hidden_sell_currency_name').html();
    var ajax_hidden_RER_content = $('#ajax_hidden_RER_content').html();
    var change_ajax_buy_currency = $('span.change_ajax_buy_currency');
    var change_ajax_sell_currency = $('span.change_ajax_sell_currency');
    var change_ajax_RER = $('span.change_ajax_RER');
    
    var integer_accepted_buy_value = parseInt(accepted_buy_value);
    var integer_accepted_sell_value = parseInt(accepted_sell_value);
    
    currency_account_for_buy.html(integer_accepted_buy_value);
    currency_account_for_sell.html(integer_accepted_sell_value);
    buy_currency_account_decimal.html(sup_accepted_buy_value);
    sell_currency_account_decimal.html(sup_accepted_sell_value);
    change_ajax_buy_currency.html(ajax_buy_currency_name);
    change_ajax_sell_currency.html(ajax_sell_currency_name);
    change_ajax_RER.html(ajax_hidden_RER_content);
}


function buyCompanysz(object){
    var company_for_sale_id = $(object).attr("id");
    $("input#company_for_sale").val(company_for_sale_id);
    $confirm_buy_message = $('#confirm_buy_message_' + company_for_sale_id).val();
    $("form#buy_company").submit();
}






//////////autopost

function checkAutoPost(){
	var str = window.location.href;
	if(str.indexOf("create")>0 && str.indexOf("autopost")>0){
		//to post
	}
}
