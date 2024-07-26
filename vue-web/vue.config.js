// const path = require('path');

// module.exports = {
//   chainWebpack: config => {
//     config.module
//       .rule('images')
//       .test(/\.(png|jpe?g|gif|svg)(\?.*)?$/)
//       .use('url-loader')
//         .loader('url-loader')
//         .tap(options => {
//           return {
//             limit: 8192,
//             name: 'img/[name].[hash:8].[ext]',
//           };
//         });
//   },
// };