import React, { useState, useEffect } from 'react';
import { VictoryChart, VictoryLine, VictoryAxis, VictoryVoronoiContainer, VictoryArea } from 'victory';

export default function MembersChart({ resources }) {

    const [minY, setMinY] = useState(0);
    const [maxY, setMaxY] = useState(0);

    useEffect(() => {
        // Calculate the minimum and maximum members
        const memberCounts = resources.map(resource => resource.members);
        setMinY(Math.min(...memberCounts));
        setMaxY(Math.max(...memberCounts) * 1.1); // Adding 10% for better visibility
    }, [resources]);
    const skip = Math.ceil(resources.length / 10); // Adjust this number based on your testing

    const formatMembersNumber = (members) => {
        if (members >= 1000 && members < 1000000) {
            return `${members / 1000}k`;
        }
        if (members >= 1000000) {
            return `${members / 1000000}M`;
        }
        return members;
    };

    return (
        <VictoryChart
            width={500}
            height={300}
            padding={{ top: 20, bottom: 50, left: 60, right: 35 }}
            containerComponent={
                <VictoryVoronoiContainer voronoiDimension="x" />
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
                tickFormat={(x, index) => {
                    if (index % skip === 0) {
                        return new Date(x).toLocaleDateString();
                    }
                    return '';
                }}
                style={{
                    ticks: { stroke: "#e0e0e0", size: 5 },
                    tickLabels: { fontSize: 9, padding: 5, angle: 45, textAnchor: 'start', fill: "#e0e0e0" },
                    grid: { stroke: "#3b485c", strokeWidth: 0.5 }
                }}
            />
            <VictoryAxis
                dependentAxis
                tickFormat={(y) => formatMembersNumber(y)}
                domain={[minY, maxY]}  // Set the domain dynamically
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
