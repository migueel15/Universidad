var DataTypes = require("sequelize").DataTypes;
var _marca = require("./marca");
var _propietario = require("./propietario");
var _servicio = require("./servicio");
var _vehiculo = require("./vehiculo");
var _vehiculo_propietario = require("./vehiculo_propietario");

function initModels(sequelize) {
  var marca = _marca(sequelize, DataTypes);
  var propietario = _propietario(sequelize, DataTypes);
  var servicio = _servicio(sequelize, DataTypes);
  var vehiculo = _vehiculo(sequelize, DataTypes);
  var vehiculo_propietario = _vehiculo_propietario(sequelize, DataTypes);

  vehiculo.belongsTo(marca, { as: "id_marca_marca", foreignKey: "id_marca"});
  marca.hasMany(vehiculo, { as: "vehiculos", foreignKey: "id_marca"});
  servicio.belongsTo(propietario, { as: "id_propietario_propietario", foreignKey: "id_propietario"});
  propietario.hasMany(servicio, { as: "servicios", foreignKey: "id_propietario"});
  vehiculo_propietario.belongsTo(propietario, { as: "id_propietario_propietario", foreignKey: "id_propietario"});
  propietario.hasMany(vehiculo_propietario, { as: "vehiculo_propietarios", foreignKey: "id_propietario"});
  servicio.belongsTo(vehiculo, { as: "id_vehiculo_vehiculo", foreignKey: "id_vehiculo"});
  vehiculo.hasMany(servicio, { as: "servicios", foreignKey: "id_vehiculo"});
  vehiculo_propietario.belongsTo(vehiculo, { as: "id_vehiculo_vehiculo", foreignKey: "id_vehiculo"});
  vehiculo.hasMany(vehiculo_propietario, { as: "vehiculo_propietarios", foreignKey: "id_vehiculo"});

  return {
    marca,
    propietario,
    servicio,
    vehiculo,
    vehiculo_propietario,
  };
}
module.exports = initModels;
module.exports.initModels = initModels;
module.exports.default = initModels;
