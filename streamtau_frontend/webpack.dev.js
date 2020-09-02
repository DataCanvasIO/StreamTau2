const path = require('path');
const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');
const CopyPlugin = require('copy-webpack-plugin');

module.exports = merge(common, {
    mode: 'development',

    devtool: 'inline-source-map',

    plugins: [
        new CopyPlugin({
            patterns: [
                {
                    from: './src/main/html/index.dev.html',
                    to: 'index.html'
                },
                {
                    from: './node_modules/react/umd/react.development.js',
                    to: 'js/lib'
                },
                {
                    from: './node_modules/react-dom/umd/react-dom.development.js',
                    to: 'js/lib'
                },
            ]
        })
    ],

    devServer: {
        contentBase: path.join(__dirname, 'dist'),
        compress: false,
        port: 9000
    },
});
