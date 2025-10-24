package com.example.demo.application;

import org.springframework.stereotype.Service;

import com.example.demo.controller.dto.AllocateUserDTO;
import com.example.demo.domain.NoAvailableWorkstationException;
import com.example.demo.domain.service.IslandDomainService;
import com.example.demo.repository.IslandRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkstationRepository;
import com.example.demo.repository.entity.Island;
import com.example.demo.repository.entity.User;
import com.example.demo.repository.entity.Workstation;

import jakarta.transaction.Transactional;

@Service
public class IslandApplicationServiceImpl implements IslandApplicationService {

    private final IslandRepository islandRepository;
    private final UserRepository userRepository;
    private final WorkstationRepository workstationRepository;
    private final IslandDomainService islandDomainService;

    public IslandApplicationServiceImpl(
            IslandRepository islandRepository, 
            UserRepository userRepository,
            WorkstationRepository workstationRepository,
            IslandDomainService islandDomainService 
    ) {
        this.islandRepository = islandRepository;
        this.userRepository = userRepository;
        this.workstationRepository = workstationRepository;
        this.islandDomainService = islandDomainService;
    }

    @Transactional
    @Override
    public Workstation alocarWorkstationDisponivel(Integer islandId, AllocateUserDTO dto) {

        User user = userRepository.findByEmail(dto.userEmail())
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + dto.userEmail()));
        
        Island island = islandRepository.findById(islandId)
            .orElseThrow(() -> new IllegalArgumentException("Ilha não encontrada: " + islandId));

        workstationRepository.findByUserId(user.getId())
            .ifPresent(ws -> {
                throw new NoAvailableWorkstationException("Usuário já está alocado na workstation " + ws.getName());
            });

        Workstation allocatedWorkstation = islandDomainService.alocar(island, user);

        return workstationRepository.save(allocatedWorkstation);
    }

    @Transactional
    @Override
    public Island setupTestIsland(String islandName, int workstationCount) {     
        Island island = new Island();
        island.setName(islandName);
        for (int i = 1; i <= workstationCount; i++) {
            Workstation ws = new Workstation();
            ws.setName("WS-" + String.format("%02d", i));
            ws.setIsland(island);
            island.getWorkstations().add(ws);
        }
        return islandRepository.save(island);
    }
}