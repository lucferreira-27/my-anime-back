import { closestTo, parse, isEqual, addDays, addMonths, addYears } from 'date-fns';

// A helper function to check if a date is within a range


const isInRange = (dateA, dateB, milesecondsDistance) => {
    const milesecondsInterval = getMilesecondsInterval(dateA, dateB)
    return milesecondsDistance > milesecondsInterval

};

const parseDate = (dateString) => {
    return new Date(dateString);
};


// A helper function to convert milliseconds to different units
const getDistanceUnit = (distance, unit) => {
    const unitMultipliers = {
        seconds: 1000,
        minutes: 1000 * 60,
        hours: 1000 * 60 * 60,
        days: 1000 * 60 * 60 * 24,
        months: 1000 * 60 * 60 * 24 * 30.44,
        years: 1000 * 60 * 60 * 24 * 365.25,
    };

    const multiplier = unitMultipliers[unit];

    if (multiplier === undefined) {
        throw new Error(`Invalid unit: ${unit}`);
    }

    return distance * multiplier;
};

const convertUnit = (ms, unit) => {
    switch (unit) {
        case "seconds":
            return ms / 1000;
        case "minutes":
            return ms / (1000 * 60);
        case "hours":
            return ms / (1000 * 60 * 60);
        case "days":
            return ms / (1000 * 60 * 60 * 24);
        case "months":
            return ms / (1000 * 60 * 60 * 24 * 30.44);
        case "years":
            return ms / (1000 * 60 * 60 * 24 * 365.25);
        default:
            return ms;
    }
};

const sortSnapshots = (snapshots) => {
    return snapshots.sort((a, b) => {
        return a.date < b.date
    })
}

const getMillisecondsInDate = (date) => {
    // Convert the date to a JavaScript Date object if it's not already
    if (!(date instanceof Date)) {
        date = new Date(date);
    }

    // Get the milliseconds since January 1, 1970 (Unix Epoch)
    return date.getTime();
};

const getMilesecondsInterval = (dateA, dateB) => {
    const milesecondsA = convertUnit(dateA, null)
    const milesecondsB = convertUnit(dateB, null)
    return Math.abs(milesecondsA - milesecondsB)
}

const getThrehold = (distanceType) => {
    // Define threshold values based on distanceType (you can adjust these values as needed)
    const thresholds = {
        "seconds": 1000, // 1 second
        "minutes": 60000, // 1 minute
        "hours": 3600000, // 1 hour
        "days": 86400000, // 1 day
        "months": 2592000000, // 30 days (approximate)
        "years": 31536000000, // 365 days (approximate)
    };
    return thresholds[distanceType] || 0;
};

const isExaclyInterval = (dateA, dateB, distance, distanceType) => {
    // Calculate the time difference in milliseconds between dateA and dateB
    const timeDifference = Math.abs(dateA - dateB);
    const milesecondsDistance = getDistanceUnit(distance, distanceType);

    // Get the threshold for the specified distanceType
    const threshold = getThrehold(distanceType);

    // Check if the time difference is within the threshold
    return timeDifference <= milesecondsDistance + threshold;
};


const addUnit = (date, number, unit) => {
    const unitFunctions = {
        "days": addDays, // 1 day
        "months": addMonths, // 30 days (approximate)
        "years": addYears, // 365 days (approximate)
    };
    return unitFunctions[unit](date, number);
}

function findClosestTo(date, arrayDates, direction) {
    if (arrayDates.length === 0) {
        return null; // Return null if the array is empty
    }

    if (direction === 'forwards') {

        // Find the first date in the array that is greater than or equal to the given date
        for (let i = 0; i < arrayDates.length; i++) {
            if (arrayDates[i].getTime() >= date.getTime()) {
                return arrayDates[i];
            }
        }
    } else if (direction === 'backwards') {
        // Find the last date in the array that is less than or equal to the given date
        for (let i = arrayDates.length - 1; i >= 0; i--) {
            if (arrayDates[i] <= date) {
                return arrayDates[i];
            }
        }
    }

    // If no date is found, return null
    return null;
}




// The main function to split the snapshots
const getSplitSnapshots = (snapshots, config) => {
    // Initialize an empty array to store the split snapshots
    const splitSnapshots = [];
    const { startDate: strStartDate, endDate: strEndDate, distanceType, distance } = config;
    const startDate = parseDate(strStartDate);
    const endDate = parseDate(strEndDate);
    const sortedDates = sortSnapshots(snapshots).map((snapshot) => snapshot.timestamp.date);
    console.log(sortedDates)
    const startDateMilliseconds = getMillisecondsInDate(startDate);
    const endDateMilliseconds = getMillisecondsInDate(endDate);
    const datesInRange = [];

    for (let i = 0; i < sortedDates.length; i++) {
        const currentDate = sortedDates[i];
        if (currentDate < startDateMilliseconds) {
            continue
        }
        // Calculate the milliseconds interval between the current date and the start date
        const interval = getMilesecondsInterval(currentDate, startDateMilliseconds);
        // Check if the interval falls within the specified distance
        if (interval <= getDistanceUnit(distance, distanceType)) {
            datesInRange.push(currentDate);
        }

        // If the current date is after the end date, stop the loop
        if (currentDate > endDateMilliseconds) {
            break;
        }
    }
    let nextDate = null
    const max = sortedDates.length
    const  dates = sortedDates.map(d => new Date(d))
    for (let i = 0; i < max; i++) {
        const currentDateMilliseconds = nextDate || sortedDates[i]
        if(currentDateMilliseconds < startDateMilliseconds || currentDateMilliseconds > endDateMilliseconds){
            continue
        }
        const currentDate = nextDate || new Date(currentDateMilliseconds)
        const targetDate = addUnit(currentDate, distance, distanceType)
        nextDate = findClosestTo(targetDate,dates, 'forwards')
        if(!nextDate){
            break
        }
        splitSnapshots.push(nextDate)
    }
    return splitSnapshots;
};

export default getSplitSnapshots;
