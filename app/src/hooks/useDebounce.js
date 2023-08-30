import { useCallback } from 'react';

const useDebounce = (callback, delay) => {
    const debouncedCallback = useCallback(
        (...args) => {
            const timeoutId = setTimeout(() => {
                callback(...args);
            }, delay);

            return () => {
                clearTimeout(timeoutId);
            };
        },
        [callback, delay]
    );

    return debouncedCallback;
};

export default useDebounce;