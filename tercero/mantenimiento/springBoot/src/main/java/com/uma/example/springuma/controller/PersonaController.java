package com.uma.example.springuma.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.uma.example.springuma.model.Persona;
import com.uma.example.springuma.model.PersonaService;

@RestController
public class PersonaController {
    
    @Autowired
    private PersonaService personaService;

    @GetMapping("/personas")
    public List<Persona> getPersonas(){
        return personaService.getAllPersonas();
    }

    @GetMapping("/persona/{id}")
    public Persona getPersona(@PathVariable("id") Long id) {
        return personaService.getPersona(id);
    }

    @PostMapping(value = "/persona",     consumes = {MediaType.APPLICATION_JSON_VALUE} )
	public ResponseEntity<?> savePersona(@RequestBody Persona persona) {
        try{
            personaService.addPersona(persona);
            return ResponseEntity.ok().body("Una nueva Persona se ha anyadido");
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body("La Persona ya existe");
        }
	}

    @PutMapping(value = "/persona",     consumes = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<?> updatePersona (Persona Persona) {
        try{
            personaService.updatePersona(Persona);
            return ResponseEntity.ok().body("La Persona se ha actualizado");
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body("Error al actualizar la Persona");
        }
    }
    
    @DeleteMapping(value = "/persona",     consumes = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<?> deletePersona(@RequestBody Persona Persona){
        try{
            personaService.removePersona(Persona);
            return ResponseEntity.ok().body("La Persona se ha eliminado");
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body("Error al eliminar la Persona");
        }
    }
}
