import { IFormLogin } from "../interfaces/IFormLogin";
import { useAxios } from "./useAxios";

export const useAuth = () => {
    const { data, error, loading, fetchData } = useAxios();

    const login = async (data: IFormLogin) => {
        const params = {
            url: `/auth/login`,
            method: "POST",
            body: data,
            params: null
        };
        // Devolvemos la promesa de fetchData
        return await fetchData(params);
    };

    const registerUser = async (user: any) => {
        const params = {
            url: `/auth/register`,
            method: "POST",
            body: user,
            params: null
        };
        // Devolvemos la promesa de fetchData
        return await fetchData(params);
    };

    return {
        data,
        error,
        loading,
        login,
        registerUser
    };
};