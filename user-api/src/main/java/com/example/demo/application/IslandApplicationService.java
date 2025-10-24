package com.example.demo.application;

import com.example.demo.controller.dto.AllocateUserDTO;
import com.example.demo.repository.entity.Island;
import com.example.demo.repository.entity.Workstation;

public interface IslandApplicationService {
    
    Workstation alocarWorkstationDisponivel(Integer islandId, AllocateUserDTO dto);

    Island setupTestIsland(String islandName, int workstationCount);
}