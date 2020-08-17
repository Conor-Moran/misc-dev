cj.controller = {
    ArrowLeft: function() {
        console.log("ArrowLeft");
        cj.universe.cueBall.setVelocity({x: 15.0, y: 0});
    },
    ArrowRight: function() {
        console.log("ArrowR");
    },

};

window.onkeydown = (evt) => {
    //console.log(evt);
    cj.controller[evt.code]();
};