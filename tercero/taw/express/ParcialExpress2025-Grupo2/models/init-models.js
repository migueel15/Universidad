var DataTypes = require("sequelize").DataTypes;
var _Frase_Celebre = require("./Frase_Celebre");
var _Genero = require("./Genero");
var _Pelicula = require("./Pelicula");
var _Pelicula_Personaje = require("./Pelicula_Personaje");
var _Personaje = require("./Personaje");
var _Planeta = require("./Planeta");

function initModels(sequelize) {
  var Frase_Celebre = _Frase_Celebre(sequelize, DataTypes);
  var Genero = _Genero(sequelize, DataTypes);
  var Pelicula = _Pelicula(sequelize, DataTypes);
  var Pelicula_Personaje = _Pelicula_Personaje(sequelize, DataTypes);
  var Personaje = _Personaje(sequelize, DataTypes);
  var Planeta = _Planeta(sequelize, DataTypes);

  Personaje.belongsTo(Genero, { as: "genero_Genero", foreignKey: "genero"});
  Genero.hasMany(Personaje, { as: "Personajes", foreignKey: "genero"});
  Frase_Celebre.belongsTo(Pelicula, { as: "pelicula_Pelicula", foreignKey: "pelicula"});
  Pelicula.hasMany(Frase_Celebre, { as: "Frase_Celebres", foreignKey: "pelicula"});
  Pelicula_Personaje.belongsTo(Pelicula, { as: "pelicula_Pelicula", foreignKey: "pelicula"});
  Pelicula.hasMany(Pelicula_Personaje, { as: "Pelicula_Personajes", foreignKey: "pelicula"});
  Frase_Celebre.belongsTo(Personaje, { as: "personaje_Personaje", foreignKey: "personaje"});
  Personaje.hasMany(Frase_Celebre, { as: "Frase_Celebres", foreignKey: "personaje"});
  Pelicula_Personaje.belongsTo(Personaje, { as: "personaje_Personaje", foreignKey: "personaje"});
  Personaje.hasMany(Pelicula_Personaje, { as: "Pelicula_Personajes", foreignKey: "personaje"});
  Personaje.belongsTo(Planeta, { as: "planeta_Planetum", foreignKey: "planeta"});
  Planeta.hasMany(Personaje, { as: "Personajes", foreignKey: "planeta"});

  return {
    Frase_Celebre,
    Genero,
    Pelicula,
    Pelicula_Personaje,
    Personaje,
    Planeta,
  };
}
module.exports = initModels;
module.exports.initModels = initModels;
module.exports.default = initModels;
