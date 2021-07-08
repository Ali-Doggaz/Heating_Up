function getObj(objID)
{   
    if (document.getElementById) {return document.getElementById(objID);}
    else if (document.all) {return document.all[objID];}
    else if (document.layers) {return document.layers[objID];}
}


function show_value() {
		var minute, hour, day, month, weekday, cmd;
		
		minute	= getSelection('minute');
		hour	= getSelection('hour');
		day		= getSelection('day');
		month	= getSelection('month');
		weekday	= getSelection('weekday');
		getObj("cron").value = minute + "\t" + hour + "\t" + day + "\t" + month + "\t" + weekday + "\t"  ;
}

function getSelection(name) {
	var chosen;
		var all_selected = [];
		var a=getObj(name);
		for ( var index=a.options.length -1 ; index >= 0; --index) {
			//console.log(a.options.length);

			if(a[index].selected) {
				if( a[index].value == '*' ) {
					chosen = '*';
					return chosen;
				}
				all_selected.push(a[index].value);
			}
		}		

		if(all_selected.length)
			chosen = all_selected.join(",");
		else
			chosen = '*';
	return chosen;
}

