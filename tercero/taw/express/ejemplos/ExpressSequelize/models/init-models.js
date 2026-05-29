var DataTypes = require("sequelize").DataTypes;
var _ADMINISTRADOR = require("./ADMINISTRADOR");
var _CUSTOMER = require("./CUSTOMER");
var _DISCOUNT_CODE = require("./DISCOUNT_CODE");
var _MANUFACTURER = require("./MANUFACTURER");
var _MICRO_MARKET = require("./MICRO_MARKET");
var _PRODUCT = require("./PRODUCT");
var _PRODUCT_CODE = require("./PRODUCT_CODE");
var _PURCHASE_ORDER = require("./PURCHASE_ORDER");

function initModels(sequelize) {
  var ADMINISTRADOR = _ADMINISTRADOR(sequelize, DataTypes);
  var CUSTOMER = _CUSTOMER(sequelize, DataTypes);
  var DISCOUNT_CODE = _DISCOUNT_CODE(sequelize, DataTypes);
  var MANUFACTURER = _MANUFACTURER(sequelize, DataTypes);
  var MICRO_MARKET = _MICRO_MARKET(sequelize, DataTypes);
  var PRODUCT = _PRODUCT(sequelize, DataTypes);
  var PRODUCT_CODE = _PRODUCT_CODE(sequelize, DataTypes);
  var PURCHASE_ORDER = _PURCHASE_ORDER(sequelize, DataTypes);


  return {
    ADMINISTRADOR,
    CUSTOMER,
    DISCOUNT_CODE,
    MANUFACTURER,
    MICRO_MARKET,
    PRODUCT,
    PRODUCT_CODE,
    PURCHASE_ORDER,
  };
}
module.exports = initModels;
module.exports.initModels = initModels;
module.exports.default = initModels;
