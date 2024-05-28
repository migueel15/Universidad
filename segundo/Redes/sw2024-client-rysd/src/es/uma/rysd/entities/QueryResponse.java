package es.uma.rysd.entities;

// Class obtained when searching by the name of a person that potentially can return many (or none)

public class QueryResponse {
	public Integer count;
	public Person[] results;
}
