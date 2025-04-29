import { useAxios } from "./useAxios";

export const useUsers = () => {

    const {data, error, loading, fetchData} = useAxios()

    const getAllUsers = async () => {
        const params = {
            url: "/users",
            method: "GET",
            body : null,
            params: null
        };
        await fetchData(params);
        return data;
    }
    const updateUser = async (user: any) => {
        const params = {
            url: `/users`,
            method: "PUT",
            body : user,
            params: null
        };
        await fetchData(params);
        return data;
    }

    const inActivateUser = async (id: number) => {
        const params = {
            url: `/users/${id}/inactivate`,
            method: "PUT",
            body : null,
            params: null
        };
        await fetchData(params);
        return data;
    }

    const activateUser = async (id: number) => {
        const params = {
            url: `/users/${id}/activate`,
            method: "PUT",
            body : null,
            params: null
        };
        await fetchData(params);
        return data;
    }

    const deleteUser = async () => {
        const params = {
            url: `/users`,
            method: "DELETE",
            body : null,
            params: null
        };
        await fetchData(params);
        return data;
    }


  return {
    data,
    error,
    loading,
    getAllUsers,
    updateUser,
    inActivateUser,
    activateUser,
    deleteUser
  };
}
