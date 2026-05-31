const Sequelize = require('sequelize');
const db = require("../config/database");
module.exports = function(sequelize, DataTypes) {
  return db.define('vehiculo_propietario', {
    id_vehiculo: {
      autoIncrement: false,
      type: DataTypes.INTEGER,
      allowNull: false,
      references: {
        model: 'vehiculo',
        key: 'id_vehiculo'
      },
      unique: true,
      primaryKey: true
    },
    id_propietario: {
      autoIncrement: false,
      type: DataTypes.INTEGER,
      allowNull: false,
      references: {
        model: 'propietario',
        key: 'id_propietario'
      },
      unique: true,
      primaryKey: true
    }
  }, {
    sequelize,
    tableName: 'vehiculo_propietario',
    timestamps: false,
    indexes: [
      {
        name: "sqlite_autoindex_vehiculo_propietario_1",
        unique: true,
        fields: [
          { name: "id_vehiculo" },
          { name: "id_propietario" },
        ]
      },
    ]
  });
};
