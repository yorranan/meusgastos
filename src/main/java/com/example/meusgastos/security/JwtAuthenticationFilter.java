package com.example.meusgastos.security;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.meusgastos.common.ConversorData;
import com.example.meusgastos.domain.dto.usuario.LoginRequestDTO;
import com.example.meusgastos.domain.dto.usuario.LoginResponseDTO;
import com.example.meusgastos.domain.dto.usuario.UsuarioResponseDTO;
import com.example.meusgastos.domain.model.ErroResposta;
import com.example.meusgastos.domain.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    // @Autowired
    private JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil){
        super();
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/auth");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        try{
            LoginRequestDTO login = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDTO.class);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(login.getEmail(), login.getSenha());
            Authentication auth = authenticationManager.authenticate(authToken);
            return auth;
        }catch(BadCredentialsException e){
            throw new BadCredentialsException("Usuário ou senha Inválidos");
        }catch(Exception e){
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException{
        Usuario usuario = (Usuario) authResult.getPrincipal();
        String token = jwtUtil.gerarToken(authResult);
        UsuarioResponseDTO usuarioResponse = new UsuarioResponseDTO();
        usuarioResponse.setId(usuario.getId());
        usuarioResponse.setNome(usuario.getNome());
        usuarioResponse.setFoto(usuario.getFoto());
        usuarioResponse.setDataCadastro(usuario.getDataCadastro());
        usuarioResponse.setDataInativacao(usuario.getDataInativacao());
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setToken("Bearer " + token);
        loginResponseDTO.setUsuario(usuarioResponse);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(loginResponseDTO));
    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, 
    AuthenticationException failed) throws IOException, ServletException{
        String dataHora = ConversorData.converterDateParaDataHora(new Date());
        ErroResposta erro =  new ErroResposta(dataHora, 401, "Unauthorized", failed.getMessage());
        response.setStatus(401);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(erro));
    }


}
