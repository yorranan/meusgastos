package com.example.meusgastos.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.meusgastos.domain.model.CentroDeCusto;
import com.example.meusgastos.domain.model.Usuario;

public interface CentroDeCustoRepository extends JpaRepository<CentroDeCusto, Long>{
 // não preciso implementar nada de diferente nesse caso.
 List<CentroDeCusto> findByUsuario(Usuario usuario);
} 
