// Center roughly on Guelph
const initialCenter = [43.5448, -80.2482];
const initialZoom = 12;

// Status text
const statusEl = document.getElementById("status");

// Initialize Leaflet map
const map = L.map("map").setView(initialCenter, initialZoom);

// Base map tiles (OpenStreetMap)
L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    maxZoom: 19,
    attribution: "&copy; OpenStreetMap contributors"
}).addTo(map);

/**
 * Build Overpass QL query:
 * We want the administrative boundary relation for Guelph.
 */
const overpassQuery = `
[out:json][timeout:25];
relation["name"="Guelph"]["boundary"="administrative"]["admin_level"="6"];
(._;>;);
out geom;
`;

/**
 * Fetch boundary from Overpass API and draw it.
 */
async function loadBoundary() {
    try {
        statusEl.textContent = "Contacting Overpass API…";

        const response = await fetch("https://overpass-api.de/api/interpreter", {
            method: "POST",
            body: overpassQuery
        });

        if (!response.ok) {
            throw new Error("Overpass error: " + response.status);
        }

        const data = await response.json();
        console.log("Overpass data:", data);

        // Collect all polygons for the relation
        const polygons = [];

        // We'll map node id -> [lat, lon] to help rebuild ways
        const nodeMap = new Map();

        data.elements.forEach(el => {
            if (el.type === "node") {
                nodeMap.set(el.id, [el.lat, el.lon]);
            }
        });

        data.elements.forEach(el => {
            // Ways in the relation define the boundary rings
            if (el.type === "way" && Array.isArray(el.nodes)) {
                const coords = el.nodes
                    .map(nodeId => nodeMap.get(nodeId))
                    .filter(Boolean); // drop any missing nodes just in case

                if (coords.length > 1) {
                    polygons.push(coords);
                }
            }

            // Some Overpass responses might give geometry directly:
            if (el.type === "way" && Array.isArray(el.geometry)) {
                const coords = el.geometry.map(pt => [pt.lat, pt.lon]);
                if (coords.length > 1) {
                    polygons.push(coords);
                }
            }
        });

        if (polygons.length === 0) {
            statusEl.textContent = "No boundary geometry found for Guelph.";
            return;
        }

        // Draw polygon(s)
        const boundaryLayer = L.polygon(polygons, {
            color: "#33aaff",
            weight: 3,
            fillColor: "#3388ff",
            fillOpacity: 0.15
        }).addTo(map);

        // Zoom map to fit boundary
        map.fitBounds(boundaryLayer.getBounds());

        statusEl.textContent = "Boundary loaded successfully.";
    } catch (err) {
        console.error(err);
        statusEl.textContent = "Error loading boundary: " + err.message;
    }
}

// Kick it off
loadBoundary();
