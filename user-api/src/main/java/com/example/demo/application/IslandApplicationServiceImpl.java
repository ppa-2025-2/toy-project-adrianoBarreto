package com.example.demo.application;

import org.springframework.stereotype.Service;

import com.example.demo.controller.dto.AllocateUserDTO;
import com.example.demo.domain.NoAvailableWorkstationException;
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

    public IslandApplicationServiceImpl(
            IslandRepository islandRepository, 
            UserRepository userRepository,
            WorkstationRepository workstationRepository
    ) {
        this.islandRepository = islandRepository;
        this.userRepository = userRepository;
        this.workstationRepository = workstationRepository;
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

        Workstation allocatedWorkstation = island.alocar(user);

        islandRepository.save(island);
        
        return allocatedWorkstation;
    }

    @Transactional
    @Override
    public Island setupTestIsland(String islandName, int workstationCount) {
        Island island = new Island();
        island.setName(islandName);
        for (int i = 1; i <= workstationCount; i++) {
            Workstation ws = new Workstation();
            ws.setName("WS-" + String.format("%02d", i));
            island.addWorkstation(ws); // Usando um método de entidade
        }
        return islandRepository.save(island);
    }
}