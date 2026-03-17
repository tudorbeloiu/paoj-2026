package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.exception.StudentNotFoundException;
import com.pao.laboratory03.exercise.model.Subject;

import java.util.*;

public class StudentService {

    private final List<Student> students = new ArrayList<>();
    private static StudentService instance;

    private StudentService(){}

    public static StudentService getInstance(){
        if(instance == null)
            instance = new StudentService();
        return instance;
    }

    public void addStudent(String name, int age){
        for(Student student: students){
            if(student.getName().equals(name))
                throw new RuntimeException("Studentul '" + name + "' exista deja");
        }
        students.add(new Student(name, age));

    }

    public Student findByName(String name){
        for(Student student: students){
            if(student.getName().equals(name))
                return student;
        }

        throw new StudentNotFoundException("Studentul '" + name + "' nu a fost gasit");

    }
    public void addGrade(String studentName, Subject subject, double grade){
        Student student = findByName(studentName);
        student.addGrade(subject, grade);
    }

    public void printAllStudents(){
        if(students.isEmpty()){
            System.out.println("Nu exista studenti");
            return;
        }
        for(Student s: students){
            System.out.println(s);

            for(Map.Entry<Subject, Double> e : s.getGrades().entrySet()){
                System.out.println("  " + e.getKey().name() + " -> " + e.getValue());
            }
        }
    }

    public void printTopStudents() {
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort((a, b) -> Double.compare(b.getAverage(), a.getAverage()));
        for (int i = 0; i < sorted.size(); i++)
            System.out.println((i + 1) + ". " + sorted.get(i));
    }

    public Map<Subject, Double> getAveragePerSubject(){

        Map<Subject, Double> sume = new HashMap<>();
        Map<Subject, Integer> cnt = new HashMap<>();

        for(Student student: students){
            for(Map.Entry<Subject, Double> e: student.getGrades().entrySet()){
                sume.put(e.getKey(), sume.getOrDefault(e.getKey(), 0.0) + e.getValue());
                cnt.put(e.getKey(), cnt.getOrDefault(e.getKey(), 0) + 1);
            }
        }
        Map<Subject, Double> medii = new HashMap<>();
        for(Subject sub: sume.keySet()){
            medii.put(sub, sume.get(sub) / cnt.get(sub));
        }
        return medii;
    }

}
