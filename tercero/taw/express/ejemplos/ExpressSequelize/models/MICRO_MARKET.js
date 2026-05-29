const Sequelize = require('sequelize');
const db = require("../config/database");
module.exports = function(sequelize, DataTypes) {
  return db.define('MICRO_MARKET', {
    ZIP_CODE: {
      type: DataTypes.STRING(10),
      allowNull: false,
      primaryKey: true
    },
    RADIUS: {
      type: DataTypes.DOUBLE,
      allowNull: true
    },
    AREA_LENGTH: {
      type: DataTypes.DOUBLE,
      allowNull: true
    },
    AREA_WIDTH: {
      type: DataTypes.DOUBLE,
      allowNull: true
    },
    ADMIN_ID: {
      type: DataTypes.INTEGER,
      allowNull: true
    }
  }, {
    sequelize,
    tableName: 'MICRO_MARKET',
    timestamps: false,
    indexes: [
      {
        name: "PRIMARY",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "ZIP_CODE" },
        ]
      },
    ]
  });
};
