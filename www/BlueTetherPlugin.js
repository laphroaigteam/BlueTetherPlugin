module.exports = {
//
// test
    connect: function (success, failure) {
        cordova.exec(success, failure, "BlueTetherPlugin", "connect", []);
    },
	setTetherOn: function (success, failure) {
        cordova.exec(success, failure, "BlueTetherPlugin", "setTetherOn", []);
    }
}
