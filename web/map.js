// =========================
//  INITIALIZE THE MAP
// =========================
var map = L.map('map').setView([43.5448, -80.2482], 13);

// Add the OpenStreetMap tile layer
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

console.log("Map Loaded.");


// =========================
//  LOAD STOPS.JSON
// =========================
fetch("stops.json")
    .then(response => response.json())
    .then(stops => {
        console.log("Loaded stops:", stops.length);

        stops.forEach(stop => {
            L.circleMarker([stop.lat, stop.lon], {
                radius: 5,
                color: "blue",
                fillColor: "blue",
                fillOpacity: 0.8
            })
            .addTo(map)
            .bindPopup(`<b>${stop.name}</b><br>ID: ${stop.stop_id}`);
        });
    })
    .catch(err => console.error("Error loading stops.json:", err));


// =========================
//  LOAD BUSES.JSON
// =========================
fetch("buses.json")
    .then(response => response.json())
    .then(buses => {
        console.log("Loaded bus positions:", buses.length);

        buses.forEach(bus => {
            L.marker([bus.lat, bus.lon])
                .addTo(map)
                .bindPopup(
                    `<b>${bus.bus_id}</b><br>` +
                    `Stop: ${bus.stop_name}`
                );
        });
    })
    .catch(err => console.error("Error loading buses.json:", err));
