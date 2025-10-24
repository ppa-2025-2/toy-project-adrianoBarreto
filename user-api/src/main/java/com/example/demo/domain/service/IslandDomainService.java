package com.example.demo.domain.service;

import org.springframework.stereotype.Service;

import com.example.demo.domain.NoAvailableWorkstationException;
import com.example.demo.repository.entity.Island;
import com.example.demo.repository.entity.User;
import com.example.demo.repository.entity.Workstation;

@Service
public class IslandDomainService {

    public Workstation alocar(Island island, User user) {

        for (Workstation ws : island.getWorkstations()) {
            if (ws.getUser() == null) { 
                ws.setUser(user);
                return ws;
            }
        }

        throw new NoAvailableWorkstationException("Nenhuma workstation disponível na ilha " + island.getName());
    }
}