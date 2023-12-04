/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entregable;

import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author carol
 */
public class Ejecutable {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int opcion = 0;
        int id_estudiante, id_curso;
        boolean continuar = true;

        String url = "jdbc:mariadb://localhost:3306/instituto";
        String usuario = "root";
        String contraseña = "penalba12";

        try {
            Metodos m = new Metodos(url, usuario, contraseña);

            m.crearTablas();

            do {
                while (continuar) {

                    System.out.println("------MENU------");
                    System.out.println("1 -> Insertar estudiantes.");
                    System.out.println("2 -> Insertar cursos.");
                    System.out.println("3 -> Registar inscripciones de estudiantes en cursos.");
                    System.out.println("4 -> Consultar estudiantes inscritos en un curso especifico.");
                    System.out.println("5 -> Consultar cursos donde estudiante particular esta inscrito.");
                    System.out.println("6 -> Actualizar información estudiantes.");
                    System.out.println("7 -> Actualizar información cursos.");
                    System.out.println("8 -> Eliminar estudiantes.");
                    System.out.println("9 -> Eliminar cursos.");
                    System.out.println("10 -> Eliminar incripciones.");
                    System.out.println("¿Que opcion desea realizar?, pon 0 para terminar.");

                    try {
                        opcion = Integer.parseInt(sc.nextLine());

                        if ((opcion != 0) || (continuar = false)) {

                            switch (opcion) {
                                case 1:
                                    System.out.print("Ingrese el nombre del estudiante: ");
                                    String nombre_estudiante = sc.nextLine();
                                    System.out.print("Ingrese la edad: ");
                                    int edad = Integer.parseInt(sc.nextLine());
                                    if (edad >= 1) {
                                        System.out.print("Ingrese la dirección: ");
                                        String direccion = sc.nextLine();
                                        System.out.print("Ingrese el correo electronico: ");
                                        String correo = sc.nextLine();
                                        m.insertarEstudiantes(nombre_estudiante, edad, direccion, correo);
                                    } else {
                                        System.out.println("Error edad invalida");
                                    }
                                    break;

                                case 2:
                                    System.out.print("Ingrese el nombre del curso: ");
                                    String nombre_curso = sc.nextLine();
                                    System.out.print("Ingrese la descripcion: ");
                                    String descripcion = sc.nextLine();
                                    System.out.print("Ingrese los creditos: ");
                                    int creditos = Integer.parseInt(sc.nextLine());
                                    m.insertarCursos(nombre_curso, descripcion, creditos);
                                    break;

                                case 3:
                                    System.out.print("Ingrese el nombre del estudiante: ");
                                    nombre_estudiante = sc.nextLine();
                                    System.out.print("Ingrese el nombre del curso: ");
                                    nombre_curso = sc.nextLine();
                                    try {
                                        m.obtenerEstudientesMismoNombre(nombre_estudiante);
                                        m.obtenerCursosMismoNombre(nombre_curso);
                                        System.out.print("Ingrese el ID del estudiante: ");
                                        id_estudiante = Integer.parseInt(sc.nextLine());
                                        System.out.print("Ingrese el ID del curso: ");
                                        id_curso = Integer.parseInt(sc.nextLine());
                                        m.registrarInscripciones(id_estudiante, id_curso);
                                    } catch (SQLException ex) {
                                        System.err.println(ex);
                                    }
                                    break;

                                case 4:
                                    System.out.print("Ingrese el nombre del curso: ");
                                    nombre_curso = sc.nextLine();
                                    try {
                                        m.obtenerCursosMismoNombre(nombre_curso);
                                        System.out.print("Ingrese el ID del curso para ver los estudiantes inscritos: ");
                                        id_curso = Integer.parseInt(sc.nextLine());
                                        m.consultarEstudiantesEnCurso(id_curso);
                                    } catch (SQLException ex) {
                                        System.err.println(ex);
                                    }
                                    break;

                                case 5:
                                    System.out.print("Ingrese el nombre del estudiante: ");
                                    nombre_estudiante = sc.nextLine();
                                    try {
                                        m.obtenerEstudientesMismoNombre(nombre_estudiante);
                                        System.out.print("Ingrese el ID del estudiante para ver en que cursos esta: ");
                                        id_estudiante = Integer.parseInt(sc.nextLine());
                                        m.consultarCursosDeUnEstudiante(id_estudiante);
                                    } catch (SQLException ex) {
                                        System.err.println(ex);
                                    }
                                    break;

                                case 6:
                                    System.out.print("Ingrese el nombre del estudiante que desea actualizar: ");
                                    nombre_estudiante = sc.nextLine();
                                    try {
                                        m.obtenerEstudientesMismoNombre(nombre_estudiante);
                                        System.out.print("Ingrese el ID del estudiante que desea actualizar: ");
                                        id_estudiante = Integer.parseInt(sc.nextLine());
                                        m.actualizarEstudiantesMenu(id_estudiante);
                                    } catch (SQLException ex) {
                                        System.err.println(ex);
                                    }
                                    break;

                                case 7:
                                    System.out.print("Ingrese el nombre del curso que desea actualizar: ");
                                    nombre_curso = sc.nextLine();
                                    try {
                                        m.obtenerCursosMismoNombre(nombre_curso);
                                        System.out.print("Ingrese el ID del curso que desea actualizar: ");
                                        id_curso = Integer.parseInt(sc.nextLine());
                                        m.actualizarCursosMenu(id_curso);
                                    } catch (SQLException ex) {
                                        System.err.println(ex);
                                    }
                                    break;

                                case 8:
                                    System.out.print("Ingrese el nombre del estudiante: ");
                                    nombre_estudiante = sc.nextLine();
                                    try {
                                        m.obtenerEstudientesMismoNombre(nombre_estudiante);
                                        System.out.print("Ingrese el ID del estudiante que desea eliminar: ");
                                        id_estudiante = Integer.parseInt(sc.nextLine());
                                        m.eliminarEstudiantes(id_estudiante);
                                    } catch (SQLException ex) {
                                        System.err.println(ex);
                                    }
                                    break;

                                case 9:
                                    System.out.print("Ingrese el nombre del curso: ");
                                    nombre_curso = sc.nextLine();
                                    try {
                                        m.obtenerCursosMismoNombre(nombre_curso);
                                        System.out.print("Ingrese el ID del curso que desea eliminar: ");
                                        id_curso = Integer.parseInt(sc.nextLine());
                                        m.eliminarCursos(id_curso);
                                    } catch (SQLException ex) {
                                        System.out.println(ex);
                                    }
                                    break;

                                case 10:
                                    m.verTablaInscripciones();
                                    System.out.print("Ingrese el ID de la inscripcion que desea eliminar: ");
                                    int id_inscripcion = Integer.parseInt(sc.nextLine());
                                    m.eliminarInscripciones(id_inscripcion);
                                    break;

                                default:
                                    System.out.println("Opcion no valida.");
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error, no es un numero.");
                    }
                }

            } while ((opcion != 0) && (continuar = false));
            m.close();

        } catch (SQLException ex) {
            System.out.println("No se podido conectar a la base de datos. " + ex);
        }
    }
}
