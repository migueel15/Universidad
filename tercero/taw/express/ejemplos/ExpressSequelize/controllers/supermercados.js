const initModels = require("../models/init-models");
const sequelize = require("sequelize");
const models = initModels(sequelize);

const controller = {};


// Listas todos ////////////////////////////////////////////////////////////////////////////////////////////////////////
controller.listarTodos = async function (req, res, next) {
    try {
        await models.MICRO_MARKET
            .findAll()
            .then(async (supermercados) => {
            //res.json(supermercados);
            res.render("index", {supermercados: supermercados});
        });
    } catch (error) {
        res.send("Se ha producido un error " + error);
    }
};
// Listas todos ////////////////////////////////////////////////////////////////////////////////////////////////////////
controller.buscarUno = async function (req, res, next) {
    try {
        await models.MICRO_MARKET
            .findOne({
                where: {
                    ZIP_CODE: req.params.id
                }
            })
            .then((data) => {
                if (data != null) {
                    //res.json(data);
                    res.render("cliente", {supermercado: data});

                } else {
                    res.send("Value not found");
                }
            });
    } catch (error) {
        res.send("Se ha producido un error " + error);
    }
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

module.exports = controller;