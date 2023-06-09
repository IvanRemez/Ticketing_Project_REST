package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/v1/task")
@Tag(name = "TaskController", description = "Task API")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Get tasks")
    public ResponseEntity<ResponseWrapper> getTasks() {

        return ResponseEntity
                .ok(new ResponseWrapper("All Tasks retrieved",
                        taskService.listAllTasks(), HttpStatus.OK));
    }

    @GetMapping("/{id}")
    @RolesAllowed("Manager")
    @Operation(summary = "Get task by ID")
    public ResponseEntity<ResponseWrapper> getTaskById(@PathVariable("id") Long id) {

        return ResponseEntity
                .ok(new ResponseWrapper("Task retrieved",
                        taskService.findById(id), HttpStatus.OK));
    }

    @PostMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Create task")
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO task) {

        taskService.save(task);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseWrapper("Task created", HttpStatus.CREATED));
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("Manager")
    @Operation(summary = "Delete task")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("id") Long id) {

        taskService.delete(id);

        return ResponseEntity
                .ok(new ResponseWrapper("Task deleted", HttpStatus.OK));
    }

    @PutMapping
    @RolesAllowed("Manager")
    @Operation(summary = "Update task")
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody TaskDTO task) {

        taskService.update(task);

        return ResponseEntity
                .ok(new ResponseWrapper("Task updated", HttpStatus.OK));
    }

    @GetMapping("/employee/pending-tasks")
    @RolesAllowed("Employee")
    @Operation(summary = "Employee - Pending tasks")
    public ResponseEntity<ResponseWrapper> employeePendingTasks() {

        return ResponseEntity
                .ok(new ResponseWrapper("Pending tasks retrieved",
                        taskService.listAllTasksByStatusIsNot(Status.COMPLETE),
                        HttpStatus.OK));
    }

    @PutMapping("/employee/update")
    @RolesAllowed("Employee")
    @Operation(summary = "Employee - Update task")
    public ResponseEntity<ResponseWrapper> employeeUpdateTasks(@RequestBody TaskDTO task) {

        taskService.update(task);

        return ResponseEntity
                .ok(new ResponseWrapper("Task updated", HttpStatus.OK));
    }

    @GetMapping("/employee/archive")
    @RolesAllowed("Employee")
    @Operation(summary = "Employee - Archived tasks")
    public ResponseEntity<ResponseWrapper> employeeArchiveTasks() {

        return ResponseEntity
                .ok(new ResponseWrapper("Archived tasks retrieved",
                        taskService.listAllTasksByStatus(Status.COMPLETE),
                        HttpStatus.OK));
    }

}
