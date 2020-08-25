cj.Tracker = function(cueBall, rotation) {
    this.gCtx = cj.gCtx;
    this.canvas = cj.canvas;
    this.cueBall = cueBall;
    this.rotation = rotation;
    this.power = 0.0;
    this.hidden = false;
};

Object.assign(cj.Tracker.prototype, {
    draw: function() {
        if (this.hidden) return;

        with(this.gCtx) {
            strokeStyle = 'white';
            save();
            beginPath();
            moveTo(this.cueBall.origin.x, this.cueBall.origin.y);
            setLineDash([5, 15]);
            translate(this.cueBall.origin.x, this.cueBall.origin.y);
            rotate(this.rotation);
            lineTo(300, 0);
            stroke();
            restore();
        }
    },

    update: function() {
    },

    updateRotation: function(delta) {
        this.rotation += delta;
        if (this.rotation > Math.PI * 2) {
            this.rotation = 0;
        }
    },

    updatePower: function(delta) {
        this.power += delta;
        if (this.power > 100) {
            this.power = 0;
        }
    }
});

