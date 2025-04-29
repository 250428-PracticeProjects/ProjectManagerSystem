
export interface IUser{
    userId: number,
    firstName: string,
    lastName: string,
    email: string,
    password: string,
    role: "USER" | "ADMIN"
    token: string,
}