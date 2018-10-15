var config = require('./build/WebPackHelper.js')
var path = require('path')
var HardSourceWebpackPlugin = require('./build/node_modules/hard-source-webpack-plugin');

module.exports = {
    entry: config.moduleName,
    output: {
        path: path.resolve('./bundle'),
        publicPath: '/build/',
        filename: 'kesselringio.bundle.js'
    },
    resolve: {
        modules: [
            path.resolve('classes/kotlin/main'),
            path.resolve('../../common-js/build/classes/kotlin/main'),
            path.resolve('resources/main'),
            path.resolve('node_modules'),
        ]
    },
    plugins: [
        new HardSourceWebpackPlugin()
    ],
    module: {},
    //devtool: '#source-map',
    mode: 'development'
};

console.log(module.exports.resolve.modules);