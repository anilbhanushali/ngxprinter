var Printer = {
    connectPrinter: function(address, successCallback, errorCallback) {
 		cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'Printer', // mapped to our native Java class called "Printer"
            'connect', // with this action name
            [{                  // and this array of custom arguments to create our entry
                "macaddress": address
            }]
        ); 
    },
    PrintText : function(macaddress,text, alignment, attribute, textsize,successCallback,errorCallback){
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'Printer', // mapped to our native Java class called "Printer"
            'printtext', // with this action name
            [{   
                "macaddress":macaddress,// and this array of custom arguments to create our entry
                "text": text,
                "alignment":alignment,
                "attribute":attribute,
                "textsize":textsize
            }]
        ); 
    }
}
module.exports = Printer;