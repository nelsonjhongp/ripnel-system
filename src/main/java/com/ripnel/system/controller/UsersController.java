package com.ripnel.system.controller;

import com.ripnel.system.model.Role;
import com.ripnel.system.model.User;
import com.ripnel.system.repository.RoleRepository;
import com.ripnel.system.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin/usuarios")
public class UsersController {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    public UsersController(UserRepository userRepo, RoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    // LISTA
    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "admin/users-list";
    }

    // CREAR: FORM
    @GetMapping("/nuevo")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepo.findAll());
        model.addAttribute("mode", "create");
        return "admin/users-form";
    }

    // CREAR: POST
    @PostMapping
    public String create(@ModelAttribute @Valid User user,
                         @RequestParam(required = false, name = "roles") List<Long> roleIds) {

        if (user.getActive() == null) user.setActive(true);

        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            for (Long id : roleIds) roleRepo.findById(id).ifPresent(roles::add);
        }
        user.setRoles(roles);

        userRepo.save(user);
        return "redirect:/admin/usuarios";
    }

    // EDITAR: FORM
    @GetMapping("/{id}/editar")
    public String editForm(@PathVariable Long id, Model model) {
        User u = userRepo.findById(id).orElseThrow();
        model.addAttribute("user", u);
        model.addAttribute("roles", roleRepo.findAll());
        model.addAttribute("mode", "edit");
        return "admin/users-form";
    }

    // EDITAR: POST (actualizar)
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute @Valid User formUser,
                         @RequestParam(required = false, name = "roles") List<Long> roleIds) {

        User db = userRepo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Campos básicos
        db.setEmail(formUser.getEmail());
        db.setName(formUser.getName());
        db.setActive(formUser.getActive() != null ? formUser.getActive() : Boolean.TRUE);

        // Password: si vino vacío en el form, NO cambiar
        String newPass = formUser.getPasswordHash();
        if (newPass != null && !newPass.isBlank()) {
            // TODO: cuando tengas BCrypt: db.setPasswordHash(encoder.encode(newPass));
            db.setPasswordHash(newPass);
        }

        // Roles
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            for (Long rid : roleIds) roleRepo.findById(rid).ifPresent(roles::add);
        }
        db.setRoles(roles);

        userRepo.save(db);
        return "redirect:/admin/usuarios";
    }

    // ELIMINAR (hard delete)
    @PostMapping("/{id}/eliminar")
    public String delete(@PathVariable Long id) {
        // dentro de delete(...)
        Long currentUserId = null; // ej: (Long) req.getSession().getAttribute("userId");
        if (Objects.equals(currentUserId, id)) {
            // opcional: evitar auto-borrado
            return "redirect:/admin/usuarios?selfDeleteError=1";
        }

        Optional<User> opt = userRepo.findById(id);
        if (opt.isPresent()) {
            User u = opt.get();
            // Limpiar join table por si acaso (suele borrarse solo, pero seremos explícitos)
            u.setRoles(Collections.emptySet());
            userRepo.delete(u);
        }
        return "redirect:/admin/usuarios";
    }

    // OPCIONAL: desactivar
    @PostMapping("/{id}/desactivar")
    public String deactivate(@PathVariable Long id) {
        userRepo.findById(id).ifPresent(u -> {
            u.setActive(false);
            userRepo.save(u);
        });
        return "redirect:/admin/usuarios";
    }

    // OPCIONAL: activar
    @PostMapping("/{id}/activar")
    public String activate(@PathVariable Long id) {
        userRepo.findById(id).ifPresent(u -> {
            u.setActive(true);
            userRepo.save(u);
        });
        return "redirect:/admin/usuarios";
    }
}
