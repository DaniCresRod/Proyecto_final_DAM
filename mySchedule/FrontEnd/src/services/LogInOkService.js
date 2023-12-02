import axiosConnection from './DataServices'

export function StoreUserData(userEmail, token, userId, userName, userRol){    
  window.localStorage.setItem("userEmail", userEmail);
  window.localStorage.setItem("userToken", token);
  window.localStorage.setItem("userId", userId);
  window.localStorage.setItem("userName", userName);
  window.localStorage.setItem("userRol", userRol);

  if(userRol==="Admin") return true;
  else return false;
}

export function ShowLogInErrorMessage(message){
  document.getElementById("logInError").innerText=message;
  document.getElementById("logInError").classList.remove("invisible");
}

export async function LogIn(userName, userPass){
  window.localStorage.clear();
  document.getElementById("logInError").classList.add("invisible");             

  userName=(userName.toLowerCase()).trim();                                     //email en minusculas y sin espacios

  if(userName!=="" && userPass!==""){
    const regExEmail=new RegExp('[a-z0-9]+@[a-z0-9]+.[a-z0-9]{2,3}');          //Revisar si tiene formato de email
  
    if(regExEmail.test(userName)){
      const logData={username:userName, password:userPass};

      const response=await axiosConnection.getLogged(logData);
      if(response==="userOrPasswordError") ShowLogInErrorMessage("Usuario o contraseña incorrectos");
      else if(response==="logInError") ShowLogInErrorMessage("No se pudo conectar");                                 
      
      else{
        let isAdmin=StoreUserData(userName, response.token, response.userId, response.userName, response.userRole);
        //changeMainPageView(isAdmin);
        
        return [true,isAdmin];          
      } 
    }
    else ShowLogInErrorMessage("El formato de email no es correcto.");
  }
  else ShowLogInErrorMessage("Faltan campos por rellenar.");     
}

export async function checkLogIn(){
  let myToken=window.localStorage.getItem("userToken");
  if(myToken!==null){
    let response=await axiosConnection.getAlive();
    if(response!=="ERR_NETWORK"){
      //Aqui llega con token válido
      window.localStorage.setItem("userId", response.userId);
      window.localStorage.setItem("userName", response.userName);
      window.localStorage.setItem("userRol", response.userRole);      
      return true;
    }
    else console.log("ERR_NETWORK");
  }
  else console.log("Token null");
  return false;
}

// function changeMainPageView(isAdmin){
//   if(isAdmin) router.push("/");
//   else router.push("/allApointment");
// }