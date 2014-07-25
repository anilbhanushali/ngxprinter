var Printer = {
    connectEvent: function(address, successCallback, errorCallback) {
 		cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'Printer', // mapped to our native Java class called "Printer"
            'connect', // with this action name
            [{                  // and this array of custom arguments to create our entry
                "macaddress": address
            }]
        ); 
    }
}
module.exports = Printer;