cj.Vec2d = function (components) {
    this.components = Object.assign({}, components);
}

cj.Vec2d.zero = new cj.Vec2d({x: 0, y: 0});
cj.Vec2d.unitI = new cj.Vec2d({x: 1, y: 0});
cj.Vec2d.unitJ = new cj.Vec2d({x: 0, y: 1});

Object.assign(cj.Vec2d.prototype, {

    times: function(mult) {
        return new cj.Vec2d({
            x: this.components.x * mult,
            y: this.components.y * mult
        });
    },

    div: function(divisor) {
        return this.times( 1 / divisor );
    },

    plus: function(other) {
        return new cj.Vec2d({
            x: this.components.x + other.components.x,
            y: this.components.y + other.components.y,
        });
    },

    unit: function() {
        const mag = Math.sqrt(this.components.x ** 2 + this.components.y ** 2);
        return this.div(mag);
    },

    unitNorm: function() {
        const unit = this.unit();
        return new cj.Vec2d({
            x: -unit.components.y,
            y: unit.components.x
        });
    },

    dot: function(other) {
        return this.components.x * other.components.x
            + this.components.y * other.components.y;
    },

    projectI: function() {
        this.project(cj.Vec2d.unitI);
    },

    projectJ: function() {
        this.project(cj.Vec2d.unitJ);
    },

    project: function(other) {
        const projectedUnit = other.unit();
        const mag = this.dot(projectedUnit);
        return projectedUnit.times(mag);
    }
});