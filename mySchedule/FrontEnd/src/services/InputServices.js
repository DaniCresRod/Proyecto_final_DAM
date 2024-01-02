export default({
    //Muestra u oculta una contraseña en el campo indicado
    ToggleImg(passwordField, imageId){
        const myImage=document.getElementById(imageId);
        if(document.getElementById(passwordField).type=="password"){    
          myImage.src="/src/assets/Images/iconoMostrar.png";  
          document.getElementById(passwordField).type="text"; 
        }
        else{    
          myImage.src="/src/assets/Images/iconoOculto.png";  
          document.getElementById(passwordField).type="password";
        }
      }




})