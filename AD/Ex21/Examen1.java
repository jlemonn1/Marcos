package AD.Ex21;

import java.sql.*;
import java.util.Scanner;

public class Examen1 {

    public static void main(String[] args) {
        try (Scanner teclado = new Scanner(System.in)) {
            Connection con = null;
            System.out.println("Con qué BBDD vas a conectar (1 o 2):");
            System.out.println("1) SQLite"); 
            System.out.println("2) HSQLDB"); 
            int opcion = teclado.nextInt();

            try {
                if (opcion == 1) {
                    con = DriverManager.getConnection("jdbc:sqlite:alumnos.db");
                } else if (opcion == 2) {
                    con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/alumnosdb", "SA", "");
                } else {
                    System.out.println("Opción no válida");
                    return;
                }

                DatabaseMetaData metaData = con.getMetaData();
                System.out.println("El nombre del driver es: " + metaData.getDriverName());

                int control = 0;
                while (control != 5) {
                    System.out.println("\nSeleccione una opción:");
                    System.out.println("1) Mostrar todos los alumnos");
                    System.out.println("2) Mostrar todos los municipios");
                    System.out.println("3) Añadir un nuevo alumno");
                    System.out.println("4) Añadir un nuevo municipio");
                    System.out.println("5) Salir");
                    control = teclado.nextInt();

                    switch (control) {
                        case 1 -> mostrarAlumnos(con);
                        case 2 -> mostrarMunicipios(con);
                        case 3 -> nuevoAlumno(con, teclado);
                        case 4 -> nuevoMunicipio(con, teclado);
                        case 5 -> System.out.println("Saliendo...");
                        default -> System.out.println("Opción no válida.");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error en la conexión: " + e.getMessage());
            } finally {
                if (con != null) con.close();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void mostrarAlumnos(Connection con) throws SQLException {
        String sql = "SELECT * FROM alumnos";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("NIA: " + rs.getString("nia") +
                                   ", Nombre: " + rs.getString("nombre") +
                                   ", Apellido: " + rs.getString("apellidos") +
                                   ", Municipio: " + rs.getString("id_municipio"));
            }
        }
    }

    public static void mostrarMunicipios(Connection con) throws SQLException {
        String sql = "SELECT * FROM municipios";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID Municipio: " + rs.getString("id_municipio") +
                                   ", Nombre: " + rs.getString("nombre"));
            }
        }
    }

    public static void nuevoAlumno(Connection con, Scanner teclado) throws SQLException {
        teclado.nextLine(); // Limpiar buffer
        System.out.print("Introduce el NIA: ");
        String nia = teclado.nextLine();
        System.out.print("Introduce el nombre: ");
        String nombre = teclado.nextLine();
        System.out.print("Introduce los apellidos: ");
        String apellidos = teclado.nextLine();
        System.out.print("Introduce el ID de municipio: ");
        String municipio = teclado.nextLine();

        String sql = "INSERT INTO alumnos (nia, nombre, apellidos, id_municipio) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, nia);
            pstmt.setString(2, nombre);
            pstmt.setString(3, apellidos);
            pstmt.setString(4, municipio);
            pstmt.executeUpdate();
            System.out.println("Alumno añadido correctamente.");
        }
    }

    public static void nuevoMunicipio(Connection con, Scanner teclado) throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        if (metaData.getDriverName().contains("HSQLDB")) {
            teclado.nextLine(); // Limpiar buffer
            System.out.print("Introduce el nombre del municipio: ");
            String nombre = teclado.nextLine();
            System.out.print("Introduce el número de habitantes: ");
            int habitantes = teclado.nextInt();
            teclado.nextLine(); // Limpiar buffer
            System.out.print("Introduce el código postal: ");
            String municipioId = teclado.nextLine();

            String sql = "{call nuevo_municipio(?, ?, ?)}";
            try (CallableStatement cstmt = con.prepareCall(sql)) {
                cstmt.setString(1, municipioId);
                cstmt.setString(2, nombre);
                cstmt.setInt(3, habitantes);
                cstmt.executeUpdate();
                System.out.println("Municipio añadido correctamente.");
            }
        } else {
            System.out.println("Esta opción no está disponible.");
        }
    }
}
