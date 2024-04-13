package com.zbohdan.accountmanager.api;

import com.zbohdan.accountmanager.models.User;
import com.zbohdan.accountmanager.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserAPI {
    private UserRepo repo;

    @Autowired
    public UserAPI(UserRepo userRepo) {
        this.repo = userRepo;
    }

    @GetMapping("")
    public List<User> all() {
        return repo.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findById(@PathVariable("id") Integer id) {
        Optional<User> user = repo.findById(id);

        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> create(@RequestBody User user) {
        try {
            repo.save(user);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(String.format("User with %d has been successfully created.", user.getId()));
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Something went wrong, please try again later.");
        }
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<String> update(@PathVariable("id") Integer id, @RequestBody User user) {
        Optional<User> updateUser = repo.findById(id);

        if (updateUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        repo.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(String.format("User with %d has been updated.", user.getId()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id) {
        Optional<User> user = repo.findById(id);

        if (user.isPresent()){
            User deleteUser = user.get();

            repo.delete(deleteUser);

            return ResponseEntity.status(HttpStatus.OK).body(String.format("User $d deleted.", id));
        }

        return ResponseEntity.notFound().build();
    }

}
