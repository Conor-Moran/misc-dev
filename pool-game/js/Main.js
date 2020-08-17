let cj = {};

cj.canvas = document.getElementById('canvas');
cj.gCtx = cj.canvas.getContext('2d');

cj.canvas.addEventListener('mousedown', function(e) {
    const rect = this.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    console.log("x: " + x + " y: " + y);
});

