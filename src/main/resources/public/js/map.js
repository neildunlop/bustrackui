//var map = L.map('map').setView([51.505, -0.09], 13);

var map = L.map('map', {center: [51.505, -0.09], zoom: 13}),
    realtime = L.realtime(undefined, {
        getFeatureId: function(f) { return f.id; },
        start: false,
        onEachFeature: onEachFeature,
        pointToLayer: pointToLayer
    }).addTo(map);


//this gets called before each geoJSON feature is added to the map - add a popup to the marker
function onEachFeature(feature, layer) {
    var popupContent = "<p>I'm a " +
        feature.properties.vehicleType + ", toot toot!</p>";

    if (feature.properties && feature.properties.popupContent) {
        popupContent += feature.properties.popupContent;
    }

    layer.bindPopup(popupContent);
}

//creates a marker with a custom icon
function pointToLayer (feature, latlng) {
    return L.marker(latlng, {icon: busIcon});
}


//add a background tile layer
L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibmVpbGR1bmxvcCIsImEiOiJjaXhycGc1bDQwMDQ3MnhxaXFibDl4cXRsIn0.crgrBXCV-JDmTRk0j7Lg8w', {
    attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
    maxZoom: 18,
    id: 'mapbox.streets',
    accessToken: 'pk.eyJ1IjoibmVpbGR1bmxvcCIsImEiOiJjaXhycGc1bDQwMDQ3MnhxaXFibDl4cXRsIn0.crgrBXCV-JDmTRk0j7Lg8w'
}).addTo(map);

//define a custom icon for our bus markers
var busIcon = L.icon({
    iconUrl: '/ShuttleIcon_Blue.gif',
    iconSize: [32, 32],
    iconAnchor: [16, 32],
    popupAnchor: [0, -28]
});

//place a simple marker on the map
//var marker = L.marker([51.5, -0.09]).addTo(map);

//geoJSON for our bus marker
// var busMarker = {
//     "type": "Feature",
//     "properties": {
//         "popupContent": "All aboard!",
//         "vehicleType": "Bendy Bus"
//     },
//     "geometry": {
//         "type": "Point",
//         "coordinates": [-0.09,51.505]
//     }
// };


// add layer for bus markers with an actual bus marker in it.. made from GeoJSON.
// var busMarkerLayer = L.geoJSON(busMarker, {
//     pointToLayer: function (feature, latlng) {
//         return L.marker(latlng, {icon: busIcon});
//     },
//     onEachFeature: onEachFeature
// }).addTo(map);

//converts a server response to GeoJSON
function convertResponseToGeoJSON(serverData) {
    console.log("serverData is: "+serverData);

    var serverObj = JSON.parse(serverData);
    var response = {"type": "Feature","properties": {"id":serverObj.chassisNumber, "popupContent": "All aboard! We are going "+serverObj.speed+" mph","vehicleType": "Bendy Bus"},"geometry": {"type": "Point","coordinates": [serverObj.latitude,serverObj.longitude]}};
    return response;
}


//Websocket management
var ws;
(function(ws) {
    "use strict";
    if (window.WebSocket) {
        console.log("WebSocket object is supported in your browser");
        ws = new WebSocket("ws://127.0.0.1:64580/api/position/live");
        ws.onopen = function() {
            console.log("onopen");
            ws.send('{"chassisNumbers":["1234"]}');
        };
        ws.onmessage = function(e) {
            console.log("echo from server : " + e.data);
            var geoJson = convertResponseToGeoJSON(e.data);
            console.log("geoJSON is: " + geoJson);
            realtime.update(geoJson);
            console.log("updated");
        };

        ws.onclose = function() {
            console.log("onclose");
        };
        ws.onerror = function() {
            console.log("onerror");
        };

    } else {
        console.log("WebSocket object is not supported in your browser");
    }
})(ws);