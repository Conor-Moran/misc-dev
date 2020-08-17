
cj.loadAsset = function(assetName) {
    let img = new Image();
    img.src = `assets/${assetName}.png`;
    img.onload = () => {
        cj.assets.loadingCount--;
        if (cj.assets.loadingCount == 0) {
            if (cj.assets.onload) {
                cj.assets.onload();
            }
        }
    }
    return img;
};


cj.assets = {
    loadingCount: 0,
};


(() => {
    let assetNames = [
        'table1',
        'cueBall',
        'blackBall',
        'redBall',
        'yellowBall',
    ];
    assetNames.forEach(assetName => {
        cj.assets.loadingCount++;
        cj.assets[assetName] = cj.loadAsset(assetName);
    });
})();

