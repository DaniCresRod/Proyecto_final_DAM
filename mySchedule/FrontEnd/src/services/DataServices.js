import axiosConnection from '../httpCommon';

export default({

    //Interactuar con la tabla users
    async getUserById(id){
        return await axiosConnection.get(`/user/${id}`);
    },

    getAllUsers(){
        return axiosConnection.get(`/user`);
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

    updateAppo(id, Data){
        return axiosConnection.put('appo/update', Data);
    }    
})