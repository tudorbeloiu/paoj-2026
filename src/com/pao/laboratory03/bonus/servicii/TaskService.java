package com.pao.laboratory03.bonus.servicii;

import com.pao.laboratory03.bonus.exceptii.TaskNotFoundException;
import com.pao.laboratory03.bonus.model.Task;
import com.pao.laboratory03.bonus.enumeratii.*;
import com.pao.laboratory03.bonus.exceptii.*;

import java.util.*;

public class TaskService {
    private final Map<String, Task> tasksById = new LinkedHashMap<>();
    private final Map<Priority, List<Task>> tasksByPriority = new HashMap<>();
    private final List<String> auditLog = new ArrayList<>();

    private int taskCounter = 0;

    private static TaskService instance;

    private TaskService() {}

    public static TaskService getInstance(){
        if(instance == null)
            instance = new TaskService();
        return instance;
    }

    private String generateId(){
        taskCounter++;
        return String.format("T%03d", taskCounter);
    }

    public Task addTask(String title, Priority priority){
        String id = generateId();
        Task task = new Task(id, title, priority);

        tasksById.put(id, task);
        if(!tasksByPriority.containsKey(priority)){
            tasksByPriority.put(priority, new ArrayList<>());
        }
        tasksByPriority.get(priority).add(task);

        auditLog.add("[ADD] " + id + ": '" + title + "' (" + priority + ")");
        return task;
    }

    public Task findById(String taskId){
        Task task = tasksById.get(taskId);
        if(task == null)
            throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost găsit");
        return task;
    }

    public void assignTask(String taskId, String assignee){
        Task task = findById(taskId);
        task.setAssignee(assignee);
        auditLog.add("[ASSIGN] " + taskId + " → " + assignee);
    }

    public void changeStatus(String taskId, Status newStatus){
        Task task = findById(taskId);
        Status current = task.getStatus();
        if(!current.canTransitionTo(newStatus))
            throw new InvalidTransitionException(current, newStatus);

        task.setStatus(newStatus);
        auditLog.add("[STATUS] " + taskId + ": " + current + " → " + newStatus);
    }

    public List<Task> getTasksByPriority(Priority priority){
        return tasksByPriority.getOrDefault(priority, new ArrayList<>());
    }

    public Map<Status, Long> getStatusSummary(){
        Map<Status, Long> summary = new LinkedHashMap<>();
        for(Status s: Status.values()){
            summary.put(s, 0L);
        }
        for(Task t: tasksById.values()){
            summary.put(t.getStatus(), summary.get(t.getStatus()) + 1);
        }
        return summary;
    }

    public List<Task> getUnassignedTasks(){
        List<Task> unsgn = new ArrayList<>();
        for(Task t: tasksById.values()){
            if(t.getAssignee() == null){
                unsgn.add(t);
            }
        }
        return unsgn;
    }

    public void printAuditLog(){
        for(String audit: auditLog){
            System.out.println(audit);
        }
    }
    public double getTotalUrgencyScore(int baseDays){
        double s = 0;
        for(Task t: tasksById.values()){
            if(t.getStatus() != Status.DONE && t.getStatus() != Status.CANCELLED){
                s = s + t.getPriority().calculateScore(baseDays);
            }
        }
        return s;
    }
}
