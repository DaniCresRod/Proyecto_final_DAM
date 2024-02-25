import iconoOculto from "@/assets/Images/iconoOculto.png";
import iconoMostrar from "@/assets/Images/iconoMostrar.png";

export default({
  //Muestra u oculta una contraseña en el campo indicado
  ToggleImg(passwordField, imageId){
    const myImage=document.getElementById(imageId);
    if(document.getElementById(passwordField).type=="password"){    
      // myImage.src="@/assets/Images/iconoMostrar.png"; 
      myImage.setAttribute('src',iconoMostrar ); 
      document.getElementById(passwordField).type="text"; 
    }
    else{    
      // myImage.src="@/assets/Images/iconoOculto.png";  
      myImage.setAttribute('src',iconoOculto);
      document.getElementById(passwordField).type="password";
    }
  }
})