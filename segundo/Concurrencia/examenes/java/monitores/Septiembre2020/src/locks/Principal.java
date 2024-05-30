package locks;


public class Principal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final int NUMALUMNOS= 15; //numero de alumnos
		
		Curso curso = new Curso();
		
		Alumno alumnos [] = new Alumno[NUMALUMNOS];
		for (int i = 0; i< NUMALUMNOS; i++){
			alumnos[i] = new Alumno(i,curso); 
		}

		for (int i = 0; i< NUMALUMNOS; i++){
			alumnos[i].start();
		}
		
		try {
			for (int i = 0; i< NUMALUMNOS; i++)
				alumnos[i].join();
			
		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
}
