cj.universe = {
    bodiesToDraw: [
        new cj.Body(cj.assets.table1, {w: 1200, h: 600}),
    ],

    balls: [
        new cj.Body(cj.assets.cueBall, {x: 150, y: 200, w: 26, h: 26}),
        new cj.Body(cj.assets.redBall, {x: 818, y: 285, w: 26, h: 26}),
        new cj.Body(cj.assets.redBall, {x: 841, y: 272, w: 26, h: 26}),
        new cj.Body(cj.assets.yellowBall, {x: 841, y: 298, w: 26, h: 26}),
        new cj.Body(cj.assets.yellowBall, {x: 865, y: 259, w: 26, h: 26}),
        new cj.Body(cj.assets.blackBall, {x: 865, y: 285, w: 26, h: 26}),
        new cj.Body(cj.assets.redBall, {x: 865, y: 311, w: 26, h: 26}),
        new cj.Body(cj.assets.redBall, {x: 888, y: 246, w: 26, h: 26}),
        new cj.Body(cj.assets.yellowBall, {x: 888, y: 272, w: 26, h: 26}),
        new cj.Body(cj.assets.redBall, {x: 888, y: 298, w: 26, h: 26}),
        new cj.Body(cj.assets.yellowBall, {x: 888, y: 324, w: 26, h: 26}),
        new cj.Body(cj.assets.yellowBall, {x: 912, y: 233, w: 26, h: 26}),
        new cj.Body(cj.assets.redBall, {x: 912, y: 259, w: 26, h: 26}),
        new cj.Body(cj.assets.yellowBall, {x: 912, y: 285, w: 26, h: 26}),
        new cj.Body(cj.assets.yellowBall, {x: 912, y: 311, w: 26, h: 26}),
        new cj.Body(cj.assets.redBall, {x: 912, y: 337, w: 26, h: 26}),
    ],

    init: function() {
        this.canvas = cj.canvas;
        this.gCtx = cj.gCtx;
        this.cueBall = this.balls[0];
        this.blackBall = this.balls[5];
        this.bodiesToDraw.push(...this.balls);
    },

    eachBody: function(callback) {
        this.bodiesToDraw.forEach(callback);
    },

    draw: function() {
        this.eachBody(item => {
            item.draw();
        });
    },

    clear: function() {
        this.gCtx.clearRect(0, 0, this.canvas.width, this.canvas.height);
    },

    update: function() {
        this.clear();
        this.draw();
        this.eachBody(item => { item.update(); });
        requestAnimationFrame(() => {
            cj.universe.update();
        });
    },
};

cj.assets.onload = () => {
    cj.universe.init();
    cj.universe.update();
};

