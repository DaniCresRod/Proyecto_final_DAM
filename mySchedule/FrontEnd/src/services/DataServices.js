import axiosConnection from '../httpCommon';

export default({
    //Interactuar con el login
    async getLogged(logData){
        let response;
        try{
            response=await axiosConnection.post(`/auth`, logData);
            return response.data;
        }
        catch(e){
            if((typeof(e.response)!=="undefined") && (e.response.status===400)) return "userOrPasswordError";
            else return "ConnectionError";
        }        
    },

    async getAlive(){
        try{
            let response=await axiosConnection.get('/auth');
            return response.data;
        }
        catch(e){console.log(e);
            window.localStorage.clear();
            return e.code;
        }
    },

    //Interactuar con la tabla users
    async getUserById(id){
        return await axiosConnection.get(`/user/${id}`);
    },

    async getAllUsers(){        
        return axiosConnection.get('/user');
    },

    async getAllUsersInTheWeekOf(date){        
        return await axiosConnection.get(`/user/date/${date}`);
    },

    deleteById(id){
        return axiosConnection.delete(`/user/delete/${id}`);
    },

    saveNewUser(Data){
        return axiosConnection.post('/user/add', Data);
    },

    updateUser(id, Data){
        return axiosConnection.put(`user/update/${id}`, Data);
    },

    //Interactuar con la tabla appointments
    saveAppo(Data){
        return axiosConnection.post('/appo/add', Data);
    },

    getAppoByDate(date){
        return axiosConnection.get(`/appo/${date}`);
    },

    deleteAppoById(id){
        return axiosConnection.delete(`/appo/delete/${id}`);
    },

    async updateAppo(Data){
        return await axiosConnection.put('appo/update', Data);
    }    
})