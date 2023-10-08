import { useState, useCallback, useEffect } from 'react';
import GIF from 'gif.js';
import domtoimage from 'dom-to-image';

const useGifGenerator = (config,timeDisplayRef, resources, setTimeMedia, filename = 'timeline') => {
    const [isGenerating, setIsGenerating] = useState(false);
    useEffect(() => {
        console.log(config)
    }, [config])
    const dataURLtoBlob = (dataUrl) => {
        const arr = dataUrl.split(',');
        const mime = arr[0].match(/:(.*?);/)[1];
        const bstr = atob(arr[1]);
        let n = bstr.length;
        const u8arr = new Uint8Array(n);
        while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
        }
        return new Blob([u8arr], { type: mime });
    }

    const convertFramesToGif = async (frames) => {
        console.log(config)
        const gif = new GIF({
            workers: 2,
            workerScript: '/gif.worker.js', // Directly reference the file from the public directory
            quality: config.quality,
            dither: config.dither,
        });

        for (let frame of frames) {
            const img = new Image();
            img.src = frame;
            await new Promise(r => img.onload = r);
            gif.addFrame(img, { delay: config.delay });
        }

        return new Promise((resolve, reject) => {
            gif.on('finished', blob => {
                resolve(blob);
            });
            gif.render();
        });
    };
    const formatFileName = (filename) => {
        const cleanFile = filename
            .replace(/[^a-zA-Z0-9\s\.\-]/g, '')  // Remove illegal characters
            .replace(/\s+/g, '_')                // Replace spaces with underscores
            .toLowerCase();                      // Convert to lowercase
        return cleanFile + '.gif';
    }

    const createGif = useCallback(async () => {
        try {
            setIsGenerating(true);
            const node = timeDisplayRef.current;
            const frames = [];
            const totalResources = resources.length;
            const maxFrames = config.frames;  // The maximum frames you want in your GIF
    
            // Calculate the interval to skip frames while keeping the max limit
            const skipInterval = Math.floor(totalResources / maxFrames);
    
            // Loop through each resource
            for (let i = 0; i < totalResources; i += skipInterval) {
                const resource = resources[i];
                if (resource) {
                    setTimeMedia(prevTimeMedia => ({
                        ...prevTimeMedia,
                        archiveDate: resource.archiveDate,
                        score: resource.scoreValue,
                        rank: resource.ranked,
                        members: resource.members,
                        popularity: resource.popularity,
                        scored_by: resource.totalVotes
                    }));
    
                   // await new Promise(resolve => setTimeout(resolve, 10));
    
                    const dataUrl = await domtoimage.toPng(node);
                    frames.push(dataUrl);
                }
            }
    
            // Handle the last frame to ensure the timelapse reaches the end
            if (resources[totalResources - 1] && frames.length < maxFrames) {
                const resource = resources[totalResources - 1];
                setTimeMedia(prevTimeMedia => ({
                    ...prevTimeMedia,
                    archiveDate: resource.archiveDate,
                    score: resource.scoreValue,
                    rank: resource.ranked,
                    members: resource.members,
                    popularity: resource.popularity,
                    scored_by: resource.totalVotes
                }));
    
                await new Promise(resolve => setTimeout(resolve, 10));
    
                const dataUrl = await domtoimage.toPng(node);
                frames.push(dataUrl);
            }
    
            const blob = await convertFramesToGif(frames);
            const gifUrl = URL.createObjectURL(blob);
    
            const a = document.createElement('a');
            a.href = gifUrl;
            a.download = formatFileName(filename)
            a.click();
    
            setIsGenerating(false);
        } catch (error) {
            console.error(error);
            setIsGenerating(false);
        }
    }, [timeDisplayRef, resources, setTimeMedia, config]);
    

    return { isGenerating, createGif, };
};

export default useGifGenerator