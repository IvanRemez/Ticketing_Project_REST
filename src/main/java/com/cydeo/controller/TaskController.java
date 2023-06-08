package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getTasks() {

        return ResponseEntity
                .ok(new ResponseWrapper("Tasks retrieved",
                        taskService.listAllTasks(), HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper> getTaskById(@PathVariable("id") Long id) {

        return ResponseEntity
                .ok(new ResponseWrapper("Task retrieved",
                        taskService.findById(id), HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO task) {

        taskService.save(task);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseWrapper("Task created", HttpStatus.CREATED));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("id") Long id) {

        taskService.delete(id);

        return ResponseEntity
                .ok(new ResponseWrapper("Task deleted", HttpStatus.OK));
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody TaskDTO task) {

        taskService.update(task);

        return ResponseEntity
                .ok(new ResponseWrapper("Task updated", HttpStatus.OK));
    }

    @GetMapping("/employee/pending-tasks")
    public ResponseEntity<ResponseWrapper> employeePendingTasks() {

        return ResponseEntity
                .ok(new ResponseWrapper("Pending tasks retrieved",
                        taskService.listAllTasksByStatusIsNot(Status.COMPLETE),
                        HttpStatus.OK));
    }

    @PutMapping("/employee/update")
    public ResponseEntity<ResponseWrapper> employeeUpdateTasks(@RequestBody TaskDTO task) {

        taskService.update(task);

        return ResponseEntity
                .ok(new ResponseWrapper("Task updated", HttpStatus.OK));
    }

    @GetMapping("/employee/archive")
    public ResponseEntity<ResponseWrapper> employeeArchiveTasks() {

        return ResponseEntity
                .ok(new ResponseWrapper("Archived tasks retrieved",
                        taskService.listAllTasksByStatus(Status.COMPLETE),
                        HttpStatus.OK));
    }

}
