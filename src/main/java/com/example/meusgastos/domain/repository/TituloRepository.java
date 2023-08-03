package com.example.meusgastos.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.meusgastos.domain.model.Titulo;
import com.example.meusgastos.domain.model.Usuario;

import java.util.List;


public interface TituloRepository extends JpaRepository<Titulo, Long>{
    List<Titulo> findByUsuario(Usuario usuario);
}
