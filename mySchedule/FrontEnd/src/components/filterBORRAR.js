import { myFilteredResponse, props } from './AllAppointmentComponent.vue';

function filter(theObjectArray) {
myFilteredResponse.value = (theObjectArray.value).filter(x => {
return ((x.name.toLowerCase()).includes(props.myfilter.toLowerCase()) || (x.alias.toLowerCase()).includes(props.myfilter.toLowerCase()));
});

}
