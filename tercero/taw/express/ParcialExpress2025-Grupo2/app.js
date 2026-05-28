const express = require("express");
const morgan = require("morgan");
const sequelize = require("sequelize");
const bodyParser = require("body-parser");
const path = require("path");

const peliculas = require("./routes/index.js");

const app = express();
app.set("view engine", "ejs");
app.set("views", path.join(__dirname, "views"));

app.use(morgan("dev"));
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.get("/", function (req, res) {
  res.send("Prueba");
});

app.use("/peliculas", peliculas);

app.use((req, res, next) => {
  const err = new Error(`${req.url} not found in this server`);
  err.status = 404;
  next(err);
});
// setting another error program
app.use((err, req, res, next) => {
  res.status(err.status || 500).json({ error: err.message });
});

module.exports = app;

