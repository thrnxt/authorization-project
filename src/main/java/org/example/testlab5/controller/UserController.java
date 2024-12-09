package org.example.testlab5.controller;

import org.example.testlab5.config.MyUserDetails;
import org.example.testlab5.model.Task;
import org.example.testlab5.model.User;
import org.example.testlab5.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    private final TaskRepository taskRepository;

    @Autowired
    public UserController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/main")
    public String mainPage(@AuthenticationPrincipal MyUserDetails myUserDetails, Model model) {
        if (myUserDetails != null) {
            User user = myUserDetails.getUser();
            model.addAttribute("user", myUserDetails);

            List<Task> tasks = taskRepository.findAllByUser(myUserDetails.getUser());
            model.addAttribute("tasks", tasks);
        } else {
            throw new IllegalArgumentException("not found user");
        }
        return "main";
    }

    @GetMapping("/add-task")
    public String showAddTaskForm(Model model, @AuthenticationPrincipal MyUserDetails myUserDetails){

        User user = myUserDetails.getUser();
        Task task = new Task();
        task.setUser(user);
        model.addAttribute("task", task);
        model.addAttribute("user", user);
        return "add-task";
    }
    @PostMapping("/add-task")
    public String addTask(@ModelAttribute Task task, @AuthenticationPrincipal MyUserDetails myUserDetails){
        task.setUser(myUserDetails.getUser());
        taskRepository.save(task);
        return "redirect:/main";
    }



    @GetMapping("/edit-task/{id}")
    public String showEditTaskForm(@PathVariable Long id, Model model) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + id));
        model.addAttribute("task", task);
        return "edit-task";
    }
    @PostMapping("/edit-task/{id}")
    public String editTask(@PathVariable Long id, @ModelAttribute Task updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + id));
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setDueDate(updatedTask.getDueDate());
        taskRepository.save(task);
        return "redirect:/main";
    }
    @PostMapping("/delete-task/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/main";
    }





}
