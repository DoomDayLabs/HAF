/**
 * 
 */
var tempSensor = IntSensor("TEMPERATURE",0,10);
var heatTrigger = Trigger("HEAT",[IntParam(30,90)]);

heatTrigger.on(function(temp){
	
});


function loop(){
	
}