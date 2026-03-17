package com.pao.laboratory03.exercise.model;

import com.pao.laboratory03.exercise.exception.InvalidStudentException;
import com.pao.laboratory03.exercise.exception.InvalidGradeException;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private final String name;
    private final int age;
    private final Map<Subject, Double> grades;

    public Student(String name, int age){
        if(age < 18 || age > 60)
            throw new InvalidStudentException("Varsta " + age + " nu esta valida(18-60)");
        this.name = name;
        this.age = age;

        this.grades = new HashMap<>();
    }
    public String getName(){
        return this.name;
    }
    public int getAge(){
        return this.age;
    }
    public Map<Subject, Double> getGrades(){
        return this.grades;
    }

    public void addGrade(Subject subject, double grade){
        if(grade < 1 || grade > 10)
            throw new InvalidGradeException("Nota " + grade + " este invalida (1-10)");

        grades.put(subject, grade);
    }

    public double getAverage(){
        if(this.grades.isEmpty()){
            return 0;
        }
        else{
            double sum = 0;
            for(double g: grades.values()){
                sum = sum + g;
            }
            return sum / grades.size();
        }
    }

    @Override
    public String toString(){
        return String.format("Student{name='%s', age=%d, avg=%.2f}", name, age, getAverage());
    }
}
