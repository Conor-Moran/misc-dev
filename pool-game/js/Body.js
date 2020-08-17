cj.Body = function(asset, sizePos, velocity) {
    this.defaultVec2D = {x: 0.0, y: 0.0};
    this.defaultSizePos = Object.assign({}, this.defaultVec2D, {w: 100, h: 100});
    this.velocity = Object.assign({}, this.defaultVec2D, velocity);
    this.accel = Object.assign({}, this.defaultVec2D, {x: 0.98, y: 0.98});
    this.gCtx = cj.gCtx;
    this.asset = asset;
    this.sizePos = Object.assign({}, this.defaultSizePos, sizePos);
};

Object.assign(cj.Body.prototype, {
    draw: function() {
        this.gCtx.drawImage(this.asset, this.sizePos.x, this.sizePos.y, this.sizePos.w, this.sizePos.h);
    },

    setVelocity: function(velocity) {
        Object.assign(this.velocity, this.defaultVec2D, velocity);
    },


    update: function() {
        const newPos = Object.assign({}, this.sizePos);
        newPos.x += this.velocity.x;
        newPos.y += this.velocity.y;
        Object.assign(this.sizePos, newPos);
        this.velocity.x *= this.accel.x;
        this.velocity.y *= this.accel.y;
    }
});