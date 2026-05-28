const Sequelize = require("sequelize");
const db = require("../config/database");
module.exports = function (sequelize, DataTypes) {
  return db.define(
    "Frase_Celebre",
    {
      frase_id: {
        autoIncrement: true,
        type: DataTypes.INTEGER,
        allowNull: true,
        primaryKey: true,
      },
      frase: {
        type: DataTypes.STRING(200),
        allowNull: true,
      },
      personaje: {
        type: DataTypes.INTEGER,
        allowNull: true,
        references: {
          model: "Personaje",
          key: "personaje_id",
        },
      },
      pelicula: {
        type: DataTypes.INTEGER,
        allowNull: true,
        references: {
          model: "Pelicula",
          key: "pelicula_id",
        },
      },
    },
    {
      sequelize,
      tableName: "Frase_Celebre",
      timestamps: false,
    },
  );
};
