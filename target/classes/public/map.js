// Initialize map centered on Guelph
const map = L.map('map').setView([43.5448, -80.2482], 12);

// Add map tiles
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

// Overpass query for Guelph boundary relation 7486148
const query = `
[out:json];
relation(7486148);
out geom;
`;

fetch("https://overpass-api.de/api/interpreter", {
    method: "POST",
    body: query
})
.then(response => response.json())
.then(data => {
    const relation = data.elements[0];

    const coords = relation.members
        .filter(m => m.type === "way" && m.geometry)
        .flatMap(m => m.geometry.map(p => [p.lat, p.lon]));

    L.polygon(coords, { color: "red" }).addTo(map);

    map.fitBounds(coords);
})
.catch(err => {
    console.error("Error loading boundary:", err);
});
