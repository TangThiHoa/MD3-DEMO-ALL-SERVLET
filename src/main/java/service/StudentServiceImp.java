package service;

import model.Class1;
import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentServiceImp implements StudentService {
    ClassService classService = new ClassServiceImpl();

    protected Connection getConnection() {
        Connection connection = null;
        try {
            java.lang.Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/managementstudent?useSSL=false", "root", "123456");
        } catch (SQLException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }


    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();


        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from student");) {
            System.out.println(preparedStatement); //in ra câu truy vấn.
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int age = rs.getInt("age");
                String name = rs.getString("name");
                int classId = rs.getInt("classId"); // lấy ra classId từ bảng student trong db
                Class1 clazz = (Class1) classService.findById(classId); // từ classId vừa lấy được, dùng ClassService để tìm đối tượng class tương ứng
                students.add(new Student(id, name, clazz, age)); //thêm đối tượng là danh sách
            }
        } catch (SQLException e) {
        }
        return students;
    }


    @Override
    public void add(Student student) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("insert into student(name, age, classId) values (?, ?,?)");) {
            preparedStatement.setString(1, student.getName());
            preparedStatement.setInt(2, student.getAge());
            preparedStatement.setInt(3, student.getClazz().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public Student findById(int id) {
        Student student = new Student();
            try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from student where id = ?");) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement); //in ra câu truy vấn.
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int age = rs.getInt("age");
                String name = rs.getString("name");
                int classId = rs.getInt("classId"); // lấy ra classId từ bảng student trong db
                Class1 clazz =classService.findById(classId);// từ classId vừa lấy được, dùng ClassService để tìm đối tượng class tương ứng
                student = new Student(id, name, clazz, age);
            }
        } catch (SQLException e) {
        }
        return student;
    }

    @Override
    public boolean update(Student student) throws SQLException {
        boolean a = false;
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("update student set name= ?,age=?,classId=? where id=?");) {
            preparedStatement.setString(1, student.getName());
            preparedStatement.setInt(2, student.getAge());
            preparedStatement.setInt(3, student.getClazz().getId());
            preparedStatement.setInt(4,student.getId());
            a = preparedStatement.executeUpdate() > 0;

        }
        return a;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("delete from student where id = ?");) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement); //in ra câu truy vấn.
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
        }
        return false;
    }

    @Override
    public List<Student> findAllByClass(int classId) {
        List<Student> students = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from student where classId = ?");) {
            preparedStatement.setInt(1, classId);
            System.out.println(preparedStatement); //in ra câu truy vấn.
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int age = rs.getInt("age");
                String name = rs.getString("name");
                int clazzId = rs.getInt("classId"); // lấy ra classId từ bảng student trong db
                Class1 clazz = classService.findById(clazzId); // từ classId vừa lấy được, dùng ClassService để tìm đối tượng class tương ứng
                students.add(new Student(id, name, clazz, age)); //thêm đối tượng là danh sách
            }
        } catch (SQLException e) {
        }
        return students;
    }

    @Override
    public List<Student> findAllByNameContains(String findName) {
        List<Student> students = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from student where name like ?");) {
            preparedStatement.setString(1, "%" + findName + "%");
            System.out.println(preparedStatement); //in ra câu truy vấn.
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int age = rs.getInt("age");
                String name = rs.getString("name");
                int clazzId = rs.getInt("classId"); // lấy ra classId từ bảng student trong db
                Class1 clazz = (Class1) classService.findById(clazzId); // từ classId vừa lấy được, dùng ClassService để tìm đối tượng class tương ứng
                students.add(new Student(id, name, clazz, age)); //thêm đối tượng là danh sách
            }
        } catch (SQLException e) {
        }
        return students;
    }
}
