var DataTypes = require("sequelize").DataTypes;
var _Especie = require("./Especie");
var _Familia_Especie = require("./Familia_Especie");
var _Genero = require("./Genero");
var _Personaje = require("./Personaje");
var _Planeta = require("./Planeta");

function initModels(sequelize) {
  var Especie = _Especie(sequelize, DataTypes);
  var Familia_Especie = _Familia_Especie(sequelize, DataTypes);
  var Genero = _Genero(sequelize, DataTypes);
  var Personaje = _Personaje(sequelize, DataTypes);
  var Planeta = _Planeta(sequelize, DataTypes);

  Personaje.belongsTo(Especie, { as: "especie_Especie", foreignKey: "especie"});
  Especie.hasMany(Personaje, { as: "Personajes", foreignKey: "especie"});
  Especie.belongsTo(Familia_Especie, { as: "clasificacion_Familia_Especie", foreignKey: "clasificacion"});
  Familia_Especie.hasMany(Especie, { as: "Especies", foreignKey: "clasificacion"});
  Personaje.belongsTo(Genero, { as: "genero_Genero", foreignKey: "genero"});
  Genero.hasMany(Personaje, { as: "Personajes", foreignKey: "genero"});
  Especie.belongsTo(Planeta, { as: "planeta_Planetum", foreignKey: "planeta"});
  Planeta.hasMany(Especie, { as: "Especies", foreignKey: "planeta"});
  Personaje.belongsTo(Planeta, { as: "planeta_Planetum", foreignKey: "planeta"});
  Planeta.hasMany(Personaje, { as: "Personajes", foreignKey: "planeta"});

  return {
    Especie,
    Familia_Especie,
    Genero,
    Personaje,
    Planeta,
  };
}
module.exports = initModels;
module.exports.initModels = initModels;
module.exports.default = initModels;
