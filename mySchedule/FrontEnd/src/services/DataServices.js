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
        catch(e){
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

    async deleteById(id){
        return await axiosConnection.delete(`/user/delete/${id}`);
    },

    async saveNewUser(Data){
        return await axiosConnection.post('/user/add', Data);
    },

    async updateUser(id, Data){
        return await axiosConnection.put(`user/update/${id}`, Data);
    },

    //Interactuar con la tabla appointments
    async saveAppo(Data){
        return await axiosConnection.post('/appo/add', Data);
    },

    getAppoByDate(date){
        return axiosConnection.get(`/appo/${date}`);
    },

    async deleteAppoById(id){
        return await axiosConnection.delete(`/appo/delete/${id}`);
    },

    async updateAppo(Data){
        return await axiosConnection.put('appo/update', Data);
    }, 
    
    async updateAppoNotes(Data){
        return await axiosConnection.put('appo/updateNotes', Data);
    },

    async generateBill(id){
        return await axiosConnection.post('appo/genBill/'+id);
    }
})