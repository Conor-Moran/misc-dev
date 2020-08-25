cj.controller = {
    ArrowUp: function() {
    },
    ArrowDown: function() {
    },
    ArrowLeft: function() {
        cj.universe.tracker.updateRotation(-0.01);
        console.log(cj.universe.tracker.rotation);
    },
    ArrowRight: function() {
        cj.universe.tracker.updateRotation(0.01);
        console.log(cj.universe.tracker.rotation);
    },
    Space: function() {
        cj.universe.tracker.hidden = true;
        cj.universe.cueBall.strike(cj.universe.tracker.power,
            cj.universe.tracker.rotation);
    },
    KeyP: function() {
        cj.universe.tracker.updatePower(1);
    }
};

window.onkeydown = (evt) => {
    console.log(evt);
    cj.controller[evt.code]();
};