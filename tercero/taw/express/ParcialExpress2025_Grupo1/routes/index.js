const express = require("express")
const router = express.Router()
const peliculasController = require("../controllers/planetas")

router.get("/peliculas", peliculasController.listarPlanetas)

module.exports = router