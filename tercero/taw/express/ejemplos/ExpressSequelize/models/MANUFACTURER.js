const Sequelize = require('sequelize');
const db = require("../config/database");
module.exports = function(sequelize, DataTypes) {
  return db.define('MANUFACTURER', {
    MANUFACTURER_ID: {
      autoIncrement: true,
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true
    },
    NAME: {
      type: DataTypes.STRING(30),
      allowNull: true
    },
    ADDRESSLINE1: {
      type: DataTypes.STRING(30),
      allowNull: true
    },
    ADDRESSLINE2: {
      type: DataTypes.STRING(30),
      allowNull: true
    },
    CITY: {
      type: DataTypes.STRING(25),
      allowNull: true
    },
    STATE: {
      type: DataTypes.CHAR(2),
      allowNull: true
    },
    ZIP: {
      type: DataTypes.CHAR(10),
      allowNull: true
    },
    PHONE: {
      type: DataTypes.STRING(12),
      allowNull: true
    },
    FAX: {
      type: DataTypes.STRING(12),
      allowNull: true
    },
    EMAIL: {
      type: DataTypes.STRING(40),
      allowNull: true
    },
    REP: {
      type: DataTypes.STRING(30),
      allowNull: true
    }
  }, {
    sequelize,
    tableName: 'MANUFACTURER',
    timestamps: false,
    indexes: [
      {
        name: "PRIMARY",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "MANUFACTURER_ID" },
        ]
      },
    ]
  });
};
