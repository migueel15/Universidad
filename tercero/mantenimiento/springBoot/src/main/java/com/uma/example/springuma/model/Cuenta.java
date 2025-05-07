package com.uma.example.springuma.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;



@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // para ignorar el serializador al devolver un objeto cuenta
public class Cuenta
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    

    @Column(name = "balance")
    //@Column(nullable=true)
    private double balance; 
    
    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Column(unique=true) 
    private int ccc; 
    
    public int getCcc() {
        return ccc;
    }

    public void setCcc(int ccc) {
        this.ccc = ccc;
    }

    public Cuenta(){    
    }

    public Cuenta(int ccc){    
        this.ccc=ccc;
    }

    // Devuelve el balance
    public double getBalance(){
        return this.balance;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    private Persona titular;
    
    public Persona getTitular() {
        return titular;
    }

    public void setTitular(Persona titular) {
        this.titular = titular;
    }

    public boolean equals(Object obj)
    {
	    return (obj instanceof Cuenta) && ((Cuenta) obj).getCcc()==this.ccc;
    }

    public int hashCode()
    {
	    return this.ccc;
    }
    
    public String toString()
    {
	    return "CCC " + this.ccc + ": " + "balance = " + this.balance;    
    }
}
