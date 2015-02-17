module.exports = {
    getStatus: function(successCallback, errorCallback) {
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'NGXPrinter', // mapped to our native Java class called "NGXPrinter"
            'getstatus', // with this action name
            []
        ); 
    },
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
    clearPrinters: function(successCallback,errorCallback){
        cordova.exec(
            successCallback,
            errorCallback,
            'NGXPrinter',
            'clearprinters',
            []
        );
    },
    SetFontSize : function(textsize,successCallback,errorCallback){
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'NGXPrinter', // mapped to our native Java class called "NGXPrinter"
            'setfontsize', // with this action name
            [{   
                // and this array of custom arguments to create our entry
                "textsize":textsize
            }]
        ); 
    },
    SetFontStyle : function(attribute,successCallback,errorCallback){
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'NGXPrinter', // mapped to our native Java class called "NGXPrinter"
            'setfontstyle', // with this action name
            [{   
                "attribute":attribute
            }]
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
    SetImage : function(file,successCallback,errorCallback){
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'NGXPrinter', // mapped to our native Java class called "NGXPrinter"
            'setimage', // with this action name
            [{   
                "file": file
            }]
        );
    },
    PrintImage : function(successCallback,errorCallback){
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'NGXPrinter', // mapped to our native Java class called "NGXPrinter"
            'printimage', // with this action name
            []
        );
    }
};
