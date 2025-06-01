package com.hopematch.hopematch_backend.controllers;

import com.hopematch.hopematch_backend.models.Encargado;
import com.hopematch.hopematch_backend.models.Padrino;
import com.hopematch.hopematch_backend.services.PadrinoService;
import com.hopematch.hopematch_backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/padrino")
@CrossOrigin(origins = {"http://localhost:4200", "https://adrianrojas4414.github.io", "https://hopematch.publicvm.com", "https://hopematch.netlify.app"})
public class PadrinoController {

    @Autowired
    private PadrinoService padrinoService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/add")
    public Padrino postPadrino(@RequestBody Padrino padrino){
        return padrinoService.savePadrino(padrino);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Padrino loginPadrino) {
        Optional<Padrino> padrino = padrinoService.findByEmail(loginPadrino.getEmail());

        if (padrino.isPresent() && padrinoService.verifyPassword(loginPadrino.getContrasenia(), padrino.get().getContrasenia())) {
            String estado = padrino.get().getEstado();
            String token = jwtUtil.generateToken(padrino.get().getEmail(), "padrino", padrino.get().getId());

            if (estado.equals("En revision")) {
                return ResponseEntity.ok("{\"message\": \"Su cuenta está en revisión, por favor contáctese con Soporte Tecnico\", \"token\": \"" + token + "\"}");
            }
            else if (estado.equals("Suspendido")) {
                return ResponseEntity.ok("{\"message\": \"Su cuenta ha sido suspendida, por favor contáctese con Soporte Tecnico\", \"token\": \"" + token + "\"}");
            }
            return ResponseEntity.ok("{\"token\": \"" + token + "\"}");
        } else {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        }
    }

    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello World! Esto es HOPEMATCH");
    }

    @GetMapping("/list")
    public List<Padrino> getAllPadrinos(){
        return padrinoService.getAllPadrinos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Padrino> getPadrinoById(@PathVariable int id){
        Optional<Padrino> padrino = padrinoService.getPadrinoById(id);
        return new ResponseEntity<>(padrino.get(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public Padrino updatePadrino(@PathVariable int id, @RequestBody Padrino padrinoDetails){
        return padrinoService.updatePadrino(id, padrinoDetails);
    }
}
