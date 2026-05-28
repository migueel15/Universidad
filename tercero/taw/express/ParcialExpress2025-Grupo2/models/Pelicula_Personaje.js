const Sequelize = require("sequelize");
const db = require("../config/database");
module.exports = function (sequelize, DataTypes) {
  return db.define(
    "Pelicula_Personaje",
    {
      pelicula: {
        type: DataTypes.INTEGER,
        allowNull: true,
        references: {
          model: "Pelicula",
          key: "pelicula_id",
        },
        unique: true,
      },
      personaje: {
        type: DataTypes.INTEGER,
        allowNull: true,
        references: {
          model: "Personaje",
          key: "personaje_id",
        },
        unique: true,
      },
    },
    {
      sequelize,
      tableName: "Pelicula_Personaje",
      timestamps: false,
      indexes: [
        {
          name: "sqlite_autoindex_Pelicula_Personaje_1",
          unique: true,
          fields: [{ name: "pelicula" }, { name: "personaje" }],
        },
      ],
    },
  );
};
