package com.example.demo.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.application.IslandApplicationService;
import com.example.demo.controller.dto.AllocateUserDTO;
import com.example.demo.controller.dto.SetupIslandDTO;
import com.example.demo.repository.IslandRepository;
import com.example.demo.repository.entity.Island;
import com.example.demo.repository.entity.Workstation;

@RestController
@RequestMapping("/api/v1/islands")
public class IslandController {

    private final IslandApplicationService islandService;
    private final IslandRepository islandRepository;

    public IslandController(
        IslandApplicationService islandService, 
        IslandRepository islandRepository
    ) {
        this.islandService = islandService;
        this.islandRepository = islandRepository;
    }
    
    @PostMapping("/{islandId}/allocate")
    public ResponseEntity<Workstation> allocateUser(
        @PathVariable Integer islandId, 
        @RequestBody AllocateUserDTO dto
    ) {
        Workstation workstation = islandService.alocarWorkstationDisponivel(islandId, dto);
        return ResponseEntity.ok(workstation);
    }

    @GetMapping
    public ResponseEntity<List<Island>> getAllIslands() {
        return ResponseEntity.ok(islandRepository.findAll());
    }

    @PostMapping("/setup-test")
    public ResponseEntity<Island> setupTestIsland(@RequestBody SetupIslandDTO dto) {
        Island island = islandService.setupTestIsland(dto.islandName(), dto.workstationCount());
        return ResponseEntity.ok(island);
    }
}