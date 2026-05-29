const sequalize = require("sequelize")
const modelInit = require("../models/init-models")
const db = modelInit(sequalize)
const {Op} = require("sequelize")

const planetas = {}

planetas.listarPlanetas = async(req,res,next) => {
  const filter = {}

  if(req.query.filter === "nombre"){
    filter.order= [
      ["nombre","DESC"]
    ]
  }

  const data = await db.Planeta.findAll(filter)
  res.render("planetas", {planetas:data})
}

module.exports = planetas