module.exports = {
    connectPrinter: function(address, successCallback, errorCallback) {
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'NGXPrinter', // mapped to our native Java class called "NGXPrinter"
            'connect', // with this action name
            [{                  // and this array of custom arguments to create our entry
                "macaddress": address
            }]
        ); 
    },
    showDeviceList: function(successCallback,errorCallback){
        cordova.exec(
            successCallback,
            errorCallback,
            'NGXPrinter',
            'showdevicelist',
            []
        );
    },
    PrintText : function(macaddress,text, alignment, attribute, textsize,successCallback,errorCallback){
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'NGXPrinter', // mapped to our native Java class called "NGXPrinter"
            'printtext', // with this action name
            [{   
                "macaddress":macaddress,// and this array of custom arguments to create our entry
                "text": text,
                "alignment":alignment,
                "attribute":attribute,
                "textsize":textsize
            }]
        ); 
    },
    PrintImage : function(macaddress,file,successCallback,errorCallback){
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'NGXPrinter', // mapped to our native Java class called "NGXPrinter"
            'printimage', // with this action name
            [{   
                "macaddress":macaddress,// and this array of custom arguments to create our entry
                "file": file
            }]
        );
    }
};
