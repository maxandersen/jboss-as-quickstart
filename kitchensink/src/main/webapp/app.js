function getRootUri() {
    return "ws://" + (document.location.hostname === "" ? "localhost" : document.location.hostname) + ":" +
        (document.location.port === "" ? "8080" : document.location.port);
}

var MembersModel = function(members) {
    var self = this;
    
    this.members = ko.observableArray(members);
    
    this.addItem = function(member) {
            this.members.push(member); 
    }.bind(this);  // Ensure that "this" is always this view model
    
    this.init = function() {
        self.websocket = new WebSocket(getRootUri() + "/jboss-as-kitchensink/registration");
        self.websocket.onopen = function (evt) {
            console.log ('open');
            window.mm.websocket.send("sending");    
        };
        self.websocket.onmessage = function (evt) {
            console.log(evt);
            var m = new Member();
            var dataobj = JSON.parse(evt.data);
            m.name(dataobj.name);
            m.email(dataobj.email);
            m.phoneNumber(dataobj.phoneNumber);
            window.mm.addItem(m);
        };
        self.websocket.onerror = function (evt) {
            console.log("ERROR: " + evt.data);
        };
        self.websocket.onclose = function (evt) { 
            console.log ('closed'); 
        };
    };
};

function Member() {
    var self = this;
    self.name = ko.observable("");
    self.email = ko.observable("");
    self.phoneNumber = ko.observable("");
}

var mm = new MembersModel([]);
window.mm = mm;
ko.applyBindings(mm);

window.addEventListener("load", mm.init, false);

