const Sequelize = require('sequelize');
const db = require("../config/database");
module.exports = function(sequelize, DataTypes) {
  return db.define('Especie', {
    especie_id: {
      autoIncrement: true,
      type: DataTypes.INTEGER,
      allowNull: true,
      primaryKey: true
    },
    especie: {
      type: DataTypes.STRING(20),
      allowNull: false
    },
    clasificacion: {
      type: DataTypes.INTEGER,
      allowNull: false,
      references: {
        model: 'Familia_Especie',
        key: 'familia_id'
      }
    },
    peso_medio: {
      type: DataTypes.INTEGER,
      allowNull: true
    },
    esperanza_vida: {
      type: DataTypes.INTEGER,
      allowNull: true
    },
    idioma: {
      type: DataTypes.STRING(25),
      allowNull: true
    },
    planeta: {
      type: DataTypes.INTEGER,
      allowNull: true,
      references: {
        model: 'Planeta',
        key: 'planeta_id'
      }
    }
  }, {
    sequelize,
    tableName: 'Especie',
    timestamps: false
  });
};
