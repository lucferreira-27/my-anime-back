import { useState, useCallback } from 'react';
import GIF from 'gif.js';
import domtoimage from 'dom-to-image';

const useGifGenerator = (timeDisplayRef, resources, setTimeMedia, filename = 'timeline.gif') => {
    const [isGenerating, setIsGenerating] = useState(false);

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

        const gif = new GIF({
            workers: 2,
            workerScript: '/gif.worker.js', // Directly reference the file from the public directory
            quality: 1, 
            dither: false
        });
    
        for (let frame of frames) {
            const img = new Image();
            img.src = frame;
            await new Promise(r => img.onload = r);
            gif.addFrame(img, { delay: 500 });
        }
    
        return new Promise((resolve, reject) => {
            gif.on('finished', blob => {
                resolve(blob);
            });
            gif.render();
        });
    };

    const createGif = useCallback(async () => {
        try {
            setIsGenerating(true);
            const node = timeDisplayRef.current;
            const frames = [];

            for (let resource of resources) {
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

                    await new Promise(resolve => setTimeout(resolve, 100));

                    const dataUrl = await domtoimage.toPng(node);
                    frames.push(dataUrl);
                }
            }

            const blob = await convertFramesToGif(frames);
            const gifUrl = URL.createObjectURL(blob);

            const a = document.createElement('a');
            a.href = gifUrl;
            a.download = filename;
            a.click();

            setIsGenerating(false);
        } catch (error) {
            console.error(error);
            setIsGenerating(false);
        }
    }, [timeDisplayRef, resources, setTimeMedia]);

    return { isGenerating, createGif };
};

export default useGifGenerator