/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entregable;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author carol
 */
public class Metodos {

    private Connection conn = null;

    public Metodos(String url, String usuario, String contraseña) throws SQLException {
        conn = DriverManager.getConnection(url, usuario, contraseña);
        if (conn != null) {
            System.out.println("Conexion exitosa a la base de datos.");
        }
    }

    public void crearTablas() {
        String crearTablaEstudiantes = "CREATE TABLE IF NOT EXISTS estudiantes ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "nombre VARCHAR(50),"
                + "edad INT,"
                + "direccion VARCHAR(100),"
                + "correoElectronico VARCHAR(100)"
                + ")";

        String crearTablaCursos = "CREATE TABLE IF NOT EXISTS cursos("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "nombre VARCHAR(50),"
                + "descripcion VARCHAR(100),"
                + "creditos INT"
                + ")";

        String crearTablaInscripciones = "CREATE TABLE IF NOT EXISTS inscripciones("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "id_estudiante INT,"
                + "id_curso INT,"
                + "fecha_inscripcion DATE,"
                + "FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id) ON DELETE CASCADE,"
                + "FOREIGN KEY (id_curso) REFERENCES cursos(id) ON DELETE CASCADE"
                + ")";

        try {
            Statement s = conn.createStatement();
            s.execute(crearTablaEstudiantes);
            s.execute(crearTablaCursos);
            s.execute(crearTablaInscripciones);
            s.close();
            System.out.println("Tablas creadas correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al crear alguna de las tablas. " + ex);
        }
    }

    public void insertarEstudiantes(String nombre_estudiante, int edad, String direccion, String correo) {
        String insertarEstudiante = "INSERT INTO estudiantes (nombre, edad, direccion, correoElectronico) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(insertarEstudiante);
            ps.setString(1, nombre_estudiante);
            ps.setInt(2, edad);
            ps.setString(3, direccion);
            ps.setString(4, correo);
            ps.execute();
            ps.close();
            System.out.println("Estudiante insertado correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al insertar el estudiante. " + ex);
        }
    }

    public void insertarCursos(String nombre_curso, String descripcion, int creditos) {
        String insertarCurso = "INSERT INTO cursos (nombre, descripcion, creditos) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(insertarCurso);
            ps.setString(1, nombre_curso);
            ps.setString(2, descripcion);
            ps.setInt(3, creditos);
            ps.execute();
            ps.close();
            System.out.println("Curso insertado correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al insertar el curso. " + ex);
        }
    }

    public void registrarInscripciones(int id_estudiante, int id_curso) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese la fecha de inscripcion (yyyy-MM-dd): ");
        String fecha = sc.nextLine();

        Date registrarFecha = null;

        try {
            registrarFecha = Date.valueOf(fecha);
        } catch (IllegalArgumentException ex) {
            System.out.println("Formato de fecha incorrecto. " + ex);
            return; //Para salir del metodos si la fecha es incorrecta
        }

        String registrarInscripcion = "INSERT INTO inscripciones (id_estudiante, id_curso, fecha_inscripcion) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(registrarInscripcion);
            ps.setInt(1, id_estudiante);
            ps.setInt(2, id_curso);
            ps.setDate(3, registrarFecha);
            ps.execute();
            ps.close();
            System.out.println("Inscripcion registrada correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al registrar la inscripcion. " + ex);
        }
    }

    public void consultarEstudiantesEnCurso(int id_curso) {
        String consultarEstudiantes = """
                                      SELECT estudiantes.id, estudiantes.nombre, estudiantes.edad, estudiantes.direccion, estudiantes.correoElectronico
                                      FROM estudiantes JOIN inscripciones ON estudiantes.id = inscripciones.id_estudiante
                                      WHERE inscripciones.id_curso = ?""";

        try {
            PreparedStatement ps = conn.prepareStatement(consultarEstudiantes);
            ps.setInt(1, id_curso);

            ResultSet rs = ps.executeQuery();

            System.out.println("Estudiantes inscritos en el curso: ");
            while (rs.next()) {
                int id_estudiante = rs.getInt("id");
                String nombre = rs.getString("nombre");
                int edad = rs.getInt("edad");
                String direccion = rs.getString("direccion");
                String correo = rs.getString("correoElectronico");
                System.out.println("Id: " + id_estudiante + ", Nombre: " + nombre
                        + ", Edad: " + edad + ", Direccion: " + direccion + ", Correo: " + correo);
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al hacer la consulta. " + ex);
        }
    }

    public void consultarCursosDeUnEstudiante(int id_estudiante) {
        String consultarCursos = """
                                 SELECT cursos.id, cursos.nombre, cursos.descripcion, cursos.creditos
                                 FROM cursos JOIN inscripciones ON cursos.id = inscripciones.id_curso
                                 WHERE inscripciones.id_estudiante = ?""";

        try {
            PreparedStatement ps = conn.prepareStatement(consultarCursos);
            ps.setInt(1, id_estudiante);

            ResultSet rs = ps.executeQuery();

            System.out.println("Cursos de este estudiante: ");
            while (rs.next()) {
                int id_curso = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                int creditos = rs.getInt("creditos");
                System.out.println("Id: " + id_curso + ", Nombre: " + nombre
                        + ", Descripcion: " + descripcion + ", Creditos: " + creditos);
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al hacer la consulta. " + ex);
        }
    }

    public void actualizarEstudiantesMenu(int id_estudiante) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("------MENU------");
            System.out.println("1. Nombre");
            System.out.println("2. Edad");
            System.out.println("3. Dirección");
            System.out.println("4. Correo electronico");
            System.out.println("¿Qué opción desea actualizar?, pon 0 para salir.");

            while (!sc.hasNextInt()) {
                System.out.println("Error no es un numero. ");
                System.out.println("¿Qué opcion desea actualizar?, pon 0 para salir.");
                sc.nextLine();
            }
            opcion = sc.nextInt();

            if (opcion != 0) {
                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese el nuevo nombre: ");
                        String nuevoNombre = sc.nextLine();
                        actualizarEstudiantes(id_estudiante, "nombre", nuevoNombre);
                        break;
                    case 2:
                        System.out.print("Ingrese la nueva edad: ");
                        while (!sc.hasNextInt()) {
                            System.out.println("Error no es un numero. ");
                            System.out.print("Ingrese la nueva edad: ");
                            sc.next();
                        }
                        int nuevaEdad = sc.nextInt();
                        actualizarEstudiantes(id_estudiante, "edad", String.valueOf(nuevaEdad));
                        break;
                    case 3:
                        System.out.print("Ingrese la nueva direccion: ");
                        String nuevaDireccion = sc.nextLine();
                        actualizarEstudiantes(id_estudiante, "direccion", nuevaDireccion);
                        break;
                    case 4:
                        System.out.print("Ingrese el nuevo correo: ");
                        String nuevoCorreo = sc.nextLine();
                        actualizarEstudiantes(id_estudiante, "correoElectronico", nuevoCorreo);
                        break;
                    default:
                        System.out.println("Opcion no valida.");
                }
            }
        } while (opcion != 0);
    }

    public void actualizarEstudiantes(int id_estudiante, String campoElegido, String nuevoValor) {
        String actualizarEstudiante = "UPDATE estudiantes SET " + campoElegido + " = ? WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(actualizarEstudiante);
            ps.setString(1, nuevoValor);
            ps.setInt(2, id_estudiante);
            ps.executeUpdate();
            ps.close();
            System.out.println("Estudiante actualizado.");
        } catch (SQLException ex) {
            System.out.println("Error al actualizar el estudiante. " + ex);
        }
    }

    public void actualizarCursosMenu(int id_curso) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("------MENU------");
            System.out.println("1 -> Nombre");
            System.out.println("2 -> Descripcion");
            System.out.println("3 -> Creditos");
            System.out.println("¿Qué opcion desea actualizar?, pon 0 para salir.");

            while (!sc.hasNextInt()) {
                System.out.println("Error no es un numero. ");
                System.out.println("¿Qué opcion desea actualizar?, pon 0 para salir.");
                sc.nextLine();
            }
            opcion = sc.nextInt();

            if (opcion != 0) {
                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese el nuevo nombre: ");
                        String nuevoNombre = sc.nextLine();
                        actualizarCursos(id_curso, "nombre", nuevoNombre);
                        break;
                    case 2:
                        System.out.print("Ingrese la nueva descripción: ");
                        String nuevaDescripcion = sc.nextLine();
                        actualizarCursos(id_curso, "descripcion", nuevaDescripcion);
                        break;
                    case 3:
                        System.out.print("Ingrese los nuevos creditos: ");
                        while (!sc.hasNextInt()) {
                            System.out.println("Error no es un numero. ");
                            System.out.print("Ingrese los nuevos creditos: ");
                            sc.next();
                        }
                        int nuevosCreditos = sc.nextInt();
                        actualizarCursos(id_curso, "creditos", String.valueOf(nuevosCreditos));
                        break;
                    default:
                        System.out.println("Opción no valida.");
                }
            }
        } while (opcion != 0);
    }

    public void actualizarCursos(int id_curso, String campoElegido, String nuevoValor) {
        String actualizarCurso = "UPDATE cursos SET " + campoElegido + " = ? WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(actualizarCurso);
            ps.setString(1, nuevoValor);
            ps.setInt(2, id_curso);
            ps.executeUpdate();
            ps.close();
            System.out.println("Curso actualizado.");
        } catch (SQLException ex) {
            System.out.println("Error al actualizar el curso. " + ex);
        }
    }

    public void eliminarEstudiantes(int id_estudiante) {
        String eliminarEstudiante = "DELETE FROM estudiantes WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(eliminarEstudiante);
            ps.setInt(1, id_estudiante);
            ps.executeUpdate();
            ps.close();
            System.out.println("Estudiante eliminado");
        } catch (SQLException ex) {
            System.out.println("Error al eliminar estudiante " + ex);
        }
    }

    public void eliminarCursos(int id_curso) {
        String eliminarCurso = "DELETE FROM cursos WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(eliminarCurso);
            ps.setInt(1, id_curso);
            ps.executeUpdate();
            ps.close();
            System.out.println("Curso eliminado.");
        } catch (SQLException ex) {
            System.out.println("Error al eliminar el curso. " + ex);
        }
    }

    public void eliminarInscripciones(int id_inscripciones) {
        String eliminarInscripcion = "DELETE FROM inscripciones WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(eliminarInscripcion);
            ps.setInt(1, id_inscripciones);
            ps.executeUpdate();
            ps.close();
            System.out.println("Inscripcion eliminada.");
        } catch (SQLException ex) {
            System.out.println("Error al eliminar la inscripcion. " + ex);
        }
    }

    public void obtenerEstudientesMismoNombre(String nombre_estudiante) throws SQLException {
        String obtenerEstudiantes = "SELECT id, nombre, edad, direccion, correoElectronico "
                + "FROM estudiantes WHERE nombre LIKE ?";
        int cont = 0;

        try {
            PreparedStatement ps = conn.prepareStatement(obtenerEstudiantes);
            ps.setString(1, "%" + nombre_estudiante + "%");

            ResultSet rs = ps.executeQuery();

            System.out.println("Estudiante/es con ese nombre: ");
            while (rs.next()) {
                cont++;
                int id_estudiante = rs.getInt("id");
                String nombreEstudiante = rs.getString("nombre");
                int edad = rs.getInt("edad");
                String direccion = rs.getString("direccion");
                String correo = rs.getString("correoElectronico");
                System.out.println("Id: " + id_estudiante + ", Nombre: " + nombreEstudiante
                        + ", Edad: " + edad + ", Direccion: " + direccion + ", Correo: " + correo);
            }
            ps.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (cont == 0) {
            throw new SQLException("Este estudiante no existe.");
        }
    }

    public void obtenerCursosMismoNombre(String nombre_curso) throws SQLException {
        String obtenerCursos = "SELECT id, nombre, descripcion, creditos FROM cursos WHERE nombre LIKE ?";
        int cont = 0;

        try {
            PreparedStatement ps = conn.prepareStatement(obtenerCursos);
            ps.setString(1, "%" + nombre_curso + "%");

            ResultSet rs = ps.executeQuery();

            System.out.println("Curso/os con ese nombre: ");
            while (rs.next()) {
                cont++;
                int id_curso = rs.getInt("id");
                String nombreCurso = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                int creditos = rs.getInt("creditos");
                System.out.println("Id: " + id_curso + ", Nombre: " + nombreCurso
                        + ", Descripcion: " + descripcion + ", Creditos: " + creditos);
            }
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (cont == 0) {
            throw new SQLException("Este curso no existe");
        }
    }

    public void verTablaInscripciones() {
        String obtenerInscripciones = "SELECT * FROM inscripciones";

        try {
            PreparedStatement ps = conn.prepareStatement(obtenerInscripciones);

            ResultSet rs = ps.executeQuery();

            System.out.println("Tabla inscripciones: ");
            while (rs.next()) {
                int id_inscripcion = rs.getInt("id");
                int id_estudiante = rs.getInt("id_estudiante");
                int id_curso = rs.getInt("id_curso");
                String fecha = rs.getString("fecha_inscripcion");
                System.out.println("Id: " + id_inscripcion + ", Id estudiante: " + id_estudiante
                        + ", Id curso: " + id_curso + ", Fecha inscripcion: " + fecha);
            }
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Conexion cerrada correctamente");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexion: " + e);
        }
    }
}
