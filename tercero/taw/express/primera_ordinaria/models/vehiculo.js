const Sequelize = require('sequelize');
const db = require("../config/database");
module.exports = function(sequelize, DataTypes) {
  return db.define('vehiculo', {
    id_vehiculo: {
      autoIncrement: true,
      type: DataTypes.INTEGER,
      allowNull: true,
      primaryKey: true
    },
    modelo: {
      type: DataTypes.TEXT,
      allowNull: false
    },
    anio: {
      type: DataTypes.INTEGER,
      allowNull: true
    },
    id_marca: {
      type: DataTypes.INTEGER,
      allowNull: false,
      references: {
        model: 'marca',
        key: 'id_marca'
      }
    }
  }, {
    sequelize,
    tableName: 'vehiculo',
    timestamps: false
  });
};
