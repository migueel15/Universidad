const Sequelize = require('sequelize');
const db = require("../config/database");
module.exports = function(sequelize, DataTypes) {
  return db.define('marca', {
    id_marca: {
      autoIncrement: true,
      type: DataTypes.INTEGER,
      allowNull: true,
      primaryKey: true
    },
    nombre: {
      type: DataTypes.TEXT,
      allowNull: false,
      unique: true
    }
  }, {
    sequelize,
    tableName: 'marca',
    timestamps: false,
    indexes: [
      {
        name: "sqlite_autoindex_marca_1",
        unique: true,
        fields: [
          { name: "nombre" },
        ]
      },
    ]
  });
};
