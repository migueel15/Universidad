const Sequelize = require('sequelize');
const db = require("../config/database");
module.exports = function(sequelize, DataTypes) {
  return db.define('servicio', {
    id_servicio: {
      autoIncrement: true,
      type: DataTypes.INTEGER,
      allowNull: true,
      primaryKey: true
    },
    id_vehiculo: {
      type: DataTypes.INTEGER,
      allowNull: false,
      references: {
        model: 'vehiculo',
        key: 'id_vehiculo'
      }
    },
    id_propietario: {
      type: DataTypes.INTEGER,
      allowNull: false,
      references: {
        model: 'propietario',
        key: 'id_propietario'
      }
    },
    fecha: {
      type: DataTypes.TEXT,
      allowNull: false
    },
    descripcion: {
      type: DataTypes.TEXT,
      allowNull: true
    },
    costo: {
      type: DataTypes.REAL,
      allowNull: true
    }
  }, {
    sequelize,
    tableName: 'servicio',
    timestamps: false
  });
};
