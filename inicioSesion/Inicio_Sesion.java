package inicioSesion;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import panel_IZ.Panel_iz_Principal;
import java.awt.Color;
import javax.swing.border.LineBorder;
import admin.Pagina_administrador;
import registro.Formulario;
import java.awt.Font;
import java.awt.Image;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Cursor;
/**
 * En la página de Iniciar Sesión puedes iniciar sesión y te redirige a la pantalla del contenido audiovisual o la opción del botón de registrarse para comprar una membresía.
 * @param getContentPane CONTIENE TODOS LOS ATRIBUTOS DE LA PÁGINA.
 * @param Panel_InicioSesion Es el panel principal donde contiene todos los atributos.
 * @param btnInicioSesion EL BOTÓN INICIO DE SESIÓN.
 * @param btnRegistrarse ES EL BOTÓN DE REGISTRO.
 * @param btnEngranaje_Admin ES EL BOTÓN PARA IR A CONFIGURAR TU CUENTA DE USUARIO
 * @param lbl_Logo ES NUESTRO LOGO TRILOGY(ESTÉTICO).	
 * @param lbl_PuertaSalida ES BOTÓN DE SALIDA.
 * @param lblusuariotitulo EL TÍTULO QUE INDICA DONDE INTRODUCIR EL USUARIO(ESTÉTICO).
 * @param text_introducirUsuario ESCRIBIR EL USUARIO.
 * @param lblcontraseñatitulo EL TÍTULO QUE INDICA DONDE INTRODUCIR LA CONTRASEÑA(ESTÉTICO).
 * @param passwordField ESCRIBIR LA CONTRASEÑA.
 * @param lblBordeSuperior BARRA VERDE SUPERIOR(ESTÉTICO).  
 * @param lblFondo_Verde FONDO DE COLOR VERDE(ESTÉTICO).
 * @author TRILOGY. Miembros: Adrian Leal Vacas, Gonzalo Amo Cano y Raul Senso Montojo.
 */
public class Inicio_Sesion extends JFrame {
	/**
	 * Es la versión número 1 de la página del contenido audiovisual del Inicio de Sesión.
	 */
	private static final long serialVersionUID = 1L;
	// ----------------------------------------------------------------------------------------
	// ATRIBUTOS
	// ----------------------------------------------------------------------------------------
	public static int id_de_membresia=-1;
	private boolean comprobador_pag_admin=false;
	private JPasswordField passwordField;
	private JTextField text_introducirUsuario;
	private JLabel lbl_PuertaSalida;
	private JButton btnRegistrarse;
	private JButton btnInicioSesion;
	private JButton btnEngranaje_Admin;
	public static String correo_usuario="";
	// ----------------------------------------------------------------------------------------
	// ATRIBUTOS U OBJETOS NECESARIOS PARA LA CONEXIÓN A LA BASE DE DATOS Y LA REALIZACIÓN DE UN CONSULTA BÁSICA
	// ----------------------------------------------------------------------------------------
	private static String bd="XE"; // NOMBRE DE LA BASE DE DATOS POR DEFECT SIEMPRE DEJAR EL "XE"
	private static String login="TRILOGY"; // USUARIO DE LA BBDD
	private static String password="TRILOGY"; // CONTRASEÑA DE LA BBDD
	// ----------------------------------------------------------------------------------------
	// RUTA DE SERVICIO
	// ----------------------------------------------------------------------------------------
	private static String url="jdbc:oracle:thin:@localhost:1521:"+bd;
	// ----------------------------------------------------------------------------------------
	// PONEMOS LOS OBJETOS A NULL Y SIN INSTANCIAR
	// ----------------------------------------------------------------------------------------
	static Connection connection=null; // CONEXION A LA BASE DE DATOS
	static Statement st; // PARA REALIZAR SQL ESTATICAS (HAY QUE ENLAZARLA SIEMPRE CON EL "Connection" SINO NO FUNCIONA)
	static ResultSet rs; // PARA REALIZAR LA CONSULTA IGUAL QUE EN SQL DEVELOPER
	// ----------------------------------------------------------------------------------------
	// MÉTODO MAIN
	// ----------------------------------------------------------------------------------------
	/**
	 * En el método main ejecutamos la ventana principal Inicio_Sesion.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Inicio_Sesion frame = new Inicio_Sesion();
					frame.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "ERROR. No se ha podido haceder a la pagina de inicio de sesión.");
				}
			}
		});
	}
	// ------------------------------------------------------------------------------------------------------
	// MÉTODOS
	// ------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------
	// MÉTODO PARA CONECTAR A LA BASE DE DATOS
	// ------------------------------------------------------------------------------------------------------
	/**
	 * El MÉTODO conectar() realiza una conexión a la base de datos.
	 */
	public static void conectar() throws Exception{
		// DRIVER PARA ORACLE
		Class.forName("oracle.jdbc.driver.OracleDriver"); // EL DRIVER DEL JDBC SIEMPRE ES EL MISMO QUE ESTA PUESTO
		connection=DriverManager.getConnection(url,login,password); // NOS CONECTAMOS A LA BASE DE DATOS CON LA URL Y LOGIN Y EL PASSWORD QUE PREVIAMENTE PUSIMOS EN LOS ATRIBUTOS
	}
	// ------------------------------------------------------------------------------------------------------
	// MÉTODO PARA CERRAR LA BASE DE DATOS
	// ------------------------------------------------------------------------------------------------------
	/**
	 * El método cerrar() cierra la base de datos.
	 */
	public static void cerrar() throws SQLException{
		// SIEMPRE EN EL MISMO ORDEN SINO DA FALLO
		if (rs!=null) rs.close(); // CERRAMOS EL RS SI ES DIFERENTE AL NULL (FUNCIONANDO)
		if (st!=null) st.close(); // CERRAMOS EL ST SI ES DIFERENTE AL NULL (FUNCIONANDO)
		if (connection!=null) connection.close(); // CERRAMOS EL connection SI ES DIFERENTE AL NULL (FUNCIONANDO)
	}
	// ------------------------------------------------------------------------------------------------------
	// MÉTODO PARA SACAR ID USER DEL CORREO
	// ------------------------------------------------------------------------------------------------------
	/**
	 * El método id_user_For_Correo() adquiere el id del usuario por su correo.
	 */
	public int id_user_For_Correo (String correo) {
		int id_user=0;
		try {
			// LLAMO AL MÉTODO CONECTAR.
			conectar();
			// NOS CONECTAMOS A LA BASE DE DATOS.
			st=connection.createStatement();
			// REALIZAMOS LA SELECT CON LOS DATOS QUE QUEREMOS ALMACENAR.
			PreparedStatement statement = connection.prepareStatement("select ID_USUARIO from USUARIOS WHERE CORREO = ?");
			// LE DECIMOS QUE LA ? ANTERIOR COJA EL VALOR DE LA VARIABLE.
			statement.setString(1, correo.toUpperCase());
			// EJECUTAMOS LA QUERY ANTERIOR.
			rs = statement.executeQuery();
			// PARA RECORER LAS LINEAS QUE NOS SALGA EN LA SENTENCIA QUERY ANTERIOR.
			// MIENTRAS QUE HHAYA SIGUIENTE
			while (rs.next()) {
				// ALMACENA EL DATO OBTENIDO.
				id_user = rs.getInt(1);
			}
			// CUANDO TERMINO LLAMO AL MÉTODO DE CERRAR LA CONEXIÓN.
			cerrar();
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERROR. No se ha podido obtener el id del usuario.");
		}
		return id_user;
	}
	// ------------------------------------------------------------------------------------------------------
	// MÉTODO CUENTA LOS USUARIOS SI EL ID_USER ES ADMINISTRADOR.
	// ------------------------------------------------------------------------------------------------------
	/**
	 * El método countAdmin() adquiere si hay un usuario con el ID indicado (id_user_For_Correo()).
	 */
	public int countAdmin (int id_user) {
		int contador_id_user=0;
		try {
			// LLAMO AL MÉTODO CONECTAR
			conectar();
			// NOS CONECTAMOS A LA BASE DE DATOS.
			st=connection.createStatement();
			// REALIZAMOS LA SELECT CON LOS DATOS QUE QUEREMOS ALMACENAR (? --> ES UN CAMPO QUE LO VA A ADQUIRIR DE LA VARIABLE correos) (SIRVE PARA CREAR LA SENTENCIA SQL).
			PreparedStatement statement = connection.prepareStatement("select count(ID_USUARIO) from ADMINISTRADORES WHERE ID_USUARIO = ?");
			// LE DECIMOS QUE LA ? ANTERIOR COJA EL VALOR DE LA VARIABLE correos (1 ES EL NÚMERO DE ? Y correos EL VALOR QUE LE QUEREMOS METER EN ESTE CASO EL VALOR ALMACENADO EN LA VARIABLE).
			statement.setInt(1, id_user);
			// EJECUTAMOS LA QUERY ANTERIOR.
			rs = statement.executeQuery();
			// PARA RECORER LAS LINEAS QUE NOS SALGA EN LA SENTENCIA QUERY ANTERIOR.
			// MIENTRAS QUE HHAYA SIGUIENTE.
			while (rs.next()) {
				// ALMACENA EL DATO DE LA COLUMNA NOMBRE EN LA VARIABLE nombre.
				contador_id_user = rs.getInt(1);
			}
			cerrar();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERROR. No se ha podido obtener si eres administrador.");
		}
		return contador_id_user;
	}
	// ------------------------------------------------------------------------------------------------------
	// MÉTODO OBTENER PASSWD DEL USER.
	// ------------------------------------------------------------------------------------------------------
	/**
	 * El método countPasswd() adquiere si la contraseña puesta pertenece al ID del usuario indicado.
	 */
	public int countPasswd (String passwd, int id_user) {
		int contador_passwd=0;
		try {
			// LLAMO AL MÉTODO CONECTAR.
			conectar();
			// NOS CONECTAMOS A LA BASE DE DATOS.
			String sql = "select count(ID_USUARIO) from USUARIOS WHERE CONTRASENA = ? and ID_USUARIO = ?";
			// REALIZAMOS EL SELECT CON LOS DATOS QUE QUEREMOS ALMACENAR (? --> ES UN CAMPO QUE LO VA A ADQUIRIR DE LA VARIABLE correos) (SIRVE PARA CREAR LA SENTENCIA SQL).
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, passwd);
			// LE DECIMOS QUE LA ? ANTERIOR COJA EL VALOR DE LA VARIABLE correos (1 ES EL NÚMERO DE ? Y correos EL VALOR QUE LE QUEREMOS METER EN ESTE CASO EL VALOR ALMACENADO EN LA VARIABLE).
			statement.setInt(2, id_user);
			// EJECUTAMOS LA QUERY ANTERIOR.
			rs = statement.executeQuery();
			// PARA RECORER LAS LÍNEAS QUE NOS SALGA EN LA SENTENCIA QUERY ANTERIOR.
			// MIENTRAS QUE HHAYA SIGUIENTE.
			while (rs.next()) {
				// ALMACENA EL DATO DE LA COLUMNA NOMBRE EN LA VARIABLE nombre.
				contador_passwd = rs.getInt(1);
			}
			cerrar();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERROR. No se ha podido obtener si la password es correcta.");
		}
		return contador_passwd;
	}
	// ------------------------------------------------------------------------------------------------------
	// MÉTODO CONTADOR ID_USER
	// ------------------------------------------------------------------------------------------------------
	/**
	 * El método countID_USER() adquiere si el ID obtenido existe en la base de datos.
	 */
	public int countID_USER (int id_user) {
		int contador_id_user=0;
		try {
			// LLAMO AL MÉTODO CONECTAR
			conectar();
			// NOS CONECTAMOS A LA BASE DE DATOS
			st=connection.createStatement();
			// REALIZAMOS EL SELECT CON LOS DATOS QUE QUEREMOS ALMACENAR (? --> ES UN CAMPO QUE LO VA A ADQUIRIR DE LA VARIABLE correos) (SIRVE PARA CREAR LA SENTENCIA SQL)
			PreparedStatement statement2 = connection.prepareStatement("select count(ID_USUARIO) from USUARIOS WHERE ID_USUARIO = ?");
			// LE DECIMOS QUE LA ? ANTERIOR COJA EL VALOR DE LA VARIABLE correos (1 ES EL NÚMERO DE ? Y correos EL VALOR QUE LE QUEREMOS METER EN ESTE CASO EL VALOR ALMACENADO EN LA VARIABLE)
			statement2.setInt(1, id_user);
			// EJECUTAMOS LA QUERY ANTERIOR
			rs = statement2.executeQuery();
			// PARA RECORER LAS LINEAS QUE NOS SALGA EN LA SENTENCIA QUERY ANTERIOR
			// MIENTRAS QUE HAYA SIGUIENTE
			while (rs.next()) {
				// ALMACENA EL DATO DE LA COLUMNA NOMBRE EN LA VARIABLE nombre
				contador_id_user = rs.getInt(1);
			}
			cerrar();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERROR. No se ha podido obtener si estas registrado como usuario.");
		}
		return contador_id_user;
	}
	// ------------------------------------------------------------------------------------------------------
	// MÉTODO OBTENER ID_MEMBRESIA
	// ------------------------------------------------------------------------------------------------------
	/**
	 * El método obtenerID_MEMBRESIA() adquiere el tipo de membresía de un usuario.
	 */
	public int obtenerID_MEMBRESIA (int id_user) {
		int id_membresia=0;
		try {
			// LLAMO AL MÉTODO CONECTAR
			conectar();
			// NOS CONECTAMOS A LA BASE DE DATOS
			st=connection.createStatement();
			// REALIZAMOS EL SELECT CON LOS DATOS QUE QUEREMOS ALMACENAR (? --> ES UN CAMPO QUE LO VA A ADQUIRIR DE LA VARIABLE correos) (SIRVE PARA CREAR LA SENTENCIA SQL)
			PreparedStatement statement4 = connection.prepareStatement("select ID_MEMBRESIAS from CON_MEMBRESIAS WHERE ID_USUARIO = ?");
			// LE DECIMOS QUE LA ? ANTERIOR COJA EL VALOR DE LA VARIABLE correos (1 ES EL NÚMERO DE ? Y correos EL VALOR QUE LE QUEREMOS METER EN ESTE CASO EL VALOR ALMACENADO EN LA VARIABLE)
			statement4.setInt(1, id_user);
			// EJECUTAMOS LA QUERY ANTERIOR
			rs = statement4.executeQuery();
			// PARA RECORER LAS LINEAS QUE NOS SALGA EN LA SENTENCIA QUERY ANTERIOR
			// MIENTRAS QUE HAYA SIGUIENTE
			while (rs.next()) {
				// ALMACENA EL DATO DE LA COLUMNA NOMBRE EN LA VARIABLE nombre
				id_membresia = rs.getInt(1);
			}
			cerrar();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERROR. No se ha podido obtener que membresia tienes.");
		}
		return id_membresia;
	}
	// ----------------------------------------------------------------------------------------
	// CONSTRUCTOR
	// ----------------------------------------------------------------------------------------
	/**
	 * En el constructor Inicio_Sesion() están todos los atributos y métodos necesarios para cuando creemos el objeto y tenga todas las funcionalidades.
	 */
	public Inicio_Sesion() {
		setType(Type.UTILITY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1180, 703);
		getContentPane().setLayout(null);
		//--------------------------------------------------------------------------------------------------------
		//PANEL INICIO DE SESIÓN	
		//--------------------------------------------------------------------------------------------------------
		JPanel Panel_InicioSesion = new JPanel();
		Panel_InicioSesion.setLayout(null);
		Panel_InicioSesion.setBounds(0, 0, 1164, 664);
		getContentPane().add(Panel_InicioSesion);
		//--------------------------------------------------------------------------------------------------------
		//lbl_Logo ES NUESTRO LOGO TRILOGY	
		//--------------------------------------------------------------------------------------------------------	
		JLabel lbl_Logo = new JLabel();
		lbl_Logo.setBounds(371, 56, 485, 123);
		ImageIcon logo = new ImageIcon(getClass().getResource("/Trilogy_imagenes/LogoTrilogy.jpg"));//
		ImageIcon imgfondo = new ImageIcon(logo.getImage().getScaledInstance(lbl_Logo.getWidth(), lbl_Logo.getHeight(), Image.SCALE_SMOOTH));
		lbl_Logo.setIcon(imgfondo);
		//--------------------------------------------------------------------------------------------------------
		//lbl_PuertaSalida ES BOTÓN DE SALIDA
		//--------------------------------------------------------------------------------------------------------
		lbl_PuertaSalida = new JLabel();
		lbl_PuertaSalida.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lbl_PuertaSalida.setBounds(10, 596, 57, 57);
		lbl_PuertaSalida.setOpaque(true);
		lbl_PuertaSalida.setBackground(new Color(32,171,85));
		ImageIcon puerta = new ImageIcon(getClass().getResource("/Trilogy_imagenes/IconoLogout.png"));// DIRECCION DE LA IMAGEN QUE QUIERO AJUSTAR
		ImageIcon imgfondopuerta = new ImageIcon(puerta.getImage().getScaledInstance(lbl_PuertaSalida.getWidth(), lbl_PuertaSalida.getHeight(), Image.SCALE_SMOOTH)); // CREAMOS OTRO OBJETO PARA QUE SE AJUSTE AUTOMATICAMENTE LA IMAGEN AL LABEL
		lbl_PuertaSalida.setIcon(imgfondopuerta);
		lbl_PuertaSalida.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lbl_PuertaSalida.setBackground(new Color(153,255,153));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lbl_PuertaSalida.setBackground(new Color(32,171,85));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		//--------------------------------------------------------------------------------------------------------
		//USUARIO
		//--a------------------------------------------------------------------------------------------------------
		//--------------------------------------------------------------------------------------------------------
		//lblusuariotitulo EL TÍTULO QUE INDICA DONDE INTRODUCIR EL USUARIO
		//--------------------------------------------------------------------------------------------------------
		JLabel lblusuariotitulo = new JLabel("USUARIO");
		lblusuariotitulo.setOpaque(true);
		lblusuariotitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblusuariotitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblusuariotitulo.setBorder(new LineBorder(new Color(211, 211, 211)));
		lblusuariotitulo.setBackground(new Color(255,255,255));//Color Blanco
		lblusuariotitulo.setBounds(371, 236, 485, 30);
		//--------------------------------------------------------------------------------------------------------
		//text_introducirUsuario ESCRIBIR EL USUARIO
		//--------------------------------------------------------------------------------------------------------
		text_introducirUsuario = new JTextField();
		text_introducirUsuario.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if(text_introducirUsuario.getText().equalsIgnoreCase("Introduce tu usuario")) { //CONDICIÓN PARA QUE SE MANTENGA EL TEXTO DEL USUARIO SI LO HAN ESCRITO
					text_introducirUsuario.setText("");
					text_introducirUsuario.setForeground(new Color( 0, 0, 0)); //COLOR NEGRO
					text_introducirUsuario.setBackground(new Color(255,255,255));//COLOR BLANCO
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (text_introducirUsuario.getText().length() == 0) {
					text_introducirUsuario.setForeground(new Color(192, 192, 192));
					text_introducirUsuario.setText("Introduce tu usuario");
					text_introducirUsuario.setBackground(UIManager.getColor("TextField.background"));		                                                                                                  
				}
			}
		});
		text_introducirUsuario.setText("Introduce tu usuario");
		text_introducirUsuario.setForeground(new Color(211, 211, 211));
		text_introducirUsuario.setFont(new Font("Tahoma", Font.PLAIN, 14));
		text_introducirUsuario.setColumns(10);
		text_introducirUsuario.setBackground(new Color(255,255,255));//COLOR BLANCO
		text_introducirUsuario.setBounds(371, 280, 485, 30);	
		//--------------------------------------------------------------------------------------------------------
		// CONTRASEÑA		
		//--------------------------------------------------------------------------------------------------------
		//--------------------------------------------------------------------------------------------------------
		//lblcontraseñatitulo EL TÍTULO QUE INDICA DONDE INTRODUCIR LA CONTRASEÑA
		//--------------------------------------------------------------------------------------------------------
		JLabel lblcontraseñatitulo = new JLabel("CONTRASEÑA");
		lblcontraseñatitulo.setOpaque(true);
		lblcontraseñatitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblcontraseñatitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblcontraseñatitulo.setBorder(new LineBorder(new Color(211, 211, 211)));//Color gris
		lblcontraseñatitulo.setBackground(new Color(255,255,255));//Color Blanco
		lblcontraseñatitulo.setBounds(373, 350, 483, 30);
		//--------------------------------------------------------------------------------------------------------
		//passwordField ESCRIBIR LA CONTRASEÑA
		//--------------------------------------------------------------------------------------------------------
		passwordField = new JPasswordField();
		passwordField.addFocusListener(new FocusAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void focusGained(FocusEvent e) {
				if(passwordField.getText().equalsIgnoreCase("******************")) { //CONDICION PARA QUE SE MANTENGA EL TEXTO DEL USUARIO SI LO HAN ESCRITO
					passwordField.setText("");
					passwordField.setForeground(new Color( 0, 0, 0)); //Color negro
					passwordField.setBackground(new Color(255,255,255));//Color Blanco
				}
			}
			@SuppressWarnings("deprecation")
			@Override
			public void focusLost(FocusEvent e) {
				if (passwordField.getText().length() == 0) {
					passwordField.setForeground(new Color(192, 192, 192));
					passwordField.setText("******************");
					passwordField.setBackground(UIManager.getColor("passwordField.background"));		                                                                                                  
				}
			}
		});
		passwordField.setToolTipText("");
		passwordField.setText("******************");
		passwordField.setForeground(new Color(211, 211, 211));//Color gris
		passwordField.setBounds(371, 391, 485, 30);
		//--------------------------------------------------------------------------------------------------------
		//btnInicioSesion EL BOTÓN INICIO DE SESIÓN	
		//--------------------------------------------------------------------------------------------------------
		btnInicioSesion = new JButton("INICIAR SESIÓN");
		btnInicioSesion.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (comprobador_pag_admin==true) {
					comprobador_pag_admin=false;
					String correo= text_introducirUsuario.getText().toUpperCase();
					@SuppressWarnings("deprecation")
					String passwd=passwordField.getText();
					int id_user=id_user_For_Correo (correo);
					int contador_id_user= countAdmin (id_user);
					int contador_passwd=countPasswd(passwd,id_user);		
					if (contador_id_user==1 && contador_passwd==1) {
						JOptionPane.showMessageDialog(null, "Usuario correcto, espere unos segundos para entrar a la pagina del administrador.");
						Pagina_administrador pag_admin = new Pagina_administrador(); // INSTANCIAR LA VENTANA DE REGISTRO.
						pag_admin.setVisible(true); // MUESTRA LA VENTANA SELECIONADA
						try {
							Pagina_administrador.conectar();
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, "ERROR. No se ha podido conectar a la base de datos. Perdone las molestias.");
						}
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "ERROR. El usuario introducido no es administrador o la contraseña es incorrecta. Perdone las molestias.");
					}
				} else {
					comprobador_pag_admin=false;
					String correo= text_introducirUsuario.getText().toUpperCase();
					@SuppressWarnings("deprecation")
					String passwd=passwordField.getText();
					int id_user=id_user_For_Correo (correo);
					int contador_id_user = countID_USER(id_user);
					int contador_passwd2=countPasswd(passwd,id_user);
					if (contador_id_user==1 && contador_passwd2==1) {
						id_de_membresia = obtenerID_MEMBRESIA(id_user);
						correo_usuario=text_introducirUsuario.getText().toUpperCase();
						JOptionPane.showMessageDialog(null, "Usuario correcto, espere unos segundos para empezar a ver su contenido audiovisual.");
						Panel_iz_Principal pan_iz = new Panel_iz_Principal();
						pan_iz.setVisible(true);
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "ERROR. El usuario introducido no existe o la contraseña es incorrecta. Perdone las molestias.");
					}
				}		
			}});
		btnInicioSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnInicioSesion.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnInicioSesion.setBackground(new Color(255,255,255));//Color Blanco
		btnInicioSesion.setBounds(623, 473, 248, 57);
		Panel_InicioSesion.add(btnInicioSesion);
		//--------------------------------------------------------------------------------------------------------
		//btnRegistrarse ES EL BOTÓN DE REGISTRO
		//--------------------------------------------------------------------------------------------------------
		btnRegistrarse = new JButton("REGISTRARSE");
		btnRegistrarse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnRegistrarse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					cerrar();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "ERROR. No se ha podido cerrar la base de datos. Perdone las molestias.");
				}
				Formulario formularioReg = new Formulario(); // INSTANCIAR LA VENTANA DE REGISTRO.
				formularioReg.setVisible(true); // MUESTRA LA VENTANA SELECIONADA
				dispose(); // CIERRA LA VENTANA ACTUAL
			}
		});
		btnRegistrarse.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnRegistrarse.setBackground(new Color(255,255,255));//Color Blanco
		btnRegistrarse.setBounds(357, 473, 248, 57);
		Panel_InicioSesion.add(btnRegistrarse);
		//--------------------------------------------------------------------------------------------------------
		//FONDO DE PANTALA INICIO DE SESIÓN	
		//--------------------------------------------------------------------------------------------------------	
		//--------------------------------------------------------------------------------------------------------
		//lblBordeSuperior BARRA VERDE SUPERIOR
		//--------------------------------------------------------------------------------------------------------
		JLabel lblBordeSuperior = new JLabel();
		lblBordeSuperior.setOpaque(true);
		lblBordeSuperior.setBackground(new Color(153, 255, 153));
		lblBordeSuperior.setBounds(0, 0, 1180, 132);
		//--------------------------------------------------------------------------------------------------------
		//lblFondo_Verde FONDO VERDE
		//--------------------------------------------------------------------------------------------------------
		JLabel lblFondo_Verde = new JLabel();
		lblFondo_Verde.setBounds(0, 128, 1280, 536);
		ImageIcon fondo1 = new ImageIcon(getClass().getResource("/Trilogy_imagenes/Fondo_Formulario.png"));// DIRECCION DE LA IMAGEN QUE QUIERO AJUSTAR
		ImageIcon imgfondo1 = new ImageIcon(fondo1.getImage().getScaledInstance(lblFondo_Verde.getWidth(), lblFondo_Verde.getHeight(), Image.SCALE_SMOOTH)); // CREAMOS OTRO OBJETO PARA QUE SE AJUSTE AUTOMATICAMENTE LA IMAGEN AL LABEL
		lblFondo_Verde.setIcon(imgfondo1);
		//--------------------------------------------------------------------------------------------------------
		// btnEngranaje_Admin BOTÓN ENGRANAJE PARA INICIO ADMINISTRADOR
		//--------------------------------------------------------------------------------------------------------
		btnEngranaje_Admin = new JButton();
		btnEngranaje_Admin.setBounds(1132, 0, 32, 32);
		btnEngranaje_Admin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnEngranaje_Admin.setBackground(new Color(241,245,245)); // COLOR DE FONDO
		btnEngranaje_Admin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				comprobador_pag_admin=true;
				JOptionPane.showMessageDialog(null, "Introduce el correo y contraseña y despues derle al botón de Iniciar sesión. En caso de ser incorrecto tendras que volver a pulsar este botón.");
			}
		});
		// AJUSTAR IMAGEN AL CONTENIDO
		ImageIcon engranaje_admin = new ImageIcon(getClass().getResource("/Trilogy_imagenes/IconoEngranaje.png")); // CREACIÓN NUEVO OBJETO CON LA RUTA DE LA IMAGEN
		ImageIcon imgEngranaje_admin = new ImageIcon(engranaje_admin.getImage().getScaledInstance(btnEngranaje_Admin.getWidth(), btnEngranaje_Admin.getHeight(), Image.SCALE_SMOOTH)); // CREAMOS OTRO OBJETO PARA QUE SE AJUSTE AUTOMATICAMENTE LA IMAGEN
		btnEngranaje_Admin.setIcon(imgEngranaje_admin);
		// ----------------------------------------------------------------------------------------
		// JERARQUÍA DEL panel_contenedor
		// ----------------------------------------------------------------------------------------
		Panel_InicioSesion.add(btnEngranaje_Admin);
		Panel_InicioSesion.add(lbl_Logo);  
		Panel_InicioSesion.add(lbl_PuertaSalida); 
		Panel_InicioSesion.add(lblusuariotitulo);
		Panel_InicioSesion.add(text_introducirUsuario);
		Panel_InicioSesion.add(lblcontraseñatitulo);
		Panel_InicioSesion.add(passwordField);
		Panel_InicioSesion.add(btnInicioSesion);
		Panel_InicioSesion.add(lblBordeSuperior);
		Panel_InicioSesion.add(lblFondo_Verde);
	}
}