var packageJSON = require('./package.json');
var path = require('path');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var webpack = require('webpack');

const jsSourcePath = path.join(__dirname, './webapp/');
const sourcePath = path.join(__dirname, './webapp');

const PATHS = {
  build: path.join(__dirname, 'target', 'classes', 'META-INF', 'resources', 'webjars', packageJSON.name, packageJSON.version)
};

const rules = [
  {
    test: /\.(js|jsx)$/,
    exclude: /node_modules/,
    use: [
      'babel-loader',
    ],
  },
]

module.exports = {
  context: jsSourcePath,

  module: {
    rules
  },

  entry: {
    js: './index.js'
  },

  output: {
    path: PATHS.build,
    publicPath: '/',
    filename: 'app-bundle.js'
  },

  plugins: [new HtmlWebpackPlugin()]
};