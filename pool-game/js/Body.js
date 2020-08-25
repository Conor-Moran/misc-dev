cj.Body = function(asset, sizePos, velocity) {
    this.minVel = 0.3;
    this.defaultSizePos = Object.assign({}, cj.Vec2d.zero.components, {w: 100, h: 100});
    this.velocity = Object.assign({}, cj.Vec2d.zero.components, velocity);
    this.accel = Object.assign({}, cj.Vec2d.zero.components, {x: 0.98, y: 0.98});
    this.gCtx = cj.gCtx;
    this.asset = asset;
    this.sizePos = Object.assign({}, this.defaultSizePos, sizePos);
    this.updateOrigin();
};

Object.assign(cj.Body.prototype, {
    updateOrigin: function() {
        this.origin = {
            x: this.sizePos.x + this.sizePos.w/2,
            y: this.sizePos.y + this.sizePos.h/2,
        };    
    },

    draw: function() {
        this.gCtx.drawImage(this.asset, this.sizePos.x, this.sizePos.y, this.sizePos.w, this.sizePos.h);
    },

    strike: function(power, rotation) {
        const vX = Math.cos(rotation) * power;
        const vY = Math.sin(rotation) * power;
        this.setVelocity({
            x: vX,
            y: vY
        });
    },

    setVelocity: function(velocity) {
        this.velocity = Object.assign({}, cj.Vec2d.zero.components, velocity);
    },

    isMoving: function() {
        return this.isFasterThan(0);
    },

    isFasterThan: function(speed) {
        return Math.abs(this.velocity.x) > speed || Math.abs(this.velocity.y) > speed;
    },

    stop: function() {
        this.setVelocity(cj.Vec2d.zero.components);
        if (this.onStop) {
            this.onStop();
        }
    },

    detectCollision(other) {
        let isCollision = false;
        if (this.isMoving() || other.isMoving()) {
            let xDiff = other.origin.x - this.origin.x;
            let yDiff = other.origin.y - this.origin.y;
            let dist = Math.sqrt(xDiff ** 2 + yDiff ** 2);
            isCollision = dist < this.sizePos.w;
        }
        return isCollision;
    },

    update: function() {
        const newPos = Object.assign({}, this.sizePos);
        newPos.x += this.velocity.x;
        newPos.y += this.velocity.y;
        Object.assign(this.sizePos, newPos);
        this.updateOrigin();
        this.velocity.x *= this.accel.x;
        this.velocity.y *= this.accel.y;
        if (!this.isFasterThan(this.minVel)) {
            this.stop();
        }
    }
});
