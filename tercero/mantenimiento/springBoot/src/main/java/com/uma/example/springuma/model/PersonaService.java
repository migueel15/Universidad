package com.uma.example.springuma.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonaService {

    @Autowired
    RepositoryPersona repositoryPersona;

    public List<Persona> getAllPersonas(){
        return repositoryPersona.findAll();
    }

    public Persona getPersona(Long id){
        return repositoryPersona.getReferenceById(id);
    }

    public Persona addPersona(Persona c){
        return repositoryPersona.saveAndFlush(c);
    }

    public void updatePersona(Persona p){
        Persona persona = repositoryPersona.getReferenceById(p.getId());
		persona.setDni(p.getDni());
		persona.setEdad(p.getEdad());
        persona.setNombre(p.getNombre());
        repositoryPersona.save(persona);
    }

    public void removePersona(Persona c){
        repositoryPersona.delete(c);
    }

    public void removePersonaID(Long id){
        repositoryPersona.deleteById(id);
    }
}
