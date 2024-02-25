import axios from "axios";

const instance=axios.create({
    baseURL:"http://localhost:18080",
    headers:{
        "Content-type": "application/json",            
    }
});

instance.interceptors.request.use(
        (config) => {
          const token = localStorage.getItem("userToken");
          
          if (token) {
            config.headers["Authorization"] = `Bearer ${token}`;
            config.headers["Content-type"] = 'application/json';                
          }
          return config;
        },
        (error) => {
          return Promise.reject(error);
        }
      );
        
export default instance;