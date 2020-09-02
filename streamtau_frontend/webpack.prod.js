const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');
const CopyPlugin = require('copy-webpack-plugin');

module.exports = merge(common, {
    mode: 'production',

    devtool: 'source-map',

    plugins: [
        new CopyPlugin({
            patterns: [
                {
                    from: './src/main/html/index.prod.html',
                    to: '../view/index.html'
                },
                {
                    from: './node_modules/react/umd/react.production.min.js',
                    to: 'js/lib'
                },
                {
                    from: './node_modules/react-dom/umd/react-dom.production.min.js',
                    to: 'js/lib'
                },
            ]
        })
    ],
});
