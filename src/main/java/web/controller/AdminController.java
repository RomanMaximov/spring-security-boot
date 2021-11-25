package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userservice;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userservice, RoleService roleService) {
        this.userservice = userservice;
        this.roleService = roleService;
    }

    @GetMapping(value = "/page")
    public String ShowAdminPage(ModelMap model) {
        model.addAttribute("users", userservice.getAllUsers());
        return "allusers";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id){
        userservice.delete(id);
        return "redirect:/admin/page";
    }

    @GetMapping("/{id}/edit")
    public String edit(ModelMap model, @PathVariable("id") int id) {
        model.addAttribute("user", userservice.getUserById(id));
        return "edit";
    }

    @PatchMapping("/{id}")
    public String editUser(@ModelAttribute("user") User user) {
        userservice.update(user);
        return "redirect:/admin/page";
    }

    @GetMapping("/adduser")
    public String addUser(@ModelAttribute("user") User user, ModelMap model){
        user.setRoles(roleService.getAllRoles());
        model.addAttribute("user", user);
        return "new";
    }

    @PostMapping("/createuser")
    public String createNewUser(@ModelAttribute("user") User user, ModelMap model) {
        userservice.addUser(user, user.getRoles());
        model.addAttribute("users", userservice.getAllUsers());
        return "redirect:/admin/page";
    }
}
