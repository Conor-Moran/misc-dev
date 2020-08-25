cj.universe = {
    // TODO debug only
    collideFlag: true,

    bodiesToDraw: [
        new cj.Body(cj.assets.table1, {w: 1200, h: 600}),
    ],

    balls: [
        new cj.Body(cj.assets.cueBall, {x: 150, y: 200, w: 26, h: 26}),
        new cj.Body(cj.assets.redBall, {x: 818, y: 285, w: 26, h: 26}),
        // new cj.Body(cj.assets.redBall, {x: 841, y: 272, w: 26, h: 26}),
        // new cj.Body(cj.assets.yellowBall, {x: 841, y: 298, w: 26, h: 26}),
        // new cj.Body(cj.assets.yellowBall, {x: 865, y: 259, w: 26, h: 26}),
        // new cj.Body(cj.assets.blackBall, {x: 865, y: 285, w: 26, h: 26}),
        // new cj.Body(cj.assets.redBall, {x: 865, y: 311, w: 26, h: 26}),
        // new cj.Body(cj.assets.redBall, {x: 888, y: 246, w: 26, h: 26}),
        // new cj.Body(cj.assets.yellowBall, {x: 888, y: 272, w: 26, h: 26}),
        // new cj.Body(cj.assets.redBall, {x: 888, y: 298, w: 26, h: 26}),
        // new cj.Body(cj.assets.yellowBall, {x: 888, y: 324, w: 26, h: 26}),
        // new cj.Body(cj.assets.yellowBall, {x: 912, y: 233, w: 26, h: 26}),
        // new cj.Body(cj.assets.redBall, {x: 912, y: 259, w: 26, h: 26}),
        // new cj.Body(cj.assets.yellowBall, {x: 912, y: 285, w: 26, h: 26}),
        // new cj.Body(cj.assets.yellowBall, {x: 912, y: 311, w: 26, h: 26}),
        // new cj.Body(cj.assets.redBall, {x: 912, y: 337, w: 26, h: 26}),
    ],

    init: function() {
        this.canvas = cj.canvas;
        this.gCtx = cj.gCtx;
        this.cueBall = this.balls[0];
        this.blackBall = this.balls[5];
        this.tracker = new cj.Tracker(this.cueBall, 0);
        this.cueBall.onStop = () => {this.tracker.hidden = false};
        this.bodiesToDraw.push(...this.balls);
        this.bodiesToDraw.push(this.tracker);
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

    detectCollisions: function() {
        for (let i = 0; i < this.balls.length; i++) {
            for (let j = i + 1; j < this.balls.length; j++) {
                const ball1 = this.balls[i];
                const ball2 = this.balls[j];
                //if (this.collideFlag && ball1.detectCollision(ball2)) {
                if (ball1.detectCollision(ball2)) {
                    this.collide(ball1, ball2);
                    this.collideFlag = false;
                }
            }
        }
    },

    collide: function(ball1, ball2) {
        let v1 = new cj.Vec2d(ball1.velocity);
        let v2 = new cj.Vec2d(ball2.velocity);

        let norm = new cj.Vec2d({
            x: ball2.sizePos.x - ball1.sizePos.x,
            y: ball2.sizePos.y - ball1.sizePos.y,
        });

        let unitNorm = norm.unit();
        let unitTan = unitNorm.unitNorm();

        let v1FNorm = v2.project(unitNorm).times(-1);
        let v1FTng = v1.project(unitTan);
        let v2FNorm = v1.project(unitNorm);
        let v2FTng = v2.project(unitTan);


        let v1f = v1FNorm.plus(v1FTng);
        let v2f = v2FNorm.plus(v2FTng);
        
        ball1.setVelocity(v1f.components);
        ball2.setVelocity(v2f.components);
    },

    update: function() {
        this.clear();
        this.draw();
        this.eachBody(item => { item.update(); });
        this.detectCollisions();
        requestAnimationFrame(() => {
            cj.universe.update();
        });
    },
};

cj.assets.onload = () => {
    cj.universe.init();
    cj.universe.update();
};

