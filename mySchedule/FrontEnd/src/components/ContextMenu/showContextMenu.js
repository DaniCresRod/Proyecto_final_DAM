import { myStore } from './../UserAppointmentCalendar.vue';

export function showContextMenu(event) {
let popUpMenu = document.getElementById("div_contextMenu");
popUpMenu.classList.toggle("invisible");

let clickedUser = event.target.__vnode.key;
console.log(clickedUser);
myStore.whatsAppUser.name = clickedUser.user;
myStore.whatsAppUser.oldAppoDate = clickedUser.appoDay;
myStore.whatsAppUser.oldAppoStart = clickedUser.appoTime;
myStore.whatsAppUser.phone = clickedUser.phone;

// myStore.changeUser.appoId=
function hideMenuOnTouchMove() {
popUpMenu.classList.add("invisible");
document.removeEventListener("touchmove", hideMenuOnTouchMove);
}

if (typeof (event.touches) !== 'undefined') {
popUpMenu.style.top = event.touches[0].clientY + "px";
popUpMenu.style.left = event.touches[0].clientX + "px";

document.addEventListener("touchmove", hideMenuOnTouchMove, { passive: true });

}
else {
popUpMenu.style.top = event.clientY + "px";
popUpMenu.style.left = event.clientX + "px";
popUpMenu.addEventListener("mouseleave", () => {
popUpMenu.classList.add("invisible");
});
}

// console.log(navigator.userAgentData.platform);
}
