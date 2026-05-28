const Sequelize = require("sequelize");
const db = require("../config/database");
module.exports = function (sequelize, DataTypes) {
  return db.define(
    "Pelicula",
    {
      pelicula_id: {
        autoIncrement: true,
        type: DataTypes.INTEGER,
        allowNull: true,
        primaryKey: true,
      },
      titulo: {
        type: DataTypes.STRING(50),
        allowNull: true,
      },
      anyo: {
        type: DataTypes.INTEGER,
        allowNull: true,
      },
      texto_apertura: {
        type: DataTypes.STRING(5000),
        allowNull: true,
      },
    },
    {
      sequelize,
      tableName: "Pelicula",
      timestamps: false,
    },
  );
};
