require('dotenv').config();
var isProduction = process.env.production === 'true';
var packageJSON = require('./package.json');
var path = require('path');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
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
  {
    test: /\.(css)$/,
    loader: ExtractTextPlugin.extract({
        use: 'css-loader'
    })
  },
  {
    test: /\.(eot|ttf|woff|woff2|svg)(\?v=\d+\.\d+)?$/,
    loader: "file-loader",
    options: {
        name: 'fonts/[name].[ext]'
    }
  }
]

module.exports = {
  context: jsSourcePath,

  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8443/',
        secure: false,
        changeOrigin: true
      }
    }
  },

  devtool: isProduction ? "" : "#inline-source-map",

  module: {
    rules
  },

  entry: {
    js: './index.js'
  },

  output: {
    path: PATHS.build,
    publicPath: isProduction ? '/app' : '/',
    filename: 'js/app-bundle.js'
  },

  plugins: [new HtmlWebpackPlugin({
    template: 'index.ejs',
    filename: './index.html'
  }),
  new ExtractTextPlugin('[name].css')
  ]
};
