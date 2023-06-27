package com.example.meusgastos.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.meusgastos.domain.dto.UsuarioRequestDTO;
import com.example.meusgastos.domain.dto.UsuarioResponseDTO;
import com.example.meusgastos.domain.exception.BadRequestException;
import com.example.meusgastos.domain.exception.ResourceNotFoundException;
import com.example.meusgastos.domain.model.Usuario;
import com.example.meusgastos.domain.repository.UsuarioRepository;

@Service
public class UsuarioService implements ICRUDService<UsuarioRequestDTO, UsuarioResponseDTO>{

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public List<UsuarioResponseDTO> obterTodos() {
        // Cria uma lista de usuários:
        List<Usuario> usuarios = usuarioRepository.findAll();
        // Vai retornar uma lista dto
        return usuarios.stream()
        // O stream() funciona com o forEach em arrow function
        // O mapper vai mapear os itens com mesmo nome do usuário para o Usuario DTO
        .map(usuario -> mapper.map(usuario, UsuarioResponseDTO.class))
        // Isso litaralmente coletar e joga na lista
        .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDTO obterPorId(Long id) {
        // O optinal se não encontrar o id ele não retorna nada
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);
        if (optUsuario.isEmpty()) {
            throw new ResourceNotFoundException("Não foi possível encontrar o usuário com o id " + id);
        }
        return mapper.map(optUsuario.get(), UsuarioResponseDTO.class);
    }

    @Override
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        if (dto.getEmail() == null || dto.getSenha() == null){
            throw new BadRequestException("Email e Senha são obrigatórios!");
        }
        Optional<Usuario> optUsuario = usuarioRepository.findByEmail(dto.getEmail());
        if(optUsuario.isPresent()) {
            throw new BadRequestException("Já existe um usuário cadastrado com esse email" + dto.getEmail());
        }
        Usuario usuario = mapper.map(dto, Usuario.class);
        usuario.setDataCadastro(new Date());
        /*
         * A linha abaixo pode ser também escrita da seguinte forma (não necessita declarar
         * outra váriavel)
         * usuario = usuarioRepository.save(usuario);
            return mapper.map(usuario, UsuarioResponseDTO.class);
         */
        Usuario usuarioRetorno = usuarioRepository.save(usuario);
        return mapper.map(usuarioRetorno, UsuarioResponseDTO.class);
    }

    @Override
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        obterPorId(id);
        /*
         * Se quiser implementar verificação certa de email, pode se busca o email, que já vai estar cadastrado. Aì o certo é verificar se o email está em outro id, ou seja, é de outro usuário, se os ids forem diferentes não permitir atualização, se for igual permitir.
         */
        if (dto.getEmail() == null || dto.getSenha() == null){
            throw new BadRequestException("Email e Senha são obrigatórios!");
        }
        Usuario usuario = mapper.map(dto, Usuario.class);
        usuario.setId(id);
        usuario = usuarioRepository.save(usuario);
        return mapper.map(usuario, UsuarioResponseDTO.class);
    }

    @Override
    public void deletar(Long id) {
        /*
         * APAGAR DA BASE
         * obterPorId();
         * usuarioRepository.deleteById(id);
         */
        // INATIVAR
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);
        if(optUsuario.isEmpty()) {

        }
        Usuario usuario = optUsuario.get();
        usuario.setDataInativacao(new Date());
        usuarioRepository.save(usuario);
    }
    
}
