const initModels = require("../models/init-models");
const sequelize = require("sequelize");
const models = initModels(sequelize);

const peliculas = {};

peliculas.listarPeliculas = async (req, res, next) => {
  const peliculas = await models.Pelicula.findAll();
  res.send("holaa");
};

module.exports = peliculas;
