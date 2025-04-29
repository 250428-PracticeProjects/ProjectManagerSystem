import { useState } from "react";
import axios, { AxiosRequestConfig, Method } from "axios";

export const useAxios = () => {
    const [data, setData] = useState<any>(null);
    const [error, setError] = useState<any>(null);
    const [loading, setLoading] = useState<boolean>(false);

    interface AxiosFetchParams {
        url: string;
        method: String;
        body?: any | null;
        params?: any | null;
    }

    const fetchData = async (params: AxiosFetchParams): Promise<void> => {
        setLoading(true); // Establecer loading a true al inicio

        const sleep = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));
        await sleep(2000); // Simular un retraso de 2 segundos (2000 ms)
        try {
            let config: AxiosRequestConfig = {
                url: params.url,
                method: params.method.trim().toUpperCase() as Method,
                withCredentials: true,
                headers: {
                    "Content-Type": "application/json",
                },
            };

            if (params.method.toUpperCase() !== "GET" && params.body) {
                config.data = params.body;
            }

            setError(null);
            const response = await axios.request(config);
            setData(response.data);
        } catch (error) {
            setData([]);
            if (axios.isAxiosError(error)) {
                setError("Axios Error with Message: " + error.message);
            } else {
                setError(error);
            }
        } finally {
            setLoading(false); // Establecer loading a false al final (éxito o error)
        }
    };

    return { data, error, loading, fetchData };
};