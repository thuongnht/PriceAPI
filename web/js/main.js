/*
 * create soap message
 */
function makeSoap() {

    // build SOAP request
    var eanID = document.getElementById("eanID").value;
    var sr =
        '<?xml version="1.0" encoding="utf-8"?>' +
        '<soap:Envelope ' +
        'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" ' +
        'xmlns:xsd="http://www.w3.org/2001/XMLSchema" ' +
        'xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">' +
        '<soap:Body soap:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">' +
        '<id xsi:type="xsd:string">' +
        eanID +
        '</id>' +
        '</soap:Body>' +
        '</soap:Envelope>';

    return sr;
}

function servletContext() {
    var sc = window.location.pathname.split( '/' );
    return "/"+sc[1];
}

/*
 * creates a new XMLHttpRequest object which is the backbone of AJAX
 * or returns false if the browser doesn't support it
 */
function getXMLHttpRequest() {
    var xmlHttpReq;
    // to create XMLHttpRequest object in non-Microsoft browsers
    if (window.XMLHttpRequest) {
        xmlHttpReq = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        try {
            //to create XMLHttpRequest object in later versions of Internet Explorer
            xmlHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (exp1) {
            try {
                //to create XMLHttpRequest object in later versions of Internet Explorer
                xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (exp2) {
                //xmlHttpReq = false;
                alert("Exception in getXMLHttpRequest()!");
            }
        }
    }
    return xmlHttpReq;
}

/*
 * AJAX call starts with this function
 */
function makeRequest() {
    var xmlHttpRequest = getXMLHttpRequest();
    // console.log(xmlHttpRequest);
    var sContext = servletContext();
    // alert(sContext);
    xmlHttpRequest.onreadystatechange = getReadyStateHandler(xmlHttpRequest);
    xmlHttpRequest.open("POST", sContext + "/soapServer.do", true);
    xmlHttpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlHttpRequest.setRequestHeader("SOAPAction", '""');
    // alert("inside makeRequest()!");
    xmlHttpRequest.send(makeSoap());
    // alert("sent! " + makeSoap());
}

/*
 * Returns a function that waits for the state change in XMLHttpRequest
 */
function getReadyStateHandler(xmlHttpRequest) {
    // an anonynous function returned
    // it listens to the XMLHttpRequest instance
    return function() {
        if (xmlHttpRequest.readyState == 4) {
            if (xmlHttpRequest.status == 200) {
                var xmlDoc = xmlHttpRequest.responseXML;

                var tablePrices = document.getElementById("tablePrices");
                // remove the last table
                var elem = document.getElementsByTagName("table");
                if (elem.length>0 && typeof elem != 'undefined')
                {
                    tablePrices.removeChild(document.getElementsByTagName("table")[0]);
                }

                var tbl  = document.createElement('table');

                // create header
                var row = tbl.insertRow(0);
                var cell = document.createElement("th");
                cell.innerHTML = "<b>Shop Name</b>";
                row.appendChild(cell);
                cell = document.createElement("th");
                cell.innerHTML = "<b>Shop URL</b>";
                row.appendChild(cell);
                cell = document.createElement("th");
                cell.innerHTML = "<b>Item URL</b>";
                row.appendChild(cell);
                cell = document.createElement("th");
                cell.innerHTML = "<b>Price</b>";
                row.appendChild(cell);
                cell = document.createElement("th");
                cell.innerHTML = "<b>Price With Shipping</b>";
                row.appendChild(cell);

                var length = xmlDoc.getElementsByTagName("price_detail").length;
                for(var i = 0; i < length; i++){
                    var price = xmlDoc.getElementsByTagName("price_detail")[i];
                    var tr = tbl.insertRow();

                    var td = tr.insertCell(0);
                    td.appendChild(document.createTextNode(price.childNodes[0].textContent));

                    td = tr.insertCell(1);
                    var url = document.createElement("a");
                    url.setAttribute("href", price.childNodes[1].textContent);
                    url.setAttribute("target", "_blank");
                    url.innerHTML = "shop url";
                    td.appendChild(url);

                    td = tr.insertCell(2);
                    url = document.createElement("a");
                    url.setAttribute("href", price.childNodes[2].textContent);
                    url.setAttribute("target", "_blank");
                    url.innerHTML = "item url";
                    td.appendChild(url);

                    td = tr.insertCell(3);
                    td.appendChild(document.createTextNode(price.childNodes[3].textContent));

                    td = tr.insertCell(4);
                    td.appendChild(document.createTextNode(price.childNodes[4].textContent));
                }

                tablePrices.appendChild(tbl);

            } else {
                alert("Http error " + xmlHttpRequest.status + ":" + xmlHttpRequest.statusText);
            }
        }
    };
}