function loadComments () {

    this.source = null;

    this.start = function () {

        var commentTable = document.getElementById("notifications");

        this.source = new EventSource("/sse");

        console.log(this.source);
        if (window.EventSource) {
            console.log("Soporta eventos")
        }

        this.source.onmessage = function (event) {
            // These events are JSON, so parsing and DOM fiddling are needed
            var notification = JSON.parse(event.data);
            console.log(notification)

            var row = commentTable.getElementsByTagName("tbody")[0].insertRow(0);
            var cell0 = row.insertCell(0);
            var cell1 = row.insertCell(1);

            cell0.innerHTML = notification.id;
            cell1.innerHTML = notification.message;

        };

        this.source.addEventListener('open', function(e) {
         console.log("Abriendo conexión")
        }, false);

        this.source.onerror = function () {
            console.log("Error, cerrando...")
            this.close();
        };

    };

    this.stop = function() {
        console.log("Parando...")
        this.source.close();
    }

}

comment = new loadComments();

/*
 * Register callbacks for starting and stopping the SSE controller.
 */
window.onload = function() {
    comment.start();
};
window.onbeforeunload = function() {
    comment.stop();
}