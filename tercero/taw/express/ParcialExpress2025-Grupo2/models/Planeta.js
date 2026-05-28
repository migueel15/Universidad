const Sequelize = require('sequelize');
const db = require("../config/database");
module.exports = function(sequelize, DataTypes) {
  return db.define('Planeta', {
    planeta_id: {
      autoIncrement: true,
      type: DataTypes.INTEGER,
      allowNull: true,
      primaryKey: true
    },
    nombre: {
      type: DataTypes.STRING(25),
      allowNull: true
    },
    diametro: {
      type: DataTypes.INTEGER,
      allowNull: true
    },
    clima: {
      type: DataTypes.STRING(25),
      allowNull: true
    },
    gravedad: {
      type: DataTypes.FLOAT,
      allowNull: true
    },
    terreno: {
      type: DataTypes.STRING(150),
      allowNull: true
    },
    porcentaje_agua: {
      type: DataTypes.INTEGER,
      allowNull: true
    }
  }, {
    sequelize,
    tableName: 'Planeta',
    timestamps: false
  });
};
