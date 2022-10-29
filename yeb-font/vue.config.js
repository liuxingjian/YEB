let proxyObj = {}
//代理所有'/'
proxyObj['/'] = {
    //websocket
    ws: false,
    //目标地址
    target: 'http://localhost:9090',
    //发送请求头host会被设置成target
    changeOrigin: true,
    //不重写请求地址
    pathRewrite: {
        '^/': '/'
    }
}


proxyObj['/ws'] = {
    //websocket
    ws: true,
    //目标地址
    target: 'http://localhost:9090',
}

module.exports = {
    devServer: {
        host: 'localhost',
        port: 9000,
        proxy: proxyObj,
    }
}
