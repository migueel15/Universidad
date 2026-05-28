const Sequelize = require("sequelize");
const db = require("../config/database");
module.exports = function (sequelize, DataTypes) {
  return db.define(
    "Personaje",
    {
      personaje_id: {
        autoIncrement: true,
        type: DataTypes.INTEGER,
        allowNull: true,
        primaryKey: true,
      },
      nombre: {
        type: DataTypes.STRING(30),
        allowNull: true,
      },
      color_pelo: {
        type: DataTypes.STRING(20),
        allowNull: true,
      },
      color_piel: {
        type: DataTypes.STRING(20),
        allowNull: true,
      },
      color_ojos: {
        type: DataTypes.STRING(20),
        allowNull: true,
      },
      anyo_nacimiento: {
        type: DataTypes.STRING(10),
        allowNull: true,
      },
      genero: {
        type: DataTypes.INTEGER,
        allowNull: true,
        references: {
          model: "Genero",
          key: "genero_id",
        },
      },
      planeta: {
        type: DataTypes.INTEGER,
        allowNull: true,
        references: {
          model: "Planeta",
          key: "planeta_id",
        },
      },
    },
    {
      sequelize,
      tableName: "Personaje",
      timestamps: false,
    },
  );
};
