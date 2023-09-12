import { React, useState } from 'react';
import { createContainer, VictoryChart, VictoryLine, VictoryAxis, VictoryVoronoiContainer, VictoryTooltip, VictoryArea } from 'victory';

export default function MembersChart({ resources }) {
    const VictoryZoomVoronoiContainer = createContainer("zoom", "voronoi");

    const [zoomDomain, setZoomDomain] = useState({ x: [0, 10] });

    const handleZoom = (domain) => {
        console.log(domain.x[1] - domain.x[0], domain)
        setZoomDomain(domain);
    };

    const formatMembersNumber = (members) =>{
        if(members >= 1000 && members < 1000000){
            return `${members / 1000}k`
        }
        if(members >= 1000000){
            return `${members / 1000000}M`
        }
        return members
    }

    const calculateTickFormatCondition = (index) => {
        // This is a placeholder condition; adjust based on your needs.
        const zoomRange = zoomDomain.x[1] - zoomDomain.x[0];
        if (zoomRange > 5) {
            return index % 3 === 0;
        }
        return true;  // Adjust this as necessary based on the zoom level
    };

    return (
        <VictoryChart
            width={500}
            height={300}
            padding={{ top: 20, bottom: 50, left: 60, right: 35 }}
            containerComponent={
                <VictoryZoomVoronoiContainer
                    voronoiDimension="x"
                    zoomDomain={zoomDomain}
                    onZoomDomainChange={handleZoom}
                />
            }
            style={{
                background: { fill: "#1f2938" }
            }}
        >
            <defs>
                <linearGradient id="gradient" x1="0%" y1="0%" x2="0%" y2="100%">
                    <stop offset="0%" style={{ stopColor: "#016ca7", stopOpacity: 1 }} />
                    <stop offset="100%" style={{ stopColor: "#003653", stopOpacity: 0.2 }} />
                </linearGradient>
            </defs>
            <VictoryAxis
                tickFormat={(x, index) => calculateTickFormatCondition(index) ? new Date(x).toLocaleDateString() : ``}
                style={{
                    ticks: { stroke: "#e0e0e0", size: 5 },
                    tickLabels: { fontSize: 10, padding: 5, angle: 45, textAnchor: 'start', fill: "#e0e0e0" },
                    grid: { stroke: "#3b485c", strokeWidth: 0.5 }
                }}
            />
            <VictoryAxis
                dependentAxis
                tickFormat={(y) => formatMembersNumber(y)}
                style={{
                    ticks: { stroke: "#016ca7", size: 5 },
                    tickLabels: { fontSize: 10, fill: "#e0e0e0" },
                    grid: { stroke: "#3b485c", strokeWidth: 0.5 }
                }}
            />
            <VictoryArea
                data={resources}
                x="archiveDate"
                y="members"
                style={{
                    data: { fill: "url(#gradient)" }
                }}
            />
            <VictoryLine
                data={resources}
                x="archiveDate"
                y="members"
                style={{
                    data: { stroke: "#e0e0e0", strokeWidth: 2.5 }, // Solid color for line
                }}
            />
        </VictoryChart>
    );
}
