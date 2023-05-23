package inicioSesion;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
public class InicioSesionTest {
	//PRUEBAS OBTENIENDO ID DE USUARIO
	@Test
	public void testIdUserForCorreo() {
		Inicio_Sesion inicioSesion = new Inicio_Sesion();
		int idEsperado = 1;
		int idObtenido = inicioSesion.id_user_For_Correo("leal.adrian.vacas@gmail.com");
		assertEquals(idEsperado, idObtenido);
	}
	//PRUEBAS DE LOS ADMINISTRADORES SI COINCIDEN EN LA BASE DE DATOS
	@Test
	public void testCountAdmin() {
		Inicio_Sesion inicioSesion = new Inicio_Sesion();
		int countEsperado = 1;
		int countObtenido = inicioSesion.countAdmin(1);
		assertEquals(countEsperado, countObtenido);
	}
	//PRUEBAS DE LA CONTRASEÑA SI COINCIDEN EN LA BASE DE DATOS
	@Test
	public void testCountPasswd() {
		Inicio_Sesion inicioSesion = new Inicio_Sesion();
		int countEsperado = 1;
		int countObtenido = inicioSesion.countPasswd("Solerocioadrian3",1);
		assertEquals(countEsperado, countObtenido);
	}
	//PRUEBAS DE LOS USUARIOS SI COINCIDEN EN LA BASE DE DATOS
	@Test
	public void testCountID_USER() {
		Inicio_Sesion inicioSesion = new Inicio_Sesion();
		int countEsperado = 1;
		int countObtenido = inicioSesion.countID_USER(1);
		assertEquals(countEsperado, countObtenido);
	}
	//PRUEBAS DE LAS MEMBRESÍAS SI COINCIDEN EN LA BASE DE DATOS
	@Test
	public void testObtenerID_MEMBRESIA() {
		Inicio_Sesion inicioSesion = new Inicio_Sesion();
		int countEsperado = 4;
		int countObtenido = inicioSesion.obtenerID_MEMBRESIA(1);
		assertEquals(countEsperado, countObtenido);
	}
}